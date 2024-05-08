package com.example.myapplication.testConflict;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RemoteViews;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.customview.widget.ViewDragHelper;

import java.lang.reflect.Field;

public class MyTextView extends AppCompatTextView {
    public MyTextView(@NonNull Context context) {
        super(context);
    }

    public MyTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    /**
     * 分析: down 事件先确定路径，由于scrollview 对第一个child intercept 为false, down必定在这里消费了，(这个关键，否则down事件都来不了
     * 滑动个屁:)) ，因此，在down事件这里设置不拦截接下里的 scrollview 的move事件 只要不允许他拦截， 一定会走到这里来。 如果在move 事件设置
     * requestDisallowInterceptTouchEvent 那么第一个确定路径的move事件还是会被scrollview拦截 而scrollview 在较大deltay时候会返回true, 这时候就无法拦截了。
     * @param event The motion event.
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }

//        if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
//            getParent().requestDisallowInterceptTouchEvent(true);
//        } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
//            getParent().requestDisallowInterceptTouchEvent(false);
//        }
        return super.onTouchEvent(event);
    }


    /* 书中方法 错误的！！！！
     */
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
//            getParent().requestDisallowInterceptTouchEvent(true);
//        }
//        return super.dispatchTouchEvent(event);
//    }
}
