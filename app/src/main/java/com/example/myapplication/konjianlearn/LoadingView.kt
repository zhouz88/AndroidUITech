package com.example.myapplication.konjianlearn

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.util.AttributeSet
import android.util.Log
import android.view.View

class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint: Paint
    private val mDstPath: Path
    private val mCirclePath: Path
    private val mPathMeasure: PathMeasure
    private val animator: ValueAnimator

    private var mCurAnimVal = 0f
    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        mPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = 0xff00ff00.toInt()
        }
        mDstPath = Path()
        mCirclePath = Path()
        mCirclePath.addCircle(100f, 100f, 50f, Path.Direction.CW)
        mPathMeasure = PathMeasure(mCirclePath, true)
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                mCurAnimVal = it.animatedValue as Float
                Log.d("zhouzheng", "anim value" + mCurAnimVal)
                invalidate()
            }
            duration = 2000
        }
    }

    fun startAnim() {
        startAnim = true
        animator.start()
    }

    var startAnim = false

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!startAnim) {
            return
        }

        val len = mPathMeasure.length
        val stop = len * mCurAnimVal

        var start =  (stop - ((0.5f - Math.abs(mCurAnimVal - 0.5f))* len)).toFloat()
        mDstPath.reset()
        canvas.drawColor(0xffffffff.toInt())
        mPathMeasure.getSegment(start, stop, mDstPath, true)
        canvas.drawPath(mDstPath, mPaint)
//        canvas.drawColor(0xffffffff.toInt())
//        val stop = mPathMeasure.length * mCurAnimVal
//
//        mDstPath.reset()
//        mPathMeasure.getSegment(0f, stop, mDstPath, true)
//
//        canvas.drawPath(mDstPath, mPaint)
    }
}