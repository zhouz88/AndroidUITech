package com.example.myapplication;


import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

import java.lang.reflect.Field;
import java.util.Arrays;

public class DragLayout extends LinearLayout {
    private ViewDragHelper mDragger;

    public DragLayout(Context context) {
        super(context);
        init(context);
    }

    public DragLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mDragger = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                Log.d("zhouzheng", "tryCaptureView");
                return child.getId() == R.id.tv1 || child.getId() == R.id.tv2;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return 1;
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return 1;
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
                Log.d("zhouzheng", "onEdgeTouched  edgeFlags:" + edgeFlags);
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
                mDragger.captureChildView(findViewById(R.id.tv3), pointerId);
                Log.d("zhouzheng", "onEdgeDragStarted  edgeFlags:" + edgeFlags);
            }

            @Override
            public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
            }

            @Override
            public boolean onEdgeLock(int edgeFlags) {
                Log.d("zhouzheng","onEdgeLock  edgeFlags:"+edgeFlags);
                if (edgeFlags == ViewDragHelper.EDGE_LEFT){
                    return true;
                }
                return false;
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                if (releasedChild.getId() == R.id.tv2){
                    TextView tv1= findViewById(R.id.tv1);
                   mDragger.settleCapturedViewAt(tv1.getLeft(),tv1.getTop());
                // mDragger.smoothSlideViewTo(releasedChild,tv1.getLeft(),tv1.getTop());
                //   mDragger.flingCapturedView(0, 0, getLeft(), getRight());
                    invalidate();
//                    postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            requestLayout();
//                        }
//                    }, 2000);
                }
            }
        });
        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT | ViewDragHelper.EDGE_TOP);
    }

    @Override
    public void computeScroll()
    {
        if(mDragger.continueSettling(true))
        {
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragger.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        try {
            rfGet();
        } catch (Exception e) {
            Log.d("zhouzheng", "iu:"+e.getMessage());

        }
        return true;
    }


    /**
     * 显示几个参数
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void rfGet() throws NoSuchFieldException, IllegalAccessException {
        //mInitialEdgesTouched
        Field field = ViewDragHelper.class.getDeclaredField("mInitialEdgesTouched");
        Field field1 = ViewDragHelper.class.getDeclaredField("mEdgeSize");
        field.setAccessible(true);
        field1.setAccessible(true);
        int[] f = (int[]) field.get(mDragger);
        int f0 = field1.getInt(mDragger);
        //Log.d("zhouzheng", Arrays.toString(f) +"val"+f0);
    }


}
