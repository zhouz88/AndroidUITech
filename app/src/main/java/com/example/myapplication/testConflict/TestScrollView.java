package com.example.myapplication.testConflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean f = super.onInterceptTouchEvent(ev);
        Log.d("zhouzheng", "拦截了哦" + f);
        return super.onInterceptTouchEvent(ev);
    }
}
