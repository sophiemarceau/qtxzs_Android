package com.wecoo.qutianxia.listener;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;

/**
 * Created by wecoo on 2017/5/24.
 * 监听Listview滚动事件，当列表滚动到底部的时候，执行加载
 */

public abstract class RecycleLoadListener extends OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (!recyclerView.canScrollVertically(1)) {
            onLoadMore();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    public abstract void onLoadMore();
}
