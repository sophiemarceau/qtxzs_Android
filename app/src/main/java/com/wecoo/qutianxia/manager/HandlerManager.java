package com.wecoo.qutianxia.manager;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * Created by mwl on 2016/11/2.
 * Handler管理
 */

public class HandlerManager {

    private static HandlerThread mHanderThread = new HandlerThread("Default");
    private static Handler mHandler = null;

    // 即时的操作
    public static Handler getHandlerThread() {
        if (mHandler == null) {
            mHanderThread.start();
            mHandler = new Handler(mHanderThread.getLooper());
        }
        return mHandler;
    }
    // 定时的操作
    public static Handler getHandlerDelayed() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }
    // 判断当前线程是否是主线程
    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
