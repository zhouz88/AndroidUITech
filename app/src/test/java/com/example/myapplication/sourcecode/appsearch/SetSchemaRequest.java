/*
 * Copyright 2020 The Android Open Source Project
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

import android.annotation.IntDef;
import android.annotation.IntRange;
import android.annotation.NonNull;
import android.annotation.SuppressLint;
import android.app.appsearch.AppSearchSchema;
import android.app.appsearch.AppSearchSession;
import android.app.appsearch.GenericDocument;
import android.app.appsearch.Migrator;
import android.app.appsearch.PackageIdentifier;
import android.app.appsearch.SetSchemaResponse;
import android.app.appsearch.annotation.CanIgnoreReturnValue;
import android.util.ArrayMap;
import android.util.ArraySet;

import com.android.internal.util.Preconditions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Encapsulates a request to update the schema of an {@link android.app.appsearch.AppSearchSession} database.
 *
 * <p>The schema is composed of a collection of {@link android.app.appsearch.AppSearchSchema} objects, each of which
 * defines a unique type of data.
 *
 * <p>The first call to SetSchemaRequest will set the provided schema and store it within the {@link
 * android.app.appsearch.AppSearchSession} database.
 *
 * <p>Subsequent calls will compare the provided schema to the previously saved schema, to determine
 * how to treat existing documents.
 *
 * <p>The following types of schema modifications are always safe and are made without deleting any
 * existing documents:
 *
 * <ul>
 *   <li>Addition of new {@link android.app.appsearch.AppSearchSchema} types
 *   <li>Addition of new properties to an existing {@link android.app.appsearch.AppSearchSchema} type
 *   <li>Changing the cardinality of a property to be less restrictive
 * </ul>
 *
 * <p>The following types of schema changes are not backwards compatible:
 *
 * <ul>
 *   <li>Removal of an existing {@link android.app.appsearch.AppSearchSchema} type
 *   <li>Removal of a property from an existing {@link android.app.appsearch.AppSearchSchema} type
 *   <li>Changing the data type of an existing property
 *   <li>Changing the cardinality of a property to be more restrictive
 * </ul>
 *
 * <p>Providing a schema with incompatible changes, will throw an {@link
 * android.app.appsearch.exceptions.AppSearchException}, with a message describing the
 * incompatibility. As a result, the previously set schema will remain unchanged.
 *
 * <p>Backward incompatible changes can be made by :
 *
 * <ul>
 *   <li>setting {@link Builder#setForceOverride} method to {@code true}. This
 *       deletes all documents that are incompatible with the new schema. The new schema is then
 *       saved and persisted to disk.
 *   <li>Add a {@link android.app.appsearch.Migrator} for each incompatible type and make no deletion. The migrator will
 *       migrate documents from its old schema version to the new version. Migrated types will be
 *       set into both {@link SetSchemaResponse#getIncompatibleTypes()} and {@link
 *       SetSchemaResponse#getMigratedTypes()}. See the migration section below.
 * </ul>
 *
 * @see android.app.appsearch.AppSearchSession#setSchema
 * @see android.app.appsearch.Migrator
 */
public final class SetSchemaRequest {

    /**
     * List of Android Permission are supported in {@link
     * Builder#addRequiredPermissionsForSchemaTypeVisibility}
     *
     * @see android.Manifest.permission
     * @hide
     */
    @IntDef(
            value = {
                READ_SMS,
                READ_CALENDAR,
                READ_CONTACTS,
                READ_EXTERNAL_STORAGE,
                READ_HOME_APP_SEARCH_DATA,
                READ_ASSISTANT_APP_SEARCH_DATA,
            })
    @Retention(RetentionPolicy.SOURCE)
    public @interface AppSearchSupportedPermission {}

    /**
     * The {@link android.Manifest.permission#READ_SMS} AppSearch supported in {@link
     * Builder#addRequiredPermissionsForSchemaTypeVisibility}
     */
    public static final int READ_SMS = 1;

