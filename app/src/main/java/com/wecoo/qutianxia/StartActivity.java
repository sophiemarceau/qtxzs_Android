package com.wecoo.qutianxia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.activity.GuideActivity;
import com.wecoo.qutianxia.activity.LoginActivity;
import com.wecoo.qutianxia.activity.MainActivity;
import com.wecoo.qutianxia.activity.ProjectInfoActivity;
import com.wecoo.qutianxia.activity.PubWebViewActivity;
import com.wecoo.qutianxia.base.BaseActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.manager.AppFolderManager;
import com.wecoo.qutianxia.manager.HandlerManager;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.AppInfoEntity;
import com.wecoo.qutianxia.requestjson.GetAppBannerRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.FileUtil;
import com.wecoo.qutianxia.utils.LogUtil;
import com.wecoo.qutianxia.utils.SPUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * 启动页
 **/
public class StartActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext = StartActivity.this;
    private final String mPageName = "StartActivity";
    private View linear_View;// 广告图布局
    private ImageView imgAd;//广告
    private Button btnTiaoguo;//跳过
    private TextView txtSecond;
    private int recLen = 7;
    private boolean boolBanner = false;
    private String img_Url = "", pic_url = "", qtx_auth = null, project_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_layout);
        //透明状态栏  
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏  
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        AppManager.getAppManager().addActivity(this);

        linear_View = findViewById(R.id.start_linear_View);
        btnTiaoguo = (Button) findViewById(R.id.start_btn_tiaoguo);
        imgAd = (ImageView) findViewById(R.id.start_img_banner);
        txtSecond = (TextView) findViewById(R.id.start_txt_second);
        imgAd.setOnClickListener(this);
        btnTiaoguo.setOnClickListener(this);


        redirectTo();
    }

    /**
     * 跳转到 主页或者引导页
     */
    private void redirectTo() {
        FileUtil.deleteDirectory(AppFolderManager.getInstance().getApkCacheFolder());
        if ((boolean) SPUtils.get(mContext, Configs.IsInstall, false)) {
            // if IsInstalled
            initActivity();
        } else {
            // if not IsInstall project/ searchProjects/
            HandlerManager.getHandlerDelayed().postDelayed(new Runnable() {
                @Override
                public void run() {
                    openActivity(mContext, GuideActivity.class);
                    finish();
                }
            }, 2000);
        }
    }

    private void initActivity() {
        qtx_auth = (String) SPUtils.get(mContext, Configs.qtx_auth, "");
        LogUtil.e("qtx_auth = " + qtx_auth);
//        LogUtil.e("user_id = " + SPUtils.get(mContext, Configs.user_id, ""));
        try {
            if (NetWorkState.isNetworkAvailable(mContext)
                    || "4G".equals(NetWorkState.getInternetType(mContext))) {
                showBanner();// 请求广告图
            } else {
                startLoginOrMain();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
//    private void init() {
//        JPushInterface.init(getApplicationContext());         // 初始化极光推送
//        JPushInterface.stopPush(getApplicationContext());     // 停止发送极光推送
//        JPushInterface.resumePush(getApplicationContext());   // 回复发送极光推送
//    }
    // 广告图
    private void showBanner() {
        GetAppBannerRequest bannerRequest = new GetAppBannerRequest();
        bannerRequest.setReturnDataClick(mContext, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                AppInfoEntity entityBanner = (AppInfoEntity) obj;
                if (entityBanner != null) {
                    img_Url = entityBanner.getPic();
                    pic_url = entityBanner.getPic_url();
                    project_id = entityBanner.getProject_id();
                    if (!TextUtils.isEmpty(img_Url)) {
                        imageManager.loadUrlImage(img_Url, imgAd, R.color.default_pics);
                    }
                }
                HandlerManager.getHandlerDelayed().postDelayed(runnable, 1000);
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen--;
            if (recLen < 0) {
                HandlerManager.getHandlerDelayed().removeCallbacks(runnable);
                startLoginOrMain();
            } else {
                linear_View.setVisibility(View.VISIBLE);
                imgAd.setVisibility(View.VISIBLE);
                txtSecond.setVisibility(View.VISIBLE);
                txtSecond.setText(String.valueOf(recLen) + "s");
                HandlerManager.getHandlerDelayed().postDelayed(runnable, 1000);
            }
        }
    };

    // 判断是否登录   是登录 直接到首页  否则 到登录页
    private void startLoginOrMain() {
        if (TextUtils.isEmpty(qtx_auth)) {
            openActivity(mContext, LoginActivity.class);
        } else {
            openActivity(mContext, MainActivity.class);
        }
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
        if (boolBanner) {
            startLoginOrMain();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HandlerManager.getHandlerDelayed().removeCallbacks(runnable);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_img_banner:
                if (!TextUtils.isEmpty(pic_url)) {
                    boolBanner = true;
                    HandlerManager.getHandlerDelayed().removeCallbacks(runnable);
                    Intent intent = new Intent(mContext, PubWebViewActivity.class);
                    intent.putExtra(PubWebViewActivity.WebUrl, pic_url);
                    startActivity(intent);
                } else if (!"0".equals(project_id)) {
                    boolBanner = true;
                    HandlerManager.getHandlerDelayed().removeCallbacks(runnable);
                    Intent intent = new Intent(mContext, ProjectInfoActivity.class);
                    intent.putExtra(ProjectInfoActivity.P_ID, project_id);
                    startActivity(intent);
                }
                break;
            case R.id.start_btn_tiaoguo:
                HandlerManager.getHandlerDelayed().removeCallbacks(runnable);
                startLoginOrMain();
                break;
        }
    }
}
