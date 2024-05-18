/*
 * Copyright (C) 2006 The Android Open Source Project
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

import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Xfermode;

/**
 * <p>Specialized implementation of {@link android.graphics.Paint}'s
 * {@link Paint#setXfermode(android.graphics.Xfermode) transfer mode}. Refer to the
 * documentation of the {@link android.graphics.PorterDuff.Mode} enum for more
 * information on the available alpha compositing and blending modes.</p>
 *
 */
public class PorterDuffXfermode extends Xfermode {
    /**
     * Create an xfermode that uses the specified porter-duff mode.
     *
     * @param mode           The porter-duff mode that is applied
     */
    public PorterDuffXfermode(PorterDuff.Mode mode) {
        porterDuffMode = mode.nativeInt;
    }
}
