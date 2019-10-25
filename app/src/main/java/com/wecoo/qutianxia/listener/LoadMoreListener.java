package com.wecoo.qutianxia.listener;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * Created by wecoo on 2017/5/24.
 * 监听Listview滚动事件，当列表滚动到底部的时候，执行加载
 */

public abstract class LoadMoreListener implements OnScrollListener {

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        switch (scrollState) {
            // 判断滚动到底部且不滚动时，执行加载
            case OnScrollListener.SCROLL_STATE_IDLE:
                if (absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
                    onLoadMore();
                }
                break;
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    public abstract void onLoadMore();
}