    /**
     * The {@link android.Manifest.permission#READ_CALENDAR} AppSearch supported in {@link
     * Builder#addRequiredPermissionsForSchemaTypeVisibility}
     */
    public static final int READ_CALENDAR = 2;

    /**
     * The {@link android.Manifest.permission#READ_CONTACTS} AppSearch supported in {@link
     * Builder#addRequiredPermissionsForSchemaTypeVisibility}
     */
    public static final int READ_CONTACTS = 3;

    /**
     * The {@link android.Manifest.permission#READ_EXTERNAL_STORAGE} AppSearch supported in {@link
     * Builder#addRequiredPermissionsForSchemaTypeVisibility}
     */
    public static final int READ_EXTERNAL_STORAGE = 4;

    /**
     * The {@link android.Manifest.permission#READ_HOME_APP_SEARCH_DATA} AppSearch supported in
     * {@link Builder#addRequiredPermissionsForSchemaTypeVisibility}
     */
    public static final int READ_HOME_APP_SEARCH_DATA = 5;

    /**
     * The {@link android.Manifest.permission#READ_ASSISTANT_APP_SEARCH_DATA} AppSearch supported in
     * {@link Builder#addRequiredPermissionsForSchemaTypeVisibility}
     */
    public static final int READ_ASSISTANT_APP_SEARCH_DATA = 6;

    private final Set<android.app.appsearch.AppSearchSchema> mSchemas;
    private final Set<String> mSchemasNotDisplayedBySystem;
    private final Map<String, Set<PackageIdentifier>> mSchemasVisibleToPackages;
    private final Map<String, Set<Set<Integer>>> mSchemasVisibleToPermissions;
    private final Map<String, android.app.appsearch.Migrator> mMigrators;
    private final boolean mForceOverride;
    private final int mVersion;

    SetSchemaRequest(
            @NonNull Set<android.app.appsearch.AppSearchSchema> schemas,
            @NonNull Set<String> schemasNotDisplayedBySystem,
            @NonNull Map<String, Set<PackageIdentifier>> schemasVisibleToPackages,
            @NonNull Map<String, Set<Set<Integer>>> schemasVisibleToPermissions,
            @NonNull Map<String, android.app.appsearch.Migrator> migrators,
            boolean forceOverride,
            int version) {
        mSchemas = Objects.requireNonNull(schemas);
        mSchemasNotDisplayedBySystem = Objects.requireNonNull(schemasNotDisplayedBySystem);
        mSchemasVisibleToPackages = Objects.requireNonNull(schemasVisibleToPackages);
        mSchemasVisibleToPermissions = Objects.requireNonNull(schemasVisibleToPermissions);
        mMigrators = Objects.requireNonNull(migrators);
        mForceOverride = forceOverride;
        mVersion = version;
    }

    /** Returns the {@link android.app.appsearch.AppSearchSchema} types that are part of this request. */
    @NonNull
    public Set<android.app.appsearch.AppSearchSchema> getSchemas() {
        return Collections.unmodifiableSet(mSchemas);
    }

    /**
     * Returns all the schema types that are opted out of being displayed and visible on any system
     * UI surface.
     */
    @NonNull
    public Set<String> getSchemasNotDisplayedBySystem() {
        return Collections.unmodifiableSet(mSchemasNotDisplayedBySystem);
    }

    /**
     * Returns a mapping of schema types to the set of packages that have access to that schema
     * type.
     *
     * <p>It’s inefficient to call this method repeatedly.
     */
    @NonNull
    public Map<String, Set<PackageIdentifier>> getSchemasVisibleToPackages() {
        Map<String, Set<PackageIdentifier>> copy = new ArrayMap<>();
        for (Map.Entry<String, Set<PackageIdentifier>> entry :
                mSchemasVisibleToPackages.entrySet()) {
            copy.put(entry.getKey(), new ArraySet<>(entry.getValue()));
        }
        return copy;
    }

