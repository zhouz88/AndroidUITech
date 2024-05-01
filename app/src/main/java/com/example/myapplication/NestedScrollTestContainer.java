package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParent3;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;

public class NestedScrollTestContainer extends FrameLayout implements NestedScrollingParent3, NestedScrollingParent2, NestedScrollingParent {


    public NestedScrollTestContainer(Context context) {
        super(context);
    }

    public NestedScrollTestContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollTestContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NestedScrollTestContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        getChildAt(getChildCount() - 1).layout(0,SizeUtils.dp2px(250f), getMeasuredWidth(), getMeasuredHeight()+SizeUtils.dp2px(200f));
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        Log.d("zhouzheng", "onNestedScroll" + dyUnconsumed);
        if (dyUnconsumed < 0 && getScrollY() >= 0) {
            //下滑还有没消费的
            int scrolled = Math.max(dyUnconsumed, -getScrollY());
            consumed[1] = scrolled;
            scrollBy(0, scrolled); //同样 appbar 是会把移动传给rv 通过 onDependentViewChanged(
        }
    }


    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.d("zhouzheng", "onStartNestedScroll");
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        //第一个child 是 路径上接受parent的第一个子view，第二个target 是目标recyclerview
        Log.d("zhouzheng", "onNestedScrollAccepted" +  (child instanceof RecyclerView));
        Log.d("zhouzheng", "onNestedScrollAccepted" +  (target instanceof RecyclerView));
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        Log.d("zhouzheng", "onStopNestedScroll");
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        Log.d("zhouzheng", "我期待");
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        Log.d("zhouzheng", "onNestedPreScroll");
        consumed[0] = 0;
        consumed[1] = 0;  //大于0 上滑
        if (dy > 0) {
            if (getScrollY() == SizeUtils.dp2px(200f)) {
                consumed[1] = 0;
            } else {
                int scrolled = Math.min(dy + getScrollY(), SizeUtils.dp2px(200f)) - getScrollY();
                consumed[1] = scrolled;
                scrollBy(0, consumed[1]); //本质是在处理appbar, appbarlayout 对应 onChildViewsChanged 这一步移动依赖于appbar的rv
            }
        } else {
            //交给小孩先处理
//            if (getScrollY() == 0) {
//                consumed[1] = 0;
//            } else {
//                int scrolled = Math.max(0, getScrollY() + dy) - getScrollY();
//                consumed[1] = scrolled;
//                scrollBy(0,scrolled);
//            }
        }
    }
}
