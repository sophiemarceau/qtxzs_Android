package com.wecoo.qutianxia.application;

import android.app.Application;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.wecoo.qutianxia.manager.CrashManager;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.models.FilterEntity;
import com.wecoo.qutianxia.utils.LogUtil;
import com.wecoo.qutianxia.view.wheelcity.AsyncAdress;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.URLConnectionNetworkExecutor;
import com.yolanda.nohttp.cache.DiskCacheStore;
import com.yolanda.nohttp.cookie.DBCookieStore;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by mwl on 2016/10/19.
 * WKApplication
 */
public class WKApplication extends Application {

    private static WKApplication _instance = null;
    // bebug开关 正式上线应该关闭
//    private boolean isDebugSwitch = true;
    private boolean isDebugSwitch = false;

    public List<FilterEntity> IndustryList = null;// 意向行业
    public List<FilterEntity> BudgetList = null;  // 投资预算
    public List<FilterEntity> PlanTimeList = null;// 计划启动时间

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;

        debugSwitch();
        initCrashHandler(); // 初始化程序崩溃捕捉处理
        initUmeng();
        initNoHttp();
        initPush();
    }

    /**
     * 友盟 初始化
     **/
    private void initUmeng(){
        UMShareAPI.get(this);
//        Config.IsToastTip = false;
        Config.DEBUG = isDebugSwitch;
        MobclickAgent.setDebugMode(isDebugSwitch);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }
    //各个平台的配置，建议放在全局Application或者程序入口
    {
        //微信 String id, String secret
        PlatformConfig.setWeixin("wxb72874c1e7f529a7", "23480ae17eccb26e65cfd3c4a61b2740");
        //tentent String id, String key
        PlatformConfig.setQQZone("1105869254","C0ODcXutokkDN8K4");
    }

    /**
     * NoHttp 初始化
     **/
    private void initNoHttp() {
        Logger.setDebug(isDebugSwitch); // 开启NoHttp调试模式。
        Logger.setTag("NoHttpSample"); // 设置NoHttp打印Log的TAG。

        NoHttp.initialize(this, new NoHttp.Config()
                // 如果不设置，默认全局超时时间是10s。
                .setConnectTimeout(30 * 1000) // 全局连接超时时间，单位毫秒。
                .setReadTimeout(30 * 1000) // 全局服务器响应超时时间，单位毫秒。
//                .setCacheStore(new DBCacheStore(this).setEnable(true)) // 配置缓存到数据库。 // true启用缓存，fasle禁用缓存。
                .setCookieStore(new DBCookieStore(this).setEnable(true)) // true启用自动维护Cookie，fasle禁用自动维护Cookie。
                .setCacheStore(new DiskCacheStore(this)) // 配置缓存到SD卡。
                .setNetworkExecutor(new URLConnectionNetworkExecutor()) // 使用HttpURLConnection做网络层。
        );

        // 要在 NoHttp之后请求
        initData();
    }

    public static WKApplication getInstance() {
        return _instance;
    }

    public static Context getAppContext() {
        if (_instance != null) {
            return _instance.getApplicationContext();
        }
        return null;
    }

    /**
     * 初始化程序崩溃捕捉处理
     */
    protected void initCrashHandler() {
        CrashManager.getInstance().init(this);
    }

    /**
     * 初始化 推送
     **/
    private void initPush() {
        JPushInterface.setDebugMode(isDebugSwitch);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
    }

    // Log 开关
    public void debugSwitch() {
        LogUtil.isDebug = isDebugSwitch;
    }

    // 初始化一些固定数据
    private void initData(){
//        new AsyncAdress(_instance).execute();
        AsyncAdress.getAdressIntance(_instance).execute();
        IndustryList = ModelManager.getInstance().getLookupIndustryAll(_instance);
        BudgetList = ModelManager.getInstance().getBudget();
        PlanTimeList = ModelManager.getInstance().getPlanTime();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

}
