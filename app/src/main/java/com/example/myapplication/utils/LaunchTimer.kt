package com.example.myapplication.utils

import android.os.SystemClock
import android.util.Log

object LaunchTimer {
    var sTime: Long = 0;


    fun startRecord() {
        sTime = SystemClock.currentThreadTimeMillis()
    }

    fun endRecord() {
        Log.d("zhouzheng", "time is : ${SystemClock.currentThreadTimeMillis() 
        - sTime}")
    }
}