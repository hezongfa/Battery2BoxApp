package com.chenyi.baselib.widget;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.chenyi.baselib.utils.ViewUtil;

/**
 * Created by 屎佛ass on 15-11-26.
 * 帮助显示左滑菜单
 */
public class ItemSlideHelper implements RecyclerView.OnItemTouchListener, GestureDetector.OnGestureListener {


    private static final String TAG = "ItemSlideHelper";


    private final int DEFAULT_DURATION = 200;
    Context context;
    int width;
    private View mTargetView;
    private int mActivePointerId;
    private int mTouchSlop;
    private int mMaxVelocity;
    private int mMinVelocity;
    private int mLastX;
    private int mLastY;
    private boolean mIsDragging;
    private boolean mAutoSlide = true;
    private Animator mExpandAndCollapseAnim;
    private GestureDetectorCompat mGestureDetector;
    private Callback mCallback;
    private int touchOffset = 0;

    public ItemSlideHelper(Context context, Callback callback, int touchOffset) {
        this.context = context;
        this.mCallback = callback;
        this.touchOffset = touchOffset;
        //手势用于处理fling
        mGestureDetector = new GestureDetectorCompat(context, this);

        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaxVelocity = configuration.getScaledMaximumFlingVelocity();
        mMinVelocity = configuration.getScaledMinimumFlingVelocity();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        width = wm.getDefaultDisplay().getWidth();
    }


    public ItemSlideHelper(Context context, Callback callback) {
        this(context, callback, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//        Log.d(TAG, "onInterceptTouchEvent: " + e.getAction() + "rv.getScrollState() ==" + rv.getScrollState());
        int action = MotionEventCompat.getActionMasked(e);
        int x = (int) e.getX();
        int y = (int) e.getY();
        View tmV = mCallback.findTargetView(x, y);
        if (mCallback.checkTouchRule(mCallback.getChildViewHolder(tmV))) {
            return false;
        }


        boolean needIntercept = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(e, 0);
                mLastX = (int) e.getX();
                mLastY = (int) e.getY();

                /*
                 * 如果之前有一个已经打开的项目，当此次点击事件没有发生在右侧的菜单中则返回TRUE，
                 * 如果点击的是右侧菜单那么返回FALSE这样做的原因是因为菜单需要响应Onclick
                 * */
                if (mTargetView != null) {
                    View ass = mCallback.findTargetView(x, y);
                    if (ass != null && ass != mTargetView) {
                        //折叠菜单
                        smoothHorizontalExpandOrCollapse(DEFAULT_DURATION / 2);
                        return true;
                    }
                    return !inView(x, y);
                }

                //查找需要显示菜单的view;
                mTargetView = mCallback.findTargetView(x, y);

                break;
            case MotionEvent.ACTION_MOVE:

                //如果RecyclerView滚动状态不是空闲targetView不是空
                if (rv.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    if (mTargetView != null) {
                        //隐藏已经打开
                        smoothHorizontalExpandOrCollapse(DEFAULT_DURATION / 2);
                        mTargetView = null;
                    }
                    return false;
                }

                //如果正在运行动画 ，直接拦截什么都不做
                if (mExpandAndCollapseAnim != null && mExpandAndCollapseAnim.isRunning()) {
                    return true;
                }


                int deltaX = (x - mLastX);
                int deltaY = (y - mLastY);

                if (Math.abs(deltaY) > Math.abs(deltaX))
                    return false;

                //如果移动距离达到要求，则拦截
                needIntercept = mIsDragging = mTargetView != null && Math.abs(deltaX) >= mTouchSlop;
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                /*
                 * 走这是因为没有发生过拦截事件
                 * */
                if (isExpanded()) {

                    if (inView(x, y)) {
                        // 如果走这那行这个ACTION_UP的事件会发生在右侧的菜单中
//                        Log.d(TAG, "click item");
                    } else {
                        //拦截事件,防止targetView执行onClick事件
                        needIntercept = true;
                    }

                    //折叠菜单
                    if (mAutoSlide) {
                        smoothHorizontalExpandOrCollapse(DEFAULT_DURATION / 2);
                    }
                }

                if (mAutoSlide)
                    mTargetView = null;
                else
                    mAutoSlide = true;
                break;
        }

        return needIntercept;
    }

    public void closeAllSlide() {
        if (isExpanded()) {
            smoothHorizontalExpandOrCollapse(DEFAULT_DURATION / 2);
        }
    }


    private boolean isExpanded() {
        return mTargetView != null && mTargetView.getScrollX() == getHorizontalRange();
    }

    private boolean isCollapsed() {

        return mTargetView != null && mTargetView.getScrollX() == 0;
    }

