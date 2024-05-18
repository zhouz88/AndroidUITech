/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.example.myapplication.sourcecode2.graphics;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.compat.annotation.UnsupportedAppUsage;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.os.Build;

/**
 * A color filter that transforms colors through a 4x5 color matrix. This filter
 * can be used to change the saturation of pixels, convert from YUV to RGB, etc.
 *
 * @see android.graphics.ColorMatrix
 */
public class ColorMatrixColorFilter extends ColorFilter {
    @UnsupportedAppUsage
    private final android.graphics.ColorMatrix mMatrix = new android.graphics.ColorMatrix();

    /**
     * Create a color filter that transforms colors through a 4x5 color matrix.
     *
     * @param matrix 4x5 matrix used to transform colors. It is copied into
     *               the filter, so changes made to the matrix after the filter
     *               is constructed will not be reflected in the filter.
     */
    public ColorMatrixColorFilter(@NonNull android.graphics.ColorMatrix matrix) {
        mMatrix.set(matrix);
    }

    /**
     * Create a color filter that transforms colors through a 4x5 color matrix.
     *
     * @param array Array of floats used to transform colors, treated as a 4x5
     *              matrix. The first 20 entries of the array are copied into
     *              the filter. See ColorMatrix.
     */
    public ColorMatrixColorFilter(@NonNull float[] array) {
        if (array.length < 20) {
            throw new ArrayIndexOutOfBoundsException();
        }
        mMatrix.set(array);
    }

    /**
     * Copies the ColorMatrix from the filter into the passed ColorMatrix.
     *
     * @param colorMatrix Set to the current value of the filter's ColorMatrix.
     */
    public void getColorMatrix(android.graphics.ColorMatrix colorMatrix) {
        colorMatrix.set(mMatrix);
    }

    /**
     * Copies the provided color matrix to be used by this filter.
     *
     * If the specified color matrix is null, this filter's color matrix will be reset to the
     * identity matrix.
     *
     * @param matrix A {@link android.graphics.ColorMatrix} or null
     *
     * @see #getColorMatrix(android.graphics.ColorMatrix)
     * @see #setColorMatrixArray(float[])
     * @see android.graphics.ColorMatrix#reset()
     *
     * @hide
     */
    @UnsupportedAppUsage
    public void setColorMatrix(@Nullable android.graphics.ColorMatrix matrix) {
        discardNativeInstance();
        if (matrix == null) {
            mMatrix.reset();
        } else {
            mMatrix.set(matrix);
        }
    }

    /**
     * Copies the provided color matrix to be used by this filter.
     *
     * If the specified color matrix is null, this filter's color matrix will be reset to the
     * identity matrix.
     *
     * @param array Array of floats used to transform colors, treated as a 4x5
     *              matrix. The first 20 entries of the array are copied into
     *              the filter. See {@link android.graphics.ColorMatrix}.
     *
     * @see #getColorMatrix(android.graphics.ColorMatrix)
     * @see #setColorMatrix(android.graphics.ColorMatrix)
     * @see ColorMatrix#reset()
     *
     * @throws ArrayIndexOutOfBoundsException if the specified array's
     *         length is < 20
     *
     * @hide
     */
    @UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)
    public void setColorMatrixArray(@Nullable float[] array) {
        // called '...Array' so that passing null isn't ambiguous
        discardNativeInstance();
        if (array == null) {
            mMatrix.reset();
        } else {
            if (array.length < 20) {
                throw new ArrayIndexOutOfBoundsException();
            }
            mMatrix.set(array);
        }
    }

    @Override
    long createNativeInstance() {
        return nativeColorMatrixFilter(mMatrix.getArray());
    }

    private static native long nativeColorMatrixFilter(float[] array);
}
