package com.wecoo.qutianxia.activity.enterprise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.FollowUpRecordAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.MyReportEntity;
import com.wecoo.qutianxia.models.ReportModel;
import com.wecoo.qutianxia.requestjson.GetLogsByReportIdRequest;
import com.wecoo.qutianxia.requestjson.GetPhoneByReportIdRequest;
import com.wecoo.qutianxia.requestjson.GetReportStatusRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.AppInfoUtil;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.refreshload.PtrFooterView;
import com.wecoo.qutianxia.view.refreshload.PtrWecooFrameLayout;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by wecoo on 2017/6/2.
 * 跟进记录
 */

public class FollowUpRecordActivity extends TitleBarActivity implements View.OnClickListener,
        OnReLoadClickListener {

    private final String mPageName = "FollowUpRecordActivity";
    private Context mContext = FollowUpRecordActivity.this;
    private View ViewAddChatLog, ViewReviewPhone,viewfengexian;
    private TextView txtReturned, txtSure;
    // 页数   状态
    private int currentPage = 0, intRbType = 1;
    private String report_id = "",report_id_str = "";
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private LoadDataErrorWidget loadDataView;
    private FollowUpRecordAdapter mAdapter;
    private ArrayList<ReportModel> dataList;
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_record_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.FollowUpRecord), None);

        intRbType = getIntent().getIntExtra("intRbType", 1);
        report_id = getIntent().getStringExtra("report_id");
        report_id_str = getIntent().getStringExtra("report_id_str");

        initList();
        initView();
    }

    // 初始化 List
    private void initList() {
        currentPage = 0;
        dataList = new ArrayList<>();
    }

    // 初始化View
    private void initView() {
        viewfengexian = findViewById(R.id.item_fengexian_view);
        ViewAddChatLog = findViewById(R.id.followUp_Record_item_ViewAddChatLog);
        ViewReviewPhone = findViewById(R.id.followUp_Record_item_ViewReviewPhone);
        txtReturned = (TextView) findViewById(R.id.followUp_Record_item_txtReturned);
        txtSure = (TextView) findViewById(R.id.followUp_Record_item_txtSure);
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.followUp_Record_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.followUp_Record_listView);
        loadDataView = (LoadDataErrorWidget) findViewById(R.id.followUp_Record_loaddataView);
        // setAdapter
        mAdapter = new FollowUpRecordAdapter(mContext, dataList);
        mListView.setAdapter(mAdapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);

        setView();
        setListener();
    }

    // 设置view
    private void setView() {
        switch (intRbType){
            case 1:
                txtSure.setText("确认通过");
                break;
            case 3:
                txtSure.setText("确认签约");
                break;
            case 6:
            case 7:
                viewfengexian.setVisibility(View.VISIBLE);
                txtReturned.setVisibility(View.GONE);
                txtSure.setVisibility(View.GONE);
                break;
        }
    }

    // 添加监听
    private void setListener() {
        ViewAddChatLog.setOnClickListener(this);
        ViewReviewPhone.setOnClickListener(this);
        txtReturned.setOnClickListener(this);
        txtSure.setOnClickListener(this);
        loadDataView.setOnReLoadClickListener(this);
        mListView.setOnScrollListener(loadMoreListener);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (!NetWorkState.isNetworkAvailable(mContext)) {
                    mPtrFrame.refreshComplete();
                    ToastUtil.showShort(mContext, getString(R.string.load_data_nonetwork));
                    return;
                }
                initList();
                getLogsData(false);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }
        });
    }

    // 加载更多
    private LoadMoreListener loadMoreListener = new LoadMoreListener() {
        @Override
        public void onLoadMore() {
            if (!NetWorkState.isNetworkAvailable(mContext)) {
                footview.changeStatus(footview.NOTNET);
                return;
            } else {
                footview.changeStatus(footview.LOADING);
            }
            currentPage++;
            getLogsData(false);
        }
    };

    // 获取跟进记录数据
    private void getLogsData(boolean isShowLoad) {
        GetLogsByReportIdRequest logsByReportIdRequest = new GetLogsByReportIdRequest();
        logsByReportIdRequest.setRequestParms(report_id);
        logsByReportIdRequest.setReturnDataClick(mContext, isShowLoad, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                MyReportEntity entity = (MyReportEntity) obj;
                if (entity != null) {
                    if (entity.getList() != null && entity.getList().size() > 0) {
                        mPtrFrame.setVisibility(View.VISIBLE);
                        loadDataView.setVisibility(View.GONE);

                        dataList.addAll(entity.getList());
                        mAdapter.setData(dataList);
                        if (currentPage == 0) {
                            mPtrFrame.refreshComplete();
                            if (isShowFoot) {
                                mListView.addFooterView(footview);
                                isShowFoot = false;
                            }
                        }
                        if (entity.getList().size() == Constants.pageSize) {
                            if (booFooter) return;
                            footview.changeStatus(footview.LOADING);
                            mListView.setOnScrollListener(loadMoreListener);
                            booFooter = true;
                        } else {
                            footview.changeStatus(footview.LOADED);
                            mListView.setOnScrollListener(null);
                            booFooter = false;
                        }
                    } else {
                        if (currentPage == 0) {
                            mPtrFrame.setVisibility(View.GONE);
                            loadDataView.setVisibility(View.VISIBLE);
                            loadDataView.dataLoadError();
                        } else {
                            footview.changeStatus(footview.LOADED);
                            mListView.setOnScrollListener(null);
                            booFooter = false;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.followUp_Record_item_ViewAddChatLog:
                Intent intent1 = new Intent(mContext, AddRecordActivity.class);
                intent1.putExtra("report_id", report_id);
                startActivity(intent1);
                break;
            case R.id.followUp_Record_item_ViewReviewPhone:
                getPhoneById();
                break;
            case R.id.followUp_Record_item_txtReturned:
                Intent intent2 = new Intent(mContext, ExamineProjectActivity.class);
                intent2.putExtra("viewType", txtReturned.getText().toString());
                intent2.putExtra("report_id", report_id);
                startActivity(intent2);
                break;
            case R.id.followUp_Record_item_txtSure:
                Intent intent3 = new Intent();
                if ("确认签约".equals(txtSure.getText().toString())) {
                    intent3.setClass(mContext, SignUpforMoneyActivity.class);
                } else {
                    intent3.setClass(mContext, ExamineProjectActivity.class);
                    intent3.putExtra("viewType", txtSure.getText().toString());
                }
                intent3.putExtra("report_id", report_id);
                mContext.startActivity(intent3);
                break;
        }
    }

    private void getPhoneById() {
        if (TextUtils.isEmpty(report_id_str)) return;
        GetPhoneByReportIdRequest byReportIdRequest = new GetPhoneByReportIdRequest();
        byReportIdRequest.setRequestParms(report_id_str);
        byReportIdRequest.setReturnDataClick(mContext, 1, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                if (obj != null) {
                    String phoneStr = (String) obj;
                    AppInfoUtil.onCallPhone(mContext,phoneStr);
                }
            }
        });
    }

    private void GetReportStatus(){
        GetReportStatusRequest statusRequest = new GetReportStatusRequest();
        statusRequest.setRequestParms(report_id);
        statusRequest.setReturnDataClick(mContext, 1, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                if (obj != null){
                    intRbType = (int) obj;
                    setView();
                }
            }
        });
    }

    @Override
    public void OnReLoadData() {
        initList();
        getLogsData(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);

        GetReportStatus();
        initList();
        getLogsData(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }
}
