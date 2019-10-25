package com.wecoo.qutianxia.listener;

import android.view.View;
import android.view.View.OnClickListener;

import java.util.Calendar;

/**
 * Created by mwl on 2017/3/6.
 * 防止过快点击造成多次事件
 */

public abstract class NoDoubleClickListener implements OnClickListener {

    private static final int MIN_CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0;

    @Override
    public void onClick(final View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    public abstract void onNoDoubleClick(View v);
}
