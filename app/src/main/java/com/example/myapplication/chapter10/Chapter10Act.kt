package com.example.myapplication.chapter10

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar

class Chapter10Act: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TelescopeView(this).apply {
            layoutParams = ViewGroup.LayoutParams(-1,-1)
        })

        ImmersionBar.with(this).init()
    }
}