    /**
     * Returns a mapping of schema types to the Map of {@link android.Manifest.permission}
     * combinations that querier must hold to access that schema type.
     *
     * <p>The querier could read the {@link android.app.appsearch.GenericDocument} objects under the {@code schemaType} if
     * they holds ALL required permissions of ANY of the individual value sets.
     *
     * <p>For example, if the Map contains {@code {% verbatim %}{{permissionA, PermissionB},
     * {PermissionC, PermissionD}, {PermissionE}}{% endverbatim %}}.
     *
     * <ul>
     *   <li>A querier holds both PermissionA and PermissionB has access.
     *   <li>A querier holds both PermissionC and PermissionD has access.
     *   <li>A querier holds only PermissionE has access.
     *   <li>A querier holds both PermissionA and PermissionE has access.
     *   <li>A querier holds only PermissionA doesn't have access.
     *   <li>A querier holds both PermissionA and PermissionC doesn't have access.
     * </ul>
     *
     * <p>It’s inefficient to call this method repeatedly.
     *
     * @return The map contains schema type and all combinations of required permission for querier
     *     to access it. The supported Permission are {@link SetSchemaRequest#READ_SMS}, {@link
     *     SetSchemaRequest#READ_CALENDAR}, {@link SetSchemaRequest#READ_CONTACTS}, {@link
     *     SetSchemaRequest#READ_EXTERNAL_STORAGE}, {@link
     *     SetSchemaRequest#READ_HOME_APP_SEARCH_DATA} and {@link
     *     SetSchemaRequest#READ_ASSISTANT_APP_SEARCH_DATA}.
     */
    @NonNull
    public Map<String, Set<Set<Integer>>> getRequiredPermissionsForSchemaTypeVisibility() {
        return deepCopy(mSchemasVisibleToPermissions);
    }

    /**
     * Returns the map of {@link android.app.appsearch.Migrator}, the key will be the schema type of the {@link android.app.appsearch.Migrator}
     * associated with.
     */
    @NonNull
    public Map<String, android.app.appsearch.Migrator> getMigrators() {
        return Collections.unmodifiableMap(mMigrators);
    }

    /**
     * Returns a mapping of {@link android.app.appsearch.AppSearchSchema} types to the set of packages that have access to
     * that schema type.
     *
     * <p>A more efficient version of {@link #getSchemasVisibleToPackages}, but it returns a
     * modifiable map. This is not meant to be unhidden and should only be used by internal classes.
     *
     * @hide
     */
    @NonNull
    public Map<String, Set<PackageIdentifier>> getSchemasVisibleToPackagesInternal() {
        return mSchemasVisibleToPackages;
    }

    /** Returns whether this request will force the schema to be overridden. */
    public boolean isForceOverride() {
        return mForceOverride;
    }

    /** Returns the database overall schema version. */
    @IntRange(from = 1)
    public int getVersion() {
        return mVersion;
    }

    /** Builder for {@link SetSchemaRequest} objects. */
    public static final class Builder {
        private static final int DEFAULT_VERSION = 1;
        private ArraySet<android.app.appsearch.AppSearchSchema> mSchemas = new ArraySet<>();
        private ArraySet<String> mSchemasNotDisplayedBySystem = new ArraySet<>();
        private ArrayMap<String, Set<PackageIdentifier>> mSchemasVisibleToPackages =
                new ArrayMap<>();
        private ArrayMap<String, Set<Set<Integer>>> mSchemasVisibleToPermissions = new ArrayMap<>();
        private ArrayMap<String, android.app.appsearch.Migrator> mMigrators = new ArrayMap<>();
        private boolean mForceOverride = false;
        private int mVersion = DEFAULT_VERSION;
        private boolean mBuilt = false;

