package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.VelocityTrackerCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.ItemTouchUIUtil;
import android.util.Log;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;


/**
 * 使用ItemTouchHelper的实现代码
 */
/*public class QQDeleteTouchHelperCallBack extends ItemTouchHelper.Callback {
    private ArrayList<String> mDatas;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;

    public QQDeleteTouchHelperCallBack(ArrayList<String> datas, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        mDatas = datas;
        mAdapter = adapter;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = ItemTouchHelper.LEFT;
        int flags = makeMovementFlags(dragFlags, swipeFlags);
        return flags;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    *//**
 * 方法一：使用getDefaultUIUtil()实现
 *//*
 *//*
    @Override
    public void onChildDraw(Canvas c,RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,float dX,float dY, int actionState, boolean isCurrentlyActive) {
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        getDefaultUIUtil().onDraw(c,recyclerView,((QQDeleteAdapter.NormalHolder)viewHolder).mItemText,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        getDefaultUIUtil().onDrawOver(c, recyclerView, ((QQDeleteAdapter.NormalHolder)viewHolder).mItemText, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
//        super.clearView(recyclerView, viewHolder);
        getDefaultUIUtil().clearView(((QQDeleteAdapter.NormalHolder)viewHolder).mItemText);
    }

    @Override
    public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
//        super.onSelectedChanged(viewHolder, actionState);
        getDefaultUIUtil().onSelected(((QQDeleteAdapter.NormalHolder)viewHolder).mItemText);
    }*//*

 *//**
 * 方法二：自定义translate对象
 *//*
    private int mMaxWidth = 500;
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            Log.d("qijian", "dx:" + dX);
            if (dX < -mMaxWidth) {
                dX = -mMaxWidth;
            }
            ((QQDeleteAdapter.NormalHolder)viewHolder).mItemText.setTranslationX((int) dX);
        }
    }
}*/



/**
 * 使用ItemTouchHelperExtension的实现代码
 */
public class QQDeleteTouchHelperCallback extends ItemTouchHelperExtension.Callback {
    private ArrayList<String> mDatas;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;

    public QQDeleteTouchHelperCallback(ArrayList<String> datas, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        mDatas = datas;
        mAdapter = adapter;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = ItemTouchHelper.LEFT;
        int flags = makeMovementFlags(dragFlags, swipeFlags);
        return flags;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            ((QQDeleteAdapter.NormalHolder)viewHolder).mItemText.setTranslationX((int) dX);
        }
    }
}

