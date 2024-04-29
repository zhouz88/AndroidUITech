package com.example.myapplication;



import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.ItemTouchUIUtil;
import android.util.Log;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
public class QQDeleteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mDatas;
    private OnBtnClickListener mOnBtnClickListener;

    public QQDeleteAdapter(Context context, ArrayList<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new QQDeleteAdapter.NormalHolder(inflater.inflate(R.layout.qq_delete_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final NormalHolder normalHolder = (NormalHolder) holder;
        normalHolder.mItemText.setText(mDatas.get(position));
            normalHolder.mDeleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("zhouzheng", "delete");
                    mOnBtnClickListener.onDelete(normalHolder);
                }
            });
            normalHolder.mRefreshTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnBtnClickListener.onRefresh(normalHolder);
                }
            });

    }

    public void setOnBtnClickListener(OnBtnClickListener listener){
        mOnBtnClickListener = listener;
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    /**
     * 使用ItemTouchHelper的实现代码
     */

/*    public class NormalHolder extends RecyclerView.ViewHolder {
        public TextView mRefreshTv;
        public TextView mDeleteTv;
        public TextView mItemText;

        public NormalHolder(View itemView) {
            super(itemView);
            mItemText = (TextView)itemView.findViewById(R.id.operate_tv);
            mDeleteTv = (TextView)itemView.findViewById(R.id.operate_delete);
            mRefreshTv = (TextView)itemView.findViewById(R.id.operate_refresh);
        }
    }*/
    /**
     * 使用ItemTouchHelperExtension的实现代码
     */
    public class NormalHolder extends RecyclerView.ViewHolder implements Extension {
        public TextView mRefreshTv;
        public TextView mDeleteTv;
        public TextView mItemText;
        public LinearLayout mActionRoot;

        public NormalHolder(View itemView) {
            super(itemView);
            mItemText = (TextView)itemView.findViewById(R.id.operate_tv);
            mDeleteTv = (TextView)itemView.findViewById(R.id.operate_delete);
            mRefreshTv = (TextView)itemView.findViewById(R.id.operate_refresh);
            mActionRoot = (LinearLayout)itemView.findViewById(R.id.view_list_repo_action_container);
        }

        @Override
        public float getActionWidth() {
            return mActionRoot.getWidth();
        }
    }
}