    /*
     * 根据targetView的scrollX计算出targetView的偏移，这样能够知道这个point
     * 是在右侧的菜单中
     * */
    private boolean inView(int x, int y) {

        if (mTargetView == null || x == 0)
            return false;

        int scrollX = mTargetView.getScrollX();
        int left = mTargetView.getWidth() - scrollX;
        int top = mTargetView.getTop();
        int right = left + getHorizontalRange();
        int bottom = mTargetView.getBottom();
        Rect rect = new Rect(left, top, right, bottom);
        int spanCont = 1;
        if (mCallback != null) {
            spanCont = mCallback.getSpanCount();
        }
        if (x > width / spanCont) {
            return rect.contains(x / spanCont, y);
        } else {
            return rect.contains(x, y);
        }


    }


    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//        Log.d(TAG, "onTouchEvent: " + e.getAction());

        if (mExpandAndCollapseAnim != null && mExpandAndCollapseAnim.isRunning() || mTargetView == null)
            return;

        //如果要响应fling事件设置将mIsDragging设为false
        if (mGestureDetector.onTouchEvent(e)) {
            mIsDragging = false;
            return;
        }


        int x = (int) e.getX();
        int y = (int) e.getY();
        int action = MotionEventCompat.getActionMasked(e);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //RecyclerView 不会转发这个Down事件

                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (mLastX - e.getX());
                if (mIsDragging) {
                    horizontalDrag(deltaX);
                }
                mLastX = x;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (mIsDragging) {
                    if (!smoothHorizontalExpandOrCollapse(0) && isCollapsed())
                        mTargetView = null;

                    mIsDragging = false;
                }

                break;
        }


    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /**
     * 根据touch事件来滚动View的scrollX
     *
     * @param delta
     */
    private void horizontalDrag(int delta) {
        int scrollX = mTargetView.getScrollX();
        int scrollY = mTargetView.getScrollY();
        if ((scrollX + delta) <= 0) {
            mTargetView.scrollTo(0, scrollY);
            return;
        }


        int horRange = getHorizontalRange();
        scrollX += delta;
        if (Math.abs(scrollX) < horRange) {
            mTargetView.scrollTo(scrollX, scrollY);
        } else {
            mTargetView.scrollTo(horRange, scrollY);
        }

//        if (scrollX >= getHorizontalRange() / 2 && mCallback != null) {
        if (scrollX >= ViewUtil.dip2px(mTargetView.getContext(), 50) && mCallback != null) {
            mCallback.onSlideOpen();
        }


    }

    public void Expand() {
        smoothHorizontalExpandOrCollapse(-1);
        mAutoSlide = false;
    }

    /**
     * 根据当前scrollX的位置判断是展开还是折叠
     *
     * @param velocityX 如果不等于0那么这是一次fling事件,否则是一次ACTION_UP或者ACTION_CANCEL
     */
    private boolean smoothHorizontalExpandOrCollapse(float velocityX) {
        if (mTargetView == null) return false;
        int scrollX = mTargetView.getScrollX();
        int scrollRange = getHorizontalRange();

        if (mExpandAndCollapseAnim != null)
            return false;

        int to = 0;
        int duration = DEFAULT_DURATION;

        if (velocityX == 0) {
            //如果已经展一半，平滑展开
            if (scrollX > scrollRange / 2) {
                to = scrollRange;
            }
        } else {


            if (velocityX > 0)
                to = 0;
            else
                to = scrollRange;

            duration = (int) ((1.f - Math.abs(velocityX) / mMaxVelocity) * DEFAULT_DURATION);
        }

        if (to == scrollX)
            return false;

        mExpandAndCollapseAnim = ObjectAnimator.ofInt(mTargetView, "scrollX", to);
        mExpandAndCollapseAnim.setDuration(duration);
        mExpandAndCollapseAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mExpandAndCollapseAnim = null;
                if (isCollapsed())
                    mTargetView = null;

//                Log.d(TAG, "onAnimationEnd");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //onAnimationEnd(animation);
                mExpandAndCollapseAnim = null;

//                Log.d(TAG, "onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mExpandAndCollapseAnim.start();

        return true;
    }


    public int getHorizontalRange() {
        RecyclerView.ViewHolder viewHolder = mCallback.getChildViewHolder(mTargetView);
        return mCallback.getHorizontalRange(viewHolder);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (Math.abs(velocityX) > mMinVelocity && Math.abs(velocityX) < mMaxVelocity) {
            if (!smoothHorizontalExpandOrCollapse(velocityX)) {
                if (isCollapsed())
                    mTargetView = null;
                return true;
            }
        }
        return false;
    }


    public interface Callback {

        int getHorizontalRange(RecyclerView.ViewHolder holder);

        RecyclerView.ViewHolder getChildViewHolder(View childView);

        View findTargetView(float x, float y);

        boolean checkTouchRule(RecyclerView.ViewHolder holder);

        int getSpanCount();

        void onSlideOpen();
    }
}
