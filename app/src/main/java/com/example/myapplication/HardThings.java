package com.example.myapplication;

import android.graphics.Matrix;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HardThings {

    public void checkHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("hello");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

            }
        };

        handlerThread.quit();

    }

}
