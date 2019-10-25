package com.wecoo.qutianxia.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.ProjectInfoActivity;
import com.wecoo.qutianxia.base.BaseFragment;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.utils.AttachUtil;

/**
 * Created by mwl
 * Introduce : 招商详情
 */
public class DescripWvFragment extends BaseFragment {

    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_webview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onEvent(getActivity(), "DescripWvFragment");
    }

    private void initView() {
        webView = (WebView) getView().findViewById(R.id.fragment_tab_webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true); //将图片调整到适合WebView的大小
        webView.getSettings().setLoadWithOverviewMode(true); //自适应屏幕
        webView.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);//设置 缓存模式
        webView.getSettings().setAppCacheEnabled(true);  //开启Application H5 Caches 功能

        webView.loadUrl(ModelManager.getInstance().getDescriptionUrl());

        // 设置Web视图
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 0 && newProgress < 100) {
                    showLoadingDialog(getActivity(), "正在加载...");
                }else {
                    closeLoadingDialog();
                }
            }
        });
        // webView does not have scroll listener
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (AttachUtil.isWebViewAttach(webView)) {
                    ProjectInfoActivity.onEvent(true);
                } else {
                    ProjectInfoActivity.onEvent(false);
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
        }
    }
}
