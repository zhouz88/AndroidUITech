package com.example.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

public class WakeLockUtils {

    private WakeLockUtils() {}
    public static volatile WakeLockUtils wakeLockUtils;

    public static WakeLockUtils getInstance() {
        if (wakeLockUtils == null) {
           synchronized (WakeLockUtils.class) {
               if (wakeLockUtils == null) {
                   wakeLockUtils = new WakeLockUtils();
               }
           }
        }
        return wakeLockUtils;
    }

    private PowerManager.WakeLock sWakeLock;

    public void acquire(Context context){
        if(sWakeLock == null){
            sWakeLock = createWakeLock(context);
        }
        if(sWakeLock != null && !sWakeLock.isHeld()){
            sWakeLock.acquire();
            sWakeLock.acquire(1000);
        }
    }

    public  void release(){
        // 一些逻辑
        try{

        }catch (Exception e){

        }finally {
            // 为了演示正确的使用方式
            if(sWakeLock != null && sWakeLock.isHeld()){
                sWakeLock.release();
                sWakeLock = null;
            }
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    private PowerManager.WakeLock createWakeLock(Context context){
        PowerManager pm = (PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if(pm != null){
            return pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"");
        }
        return null;
    }

}
