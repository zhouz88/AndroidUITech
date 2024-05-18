package com.example.myapplication.chapter10

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

class DoubleBufferingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SurfaceView(context, attrs) {

    private val mPaint: Paint = Paint()

    init {
        mPaint.color = Color.RED
        mPaint.textSize = 100f
        holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder) {
               drawText(holder)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {

            }

        })
    }

    private fun drawText(holder: SurfaceHolder) {
        for (i in 0 ..12) {
            val canvas = holder.lockCanvas()
            canvas?.let {
                canvas.drawColor(0x33ff00ff)
                canvas.drawText("${i}", 30f*i, 400f, mPaint)
                Log.d("zhouzheng", ""+i)
            }
            holder.unlockCanvasAndPost(canvas)
            Thread.sleep(800)
        }
    }
}