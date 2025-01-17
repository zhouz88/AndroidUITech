/*
 * Copyright 2022 The Android Open Source Project
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
import android.annotation.Nullable;
import android.app.appsearch.SetSchemaResponse;
import android.os.Bundle;

import java.util.Objects;

/**
 * An internal wrapper class of {@link android.app.appsearch.SetSchemaResponse}.
 *
 * <p>For public users, if the {@link android.app.appsearch.AppSearchSession#setSchema} failed, we
 * will directly throw an Exception. But AppSearch internal need to divert the incompatible changes
 * form other call flows. This class adds a {@link #isSuccess()} to indicate if the call fails
 * because of incompatible change.
 *
 * @hide
 */
public class InternalSetSchemaResponse {

    private static final String IS_SUCCESS_FIELD = "isSuccess";
    private static final String SET_SCHEMA_RESPONSE_BUNDLE_FIELD = "setSchemaResponseBundle";
    private static final String ERROR_MESSAGE_FIELD = "errorMessage";

    private final Bundle mBundle;

    public InternalSetSchemaResponse(@NonNull Bundle bundle) {
        mBundle = Objects.requireNonNull(bundle);
    }

    private InternalSetSchemaResponse(
            boolean isSuccess,
            @NonNull android.app.appsearch.SetSchemaResponse setSchemaResponse,
            @Nullable String errorMessage) {
        Objects.requireNonNull(setSchemaResponse);
        mBundle = new Bundle();
        mBundle.putBoolean(IS_SUCCESS_FIELD, isSuccess);
        mBundle.putBundle(SET_SCHEMA_RESPONSE_BUNDLE_FIELD, setSchemaResponse.getBundle());
        mBundle.putString(ERROR_MESSAGE_FIELD, errorMessage);
    }

    /**
     * Returns the {@link Bundle} populated by this builder.
     *
     * @hide
     */
    @NonNull
    public Bundle getBundle() {
        return mBundle;
    }

    /**
     * Creates a new successful {@link InternalSetSchemaResponse}.
     *
     * @param setSchemaResponse The object this internal object represents.
     */
    @NonNull
    public static InternalSetSchemaResponse newSuccessfulSetSchemaResponse(
            @NonNull android.app.appsearch.SetSchemaResponse setSchemaResponse) {
        return new InternalSetSchemaResponse(
                /*isSuccess=*/ true, setSchemaResponse, /*errorMessage=*/ null);
    }

    /**
     * Creates a new failed {@link InternalSetSchemaResponse}.
     *
     * @param setSchemaResponse The object this internal object represents.
     * @param errorMessage An string describing the reason or nature of the failure.
     */
    @NonNull
    public static InternalSetSchemaResponse newFailedSetSchemaResponse(
            @NonNull android.app.appsearch.SetSchemaResponse setSchemaResponse, @NonNull String errorMessage) {
        return new InternalSetSchemaResponse(/*isSuccess=*/ false, setSchemaResponse, errorMessage);
    }

    /** Returns {@code true} if the schema request is proceeded successfully. */
    public boolean isSuccess() {
        return mBundle.getBoolean(IS_SUCCESS_FIELD);
    }

    /**
     * Returns the {@link android.app.appsearch.SetSchemaResponse} of the set schema call.
     *
     * <p>The call may or may not success. Check {@link #isSuccess()} before call this method.
     */
    @NonNull
    public android.app.appsearch.SetSchemaResponse getSetSchemaResponse() {
        return new SetSchemaResponse(mBundle.getBundle(SET_SCHEMA_RESPONSE_BUNDLE_FIELD));
    }

    /**
     * Returns the error message associated with this response.
     *
     * <p>If {@link #isSuccess} is {@code true}, the error message is always {@code null}.
     */
    @Nullable
    public String getErrorMessage() {
        return mBundle.getString(ERROR_MESSAGE_FIELD);
    }
}
