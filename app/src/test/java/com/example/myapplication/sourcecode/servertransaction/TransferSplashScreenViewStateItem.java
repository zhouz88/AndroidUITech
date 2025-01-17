/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.example.myapplication.sourcecode.servertransaction;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.app.ActivityThread;
import android.app.ClientTransactionHandler;
import android.os.Parcel;
import android.view.SurfaceControl;
import android.window.SplashScreenView.SplashScreenViewParcelable;

/**
 * Transfer a splash screen view to an Activity.
 * @hide
 */
public class TransferSplashScreenViewStateItem extends ActivityTransactionItem {

    private SplashScreenViewParcelable mSplashScreenViewParcelable;
    private SurfaceControl mStartingWindowLeash;

    @Override
    public void execute(@NonNull ClientTransactionHandler client,
            @NonNull ActivityThread.ActivityClientRecord r,
            PendingTransactionActions pendingActions) {
        client.handleAttachSplashScreenView(r, mSplashScreenViewParcelable, mStartingWindowLeash);
    }

    @Override
    public void recycle() {
        ObjectPool.recycle(this);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(mSplashScreenViewParcelable, flags);
        dest.writeTypedObject(mStartingWindowLeash, flags);
    }

    private TransferSplashScreenViewStateItem() {}
    private TransferSplashScreenViewStateItem(Parcel in) {
        mSplashScreenViewParcelable = in.readTypedObject(SplashScreenViewParcelable.CREATOR);
        mStartingWindowLeash = in.readTypedObject(SurfaceControl.CREATOR);
    }

    /** Obtain an instance initialized with provided params. */
    public static TransferSplashScreenViewStateItem obtain(
            @Nullable SplashScreenViewParcelable parcelable,
            @Nullable SurfaceControl startingWindowLeash) {
        TransferSplashScreenViewStateItem instance =
                ObjectPool.obtain(TransferSplashScreenViewStateItem.class);
        if (instance == null) {
            instance = new TransferSplashScreenViewStateItem();
        }
        instance.mSplashScreenViewParcelable = parcelable;
        instance.mStartingWindowLeash = startingWindowLeash;

        return instance;
    }

    public static final @NonNull Creator<TransferSplashScreenViewStateItem> CREATOR =
            new Creator<TransferSplashScreenViewStateItem>() {
                public TransferSplashScreenViewStateItem createFromParcel(Parcel in) {
                    return new TransferSplashScreenViewStateItem(in);
                }

                public TransferSplashScreenViewStateItem[] newArray(int size) {
                    return new TransferSplashScreenViewStateItem[size];
                }
            };
}
