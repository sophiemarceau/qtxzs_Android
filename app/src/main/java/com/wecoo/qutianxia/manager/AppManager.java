package com.wecoo.qutianxia.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.requestset.CallServer;

import java.util.Stack;

/**
 * Created by mwl on 2016/10/20.
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }
//
//    /**
//     * 获取当前Activity（堆栈中最后一个压入的）
//     */
//    public Activity currentActivity() {
//        Activity activity = activityStack.lastElement();
//        return activity;
//    }
//
//    /**
//     * 结束当前Activity（堆栈中最后一个压入的）
//     */
//    public void finishActivity() {
//        Activity activity = activityStack.lastElement();
//        finishActivity(activity);
//    }

    /**
     * 结束指定的Activity
     */
    private void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishOtherActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
            activityStack.add(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            MobclickAgent.onKillProcess(context);
            CallServer.getInstance().cancelAll();
            finishAllActivity();
//            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            activityMgr.restartPackage(context.getPackageName());
//            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
