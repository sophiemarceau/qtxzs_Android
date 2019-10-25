package com.wecoo.qutianxia.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.ReportProgressAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.ReportModel;
import com.wecoo.qutianxia.requestjson.GetReportProgressRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

import java.util.ArrayList;

/**
 * Created by mwl on 2016/10/25.
 * 报备进度
 */

public class ReportProgressActivity extends TitleBarActivity implements OnReLoadClickListener {

    private final String mPageName = "ReportProgressActivity";
    private String report_id,questType;
    private ListView listView;
    private LoadDataErrorWidget loadDataErrorWidget;
    private ReportProgressAdapter adapter;
    private ArrayList<ReportModel> dataList = new ArrayList<ReportModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportprogress_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.report_progress), None);

        report_id = getIntent().getStringExtra("report_id");
        questType = getIntent().getStringExtra("questType");

        initView();
        initData(true);
    }

    private void initView() {
        loadDataErrorWidget = (LoadDataErrorWidget) findViewById(R.id.reportprogress_loaddataView);
        listView = (ListView) findViewById(R.id.reportprogress_listView);
        adapter = new ReportProgressAdapter(ReportProgressActivity.this, dataList);
        listView.setAdapter(adapter);
        // 添加监听
        loadDataErrorWidget.setOnReLoadClickListener(this);
    }

    // 初始化报备数据
    private void initData(boolean isshowLoad) {
        if (TextUtils.isEmpty(report_id)) {
            return;
        }
        if (NetWorkState.isNetworkAvailable(this)) {
            GetReportProgressRequest progressRequest;
            if ("渠天下客服".equals(questType)) {
                progressRequest = new GetReportProgressRequest(WebUrl.searchPlatformFeedbackCrlDtoList);
            } else {
                progressRequest = new GetReportProgressRequest(WebUrl.searchReportProgress);
            }
            progressRequest.setRequestParms(report_id);
            progressRequest.setReturnDataClick(this, isshowLoad, 0, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    ArrayList<ReportModel> objList = (ArrayList<ReportModel>) obj;
                    if (objList != null && objList.size() > 0) {
                        listView.setVisibility(View.VISIBLE);
                        loadDataErrorWidget.setVisibility(View.GONE);

                        dataList.addAll(objList);
                        adapter.setData(dataList);
                    } else {
                        listView.setVisibility(View.GONE);
                        loadDataErrorWidget.setVisibility(View.VISIBLE);
                        loadDataErrorWidget.dataLoadError();
                    }
                }
            });
        } else {
            listView.setVisibility(View.GONE);
            loadDataErrorWidget.setVisibility(View.VISIBLE);
            loadDataErrorWidget.netWorkError();
        }
    }

    @Override
    public void OnReLoadData() {
        initData(true);
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
    protected void onDestroy() {
        super.onDestroy();
        CallServer.getInstance().cancelBySign(this);
    }
}
