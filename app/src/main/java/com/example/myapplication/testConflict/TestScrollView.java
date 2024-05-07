package com.example.myapplication.testConflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.lang.reflect.Field;

public class TestScrollView extends ScrollView {
    public TestScrollView(Context context) {
        super(context);
    }

    public TestScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private View child;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        child = ((LinearLayout)getChildAt(0)).getChildAt(0);
    }


    //不是拦截法 关闭下面就是走requetdiallowtouch
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        if (y >= child.getTop() - getScrollY()
                && y <= child.getBottom() - getScrollY()
                && x >= child.getLeft()
                && x <= child.getRight()) {
            if (ev.getActionMasked() == MotionEvent.ACTION_DOWN ||
                ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
                Log.d("zhouzheng", "拦截");
                return false;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }
}
