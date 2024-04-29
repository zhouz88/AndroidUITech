package com.example.myapplication;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class ClockViewGroup extends LinearLayout {
    private int mCenterX;
    private int mCenterY;

    private float mCanvasRotateX = 0;
    private float mCanvasRotateY = 0;
    private static final float MAX_ROTATE_DEGREE = 20;
    private ValueAnimator mSteadyAnim;
    private Matrix mMatrixCanvas = new Matrix();
    private Camera mCamera = new Camera();

    public ClockViewGroup(Context context) {
        super(context);
    }

    public ClockViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mMatrixCanvas.reset();

        mCamera.save();
        mCamera.rotateX(mCanvasRotateX);
        mCamera.rotateY(mCanvasRotateY);
        mCamera.getMatrix(mMatrixCanvas);
        mCamera.restore();

        //将中心点移到图片中心
        mMatrixCanvas.preTranslate(-mCenterX, -mCenterY);
        mMatrixCanvas.postTranslate(mCenterX, mCenterY);


        canvas.save();
        canvas.setMatrix(mMatrixCanvas);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int action = event.getActionMasked();
        switch (action) {
        case MotionEvent.ACTION_DOWN: {
            cancelSteadyAnimIfNeed();
            rotateCanvasWhenMove(x, y);
            return true;
        }
        case MotionEvent.ACTION_MOVE: {
            rotateCanvasWhenMove(x, y);
            return true;
        }
        case MotionEvent.ACTION_UP: {
            startNewSteadyAnim();
            return true;
        }
        }
        return super.onTouchEvent(event);
    }

    private void rotateCanvasWhenMove(float x, float y) {
        float dx = x - mCenterX;
        float dy = y - mCenterY;

        float percentX = dx / (getWidth() / 2);
        float percentY = dy / (getHeight() / 2);

        if (percentX > 1f) {
            percentX = 1f;
        } else if (percentX < -1f) {
            percentX = -1f;
        }
        if (percentY > 1f) {
            percentY = 1f;
        } else if (percentY < -1f) {
            percentY = -1f;
        }

        mCanvasRotateY = MAX_ROTATE_DEGREE * percentX;
        mCanvasRotateX = -(MAX_ROTATE_DEGREE * percentY);

        postInvalidate();
    }

    /**
     * 补充ValueAnimator.ofPropertyValuesHolder的使用
     */
    private void startNewSteadyAnim() {
        final String propertyNameRotateX = "mCanvasRotateX";
        final String propertyNameRotateY = "mCanvasRotateY";

        PropertyValuesHolder holderRotateX = PropertyValuesHolder.ofFloat(propertyNameRotateX, mCanvasRotateX, 0);
        PropertyValuesHolder holderRotateY = PropertyValuesHolder.ofFloat(propertyNameRotateY, mCanvasRotateY, 0);
        mSteadyAnim = ValueAnimator.ofPropertyValuesHolder(holderRotateX, holderRotateY);
        mSteadyAnim.setDuration(1000);
        mSteadyAnim.setInterpolator(new BounceInterpolator());
        mSteadyAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCanvasRotateX = (float) animation.getAnimatedValue(propertyNameRotateX);
                mCanvasRotateY = (float) animation.getAnimatedValue(propertyNameRotateY);
                postInvalidate();
            }
        });
        mSteadyAnim.start();
    }

    private void cancelSteadyAnimIfNeed() {
        if (mSteadyAnim != null && (mSteadyAnim.isStarted() || mSteadyAnim.isRunning())) {
            mSteadyAnim.cancel();
        }
    }
}
