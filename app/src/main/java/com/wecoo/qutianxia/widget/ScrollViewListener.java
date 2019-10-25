package com.wecoo.qutianxia.widget;

import android.widget.ScrollView;

/**
 * Created by mwl on 2016/12/6.
 * ScrollView 滑动的接口
 */

public interface ScrollViewListener {
    void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy);
}
