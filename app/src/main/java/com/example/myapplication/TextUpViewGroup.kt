package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.view.ViewCompat

class TextUpViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    var lastX = 0f
    var lastY = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d("zhouzheng", "跑呀 事件" + ev?.action?:"0")
        if (ev?.action == MotionEvent.ACTION_MOVE) {
            lastX = ev?.x ?: 0f
            lastY = ev?.y ?: 0f
        }
        return ev?.action == MotionEvent.ACTION_MOVE
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("zhouzheng", "跑了 ONTOUCH")
        if (event?.action == MotionEvent.ACTION_MOVE) {
            ViewCompat.offsetLeftAndRight(getChildAt(0), (event!!.x - lastX).toInt())
            ViewCompat.offsetTopAndBottom(getChildAt(0), (event!!.y - lastY).toInt())
            Log.d("zhouzheng", "跑了  UP")
            lastX = event.x
            lastY = event.y
        }
        return super.onTouchEvent(event)
    }
}