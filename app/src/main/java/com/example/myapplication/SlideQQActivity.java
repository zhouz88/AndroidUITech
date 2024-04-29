package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.SizeUtils;

public class SlideQQActivity extends AppCompatActivity {
    private SlideMenuGroup mSlideMenuGroup;
    private TextView mMainViewTv;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_qq);

        mSlideMenuGroup = (SlideMenuGroup)findViewById(R.id.slide_menu_container);

        //获取mainView及LayoutParams
        View mainView = getLayoutInflater().inflate(R.layout.slide_content_layout,null,false);
        LayoutParams mainLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        //获取menuView及LayoutParams
        int menuWidth = SizeUtils.dp2px(200f);
        LayoutParams menuLayoutParams = new LayoutParams(menuWidth, LayoutParams.WRAP_CONTENT);
        View menuView = getLayoutInflater().inflate(R.layout.slide_menu_layout,null,false);

        mSlideMenuGroup.setView(mainView,mainLayoutParams,menuView,menuLayoutParams);

        mMainViewTv = (TextView)mainView.findViewById(R.id.slide_main_view_text);
        //menu处理
        menuView.findViewById(R.id.menu_apple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMainViewText("苹果");
//                Intent intent = new Intent(SlideQQActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });

        menuView.findViewById(R.id.menu_banana).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMainViewText("香蕉");
            }
        });

        menuView.findViewById(R.id.menu_pear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMainViewText("大鸭梨");
            }
        });

        mainView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
        mainView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainView.requestLayout();
                int va = 1;
                int b = 2;
            }
        }, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("zhouzheng", "1 resume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("zhouzheng", "1 restart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("zhouzheng", "1 onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("zhouzheng", "1 onstop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("zhouzheng", "1 onrestart");
    }

    private void changeMainViewText(String text){
        mMainViewTv.setText(text);
        mSlideMenuGroup.closeMenu();
    }
}

