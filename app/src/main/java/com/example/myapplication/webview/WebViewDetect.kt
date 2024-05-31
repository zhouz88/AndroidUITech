package com.example.myapplication.webview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.view.View
import java.util.Arrays

  class BlankDetect {
        /**
         * 判断Bitmap是否都是一个颜色
         * @param bitmap
         * @return
         */
        fun isBlank(view: View): Boolean {
            val bitmap = getBitmapFromView(view) ?: return true
            val width = bitmap.width
            val height = bitmap.height
            if (width > 0 && height > 0) {
                val originPix = bitmap.getPixel(0, 0)
                val target = IntArray(width)
                Arrays.fill(target, originPix)
                val source = IntArray(width)
                var isWhiteScreen = true
                for (col in 0 until height) {
                    bitmap.getPixels(source, 0, width, 0, col, width, 1)
                    if (!Arrays.equals(target, source)) {
                        isWhiteScreen = false
                        break
                    }
                }
                return isWhiteScreen
            }
            return false
        }

        /**
         * 从View获取转换到的Bitmap
         * @param view
         * @return
         */
        private fun getBitmapFromView(view: View): Bitmap {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            if (Build.VERSION.SDK_INT >= 11) {
                view.measure(
                    View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY)
                )
                view.layout(
                    view.x.toInt(),
                    view.y.toInt(),
                    view.x.toInt() + view.measuredWidth,
                    view.y.toInt() + view.measuredHeight
                )
            } else {
                view.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            }
            view.draw(canvas)
            return bitmap
        }
    }