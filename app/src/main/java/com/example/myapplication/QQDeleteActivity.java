package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchUIUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ActivityQqdeleteBinding;

import java.util.ArrayList;

public class QQDeleteActivity extends AppCompatActivity {
        private ArrayList<String> mDatas = new ArrayList<>();
        
        private ActivityQqdeleteBinding binding;
        private ItemTouchHelper mItemTouchHelper;
        private ItemTouchHelperExtension mItemTouchHelperExtension;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            generateDatas();
            binding = ActivityQqdeleteBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            //线性布局
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            binding.rv.setLayoutManager(linearLayoutManager);
            //添加分割线
            binding.rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

            final QQDeleteAdapter adapter = new QQDeleteAdapter(this, mDatas);
            adapter.setOnBtnClickListener(new OnBtnClickListener() {
                @Override
                public void onDelete(QQDeleteAdapter.NormalHolder holder) {
                    Toast.makeText(QQDeleteActivity.this,"点击delete",Toast.LENGTH_SHORT).show();
                    /**
                     * 使用ItemTouchHelperExtension实现
                     */
                    mDatas.remove(holder);
                    adapter.notifyItemRemoved(holder.getAdapterPosition());
                }

                @Override
                public void onRefresh(QQDeleteAdapter.NormalHolder holder) {
                    Toast.makeText(QQDeleteActivity.this,"点击refresh",Toast.LENGTH_SHORT).show();
                    /**
                     * 使用ItemTouchHelperExtension实现
                     */
                    mItemTouchHelperExtension.closeOpened();
                }
            });
            binding.rv.setAdapter(adapter);

//            /**
//             * 使用ItemTouchHelper实现
//             */
//        mItemTouchHelper = new ItemTouchHelper(new QQDeleteTouchHelperCallback(mDatas,adapter));
//        mItemTouchHelper.attachToRecyclerView(binding.rv);

            /**
             * 使用ItemTouchHelperExtension实现
             */
            mItemTouchHelperExtension = new ItemTouchHelperExtension(new QQDeleteTouchHelperCallback(mDatas,adapter));
            mItemTouchHelperExtension.attachToRecyclerView(binding.rv);
        }

        private void generateDatas() {
            for (int i = 0; i < 200; i++) {
                mDatas.add("第 " + i + " 个item");
            }
        }
    }
