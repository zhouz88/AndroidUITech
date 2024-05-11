//package com.example.myapplication.testMatrix;
///*
// * Copyright (C) 2006 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package android.graphics;
//
//import android.annotation.ColorInt;
//import android.annotation.ColorLong;
//import android.annotation.IntDef;
//import android.annotation.IntRange;
//import android.annotation.NonNull;
//import android.annotation.Nullable;
//import android.annotation.Size;
//import android.compat.annotation.UnsupportedAppUsage;
//import android.graphics.Bitmap;
//import android.graphics.BlendMode;
//import android.graphics.Color;
//import android.graphics.DrawFilter;
//import android.graphics.Matrix;
//import android.graphics.NinePatch;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.Picture;
//import android.graphics.PorterDuff;
//import android.graphics.Rect;
//import android.graphics.RectF;
//import android.graphics.Region;
//import android.graphics.RenderNode;
//import android.graphics.fonts.Font;
//import android.graphics.text.MeasuredText;
//import android.graphics.text.TextRunShaper;
//import android.os.Build;
//import android.text.TextShaper;
//
//import dalvik.annotation.optimization.CriticalNative;
//import dalvik.annotation.optimization.FastNative;
//
//import libcore.util.NativeAllocationRegistry;
//
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//
//public class Canvas extends BaseCanvas {
//    private static int sCompatiblityVersion = 0;
//    private static boolean sCompatibilityRestore = false;
//    private static boolean sCompatibilitySetBitmap = false;
//
//    /** @hide */
//    @UnsupportedAppUsage
//    public long getNativeCanvasWrapper() {
//        return mNativeCanvasWrapper;
//    }
//
//    // may be null
//    @UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 117521088)
//    private Bitmap mBitmap;
//
//    // optional field set by the caller
//    private DrawFilter mDrawFilter;
//
//    // Maximum bitmap size as defined in Skia's native code
//    // (see SkCanvas.cpp, SkDraw.cpp)
//    private static final int MAXMIMUM_BITMAP_SIZE = 32766;
//
//    // Use a Holder to allow static initialization of Canvas in the boot image.
//    private static class NoImagePreloadHolder {
//        public static final NativeAllocationRegistry sRegistry =
//                NativeAllocationRegistry.createMalloced(
//                        android.graphics.Canvas.class.getClassLoader(), nGetNativeFinalizer());
//    }
//
//    // This field is used to finalize the native Canvas properly
//    private Runnable mFinalizer;
//
//    /**
//     * Construct an empty raster canvas. Use setBitmap() to specify a bitmap to
//     * draw into.  The initial target density is {@link Bitmap#DENSITY_NONE};
//     * this will typically be replaced when a target bitmap is set for the
//     * canvas.
//     */
//    public Canvas() {
//        if (!isHardwareAccelerated()) {
//            // 0 means no native bitmap
//            mNativeCanvasWrapper = nInitRaster(0);
//            mFinalizer = NoImagePreloadHolder.sRegistry.registerNativeAllocation(
//                    this, mNativeCanvasWrapper);
//        } else {
//            mFinalizer = null;
//        }
//    }
//    public Canvas(@NonNull Bitmap bitmap) {
//        if (!bitmap.isMutable()) {
//            throw new IllegalStateException("Immutable bitmap passed to Canvas constructor");
//        }
//        throwIfCannotDraw(bitmap);
//        mNativeCanvasWrapper = nInitRaster(bitmap.getNativeInstance());
//        mFinalizer = NoImagePreloadHolder.sRegistry.registerNativeAllocation(
//                this, mNativeCanvasWrapper);
//        mBitmap = bitmap;
//        mDensity = bitmap.mDensity;
//    }
//
//    /**
//     *  @hide Needed by android.graphics.pdf.PdfDocument, but should not be called from
//     *  outside the UI rendering module.
//     */
//    public Canvas(long nativeCanvas) {
//        if (nativeCanvas == 0) {
//            throw new IllegalStateException();
//        }
//        mNativeCanvasWrapper = nativeCanvas;
//        mFinalizer = NoImagePreloadHolder.sRegistry.registerNativeAllocation(
//                this, mNativeCanvasWrapper);
//        mDensity = Bitmap.getDefaultDensity();
//    }
//
//    /**
//     * Indicates whether this Canvas uses hardware acceleration.
//     *
//     * Note that this method does not define what type of hardware acceleration
//     * may or may not be used.
//     *
//     * @return True if drawing operations are hardware accelerated,
//     *         false otherwise.
//     */
//    public boolean isHardwareAccelerated() {
//        return false;
//    }
//
//    public void setBitmap(@Nullable Bitmap bitmap) {
//        if (isHardwareAccelerated()) {
//            throw new RuntimeException("Can't set a bitmap device on a HW accelerated canvas");
//        }
//
//        Matrix preservedMatrix = null;
//        if (bitmap != null && sCompatibilitySetBitmap) {
//            preservedMatrix = getMatrix();
//        }
//
//        if (bitmap == null) {
//            nSetBitmap(mNativeCanvasWrapper, 0);
//            mDensity = Bitmap.DENSITY_NONE;
//        } else {
//            if (!bitmap.isMutable()) {
//                throw new IllegalStateException();
//            }
//            throwIfCannotDraw(bitmap);
//
//            nSetBitmap(mNativeCanvasWrapper, bitmap.getNativeInstance());
//            mDensity = bitmap.mDensity;
//        }
//
//        if (preservedMatrix != null) {
//            setMatrix(preservedMatrix);
//        }
//
//        mBitmap = bitmap;
//    }
//
//    public void disableZ() {
//    }
//
//
//    public boolean isOpaque() {
//        return nIsOpaque(mNativeCanvasWrapper);
//    }
//
//
//    public int getWidth() {
//        return nGetWidth(mNativeCanvasWrapper);
//    }
//
//
//    public int getHeight() {
//        return nGetHeight(mNativeCanvasWrapper);
//    }
//
//
//    public int getDensity() {
//        return mDensity;
//    }
//
//    public void setDensity(int density) {
//        if (mBitmap != null) {
//            mBitmap.setDensity(density);
//        }
//        mDensity = density;
//    }
//
//    /** @hide */
//    @UnsupportedAppUsage
//    public void setScreenDensity(int density) {
//        mScreenDensity = density;
//    }
//    public int getMaximumBitmapWidth() {
//        return MAXMIMUM_BITMAP_SIZE;
//    }
//    public int getMaximumBitmapHeight() {
//        return MAXMIMUM_BITMAP_SIZE;
//    }
//
//    // the SAVE_FLAG constants must match their native equivalents
//
//    /** @hide */
//    @IntDef(flag = true,
//            value = {
//                    ALL_SAVE_FLAG
//            })
//    @Retention(RetentionPolicy.SOURCE)
//    public @interface Saveflags {}
//
//
//    public static final int MATRIX_SAVE_FLAG = 0x01;
//
//    public static final int CLIP_SAVE_FLAG = 0x02;
//
//
//    public static final int HAS_ALPHA_LAYER_SAVE_FLAG = 0x04;
//
//    public static final int FULL_COLOR_LAYER_SAVE_FLAG = 0x08;
//
//    public static final int CLIP_TO_LAYER_SAVE_FLAG = 0x10;
//
//    public static final int ALL_SAVE_FLAG = 0x1F;
//
//    private static void checkValidSaveFlags(int saveFlags) {
//        if (sCompatiblityVersion >= Build.VERSION_CODES.P
//                && saveFlags != ALL_SAVE_FLAG) {
//            throw new IllegalArgumentException(
//                    "Invalid Layer Save Flag - only ALL_SAVE_FLAGS is allowed");
//        }
//    }
//
//    public int save() {
//        return nSave(mNativeCanvasWrapper, MATRIX_SAVE_FLAG | CLIP_SAVE_FLAG);
//    }
//
//
//    public int save(@Saveflags int saveFlags) {
//        return nSave(mNativeCanvasWrapper, saveFlags);
//    }
//
//    public int saveLayer(@Nullable RectF bounds, @Nullable Paint paint, @Saveflags int saveFlags) {
//        if (bounds == null) {
//            bounds = new RectF(getClipBounds());
//        }
//        checkValidSaveFlags(saveFlags);
//        return saveLayer(bounds.left, bounds.top, bounds.right, bounds.bottom, paint,
//                ALL_SAVE_FLAG);
//    }
//
//    public int saveLayer(@Nullable RectF bounds, @Nullable Paint paint) {
//        return saveLayer(bounds, paint, ALL_SAVE_FLAG);
//    }
//
//    /**
//     * @hide
//     */
//    public int saveUnclippedLayer(int left, int top, int right, int bottom) {
//        return nSaveUnclippedLayer(mNativeCanvasWrapper, left, top, right, bottom);
//    }
//
//    /**
//     * @hide
//     * @param saveCount The save level to restore to.
//     * @param paint     This is copied and is applied to the area within the unclipped layer's
//     *                  bounds (i.e. equivalent to a drawPaint()) before restore() is called.
//     */
//    public void restoreUnclippedLayer(int saveCount, Paint paint) {
//        nRestoreUnclippedLayer(mNativeCanvasWrapper, saveCount, paint.getNativeInstance());
//    }
//
//    public int saveLayer(float left, float top, float right, float bottom, @Nullable Paint paint,
//                         @Saveflags int saveFlags) {
//        checkValidSaveFlags(saveFlags);
//        return nSaveLayer(mNativeCanvasWrapper, left, top, right, bottom,
//                paint != null ? paint.getNativeInstance() : 0);
//    }
//
//    public int saveLayer(float left, float top, float right, float bottom, @Nullable Paint paint) {
//        return saveLayer(left, top, right, bottom, paint, ALL_SAVE_FLAG);
//    }
//
//    public int saveLayerAlpha(@Nullable RectF bounds, int alpha, @Saveflags int saveFlags) {
//        if (bounds == null) {
//            bounds = new RectF(getClipBounds());
//        }
//        checkValidSaveFlags(saveFlags);
//        return saveLayerAlpha(bounds.left, bounds.top, bounds.right, bounds.bottom, alpha,
//                ALL_SAVE_FLAG);
//    }
//
//    public int saveLayerAlpha(@Nullable RectF bounds, int alpha) {
//        return saveLayerAlpha(bounds, alpha, ALL_SAVE_FLAG);
//    }
//
//    public int saveLayerAlpha(float left, float top, float right, float bottom, int alpha,
//                              @Saveflags int saveFlags) {
//        checkValidSaveFlags(saveFlags);
//        alpha = Math.min(255, Math.max(0, alpha));
//        return nSaveLayerAlpha(mNativeCanvasWrapper, left, top, right, bottom, alpha);
//    }
//
//    /**
//     * Convenience for {@link #saveLayerAlpha(RectF, int)} that takes the four float coordinates of
//     * the bounds rectangle.
//     */
//    public int saveLayerAlpha(float left, float top, float right, float bottom, int alpha) {
//        return saveLayerAlpha(left, top, right, bottom, alpha, ALL_SAVE_FLAG);
//    }
//
//    /**
//     * This call balances a previous call to save(), and is used to remove all
//     * modifications to the matrix/clip state since the last save call. It is
//     * an error to call restore() more times than save() was called.
//     */
//    public void restore() {
//        if (!nRestore(mNativeCanvasWrapper)
//                && (!sCompatibilityRestore || !isHardwareAccelerated())) {
//            throw new IllegalStateException("Underflow in restore - more restores than saves");
//        }
//    }
//
//    public int getSaveCount() {
//        return nGetSaveCount(mNativeCanvasWrapper);
//    }
//
//
//    public void restoreToCount(int saveCount) {
//        if (saveCount < 1) {
//            if (!sCompatibilityRestore || !isHardwareAccelerated()) {
//                // do nothing and throw without restoring
//                throw new IllegalArgumentException(
//                        "Underflow in restoreToCount - more restores than saves");
//            }
//            // compat behavior - restore as far as possible
//            saveCount = 1;
//        }
//        nRestoreToCount(mNativeCanvasWrapper, saveCount);
//    }
//
//    public void translate(float dx, float dy) {
//        if (dx == 0.0f && dy == 0.0f) return;
//        nTranslate(mNativeCanvasWrapper, dx, dy);
//    }
//
//    public void scale(float sx, float sy) {
//        if (sx == 1.0f && sy == 1.0f) return;
//        nScale(mNativeCanvasWrapper, sx, sy);
//    }
//
//    public final void scale(float sx, float sy, float px, float py) {
//        if (sx == 1.0f && sy == 1.0f) return;
//        translate(px, py);
//        scale(sx, sy);
//        translate(-px, -py);
//    }
//
//    /**
//     * Preconcat the current matrix with the specified rotation.
//     *
//     * @param degrees The amount to rotate, in degrees
//     */
//    public void rotate(float degrees) {
//        if (degrees == 0.0f) return;
//        nRotate(mNativeCanvasWrapper, degrees);
//    }
//
//    public final void rotate(float degrees, float px, float py) {
//        if (degrees == 0.0f) return;
//        translate(px, py);
//        rotate(degrees);
//        translate(-px, -py);
//    }
//
//    public void skew(float sx, float sy) {
//        if (sx == 0.0f && sy == 0.0f) return;
//        nSkew(mNativeCanvasWrapper, sx, sy);
//    }
//
//    public void concat(@Nullable Matrix matrix) {
//        if (matrix != null) nConcat(mNativeCanvasWrapper, matrix.ni());
//    }
//
//    public void setMatrix(@Nullable Matrix matrix) {
//        nSetMatrix(mNativeCanvasWrapper,
//                matrix == null ? 0 : matrix.ni());
//    }
//    @Deprecated
//    public void getMatrix(@NonNull Matrix ctm) {
//        nGetMatrix(mNativeCanvasWrapper, ctm.ni());
//    }
//
//    @Deprecated
//    public final @NonNull Matrix getMatrix() {
//        Matrix m = new Matrix();
//        //noinspection deprecation
//        getMatrix(m);
//        return m;
//    }
//
//    private static void checkValidClipOp(@NonNull Region.Op op) {
//        if (sCompatiblityVersion >= Build.VERSION_CODES.P
//                && op != Region.Op.INTERSECT && op != Region.Op.DIFFERENCE) {
//            throw new IllegalArgumentException(
//                    "Invalid Region.Op - only INTERSECT and DIFFERENCE are allowed");
//        }
//    }
//
//    @Deprecated
//    public boolean clipRect(@NonNull RectF rect, @NonNull Region.Op op) {
//        checkValidClipOp(op);
//        return nClipRect(mNativeCanvasWrapper, rect.left, rect.top, rect.right, rect.bottom,
//                op.nativeInt);
//    }
//
//    @Deprecated
//    public boolean clipRect(@NonNull Rect rect, @NonNull Region.Op op) {
//        checkValidClipOp(op);
//        return nClipRect(mNativeCanvasWrapper, rect.left, rect.top, rect.right, rect.bottom,
//                op.nativeInt);
//    }
//
//    public boolean clipRectUnion(@NonNull Rect rect) {
//        return nClipRect(mNativeCanvasWrapper, rect.left, rect.top, rect.right, rect.bottom,
//                Region.Op.UNION.nativeInt);
//    }
//
//    public boolean clipRect(@NonNull RectF rect) {
//        return nClipRect(mNativeCanvasWrapper, rect.left, rect.top, rect.right, rect.bottom,
//                Region.Op.INTERSECT.nativeInt);
//    }
//
//    public boolean clipOutRect(@NonNull RectF rect) {
//        return nClipRect(mNativeCanvasWrapper, rect.left, rect.top, rect.right, rect.bottom,
//                Region.Op.DIFFERENCE.nativeInt);
//    }
//    public boolean clipRect(@NonNull Rect rect) {
//        return nClipRect(mNativeCanvasWrapper, rect.left, rect.top, rect.right, rect.bottom,
//                Region.Op.INTERSECT.nativeInt);
//    }
//
//    public boolean clipOutRect(@NonNull Rect rect) {
//        return nClipRect(mNativeCanvasWrapper, rect.left, rect.top, rect.right, rect.bottom,
//                Region.Op.DIFFERENCE.nativeInt);
//    }
//
//    /**
//     * Modify the current clip with the specified rectangle, which is
//     * expressed in local coordinates.
//     *
//     * @param left   The left side of the rectangle to intersect with the
//     *               current clip
//     * @param top    The top of the rectangle to intersect with the current
//     *               clip
//     * @param right  The right side of the rectangle to intersect with the
//     *               current clip
//     * @param bottom The bottom of the rectangle to intersect with the current
//     *               clip
//     * @param op     How the clip is modified
//     * @return       true if the resulting clip is non-empty
//     *
//     * @deprecated Region.Op values other than {@link Region.Op#INTERSECT} and
//     * {@link Region.Op#DIFFERENCE} have the ability to expand the clip. The canvas clipping APIs
//     * are intended to only expand the clip as a result of a restore operation. This enables a view
//     * parent to clip a canvas to clearly define the maximal drawing area of its children. The
//     * recommended alternative calls are {@link #clipRect(float,float,float,float)} and
//     * {@link #clipOutRect(float,float,float,float)};
//     *
//     * As of API Level API level {@value Build.VERSION_CODES#P} only {@link Region.Op#INTERSECT} and
//     * {@link Region.Op#DIFFERENCE} are valid Region.Op parameters.
//     */
//    @Deprecated
//    public boolean clipRect(float left, float top, float right, float bottom,
//                            @NonNull Region.Op op) {
//        checkValidClipOp(op);
//        return nClipRect(mNativeCanvasWrapper, left, top, right, bottom, op.nativeInt);
//    }
//
//    /**
//     * Intersect the current clip with the specified rectangle, which is
//     * expressed in local coordinates.
//     *
//     * @param left   The left side of the rectangle to intersect with the
//     *               current clip
//     * @param top    The top of the rectangle to intersect with the current clip
//     * @param right  The right side of the rectangle to intersect with the
//     *               current clip
//     * @param bottom The bottom of the rectangle to intersect with the current
//     *               clip
//     * @return       true if the resulting clip is non-empty
//     */
//    public boolean clipRect(float left, float top, float right, float bottom) {
//        return nClipRect(mNativeCanvasWrapper, left, top, right, bottom,
//                Region.Op.INTERSECT.nativeInt);
//    }
//
//    /**
//     * Set the clip to the difference of the current clip and the specified rectangle, which is
//     * expressed in local coordinates.
//     *
//     * @param left   The left side of the rectangle used in the difference operation
//     * @param top    The top of the rectangle used in the difference operation
//     * @param right  The right side of the rectangle used in the difference operation
//     * @param bottom The bottom of the rectangle used in the difference operation
//     * @return       true if the resulting clip is non-empty
//     */
//    public boolean clipOutRect(float left, float top, float right, float bottom) {
//        return nClipRect(mNativeCanvasWrapper, left, top, right, bottom,
//                Region.Op.DIFFERENCE.nativeInt);
//    }
//
//    /**
//     * Intersect the current clip with the specified rectangle, which is
//     * expressed in local coordinates.
//     *
//     * @param left   The left side of the rectangle to intersect with the
//     *               current clip
//     * @param top    The top of the rectangle to intersect with the current clip
//     * @param right  The right side of the rectangle to intersect with the
//     *               current clip
//     * @param bottom The bottom of the rectangle to intersect with the current
//     *               clip
//     * @return       true if the resulting clip is non-empty
//     */
//    public boolean clipRect(int left, int top, int right, int bottom) {
//        return nClipRect(mNativeCanvasWrapper, left, top, right, bottom,
//                Region.Op.INTERSECT.nativeInt);
//    }
//
//    /**
//     * Set the clip to the difference of the current clip and the specified rectangle, which is
//     * expressed in local coordinates.
//     *
//     * @param left   The left side of the rectangle used in the difference operation
//     * @param top    The top of the rectangle used in the difference operation
//     * @param right  The right side of the rectangle used in the difference operation
//     * @param bottom The bottom of the rectangle used in the difference operation
//     * @return       true if the resulting clip is non-empty
//     */
//    public boolean clipOutRect(int left, int top, int right, int bottom) {
//        return nClipRect(mNativeCanvasWrapper, left, top, right, bottom,
//                Region.Op.DIFFERENCE.nativeInt);
//    }
//
//    /**
//     * Modify the current clip with the specified path.
//     *
//     * @param path The path to operate on the current clip
//     * @param op   How the clip is modified
//     * @return     true if the resulting is non-empty
//     *
//     * @deprecated Region.Op values other than {@link Region.Op#INTERSECT} and
//     * {@link Region.Op#DIFFERENCE} have the ability to expand the clip. The canvas clipping APIs
//     * are intended to only expand the clip as a result of a restore operation. This enables a view
//     * parent to clip a canvas to clearly define the maximal drawing area of its children. The
//     * recommended alternative calls are {@link #clipPath(Path)} and
//     * {@link #clipOutPath(Path)};
//     *
//     * As of API Level API level {@value Build.VERSION_CODES#P} only {@link Region.Op#INTERSECT} and
//     * {@link Region.Op#DIFFERENCE} are valid Region.Op parameters.
//     */
//    @Deprecated
//    public boolean clipPath(@NonNull Path path, @NonNull Region.Op op) {
//        checkValidClipOp(op);
//        return nClipPath(mNativeCanvasWrapper, path.readOnlyNI(), op.nativeInt);
//    }
//
//    /**
//     * Intersect the current clip with the specified path.
//     *
//     * @param path The path to intersect with the current clip
//     * @return     true if the resulting clip is non-empty
//     */
//    public boolean clipPath(@NonNull Path path) {
//        return clipPath(path, Region.Op.INTERSECT);
//    }
//
//    /**
//     * Set the clip to the difference of the current clip and the specified path.
//     *
//     * @param path The path used in the difference operation
//     * @return     true if the resulting clip is non-empty
//     */
//    public boolean clipOutPath(@NonNull Path path) {
//        return clipPath(path, Region.Op.DIFFERENCE);
//    }
//
//    /**
//     * Modify the current clip with the specified region. Note that unlike
//     * clipRect() and clipPath() which transform their arguments by the
//     * current matrix, clipRegion() assumes its argument is already in the
//     * coordinate system of the current layer's bitmap, and so not
//     * transformation is performed.
//     *
//     * @param region The region to operate on the current clip, based on op
//     * @param op How the clip is modified
//     * @return true if the resulting is non-empty
//     *
//     * @removed
//     * @deprecated Unlike all other clip calls this API does not respect the
//     *             current matrix. Use {@link #clipRect(Rect)} as an alternative.
//     */
//    @Deprecated
//    public boolean clipRegion(@NonNull Region region, @NonNull Region.Op op) {
//        return false;
//    }
//
//    /**
//     * Intersect the current clip with the specified region. Note that unlike
//     * clipRect() and clipPath() which transform their arguments by the
//     * current matrix, clipRegion() assumes its argument is already in the
//     * coordinate system of the current layer's bitmap, and so not
//     * transformation is performed.
//     *
//     * @param region The region to operate on the current clip, based on op
//     * @return true if the resulting is non-empty
//     *
//     * @removed
//     * @deprecated Unlike all other clip calls this API does not respect the
//     *             current matrix. Use {@link #clipRect(Rect)} as an alternative.
//     */
//    @Deprecated
//    public boolean clipRegion(@NonNull Region region) {
//        return false;
//    }
//
//    public @Nullable DrawFilter getDrawFilter() {
//        return mDrawFilter;
//    }
//
//    public void setDrawFilter(@Nullable DrawFilter filter) {
//        long nativeFilter = 0;
//        if (filter != null) {
//            nativeFilter = filter.mNativeInt;
//        }
//        mDrawFilter = filter;
//        nSetDrawFilter(mNativeCanvasWrapper, nativeFilter);
//    }
//
//    /**
//     * Constant values used as parameters to {@code quickReject()} calls. These values
//     * specify how much space around the shape should be accounted for, depending on whether
//     * the shaped area is antialiased or not.
//     *
//     * @see #quickReject(float, float, float, float, android.graphics.Canvas.EdgeType)
//     * @see #quickReject(Path, android.graphics.Canvas.EdgeType)
//     * @see #quickReject(RectF, android.graphics.Canvas.EdgeType)
//     * @deprecated quickReject no longer uses this.
//     */
//    public enum EdgeType {
//
//        /**
//         * Black-and-White: Treat edges by just rounding to nearest pixel boundary
//         */
//        BW,
//
//        /**
//         * Antialiased: Treat edges by rounding-out, since they may be antialiased
//         */
//        AA;
//    }
//
//    /**
//     * Return true if the specified rectangle, after being transformed by the
//     * current matrix, would lie completely outside of the current clip. Call
//     * this to check if an area you intend to draw into is clipped out (and
//     * therefore you can skip making the draw calls).
//     *
//     * @param rect  the rect to compare with the current clip
//     * @param type  {@link android.graphics.Canvas.EdgeType#AA} if the path should be considered antialiased,
//     *              since that means it may affect a larger area (more pixels) than
//     *              non-antialiased ({@link android.graphics.Canvas.EdgeType#BW}).
//     * @return      true if the rect (transformed by the canvas' matrix)
//     *              does not intersect with the canvas' clip
//     * @deprecated The EdgeType is ignored. Use {@link #quickReject(RectF)} instead.
//     */
//    @Deprecated
//    public boolean quickReject(@NonNull RectF rect, @NonNull android.graphics.Canvas.EdgeType type) {
//        return nQuickReject(mNativeCanvasWrapper,
//                rect.left, rect.top, rect.right, rect.bottom);
//    }
//
//    /**
//     * Return true if the specified rectangle, after being transformed by the
//     * current matrix, would lie completely outside of the current clip. Call
//     * this to check if an area you intend to draw into is clipped out (and
//     * therefore you can skip making the draw calls).
//     *
//     * @param rect  the rect to compare with the current clip
//     * @return      true if the rect (transformed by the canvas' matrix)
//     *              does not intersect with the canvas' clip
//     */
//    public boolean quickReject(@NonNull RectF rect) {
//        return nQuickReject(mNativeCanvasWrapper,
//                rect.left, rect.top, rect.right, rect.bottom);
//    }
//
//    /**
//     * Return true if the specified path, after being transformed by the
//     * current matrix, would lie completely outside of the current clip. Call
//     * this to check if an area you intend to draw into is clipped out (and
//     * therefore you can skip making the draw calls). Note: for speed it may
//     * return false even if the path itself might not intersect the clip
//     * (i.e. the bounds of the path intersects, but the path does not).
//     *
//     * @param path        The path to compare with the current clip
//     * @param type        {@link android.graphics.Canvas.EdgeType#AA} if the path should be considered antialiased,
//     *                    since that means it may affect a larger area (more pixels) than
//     *                    non-antialiased ({@link android.graphics.Canvas.EdgeType#BW}).
//     * @return            true if the path (transformed by the canvas' matrix)
//     *                    does not intersect with the canvas' clip
//     * @deprecated The EdgeType is ignored. Use {@link #quickReject(Path)} instead.
//     */
//    @Deprecated
//    public boolean quickReject(@NonNull Path path, @NonNull android.graphics.Canvas.EdgeType type) {
//        return nQuickReject(mNativeCanvasWrapper, path.readOnlyNI());
//    }
//
//    /**
//     * Return true if the specified path, after being transformed by the
//     * current matrix, would lie completely outside of the current clip. Call
//     * this to check if an area you intend to draw into is clipped out (and
//     * therefore you can skip making the draw calls). Note: for speed it may
//     * return false even if the path itself might not intersect the clip
//     * (i.e. the bounds of the path intersects, but the path does not).
//     *
//     * @param path        The path to compare with the current clip
//     * @return            true if the path (transformed by the canvas' matrix)
//     *                    does not intersect with the canvas' clip
//     */
//    public boolean quickReject(@NonNull Path path) {
//        return nQuickReject(mNativeCanvasWrapper, path.readOnlyNI());
//    }
//
//    /**
//     * Return true if the specified rectangle, after being transformed by the
//     * current matrix, would lie completely outside of the current clip. Call
//     * this to check if an area you intend to draw into is clipped out (and
//     * therefore you can skip making the draw calls).
//     *
//     * @param left        The left side of the rectangle to compare with the
//     *                    current clip
//     * @param top         The top of the rectangle to compare with the current
//     *                    clip
//     * @param right       The right side of the rectangle to compare with the
//     *                    current clip
//     * @param bottom      The bottom of the rectangle to compare with the
//     *                    current clip
//     * @param type        {@link android.graphics.Canvas.EdgeType#AA} if the path should be considered antialiased,
//     *                    since that means it may affect a larger area (more pixels) than
//     *                    non-antialiased ({@link android.graphics.Canvas.EdgeType#BW}).
//     * @return            true if the rect (transformed by the canvas' matrix)
//     *                    does not intersect with the canvas' clip
//     * @deprecated The EdgeType is ignored. Use {@link #quickReject(float, float, float, float)}
//     *             instead.
//     */
//    @Deprecated
//    public boolean quickReject(float left, float top, float right, float bottom,
//                               @NonNull android.graphics.Canvas.EdgeType type) {
//        return nQuickReject(mNativeCanvasWrapper, left, top, right, bottom);
//    }
//
//    /**
//     * Return true if the specified rectangle, after being transformed by the
//     * current matrix, would lie completely outside of the current clip. Call
//     * this to check if an area you intend to draw into is clipped out (and
//     * therefore you can skip making the draw calls).
//     *
//     * @param left        The left side of the rectangle to compare with the
//     *                    current clip
//     * @param top         The top of the rectangle to compare with the current
//     *                    clip
//     * @param right       The right side of the rectangle to compare with the
//     *                    current clip
//     * @param bottom      The bottom of the rectangle to compare with the
//     *                    current clip
//     * @return            true if the rect (transformed by the canvas' matrix)
//     *                    does not intersect with the canvas' clip
//     */
//    public boolean quickReject(float left, float top, float right, float bottom) {
//        return nQuickReject(mNativeCanvasWrapper, left, top, right, bottom);
//    }
//
//    /**
//     * Return the bounds of the current clip (in local coordinates) in the
//     * bounds parameter, and return true if it is non-empty. This can be useful
//     * in a way similar to quickReject, in that it tells you that drawing
//     * outside of these bounds will be clipped out.
//     *
//     * @param bounds Return the clip bounds here.
//     * @return true if the current clip is non-empty.
//     */
//    public boolean getClipBounds(@NonNull Rect bounds) {
//        return nGetClipBounds(mNativeCanvasWrapper, bounds);
//    }
//
//    /**
//     * Retrieve the bounds of the current clip (in local coordinates).
//     *
//     * @return the clip bounds, or [0, 0, 0, 0] if the clip is empty.
//     */
//    public final @NonNull Rect getClipBounds() {
//        Rect r = new Rect();
//        getClipBounds(r);
//        return r;
//    }
//
//    /**
//     * Save the canvas state, draw the picture, and restore the canvas state.
//     * This differs from picture.draw(canvas), which does not perform any
//     * save/restore.
//     *
//     * <p>
//     * <strong>Note:</strong> This forces the picture to internally call
//     * {@link Picture#endRecording} in order to prepare for playback.
//     *
//     * @param picture  The picture to be drawn
//     */
//    public void drawPicture(@NonNull Picture picture) {
//        picture.endRecording();
//        int restoreCount = save();
//        picture.draw(this);
//        restoreToCount(restoreCount);
//    }
//
//    /**
//     * Draw the picture, stretched to fit into the dst rectangle.
//     */
//    public void drawPicture(@NonNull Picture picture, @NonNull RectF dst) {
//        save();
//        translate(dst.left, dst.top);
//        if (picture.getWidth() > 0 && picture.getHeight() > 0) {
//            scale(dst.width() / picture.getWidth(), dst.height() / picture.getHeight());
//        }
//        drawPicture(picture);
//        restore();
//    }
//
//    /**
//     * Draw the picture, stretched to fit into the dst rectangle.
//     */
//    public void drawPicture(@NonNull Picture picture, @NonNull Rect dst) {
//        save();
//        translate(dst.left, dst.top);
//        if (picture.getWidth() > 0 && picture.getHeight() > 0) {
//            scale((float) dst.width() / picture.getWidth(),
//                    (float) dst.height() / picture.getHeight());
//        }
//        drawPicture(picture);
//        restore();
//    }
//
//    public enum VertexMode {
//        TRIANGLES(0),
//        TRIANGLE_STRIP(1),
//        TRIANGLE_FAN(2);
//
//        VertexMode(int nativeInt) {
//            this.nativeInt = nativeInt;
//        }
//
//        /*package*/ final int nativeInt;
//    }
//
//    /**
//     * Releases the resources associated with this canvas.
//     *
//     * @hide
//     */
//    @UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)
//    public void release() {
//        mNativeCanvasWrapper = 0;
//        if (mFinalizer != null) {
//            mFinalizer.run();
//            mFinalizer = null;
//        }
//    }
//
//    /**
//     * Free up as much memory as possible from private caches (e.g. fonts, images)
//     *
//     * @hide
//     */
//    @UnsupportedAppUsage
//    public static void freeCaches() {
//        nFreeCaches();
//    }
//
//    /**
//     * Free up text layout caches
//     *
//     * @hide
//     */
//    @UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)
//    public static void freeTextLayoutCaches() {
//        nFreeTextLayoutCaches();
//    }
//
//    /*package*/ static void setCompatibilityVersion(int apiLevel) {
//        sCompatiblityVersion = apiLevel;
//        sCompatibilityRestore = apiLevel < Build.VERSION_CODES.M;
//        sCompatibilitySetBitmap = apiLevel < Build.VERSION_CODES.O;
//        nSetCompatibilityVersion(apiLevel);
//    }
//
//    private static native void nFreeCaches();
//    private static native void nFreeTextLayoutCaches();
//    private static native long nGetNativeFinalizer();
//    private static native void nSetCompatibilityVersion(int apiLevel);
//
//    // ---------------- @FastNative -------------------
//
//    @FastNative
//    private static native long nInitRaster(long bitmapHandle);
//
//    @FastNative
//    private static native void nSetBitmap(long canvasHandle, long bitmapHandle);
//
//    @FastNative
//    private static native boolean nGetClipBounds(long nativeCanvas, Rect bounds);
//
//    // ---------------- @CriticalNative -------------------
//
//    @CriticalNative
//    private static native boolean nIsOpaque(long canvasHandle);
//    @CriticalNative
//    private static native int nGetWidth(long canvasHandle);
//    @CriticalNative
//    private static native int nGetHeight(long canvasHandle);
//
//    @CriticalNative
//    private static native int nSave(long canvasHandle, int saveFlags);
//    @CriticalNative
//    private static native int nSaveLayer(long nativeCanvas, float l, float t, float r, float b,
//                                         long nativePaint);
//    @CriticalNative
//    private static native int nSaveLayerAlpha(long nativeCanvas, float l, float t, float r, float b,
//                                              int alpha);
//    @CriticalNative
//    private static native int nSaveUnclippedLayer(long nativeCanvas, int l, int t, int r, int b);
//    @CriticalNative
//    private static native void nRestoreUnclippedLayer(long nativeCanvas, int saveCount,
//                                                      long nativePaint);
//    @CriticalNative
//    private static native boolean nRestore(long canvasHandle);
//    @CriticalNative
//    private static native void nRestoreToCount(long canvasHandle, int saveCount);
//    @CriticalNative
//    private static native int nGetSaveCount(long canvasHandle);
//
//    @CriticalNative
//    private static native void nTranslate(long canvasHandle, float dx, float dy);
//    @CriticalNative
//    private static native void nScale(long canvasHandle, float sx, float sy);
//    @CriticalNative
//    private static native void nRotate(long canvasHandle, float degrees);
//    @CriticalNative
//    private static native void nSkew(long canvasHandle, float sx, float sy);
//    @CriticalNative
//    private static native void nConcat(long nativeCanvas, long nativeMatrix);
//    @CriticalNative
//    private static native void nSetMatrix(long nativeCanvas, long nativeMatrix);
//    @CriticalNative
//    private static native boolean nClipRect(long nativeCanvas,
//                                            float left, float top, float right, float bottom, int regionOp);
//    @CriticalNative
//    private static native boolean nClipPath(long nativeCanvas, long nativePath, int regionOp);
//    @CriticalNative
//    private static native void nSetDrawFilter(long nativeCanvas, long nativeFilter);
//    @CriticalNative
//    private static native void nGetMatrix(long nativeCanvas, long nativeMatrix);
//    @CriticalNative
//    private static native boolean nQuickReject(long nativeCanvas, long nativePath);
//    @CriticalNative
//    private static native boolean nQuickReject(long nativeCanvas, float left, float top,
//                                               float right, float bottom);
//
//
//    // ---------------- Draw Methods -------------------
//
//    /**
//     * <p>
//     * Draw the specified arc, which will be scaled to fit inside the specified oval.
//     * </p>
//     * <p>
//     * If the start angle is negative or >= 360, the start angle is treated as start angle modulo
//     * 360.
//     * </p>
//     * <p>
//     * If the sweep angle is >= 360, then the oval is drawn completely. Note that this differs
//     * slightly from SkPath::arcTo, which treats the sweep angle modulo 360. If the sweep angle is
//     * negative, the sweep angle is treated as sweep angle modulo 360
//     * </p>
//     * <p>
//     * The arc is drawn clockwise. An angle of 0 degrees correspond to the geometric angle of 0
//     * degrees (3 o'clock on a watch.)
//     * </p>
//     *
//     * @param oval The bounds of oval used to define the shape and size of the arc
//     * @param startAngle Starting angle (in degrees) where the arc begins
//     * @param sweepAngle Sweep angle (in degrees) measured clockwise
//     * @param useCenter If true, include the center of the oval in the arc, and close it if it is
//     *            being stroked. This will draw a wedge
//     * @param paint The paint used to draw the arc
//     */
//    public void drawArc(@NonNull RectF oval, float startAngle, float sweepAngle, boolean useCenter,
//                        @NonNull Paint paint) {
//        super.drawArc(oval, startAngle, sweepAngle, useCenter, paint);
//    }
//
//    /**
//     * <p>
//     * Draw the specified arc, which will be scaled to fit inside the specified oval.
//     * </p>
//     * <p>
//     * If the start angle is negative or >= 360, the start angle is treated as start angle modulo
//     * 360.
//     * </p>
//     * <p>
//     * If the sweep angle is >= 360, then the oval is drawn completely. Note that this differs
//     * slightly from SkPath::arcTo, which treats the sweep angle modulo 360. If the sweep angle is
//     * negative, the sweep angle is treated as sweep angle modulo 360
//     * </p>
//     * <p>
//     * The arc is drawn clockwise. An angle of 0 degrees correspond to the geometric angle of 0
//     * degrees (3 o'clock on a watch.)
//     * </p>
//     *
//     * @param startAngle Starting angle (in degrees) where the arc begins
//     * @param sweepAngle Sweep angle (in degrees) measured clockwise
//     * @param useCenter If true, include the center of the oval in the arc, and close it if it is
//     *            being stroked. This will draw a wedge
//     * @param paint The paint used to draw the arc
//     */
//    public void drawArc(float left, float top, float right, float bottom, float startAngle,
//                        float sweepAngle, boolean useCenter, @NonNull Paint paint) {
//        super.drawArc(left, top, right, bottom, startAngle, sweepAngle, useCenter, paint);
//    }
//
//    /**
//     * Fill the entire canvas' bitmap (restricted to the current clip) with the specified ARGB
//     * color, using srcover porterduff mode.
//     *
//     * @param a alpha component (0..255) of the color to draw onto the canvas
//     * @param r red component (0..255) of the color to draw onto the canvas
//     * @param g green component (0..255) of the color to draw onto the canvas
//     * @param b blue component (0..255) of the color to draw onto the canvas
//     */
//    public void drawARGB(int a, int r, int g, int b) {
//        super.drawARGB(a, r, g, b);
//    }
//
//    /**
//     * Draw the specified bitmap, with its top/left corner at (x,y), using the specified paint,
//     * transformed by the current matrix.
//     * <p>
//     * Note: if the paint contains a maskfilter that generates a mask which extends beyond the
//     * bitmap's original width/height (e.g. BlurMaskFilter), then the bitmap will be drawn as if it
//     * were in a Shader with CLAMP mode. Thus the color outside of the original width/height will be
//     * the edge color replicated.
//     * <p>
//     * If the bitmap and canvas have different densities, this function will take care of
//     * automatically scaling the bitmap to draw at the same density as the canvas.
//     *
//     * @param bitmap The bitmap to be drawn
//     * @param left The position of the left side of the bitmap being drawn
//     * @param top The position of the top side of the bitmap being drawn
//     * @param paint The paint used to draw the bitmap (may be null)
//     */
//    public void drawBitmap(@NonNull Bitmap bitmap, float left, float top, @Nullable Paint paint) {
//        super.drawBitmap(bitmap, left, top, paint);
//    }
//
//    /**
//     * Draw the specified bitmap, scaling/translating automatically to fill the destination
//     * rectangle. If the source rectangle is not null, it specifies the subset of the bitmap to
//     * draw.
//     * <p>
//     * Note: if the paint contains a maskfilter that generates a mask which extends beyond the
//     * bitmap's original width/height (e.g. BlurMaskFilter), then the bitmap will be drawn as if it
//     * were in a Shader with CLAMP mode. Thus the color outside of the original width/height will be
//     * the edge color replicated.
//     * <p>
//     * This function <em>ignores the density associated with the bitmap</em>. This is because the
//     * source and destination rectangle coordinate spaces are in their respective densities, so must
//     * already have the appropriate scaling factor applied.
//     *
//     * @param bitmap The bitmap to be drawn
//     * @param src May be null. The subset of the bitmap to be drawn
//     * @param dst The rectangle that the bitmap will be scaled/translated to fit into
//     * @param paint May be null. The paint used to draw the bitmap
//     */
//    public void drawBitmap(@NonNull Bitmap bitmap, @Nullable Rect src, @NonNull RectF dst,
//                           @Nullable Paint paint) {
//        super.drawBitmap(bitmap, src, dst, paint);
//    }
//
//    /**
//     * Draw the specified bitmap, scaling/translating automatically to fill the destination
//     * rectangle. If the source rectangle is not null, it specifies the subset of the bitmap to
//     * draw.
//     * <p>
//     * Note: if the paint contains a maskfilter that generates a mask which extends beyond the
//     * bitmap's original width/height (e.g. BlurMaskFilter), then the bitmap will be drawn as if it
//     * were in a Shader with CLAMP mode. Thus the color outside of the original width/height will be
//     * the edge color replicated.
//     * <p>
//     * This function <em>ignores the density associated with the bitmap</em>. This is because the
//     * source and destination rectangle coordinate spaces are in their respective densities, so must
//     * already have the appropriate scaling factor applied.
//     *
//     * @param bitmap The bitmap to be drawn
//     * @param src May be null. The subset of the bitmap to be drawn
//     * @param dst The rectangle that the bitmap will be scaled/translated to fit into
//     * @param paint May be null. The paint used to draw the bitmap
//     */
//    public void drawBitmap(@NonNull Bitmap bitmap, @Nullable Rect src, @NonNull Rect dst,
//                           @Nullable Paint paint) {
//        super.drawBitmap(bitmap, src, dst, paint);
//    }
//
//    /**
//     * Treat the specified array of colors as a bitmap, and draw it. This gives the same result as
//     * first creating a bitmap from the array, and then drawing it, but this method avoids
//     * explicitly creating a bitmap object which can be more efficient if the colors are changing
//     * often.
//     *
//     * @param colors Array of colors representing the pixels of the bitmap
//     * @param offset Offset into the array of colors for the first pixel
//     * @param stride The number of colors in the array between rows (must be >= width or <= -width).
//     * @param x The X coordinate for where to draw the bitmap
//     * @param y The Y coordinate for where to draw the bitmap
//     * @param width The width of the bitmap
//     * @param height The height of the bitmap
//     * @param hasAlpha True if the alpha channel of the colors contains valid values. If false, the
//     *            alpha byte is ignored (assumed to be 0xFF for every pixel).
//     * @param paint May be null. The paint used to draw the bitmap
//     * @deprecated Usage with a {@link #isHardwareAccelerated() hardware accelerated} canvas
//     *             requires an internal copy of color buffer contents every time this method is
//     *             called. Using a Bitmap avoids this copy, and allows the application to more
//     *             explicitly control the lifetime and copies of pixel data.
//     */
//    @Deprecated
//    public void drawBitmap(@NonNull int[] colors, int offset, int stride, float x, float y,
//                           int width, int height, boolean hasAlpha, @Nullable Paint paint) {
//        super.drawBitmap(colors, offset, stride, x, y, width, height, hasAlpha, paint);
//    }
//
//    /**
//     * Legacy version of drawBitmap(int[] colors, ...) that took ints for x,y
//     *
//     * @deprecated Usage with a {@link #isHardwareAccelerated() hardware accelerated} canvas
//     *             requires an internal copy of color buffer contents every time this method is
//     *             called. Using a Bitmap avoids this copy, and allows the application to more
//     *             explicitly control the lifetime and copies of pixel data.
//     */
//    @Deprecated
//    public void drawBitmap(@NonNull int[] colors, int offset, int stride, int x, int y,
//                           int width, int height, boolean hasAlpha, @Nullable Paint paint) {
//        super.drawBitmap(colors, offset, stride, x, y, width, height, hasAlpha, paint);
//    }
//
//    /**
//     * Draw the bitmap using the specified matrix.
//     *
//     * @param bitmap The bitmap to draw
//     * @param matrix The matrix used to transform the bitmap when it is drawn
//     * @param paint May be null. The paint used to draw the bitmap
//     */
//    public void drawBitmap(@NonNull Bitmap bitmap, @NonNull Matrix matrix, @Nullable Paint paint) {
//        super.drawBitmap(bitmap, matrix, paint);
//    }
//
//    public void drawBitmapMesh(@NonNull Bitmap bitmap, int meshWidth, int meshHeight,
//                               @NonNull float[] verts, int vertOffset, @Nullable int[] colors, int colorOffset,
//                               @Nullable Paint paint) {
//        super.drawBitmapMesh(bitmap, meshWidth, meshHeight, verts, vertOffset, colors, colorOffset,
//                paint);
//    }
//
//    public void drawCircle(float cx, float cy, float radius, @NonNull Paint paint) {
//        super.drawCircle(cx, cy, radius, paint);
//    }
//
//    public void drawColor(@ColorInt int color) {
//        super.drawColor(color);
//    }
//
//    public void drawColor(@ColorLong long color) {
//        super.drawColor(color, BlendMode.SRC_OVER);
//    }
//
//    public void drawColor(@ColorInt int color, @NonNull PorterDuff.Mode mode) {
//        super.drawColor(color, mode);
//    }
//
//    public void drawColor(@ColorInt int color, @NonNull BlendMode mode) {
//        super.drawColor(color, mode);
//    }
//
//    public void drawColor(@ColorLong long color, @NonNull BlendMode mode) {
//        super.drawColor(color, mode);
//    }
//
//    public void drawLine(float startX, float startY, float stopX, float stopY,
//                         @NonNull Paint paint) {
//        super.drawLine(startX, startY, stopX, stopY, paint);
//    }
//
//    public void drawLines(@Size(multiple = 4) @NonNull float[] pts, int offset, int count,
//                          @NonNull Paint paint) {
//        super.drawLines(pts, offset, count, paint);
//    }
//
//    public void drawLines(@Size(multiple = 4) @NonNull float[] pts, @NonNull Paint paint) {
//        super.drawLines(pts, paint);
//    }
//    public void drawOval(@NonNull RectF oval, @NonNull Paint paint) {
//        super.drawOval(oval, paint);
//    }
//
//    public void drawOval(float left, float top, float right, float bottom, @NonNull Paint paint) {
//        super.drawOval(left, top, right, bottom, paint);
//    }
//    public void drawPaint(@NonNull Paint paint) {
//        super.drawPaint(paint);
//    }
//
//    public void drawPatch(@NonNull NinePatch patch, @NonNull Rect dst, @Nullable Paint paint) {
//        super.drawPatch(patch, dst, paint);
//    }
//    public void drawPatch(@NonNull NinePatch patch, @NonNull RectF dst, @Nullable Paint paint) {
//        super.drawPatch(patch, dst, paint);
//
//    public void drawPath(@NonNull Path path, @NonNull Paint paint) {
//        super.drawPath(path, paint);
//    }
//
//    public void drawPoint(float x, float y, @NonNull Paint paint) {
//        super.drawPoint(x, y, paint);
//    }
//
//    public void drawPoints(@Size(multiple = 2) float[] pts, int offset, int count,
//                           @NonNull Paint paint) {
//        super.drawPoints(pts, offset, count, paint);
//    }
//
//    public void drawPoints(@Size(multiple = 2) @NonNull float[] pts, @NonNull Paint paint) {
//        super.drawPoints(pts, paint);
//    }
//
//    @Deprecated
//    public void drawPosText(@NonNull char[] text, int index, int count,
//                            @NonNull @Size(multiple = 2) float[] pos,
//                            @NonNull Paint paint) {
//        super.drawPosText(text, index, count, pos, paint);
//    }
//
//    @Deprecated
//    public void drawPosText(@NonNull String text, @NonNull @Size(multiple = 2) float[] pos,
//                            @NonNull Paint paint) {
//        super.drawPosText(text, pos, paint);
//    }
//
//    public void drawRect(@NonNull RectF rect, @NonNull Paint paint) {
//        super.drawRect(rect, paint);
//    }
//
//    public void drawRect(@NonNull Rect r, @NonNull Paint paint) {
//        super.drawRect(r, paint);
//    }
//
//
//    public void drawRect(float left, float top, float right, float bottom, @NonNull Paint paint) {
//        super.drawRect(left, top, right, bottom, paint);
//    }
//
//    public void drawRGB(int r, int g, int b) {
//        super.drawRGB(r, g, b);
//    }
//
//    public void drawRoundRect(@NonNull RectF rect, float rx, float ry, @NonNull Paint paint) {
//        super.drawRoundRect(rect, rx, ry, paint);
//    }
//
//    public void drawRoundRect(float left, float top, float right, float bottom, float rx, float ry,
//                              @NonNull Paint paint) {
//        super.drawRoundRect(left, top, right, bottom, rx, ry, paint);
//    }
//
//
//    @Override
//    public void drawDoubleRoundRect(@NonNull RectF outer, float outerRx, float outerRy,
//                                    @NonNull RectF inner, float innerRx, float innerRy, @NonNull Paint paint) {
//        super.drawDoubleRoundRect(outer, outerRx, outerRy, inner, innerRx, innerRy, paint);
//    }
//
//    @Override
//    public void drawDoubleRoundRect(@NonNull RectF outer, @NonNull float[] outerRadii,
//                                    @NonNull RectF inner, @NonNull float[] innerRadii, @NonNull Paint paint) {
//        super.drawDoubleRoundRect(outer, outerRadii, inner, innerRadii, paint);
//    }
//    public void drawGlyphs(
//            @NonNull int[] glyphIds,
//            @IntRange(from = 0) int glyphIdOffset,
//            @NonNull float[] positions,
//            @IntRange(from = 0) int positionOffset,
//            @IntRange(from = 0) int glyphCount,
//            @NonNull Font font,
//            @NonNull Paint paint) {
//        super.drawGlyphs(glyphIds, glyphIdOffset, positions, positionOffset, glyphCount, font,
//                paint);
//    }
//    public void drawText(@NonNull char[] text, int index, int count, float x, float y,
//                         @NonNull Paint paint) {
//        super.drawText(text, index, count, x, y, paint);
//    }
//
//    public void drawText(@NonNull String text, float x, float y, @NonNull Paint paint) {
//        super.drawText(text, x, y, paint);
//    }
//    public void drawText(@NonNull String text, int start, int end, float x, float y,
//                         @NonNull Paint paint) {
//        super.drawText(text, start, end, x, y, paint);
//    }
//    public void drawText(@NonNull CharSequence text, int start, int end, float x, float y,
//                         @NonNull Paint paint) {
//        super.drawText(text, start, end, x, y, paint);
//    }
//
//    public void drawTextOnPath(@NonNull char[] text, int index, int count, @NonNull Path path,
//                               float hOffset, float vOffset, @NonNull Paint paint) {
//        super.drawTextOnPath(text, index, count, path, hOffset, vOffset, paint);
//    }
//    public void drawTextOnPath(@NonNull String text, @NonNull Path path, float hOffset,
//                               float vOffset, @NonNull Paint paint) {
//        super.drawTextOnPath(text, path, hOffset, vOffset, paint);
//    }
//    public void drawTextRun(@NonNull char[] text, int index, int count, int contextIndex,
//                            int contextCount, float x, float y, boolean isRtl, @NonNull Paint paint) {
//        super.drawTextRun(text, index, count, contextIndex, contextCount, x, y, isRtl, paint);
//    }
//    public void drawTextRun(@NonNull CharSequence text, int start, int end, int contextStart,
//                            int contextEnd, float x, float y, boolean isRtl, @NonNull Paint paint) {
//        super.drawTextRun(text, start, end, contextStart, contextEnd, x, y, isRtl, paint);
//    }
//
//    public void drawTextRun(@NonNull MeasuredText text, int start, int end, int contextStart,
//                            int contextEnd, float x, float y, boolean isRtl, @NonNull Paint paint) {
//        super.drawTextRun(text, start, end, contextStart, contextEnd, x, y, isRtl, paint);
//    }
//
//    public void drawVertices(@NonNull android.graphics.Canvas.VertexMode mode, int vertexCount, @NonNull float[] verts,
//                             int vertOffset, @Nullable float[] texs, int texOffset, @Nullable int[] colors,
//                             int colorOffset, @Nullable short[] indices, int indexOffset, int indexCount,
//                             @NonNull Paint paint) {
//        super.drawVertices(mode, vertexCount, verts, vertOffset, texs, texOffset,
//                colors, colorOffset, indices, indexOffset, indexCount, paint);
//    }
//
//    public void drawRenderNode(@NonNull RenderNode renderNode) {
//        throw new IllegalArgumentException("Software rendering doesn't support drawRenderNode");
//    }
//}
