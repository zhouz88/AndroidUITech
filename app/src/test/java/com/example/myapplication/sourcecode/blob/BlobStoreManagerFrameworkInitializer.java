/*
 * Copyright 2019 The Android Open Source Project
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
package com.example.myapplication.sourcecode.blob;

import android.app.SystemServiceRegistry;
import android.app.blob.BlobStoreManager;
import android.content.Context;

/**
 * This is where the BlobStoreManagerService wrapper is registered.
 *
 * @hide
 */
public class BlobStoreManagerFrameworkInitializer {
    /** Register the BlobStoreManager wrapper class */
    public static void initialize() {
        SystemServiceRegistry.registerContextAwareService(
                Context.BLOB_STORE_SERVICE, android.app.blob.BlobStoreManager.class,
                (context, service) ->
                        new BlobStoreManager(context, IBlobStoreManager.Stub.asInterface(service)));
    }
}
