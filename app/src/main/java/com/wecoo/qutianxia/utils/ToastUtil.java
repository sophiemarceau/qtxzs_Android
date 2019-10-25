package com.wecoo.qutianxia.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by mwl on 2016/10/19.
 * Toast统一管理类
 */
public class ToastUtil {

    private static Toast mToast = null;
    public static int duration = 2800;

    /**
     * Toast定义
     */
    public static void showToast(Context mContext, String text, int duration) {
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, duration);
        mToast.show();
    }

    /**
     * 长时间显示Toast
     */
    public static void showLong(Context mContext,String tvString) {
        showToast(mContext,tvString,Toast.LENGTH_LONG);
    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(Context mContext, String tvString) {
//        showToast(mContext,tvString,Toast.LENGTH_SHORT);
        showToast(mContext,tvString,duration);
    }
}
