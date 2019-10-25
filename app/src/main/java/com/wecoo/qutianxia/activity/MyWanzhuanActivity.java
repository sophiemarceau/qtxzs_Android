package com.wecoo.qutianxia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.base.TitleBarActivity.RightCallbackListener;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.umeng.Defaultcontent;
import com.wecoo.qutianxia.umeng.ShareWindow;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

/**
 * Created by mwl on 2016/10/25.
 * 玩转问渠
 */

public class MyWanzhuanActivity extends TitleBarActivity implements OnReLoadClickListener, RightCallbackListener{

    private final String mPageName = "MyWanzhuanActivity";
    private LoadDataErrorWidget errorWidget;
    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.my_wanzhuan), Right);
        setRightCallbackListener(this);

        initView();

        MobclickAgent.onEvent(MyWanzhuanActivity.this, "MyWanzhuanOnlick");
    }

    private void initView() {
        errorWidget = (LoadDataErrorWidget) findViewById(R.id.aboutUs_LoadDataErrorWidget);
        wv = (WebView) findViewById(R.id.aboutUs_webView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true); //自适应屏幕
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); //不使用缓存
        wv.loadUrl(WebUrl.wanZhuan);
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("pkgname=com.wecoo.qutianxia")) {
                    SPUtils.put(MyWanzhuanActivity.this, Configs.CurrentTab, 0);
                    openActivity(MyWanzhuanActivity.this, MainActivity.class);
                    finish();
                    return true;
                }
                return false;
            }
        });
        checkNet();
        errorWidget.setOnReLoadClickListener(this);
    }

    private void checkNet() {
        if (NetWorkState.isNetworkAvailable(MyWanzhuanActivity.this)) {
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

    @Override
    public void onRightCallback(View view) {
        MobclickAgent.onEvent(MyWanzhuanActivity.this, "MyWanzhuanShare");
        ShareWindow shareWindow = new ShareWindow(MyWanzhuanActivity.this);
        shareWindow.setShareType(mPageName);
        shareWindow.setView(false);
        shareWindow.setShareData(R.mipmap.share_friend_icon, Defaultcontent.shareWanzhuanTitle,
                Defaultcontent.shareWanzhuantext, Defaultcontent.shareWanzhuanurl);
        shareWindow.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

}
