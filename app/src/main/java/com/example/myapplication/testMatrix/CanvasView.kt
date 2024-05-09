package com.example.myapplication.testMatrix

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.graphics.RegionIterator
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.example.myapplication.R
import java.lang.reflect.Type

//canvas 线性或者平移旋转变换，本质就是操作Canvas类内部的 matrix, 每个matrix 其实是对一个点的变换。
//比如在原始坐标先画个矩形，这个矩形内的点会按照平移变换
//canvas.save()  canvas.restore, 会把这个matrix, region(比如clip) 清空
class CanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        val rectF = RectF(0f, 0f, 400f, 400f)
        if (false) {
            test1(canvas)
            return
        }
        if (false) {
            test2(canvas)
            return
        }
        if (false) {
            testRegion(canvas)
            return
        }
        if (false) {
            testRegion2(canvas)
            return
        }
        if (false) {
            testRegion3(canvas)
            return
        }
        if (false) {
            testRegion3(canvas)
            return
        }
        if (false) {
            testPaint(canvas)
            return
        }
        if (false) {
            testPaint1(canvas)
            return
        }
        if (false) {
            testPath(canvas)
            return
        }
        if (false) {
            drawMask(canvas)
            return
        }
        if (true) {
            shader(canvas)
            return
        }
       val paint = Paint()
        val matrix = Matrix()
        matrix.preTranslate(width/2f, height/2f)

        canvas.concat(matrix)  //这个狠！ 坐标跟着都平移了
        paint.color = 0x33000000
        canvas.drawRect(rectF, paint)

        matrix.preScale(0.5f, 0.5f, 400f, 400f)
        canvas.setMatrix(matrix)
        paint.color = Color.RED
        canvas.drawRect(rectF, paint)
        canvas.restore()
    }

    private fun test1(canvas: Canvas) {
        //canvas.save()
        val paint = Paint()
        val rectF = RectF(0f, 0f, 400f, 400f)
        canvas.translate(width/2f, height/2f) //并没平移坐标
        paint.color = 0x3300ff00
        canvas.drawRect(rectF, paint)

        //canvas.restore()
        val matrix = Matrix()
        matrix.postScale(0.5f, 0.5f, 400f, 400f)
       // canvas.scale(0.5f, 0.5f, 400f, 400f)
        canvas.concat(matrix)
        paint.color = Color.RED
        canvas.drawRect(rectF, paint)

        val r = RectF(0f, 0f, 500f, 500f)
        val layerID = canvas.saveLayer(r, null)
        canvas.drawColor( 0x1aff0000)
        canvas.restoreToCount(layerID)
    }

    private fun test2(canvas: Canvas) {
        val rectF = RectF(0f, 0f, 400f, 400f)
        canvas.translate(width/2f, height/2f) //并没平移坐标
        val paint = Paint()
        paint.color = 0x3300ff00
        canvas.drawRect(rectF, paint)

        //canvas.restore()
        val matrix = Matrix()
        matrix.postScale(0.5f, 0.5f, 400f, 400f)
        // canvas.scale(0.5f, 0.5f, 400f, 400f)
        canvas.concat(matrix)
        paint.color = Color.RED
        canvas.drawRect(rectF, paint)

        val r = RectF(0f, 0f, 500f, 500f)
        // val layerID = canvas.saveLayer(r, null)
        canvas.clipRect(r)
        canvas.drawColor( 0x1aff0000)
        //canvas.restoreToCount(layerID)
    }

    private fun testRegion(canvas: Canvas) : Boolean{
        val p = Paint()
        val paint = Paint()
        p.style = Paint.Style.FILL
        paint.color = Color.RED

        val f = Rect(50,50,200,100)
        val region = Region(f)

        val regionIterator = RegionIterator(region)
        val re = Rect()
        while(regionIterator.next(re)) {
            canvas.drawRect(re, paint)
        }

        canvas.clipRect(f)
        canvas.drawColor(0x1A00ff00)
        return true
    }

    private fun testRegion2(canvas: Canvas) : Boolean{
        canvas.drawColor(Color.WHITE)
        val p = Paint()
        //p.style = Paint.Style.STROKE
        p.color = Color.RED
      //  p.strokeWidth = 5f

        val f = RectF(50f,50f,200f,500f)
        val path = Path()
        path.addOval(f, Path.Direction.CW)

        val region = Region()
        region.setPath(path, Region(50,50,200,200))

        //canvas.drawPath(path, p)
        canvas.drawRegion(region, p)

//        canvas.drawPath(path, p)
//        canvas.drawCircle( 400f, 400f, 30f, p)
        return true
    }

    private fun testRegion3(canvas: Canvas) : Boolean{
        canvas.drawColor(Color.WHITE)

        val rect1 = Rect(100, 100, 400, 200)
        val rect2 = Rect(200, 0, 300, 300)

        val pt = Paint().apply {
            color = 0xff00ff00.toInt()
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawRect(rect1, pt)
        canvas.drawRect(rect2, pt)

        val region1 = Region(rect1)
        val region2 = Region(rect2)

        region1.op(region2, Region.Op.INTERSECT)
        val paint_fill = Paint()
        paint_fill.color = 0xff0000ff.toInt()
        paint_fill.style = Paint.Style.FILL

        canvas.drawRegion(region1, paint_fill)
        return false
    }

    //逻辑总结
    //画直线：
//    找STYLE
//        STROKE StrokeWidth 有效
//        Stroke_fill strokewidth 有效
//         STROKE Stroke 那么Width 有效
//  画圆形
//     找STYLE
//         STROKE Stroke 那么Width有效
//         Stroke_fill 那么strokewidth有效
//         fill 那么strokewidht 无效
    private fun testPaint(canvas: Canvas) {
        canvas.save()
        val paint = Paint()
        //只有STROKE 或者STROKE_FILL 才会激活STROKEWIDTH
        canvas.translate(width/2 - 200f, 100f)
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 100f
            paint.color = 0xff00ff00.toInt()
        }
        val path = Path()
        path.addOval(RectF(0f, 0f, 400f, 400f) , Path.Direction.CCW)
        canvas.drawPath(path, paint)

        canvas.translate(0f, 500f)

        paint.apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 100f
            paint.color = 0xff0000ff.toInt()
        }
        canvas.drawPath(path,paint)
        canvas.translate(0f, 500f)
        paint.apply {
            style = Paint.Style.FILL
            strokeWidth = 100f  //无效
            paint.color = 0xffff0000.toInt()
        }
        canvas.drawPath(path, paint)
        canvas.restore()
        //如果画直线，粗细和Style没关系 只和strokewidth有关
        paint.apply {
            color = 0xff000000.toInt()
            strokeWidth = dp(1f).toFloat()
            style = Paint.Style.STROKE
        }
        var left = width/2f - 250
        canvas.drawLine(left,0f,left,height.toFloat(),paint)
        left = width/2f - 150
        paint.apply {
            color = 0xff000000.toInt()
            strokeWidth = dp(1f).toFloat()
            style = Paint.Style.FILL_AND_STROKE
        }
        canvas.drawLine(left,0f,left,height.toFloat(),paint)
        left = width/2f - 200
        paint.apply {
            color = 0xff000000.toInt()
            strokeWidth = dp(1f).toFloat()
            style = Paint.Style.FILL
        }
        canvas.drawLine(left,0f,left,height.toFloat(),paint)
    }


    //矩形
    private fun testPaint1(canvas: Canvas) {
        canvas.save()
        val paint = Paint()
        //只有STROKE 或者STROKE_FILL 才会激活STROKEWIDTH
        canvas.translate(width/2 - 200f, 100f)
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 100f
            paint.color = 0xff00ff00.toInt()
        }
        val path = Path()
        path.addRect(RectF(0f, 0f, 400f, 400f) , Path.Direction.CCW)
        canvas.drawPath(path, paint)

        canvas.translate(0f, 500f)

        paint.apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 100f
            paint.color = 0xff0000ff.toInt()
        }
        canvas.drawPath(path,paint)
        canvas.translate(0f, 500f)
        paint.apply {
            style = Paint.Style.FILL
            strokeWidth = 100f  //无效
            paint.color = 0xffff0000.toInt()
        }
        canvas.drawPath(path, paint)
        canvas.restore()
        //如果画直线，粗细和Style没关系 只和strokewidth有关
        paint.apply {
            color = 0xff000000.toInt()
            strokeWidth = dp(1f).toFloat()
            style = Paint.Style.STROKE
        }
        var left = width/2f - 250
        canvas.drawLine(left,0f,left,height.toFloat(),paint)
        left = width/2f - 150
        paint.apply {
            color = 0xff000000.toInt()
            strokeWidth = dp(1f).toFloat()
            style = Paint.Style.FILL_AND_STROKE
        }
        canvas.drawLine(left,0f,left,height.toFloat(),paint)
        left = width/2f - 200
        paint.apply {
            color = 0xff000000.toInt()
            strokeWidth = dp(1f).toFloat()
            style = Paint.Style.FILL
        }
        canvas.drawLine(left,0f,left,height.toFloat(),paint)
    }

    private fun testPath(canvas: Canvas) {
        val path = Path()
        path.moveTo(width/2f, 200f)
        path.lineTo(width/2f + 300f, 200f + 300)
        path.lineTo(width/2f, 200f + 300)
        path.close()
        val paint = Paint().apply {
            style = Paint.Style.FILL
            color = 0xff00ffff.toInt()
            strokeWidth = dpF(20f)
        }
        canvas.drawPath(path, paint)

        val path2 = Path()
        path2.moveTo(width/2f, 600f)
        path2.lineTo(width/2f + 300f, 600f + 300)
        path2.lineTo(width/2f, 600f + 300)
        path2.close()
        paint.apply {
            style = Paint.Style.FILL_AND_STROKE
            color = 0xffff00ff.toInt()
            strokeWidth = dpF(20f)
        }
        canvas.drawPath(path2, paint)

        val path3 = Path()
        path3.moveTo(width/2f, 1000f)
        path3.lineTo(width/2f + 300f, 1000f + 300)
        path3.lineTo(width/2f, 1000f + 300)
        path3.close()
        paint.apply {
            style = Paint.Style.STROKE
            color = 0xff0000ff.toInt()
            strokeWidth = dpF(20f)
        }
        canvas.drawPath(path3, paint)
        canvas.drawLine(width/2f, 0f, width/2f,height.toFloat(),paint.apply {
            strokeWidth = dpF(1f)
            color = 0xff000000.toInt()
        })
        canvas.drawLine(width/2f - dpF(10f), 0f, width/2f- dpF(10f),height.toFloat(),paint.apply {
            strokeWidth = dpF(1f)
            color = 0xff000000.toInt()
        })
        canvas.drawLine(width/2f + dpF(10f), 0f, width/2f + dpF(10f),height.toFloat(),paint.apply {
            strokeWidth = dpF(1f)
            color = 0xff000000.toInt()
        })

        paint.apply {
            strokeWidth = dpF(20f)
            color = 0xff00ff00.toInt()
        }
        canvas.drawLine(width/2f, 1400f, width/2f + 300f, 1400f + 300f, paint)
        canvas.drawLine(width/2f + 300F, 1400f + 300f, width/2f, 1400f + 300f, paint)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        //setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    private fun drawMask(canvas: Canvas) {
//        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.lucky_box_end_selected)
//        val alphaBitmap = bitmap.extractAlpha()
//        val paint = Paint().apply {
//            color = Color.GRAY
//            maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL)
//        }
//        canvas.drawBitmap(alphaBitmap, null, RectF(80f, 80f, 620f, 620f), paint)
//
//        canvas.save()
//        //canvas.translate(-5f, -5f)
//
//        paint.apply {
//            maskFilter = null
//            color = Color.RED
//            style = Paint.Style.STROKE
//            strokeWidth = dpF(10f)
//        }
//        canvas.drawBitmap(bitmap, null, RectF(100f, 100f, 600f, 600f), paint) //paint 这里可以认为无效 除非用ColorFilter
//        canvas.restore()

    }

    private fun bitmapColorFilter() {

    }


    private fun shader(canvas: Canvas) {
        canvas.drawColor(0x3300ff00)
        val rect = Rect(0,0,dp(90f), dp(90f))
        rect.offset(dp(30f), dp(30f))
        val lightBgDrawable = LightBgDrawable(dpF(10F), 0xffffffff.toInt()).apply {
            bounds = rect
        }

        lightBgDrawable.draw(canvas = canvas)


        val rect2 = Rect(dp(30f),500,dp(90f)+dp(30f), dp(90f)+500)
        val lightBgDrawable2 = LightBgDrawable2(dpF(10F), 0xffffffff.toInt()).apply {
            bounds = rect2
        }
        lightBgDrawable2.draw(canvas = canvas)

        canvas.drawLine(dp(30f)  + dpF(10f),0f,dp(30f)  + dpF(10f) , height.toFloat(), Paint().apply{
            strokeWidth = dpF(1f)
            color = Color.BLACK
        })

        canvas.drawLine(dp(30f)  + dpF(5f),0f,dp(30f)  + dpF(5f) , height.toFloat(), Paint().apply{
            strokeWidth = dpF(1f)
            color = Color.BLACK
        })
        canvas.drawLine(dpF(30f),0f,dpF(30f) , height.toFloat(), Paint().apply{
            strokeWidth = dpF(1f)
            color = Color.BLACK
        })
    }
}

