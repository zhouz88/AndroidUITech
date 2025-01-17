/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myapplication.sourcecode.appsearch;

import static android.app.appsearch.AppSearchResult.RESULT_INVALID_SCHEMA;
import static android.os.ParcelFileDescriptor.MODE_READ_ONLY;
import static android.os.ParcelFileDescriptor.MODE_WRITE_ONLY;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.annotation.WorkerThread;
import android.app.appsearch.AppSearchResult;
import android.app.appsearch.AppSearchSchema;
import android.app.appsearch.AppSearchSession;
import android.app.appsearch.GenericDocument;
import android.app.appsearch.Migrator;
import android.app.appsearch.SearchSpec;
import android.app.appsearch.SetSchemaResponse;
import android.app.appsearch.aidl.AppSearchResultParcel;
import android.app.appsearch.aidl.IAppSearchManager;
import android.app.appsearch.aidl.IAppSearchResultCallback;
import android.app.appsearch.exceptions.AppSearchException;
import android.app.appsearch.stats.SchemaMigrationStats;
import android.content.AttributionSource;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.ArraySet;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The helper class for {@link android.app.appsearch.AppSearchSchema} migration.
 *
 * <p>It will query and migrate {@link android.app.appsearch.GenericDocument} in given type to a new version.
 * @hide
 */
public class AppSearchMigrationHelper implements Closeable {
    private final IAppSearchManager mService;
    private final AttributionSource mCallerAttributionSource;
    private final String mDatabaseName;
    private final UserHandle mUserHandle;
    private final File mMigratedFile;
    private final Set<String> mDestinationTypes;
    private int mTotalNeedMigratedDocumentCount = 0;

    AppSearchMigrationHelper(@NonNull IAppSearchManager service,
            @NonNull UserHandle userHandle,
            @NonNull AttributionSource callerAttributionSource,
            @NonNull String databaseName,
            @NonNull Set<android.app.appsearch.AppSearchSchema> newSchemas) throws IOException {
        mService = Objects.requireNonNull(service);
        mUserHandle = Objects.requireNonNull(userHandle);
        mCallerAttributionSource = Objects.requireNonNull(callerAttributionSource);
        mDatabaseName = Objects.requireNonNull(databaseName);
        mMigratedFile = File.createTempFile(/*prefix=*/"appsearch", /*suffix=*/null);
        mDestinationTypes = new ArraySet<>(newSchemas.size());
        for (AppSearchSchema newSchema : newSchemas) {
            mDestinationTypes.add(newSchema.getSchemaType());
        }
    }

    /**
     * Queries all documents that need to be migrated to a different version and transform
     * documents to that version by passing them to the provided {@link android.app.appsearch.Migrator}.
     *
     * <p>The method will be executed on the executor provided to
     * {@link android.app.appsearch.AppSearchSession#setSchema}.
     *
     * @param schemaType The schema type that needs to be updated and whose {@link android.app.appsearch.GenericDocument}
     *                   need to be migrated.
     * @param migrator The {@link android.app.appsearch.Migrator} that will upgrade or downgrade a {@link
     *     android.app.appsearch.GenericDocument} to new version.
     * @param schemaMigrationStatsBuilder    The {@link SchemaMigrationStats.Builder} contains
     *                                       schema migration stats information
     */
    @WorkerThread
    void queryAndTransform(@NonNull String schemaType, @NonNull android.app.appsearch.Migrator migrator,
            int currentVersion, int finalVersion,
            @Nullable SchemaMigrationStats.Builder schemaMigrationStatsBuilder)
            throws IOException, AppSearchException, InterruptedException, ExecutionException {
        File queryFile = File.createTempFile(/*prefix=*/"appsearch", /*suffix=*/null);
        try (ParcelFileDescriptor fileDescriptor =
                     ParcelFileDescriptor.open(queryFile, MODE_WRITE_ONLY)) {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<android.app.appsearch.AppSearchResult<Void>> resultReference = new AtomicReference<>();
            mService.writeQueryResultsToFile(mCallerAttributionSource, mDatabaseName,
                    fileDescriptor,
                    /*queryExpression=*/ "",
                    new android.app.appsearch.SearchSpec.Builder()
                            .addFilterSchemas(schemaType)
                            .setTermMatch(SearchSpec.TERM_MATCH_EXACT_ONLY)
                            .build().getBundle(),
                    mUserHandle,
                    /*binderCallStartTimeMillis=*/ SystemClock.elapsedRealtime(),
                    new IAppSearchResultCallback.Stub() {
                        @Override
                        public void onResult(AppSearchResultParcel resultParcel) {
                            resultReference.set(resultParcel.getResult());
                            latch.countDown();
                        }
                    });
            latch.await();
            android.app.appsearch.AppSearchResult<Void> result = resultReference.get();
            if (!result.isSuccess()) {
                throw new AppSearchException(result.getResultCode(), result.getErrorMessage());
            }
            readAndTransform(queryFile, migrator, currentVersion, finalVersion,
                    schemaMigrationStatsBuilder);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        } finally {
            queryFile.delete();
        }
    }

