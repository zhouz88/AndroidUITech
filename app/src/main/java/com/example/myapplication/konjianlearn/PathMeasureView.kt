package com.example.myapplication.konjianlearn

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.renderscript.Sampler.Value
import android.util.AttributeSet
import android.util.Log
import android.view.View

class PathMeasureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint
    init {
        paint = Paint().apply {
            color = 0xff000000.toInt()
            strokeWidth = 8f
            style = Paint.Style.STROKE
        }
    }

    var test = 0
    override fun onFinishInflate() {
        super.onFinishInflate()
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        setOnClickListener {
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when (test) {
            0 -> test0(canvas)
            1 -> test1(canvas)
            2 -> test2(canvas)
        }
    }

    private fun test1(canvas: Canvas) {
        canvas.translate(150f, 150f)
        val path = Path()
        path.addRect(-50f, -50f, 50f, 50f, Path.Direction.CW)
        path.addRect(-100f, -100f, 100f, 100f, Path.Direction.CW)
        path.addRect(-120f, -120f, 120f, 120f, Path.Direction.CW)

        canvas.drawPath(path, paint)
        val a = ValueAnimator()
        a.start()
        val pathMeasure = PathMeasure(path, false)

        do {
            val len = pathMeasure.length
            Log.d("zhouzheng", "len="+len)
        } while (pathMeasure.nextContour())
    }

    private fun test0(canvas: Canvas) {
        canvas.translate(50f,50f)
        val path = Path()

        path.moveTo(0f ,0f)
        path.lineTo(0f, 100f)
        path.lineTo(100f, 100f)
        path.lineTo(100f, 0f)

        val measure1 = PathMeasure(path, false)
        val measure2 = PathMeasure(path, true)

        Log.d("zhouzheng", ""+measure1.length + ":" + measure1.isClosed)
        Log.d("zhouzheng", ""+measure2.length + ":" + measure2.isClosed)
        canvas.drawPath(path, paint)
    }

    fun test2(canvas: Canvas) {
        canvas.translate(100f, 100f)
        val path = Path()
        path.addRect(-50f, -50f, 50f,50f, Path.Direction.CW)

        val dst = Path()

        dst.lineTo(10f, 100f)
        val pathMeasure = PathMeasure(path, false)

        pathMeasure.getSegment(0f, 150f, dst, false)

        canvas.drawPath(dst, paint)
    }
}