package com.example.myapplication;

import android.app.Application;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.view.Choreographer;

import androidx.core.os.TraceCompat;

import com.example.myapplication.adapters.DAdapter;
import com.example.myapplication.utils.LaunchTimer;


/**
 * adb shell am start -W com.example.myapplication/com.example.myapplication.MainActivity
 */

/**
 * zhouzheng@zhouzhengs-MBP platform-tools % adb shell am start -W com.example.myapplication/com.example.myapplication.SlideQQActivity
 * Starting: Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] cmp=com.example.myapplication/.SlideQQActivity }
 * Status: ok
 * LaunchState: WARM
 * Activity: com.example.myapplication/.SlideQQActivity
 * TotalTime: 90    //所有act 启动耗时
 * WaitTime: 102    //AMS 启动act 的时间
 * Complete
 *
 */
public class LearningApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TraceCompat.beginSection("");
        initStrictMode();
//        // traceview 的使用方式:
//        if (BuildConfig.DEBUG) {
//            Debug.startMethodTracing("App");
//        }
            //        new Handler(Looper.getMainLooper()).postDelayed(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        Debug.stopMethodTracing();
//                    }
//                }, 5000
//        );

        initGlide();
        initRetrofit();
        initSDK1();
        initSDK2();
//        if (BuildConfig.DEBUG) {
//            Debug.stopMethodTracing();
//        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        LaunchTimer.INSTANCE.startRecord();
    }

    private void initStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectCustomSlowCalls() //API等级11，使用StrictMode.noteSlowCode
                    .detectDiskReads()  //磁盘读取
                    .detectDiskWrites()  //磁盘写入
                    .detectNetwork()// 网络请求 or .detectAll() for all detectable problems
                    .penaltyLog() //在Logcat 中打印违规异常信息
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .setClassInstanceLimit(DAdapter.class, 1)
                    .detectLeakedClosableObjects() //API等级11
                    .penaltyLog()
                    .build());
        }
    }

//    private void getNetsStat() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return;
//        }
//        Choreographer.getInstance().postFrameCallback();
//        try {
//            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELECOM_SERVICE);
//            NetworkStatsManager manager = (NetworkStatsManager) getSystemService(Context.NETWORK_STATS_SERVICE);
//            manager.querySummary(NetworkCapabilities.TRANSPORT_WIFI, telephonyManager.getSubscriberId(), );
//        } catch (Exception e) {
//
//        }
//    }

    private void initRetrofit() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {}
    }

    private void initGlide() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {}
    }

    private void initSDK1() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {}
    }

    private void initSDK2() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {}
    }

    private void loopLog() {
       // Looper.getMainLooper().setMessageLogging();
    }

}
