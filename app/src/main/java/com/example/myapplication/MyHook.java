package com.example.myapplication;

import android.util.Log;

import me.ele.lancet.base.Origin;
import me.ele.lancet.base.annotations.Insert;
import me.ele.lancet.base.annotations.TargetClass;

public class MyHook {

    @Insert("initGlide")
    @TargetClass(value = "com.example.myapplication.LearningApp")
    public void fd() {
        long now = System.currentTimeMillis();
        Log.d("zhouzheng", "initGlide start"+now);
        try {
            Origin.callVoid();
            Log.d("zhouzheng", "initGlide  end" + (System.currentTimeMillis() - now));
        }catch (Exception e) {

        }
    }

    @Insert("initRetrofit")
    @TargetClass(value = "com.example.myapplication.LearningApp")
    public void ref() {
        long now = System.currentTimeMillis();
        Log.d("zhouzheng", "retrofit start"+now);
        try {
            Origin.callVoid();
            Log.d("zhouzheng", "retrofit end" + (System.currentTimeMillis() - now));
        }catch (Exception e) {

        }
    }

    @Insert("initSDK2")
    @TargetClass(value = "com.example.myapplication.LearningApp")
    public void iniSDK1() {
        long now = System.currentTimeMillis();
        Log.d("zhouzheng", "sdk2 start"+now);
        try {
            Origin.callVoid();
            Log.d("zhouzheng", "sdk2  end" + (System.currentTimeMillis() - now));
        }catch (Exception e) {

        }
    }

    @Insert("initSDK1")
    @TargetClass(value = "com.example.myapplication.LearningApp")
    public void iniSDK2() {
        long now = System.currentTimeMillis();
        Log.d("zhouzheng", "sdk1 start"+now);
        try {
            Origin.callVoid();
            Log.d("zhouzheng", "sdk1  end" + (System.currentTimeMillis() - now));
        }catch (Exception e) {

        }
    }
}