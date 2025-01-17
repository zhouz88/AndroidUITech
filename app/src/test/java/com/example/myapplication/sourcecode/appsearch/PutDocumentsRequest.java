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


import android.annotation.NonNull;
import android.app.appsearch.AppSearchSession;
import android.app.appsearch.GenericDocument;
import android.app.appsearch.annotation.CanIgnoreReturnValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Encapsulates a request to index documents into an {@link android.app.appsearch.AppSearchSession} database.
 *
 * @see AppSearchSession#put
 */
public final class PutDocumentsRequest {
    private final List<android.app.appsearch.GenericDocument> mDocuments;

    PutDocumentsRequest(List<android.app.appsearch.GenericDocument> documents) {
        mDocuments = documents;
    }

    /** Returns a list of {@link android.app.appsearch.GenericDocument} objects that are part of this request. */
    @NonNull
    public List<android.app.appsearch.GenericDocument> getGenericDocuments() {
        return Collections.unmodifiableList(mDocuments);
    }

    /** Builder for {@link PutDocumentsRequest} objects. */
    public static final class Builder {
        private ArrayList<android.app.appsearch.GenericDocument> mDocuments = new ArrayList<>();
        private boolean mBuilt = false;

        /** Adds one or more {@link android.app.appsearch.GenericDocument} objects to the request. */
        @CanIgnoreReturnValue
        @NonNull
        public Builder addGenericDocuments(@NonNull android.app.appsearch.GenericDocument... documents) {
            Objects.requireNonNull(documents);
            resetIfBuilt();
            return addGenericDocuments(Arrays.asList(documents));
        }

        /** Adds a collection of {@link android.app.appsearch.GenericDocument} objects to the request. */
        @CanIgnoreReturnValue
        @NonNull
        public Builder addGenericDocuments(
                @NonNull Collection<? extends GenericDocument> documents) {
            Objects.requireNonNull(documents);
            resetIfBuilt();
            mDocuments.addAll(documents);
            return this;
        }

        /** Creates a new {@link PutDocumentsRequest} object. */
        @NonNull
        public PutDocumentsRequest build() {
            mBuilt = true;
            return new PutDocumentsRequest(mDocuments);
        }

        private void resetIfBuilt() {
            if (mBuilt) {
                mDocuments = new ArrayList<>(mDocuments);
                mBuilt = false;
            }
        }
    }
}