        /**
         * Adds one or more {@link android.app.appsearch.AppSearchSchema} types to the schema.
         *
         * <p>An {@link android.app.appsearch.AppSearchSchema} object represents one type of structured data.
         *
         * <p>Any documents of these types will be displayed on system UI surfaces by default.
         */
        @CanIgnoreReturnValue
        @NonNull
        public Builder addSchemas(@NonNull android.app.appsearch.AppSearchSchema... schemas) {
            Objects.requireNonNull(schemas);
            resetIfBuilt();
            return addSchemas(Arrays.asList(schemas));
        }

        /**
         * Adds a collection of {@link android.app.appsearch.AppSearchSchema} objects to the schema.
         *
         * <p>An {@link android.app.appsearch.AppSearchSchema} object represents one type of structured data.
         */
        @CanIgnoreReturnValue
        @NonNull
        public Builder addSchemas(@NonNull Collection<android.app.appsearch.AppSearchSchema> schemas) {
            Objects.requireNonNull(schemas);
            resetIfBuilt();
            mSchemas.addAll(schemas);
            return this;
        }

        /**
         * Sets whether or not documents from the provided {@code schemaType} will be displayed and
         * visible on any system UI surface.
         *
         * <p>This setting applies to the provided {@code schemaType} only, and does not persist
         * across {@link android.app.appsearch.AppSearchSession#setSchema} calls.
         *
         * <p>The default behavior, if this method is not called, is to allow types to be displayed
         * on system UI surfaces.
         *
         * @param schemaType The name of an {@link android.app.appsearch.AppSearchSchema} within the same {@link
         *     SetSchemaRequest}, which will be configured.
         * @param displayed Whether documents of this type will be displayed on system UI surfaces.
         */
        // Merged list available from getSchemasNotDisplayedBySystem
        @CanIgnoreReturnValue
        @SuppressLint("MissingGetterMatchingBuilder")
        @NonNull
        public Builder setSchemaTypeDisplayedBySystem(
                @NonNull String schemaType, boolean displayed) {
            Objects.requireNonNull(schemaType);
            resetIfBuilt();
            if (displayed) {
                mSchemasNotDisplayedBySystem.remove(schemaType);
            } else {
                mSchemasNotDisplayedBySystem.add(schemaType);
            }
            return this;
        }

        /**
         * Adds a set of required Android {@link android.Manifest.permission} combination to the
         * given schema type.
         *
         * <p>If the querier holds ALL of the required permissions in this combination, they will
         * have access to read {@link android.app.appsearch.GenericDocument} objects of the given schema type.
         *
         * <p>You can call this method to add multiple permission combinations, and the querier will
         * have access if they holds ANY of the combinations.
         *
         * <p>The supported Permissions are {@link #READ_SMS}, {@link #READ_CALENDAR}, {@link
         * #READ_CONTACTS}, {@link #READ_EXTERNAL_STORAGE}, {@link #READ_HOME_APP_SEARCH_DATA} and
         * {@link #READ_ASSISTANT_APP_SEARCH_DATA}.
         *
         * @see android.Manifest.permission#READ_SMS
         * @see android.Manifest.permission#READ_CALENDAR
         * @see android.Manifest.permission#READ_CONTACTS
         * @see android.Manifest.permission#READ_EXTERNAL_STORAGE
         * @see android.Manifest.permission#READ_HOME_APP_SEARCH_DATA
         * @see android.Manifest.permission#READ_ASSISTANT_APP_SEARCH_DATA
         * @param schemaType The schema type to set visibility on.
         * @param permissions A set of required Android permissions the caller need to hold to
         *     access {@link android.app.appsearch.GenericDocument} objects that under the given schema.
         * @throws IllegalArgumentException – if input unsupported permission.
         */
        // Merged list available from getRequiredPermissionsForSchemaTypeVisibility
        @CanIgnoreReturnValue
        @SuppressLint("MissingGetterMatchingBuilder")
        @NonNull
        public Builder addRequiredPermissionsForSchemaTypeVisibility(
                @NonNull String schemaType,
                @AppSearchSupportedPermission @NonNull Set<Integer> permissions) {
            Objects.requireNonNull(schemaType);
            Objects.requireNonNull(permissions);
            for (int permission : permissions) {
                Preconditions.checkArgumentInRange(
                        permission, READ_SMS, READ_ASSISTANT_APP_SEARCH_DATA, "permission");
            }
            resetIfBuilt();
            Set<Set<Integer>> visibleToPermissions = mSchemasVisibleToPermissions.get(schemaType);
            if (visibleToPermissions == null) {
                visibleToPermissions = new ArraySet<>();
                mSchemasVisibleToPermissions.put(schemaType, visibleToPermissions);
            }
            visibleToPermissions.add(permissions);
            return this;
        }

