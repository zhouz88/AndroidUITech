package com.example.myapplication.tasks;

import android.app.Application;

import com.example.myapplication.launchstarter.task.MainTask;
//import com.taobao.weex.InitConfig;
//import com.taobao.weex.WXSDKEngine;

/**
 * 主线程执行的task
 */
public class InitWeexTask extends MainTask {

    @Override
    public boolean needWait() {
        return true;
    }

    @Override
    public void run() {
//
//        InitConfig config = new InitConfig.Builder().build();
//        WXSDKEngine.initialize((Application) mContext, config);
    }
}
