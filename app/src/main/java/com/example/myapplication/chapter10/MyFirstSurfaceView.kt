package com.example.myapplication.chapter10

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi
import com.example.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyFirstSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SurfaceView(context, attrs) {

    private var surfaceHolder: SurfaceHolder? = null
    private var flag = false
    private var bitmap_bg: Bitmap? = null
    private var mSurfaceWidth = 0f
    private var mSurfaceHeight = 0f
    private var mBitposX = 0
    private var mCanvas : Canvas? = null
    private val thread: Thread? = null

    enum class State {
        LEFT,
        RIGHT
    }

    private var state = State.LEFT

    private val BITMAP_STEP = 1

    init {
        holder.addCallback(object : SurfaceHolder.Callback {
            //每个surface是一帧的信息。
            //每次 vsync 来了，choregrapher 发起一次绘制命令-> 集体写入surface到wms -> 汇总surfaceflinger
            //

            override fun surfaceCreated(holder: SurfaceHolder) {
                //此处由window的正常impl 的 traversal的 predrawlistens调用
                Log.d("zhouzheng", Thread.currentThread().name)
                flag = true
                mSurfaceWidth = width.toFloat()
                mSurfaceHeight = height.toFloat()
                val mWidth = (mSurfaceWidth *3/2).toInt()

                val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.item2)
                bitmap_bg = Bitmap.createScaledBitmap(bitmap, mWidth, mSurfaceHeight.toInt(), true)

                GlobalScope.launch(Dispatchers.IO) {
                    while(flag) {
                        //目前的理解是 不同于viewrootimpl, 下面的操作skia处理canvas命令在子线程完成操作写入 surface
                        mCanvas = holder.lockCanvas()
                        mCanvas?.apply {
                            drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                            drawBitmap(bitmap_bg!!, mBitposX.toFloat(), 0f, null)
                        }
                        when (state) {
                            State.LEFT -> {
                                mBitposX -= BITMAP_STEP
                            }
                            State.RIGHT -> {
                                mBitposX += BITMAP_STEP
                            }
                            else ->{}
                        }
                        if (mBitposX <= -mSurfaceWidth/2) {
                            state = State.RIGHT
                        }
                        if (mBitposX >= 0) {
                            state = State.LEFT
                        }
                        holder.unlockCanvasAndPost(mCanvas)
                        delay(4)
                    }
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                flag = false
            }

        })
    }

}