package com.example.myapplication;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.NestedScrollingChild;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.myapplication.databinding.ActivityFlowLayoutBinding;

import java.util.ArrayList;

public class CoverFlowActivity extends AppCompatActivity {
    private ArrayList<String> mDatas = new ArrayList<>();
    private ActivityFlowLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFlowLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        generateDatas();

        //线性布局
        binding.rv.setLayoutManager(new CoverFlowLayoutManager());

        CoverFlowAdapter adapter = new CoverFlowAdapter(this, mDatas);
        binding.rv.setAdapter(adapter);
    }

    private void generateDatas() {
        for (int i = 0; i < 20; i++) {
            mDatas.add("第 " + i + " 个item");
        }
    }
}
