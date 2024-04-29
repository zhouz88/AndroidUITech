package com.example.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;

public class CoverFlowAdapter extends Adapter<CoverFlowAdapter.NormalHolder>{

    private Context mContext;

    public CoverFlowAdapter(Context mContext, ArrayList<String> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    private ArrayList<String> mDatas;

    private int mCreatedHolder = 0;

    private int[] mPics = {R.mipmap.item1, R.mipmap.item2,R.mipmap.item3,R.mipmap.item4,R.mipmap.item5,R.mipmap.item6};

    @NonNull
    @Override
    public NormalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mCreatedHolder++;
        return new NormalHolder(LayoutInflater.from(mContext).inflate(R.layout.item_coverflow, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NormalHolder holder, int position) {
        holder.mTV.setText(mDatas.get(position));
        holder.mImg.setImageResource(mPics[position%mPics.length]);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class NormalHolder extends RecyclerView.ViewHolder {
        public TextView mTV;
        public ImageView mImg;

        public NormalHolder(@NonNull View itemView) {
            super(itemView);

            mTV = itemView.findViewById(R.id.text);
            mImg = itemView.findViewById(R.id.img);
            mTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mTV.getText(), Toast.LENGTH_SHORT);
                }
            });
        }
    }
}