        /** Clears all required permissions combinations for the given schema type. */
        @NonNull
        public Builder clearRequiredPermissionsForSchemaTypeVisibility(@NonNull String schemaType) {
            Objects.requireNonNull(schemaType);
            resetIfBuilt();
            mSchemasVisibleToPermissions.remove(schemaType);
            return this;
        }

        /**
         * Sets whether or not documents from the provided {@code schemaType} can be read by the
         * specified package.
         *
         * <p>Each package is represented by a {@link PackageIdentifier}, containing a package name
         * and a byte array of type {@link android.content.pm.PackageManager#CERT_INPUT_SHA256}.
         *
         * <p>To opt into one-way data sharing with another application, the developer will need to
         * explicitly grant the other application’s package name and certificate Read access to its
         * data.
         *
         * <p>For two-way data sharing, both applications need to explicitly grant Read access to
         * one another.
         *
         * <p>By default, data sharing between applications is disabled.
         *
         * @param schemaType The schema type to set visibility on.
         * @param visible Whether the {@code schemaType} will be visible or not.
         * @param packageIdentifier Represents the package that will be granted visibility.
         */
        // Merged list available from getSchemasVisibleToPackages
        @CanIgnoreReturnValue
        @SuppressLint("MissingGetterMatchingBuilder")
        @NonNull
        public Builder setSchemaTypeVisibilityForPackage(
                @NonNull String schemaType,
                boolean visible,
                @NonNull PackageIdentifier packageIdentifier) {
            Objects.requireNonNull(schemaType);
            Objects.requireNonNull(packageIdentifier);
            resetIfBuilt();

            Set<PackageIdentifier> packageIdentifiers = mSchemasVisibleToPackages.get(schemaType);
            if (visible) {
                if (packageIdentifiers == null) {
                    packageIdentifiers = new ArraySet<>();
                }
                packageIdentifiers.add(packageIdentifier);
                mSchemasVisibleToPackages.put(schemaType, packageIdentifiers);
            } else {
                if (packageIdentifiers == null) {
                    // Return early since there was nothing set to begin with.
                    return this;
                }
                packageIdentifiers.remove(packageIdentifier);
                if (packageIdentifiers.isEmpty()) {
                    // Remove the entire key so that we don't have empty sets as values.
                    mSchemasVisibleToPackages.remove(schemaType);
                }
            }

            return this;
        }

