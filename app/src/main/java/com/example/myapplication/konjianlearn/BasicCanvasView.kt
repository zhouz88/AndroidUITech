package com.example.myapplication.konjianlearn

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.ScaleAnimation
import com.blankj.utilcode.util.ToastUtils

class BasicCanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        //setLayerType(LAYER_TYPE_SOFTWARE, null)
        setOnClickListener {
            ToastUtils.showShort("hi, inside", false)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        val paint = Paint()
//        paint.color = 0xffff0000.toInt()
//        paint.style = Paint.Style.STROKE
//        paint.strokeWidth = 50f
//
//        canvas.drawCircle(175f, 175f, 150f, paint)

        canvas.isHardwareAccelerated
        val paint = Paint()
        paint.color = 0xff00ff00.toInt()
        paint.style = Paint.Style.FILL
        val rect = Rect(0, 0, 400,220)
        canvas.drawRect(rect, paint)

        val paint2 = Paint()
        paint2.color = 0xffff0000.toInt()
        paint2.style = Paint.Style.FILL

        val paint3 = Paint()
        paint3.color = 0xff0000ff.toInt()
        paint3.style = Paint.Style.FILL

        canvas.save()
        canvas.translate(100f, 100f)
        canvas.drawRect(rect, paint2)

        canvas.restore()
        canvas.drawRect(rect, paint3)

        val matrix = Matrix()
        //matrix.preTranslate(200f, 100f)
        //matrix.setTranslate(200f, 100f)
        matrix.postRotate(30f)
        canvas.concat(matrix)
        canvas.drawRect(rect, paint)

//        canvas.drawColor(Color.RED)
//        canvas.save()
//        canvas.clipRect(Rect(100, 100, 200, 200))
//        canvas.drawColor(0xff00ff00.toInt())
//
//        canvas.restore()
//
//        canvas.drawColor(0xff0000ff.toInt())
    }
}