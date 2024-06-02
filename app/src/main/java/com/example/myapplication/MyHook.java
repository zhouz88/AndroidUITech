package com.example.myapplication;

import android.util.Log;
import android.view.View;

import java.lang.reflect.Method;

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

    @Insert("setContentView")
    @TargetClass(value = "com.example.myapplication.NestedScrollActivity")
    public void hookContentView(View view) {
        long now = System.currentTimeMillis();
        StackTraceElement[] elements = new Throwable().getStackTrace();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            if (i < 14) {
                StackTraceElement element = elements[i];
                builder.append(element.getClassName()).append(".").append(element.getMethodName()).append("\n");
            }
        }
        Log.d("zhouzheng d", builder.toString());
        Class<?> clazz = null;
        try {
            clazz = Class.forName("com.example.myapplication.NestedScrollActivity$_lancet");
            //clazz = Class.forName("com.example.myapplication.utils.Utils");
        } catch (Throwable e) {
            //throw new RuntimeException(e);
            Log.d("zhouzheng 大小是", "");
            StackTraceElement[] elemen = e.getStackTrace();
            StringBuilder builder1 = new StringBuilder();
            for (int i = 0; i < elemen.length; i++) {
                StackTraceElement element = elemen[i];
                builder1.append(element.getClassName()).append(".").append(element.getMethodName()).append("\n");
            }
            Log.d("zhouzheng c", builder1.toString());
        }
        // 获取myStaticMethod方法的Method对象
        try {
            Method[] methods = clazz.getDeclaredMethods();
            Log.d("zhouzheng 方法", clazz.getName());
            for (Method m : methods) {
                m.setAccessible(true);
                Log.d("zhouzheng 方法", m.getName()+":注解"+m.getAnnotations().length);
                Log.d("zhouzheng 方法", m.getName()+":参数"+m.getGenericParameterTypes().length);
                //这里可以看到 com_example_myapplication_MyHook_hookContentView 方法的注解没有了
            }
            //Log.d("zhouzheng 大小是", ""+method.getDeclaredAnnotations().length);
        } catch (Throwable e) {
            //throw new RuntimeException(e);
            Log.d("zhouzheng 大小是", e.getClass().getName());
            StackTraceElement[] elemen = e.getStackTrace();
            StringBuilder builder1 = new StringBuilder();
            for (int i = 0; i < elemen.length; i++) {
                StackTraceElement element = elemen[i];
                builder1.append(element.getClassName()).append(".").append(element.getMethodName()).append("\n");
            }
            Log.d("zhouzheng w", builder1.toString());
        }
        Log.d("zhouzheng", "infalte 和 io总时间"+now);
        try {
            Origin.callVoid();
            Log.d("zhouzheng", "infalte 和 io总时间" + (System.currentTimeMillis() - now));
        }catch (Exception e) {

        }
    }

    private void show(Throwable throwable) {
        StackTraceElement[] elements = throwable.getStackTrace();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
                StackTraceElement element = elements[i];
                builder.append(element.getClassName()).append(".").append(element.getMethodName()).append("\n");
        }
        Log.d("zhouzheng c", builder.toString());
    }
}
