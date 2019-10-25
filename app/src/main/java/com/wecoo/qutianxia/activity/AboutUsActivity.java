package com.wecoo.qutianxia.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.AppInfoUtil;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

/**
 * Created by mwl on 2016/10/25.
 * 关于我们
 */

public class AboutUsActivity extends TitleBarActivity implements OnReLoadClickListener {

    private final String mPageName = "AboutUsActivity";
    private LoadDataErrorWidget errorWidget;
    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.my_about), None);

        initView();
        MobclickAgent.onEvent(AboutUsActivity.this, "AboutUsOnclick");
    }

    private void initView() {
        errorWidget = (LoadDataErrorWidget) findViewById(R.id.aboutUs_LoadDataErrorWidget);
        wv = (WebView) findViewById(R.id.aboutUs_webView);
        wv.getSettings().setJavaScriptEnabled(true);//设置JS可用
        wv.getSettings().setLoadWithOverviewMode(true); //自适应屏幕
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); //不使用缓存
        wv.loadUrl(WebUrl.aboutUs + "?versionName=" + AppInfoUtil.getInstance().getVersionName(AboutUsActivity.this));
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                //用intent启动拨打电话  tel:4004000499 // "tel:" + 12
                // 跳转到拨号界面
                if (url.contains("tel")) {
                    MobclickAgent.onEvent(AboutUsActivity.this, "about_qtxzsCallOnlick");
                    String phone = url.substring(4, url.length());
                    AppInfoUtil.onCallPhone(AboutUsActivity.this,phone);
                    return true;
                } else {
                    return false;
                }
            }
        });
        checkNet();
        errorWidget.setOnReLoadClickListener(this);
    }

    private void checkNet() {
        if (NetWorkState.isNetworkAvailable(AboutUsActivity.this)) {
            wv.setVisibility(View.VISIBLE);
            errorWidget.setVisibility(View.GONE);
        } else {
            wv.setVisibility(View.GONE);
            errorWidget.setVisibility(View.VISIBLE);
            errorWidget.netWorkError();
        }
    }

    @Override
    public void OnReLoadData() {
        wv.reload();
        checkNet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }
}
