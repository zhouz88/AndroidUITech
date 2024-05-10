package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.customview.widget.ViewDragHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.NormalRefreshLayoutBinding
import com.example.myapplication.databinding.SmartLayoutBinding
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshContent
import com.scwang.smart.refresh.layout.wrapper.RefreshContentWrapper

class NestedScrollActivity: AppCompatActivity() {
    //private lateinit var binding: ActivityNestedScrollBinding
    //private lateinit var binding1: ActivityCoordinateBinding
   private lateinit var binding2: SmartLayoutBinding

    //private lateinit var binding3: NormalRefreshLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
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
        ImmersionBar.with(this).statusBarDarkFont(false).init()


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