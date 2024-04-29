package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

import java.util.concurrent.CountDownLatch;

public class WholeToggleButton extends ViewGroup {
    private boolean mIsOpen = false;
    private Scroller mScroller;
    private int mSliderWidth,mScrollerWidth;
    public WholeToggleButton(Context context) {
        super(context);
        init(context);
    }

    public WholeToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WholeToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mScroller = new Scroller(context);
        setBackgroundResource(R.mipmap.background);
        ImageView slide = new ImageView(context);
        slide.setBackgroundResource(R.mipmap.slide);
        slide.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mIsOpen) {
                    mScroller.startScroll(-mScrollerWidth, 0, mScrollerWidth, 0, 500);
                } else {
                    mScroller.startScroll(0, 0, - mScrollerWidth, 0, 500);
                }
                mIsOpen = !mIsOpen;
                invalidate();
            }
        });
        addView(slide);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Drawable bgDrawable = getResources().getDrawable(R.mipmap.background);
        setMeasuredDimension(bgDrawable.getIntrinsicWidth(),bgDrawable.getIntrinsicHeight());
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mSliderWidth = getMeasuredWidth() / 2;
        mScrollerWidth = getMeasuredWidth() - mSliderWidth;
        View view = getChildAt(0);
        view.layout(0, 0, mSliderWidth, getMeasuredHeight());
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }
    }

    private int mLastX;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mLastX = (int) ev.getX();
        Log.d("qijian","onInterceptTouchEvent--mLastX:"+mLastX  +"  event:"+ev.getAction());
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = mLastX - x;
                //边界检测判断，防止滑块越界
                if (deltaX + getScrollX() > 0) {
                    this.scrollTo(0, 0);
                } else if (deltaX + getScrollX() < -mSliderWidth) {
                    this.scrollTo(-mScrollerWidth, 0);
                } else {
                    this.scrollBy(deltaX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                smoothScroll();
                break;
        }
        mLastX = x;
        return super.onTouchEvent(event);
    }

    private void smoothScroll() {
        int bound =  - getMeasuredWidth() / 4;
        int deltaX = 0;
        if (getScrollX() < bound) {
            deltaX = - mScrollerWidth - getScrollX();
            if (!mIsOpen) {
                mIsOpen = true;
            }
        }

        if (getScrollX() >= bound) {
            deltaX = -getScrollX();
            if (mIsOpen) {
                mIsOpen = false;
            }
        }
        mScroller.startScroll(getScrollX(), 0, deltaX, 0, 500);
        invalidate();
    }
}
