package com.wecoo.qutianxia.view.refreshload;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by wecoo on 2017/5/23.
 * 下拉刷新的布局
 */

public class PtrWecooFrameLayout extends PtrFrameLayout {

    public PtrWecooFrameLayout(Context context) {
        super(context);
        initView();
    }

    public PtrWecooFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PtrWecooFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    // 初始化View
    private void initView() {
        PtrHeaderView header = new PtrHeaderView(getContext());
        setHeaderView(header);
        addPtrUIHandler(header);

        autoRefresh(false);// 设置时候自动刷新
        setDurationToClose(200);//设置下拉回弹的时间
        setDurationToCloseHeader(500);//设置刷新完成，头部回弹时间，注意和前一个进行区别
        setKeepHeaderWhenRefresh(true);//设置刷新的时候是否保持头部
        setPullToRefresh(false);//设置下拉过程中执行刷新，我们一般设置为false
        setRatioOfHeaderHeightToRefresh(1.2f);//设置超过头部的多少时，释放可以执行刷新操作
        setResistance(1.7f);//设置下拉的阻尼系数，值越大感觉越难下拉
    }

    float lastx = 0;
    float lasty = 0;
    boolean ismovepic = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastx = ev.getX();
            lasty = ev.getY();
            ismovepic = false;
            return super.onInterceptTouchEvent(ev);
        }
        int x2 = (int) Math.abs(ev.getX() - lastx);
        int y2 = (int) Math.abs(ev.getY() - lasty);
        //滑动图片最小距离检查
        if (x2 > y2) {
            if (x2 >= 100) ismovepic = true;
            return false;
        }
        //是否移动图片(下拉刷新不处理)
        return !ismovepic && super.onInterceptTouchEvent(ev);
    }
}