    /**
     * Puts all {@link android.app.appsearch.GenericDocument} migrated from the previous call to
     * {@link #queryAndTransform} into AppSearch.
     *
     * <p> This method should be only called once.
     *
     * @param responseBuilder a SetSchemaResponse builder whose result will be returned by this
     *                        function with any
     *                        {@link android.app.appsearch.SetSchemaResponse.MigrationFailure}
     *                        added in.
     * @param schemaMigrationStatsBuilder    The {@link SchemaMigrationStats.Builder} contains
     *                                       schema migration stats information
     * @param totalLatencyStartTimeMillis start timestamp to calculate total migration latency in
     *     Millis
     * @return the {@link android.app.appsearch.SetSchemaResponse} for {@link AppSearchSession#setSchema} call.
     */
    @NonNull
    android.app.appsearch.AppSearchResult<android.app.appsearch.SetSchemaResponse> putMigratedDocuments(
            @NonNull android.app.appsearch.SetSchemaResponse.Builder responseBuilder,
            @NonNull SchemaMigrationStats.Builder schemaMigrationStatsBuilder,
            long totalLatencyStartTimeMillis) {
        if (mTotalNeedMigratedDocumentCount == 0) {
            return android.app.appsearch.AppSearchResult.newSuccessfulResult(responseBuilder.build());
        }
        try (ParcelFileDescriptor fileDescriptor =
                     ParcelFileDescriptor.open(mMigratedFile, MODE_READ_ONLY)) {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<android.app.appsearch.AppSearchResult<List<Bundle>>> resultReference =
                    new AtomicReference<>();
            mService.putDocumentsFromFile(mCallerAttributionSource, mDatabaseName, fileDescriptor,
                    mUserHandle,
                    schemaMigrationStatsBuilder.build().getBundle(),
                    totalLatencyStartTimeMillis,
                    /*binderCallStartTimeMillis=*/ SystemClock.elapsedRealtime(),
                    new IAppSearchResultCallback.Stub() {
                        @Override
                        public void onResult(AppSearchResultParcel resultParcel) {
                            resultReference.set(resultParcel.getResult());
                            latch.countDown();
                        }
                    });
            latch.await();
            android.app.appsearch.AppSearchResult<List<Bundle>> result = resultReference.get();
            if (!result.isSuccess()) {
                return android.app.appsearch.AppSearchResult.newFailedResult(result);
            }
            List<Bundle> migratedFailureBundles = Objects.requireNonNull(result.getResultValue());
            for (int i = 0; i < migratedFailureBundles.size(); i++) {
                responseBuilder.addMigrationFailure(
                        new SetSchemaResponse.MigrationFailure(migratedFailureBundles.get(i)));
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        } catch (Throwable t) {
            return android.app.appsearch.AppSearchResult.throwableToFailedResult(t);
        } finally {
            mMigratedFile.delete();
        }
        return AppSearchResult.newSuccessfulResult(responseBuilder.build());
    }

    /**
     * Reads all saved {@link android.app.appsearch.GenericDocument}s from the given {@link File}.
     *
     * <p>Transforms those {@link android.app.appsearch.GenericDocument}s to the final version.
     *
     * <p>Save migrated {@link android.app.appsearch.GenericDocument}s to the {@link #mMigratedFile}.
     */
    private void readAndTransform(@NonNull File file, @NonNull Migrator migrator,
            int currentVersion, int finalVersion,
            @Nullable SchemaMigrationStats.Builder schemaMigrationStatsBuilder)
            throws IOException, AppSearchException {
        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(file));
             DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(
                     mMigratedFile, /*append=*/ true))) {
            android.app.appsearch.GenericDocument document;
            while (true) {
                try {
                    document = readDocumentFromInputStream(inputStream);
                } catch (EOFException e) {
                    break;
                    // Nothing wrong. We just finished reading.
                }

                android.app.appsearch.GenericDocument newDocument;
                if (currentVersion < finalVersion) {
                    newDocument = migrator.onUpgrade(currentVersion, finalVersion, document);
                } else {
                    // currentVersion == finalVersion case won't trigger migration and get here.
                    newDocument = migrator.onDowngrade(currentVersion, finalVersion, document);
                }
                ++mTotalNeedMigratedDocumentCount;

                if (!mDestinationTypes.contains(newDocument.getSchemaType())) {
                    // we exit before the new schema has been set to AppSearch. So no
                    // observable changes will be applied to stored schemas and documents.
                    // And the temp file will be deleted at close(), which will be triggered at
                    // the end of try-with-resources block of SearchSessionImpl.
                    throw new AppSearchException(
                            RESULT_INVALID_SCHEMA,
                            "Receive a migrated document with schema type: "
                                    + newDocument.getSchemaType()
                                    + ". But the schema types doesn't exist in the request");
                }
                writeBundleToOutputStream(outputStream, newDocument.getBundle());
            }
        }
        if (schemaMigrationStatsBuilder != null) {
            schemaMigrationStatsBuilder.setTotalNeedMigratedDocumentCount(
                    mTotalNeedMigratedDocumentCount);
        }
    }

    /**
     * Reads the {@link Bundle} of a {@link android.app.appsearch.GenericDocument} from given {@link DataInputStream}.
     *
     * @param inputStream The inputStream to read from
     *
     * @throws IOException        on read failure.
     * @throws EOFException       if {@link java.io.InputStream} reaches the end.
     */
    @NonNull
    public static android.app.appsearch.GenericDocument readDocumentFromInputStream(
            @NonNull DataInputStream inputStream) throws IOException {
        int length = inputStream.readInt();
        if (length == 0) {
            throw new EOFException();
        }
        byte[] serializedMessage = new byte[length];
        inputStream.read(serializedMessage);

        Parcel parcel = Parcel.obtain();
        try {
            parcel.unmarshall(serializedMessage, 0, serializedMessage.length);
            parcel.setDataPosition(0);
            Bundle bundle = parcel.readBundle();
            return new GenericDocument(bundle);
        } finally {
            parcel.recycle();
        }
    }

    /**
     * Serializes a {@link Bundle} and writes into the given {@link DataOutputStream}.
     */
    public static void writeBundleToOutputStream(
            @NonNull DataOutputStream outputStream, @NonNull Bundle bundle)
            throws IOException {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeBundle(bundle);
            byte[] serializedMessage = parcel.marshall();
            outputStream.writeInt(serializedMessage.length);
            outputStream.write(serializedMessage);
        } finally {
            parcel.recycle();
        }
    }

    @Override
    public void close() throws IOException {
        mMigratedFile.delete();
    }
}
