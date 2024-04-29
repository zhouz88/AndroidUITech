package com.example.myapplication;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3dAnimation extends Animation {
    private final float mFromDegrees;
    private final float mEndDegree;
    private float mDepthZ = 400;
    private float mCenterX,mCenterY;
    private final boolean mReverse;
    private Camera mCamera;

    public Rotate3dAnimation(float fromDegrees, float toDegrees,
                             boolean reverse) {
        mFromDegrees = fromDegrees;
        mEndDegree = toDegrees;
        mReverse = reverse;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
        mCenterX = width/2;
        mCenterY = height/2;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float degrees = mFromDegrees + ((mEndDegree - mFromDegrees) * interpolatedTime);

        mCamera.save();
        float z;
        if (mReverse) {
            z = mDepthZ * interpolatedTime;
            mCamera.translate(0.0f, 0.0f, z);
        } else {
            z = mDepthZ * (1.0f - interpolatedTime);
            mCamera.translate(0.0f, 0.0f, z);
        }

        final Matrix matrix = t.getMatrix();
        mCamera.rotateY(degrees);
        mCamera.getMatrix(matrix);
        mCamera.restore();

        matrix.preTranslate(-mCenterX, -mCenterY);
        matrix.postTranslate(mCenterX, mCenterY);

        super.applyTransformation(interpolatedTime, t);
    }
}
