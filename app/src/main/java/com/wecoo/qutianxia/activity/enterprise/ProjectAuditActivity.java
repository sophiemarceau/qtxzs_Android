package com.wecoo.qutianxia.activity.enterprise;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.ProjectAuditAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.MyReportEntity;
import com.wecoo.qutianxia.models.ReportModel;
import com.wecoo.qutianxia.requestjson.GetProjectAuditRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.refreshload.PtrFooterView;
import com.wecoo.qutianxia.view.refreshload.PtrWecooFrameLayout;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by wecoo on 2017/6/2.
 * 项目审核
 */

public class ProjectAuditActivity extends TitleBarActivity implements View.OnClickListener, OnReLoadClickListener {

    private final String mPageName = "ProjectAuditActivity";
    private Context mContext = ProjectAuditActivity.this;
    // 页数   状态
    private int currentPage = 0, intRbType = 1;
    // 审核中    跟进中      已签订    已退回
    private RadioButton rbVerify, rbFollowup, rbSigned, rbReturned;
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private ProjectAuditAdapter mAdapter;
    private LoadDataErrorWidget loadDataView;
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;
    private List<ReportModel> dataList;
    private String project_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectaudit_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.Project_audit_list), None);

        project_id = getIntent().getStringExtra("project_id");

        initList();
        initView();
    }

    private void initList() {
        currentPage = 0;
        dataList = new ArrayList<>();
    }

    // 初始化View
    private void initView() {
        rbVerify = (RadioButton) findViewById(R.id.ProjectAudit_rb_verifing);
        rbFollowup = (RadioButton) findViewById(R.id.ProjectAudit_rb_followuping);
        rbSigned = (RadioButton) findViewById(R.id.ProjectAudit_rb_signed);
        rbReturned = (RadioButton) findViewById(R.id.ProjectAudit_rb_returned);
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.ProjectAudit_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.ProjectAudit_listView);
        loadDataView = (LoadDataErrorWidget) findViewById(R.id.ProjectAudit_loaddataView);
        // setAdapter
        mAdapter = new ProjectAuditAdapter(mContext, dataList);
        mListView.setAdapter(mAdapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);

        //setListener
        setListener();
    }

    // 添加监听
    private void setListener() {
        rbVerify.setOnClickListener(this);
        rbFollowup.setOnClickListener(this);
        rbSigned.setOnClickListener(this);
        rbReturned.setOnClickListener(this);
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
                getData(false);
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
            getData(false);
        }
    };

    // 获取数据
    private void getData(boolean isshowLoad) {
        if (!NetWorkState.isNetworkAvailable(mContext)) {
            mPtrFrame.setVisibility(View.GONE);
            loadDataView.setVisibility(View.VISIBLE);
            loadDataView.dataLoadError();
            return;
        }
        GetProjectAuditRequest auditRequest = new GetProjectAuditRequest();
        auditRequest.setRequestParms(project_id, intRbType, currentPage, Constants.pageSize);
        auditRequest.setReturnDataClick(mContext, isshowLoad, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                MyReportEntity entity = (MyReportEntity) obj;
                if (entity != null) {
                    if (entity.getList() != null && entity.getList().size() > 0) {
                        mPtrFrame.setVisibility(View.VISIBLE);
                        loadDataView.setVisibility(View.GONE);

                        dataList.addAll(entity.getList());
                        mAdapter.setIntRbType(intRbType, dataList);
                        if (currentPage == 0) {
                            mListView.setSelection(0);
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

    // 设置监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ProjectAudit_rb_verifing:
                intRbType = 1;
                break;
            case R.id.ProjectAudit_rb_followuping:
                intRbType = 3;
                break;
            case R.id.ProjectAudit_rb_signed:
                intRbType = 6;
                break;
            case R.id.ProjectAudit_rb_returned:
                intRbType = 7;
                break;
        }
        initList();
        getData(true);
    }

    @Override
    public void OnReLoadData() {
        initList();
        getData(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
        initList();
        getData(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

}
