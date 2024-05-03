package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityCoordinateBinding
import com.example.myapplication.databinding.ActivityNestedScrollBinding
import com.example.myapplication.databinding.SmartLayoutBinding
import com.example.myapplication.databinding.SmartRefreshlayoutBinding
import com.gyf.immersionbar.ImmersionBar

class NestedScrollActivity: AppCompatActivity() {
    //private lateinit var binding: ActivityNestedScrollBinding
    //private lateinit var binding1: ActivityCoordinateBinding
    private lateinit var binding2: SmartLayoutBinding

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
        ImmersionBar.with(this).statusBarDarkFont(false).init()

    }
}