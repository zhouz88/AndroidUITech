package com.example.myapplication.tasks.delayinittask;

import com.example.myapplication.launchstarter.task.MainTask;
//import com.example.myapplication.utils.LogUtils;

public class DelayInitTaskB extends MainTask {

    @Override
    public void run() {
        // 模拟一些操作

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        LogUtils.i("DelayInitTaskB finished");
    }
}