        /**
         * Sets the {@link android.app.appsearch.Migrator} associated with the given SchemaType.
         *
         * <p>The {@link android.app.appsearch.Migrator} migrates all {@link android.app.appsearch.GenericDocument}s under given schema type
         * from the current version number stored in AppSearch to the final version set via {@link
         * #setVersion}.
         *
         * <p>A {@link android.app.appsearch.Migrator} will be invoked if the current version number stored in AppSearch
         * is different from the final version set via {@link #setVersion} and {@link
         * android.app.appsearch.Migrator#shouldMigrate} returns {@code true}.
         *
         * <p>The target schema type of the output {@link android.app.appsearch.GenericDocument} of {@link
         * android.app.appsearch.Migrator#onUpgrade} or {@link android.app.appsearch.Migrator#onDowngrade} must exist in this {@link
         * SetSchemaRequest}.
         *
         * @param schemaType The schema type to set migrator on.
         * @param migrator The migrator translates a document from its current version to the final
         *     version set via {@link #setVersion}.
         * @see Builder#setVersion
         * @see Builder#addSchemas
         * @see android.app.appsearch.AppSearchSession#setSchema
         */
        @CanIgnoreReturnValue
        @NonNull
        @SuppressLint("MissingGetterMatchingBuilder") // Getter return plural objects.
        public Builder setMigrator(@NonNull String schemaType, @NonNull android.app.appsearch.Migrator migrator) {
            Objects.requireNonNull(schemaType);
            Objects.requireNonNull(migrator);
            resetIfBuilt();
            mMigrators.put(schemaType, migrator);
            return this;
        }

        /**
         * Sets a Map of {@link android.app.appsearch.Migrator}s.
         *
         * <p>The key of the map is the schema type that the {@link android.app.appsearch.Migrator} value applies to.
         *
         * <p>The {@link android.app.appsearch.Migrator} migrates all {@link android.app.appsearch.GenericDocument}s under given schema type
         * from the current version number stored in AppSearch to the final version set via {@link
         * #setVersion}.
         *
         * <p>A {@link android.app.appsearch.Migrator} will be invoked if the current version number stored in AppSearch
         * is different from the final version set via {@link #setVersion} and {@link
         * android.app.appsearch.Migrator#shouldMigrate} returns {@code true}.
         *
         * <p>The target schema type of the output {@link GenericDocument} of {@link
         * android.app.appsearch.Migrator#onUpgrade} or {@link android.app.appsearch.Migrator#onDowngrade} must exist in this {@link
         * SetSchemaRequest}.
         *
         * @param migrators A {@link Map} of migrators that translate a document from its current
         *     version to the final version set via {@link #setVersion}. The key of the map is the
         *     schema type that the {@link android.app.appsearch.Migrator} value applies to.
         * @see Builder#setVersion
         * @see Builder#addSchemas
         * @see android.app.appsearch.AppSearchSession#setSchema
         */
        @CanIgnoreReturnValue
        @NonNull
        public Builder setMigrators(@NonNull Map<String, android.app.appsearch.Migrator> migrators) {
            Objects.requireNonNull(migrators);
            resetIfBuilt();
            mMigrators.putAll(migrators);
            return this;
        }

        /**
         * Sets whether or not to override the current schema in the {@link android.app.appsearch.AppSearchSession}
         * database.
         *
         * <p>Call this method whenever backward incompatible changes need to be made by setting
         * {@code forceOverride} to {@code true}. As a result, during execution of the setSchema
         * operation, all documents that are incompatible with the new schema will be deleted and
         * the new schema will be saved and persisted.
         *
         * <p>By default, this is {@code false}.
         */
        @CanIgnoreReturnValue
        @NonNull
        public Builder setForceOverride(boolean forceOverride) {
            resetIfBuilt();
            mForceOverride = forceOverride;
            return this;
        }

        /**
         * Sets the version number of the overall {@link android.app.appsearch.AppSearchSchema} in the database.
         *
         * <p>The {@link android.app.appsearch.AppSearchSession} database can only ever hold documents for one version at
         * a time.
         *
         * <p>Setting a version number that is different from the version number currently stored in
         * AppSearch will result in AppSearch calling the {@link android.app.appsearch.Migrator}s provided to {@link
         * android.app.appsearch.AppSearchSession#setSchema} to migrate the documents already in AppSearch from the
         * previous version to the one set in this request. The version number can be updated
         * without any other changes to the set of schemas.
         *
         * <p>The version number can stay the same, increase, or decrease relative to the current
         * version number that is already stored in the {@link android.app.appsearch.AppSearchSession} database.
         *
         * <p>The version of an empty database will always be 0. You cannot set version to the
         * {@link SetSchemaRequest}, if it doesn't contains any {@link android.app.appsearch.AppSearchSchema}.
         *
         * @param version A positive integer representing the version of the entire set of schemas
         *     represents the version of the whole schema in the {@link android.app.appsearch.AppSearchSession} database,
         *     default version is 1.
         * @throws IllegalArgumentException if the version is negative.
         * @see AppSearchSession#setSchema
         * @see Migrator
         * @see Builder#setMigrator
         */
        @CanIgnoreReturnValue
        @NonNull
        public Builder setVersion(@IntRange(from = 1) int version) {
            Preconditions.checkArgument(version >= 1, "Version must be a positive number.");
            resetIfBuilt();
            mVersion = version;
            return this;
        }

