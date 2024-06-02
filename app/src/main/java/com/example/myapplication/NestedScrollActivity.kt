package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.HandlerThread
import android.os.Process.THREAD_PRIORITY_LOWEST
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.Choreographer
import android.view.Choreographer.FrameCallback
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.view.LayoutInflaterCompat
import com.example.myapplication.databinding.SmartLayoutBinding
import com.gyf.immersionbar.ImmersionBar

class NestedScrollActivity: AppCompatActivity() {
    //private lateinit var binding: ActivityNestedScrollBinding
    //private lateinit var binding1: ActivityCoordinateBinding
   private lateinit var binding2: SmartLayoutBinding

    //private lateinit var binding3: NormalRefreshLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //优雅获取任一控件耗时
        LayoutInflaterCompat.setFactory2(layoutInflater, object: LayoutInflater.Factory2 {
            // 一个 hook
            override fun onCreateView(
                parent: View?,
                name: String,
                context: Context,
                attrs: AttributeSet
            ): View? {
                if (TextUtils.equals("TextView", name)) {
                    //创建自定义textview
                }
                val startTime = System.currentTimeMillis()
                val view = delegate.createView(parent, name, context, attrs)
                Log.d("zhouzheng", "${name}"+(System.currentTimeMillis() - startTime))
                return view
            }

            override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
                return null
            }
        })
        super.onCreate(savedInstanceState)
//        binding = ActivityNestedScrollBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.rv.layoutManager = LinearLayoutManager(this)
//        binding.rv.adapter = com.example.myapplication.adapters.DAdapter(this)

        //cooridinate layout
//        binding1 = ActivityCoordinateBinding.inflate(layoutInflater)
//        setContentView(binding1.root)
//        binding1.rv.layoutManager = LinearLayoutManager(this)
//        binding1.rv.adapter = com.example.myapplication.adapters.DAdapter(this)
//        ImmersionBar.with(this).statusBarDarkFont(false).init()

        //refresh layout
        binding2 = SmartLayoutBinding.inflate(layoutInflater)
        setContentView(binding2.root)

//        binding3 = NormalRefreshLayoutBinding.inflate(layoutInflater)
//        setContentView(binding3.root)
//        binding3.rv.layoutManager = LinearLayoutManager(this)
//        binding3.rv.adapter = com.example.myapplication.adapters.DAdapter(this).apply {
//            runnable = Runnable {
//                rf()
//            }
//        }
        //
        AsyncLayoutInflater(this).inflate(R.layout.smart_layout, null
        ) { view, resid, parent ->
            binding2 = SmartLayoutBinding.bind(view)
            setContentView(binding2.root)
        }

//        nice 和 cgroup
//        THREAD_PRIORITY_LOWEST
//        val t = HandlerThread()
//        t.priority = THREAD_PRIORITY_LOWEST

        ImmersionBar.with(this).statusBarDarkFont(false).init()


        Choreographer.getInstance().postFrameCallback (object : FrameCallback{
            override fun doFrame(frameTimeNanos: Long) {
                if (mStart == 0L) {
                    mStart = System.nanoTime()
                }
                val interval = frameTimeNanos - mStart
                if (interval > 0.16 * 1000_000_000) {
                    //10 倍 16ms就计算下
                    //Log.d("zhouzheng frame", "${count} ${interval}")
                    count = 0
                    mStart = 0
                } else {
                    count++
                }
                Choreographer.getInstance().postFrameCallback(this)
            }
        })
        val intent = Intent(this, SlideQQActivity::class.java)
        startActivity(intent)
    }

    var mStart: Long = 0
    var count = 0
    var max = 160;

    override fun setContentView(view: View?) {
        super.setContentView(view)
    }


//    private fun rf() {
//        //mInitialEdgesTouched
//        val field = SmartRefreshLayout::class.java.getDeclaredField("mRefreshContent")
//        field.isAccessible = true
//        val f = field.get(binding3.refresh)
//
//        Log.d("zhouzheng", ""+(f as RefreshContentWrapper).canRefresh())
//    }
}

