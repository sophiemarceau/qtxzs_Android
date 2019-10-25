package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.requestjson.GetReportRateRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.view.ReportCircleView;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwl on 2016/10/25.
 * 我的报备质量分
 */

public class MyReportRateActivity extends TitleBarActivity implements OnReLoadClickListener {

    private final String mPageName = "MyReportRateActivity";
    private Context mContext = MyReportRateActivity.this;
    private TextView /*txtContent, */txtNum;
    private ReportCircleView circle_view; // 刻度
    private String strRate = "0";
    private int mValue = 0;
    private WebView wvContent;
    private Button btnLook;
    private LoadDataErrorWidget errorWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportrate_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.my_reportRate), None);

//        strRate = (String) SPUtils.get(mContext, Configs.report_rate, "");

        initView();
    }

    private void initView() {
        txtNum = (TextView) findViewById(R.id.reportRate_txtNum);
        circle_view = (ReportCircleView) findViewById(R.id.reportRate_CircleVivw);
        circle_view.setValueNameList(getData());
        circle_view.setPointer(false);

        getRateData();

        errorWidget = (LoadDataErrorWidget) findViewById(R.id.reportrate_LoadDataError);
        btnLook = (Button) findViewById(R.id.reportrate_btn_lookDetailed);
        wvContent = (WebView) findViewById(R.id.reportrate_wv_content);
        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.getSettings().setLoadWithOverviewMode(true); //自适应屏幕
        wvContent.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); //不使用缓存
        wvContent.loadUrl(WebUrl.guize);
        wvContent.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                wvContent.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }
        });
        checkNet();
        errorWidget.setOnReLoadClickListener(this);

        // 跳转明细
        btnLook.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openActivity(mContext, ReportFenListActivity.class);
        }
    };

    // 等级数据
    private List<String> getData() {
        List<String> valueNameList = new ArrayList<String>();
        valueNameList.add("较差");
        valueNameList.add("一般");
        valueNameList.add("中等");
        valueNameList.add("良好");
        valueNameList.add("优秀");
        valueNameList.add("极好");
        return valueNameList;
    }

    // 获取质量分
    private void getRateData() {
        if (!NetWorkState.isNetworkAvailable(mContext)) {
            if (TextUtils.isEmpty(strRate)) {
                mValue = 0;
            } else {
                mValue = Integer.valueOf(strRate);
            }
            circle_view.setValue(mValue);
            txtNum.setText(strRate);
            return;
        }
        GetReportRateRequest reportRateRequest = new GetReportRateRequest();
        reportRateRequest.setReturnDataClick(mContext, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                String result = (String) obj;
                if (!TextUtils.isEmpty(result)) {
//                    SPUtils.put(mContext, Configs.report_rate, result);// 保存质量分
                    mValue = Integer.valueOf(result);
                    circle_view.setValue(mValue); // 刻度的大小
                    txtNum.setText(result);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    private void checkNet(){
        if (NetWorkState.isNetworkAvailable(mContext)) {
            wvContent.setVisibility(View.VISIBLE);
            errorWidget.setVisibility(View.GONE);
        } else {
            wvContent.setVisibility(View.GONE);
            errorWidget.setVisibility(View.VISIBLE);
            errorWidget.netWorkError();
        }
    }

    @Override
    public void OnReLoadData() {
        wvContent.reload();
        checkNet();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallServer.getInstance().cancelBySign(mContext);
    }
}
