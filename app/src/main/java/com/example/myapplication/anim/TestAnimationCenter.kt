package com.example.myapplication.anim

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.SizeUtils
import com.example.myapplication.databinding.ActivityStrangeAnimationBinding

class TestAnimationCenter: AppCompatActivity() {

    private lateinit var binding: ActivityStrangeAnimationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStrangeAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ObjectAnimator.ofPropertyValuesHolder(
            binding.myText,
            PropertyValuesHolder.ofFloat("translationY", -SizeUtils.dp2px(400f).toFloat(), 0f))
            .setDuration(5000)
            .start()

        binding.root.postDelayed({
            view2Bitmap(binding.root)
        }, 3000)
    }

    fun view2Bitmap(view: View?): Bitmap? {
        if (view == null) return null
        val drawingCacheEnabled = view.isDrawingCacheEnabled
        val willNotCacheDrawing = view.willNotCacheDrawing()
        view.isDrawingCacheEnabled = true
        view.setWillNotCacheDrawing(false)
        var drawingCache = view.drawingCache
        val bitmap: Bitmap
        if (null == drawingCache || drawingCache.isRecycled) {
            view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            Log.d("zhouzheng", "跑这了${view.measuredHeight}")
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            view.buildDrawingCache()
            drawingCache = view.drawingCache
            if (null == drawingCache || drawingCache.isRecycled) {
                bitmap = Bitmap.createBitmap(
                    view.measuredWidth,
                    view.measuredHeight,
                    Bitmap.Config.RGB_565
                )
                val canvas = Canvas(bitmap)
                view.draw(canvas)
            } else {
                bitmap = Bitmap.createBitmap(drawingCache)
            }
        } else {
            Log.d("zhouzheng", "跑这了2")
            bitmap = Bitmap.createBitmap(drawingCache)
        }
        view.setWillNotCacheDrawing(willNotCacheDrawing)
        view.isDrawingCacheEnabled = drawingCacheEnabled
        return bitmap
    }
}

/**
 * clipChildren 作用于爷ViewGroup，用于限制子View是否可以超出父ViewGroup的范围，默认为true即不可以，
 * 也可以在代码中设置：setClipChildren (boolean clipChildren)，也可以从代码中判断某个ViewGroup的clipChildren值
 * ：boolean getClipChildren()。
 *
 * ————————————————
 *
 *                             版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
 *
 * 原文链接：https://blog.csdn.net/u013394527/article/details/80066654
 */

class CutomeAvoidOverlapview @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}


//https://blog.csdn.net/baidu_27196493/article/details/119147688

// 会在 setbackgrounddrawable 清除
//if ((mPrivateFlags and android.view.View.PFLAG_SKIP_DRAW) != 0){
//    mPrivateFlags = mPrivateFlags and View.PFLAG_SKIP_DRAW.inv()
//    requestLayout = true
//}
class CustomFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

//    init {
//        //剪裁方法二
//        setWillNotDraw(false)
//    }
//
//
//    override fun draw(canvas: Canvas) {
//        canvas.save()
//        canvas.clipRect(0f, 0f, width.toFloat(), height.toFloat())
//        super.draw(canvas)
//        canvas.restore()
//    }
//
    //或者


    init {
        setBackgroundColor(0xff000000.toInt())
    }

    override fun dispatchDraw(canvas: Canvas) {
        Log.d("zhouzheng", "还是跑了 dispatchdraw")
        canvas.save()
        canvas.clipRect(0f, 0f, width.toFloat(), height.toFloat())
        super.dispatchDraw(canvas)
        canvas.restore()
    }

    override fun draw(canvas: Canvas) {
        //rfGet()
        Log.d("zhouzheng", "还是跑了 draw")
        super.draw(canvas)
    }

    override fun onDraw(canvas: Canvas) {
        Log.d("zhouzheng", "还是跑了 ondraw")
        super.onDraw(canvas)
    }
}