package com.wecoo.qutianxia.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.base.TitleBarActivity.RightCallbackListener;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.umeng.Defaultcontent;
import com.wecoo.qutianxia.umeng.ShareWindow;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.view.DialogView;
import com.wecoo.qutianxia.view.DialogView.DialogCallback;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

/**
 * Created by mwl on 2016/10/25.
 * 公共的H5
 */

public class PubWebViewActivity extends TitleBarActivity implements RightCallbackListener, OnReLoadClickListener {

    private Activity mActivity = PubWebViewActivity.this;
    private final String mPageName = "PubWebViewActivity";
    private LoadDataErrorWidget errorWidget;
    private WebView wv;
    private ProgressBar pBar;
    private String sharetitle = "", loadUrl = "", skillShare = "";
    public static String WebUrl = "webUrl", JNShare = "JNShare";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        AppManager.getAppManager().addActivity(this);

        loadUrl = getIntent().getStringExtra(WebUrl);
        skillShare = getIntent().getStringExtra(JNShare);

        initActionBar(this);
        if (TextUtils.isEmpty(loadUrl)) {
            setBanner(Left, "详情", None);
        }
        if (!TextUtils.isEmpty(skillShare)) {
            setRightCallbackListener(this);
        }

        initView();
    }

    private void initView() {
        errorWidget = (LoadDataErrorWidget) findViewById(R.id.pubWeb_LoadDataErrorWidget);
        wv = (WebView) findViewById(R.id.pubWeb_webView);
        pBar = (ProgressBar) findViewById(R.id.pubWe_pBar);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        wv.getSettings().setUseWideViewPort(true); //将图片调整到适合WebView的大小
        wv.getSettings().setLoadWithOverviewMode(true); //自适应屏幕
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); //不使用缓存
        wv.getSettings().setAppCacheEnabled(true);  //开启Application H5 Caches 功能
        wv.getSettings().setDomStorageEnabled(true);// 开启 DOM storage API 功能 
        wv.getSettings().setDatabaseEnabled(true);//开启 database API 功能  
        wv.loadUrl(loadUrl);
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (TextUtils.isEmpty(skillShare)) {
                    setBanner(Left, title, None);
                } else {
                    setBanner(Left, title, Right);
                    sharetitle = title;
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 50) {
                    pBar.setVisibility(View.GONE);
                } else {
                    pBar.setVisibility(View.VISIBLE);
                    pBar.setProgress(newProgress);
                }
            }
        });

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                pBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
                if (url.contains("appShare.html")) {
                    setShare(R.mipmap.share_friend_icon, Defaultcontent.shareFriendtitle,
                            Defaultcontent.shareFriendtext, Defaultcontent.shareFriendurl);
                    return true;
                } else if (url.contains("projectList.html")) {
                    SPUtils.put(mActivity, Configs.CurrentTab, 1);
                    openActivity(mActivity, MainActivity.class);
                    finish();
                    return true;
                } else if (url.contains("invitationlink2app.html")) {
                    openActivity(mActivity, MyInvitationActivity.class);
                    return true;
                } else if (url.contains("tel")) {
                    new DialogView(mActivity).createDialog("确认拨打电话 " + url.substring(4, url.length()) + " 吗？",
                            false, new DialogCallback() {
                                @Override
                                public void onSureClick() {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                    return true;
                } else {
                    wv.loadUrl(url);
                    return true;
                }
            }

        });

        checkNet();
        errorWidget.setOnReLoadClickListener(this);
    }

    private void checkNet() {
        if (NetWorkState.isNetworkAvailable(mActivity)) {
            wv.setVisibility(View.VISIBLE);
            errorWidget.setVisibility(View.GONE);
        } else {
            wv.setVisibility(View.GONE);
            errorWidget.setVisibility(View.VISIBLE);
            errorWidget.netWorkError();
        }
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
    public void OnReLoadData() {
        wv.reload();
        checkNet();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (wv != null && wv.canGoBack()) {
            wv.goBack();
        } else {
            this.finish();
        }
    }

    @Override
    public void onRightCallback(View view) {
        MobclickAgent.onEvent(PubWebViewActivity.this, "h5Share");
        setShare(R.mipmap.share_friend_icon, sharetitle, sharetitle, loadUrl);
    }

    // 分享的配置
    private void setShare(int imgId, String shareTitle, String shareContent, String shareUrl) {
        ShareWindow shareWindow = new ShareWindow(PubWebViewActivity.this);
        shareWindow.setView(false);
        shareWindow.setShareData(imgId, shareTitle, shareContent, shareUrl);
        shareWindow.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv != null) {
            wv.destroy();
        }
        UMShareAPI.get(this).release();
    }
}
