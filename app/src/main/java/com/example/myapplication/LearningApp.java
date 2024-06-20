package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.example.myapplication.adapters.DAdapter;
import com.example.myapplication.utils.LaunchTimer;
import com.zz.lib1.HiLib;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



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
 */
public class LearningApp extends Application {

    private final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private final ExecutorService service = Executors.newFixedThreadPool(Math.max(2, Math.min(CPU_COUNT - 1, 4)));
    private CountDownLatch mCountDownLatch = new CountDownLatch(1);

    @Override
    public void onCreate() {
        super.onCreate();
        androidx.tracing.Trace.beginSection("appOncreateSystrace");
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
        initAppsFlyer();

        FileUtils.deleteAllInDir(getApplicationContext().getCacheDir());
//        if (BuildConfig.DEBUG) {
//            Debug.stopMethodTracing();
//        }

        androidx.tracing.Trace.endSection();

/**
 * 	 * Hooks all methods with a certain name that were declared in the specified class. Inherited
 * 	 * methods and constructors are not considered. For constructors, use
 * 	 * {@link #hookAllConstructors} instead.
 * 	 *
 * 	 * @param hookClass The class to check for declared methods.
 * 	 * @param methodName The name of the method(s) to hook.
 * 	 * @param callback The callback to be executed when the hooked methods are called.
 * 	 * @return A set containing one object for each found method which can be used to unhook it.
//**/
//        DexposedBridge.hookAllConstructors(ImageView.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                DexposedBridge.findAndHookMethod(ImageView.class, "setImageBitmap",
//                        Bitmap.class, new ImageHook());
//            }
//        });
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
        } catch (Exception e) {
        }
    }

    private void initGlide() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
        }
    }

    private void initSDK1() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
        }
    }

    private void initAppsFlyer() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
        }
    }


    private void initSDK2() {
        try {
            HiLib hi = new HiLib();
            Log.d("zhouzheng", ""+hi.getInt() +":"+  hi.noExp());
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("zhouzheng", "bugs bugts bussss");
        }
    }

    private void loopLog() {
        // Looper.getMainLooper().setMessageLogging();
    }

}