class LightBgDrawable(val ringThickNess: Float, val circleColor: Int) : Drawable(){
    val mPaint = Paint()
    private val mShaderMatrix = Matrix()
    override fun draw(canvas: Canvas) {
        val width = bounds.width()
        val height = bounds.height()
        Log.d("zhouzheng", "${width}:${height}")
        mPaint.reset()
        mPaint.shader = LinearGradient(
            0f, (bounds.top + ringThickNess/2), 0f,
            bounds.bottom - ringThickNess/2,  intArrayOf(0xFFFD00D8.toInt(), 0xFFFF4771.toInt(), 0xFFFFA403.toInt()), floatArrayOf(0f, 0.5f, 1f), Shader.TileMode.CLAMP)
        mShaderMatrix.reset()
        mPaint.apply {
            strokeWidth = ringThickNess
            style = Paint.Style.STROKE
        }
        mShaderMatrix.setRotate(45f, bounds.right - ringThickNess/2, bounds.top - ringThickNess/2)
        mPaint.shader.setLocalMatrix(mShaderMatrix)
        canvas.drawCircle((bounds.left.toFloat() + bounds.right.toFloat())/2, (bounds.top.toFloat() + bounds.bottom.toFloat())/2, width/2f - ringThickNess/2, mPaint)
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}


fun Canvas.drawRegion(region: Region,paint: Paint) {
    val iter = RegionIterator(region)
    val r = Rect()
    while (iter.next(r)) {
        drawRect(r, paint)
    }
}

class LightBgDrawable2(val ringThickNess: Float, val circleColor: Int) : Drawable(){
    val mPaint = Paint()
    private val mShaderMatrix = Matrix()
    override fun draw(canvas: Canvas) {
        val width = bounds.width()
        val height = bounds.height()
        mPaint.reset()
        mPaint.shader = LinearGradient(
            0f,  bounds.top.toFloat(), 0f,
             bounds.bottom.toFloat(),  intArrayOf(0xFFFD00D8.toInt(), 0xFFFF4771.toInt(), 0xFFFFA403.toInt()), floatArrayOf(0f, 0.5f, 1f), Shader.TileMode.CLAMP)
        mShaderMatrix.reset()
        mShaderMatrix.setRotate(45f,  bounds.right.toFloat(), bounds.top.toFloat())
        mPaint.shader.setLocalMatrix(mShaderMatrix)
        canvas.drawCircle((bounds.left.toFloat() + bounds.right.toFloat())/2, (bounds.top.toFloat() + bounds.bottom.toFloat())/2, width/2f, mPaint)
        mPaint.reset()
        mPaint.color = circleColor
        canvas.drawCircle((bounds.left.toFloat() + bounds.right.toFloat())/2, (bounds.top.toFloat() + bounds.bottom.toFloat())/2, width/2f - ringThickNess, mPaint)
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}

fun dp(t:Float) : Int = SizeUtils.dp2px(t)
fun dpF(t:Float) : Float = SizeUtils.dp2px(t).toFloat()