        /**
         * Builds a new {@link SetSchemaRequest} object.
         *
         * @throws IllegalArgumentException if schema types were referenced, but the corresponding
         *     {@link android.app.appsearch.AppSearchSchema} type was never added.
         */
        @NonNull
        public SetSchemaRequest build() {
            // Verify that any schema types with display or visibility settings refer to a real
            // schema.
            // Create a copy because we're going to remove from the set for verification purposes.
            Set<String> referencedSchemas = new ArraySet<>(mSchemasNotDisplayedBySystem);
            referencedSchemas.addAll(mSchemasVisibleToPackages.keySet());
            referencedSchemas.addAll(mSchemasVisibleToPermissions.keySet());

            for (AppSearchSchema schema : mSchemas) {
                referencedSchemas.remove(schema.getSchemaType());
            }
            if (!referencedSchemas.isEmpty()) {
                // We still have schema types that weren't seen in our mSchemas set. This means
                // there wasn't a corresponding AppSearchSchema.
                throw new IllegalArgumentException(
                        "Schema types " + referencedSchemas + " referenced, but were not added.");
            }
            if (mSchemas.isEmpty() && mVersion != DEFAULT_VERSION) {
                throw new IllegalArgumentException(
                        "Cannot set version to the request if schema is empty.");
            }
            mBuilt = true;
            return new SetSchemaRequest(
                    mSchemas,
                    mSchemasNotDisplayedBySystem,
                    mSchemasVisibleToPackages,
                    mSchemasVisibleToPermissions,
                    mMigrators,
                    mForceOverride,
                    mVersion);
        }

        private void resetIfBuilt() {
            if (mBuilt) {
                ArrayMap<String, Set<PackageIdentifier>> schemasVisibleToPackages =
                        new ArrayMap<>(mSchemasVisibleToPackages.size());
                for (Map.Entry<String, Set<PackageIdentifier>> entry :
                        mSchemasVisibleToPackages.entrySet()) {
                    schemasVisibleToPackages.put(entry.getKey(), new ArraySet<>(entry.getValue()));
                }
                mSchemasVisibleToPackages = schemasVisibleToPackages;

                mSchemasVisibleToPermissions = deepCopy(mSchemasVisibleToPermissions);

                mSchemas = new ArraySet<>(mSchemas);
                mSchemasNotDisplayedBySystem = new ArraySet<>(mSchemasNotDisplayedBySystem);
                mMigrators = new ArrayMap<>(mMigrators);
                mBuilt = false;
            }
        }
    }

    static ArrayMap<String, Set<Set<Integer>>> deepCopy(
            @NonNull Map<String, Set<Set<Integer>>> original) {
        ArrayMap<String, Set<Set<Integer>>> copy = new ArrayMap<>(original.size());
        for (Map.Entry<String, Set<Set<Integer>>> entry : original.entrySet()) {
            Set<Set<Integer>> valueCopy = new ArraySet<>();
            for (Set<Integer> innerValue : entry.getValue()) {
                valueCopy.add(new ArraySet<>(innerValue));
            }
            copy.put(entry.getKey(), valueCopy);
        }
        return copy;
    }
}
