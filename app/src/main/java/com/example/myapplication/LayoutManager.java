//package com.example.myapplication;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.Matrix;
//import android.graphics.Rect;
//import android.graphics.RectF;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.FocusFinder;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewParent;
//import android.view.accessibility.AccessibilityEvent;
//
//import androidx.annotation.CallSuper;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.annotation.Px;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
//import androidx.recyclerview.widget.ChildHelper;
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.GapWorker;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.ViewBoundsCheck;
//
//import java.util.ArrayList;
//
///**
// * A <code>LayoutManager</code> is responsible for measuring and positioning item views
// * within a <code>RecyclerView</code> as well as determining the policy for when to recycle
// * item views that are no longer visible to the user. By changing the <code>LayoutManager</code>
// * a <code>RecyclerView</code> can be used to implement a standard vertically scrolling list,
// * a uniform grid, staggered grids, horizontally scrolling collections and more. Several stock
// * layout managers are provided for general use.
// * <p/>
// * If the LayoutManager specifies a default constructor or one with the signature
// * ({@link Context}, {@link AttributeSet}, {@code int}, {@code int}), RecyclerView will
// * instantiate and set the LayoutManager when being inflated. Most used properties can
// * be then obtained from {@link #getProperties(Context, AttributeSet, int, int)}. In case
// * a LayoutManager specifies both constructors, the non-default constructor will take
// * precedence.
// *
// */
//public abstract static class LayoutManager {
//    ChildHelper mChildHelper;
//    RecyclerView mRecyclerView;
//
//    /**
//     * The callback used for retrieving information about a RecyclerView and its children in the
//     * horizontal direction.
//     */
//    private final ViewBoundsCheck.Callback mHorizontalBoundCheckCallback =
//            new ViewBoundsCheck.Callback() {
//                @Override
//                public View getChildAt(int index) {
//                    return RecyclerView.LayoutManager.this.getChildAt(index);
//                }
//
//                @Override
//                public int getParentStart() {
//                    return RecyclerView.LayoutManager.this.getPaddingLeft();
//                }
//
//                @Override
//                public int getParentEnd() {
//                    return RecyclerView.LayoutManager.this.getWidth() - RecyclerView.LayoutManager.this.getPaddingRight();
//                }
//
//                @Override
//                public int getChildStart(View view) {
//                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
//                            view.getLayoutParams();
//                    return RecyclerView.LayoutManager.this.getDecoratedLeft(view) - params.leftMargin;
//                }
//
//                @Override
//                public int getChildEnd(View view) {
//                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
//                            view.getLayoutParams();
//                    return RecyclerView.LayoutManager.this.getDecoratedRight(view) + params.rightMargin;
//                }
//            };
//
//    /**
//     * The callback used for retrieving information about a RecyclerView and its children in the
//     * vertical direction.
//     */
//    private final ViewBoundsCheck.Callback mVerticalBoundCheckCallback =
//            new ViewBoundsCheck.Callback() {
//                @Override
//                public View getChildAt(int index) {
//                    return RecyclerView.LayoutManager.this.getChildAt(index);
//                }
//
//                @Override
//                public int getParentStart() {
//                    return RecyclerView.LayoutManager.this.getPaddingTop();
//                }
//
//                @Override
//                public int getParentEnd() {
//                    return RecyclerView.LayoutManager.this.getHeight()
//                            - RecyclerView.LayoutManager.this.getPaddingBottom();
//                }
//
//                @Override
//                public int getChildStart(View view) {
//                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
//                            view.getLayoutParams();
//                    return RecyclerView.LayoutManager.this.getDecoratedTop(view) - params.topMargin;
//                }
//
//                @Override
//                public int getChildEnd(View view) {
//                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
//                            view.getLayoutParams();
//                    return RecyclerView.LayoutManager.this.getDecoratedBottom(view) + params.bottomMargin;
//                }
//            };
//
//    ViewBoundsCheck mHorizontalBoundCheck = new ViewBoundsCheck(mHorizontalBoundCheckCallback);
//    ViewBoundsCheck mVerticalBoundCheck = new ViewBoundsCheck(mVerticalBoundCheckCallback);
//
//    @Nullable
//    RecyclerView.SmoothScroller mSmoothScroller;
//
//    boolean mRequestedSimpleAnimations = false;
//
//    boolean mIsAttachedToWindow = false;
//
//    boolean mAutoMeasure = false;
//
//    private boolean mMeasurementCacheEnabled = true;
//
//    private boolean mItemPrefetchEnabled = true;
//
//
//    int mPrefetchMaxCountObserved;
//
//    /**
//     * If true, mPrefetchMaxCountObserved is only valid until next layout, and should be reset.
//     */
//    boolean mPrefetchMaxObservedInInitialPrefetch;
//
//
//    private int mWidthMode, mHeightMode;
//    private int mWidth, mHeight;
//
//
//    public interface LayoutPrefetchRegistry {
//
//        void addPosition(int layoutPosition, int pixelDistance);
//    }
//
//    void setRecyclerView(RecyclerView recyclerView) {
//        if (recyclerView == null) {
//            mRecyclerView = null;
//            mChildHelper = null;
//            mWidth = 0;
//            mHeight = 0;
//        } else {
//            mRecyclerView = recyclerView;
//            mChildHelper = recyclerView.mChildHelper;
//            mWidth = recyclerView.getWidth();
//            mHeight = recyclerView.getHeight();
//        }
//        mWidthMode = View.MeasureSpec.EXACTLY;
//        mHeightMode = View.MeasureSpec.EXACTLY;
//    }
//
//    void setMeasureSpecs(int wSpec, int hSpec) {
//        mWidth = View.MeasureSpec.getSize(wSpec);
//        mWidthMode = View.MeasureSpec.getMode(wSpec);
//        if (mWidthMode == View.MeasureSpec.UNSPECIFIED && !ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
//            mWidth = 0;
//        }
//
//        mHeight = View.MeasureSpec.getSize(hSpec);
//        mHeightMode = View.MeasureSpec.getMode(hSpec);
//        if (mHeightMode == View.MeasureSpec.UNSPECIFIED && !ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
//            mHeight = 0;
//        }
//    }
//
//    /**
//     * Called after a layout is calculated during a measure pass when using auto-measure.
//     * <p>
//     * It simply traverses all children to calculate a bounding box then calls
//     * {@link #setMeasuredDimension(Rect, int, int)}. LayoutManagers can override that method
//     * if they need to handle the bounding box differently.
//     * <p>
//     * For example, GridLayoutManager override that method to ensure that even if a column is
//     * empty, the GridLayoutManager still measures wide enough to include it.
//     *
//     * @param widthSpec The widthSpec that was passing into RecyclerView's onMeasure
//     * @param heightSpec The heightSpec that was passing into RecyclerView's onMeasure
//     */
//    void setMeasuredDimensionFromChildren(int widthSpec, int heightSpec) {
//        final int count = getChildCount();
//        if (count == 0) {
//            mRecyclerView.defaultOnMeasure(widthSpec, heightSpec);
//            return;
//        }
//        int minX = Integer.MAX_VALUE;
//        int minY = Integer.MAX_VALUE;
//        int maxX = Integer.MIN_VALUE;
//        int maxY = Integer.MIN_VALUE;
//
//        for (int i = 0; i < count; i++) {
//            View child = getChildAt(i);
//            final Rect bounds = mRecyclerView.mTempRect;
//            getDecoratedBoundsWithMargins(child, bounds);
//            if (bounds.left < minX) {
//                minX = bounds.left;
//            }
//            if (bounds.right > maxX) {
//                maxX = bounds.right;
//            }
//            if (bounds.top < minY) {
//                minY = bounds.top;
//            }
//            if (bounds.bottom > maxY) {
//                maxY = bounds.bottom;
//            }
//        }
//        mRecyclerView.mTempRect.set(minX, minY, maxX, maxY);
//        setMeasuredDimension(mRecyclerView.mTempRect, widthSpec, heightSpec);
//    }
//
//
//    public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
//        int usedWidth = childrenBounds.width() + getPaddingLeft() + getPaddingRight();
//        int usedHeight = childrenBounds.height() + getPaddingTop() + getPaddingBottom();
//        int width = chooseSize(wSpec, usedWidth, getMinimumWidth());
//        int height = chooseSize(hSpec, usedHeight, getMinimumHeight());
//        setMeasuredDimension(width, height);
//    }
//
//    /**
//     * Calls {@code RecyclerView#requestLayout} on the underlying RecyclerView
//     */
//    public void requestLayout() {
//        if (mRecyclerView != null) {
//            mRecyclerView.requestLayout();
//        }
//    }
//
//    public void assertInLayoutOrScroll(String message) {
//        if (mRecyclerView != null) {
//            mRecyclerView.assertInLayoutOrScroll(message);
//        }
//    }
//
//    public static int chooseSize(int spec, int desired, int min) {
//        final int mode = View.MeasureSpec.getMode(spec);
//        final int size = View.MeasureSpec.getSize(spec);
//        switch (mode) {
//            case View.MeasureSpec.EXACTLY:
//                return size;
//            case View.MeasureSpec.AT_MOST:
//                return Math.min(size, Math.max(desired, min));
//            case View.MeasureSpec.UNSPECIFIED:
//            default:
//                return Math.max(desired, min);
//        }
//    }
//
//    public void assertNotInLayoutOrScroll(String message) {
//        if (mRecyclerView != null) {
//            mRecyclerView.assertNotInLayoutOrScroll(message);
//        }
//    }
//
//    @Deprecated
//    public void setAutoMeasureEnabled(boolean enabled) {
//        mAutoMeasure = enabled;
//    }
//
//    public boolean isAutoMeasureEnabled() {
//        return mAutoMeasure;
//    }
//
//    public boolean supportsPredictiveItemAnimations() {
//        return false;
//    }
//
//    /**
//     * Sets whether the LayoutManager should be queried for views outside of
//     * its viewport while the UI thread is idle between frames.
//     *
//     * <p>If enabled, the LayoutManager will be queried for items to inflate/bind in between
//     * view system traversals on devices running API 21 or greater. Default value is true.</p>
//     *
//     * <p>On platforms API level 21 and higher, the UI thread is idle between passing a frame
//     * to RenderThread and the starting up its next frame at the next VSync pulse. By
//     * prefetching out of window views in this time period, delays from inflation and view
//     * binding are much less likely to cause jank and stuttering during scrolls and flings.</p>
//     *
//     * <p>While prefetch is enabled, it will have the side effect of expanding the effective
//     * size of the View cache to hold prefetched views.</p>
//     *
//     * @param enabled <code>True</code> if items should be prefetched in between traversals.
//     *
//     * @see #isItemPrefetchEnabled()
//     */
//    public final void setItemPrefetchEnabled(boolean enabled) {
//        if (enabled != mItemPrefetchEnabled) {
//            mItemPrefetchEnabled = enabled;
//            mPrefetchMaxCountObserved = 0;
//            if (mRecyclerView != null) {
//                mRecyclerView.mRecycler.updateViewCacheSize();
//            }
//        }
//    }
//
//    /**
//     * Sets whether the LayoutManager should be queried for views outside of
//     * its viewport while the UI thread is idle between frames.
//     *
//     * @see #setItemPrefetchEnabled(boolean)
//     *
//     * @return true if item prefetch is enabled, false otherwise
//     */
//    public final boolean isItemPrefetchEnabled() {
//        return mItemPrefetchEnabled;
//    }
//
//
//    public void collectAdjacentPrefetchPositions(int dx, int dy, RecyclerView.State state,
//                                                 RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {}
//
//
//    public void collectInitialPrefetchPositions(int adapterItemCount,
//                                                RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {}
//
//    void dispatchAttachedToWindow(RecyclerView view) {
//        mIsAttachedToWindow = true;
//        onAttachedToWindow(view);
//    }
//
//    void dispatchDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
//        mIsAttachedToWindow = false;
//        onDetachedFromWindow(view, recycler);
//    }
//
//    /**
//     * Returns whether LayoutManager is currently attached to a RecyclerView which is attached
//     * to a window.
//     *
//     * @return True if this LayoutManager is controlling a RecyclerView and the RecyclerView
//     * is attached to window.
//     */
//    public boolean isAttachedToWindow() {
//        return mIsAttachedToWindow;
//    }
//
//    /**
//     * Causes the Runnable to execute on the next animation time step.
//     * The runnable will be run on the user interface thread.
//     * <p>
//     * Calling this method when LayoutManager is not attached to a RecyclerView has no effect.
//     *
//     * @param action The Runnable that will be executed.
//     *
//     * @see #removeCallbacks
//     */
//    public void postOnAnimation(Runnable action) {
//        if (mRecyclerView != null) {
//            ViewCompat.postOnAnimation(mRecyclerView, action);
//        }
//    }
//
//    /**
//     * Removes the specified Runnable from the message queue.
//     * <p>
//     * Calling this method when LayoutManager is not attached to a RecyclerView has no effect.
//     *
//     * @param action The Runnable to remove from the message handling queue
//     *
//     * @return true if RecyclerView could ask the Handler to remove the Runnable,
//     *         false otherwise. When the returned value is true, the Runnable
//     *         may or may not have been actually removed from the message queue
//     *         (for instance, if the Runnable was not in the queue already.)
//     *
//     * @see #postOnAnimation
//     */
//    public boolean removeCallbacks(Runnable action) {
//        if (mRecyclerView != null) {
//            return mRecyclerView.removeCallbacks(action);
//        }
//        return false;
//    }
//    /**
//     * Called when this LayoutManager is both attached to a RecyclerView and that RecyclerView
//     * is attached to a window.
//     * <p>
//     * If the RecyclerView is re-attached with the same LayoutManager and Adapter, it may not
//     * call {@link #onLayoutChildren(RecyclerView.Recycler, RecyclerView.State)} if nothing has changed and a layout was
//     * not requested on the RecyclerView while it was detached.
//     * <p>
//     * Subclass implementations should always call through to the superclass implementation.
//     *
//     * @param view The RecyclerView this LayoutManager is bound to
//     *
//     * @see #onDetachedFromWindow(RecyclerView, RecyclerView.Recycler)
//     */
//    @CallSuper
//    public void onAttachedToWindow(RecyclerView view) {
//    }
//
//    /**
//     * @deprecated
//     * override {@link #onDetachedFromWindow(RecyclerView, RecyclerView.Recycler)}
//     */
//    @Deprecated
//    public void onDetachedFromWindow(RecyclerView view) {
//
//    }
//
//    @CallSuper
//    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
//        onDetachedFromWindow(view);
//    }
//    public boolean getClipToPadding() {
//        return mRecyclerView != null && mRecyclerView.mClipToPadding;
//    }
//
//
//    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//        Log.e(TAG, "You must override onLayoutChildren(Recycler recycler, State state) ");
//    }
//
//
//    public void onLayoutCompleted(RecyclerView.State state) {
//    }
//
//
//    public abstract RecyclerView.LayoutParams generateDefaultLayoutParams();
//
//    public ViewBoundsCheck.Callback getmVerticalBoundCheckCallback() {
//        return mVerticalBoundCheckCallback;
//    }
//
//    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
//        return lp != null;
//    }
//
//
//    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
//        if (lp instanceof RecyclerView.LayoutParams) {
//            return new RecyclerView.LayoutParams((RecyclerView.LayoutParams) lp);
//        } else if (lp instanceof ViewGroup.MarginLayoutParams) {
//            return new RecyclerView.LayoutParams((ViewGroup.MarginLayoutParams) lp);
//        } else {
//            return new RecyclerView.LayoutParams(lp);
//        }
//    }
//
//    /**
//     * Create a LayoutParams object suitable for this LayoutManager from
//     * an inflated layout resource.
//     *
//     * <p><em>Important:</em> if you use your own custom <code>LayoutParams</code> type
//     * you must also override
//     * {@link #checkLayoutParams(RecyclerView.LayoutParams)},
//     * {@link #generateLayoutParams(android.view.ViewGroup.LayoutParams)} and
//     * {@link #generateLayoutParams(android.content.Context, android.util.AttributeSet)}.</p>
//     *
//     * @param c Context for obtaining styled attributes
//     * @param attrs AttributeSet describing the supplied arguments
//     * @return a new LayoutParams object
//     */
//    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
//        return new RecyclerView.LayoutParams(c, attrs);
//    }
//
//
//    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        return 0;
//    }
//
//    /**
//     * Scroll vertically by dy pixels in screen coordinates and return the distance traveled.
//     * The default implementation does nothing and returns 0.
//     *
//     * @param dy            distance to scroll in pixels. Y increases as scroll position
//     *                      approaches the bottom.
//     * @param recycler      Recycler to use for fetching potentially cached views for a
//     *                      position
//     * @param state         Transient state of RecyclerView
//     * @return The actual distance scrolled. The return value will be negative if dy was
//     * negative and scrolling proceeeded in that direction.
//     * <code>Math.abs(result)</code> may be less than dy if a boundary was reached.
//     */
//    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        return 0;
//    }
//
//    /**
//     * Query if horizontal scrolling is currently supported. The default implementation
//     * returns false.
//     *
//     * @return True if this LayoutManager can scroll the current contents horizontally
//     */
//    public boolean canScrollHorizontally() {
//        return false;
//    }
//
//    /**
//     * Query if vertical scrolling is currently supported. The default implementation
//     * returns false.
//     *
//     * @return True if this LayoutManager can scroll the current contents vertically
//     */
//    public boolean canScrollVertically() {
//        return false;
//    }
//
//    /**
//     * Scroll to the specified adapter position.
//     *
//     * Actual position of the item on the screen depends on the LayoutManager implementation.
//     * @param position Scroll to this adapter position.
//     */
//    public void scrollToPosition(int position) {
//        if (DEBUG) {
//            Log.e(TAG, "You MUST implement scrollToPosition. It will soon become abstract");
//        }
//    }
//    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
//                                       int position) {
//        Log.e(TAG, "You must override smoothScrollToPosition to support smooth scrolling");
//    }
//
//    /**
//     * Starts a smooth scroll using the provided {@link RecyclerView.SmoothScroller}.
//     *
//     * <p>Each instance of SmoothScroller is intended to only be used once. Provide a new
//     * SmoothScroller instance each time this method is called.
//     *
//     * <p>Calling this method will cancel any previous smooth scroll request.
//     *
//     * @param smoothScroller Instance which defines how smooth scroll should be animated
//     */
//    public void startSmoothScroll(RecyclerView.SmoothScroller smoothScroller) {
//        if (mSmoothScroller != null && smoothScroller != mSmoothScroller
//                && mSmoothScroller.isRunning()) {
//            mSmoothScroller.stop();
//        }
//        mSmoothScroller = smoothScroller;
//        mSmoothScroller.start(mRecyclerView, this);
//    }
//
//    /**
//     * @return true if RecyclerView is currently in the state of smooth scrolling.
//     */
//    public boolean isSmoothScrolling() {
//        return mSmoothScroller != null && mSmoothScroller.isRunning();
//    }
//
//    public int getLayoutDirection() {
//        return ViewCompat.getLayoutDirection(mRecyclerView);
//    }
//
//    /**
//     * Ends all animations on the view created by the {@link RecyclerView.ItemAnimator}.
//     *
//     * @param view The View for which the animations should be ended.
//     * @see RecyclerView.ItemAnimator#endAnimations()
//     */
//    public void endAnimation(View view) {
//        if (mRecyclerView.mItemAnimator != null) {
//            mRecyclerView.mItemAnimator.endAnimation(getChildViewHolderInt(view));
//        }
//    }
//
//    public void addDisappearingView(View child) {
//        addDisappearingView(child, -1);
//    }
//
//
//    public void addDisappearingView(View child, int index) {
//        addViewInt(child, index, true);
//    }
//
//    /**
//     * Add a view to the currently attached RecyclerView if needed. LayoutManagers should
//     * use this method to add views obtained from a {@link RecyclerView.Recycler} using
//     * {@link RecyclerView.Recycler#getViewForPosition(int)}.
//     *
//     * @param child View to add
//     */
//    public void addView(View child) {
//        addView(child, -1);
//    }
//
//    /**
//     * Add a view to the currently attached RecyclerView if needed. LayoutManagers should
//     * use this method to add views obtained from a {@link RecyclerView.Recycler} using
//     * {@link RecyclerView.Recycler#getViewForPosition(int)}.
//     *
//     * @param child View to add
//     * @param index Index to add child at
//     */
//    public void addView(View child, int index) {
//        addViewInt(child, index, false);
//    }
//
//    private void addViewInt(View child, int index, boolean disappearing) {
//        final RecyclerView.ViewHolder holder = getChildViewHolderInt(child);
//        if (disappearing || holder.isRemoved()) {
//            // these views will be hidden at the end of the layout pass.
//            mRecyclerView.mViewInfoStore.addToDisappearedInLayout(holder);
//        } else {
//            // This may look like unnecessary but may happen if layout manager supports
//            // predictive layouts and adapter removed then re-added the same item.
//            // In this case, added version will be visible in the post layout (because add is
//            // deferred) but RV will still bind it to the same View.
//            // So if a View re-appears in post layout pass, remove it from disappearing list.
//            mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(holder);
//        }
//        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
//        if (holder.wasReturnedFromScrap() || holder.isScrap()) {
//            if (holder.isScrap()) {
//                holder.unScrap();
//            } else {
//                holder.clearReturnedFromScrapFlag();
//            }
//            mChildHelper.attachViewToParent(child, index, child.getLayoutParams(), false);
//            if (DISPATCH_TEMP_DETACH) {
//                ViewCompat.dispatchFinishTemporaryDetach(child);
//            }
//        } else if (child.getParent() == mRecyclerView) { // it was not a scrap but a valid child
//            // ensure in correct position
//            int currentIndex = mChildHelper.indexOfChild(child);
//            if (index == -1) {
//                index = mChildHelper.getChildCount();
//            }
//            if (currentIndex == -1) {
//                throw new IllegalStateException("Added View has RecyclerView as parent but"
//                        + " view is not a real child. Unfiltered index:"
//                        + mRecyclerView.indexOfChild(child) + mRecyclerView.exceptionLabel());
//            }
//            if (currentIndex != index) {
//                mRecyclerView.mLayout.moveView(currentIndex, index);
//            }
//        } else {
//            mChildHelper.addView(child, index, false);
//            lp.mInsetsDirty = true;
//            if (mSmoothScroller != null && mSmoothScroller.isRunning()) {
//                mSmoothScroller.onChildAttachedToWindow(child);
//            }
//        }
//        if (lp.mPendingInvalidate) {
//            if (DEBUG) {
//                Log.d(TAG, "consuming pending invalidate on child " + lp.mViewHolder);
//            }
//            holder.itemView.invalidate();
//            lp.mPendingInvalidate = false;
//        }
//    }
//
//    public void removeView(View child) {
//        mChildHelper.removeView(child);
//    }
//
//    /**
//     * Remove a view from the currently attached RecyclerView if needed. LayoutManagers should
//     * use this method to completely remove a child view that is no longer needed.
//     * LayoutManagers should strongly consider recycling removed views using
//     * {@link RecyclerView.Recycler#recycleView(android.view.View)}.
//     *
//     * @param index Index of the child view to remove
//     */
//    public void removeViewAt(int index) {
//        final View child = getChildAt(index);
//        if (child != null) {
//            mChildHelper.removeViewAt(index);
//        }
//    }
//
//    /**
//     * Remove all views from the currently attached RecyclerView. This will not recycle
//     * any of the affected views; the LayoutManager is responsible for doing so if desired.
//     */
//    public void removeAllViews() {
//        // Only remove non-animating views
//        final int childCount = getChildCount();
//        for (int i = childCount - 1; i >= 0; i--) {
//            mChildHelper.removeViewAt(i);
//        }
//    }
//
//    /**
//     * Returns offset of the RecyclerView's text baseline from the its top boundary.
//     *
//     * @return The offset of the RecyclerView's text baseline from the its top boundary; -1 if
//     * there is no baseline.
//     */
//    public int getBaseline() {
//        return -1;
//    }
//
//    /**
//     * Returns the adapter position of the item represented by the given View. This does not
//     * contain any adapter changes that might have happened after the last layout.
//     *
//     * @param view The view to query
//     * @return The adapter position of the item which is rendered by this View.
//     */
//    public int getPosition(@NonNull View view) {
//        return ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
//    }
//
//    /**
//     * Returns the View type defined by the adapter.
//     *
//     * @param view The view to query
//     * @return The type of the view assigned by the adapter.
//     */
//    public int getItemViewType(@NonNull View view) {
//        return getChildViewHolderInt(view).getItemViewType();
//    }
//    @Nullable
//    public View findContainingItemView(@NonNull View view) {
//        if (mRecyclerView == null) {
//            return null;
//        }
//        View found = mRecyclerView.findContainingItemView(view);
//        if (found == null) {
//            return null;
//        }
//        if (mChildHelper.isHidden(found)) {
//            return null;
//        }
//        return found;
//    }
//
//    @Nullable
//    public View findViewByPosition(int position) {
//        final int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = getChildAt(i);
//            RecyclerView.ViewHolder vh = getChildViewHolderInt(child);
//            if (vh == null) {
//                continue;
//            }
//            if (vh.getLayoutPosition() == position && !vh.shouldIgnore()
//                    && (mRecyclerView.mState.isPreLayout() || !vh.isRemoved())) {
//                return child;
//            }
//        }
//        return null;
//    }
//
//    public void detachView(@NonNull View child) {
//        final int ind = mChildHelper.indexOfChild(child);
//        if (ind >= 0) {
//            detachViewInternal(ind, child);
//        }
//    }
//
//    public void detachViewAt(int index) {
//        detachViewInternal(index, getChildAt(index));
//    }
//
//    private void detachViewInternal(int index, @NonNull View view) {
//        if (DISPATCH_TEMP_DETACH) {
//            ViewCompat.dispatchStartTemporaryDetach(view);
//        }
//        mChildHelper.detachViewFromParent(index);
//    }
//
//    public void attachView(@NonNull View child, int index, RecyclerView.LayoutParams lp) {
//        RecyclerView.ViewHolder vh = getChildViewHolderInt(child);
//        if (vh.isRemoved()) {
//            mRecyclerView.mViewInfoStore.addToDisappearedInLayout(vh);
//        } else {
//            mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(vh);
//        }
//        mChildHelper.attachViewToParent(child, index, lp, vh.isRemoved());
//        if (DISPATCH_TEMP_DETACH)  {
//            ViewCompat.dispatchFinishTemporaryDetach(child);
//        }
//    }
//
//    /**
//     * Reattach a previously {@link #detachView(android.view.View) detached} view.
//     * This method should not be used to reattach views that were previously
//     * {@link #detachAndScrapView(android.view.View, RecyclerView.Recycler)}  scrapped}.
//     *
//     * @param child Child to reattach
//     * @param index Intended child index for child
//     */
//    public void attachView(@NonNull View child, int index) {
//        attachView(child, index, (RecyclerView.LayoutParams) child.getLayoutParams());
//    }
//
//    /**
//     * Reattach a previously {@link #detachView(android.view.View) detached} view.
//     * This method should not be used to reattach views that were previously
//     * {@link #detachAndScrapView(android.view.View, RecyclerView.Recycler)}  scrapped}.
//     *
//     * @param child Child to reattach
//     */
//    public void attachView(@NonNull View child) {
//        attachView(child, -1);
//    }
//
//    /**
//     * Finish removing a view that was previously temporarily
//     * {@link #detachView(android.view.View) detached}.
//     *
//     * @param child Detached child to remove
//     */
//    public void removeDetachedView(@NonNull View child) {
//        mRecyclerView.removeDetachedView(child, false);
//    }
//
//    /**
//     * Moves a View from one position to another.
//     *
//     * @param fromIndex The View's initial index
//     * @param toIndex The View's target index
//     */
//    public void moveView(int fromIndex, int toIndex) {
//        View view = getChildAt(fromIndex);
//        if (view == null) {
//            throw new IllegalArgumentException("Cannot move a child from non-existing index:"
//                    + fromIndex + mRecyclerView.toString());
//        }
//        detachViewAt(fromIndex);
//        attachView(view, toIndex);
//    }
//
//    /**
//     * Detach a child view and add it to a {@link RecyclerView.Recycler Recycler's} scrap heap.
//     *
//     * <p>Scrapping a view allows it to be rebound and reused to show updated or
//     * different data.</p>
//     *
//     * @param child Child to detach and scrap
//     * @param recycler Recycler to deposit the new scrap view into
//     */
//    public void detachAndScrapView(@NonNull View child, @NonNull RecyclerView.Recycler recycler) {
//        int index = mChildHelper.indexOfChild(child);
//        scrapOrRecycleView(recycler, index, child);
//    }
//
//    public void detachAndScrapViewAt(int index, @NonNull RecyclerView.Recycler recycler) {
//        final View child = getChildAt(index);
//        scrapOrRecycleView(recycler, index, child);
//    }
//
//    /**
//     * Remove a child view and recycle it using the given Recycler.
//     *
//     * @param child Child to remove and recycle
//     * @param recycler Recycler to use to recycle child
//     */
//    public void removeAndRecycleView(@NonNull View child, @NonNull RecyclerView.Recycler recycler) {
//        removeView(child);
//        recycler.recycleView(child);
//    }
//
//    public void removeAndRecycleViewAt(int index, @NonNull RecyclerView.Recycler recycler) {
//        final View view = getChildAt(index);
//        removeViewAt(index);
//        recycler.recycleView(view);
//    }
//    public int getChildCount() {
//        return mChildHelper != null ? mChildHelper.getChildCount() : 0;
//    }
//
//    @Nullable
//    public View getChildAt(int index) {
//        return mChildHelper != null ? mChildHelper.getChildAt(index) : null;
//    }
//
//    public int getWidthMode() {
//        return mWidthMode;
//    }
//
//    public int getHeightMode() {
//        return mHeightMode;
//    }
//
//    @Px
//    public int getWidth() {
//        return mWidth;
//    }
//
//    @Px
//    public int getHeight() {
//        return mHeight;
//    }
//
//    @Px
//    public int getPaddingLeft() {
//        return mRecyclerView != null ? mRecyclerView.getPaddingLeft() : 0;
//    }
//
//
//    @Px
//    public int getPaddingTop() {
//        return mRecyclerView != null ? mRecyclerView.getPaddingTop() : 0;
//    }
//
//    /**
//     * Return the right padding of the parent RecyclerView
//     *
//     * @return Padding in pixels
//     */
//    @Px
//    public int getPaddingRight() {
//        return mRecyclerView != null ? mRecyclerView.getPaddingRight() : 0;
//    }
//
//    /**
//     * Return the bottom padding of the parent RecyclerView
//     *
//     * @return Padding in pixels
//     */
//    @Px
//    public int getPaddingBottom() {
//        return mRecyclerView != null ? mRecyclerView.getPaddingBottom() : 0;
//    }`
//    @Px
//    public int getPaddingStart() {
//        return mRecyclerView != null ? ViewCompat.getPaddingStart(mRecyclerView) : 0;
//    }
//
//    /**
//     * Return the end padding of the parent RecyclerView
//     *
//     * @return Padding in pixels
//     */
//    @Px
//    public int getPaddingEnd() {
//        return mRecyclerView != null ? ViewCompat.getPaddingEnd(mRecyclerView) : 0;
//    }
//
//    /**
//     * Returns true if the RecyclerView this LayoutManager is bound to has focus.
//     *
//     * @return True if the RecyclerView has focus, false otherwise.
//     * @see View#isFocused()
//     */
//    public boolean isFocused() {
//        return mRecyclerView != null && mRecyclerView.isFocused();
//    }
//
//    /**
//     * Returns true if the RecyclerView this LayoutManager is bound to has or contains focus.
//     *
//     * @return true if the RecyclerView has or contains focus
//     * @see View#hasFocus()
//     */
//    public boolean hasFocus() {
//        return mRecyclerView != null && mRecyclerView.hasFocus();
//    }
//
//    /**
//     * Returns the item View which has or contains focus.
//     *
//     * @return A direct child of RecyclerView which has focus or contains the focused child.
//     */
//    @Nullable
//    public View getFocusedChild() {
//        if (mRecyclerView == null) {
//            return null;
//        }
//        final View focused = mRecyclerView.getFocusedChild();
//        if (focused == null || mChildHelper.isHidden(focused)) {
//            return null;
//        }
//        return focused;
//    }
//
//
//    public int getItemCount() {
//        final RecyclerView.Adapter a = mRecyclerView != null ? mRecyclerView.getAdapter() : null;
//        return a != null ? a.getItemCount() : 0;
//    }
//
//
//    public void offsetChildrenHorizontal(@Px int dx) {
//        if (mRecyclerView != null) {
//            mRecyclerView.offsetChildrenHorizontal(dx);
//        }
//    }
//
//
//    public void offsetChildrenVertical(@Px int dy) {
//        if (mRecyclerView != null) {
//            mRecyclerView.offsetChildrenVertical(dy);
//        }
//    }
//
//
//    public void ignoreView(@NonNull View view) {
//        if (view.getParent() != mRecyclerView || mRecyclerView.indexOfChild(view) == -1) {
//            // checking this because calling this method on a recycled or detached view may
//            // cause loss of state.
//            throw new IllegalArgumentException("View should be fully attached to be ignored"
//                    + mRecyclerView.exceptionLabel());
//        }
//        final RecyclerView.ViewHolder vh = getChildViewHolderInt(view);
//        vh.addFlags(RecyclerView.ViewHolder.FLAG_IGNORE);
//        mRecyclerView.mViewInfoStore.removeViewHolder(vh);
//    }
//
//    /**
//     * View can be scrapped and recycled again.
//     * <p>
//     * Note that calling this method removes all information in the view holder.
//     * <p>
//     * You can call this method only if your LayoutManger is in onLayout or onScroll callback.
//     *
//     * @param view View to ignore.
//     */
//    public void stopIgnoringView(@NonNull View view) {
//        final RecyclerView.ViewHolder vh = getChildViewHolderInt(view);
//        vh.stopIgnoring();
//        vh.resetInternal();
//        vh.addFlags(RecyclerView.ViewHolder.FLAG_INVALID);
//    }
//
//    public void detachAndScrapAttachedViews(@NonNull RecyclerView.Recycler recycler) {
//        final int childCount = getChildCount();
//        for (int i = childCount - 1; i >= 0; i--) {
//            final View v = getChildAt(i);
//            scrapOrRecycleView(recycler, i, v);
//        }
//    }
//
//    private void scrapOrRecycleView(RecyclerView.Recycler recycler, int index, View view) {
//        final RecyclerView.ViewHolder viewHolder = getChildViewHolderInt(view);
//        if (viewHolder.shouldIgnore()) {
//            if (DEBUG) {
//                Log.d(TAG, "ignoring view " + viewHolder);
//            }
//            return;
//        }
//        if (viewHolder.isInvalid() && !viewHolder.isRemoved()
//                && !mRecyclerView.mAdapter.hasStableIds()) {
//            removeViewAt(index);
//            recycler.recycleViewHolderInternal(viewHolder);
//        } else {
//            detachViewAt(index);
//            recycler.scrapView(view);
//            mRecyclerView.mViewInfoStore.onViewDetached(viewHolder);
//        }
//    }
//
//
//    void removeAndRecycleScrapInt(RecyclerView.Recycler recycler) {
//        final int scrapCount = recycler.getScrapCount();
//        // Loop backward, recycler might be changed by removeDetachedView()
//        for (int i = scrapCount - 1; i >= 0; i--) {
//            final View scrap = recycler.getScrapViewAt(i);
//            final RecyclerView.ViewHolder vh = getChildViewHolderInt(scrap);
//            if (vh.shouldIgnore()) {
//                continue;
//            }
//            // If the scrap view is animating, we need to cancel them first. If we cancel it
//            // here, ItemAnimator callback may recycle it which will cause double recycling.
//            // To avoid this, we mark it as not recycleable before calling the item animator.
//            // Since removeDetachedView calls a user API, a common mistake (ending animations on
//            // the view) may recycle it too, so we guard it before we call user APIs.
//            vh.setIsRecyclable(false);
//            if (vh.isTmpDetached()) {
//                mRecyclerView.removeDetachedView(scrap, false);
//            }
//            if (mRecyclerView.mItemAnimator != null) {
//                mRecyclerView.mItemAnimator.endAnimation(vh);
//            }
//            vh.setIsRecyclable(true);
//            recycler.quickRecycleScrapView(scrap);
//        }
//        recycler.clearScrap();
//        if (scrapCount > 0) {
//            mRecyclerView.invalidate();
//        }
//    }
//
//
//
//    public void measureChild(@NonNull View child, int widthUsed, int heightUsed) {
//        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
//
//        final Rect insets = mRecyclerView.getItemDecorInsetsForChild(child);
//        widthUsed += insets.left + insets.right;
//        heightUsed += insets.top + insets.bottom;
//        final int widthSpec = getChildMeasureSpec(getWidth(), getWidthMode(),
//                getPaddingLeft() + getPaddingRight() + widthUsed, lp.width,
//                canScrollHorizontally());
//        final int heightSpec = getChildMeasureSpec(getHeight(), getHeightMode(),
//                getPaddingTop() + getPaddingBottom() + heightUsed, lp.height,
//                canScrollVertically());
//        if (shouldMeasureChild(child, widthSpec, heightSpec, lp)) {
//            child.measure(widthSpec, heightSpec);
//        }
//    }
//
//    /**
//     * RecyclerView internally does its own View measurement caching which should help with
//     * WRAP_CONTENT.
//     * <p>
//     * Use this method if the View is already measured once in this layout pass.
//     */
//    boolean shouldReMeasureChild(View child, int widthSpec, int heightSpec, RecyclerView.LayoutParams lp) {
//        return !mMeasurementCacheEnabled
//                || !isMeasurementUpToDate(child.getMeasuredWidth(), widthSpec, lp.width)
//                || !isMeasurementUpToDate(child.getMeasuredHeight(), heightSpec, lp.height);
//    }
//
//
//    boolean shouldMeasureChild(View child, int widthSpec, int heightSpec, RecyclerView.LayoutParams lp) {
//        return child.isLayoutRequested()
//                || !mMeasurementCacheEnabled
//                || !isMeasurementUpToDate(child.getWidth(), widthSpec, lp.width)
//                || !isMeasurementUpToDate(child.getHeight(), heightSpec, lp.height);
//    }
//
//
//    public boolean isMeasurementCacheEnabled() {
//        return mMeasurementCacheEnabled;
//    }
//
//    public void setMeasurementCacheEnabled(boolean measurementCacheEnabled) {
//        mMeasurementCacheEnabled = measurementCacheEnabled;
//    }
//
//    private static boolean isMeasurementUpToDate(int childSize, int spec, int dimension) {
//        final int specMode = View.MeasureSpec.getMode(spec);
//        final int specSize = View.MeasureSpec.getSize(spec);
//        if (dimension > 0 && childSize != dimension) {
//            return false;
//        }
//        switch (specMode) {
//            case View.MeasureSpec.UNSPECIFIED:
//                return true;
//            case View.MeasureSpec.AT_MOST:
//                return specSize >= childSize;
//            case View.MeasureSpec.EXACTLY:
//                return  specSize == childSize;
//        }
//        return false;
//    }
//
//    public void measureChildWithMargins(@NonNull View child, int widthUsed, int heightUsed) {
//        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
//
//        final Rect insets = mRecyclerView.getItemDecorInsetsForChild(child);
//        widthUsed += insets.left + insets.right;
//        heightUsed += insets.top + insets.bottom;
//
//        final int widthSpec = getChildMeasureSpec(getWidth(), getWidthMode(),
//                getPaddingLeft() + getPaddingRight()
//                        + lp.leftMargin + lp.rightMargin + widthUsed, lp.width,
//                canScrollHorizontally());
//        final int heightSpec = getChildMeasureSpec(getHeight(), getHeightMode(),
//                getPaddingTop() + getPaddingBottom()
//                        + lp.topMargin + lp.bottomMargin + heightUsed, lp.height,
//                canScrollVertically());
//        if (shouldMeasureChild(child, widthSpec, heightSpec, lp)) {
//            child.measure(widthSpec, heightSpec);
//        }
//    }
//
//    @Deprecated
//    public static int getChildMeasureSpec(int parentSize, int padding, int childDimension,
//                                          boolean canScroll) {
//        int size = Math.max(0, parentSize - padding);
//        int resultSize = 0;
//        int resultMode = 0;
//        if (canScroll) {
//            if (childDimension >= 0) {
//                resultSize = childDimension;
//                resultMode = View.MeasureSpec.EXACTLY;
//            } else {
//                // MATCH_PARENT can't be applied since we can scroll in this dimension, wrap
//                // instead using UNSPECIFIED.
//                resultSize = 0;
//                resultMode = View.MeasureSpec.UNSPECIFIED;
//            }
//        } else {
//            if (childDimension >= 0) {
//                resultSize = childDimension;
//                resultMode = View.MeasureSpec.EXACTLY;
//            } else if (childDimension == RecyclerView.LayoutParams.MATCH_PARENT) {
//                resultSize = size;
//                // TODO this should be my spec.
//                resultMode = View.MeasureSpec.EXACTLY;
//            } else if (childDimension == RecyclerView.LayoutParams.WRAP_CONTENT) {
//                resultSize = size;
//                resultMode = View.MeasureSpec.AT_MOST;
//            }
//        }
//        return View.MeasureSpec.makeMeasureSpec(resultSize, resultMode);
//    }
//
//    public static int getChildMeasureSpec(int parentSize, int parentMode, int padding,
//                                          int childDimension, boolean canScroll) {
//        int size = Math.max(0, parentSize - padding);
//        int resultSize = 0;
//        int resultMode = 0;
//        if (canScroll) {
//            if (childDimension >= 0) {
//                resultSize = childDimension;
//                resultMode = View.MeasureSpec.EXACTLY;
//            } else if (childDimension == RecyclerView.LayoutParams.MATCH_PARENT) {
//                switch (parentMode) {
//                    case View.MeasureSpec.AT_MOST:
//                    case View.MeasureSpec.EXACTLY:
//                        resultSize = size;
//                        resultMode = parentMode;
//                        break;
//                    case View.MeasureSpec.UNSPECIFIED:
//                        resultSize = 0;
//                        resultMode = View.MeasureSpec.UNSPECIFIED;
//                        break;
//                }
//            } else if (childDimension == RecyclerView.LayoutParams.WRAP_CONTENT) {
//                resultSize = 0;
//                resultMode = View.MeasureSpec.UNSPECIFIED;
//            }
//        } else {
//            if (childDimension >= 0) {
//                resultSize = childDimension;
//                resultMode = View.MeasureSpec.EXACTLY;
//            } else if (childDimension == RecyclerView.LayoutParams.MATCH_PARENT) {
//                resultSize = size;
//                resultMode = parentMode;
//            } else if (childDimension == RecyclerView.LayoutParams.WRAP_CONTENT) {
//                resultSize = size;
//                if (parentMode == View.MeasureSpec.AT_MOST || parentMode == View.MeasureSpec.EXACTLY) {
//                    resultMode = View.MeasureSpec.AT_MOST;
//                } else {
//                    resultMode = View.MeasureSpec.UNSPECIFIED;
//                }
//
//            }
//        }
//        //noinspection WrongConstant
//        return View.MeasureSpec.makeMeasureSpec(resultSize, resultMode);
//    }
//
//    /**
//     * Returns the measured width of the given child, plus the additional size of
//     * any insets applied by {@link RecyclerView.ItemDecoration ItemDecorations}.
//     *
//     * @param child Child view to query
//     * @return child's measured width plus <code>ItemDecoration</code> insets
//     *
//     * @see View#getMeasuredWidth()
//     */
//    public int getDecoratedMeasuredWidth(@NonNull View child) {
//        final Rect insets = ((RecyclerView.LayoutParams) child.getLayoutParams()).mDecorInsets;
//        return child.getMeasuredWidth() + insets.left + insets.right;
//    }
//
//    /**
//     * Returns the measured height of the given child, plus the additional size of
//     * any insets applied by {@link RecyclerView.ItemDecoration ItemDecorations}.
//     *
//     * @param child Child view to query
//     * @return child's measured height plus <code>ItemDecoration</code> insets
//     *
//     * @see View#getMeasuredHeight()
//     */
//    public int getDecoratedMeasuredHeight(@NonNull View child) {
//        final Rect insets = ((RecyclerView.LayoutParams) child.getLayoutParams()).mDecorInsets;
//        return child.getMeasuredHeight() + insets.top + insets.bottom;
//    }
//    public void layoutDecorated(@NonNull View child, int left, int top, int right, int bottom) {
//        final Rect insets = ((RecyclerView.LayoutParams) child.getLayoutParams()).mDecorInsets;
//        child.layout(left + insets.left, top + insets.top, right - insets.right,
//                bottom - insets.bottom);
//    }
//
//    public void layoutDecoratedWithMargins(@NonNull View child, int left, int top, int right,
//                                           int bottom) {
//        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
//        final Rect insets = lp.mDecorInsets;
//        child.layout(left + insets.left + lp.leftMargin, top + insets.top + lp.topMargin,
//                right - insets.right - lp.rightMargin,
//                bottom - insets.bottom - lp.bottomMargin);
//    }
//
//    public void getTransformedBoundingBox(@NonNull View child, boolean includeDecorInsets,
//                                          @NonNull Rect out) {
//        if (includeDecorInsets) {
//            Rect insets = ((RecyclerView.LayoutParams) child.getLayoutParams()).mDecorInsets;
//            out.set(-insets.left, -insets.top,
//                    child.getWidth() + insets.right, child.getHeight() + insets.bottom);
//        } else {
//            out.set(0, 0, child.getWidth(), child.getHeight());
//        }
//
//        if (mRecyclerView != null) {
//            final Matrix childMatrix = child.getMatrix();
//            if (childMatrix != null && !childMatrix.isIdentity()) {
//                final RectF tempRectF = mRecyclerView.mTempRectF;
//                tempRectF.set(out);
//                childMatrix.mapRect(tempRectF);
//                out.set(
//                        (int) Math.floor(tempRectF.left),
//                        (int) Math.floor(tempRectF.top),
//                        (int) Math.ceil(tempRectF.right),
//                        (int) Math.ceil(tempRectF.bottom)
//                );
//            }
//        }
//        out.offset(child.getLeft(), child.getTop());
//    }
//
//    /**
//     * Returns the bounds of the view including its decoration and margins.
//     *
//     * @param view The view element to check
//     * @param outBounds A rect that will receive the bounds of the element including its
//     *                  decoration and margins.
//     */
//    public void getDecoratedBoundsWithMargins(@NonNull View view, @NonNull Rect outBounds) {
//        RecyclerView.getDecoratedBoundsWithMarginsInt(view, outBounds);
//    }
//
//    /**
//     * Returns the left edge of the given child view within its parent, offset by any applied
//     * {@link RecyclerView.ItemDecoration ItemDecorations}.
//     *
//     * @param child Child to query
//     * @return Child left edge with offsets applied
//     * @see #getLeftDecorationWidth(View)
//     */
//    public int getDecoratedLeft(@NonNull View child) {
//        return child.getLeft() - getLeftDecorationWidth(child);
//    }
//
//    /**
//     * Returns the top edge of the given child view within its parent, offset by any applied
//     * {@link RecyclerView.ItemDecoration ItemDecorations}.
//     *
//     * @param child Child to query
//     * @return Child top edge with offsets applied
//     * @see #getTopDecorationHeight(View)
//     */
//    public int getDecoratedTop(@NonNull View child) {
//        return child.getTop() - getTopDecorationHeight(child);
//    }
//
//
//    public int getDecoratedRight(@NonNull View child) {
//        return child.getRight() + getRightDecorationWidth(child);
//    }
//
//
//    public int getDecoratedBottom(@NonNull View child) {
//        return child.getBottom() + getBottomDecorationHeight(child);
//    }
//
//    public void calculateItemDecorationsForChild(@NonNull View child, @NonNull Rect outRect) {
//        if (mRecyclerView == null) {
//            outRect.set(0, 0, 0, 0);
//            return;
//        }
//        Rect insets = mRecyclerView.getItemDecorInsetsForChild(child);
//        outRect.set(insets);
//    }
//    public int getTopDecorationHeight(@NonNull View child) {
//        return ((RecyclerView.LayoutParams) child.getLayoutParams()).mDecorInsets.top;
//    }
//
//    public int getBottomDecorationHeight(@NonNull View child) {
//        return ((RecyclerView.LayoutParams) child.getLayoutParams()).mDecorInsets.bottom;
//    }
//
//    public int getLeftDecorationWidth(@NonNull View child) {
//        return ((RecyclerView.LayoutParams) child.getLayoutParams()).mDecorInsets.left;
//    }
//
//    public int getRightDecorationWidth(@NonNull View child) {
//        return ((RecyclerView.LayoutParams) child.getLayoutParams()).mDecorInsets.right;
//    }
//
//
//    @Nullable
//    public View onFocusSearchFailed(@NonNull View focused, int direction,
//                                    @NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state) {
//        return null;
//    }
//
//    @Nullable
//    public View onInterceptFocusSearch(@NonNull View focused, int direction) {
//        return null;
//    }
//
//
//    private int[] getChildRectangleOnScreenScrollAmount(View child, Rect rect) {
//        int[] out = new int[2];
//        final int parentLeft = getPaddingLeft();
//        final int parentTop = getPaddingTop();
//        final int parentRight = getWidth() - getPaddingRight();
//        final int parentBottom = getHeight() - getPaddingBottom();
//        final int childLeft = child.getLeft() + rect.left - child.getScrollX();
//        final int childTop = child.getTop() + rect.top - child.getScrollY();
//        final int childRight = childLeft + rect.width();
//        final int childBottom = childTop + rect.height();
//
//        final int offScreenLeft = Math.min(0, childLeft - parentLeft);
//        final int offScreenTop = Math.min(0, childTop - parentTop);
//        final int offScreenRight = Math.max(0, childRight - parentRight);
//        final int offScreenBottom = Math.max(0, childBottom - parentBottom);
//
//        // Favor the "start" layout direction over the end when bringing one side or the other
//        // of a large rect into view. If we decide to bring in end because start is already
//        // visible, limit the scroll such that start won't go out of bounds.
//        final int dx;
//        if (getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL) {
//            dx = offScreenRight != 0 ? offScreenRight
//                    : Math.max(offScreenLeft, childRight - parentRight);
//        } else {
//            dx = offScreenLeft != 0 ? offScreenLeft
//                    : Math.min(childLeft - parentLeft, offScreenRight);
//        }
//
//        // Favor bringing the top into view over the bottom. If top is already visible and
//        // we should scroll to make bottom visible, make sure top does not go out of bounds.
//        final int dy = offScreenTop != 0 ? offScreenTop
//                : Math.min(childTop - parentTop, offScreenBottom);
//        out[0] = dx;
//        out[1] = dy;
//        return out;
//    }
//
//    public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent,
//                                                 @NonNull View child, @NonNull Rect rect, boolean immediate) {
//        return requestChildRectangleOnScreen(parent, child, rect, immediate, false);
//    }
//
//    public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent,
//                                                 @NonNull View child, @NonNull Rect rect, boolean immediate,
//                                                 boolean focusedChildVisible) {
//        int[] scrollAmount = getChildRectangleOnScreenScrollAmount(child, rect
//        );
//        int dx = scrollAmount[0];
//        int dy = scrollAmount[1];
//        if (!focusedChildVisible || isFocusedChildVisibleAfterScrolling(parent, dx, dy)) {
//            if (dx != 0 || dy != 0) {
//                if (immediate) {
//                    parent.scrollBy(dx, dy);
//                } else {
//                    parent.smoothScrollBy(dx, dy);
//                }
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean isViewPartiallyVisible(@NonNull View child, boolean completelyVisible,
//                                          boolean acceptEndPointInclusion) {
//        int boundsFlag = (ViewBoundsCheck.FLAG_CVS_GT_PVS | ViewBoundsCheck.FLAG_CVS_EQ_PVS
//                | ViewBoundsCheck.FLAG_CVE_LT_PVE | ViewBoundsCheck.FLAG_CVE_EQ_PVE);
//        boolean isViewFullyVisible = mHorizontalBoundCheck.isViewWithinBoundFlags(child,
//                boundsFlag)
//                && mVerticalBoundCheck.isViewWithinBoundFlags(child, boundsFlag);
//        if (completelyVisible) {
//            return isViewFullyVisible;
//        } else {
//            return !isViewFullyVisible;
//        }
//    }
//    private boolean isFocusedChildVisibleAfterScrolling(RecyclerView parent, int dx, int dy) {
//        final View focusedChild = parent.getFocusedChild();
//        if (focusedChild == null) {
//            return false;
//        }
//        final int parentLeft = getPaddingLeft();
//        final int parentTop = getPaddingTop();
//        final int parentRight = getWidth() - getPaddingRight();
//        final int parentBottom = getHeight() - getPaddingBottom();
//        final Rect bounds = mRecyclerView.mTempRect;
//        getDecoratedBoundsWithMargins(focusedChild, bounds);
//
//        if (bounds.left - dx >= parentRight || bounds.right - dx <= parentLeft
//                || bounds.top - dy >= parentBottom || bounds.bottom - dy <= parentTop) {
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * @deprecated Use {@link #onRequestChildFocus(RecyclerView, RecyclerView.State, View, View)}
//     */
//    @Deprecated
//    public boolean onRequestChildFocus(@NonNull RecyclerView parent, @NonNull View child,
//                                       @Nullable View focused) {
//        // eat the request if we are in the middle of a scroll or layout
//        return isSmoothScrolling() || parent.isComputingLayout();
//    }
//
//    public boolean onRequestChildFocus(@NonNull RecyclerView parent, @NonNull RecyclerView.State state,
//                                       @NonNull View child, @Nullable View focused) {
//        return onRequestChildFocus(parent, child, focused);
//    }
//
//    public void onAdapterChanged(@Nullable RecyclerView.Adapter oldAdapter, @Nullable RecyclerView.Adapter newAdapter) {
//    }
//
//
//    public boolean onAddFocusables(@NonNull RecyclerView recyclerView,
//                                   @NonNull ArrayList<View> views, int direction, int focusableMode) {
//        return false;
//    }
//
//    /**
//     * Called in response to a call to {@link RecyclerView.Adapter#notifyDataSetChanged()} or
//     * {@link RecyclerView#swapAdapter(RecyclerView.Adapter, boolean)} ()} and signals that the the entire
//     * data set has changed.
//     *
//     * @param recyclerView
//     */
//    public void onItemsChanged(@NonNull RecyclerView recyclerView) {
//    }
//
//    /**
//     * Called when items have been added to the adapter. The LayoutManager may choose to
//     * requestLayout if the inserted items would require refreshing the currently visible set
//     * of child views. (e.g. currently empty space would be filled by appended items, etc.)
//     *
//     * @param recyclerView
//     * @param positionStart
//     * @param itemCount
//     */
//    public void onItemsAdded(@NonNull RecyclerView recyclerView, int positionStart,
//                             int itemCount) {
//    }
//
//    /**
//     * Called when items have been removed from the adapter.
//     *
//     * @param recyclerView
//     * @param positionStart
//     * @param itemCount
//     */
//    public void onItemsRemoved(@NonNull RecyclerView recyclerView, int positionStart,
//                               int itemCount) {
//    }
//
//
//    public void onItemsUpdated(@NonNull RecyclerView recyclerView, int positionStart,
//                               int itemCount) {
//    }
//
//
//    public void onItemsUpdated(@NonNull RecyclerView recyclerView, int positionStart,
//                               int itemCount, @Nullable Object payload) {
//        onItemsUpdated(recyclerView, positionStart, itemCount);
//    }
//
//    public void onItemsMoved(@NonNull RecyclerView recyclerView, int from, int to,
//                             int itemCount) {
//
//    }
//
//
//
//    public int computeHorizontalScrollExtent(@NonNull RecyclerView.State state) {
//        return 0;
//    }
//
//
//    public int computeHorizontalScrollOffset(@NonNull RecyclerView.State state) {
//        return 0;
//    }
//
//
//    public int computeHorizontalScrollRange(@NonNull RecyclerView.State state) {
//        return 0;
//    }
//
//
//    public int computeVerticalScrollExtent(@NonNull RecyclerView.State state) {
//        return 0;
//    }
//
//    public int computeVerticalScrollOffset(@NonNull RecyclerView.State state) {
//        return 0;
//    }
//
//    public int computeVerticalScrollRange(@NonNull RecyclerView.State state) {
//        return 0;
//    }
//
//    public void onMeasure(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state, int widthSpec,
//                          int heightSpec) {
//        mRecyclerView.defaultOnMeasure(widthSpec, heightSpec);
//    }
//
//    public void setMeasuredDimension(int widthSize, int heightSize) {
//        mRecyclerView.setMeasuredDimension(widthSize, heightSize);
//    }
//
//    /**
//     * @return The host RecyclerView's {@link View#getMinimumWidth()}
//     */
//    @Px
//    public int getMinimumWidth() {
//        return ViewCompat.getMinimumWidth(mRecyclerView);
//    }
//
//    /**
//     * @return The host RecyclerView's {@link View#getMinimumHeight()}
//     */
//    @Px
//    public int getMinimumHeight() {
//        return ViewCompat.getMinimumHeight(mRecyclerView);
//    }
//
//    @Nullable
//    public Parcelable onSaveInstanceState() {
//        return null;
//    }
//
//
//    public void onRestoreInstanceState(Parcelable state) {
//
//    }
//
//    void stopSmoothScroller() {
//        if (mSmoothScroller != null) {
//            mSmoothScroller.stop();
//        }
//    }
//
//    void onSmoothScrollerStopped(RecyclerView.SmoothScroller smoothScroller) {
//        if (mSmoothScroller == smoothScroller) {
//            mSmoothScroller = null;
//        }
//    }
//
//    public void onScrollStateChanged(int state) {
//    }
//
//    public void removeAndRecycleAllViews(@NonNull RecyclerView.Recycler recycler) {
//        for (int i = getChildCount() - 1; i >= 0; i--) {
//            final View view = getChildAt(i);
//            if (!getChildViewHolderInt(view).shouldIgnore()) {
//                removeAndRecycleViewAt(i, recycler);
//            }
//        }
//    }
//
//    // called by accessibility delegate
//    void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat info) {
//        onInitializeAccessibilityNodeInfo(mRecyclerView.mRecycler, mRecyclerView.mState, info);
//    }
//
//    public void onInitializeAccessibilityNodeInfo(@NonNull RecyclerView.Recycler recycler,
//                                                  @NonNull RecyclerView.State state, @NonNull AccessibilityNodeInfoCompat info) {
//        if (mRecyclerView.canScrollVertically(-1) || mRecyclerView.canScrollHorizontally(-1)) {
//            info.addAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
//            info.setScrollable(true);
//        }
//        if (mRecyclerView.canScrollVertically(1) || mRecyclerView.canScrollHorizontally(1)) {
//            info.addAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
//            info.setScrollable(true);
//        }
//        final AccessibilityNodeInfoCompat.CollectionInfoCompat collectionInfo =
//                AccessibilityNodeInfoCompat.CollectionInfoCompat
//                        .obtain(getRowCountForAccessibility(recycler, state),
//                                getColumnCountForAccessibility(recycler, state),
//                                isLayoutHierarchical(recycler, state),
//                                getSelectionModeForAccessibility(recycler, state));
//        info.setCollectionInfo(collectionInfo);
//    }
//
//    // called by accessibility delegate
//    public void onInitializeAccessibilityEvent(@NonNull AccessibilityEvent event) {
//        onInitializeAccessibilityEvent(mRecyclerView.mRecycler, mRecyclerView.mState, event);
//    }
//
//    /**
//     * Called by the accessibility delegate to initialize an accessibility event.
//     * <p>
//     * Default implementation adds item count and scroll information to the event.
//     *
//     * @param recycler The Recycler that can be used to convert view positions into adapter
//     *                 positions
//     * @param state    The current state of RecyclerView
//     * @param event    The event instance to initialize
//     * @see View#onInitializeAccessibilityEvent(android.view.accessibility.AccessibilityEvent)
//     */
//    public void onInitializeAccessibilityEvent(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state,
//                                               @NonNull AccessibilityEvent event) {
//        if (mRecyclerView == null || event == null) {
//            return;
//        }
//        event.setScrollable(mRecyclerView.canScrollVertically(1)
//                || mRecyclerView.canScrollVertically(-1)
//                || mRecyclerView.canScrollHorizontally(-1)
//                || mRecyclerView.canScrollHorizontally(1));
//
//        if (mRecyclerView.mAdapter != null) {
//            event.setItemCount(mRecyclerView.mAdapter.getItemCount());
//        }
//    }
//
//    // called by accessibility delegate
//    void onInitializeAccessibilityNodeInfoForItem(View host, AccessibilityNodeInfoCompat info) {
//        final RecyclerView.ViewHolder vh = getChildViewHolderInt(host);
//        // avoid trying to create accessibility node info for removed children
//        if (vh != null && !vh.isRemoved() && !mChildHelper.isHidden(vh.itemView)) {
//            onInitializeAccessibilityNodeInfoForItem(mRecyclerView.mRecycler,
//                    mRecyclerView.mState, host, info);
//        }
//    }
//
//    public void onInitializeAccessibilityNodeInfoForItem(@NonNull RecyclerView.Recycler recycler,
//                                                         @NonNull RecyclerView.State state, @NonNull View host,
//                                                         @NonNull AccessibilityNodeInfoCompat info) {
//        int rowIndexGuess = canScrollVertically() ? getPosition(host) : 0;
//        int columnIndexGuess = canScrollHorizontally() ? getPosition(host) : 0;
//        final AccessibilityNodeInfoCompat.CollectionItemInfoCompat itemInfo =
//                AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(rowIndexGuess, 1,
//                        columnIndexGuess, 1, false, false);
//        info.setCollectionItemInfo(itemInfo);
//    }
//
//    public void requestSimpleAnimationsInNextLayout() {
//        mRequestedSimpleAnimations = true;
//    }
//    public int getSelectionModeForAccessibility(@NonNull RecyclerView.Recycler recycler,
//                                                @NonNull RecyclerView.State state) {
//        return AccessibilityNodeInfoCompat.CollectionInfoCompat.SELECTION_MODE_NONE;
//    }
//
//    public int getRowCountForAccessibility(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state) {
//        if (mRecyclerView == null || mRecyclerView.mAdapter == null) {
//            return 1;
//        }
//        return canScrollVertically() ? mRecyclerView.mAdapter.getItemCount() : 1;
//    }
//
//    public int getColumnCountForAccessibility(@NonNull RecyclerView.Recycler recycler,
//                                              @NonNull RecyclerView.State state) {
//        if (mRecyclerView == null || mRecyclerView.mAdapter == null) {
//            return 1;
//        }
//        return canScrollHorizontally() ? mRecyclerView.mAdapter.getItemCount() : 1;
//    }
//
//    public boolean isLayoutHierarchical(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state) {
//        return false;
//    }
//
//    // called by accessibility delegate
//    boolean performAccessibilityAction(int action, @Nullable Bundle args) {
//        return performAccessibilityAction(mRecyclerView.mRecycler, mRecyclerView.mState,
//                action, args);
//    }
//
//    public boolean performAccessibilityAction(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state,
//                                              int action, @Nullable Bundle args) {
//        if (mRecyclerView == null) {
//            return false;
//        }
//        int vScroll = 0, hScroll = 0;
//        switch (action) {
//            case AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD:
//                if (mRecyclerView.canScrollVertically(-1)) {
//                    vScroll = -(getHeight() - getPaddingTop() - getPaddingBottom());
//                }
//                if (mRecyclerView.canScrollHorizontally(-1)) {
//                    hScroll = -(getWidth() - getPaddingLeft() - getPaddingRight());
//                }
//                break;
//            case AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD:
//                if (mRecyclerView.canScrollVertically(1)) {
//                    vScroll = getHeight() - getPaddingTop() - getPaddingBottom();
//                }
//                if (mRecyclerView.canScrollHorizontally(1)) {
//                    hScroll = getWidth() - getPaddingLeft() - getPaddingRight();
//                }
//                break;
//        }
//        if (vScroll == 0 && hScroll == 0) {
//            return false;
//        }
//        mRecyclerView.smoothScrollBy(hScroll, vScroll, null, UNDEFINED_DURATION, true);
//        return true;
//    }
//
//    // called by accessibility delegate
//    boolean performAccessibilityActionForItem(@NonNull View view, int action,
//                                              @Nullable Bundle args) {
//        return performAccessibilityActionForItem(mRecyclerView.mRecycler, mRecyclerView.mState,
//                view, action, args);
//    }
//
//    public boolean performAccessibilityActionForItem(@NonNull RecyclerView.Recycler recycler,
//                                                     @NonNull RecyclerView.State state, @NonNull View view, int action, @Nullable Bundle args) {
//        return false;
//    }
//
//    public static RecyclerView.LayoutManager.Properties getProperties(@NonNull Context context,
//                                                                      @Nullable AttributeSet attrs,
//                                                                      int defStyleAttr, int defStyleRes) {
//        RecyclerView.LayoutManager.Properties properties = new RecyclerView.LayoutManager.Properties();
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerView,
//                defStyleAttr, defStyleRes);
//        properties.orientation = a.getInt(R.styleable.RecyclerView_android_orientation,
//                DEFAULT_ORIENTATION);
//        properties.spanCount = a.getInt(R.styleable.RecyclerView_spanCount, 1);
//        properties.reverseLayout = a.getBoolean(R.styleable.RecyclerView_reverseLayout, false);
//        properties.stackFromEnd = a.getBoolean(R.styleable.RecyclerView_stackFromEnd, false);
//        a.recycle();
//        return properties;
//    }
//
//    void setExactMeasureSpecsFrom(RecyclerView recyclerView) {
//        setMeasureSpecs(
//                View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), View.MeasureSpec.EXACTLY)
//        );
//    }
//
//    boolean shouldMeasureTwice() {
//        return false;
//    }
//
//    boolean hasFlexibleChildInBothOrientations() {
//        final int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View child = getChildAt(i);
//            final ViewGroup.LayoutParams lp = child.getLayoutParams();
//            if (lp.width < 0 && lp.height < 0) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Some general properties that a LayoutManager may want to use.
//     */
//    public static class Properties {
//        /** {@link android.R.attr#orientation} */
//        public int orientation;
//        /** {@link androidx.recyclerview.R.attr#spanCount} */
//        public int spanCount;
//        /** {@link androidx.recyclerview.R.attr#reverseLayout} */
//        public boolean reverseLayout;
//        /** {@link androidx.recyclerview.R.attr#stackFromEnd} */
//        public boolean stackFromEnd;
//    }
//}
