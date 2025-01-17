package com.example.myapplication.doubleTouchTest

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.myapplication.databinding.TestMultiPointsTouchBinding

/**
 * 1.onAttachedToWindow调用顺序：ActivityThread.handleResumeActivity->
 * 拿到activity进拿到window ->WindowManagerImpl.addView->WindowManagerGlobal.addView->
 * ViewRootImpl.performTraversals->ViewGroup.dispatchAttachedToWindow->
 * View.dispatchAttachedToWindow->onAttachedToWindow
 * 而activity 的onattachto window 在decorview 调用
 *
 * 2.onDetachedFromWindow调用顺序：ActivityThread.handleDestroyActivity->
 * 拿到activity进而拿到window r.activity.getWindowManager() ->
 * WindowManagerImpl.removeViewImmediate->WindowManagerGlobal.removeView->
 * ViewRootImpl.die->ViewRootImpl.doDie->ViewRootImpl.dispatchDetachedFromWindow->
 * ViewGroup.dispatchDetachedFromWindow->View.dispatchDetachedFromWindow->onDetachedToWindow
 * ->      destroyHardwareRenderer();
 *
 * 3.onAttachedToWindow和onDetachedFromWindow的调用与visibility无关。
 *
 * 4.onAttachedToWindow是先调用自己，然后调用儿子View的。onDetachedFromWindow是先调用儿子View的，然后再调用自己的。
而activity 的onattachto window 在decorview 调用
 */

//dialog绝大部分window机制和act一样   act 在 resume中调用，但是dialog 在show中wm.addview
/**
 * setcontentview -> getwindow().setcontentvew() -> installdecor -> cb.onContentChanged()
 * 然后 在 handleresumeactivity -> makesible
 *         if (!mWindowAdded) {
 *             ViewManager wm = getWindowManager();
 *             wm.addView(mDecor, getWindow().getAttributes());
 *             mWindowAdded = true;
 *         }
 *         mDecor.setVisibility(View.VISIBLE);
 *         这里 addview 在WindowManagerGlobal 调用，创建viewrootimpl 会添加到
 *              mViews.add(view);
 *             mRoots.add(root);
 *             mParams.add(wparams);
 *             ...
 *                     root.setView(view, wparams, panelParentView, userId);
 *
 *                     最后一步会requestlayout!!! 从而choreographer.postcallback
 *
 *  在makevisible 之前handleresumeactivity方法内会调用 activity.performresume ->       mInstrumentation.callActivityOnResume(this);
 *  -> activiyt.onRESUME!!!!!! 因此 onResume调用时候还没调用requestlayout!
 *
 *
 *  perform launch activity -> activity的 attach方法创建phonewindw
 *
 *  启动流程
 *
 *  https://luisliu.cn/post/android/android-activity_startup/#1-1-ActivityThread%E5%88%9D%E5%A7%8B%E5%8C%96
 *  有的人说是因为这个文件是保护文件，就是注解了是内部保护文件，所以在eclipse，Androidstudio中都是不显示的。所以可以去SDk的安装目录
 *  中的sources文件夹中直接找那个Java文件，/android-sdk/sources/android-19/android/app/ContextImpl.java
 *
 * 其实不是哦，android确实有隐藏文件，但是ContextImpl， ActivityThread，PhoneWindow这些都不是隐藏文件，但是为什么在Eclip
 * se和 AndroidStudio都搜索不到呢，是因为正常搜索的搜的是本项目目录下的文件，正确的方法如下：
 * 双击shift调出类搜索框，需要勾选Include non-project items条目，勾选了以后才会把android所有非隐藏类都包含进来。
 * ————————————————
 *
 *                             版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
 *
 * 原文链接：https://blog.csdn.net/KwunYamShan/article/details/122359973
 *
 * 看源码 open /Users/zhouzheng/Library/Android/sdk/sources/android-34/android/app
 */
class TestActivity: AppCompatActivity() {

    private lateinit var binding: TestMultiPointsTouchBinding

    private lateinit var llayoutParams: WindowManager.LayoutParams
    val callback = Runnable{
        val button = FloatingButton(this)
        button.setBackgroundColor(0x3300ff00)
        //https://stackoverflow.com/questions/46208897/android-permission-denied-for-window-type-2038-using-type-application-overlay
        val LAYOUT_FLAG: Int
        LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        //https://stackoverflow.com/questions/77907401/unable-to-add-window-permission-denied-for-window-type-2038

        llayoutParams = WindowManager.LayoutParams(-2,-2, LAYOUT_FLAG,0,PixelFormat.TRANSPARENT)
        llayoutParams .flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN

        llayoutParams .apply {
            gravity = Gravity.LEFT or Gravity.TOP
            x = 0
            y = 0
        }

        windowManager.addView(button, llayoutParams )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TestMultiPointsTouchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!Settings.canDrawOverlays(this)) {
            // ask for permission
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 1)
        } else {
            callback.run()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {

            binding.root.postDelayed(callback, 2000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.root.removeCallbacks(callback)
    }

    override fun onContentChanged() {
        super.onContentChanged()
        //onContentChanged
    }


    inner class FloatingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null
    ) : AppCompatButton(context, attrs) {


        override fun onTouchEvent(event: MotionEvent): Boolean {
            val rawx = event.rawX.toInt()
            val rawy = event.rawY.toInt()
            when (event.actionMasked) {
                MotionEvent.ACTION_MOVE -> {
                    llayoutParams.x = rawx
                    llayoutParams.y = rawy
                    windowManager.updateViewLayout(this, llayoutParams)
                }
                MotionEvent.ACTION_DOWN -> {
                    return true
                }
            }
            return super.onTouchEvent(event)
        }
    }
}

class TestView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.actionMasked == MotionEvent.ACTION_DOWN) {
            Log.d("zhouzheng", "上面 down")
            return true
        }
        if (event?.actionMasked == MotionEvent.ACTION_MOVE) {
            Log.d("zhouzheng", "上面 move")
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}

class Test2View @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.d("zhouzheng", "下面 down吗1 ${event?.actionMasked == MotionEvent.ACTION_DOWN}")

        val s = super.dispatchTouchEvent(event)
        Log.d("zhouzheng", "下面 down吗3 ${event?.actionMasked == MotionEvent.ACTION_DOWN}")

        return s
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("zhouzheng", "下面 down吗2 ${event?.actionMasked == MotionEvent.ACTION_DOWN}")
        if (event?.actionMasked == MotionEvent.ACTION_DOWN) {
            Log.d("zhouzheng", "下面 pointer down")
            return true
        }
        if (event?.actionMasked == MotionEvent.ACTION_MOVE) {
            Log.d("zhouzheng", "下面 move")
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}

class TestLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.actionMasked == MotionEvent.ACTION_MOVE) {
            Log.d("zhouzheng", "分发啦 move start")
        }
        Log.d("zhouzheng", "分发啦1:" + event?.actionMasked)
        val res = super.dispatchTouchEvent(event)
        Log.d("zhouzheng", "分发啦2:" + event?.actionMasked)
        if (event?.actionMasked == MotionEvent.ACTION_MOVE) {
            Log.d("zhouzheng", "分发啦 move end")
        }
        return res
    }
}

/**
 * .example.myapplication            D  分发啦 move start
 * 2024-05-11 15:13:07.241 22747-22747 zhouzheng               com.example.myapplication            D  下面 move
 * 2024-05-11 15:13:07.241 22747-22747 zhouzheng               com.example.myapplication            D  上面 move
 * 2024-05-11 15:13:07.241 22747-22747 zhouzheng               com.example.myapplication            D  分发啦 move end
 */