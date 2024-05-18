package com.example.myapplication.testMatrix

import android.animation.ValueAnimator
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.BlurMaskFilter
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.Style
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.graphics.RegionIterator
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.SizeUtils
import com.example.myapplication.R

//canvas 线性或者平移旋转变换，本质就是操作Canvas类内部的 matrix, 每个matrix 其实是对一个点的变换。
//比如在原始坐标先画个矩形，这个矩形内的点会按照平移变换
//canvas.save()  canvas.restore, 会把这个matrix, region(比如clip) 清空
class CanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var drawing = true

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!drawing) {
            return
        }
       // canvas.save()
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
        if (false) {
            shader(canvas)
            return
        }
        if (false) {
            test10(canvas)
            return
        }
        if (false) {
            testPolyToPoly(canvas)
            return
        }
        if (false) {
            testRectToRect(canvas)
            return
        }
        if (false) {
            testBitmapShader(canvas)
            return
        }
        if (false) {
            testRadialGradient(canvas)
            return
        }
        if (true) {
            drawable.draw(canvas)
            return
        }
        val paint = Paint()
        val matrix = Matrix()
        val xiangji = Camera()
        Log.d("zhouzheng pre", matrix.toShortString())
        xiangji.save()
        xiangji.rotateY(30f)
        xiangji.getMatrix(matrix)
        xiangji.restore()
        Log.d("zhouzheng b", matrix.toShortString())
        matrix.postTranslate(width / 2f, height / 2f)
        Log.d("zhouzheng a", matrix.toShortString())
        canvas.concat(matrix)  //这个狠！ 坐标跟着都平移了
        paint.color = 0x33000000
        canvas.drawRect(rectF, paint)

        matrix.preScale(0.5f, 0.5f, 400f, 400f)
        canvas.setMatrix(matrix)
        paint.color = Color.RED
        canvas.drawRect(rectF, paint)
        canvas.restore()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun testRadialGradient(canvas: Canvas) {
        val paint = Paint().apply {
            shader = RadialGradient(
                width / 2f,
                height / 2f,
                dpF(200f),
                intArrayOf(0xff00ff00.toInt(), 0xff0000ff.toInt(), 0xffff0000.toInt()),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }

        //canvas.drawCircle(width/2f, height/2f, 200f, paint)
        //val path = Path()
        //path.addOval(RectF(width/2f - 400, height/2f - 400f, width/2 + 400f, height/2f + 400f), Path.Direction.CW)

        //canvas.drawPath(path, paint)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun testBitmapShader(canvas: Canvas) {
        val options = BitmapFactory.Options()
//        options.outHeight = dp(100f)
//        options.outWidth = dp(100f)
        options.inSampleSize = 1
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.item5, options)

        val paint = Paint().apply {
            shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }

        val newBitmap: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val array = IntArray(newBitmap.width * newBitmap.height)
        newBitmap.getPixels(array, 0, newBitmap.width, 0, 0, newBitmap.width, newBitmap.height)
        //val c
        //这里 bitmap 作为shader啦
        Log.d("zhouzheng", "bitmap w${bitmap.width}")
        val path = Path()
        path.addOval(RectF(100f, 100f, 500f, 500f), Path.Direction.CW)
        val matrix = Matrix()
        //matrix.preTranslate(-bitmap.width.toFloat()/3, bitmap.height.toFloat())
        //matrix.preTranslate(-bitmap.width.toFloat(), -bitmap.height.toFloat())
        //matrix.setRotate(0f,bitmap.width.toFloat()/2,bitmap.height.toFloat()/2)
        // matrix.postTranslate(bitmap.width.toFloat(), bitmap.height.toFloat())
        paint.shader.setLocalMatrix(matrix)
//        canvas.drawPath(path, paint)
//        canvas.drawRect(0f,0f, width.toFloat(), height.toFloat(), paint)
//        //matrix.setRotate(45f,bitmap.width.toFloat()/2,bitmap.height.toFloat()/2)
//        paint.shader.setLocalMatrix(matrix)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        val m = Matrix()
        m.preTranslate(0f, 0.5f * bitmap.height.toFloat())
        paint.shader.setLocalMatrix(m)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun testPolyToPoly(canvas: Canvas) {
        val m = Matrix()
        val paintGreen = Paint().apply {
            style = Style.STROKE
            color = 0xff00ff00.toInt()
            strokeWidth = dpF(2f)
        }
        val redGreen = Paint().apply {
            style = Style.STROKE
            color = 0xffff0000.toInt()
            strokeWidth = dpF(2f)
        }
        val blue = Paint().apply {
            style = Style.STROKE
            color = 0xff0000ff.toInt()
            strokeWidth = dpF(2f)
        }
        val rect0 = floatArrayOf(
            0f, 0f,
            0f, 400f,
            400f, 400f,
            400f, 0f
        )
        val rect1 = floatArrayOf(
            500f, 500f,
            500f, 700f,
            600f, 800f,
            600f, 700f
        )
        val path = Path()
        path.moveTo(rect0[0], rect0[1])
        path.lineTo(rect0[2], rect0[3])
        path.lineTo(rect0[4], rect0[5])
        path.lineTo(rect0[6], rect0[7])
        path.close()
        canvas.drawPath(path, paintGreen)
        path.reset()
        path.moveTo(rect1[0], rect1[1])
        path.lineTo(rect1[2], rect1[3])
        path.lineTo(rect1[4], rect1[5])
        path.lineTo(rect1[6], rect1[7])
        path.close()
        canvas.drawPath(path, redGreen)

        m.setPolyToPoly(rect0, 0, rect1, 0, 4)

        val f = rect0.clone()
        m.mapPoints(f)
        path.reset()
        path.moveTo(f[0], f[1])
        path.lineTo(f[2], f[3])
        path.lineTo(f[4], f[5])
        path.lineTo(f[6], f[7])
        path.close()
        canvas.drawPath(path, blue)

        canvas.setMatrix(m)
        canvas.drawRect(Rect(244, 100, 300, 700), Paint().apply {
            style = Style.STROKE
            color = 0xff000000.toInt()
            strokeWidth = dpF(3f)
        })


        val f1 = RectF(50f, 50f, 200f, 500f)
        val path1 = Path()
        path.addOval(f1, Path.Direction.CW)

        canvas.drawPath(path1, Paint().apply {
            style = Style.STROKE
            color = 0xff000000.toInt()
            strokeWidth = dpF(4f)
        })
    }


    //可以实现局部图放大
    private fun testRectToRect(canvas: Canvas) {
        val m = Matrix()
        canvas.save()
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.item2)
        val rectF1 = RectF(0f, 0f, 300f, 400f)

        val rectF2 = RectF(0f, 0f, width.toFloat(), height.toFloat())

        m.setRectToRect(rectF1, rectF2, Matrix.ScaleToFit.END)

        canvas.concat(m)
        canvas.drawBitmap(bitmap, Matrix(), null)
        canvas.drawRect(100f, 100f, 1000f, 2000f, Paint().apply {
            style = Style.STROKE
            color = 0xff000000.toInt()
            strokeWidth = dpF(4f)
        })

        canvas.restore()
        //投射前
        canvas.clipRect(rectF1)
        canvas.drawBitmap(bitmap, Matrix(), null)
        canvas.drawRect(100f, 100f, 1000f, 2000f, Paint().apply {
            style = Style.STROKE
            color = 0xff000000.toInt()
            strokeWidth = dpF(4f)
        })
    }

    private fun test10(canvas: Canvas) {
        canvas.save()
        val rect = RectF(0f, 0f, 300f, 200f)
        val m = Matrix()
        m.preTranslate(width / 2f, height / 2f)
        canvas.setMatrix(m)
        val paint = Paint().apply {
            color = Color.BLACK
        }
        canvas.drawRect(rect, paint)
        val m2 = Matrix()
        m2.setSinCos(1f, 0f)
        m.preConcat(m2)
        canvas.setMatrix(m)
        paint.setColor(Color.RED)
        canvas.drawRect(rect, paint)
        canvas.drawCircle(0f, 0f, 5f, paint)
        canvas.restore()
    }

    private fun test1(canvas: Canvas) {
        //canvas.save()
        val paint = Paint()
        val rectF = RectF(0f, 0f, 400f, 400f)
        canvas.translate(width / 2f, height / 2f) //并没平移坐标
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
        canvas.drawARGB(0x1a, 0xff, 0x00, 0x00)
        canvas.restoreToCount(layerID)
    }

    private fun test2(canvas: Canvas) {
        val rectF = RectF(0f, 0f, 400f, 400f)
        canvas.translate(width / 2f, height / 2f) //并没平移坐标
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
        canvas.drawColor(0x1aff0000)
        //canvas.restoreToCount(layerID)
    }

    private fun testRegion(canvas: Canvas): Boolean {
        val p = Paint()
        val paint = Paint()
        p.style = Paint.Style.FILL
        paint.color = Color.RED

        val f = Rect(50, 50, 200, 100)
        val region = Region(f)

        val regionIterator = RegionIterator(region)
        val re = Rect()
        while (regionIterator.next(re)) {
            canvas.drawRect(re, paint)
        }

        canvas.clipRect(f)
        canvas.drawColor(0x1A00ff00)
        return true
    }

    private fun testRegion2(canvas: Canvas): Boolean {
        canvas.drawColor(Color.WHITE)
        val p = Paint()
        //p.style = Paint.Style.STROKE
        p.color = Color.RED
        //  p.strokeWidth = 5f

        val f = RectF(50f, 50f, 200f, 500f)
        val path = Path()
        path.addOval(f, Path.Direction.CW)

        val region = Region()
        region.setPath(path, Region(50, 50, 200, 200))

        //canvas.drawPath(path, p)
        canvas.drawRegion(region, p)

//        canvas.drawPath(path, p)
//        canvas.drawCircle( 400f, 400f, 30f, p)
        return true
    }

    private fun testRegion3(canvas: Canvas): Boolean {
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
        canvas.translate(width / 2 - 200f, 100f)
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 100f
            paint.color = 0xff00ff00.toInt()
        }
        val path = Path()
        path.addOval(RectF(0f, 0f, 400f, 400f), Path.Direction.CCW)
        canvas.drawPath(path, paint)

        canvas.translate(0f, 500f)

        paint.apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 100f
            paint.color = 0xff0000ff.toInt()
        }
        canvas.drawPath(path, paint)
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
        var left = width / 2f - 250
        canvas.drawLine(left, 0f, left, height.toFloat(), paint)
        left = width / 2f - 150
        paint.apply {
            color = 0xff000000.toInt()
            strokeWidth = dp(1f).toFloat()
            style = Paint.Style.FILL_AND_STROKE
        }
        canvas.drawLine(left, 0f, left, height.toFloat(), paint)
        left = width / 2f - 200
        paint.apply {
            color = 0xff000000.toInt()
            strokeWidth = dp(1f).toFloat()
            style = Paint.Style.FILL
        }
        canvas.drawLine(left, 0f, left, height.toFloat(), paint)
    }


    //矩形
    private fun testPaint1(canvas: Canvas) {
        canvas.save()
        val paint = Paint()
        //只有STROKE 或者STROKE_FILL 才会激活STROKEWIDTH
        canvas.translate(width / 2 - 200f, 100f)
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 100f
            paint.color = 0xff00ff00.toInt()
        }
        val path = Path()
        path.addRect(RectF(0f, 0f, 400f, 400f), Path.Direction.CCW)
        canvas.drawPath(path, paint)

        canvas.translate(0f, 500f)

        paint.apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 100f
            paint.color = 0xff0000ff.toInt()
        }
        canvas.drawPath(path, paint)
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
        var left = width / 2f - 250
        canvas.drawLine(left, 0f, left, height.toFloat(), paint)
        left = width / 2f - 150
        paint.apply {
            color = 0xff000000.toInt()
            strokeWidth = dp(1f).toFloat()
            style = Paint.Style.FILL_AND_STROKE
        }
        canvas.drawLine(left, 0f, left, height.toFloat(), paint)
        left = width / 2f - 200
        paint.apply {
            color = 0xff000000.toInt()
            strokeWidth = dp(1f).toFloat()
            style = Paint.Style.FILL
        }
        canvas.drawLine(left, 0f, left, height.toFloat(), paint)
    }

    private fun testPath(canvas: Canvas) {
        val path = Path()
        path.moveTo(width / 2f, 200f)
        path.lineTo(width / 2f + 300f, 200f + 300)
        path.lineTo(width / 2f, 200f + 300)
        path.close()
        val paint = Paint().apply {
            style = Paint.Style.FILL
            color = 0xff00ffff.toInt()
            strokeWidth = dpF(20f)
        }
        canvas.drawPath(path, paint)

        val path2 = Path()
        path2.moveTo(width / 2f, 600f)
        path2.lineTo(width / 2f + 300f, 600f + 300)
        path2.lineTo(width / 2f, 600f + 300)
        path2.close()
        paint.apply {
            style = Paint.Style.FILL_AND_STROKE
            color = 0xffff00ff.toInt()
            strokeWidth = dpF(20f)
        }
        canvas.drawPath(path2, paint)

        val path3 = Path()
        path3.moveTo(width / 2f, 1000f)
        path3.lineTo(width / 2f + 300f, 1000f + 300)
        path3.lineTo(width / 2f, 1000f + 300)
        path3.close()
        paint.apply {
            style = Paint.Style.STROKE
            color = 0xff0000ff.toInt()
            strokeWidth = dpF(20f)
        }
        canvas.drawPath(path3, paint)
        canvas.drawLine(width / 2f, 0f, width / 2f, height.toFloat(), paint.apply {
            strokeWidth = dpF(1f)
            color = 0xff000000.toInt()
        })
        canvas.drawLine(
            width / 2f - dpF(10f),
            0f,
            width / 2f - dpF(10f),
            height.toFloat(),
            paint.apply {
                strokeWidth = dpF(1f)
                color = 0xff000000.toInt()
            })
        canvas.drawLine(
            width / 2f + dpF(10f),
            0f,
            width / 2f + dpF(10f),
            height.toFloat(),
            paint.apply {
                strokeWidth = dpF(1f)
                color = 0xff000000.toInt()
            })

        paint.apply {
            strokeWidth = dpF(20f)
            color = 0xff00ff00.toInt()
        }
        canvas.drawLine(width / 2f, 1400f, width / 2f + 300f, 1400f + 300f, paint)
        canvas.drawLine(width / 2f + 300F, 1400f + 300f, width / 2f, 1400f + 300f, paint)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        //setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    private fun drawMask(canvas: Canvas) {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.lover_pop_anim_006)
        val alphaBitmap = bitmap.extractAlpha()
        val paint = Paint().apply {
            color = Color.GRAY
            maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL)
        }
        canvas.drawBitmap(alphaBitmap, null, RectF(80f, 80f, 620f, 620f), paint)

        canvas.save()
        //canvas.translate(-5f, -5f)

        paint.apply {
            maskFilter = null
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = dpF(10f)
        }
        canvas.drawBitmap(
            bitmap,
            null,
            RectF(100f, 100f, 600f, 600f),
            paint
        ) //paint 这里可以认为无效 除非用ColorFilter
        canvas.restore()

    }

    private fun bitmapColorFilter() {

    }


    private fun shader(canvas: Canvas) {
        canvas.drawColor(0x3300ff00)
        val rect = Rect(0, 0, dp(90f), dp(90f))
        rect.offset(dp(30f), dp(30f))
        val lightBgDrawable = LightBgDrawable(dpF(10F), 0xffffffff.toInt()).apply {
            bounds = rect
        }

        lightBgDrawable.draw(canvas = canvas)


        val rect2 = Rect(dp(30f), 500, dp(90f) + dp(30f), dp(90f) + 500)
        val lightBgDrawable2 = LightBgDrawable2(dpF(10F), 0xffffffff.toInt()).apply {
            bounds = rect2
        }
        lightBgDrawable2.draw(canvas = canvas)

        canvas.drawLine(
            dp(30f) + dpF(10f),
            0f,
            dp(30f) + dpF(10f),
            height.toFloat(),
            Paint().apply {
                strokeWidth = dpF(1f)
                color = Color.BLACK
            })

        canvas.drawLine(dp(30f) + dpF(5f), 0f, dp(30f) + dpF(5f), height.toFloat(), Paint().apply {
            strokeWidth = dpF(1f)
            color = Color.BLACK
        })
        canvas.drawLine(dpF(30f), 0f, dpF(30f), height.toFloat(), Paint().apply {
            strokeWidth = dpF(1f)
            color = Color.BLACK
        })
    }

    val drawable = ZZTransitionDrawable()
    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        drawable.callback = this
        drawable.initialRect = RectF(dpF(100f), dpF(100f), dpF(200f), dpF(200F))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        drawable.finalRect = RectF(0f,0f, width.toFloat(), height.toFloat())
        drawable.bounds = Rect(0,0,width,height) //important!!!
        Log.d("zhouzheng", "nana")
        drawable.init()
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who == drawable
    }

    inner class ZZTransitionDrawable: Drawable() {

        private var valueAnimator : ValueAnimator? = null
        private val listener = ValueAnimator.AnimatorUpdateListener {
            Log.d("zhouzheng", "value$bounds$scrollX$scrollY")
            invalidateSelf()

        }
        val path = Path().apply {
            //addRoundRect(currentRectF, dpF(20f), dpF(20f), Path.Direction.CW)
        }
        private val ma= Matrix()
        private val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.item2,
            BitmapFactory.Options().apply {
                inSampleSize = 3
            }
        )
        var initialRect : RectF? = null
            set(value) {
                field = value
            }
        var finalRect: RectF? = null
            set(value) {
                field = value
            }
        fun init() {
                valueAnimator = ValueAnimator.ofFloat(0f,1f)
                valueAnimator?.addUpdateListener(listener)
                valueAnimator?.duration = 3000
                valueAnimator?.start()
        }

        fun calculateCenterDistance(rate: Float): FloatArray {
            val distanceX = ((finalRect?.left ?: 0f) + (finalRect?.right ?: 0f))/2f - ((initialRect?.left ?: 0f) + (initialRect?.right ?: 0f))/2f
            val distanceY = ((finalRect?.top ?: 0f) + (finalRect?.bottom ?: 0f))/2f- ((initialRect?.top ?: 0f) + (initialRect?.bottom ?: 0f))/2f

            return floatArrayOf((distanceX * rate + ((initialRect?.left ?: 0f) + (initialRect?.right ?: 0f))/2f)  ,
                (distanceY * rate +  ((initialRect?.top ?: 0f) + (initialRect?.bottom ?: 0f))/2f))
        }

        fun calculateWithHeightRatio(rate: Float): FloatArray {
            val distanceX = ((finalRect?.right ?: 0f) - (finalRect?.left ?: 0f)) - ((initialRect?.right ?: 0f) - (initialRect?.left ?: 0f))
            val distanceY = ((finalRect?.bottom ?: 0f) - (finalRect?.top ?: 0f))- ((initialRect?.bottom ?: 0f) - (initialRect?.top ?: 0f))

            return floatArrayOf((distanceX * rate + (initialRect?.right ?: 0f) - (initialRect?.left ?: 0f))  ,
                distanceY * rate +  ((initialRect?.bottom ?: 0f) - (initialRect?.top ?: 0f)))
        }

        override fun draw(canvas: Canvas) {
            canvas.save()
            ma.reset()
            //Log.d("zhouzheng draw", "${valueAnimator!!.animatedValue as Float}")
            val rate = if (valueAnimator == null) 0f else valueAnimator!!.animatedValue as Float
            val centerNow = calculateCenterDistance(rate)
            val widhtHeightNow = calculateWithHeightRatio(rate)
            val currentRectF = RectF(centerNow[0] - widhtHeightNow[0]/2,
                centerNow[1] - widhtHeightNow[1]/2, centerNow[0] + widhtHeightNow[0]/2,
                centerNow[1] + widhtHeightNow[1]/2)

            Log.d("zhouzheng drawl", currentRectF.toShortString())
            path.apply {
                reset()
                addRoundRect(currentRectF, dpF(20f), dpF(20f), Path.Direction.CW)
            }
            canvas.clipPath(path)
            ma.setRectToRect(RectF(0f,0f,bitmap.width.toFloat(), bitmap.height.toFloat()),
                currentRectF, Matrix.ScaleToFit.FILL)
            canvas.setMatrix(ma)
            canvas.drawColor(0x3300ff00)

            canvas.drawBitmap(bitmap, Matrix(), null)
            canvas.restore()
        }

        override fun setAlpha(alpha: Int) {

        }

        override fun setColorFilter(colorFilter: ColorFilter?) {

        }

        override fun getOpacity(): Int {
            return PixelFormat.TRANSLUCENT
        }

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
            100000f, (bounds.top + ringThickNess/2), 100000f,
            bounds.bottom - ringThickNess/2,  intArrayOf(0xFFFD00D8.toInt(), 0xFFFF4771.toInt(), 0xFFFFA403.toInt()), floatArrayOf(0f, 0.5f, 1f), Shader.TileMode.CLAMP)
        mShaderMatrix.reset()
        mPaint.apply {
            flags = ANTI_ALIAS_FLAG
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
        //mPaint.maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL)
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
