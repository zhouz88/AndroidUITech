package com.example.myapplication.testMatrix

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import com.blankj.utilcode.util.SizeUtils

class AnimationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        //view anim
        postDelayed({
            val anim = ScaleAnimation(0f, 0.5f, 1.0f, 1.0f)
            pivotX = width/2f
            pivotY = height/2f
            anim.duration = 5000
            startAnimation(anim)
        }, 3000)
        //Propertyanim
        postDelayed({
            ObjectAnimator.ofPropertyValuesHolder(Wrapper(this@AnimationView),
                PropertyValuesHolder.ofInt("width", 0, width)
            ).setDuration(5000).start()
        }, 3000)
        isVisible = false
    }

    private class Wrapper(val view: View) {

        fun getWidth() : Int{
            return view.layoutParams.width
        }

        fun setWidth(width: Int) {
            view.layoutParams.width = width;
            view.requestLayout()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}