package com.example.myapplication.tasks;

//import com.example.myapplication.PerformanceApp;
import com.example.myapplication.launchstarter.task.Task;

import java.util.ArrayList;
import java.util.List;

//import cn.jpush.android.api.JPushInterface;

/**
 * 需要在getDeviceId之后执行
 */
public class InitJPushTask extends Task {

    @Override
    public List<Class<? extends Task>> dependsOn() {
        List<Class<? extends Task>> task = new ArrayList<>();
        task.add(GetDeviceIdTask.class);
        return task;
    }

    @Override
    public void run() {
//        JPushInterface.init(mContext);
//        PerformanceApp app = (PerformanceApp) mContext;
//        JPushInterface.setAlias(mContext, 0, app.getDeviceId());
    }
}
