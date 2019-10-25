package com.wecoo.qutianxia.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mwl on 2016/12/17.
 * 自定义ViewPager 解决滑动冲突
 */

public class MyViewPager extends ViewPager {

    // 触摸时按下的点 
    private PointF downP = new PointF();
     //触摸时当前的点 
    private PointF curP = new PointF();
    // 点击事件
    private OnSingleTouchListener onSingleTouchListener;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) {
                height = h;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 当拦截触摸事件到达此位置的时候，返回true，
        // 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent  
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //每次进行onTouch事件都记录当前的按下的坐标  
        curP.x = ev.getX();
        curP.y = ev.getY();
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //记录按下时候的坐标
            // 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变  
            downP.x = ev.getX();
            downP.y = ev.getY();
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰  
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰  
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            //在up时判断是否按下和松手的坐标为一个点  
            //如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick  
            if (downP.x == curP.x && downP.y == curP.y) {
                if (onSingleTouchListener != null) {
                    onSingleTouchListener.onSingleTouch();
                }
                return true;
            }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 创建点击事件接口     
     **/
    public interface OnSingleTouchListener {
        void onSingleTouch();
    }

    public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }
}
