package com.wecoo.qutianxia.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * Created by mwl on 2016/12/29.
 * 可滑动控件的配置
 */

public class AttachUtil {

    public static boolean isAdapterViewAttach(AbsListView listView) {
        if (listView != null && listView.getChildCount() > 0) {
            if (listView.getChildAt(0).getTop() < 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isRecyclerViewAttach(RecyclerView recyclerView) {
        if (recyclerView != null && recyclerView.getChildCount() > 0) {
            if (recyclerView.getChildAt(0).getTop() < 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isScrollViewAttach(ScrollView scrollView) {
        if (scrollView != null) {
            if (scrollView.getScrollY() > 0) {
                return false;
            }
        }
        return true;
    }

    // true WebView不滑
    public static boolean isWebViewAttach(WebView webView) {
        if (webView != null) {
            if (webView.getScrollY() > 0) {
                return false;
            }else if (webView.getScrollY() == 0){
                return true;
            }
        }
        return true;
    }

    public static boolean isViewAttach(View view) {
        if (view != null) {
            if (view.getScrollY() > 0) {
                return false;
            }
        }
        return true;
    }
}
