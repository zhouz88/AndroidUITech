package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.FrameLayout
import android.widget.Scroller
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.example.myapplication.databinding.SmartRefreshlayoutBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ZZSmartRefreshLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        val MAX_DRAG_DISTANCE = SizeUtils.dp2px(60f)
    }
    private val binding = SmartRefreshlayoutBinding.inflate(LayoutInflater.from(context), this)

    private var mMaxVelocity = 0
    private var mMinVelocity = 0

    private val mVelecityTracker: VelocityTracker
    private var isDragging = false
    private var scroller = Scroller(context)
    private val flingRunnable = FlingRunnable()

    init {
        val vg = ViewConfiguration.get(context)
        mMinVelocity = vg.scaledMinimumFlingVelocity
        mMaxVelocity = vg.scaledMaximumFlingVelocity
        mVelecityTracker = VelocityTracker.obtain()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.adapter = com.example.myapplication.adapters.DAdapter(context)
    }

    var downx = 0f
    var downy = 0f
    var distance = 0f

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (flingRunnable.anim) {
            return false
        }
        mVelecityTracker.addMovement(ev)
        // ev.actionMasked multiple points  ev.action: single point touch
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // 1. assure that Downevents is not prevented 2 consumed down events here
                downx = ev.y
                downy = ev.y
                distance = 0f
                isDragging = false
                Log.d("zhouzheng", "down")
                super.dispatchTouchEvent(ev)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                // 如果可以move 就
                if (canDragContent(ev)) {
                    isDragging = true
                   // Log.d("zhouzheng", "isdrag 为true啦")
                } else {
                    isDragging = false
                }
                if (isDragging) {
                    val trueDelta = (ev.y - downy)  * dragRate(distance.toInt(), MAX_DRAG_DISTANCE)
                    Log.d("zhouzheng", "dragragte${dragRate(distance.toInt(), MAX_DRAG_DISTANCE)}")
                    var offset = trueDelta.toInt()
                    if (trueDelta + distance <= 0) {
                        offset = -distance.toInt()
                      //  Log.d("zhouzheng", "isdrag 为false啦")
                        isDragging = false
                    }
                    if (trueDelta + distance >= MAX_DRAG_DISTANCE) {
                        offset = (MAX_DRAG_DISTANCE - distance).toInt()
                    }
                    ViewCompat.offsetTopAndBottom(binding.coordinator, offset)
                    distance += offset
                    downx = ev.y
                    downy = ev.y
                    return true
                }
                downx = ev.y
                downy = ev.y
            }
            MotionEvent.ACTION_UP -> {
                Log.d("zhouzheng", "up")
                if (isDragging) {
                    flingRunnable.anim = true
                    flingRunnable.postOnAnimation()
                    //mVelecityTracker.computeCurrentVelocity(1000, mMaxVelocity.toFloat())
//                    (context as AppCompatActivity).lifecycle.coroutineScope.launch {
//                       delay(2000)
//                       flingRunnable.postOnAnimation()
//                    }
                }
                isDragging = false
            }
            MotionEvent.ACTION_CANCEL -> {

            }
        }
        return super.dispatchTouchEvent(ev)
    }

    inner class FlingRunnable : Runnable {
        var anim = false
        override fun run() {
            if (scroller.computeScrollOffset()) {
               // Log.d("zhouzheng", "手指放开动画啦${-scroller.currY} distrance${distance} detal${before - scroller.currY}")
                val delta = before - scroller.currY
                ViewCompat.offsetTopAndBottom(binding.coordinator, delta.toInt())
                before = scroller.currY.toFloat()
                this@ZZSmartRefreshLayout.postOnAnimation(this)
            } else {
                anim = false
            }
        }

        fun postOnAnimation() {
            anim = true
            before = distance
            scroller.startScroll(0, distance.toInt(), 0, distance.toInt() ,150)
            this@ZZSmartRefreshLayout.postOnAnimation(this)
        }
        var before = 0f
    }

    private fun dragRate(y: Int, total: Int) : Float{
        return Math.sqrt(1 - 1.0 *y/total).toFloat()
    }

    private fun canDragContent(event: MotionEvent): Boolean {
        return (distance > 0 || distance == 0f && event.y - downy > 0) && !canRvScroll() && !canAppbarScroll()
    }

    private fun canAppbarScroll(): Boolean {
        Log.d("zhouzheng", "appbar offset${binding.appbar.top}")
        return binding.appbar.top < 0
    }

    private fun canRvScroll(): Boolean {
      //  Log.d("zhouzheng", "recyclervierw 能下滑吗${binding.rv.canScrollVertically(-1)}")
        return binding.rv.canScrollVertically(-1)
    }
}