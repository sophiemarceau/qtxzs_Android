package com.wecoo.qutianxia.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.wecoo.qutianxia.application.WKApplication;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.view.DialogView;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by mwl on 2016/10/19.
 * 跟App相关的辅助类
 */
public class AppInfoUtil {

    private static AppInfoUtil AppInfoInstance = null;

    public static AppInfoUtil getInstance() {
        if (AppInfoInstance == null) {
            AppInfoInstance = new AppInfoUtil();
        }
        return AppInfoInstance;
    }

    /**
     * 新的唯一标识算法，imei+android_id+serialno
     */
    public String getTokenM2() {
//        String imei = getDeviceId(WKApplication.getAppContext());
        String AndroidID = android.provider.Settings.System.getString(WKApplication.getAppContext().getContentResolver(), "android_id");
//        String serialNo = getDeviceSerial();
        return AndroidID;
    }

    /**
     * 获取DeviceId
     */
    public String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) WKApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    private String getDeviceSerial() {
        String serial = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {
        }
        return serial;
    }

    /**
     * 获取应用程序名称
     */
    public String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序包名]
     */
    private static String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return context.getPackageName();
        }
    }

    /**
     * 安装apk
     */
    public void instanceApk(Activity context, File file) {
        if (file != null && file.exists()) {
            LogUtil.e("安装apk" + file.toString());
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
            AppManager.getAppManager().finishAllActivity();
        } else {
            LogUtil.e("没有最新的apk");
        }
    }

    /**
     * 通过包名 在应用商店打开应用
     */
    public static void openApplicationMarket(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName(context));
            Intent localIntent = new Intent(Intent.ACTION_VIEW, uri);
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(localIntent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShort(context, "应用市场打开失败");
        }
    }

    // 打电话
    public static void onCallPhone(final Context mContext, final String phone) {
        new DialogView(mContext).createDialog("确认拨打电话 " + phone + " 吗？",
                false, new DialogView.DialogCallback() {
                    @Override
                    public void onSureClick() {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                });
    }

}
