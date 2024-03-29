package com.wecoo.qutianxia.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ListView;

import java.util.List;

/**
 * Created by mwl on 2016/10/24.
 * 给Listview添加头部
 */

public abstract class HeaderViewInterface<T> {

    protected Activity mContext;
    LayoutInflater mInflate;
//    private T mEntity;

    HeaderViewInterface(Activity context) {
        this.mContext = context;
        mInflate = LayoutInflater.from(context);
    }

    public boolean fillView(T t, ListView listView) {
        if (t == null) {
            return false;
        }
        if ((t instanceof List) && ((List) t).size() == 0) {
            return false;
        }
//        this.mEntity = t;
        getView(t, listView);
        return true;
    }

    protected abstract void getView(T t, ListView listView);
}
