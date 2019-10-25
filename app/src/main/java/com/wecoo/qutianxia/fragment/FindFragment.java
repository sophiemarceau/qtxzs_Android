package com.wecoo.qutianxia.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.PubWebViewActivity;
import com.wecoo.qutianxia.base.BaseFragment;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.umeng.Defaultcontent;
import com.wecoo.qutianxia.utils.LogUtil;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

/**
 * Created by mwl on 2016/10/20.
 * 发现
 */
public class FindFragment extends BaseFragment implements OnReLoadClickListener {

    private final String mPageName = "FindFragment";
    private View rootView, topView;
    private TextView txtTitle;
    private WebView webView;
    private WebSettings webSettings;
    private LoadDataErrorWidget errorWidget;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_find_layout, container, false);
            initData();
            initView(rootView);
        }
        return rootView;
    }

    private void initView(View rootView) {
        topView = rootView.findViewById(R.id.top_View);
        txtTitle = (TextView) topView.findViewById(R.id.title_textView_center);
        txtTitle.setText(getResources().getString(R.string.find));
        webView = (WebView) rootView.findViewById(R.id.find_webView);
        errorWidget = (LoadDataErrorWidget) rootView.findViewById(R.id.find_LoadDataError);
        errorWidget.setOnReLoadClickListener(this);

        checkNetWork();

        webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本  
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);// 开启 DOM storage API 功能 
        webSettings.setDatabaseEnabled(true);//开启 database API 功能  

        String qtx_auth = (String) SPUtils.get(getActivity(), Configs.qtx_auth, "");
        StringBuilder sbAuth = new StringBuilder();//创建一个拼接cookie的容器
        sbAuth.append(WebUrl.discover);
        sbAuth.append("?qtx_auth=" + qtx_auth);
        sbAuth.append("&t=" + System.currentTimeMillis());
        String sbAuthValue = sbAuth.toString();
        LogUtil.e("finaUrl sbAuthValue = " + sbAuthValue);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(sbAuthValue);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                MobclickAgent.onEvent(getActivity(), "discoverhtml");
                Intent intent = new Intent(getActivity(), PubWebViewActivity.class);
                intent.putExtra(PubWebViewActivity.WebUrl, url);
                startActivity(intent);
//                webView.loadUrl(url);
                return true;
            }
        });

    }

    private void checkNetWork() {
        if (NetWorkState.isNetworkAvailable(getActivity())) {
            webView.reload(); //刷新
            webView.setVisibility(View.VISIBLE);
            errorWidget.setVisibility(View.GONE);
        } else {
            webView.setVisibility(View.GONE);
            errorWidget.setVisibility(View.VISIBLE);
            errorWidget.netWorkError();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
        }
    }

    private void initData() {

    }

    // 网络异常时，点击重新加载
    @Override
    public void OnReLoadData() {
        checkNetWork();
    }

    @Override
    public void onResume() {
        super.onResume();
        SPUtils.put(getActivity(), Configs.CurrentTab, 2);
        MobclickAgent.onPageStart(mPageName);
        checkNetWork();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
}
