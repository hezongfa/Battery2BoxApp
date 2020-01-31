package com.chenyi.baselib.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

/**
 * Created by ass on 2019/3/2.
 * Description
 */
public class PageRecyclerView extends RecyclerView {
    public boolean interceptTouchToLeft = false;

    public void setInterceptTouchToLeft(boolean interceptTouchToLeft) {
        this.interceptTouchToLeft = interceptTouchToLeft;
    }

    public PageRecyclerView(Context context) {
        super(context);
    }

    public PageRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PageRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    float tempx = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // interceptTouch是自定义属性控制是否拦截事件

        boolean toLeft = false;
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            tempx = ev.getX();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            toLeft = tempx - ev.getX() >= 0;
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            tempx = 0;
        }
//        FQL.d("ccccc----tempx=" + tempx);
//        FQL.d("ccccc----x=" + ev.getX());

        if (toLeft && interceptTouchToLeft) {
            ViewParent parent = this;
            // 循环查找ViewPager, 请求ViewPager不拦截触摸事件
            while (!((parent = parent.getParent()) instanceof ViewPager)) {
                // nop
            }

            parent.requestDisallowInterceptTouchEvent(true);
        }


        return super.dispatchTouchEvent(ev);

    }
}
