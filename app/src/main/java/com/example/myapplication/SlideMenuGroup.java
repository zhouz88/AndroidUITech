package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

public class SlideMenuGroup extends FrameLayout {
    private ViewDragHelper mViewDragHelper;
    private View mMainView;
    private View mDrawer;
    int mMenuViewWidth = 500;

    public SlideMenuGroup(Context context) {
        super(context, null);
        initView();
    }

    public SlideMenuGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SlideMenuGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        if (mViewDragHelper != null) {
            return;
        }
        /**
         * ViewDragHelper 通常定义在一个ViewGroup的内部，并通过其静态工厂方法进行初始化。
         *它的第一个参数是要监听的View，通常需要是一个ViewGroup，即parentView；第二个参数是一个Callback 回调，这个回调就是整个ViewDragHelp的逻辑核心
         */
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            /**
             * 通过这个方法，我们可以指定在创建ViewDragHelper时，参数parentView中的哪个子View可以被移动，
             * 例如这个实例中自定义了一个ViewGroup，里面定义了两个 子View（menuView mainView）,
             * 当指定如下代码时，则只有MainView是可以被拖动的。
             * @param child
             * @param pointerId
             * @return
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //若果当前触摸的child是mMainView时开始检测
                return child == mMainView;
            }

            /**
             * 水平方向的滑动。默认返回值是0，即不发生滑动。
             * @param child
             * @param left 水平方向上child移动的距离。
             * @param dx 比较前一次的增量
             * @return
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (left > 0) {
                    return Math.min(left, mMenuViewWidth);
                }
                return 0;
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return 1;
            }

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return 1;
            }

            /**
             * 拖拽结束时调用
             * 当然，这个方法内部是通过Scroller来实现的，这也是需要重写computeScroll()方法的原因。
             * @param releasedChild
             * @param xvel
             * @param yvel
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                //手指抬起后缓慢移动到指定位置
                if (mMainView.getLeft() < mMenuViewWidth / 2) {
                    //关闭菜单，相当于Scroller的startScroll方法
                    mViewDragHelper.smoothSlideViewTo(mMainView, 0, 0);
                } else {
                    //打开菜单
                    mViewDragHelper.smoothSlideViewTo(mMainView, mMenuViewWidth, 0);
                }
                invalidate();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);

                float percent = mMainView.getLeft() / (float) mMenuViewWidth;
                excuteAnimation(percent);
            }

        });
    }

    private void excuteAnimation(float percent) {
        mDrawer.setScaleX(0.5f + 0.5f * percent);
        mDrawer.setScaleY(0.5f + 0.5f * percent);

        mMainView.setScaleX(1 - percent * 0.2f);
        mMainView.setScaleY(1 - percent * 0.2f);

        mDrawer.setTranslationX( -mMenuViewWidth / 4 + mMenuViewWidth / 4 * percent);
    }

    public void setView(View mainView, LayoutParams mainLayoutParams,
                        View menuView, LayoutParams menuLayoutParams) {
        mDrawer = menuView;
        addView(menuView, menuLayoutParams);
        mMenuViewWidth = menuLayoutParams.width;

        mMainView = mainView;
        addView(mainView, mainLayoutParams);
    }


    /**
     * 重写事件拦截方法，将事件传给ViewDragHelper进行处理
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**将触摸事件传递给ViewDragHelper，此操作必不可少
         */
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mViewDragHelper.smoothSlideViewTo(mMainView, 0, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }
}

