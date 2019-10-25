package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.ReportAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.MyReportEntity;
import com.wecoo.qutianxia.models.ReportModel;
import com.wecoo.qutianxia.requestjson.GetReportRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.refreshload.PtrFooterView;
import com.wecoo.qutianxia.view.refreshload.PtrWecooFrameLayout;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by mwl on 2016/10/25.
 * 我的报备
 */

public class MyReportActivity extends TitleBarActivity implements View.OnClickListener,
        OnReLoadClickListener {

    private final String mPageName = "MyReportActivity";
    private Context mContext = MyReportActivity.this;
    public static String rbType = "rbType";
    // 页数   状态
    private int currentPage = 0, intRbType = 1;
    // 审核中    跟进中         已签订    已退回
    private RadioButton rbVerify, rbFollowup, rbSigned, rbReturned;
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private LoadDataErrorWidget errorWidget;
    private ReportAdapter reportAdapter;
    private ArrayList<ReportModel> dataList;
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreport_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.my_report), None);

        intRbType = getIntent().getIntExtra(rbType, intRbType);

        initList();
        initView();

        MobclickAgent.onEvent(mContext, "MyReportOnclick");
    }

    // 初始化List
    private void initList() {
        currentPage = 0;
        dataList = new ArrayList<ReportModel>();
    }

    // 初始化View
    private void initView() {
        rbVerify = (RadioButton) findViewById(R.id.myReport_rb_verifing);
        rbFollowup = (RadioButton) findViewById(R.id.myReport_rb_followuping);
        rbSigned = (RadioButton) findViewById(R.id.myReport_rb_signed);
        rbReturned = (RadioButton) findViewById(R.id.myReport_rb_returned);
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.myReport_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.myReport_listView);
        errorWidget = (LoadDataErrorWidget) findViewById(R.id.myReport_loaddataView);
        reportAdapter = new ReportAdapter(mContext, dataList);
        mListView.setAdapter(reportAdapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);

        setListener();
        updateData(true, intRbType);
    }

    // 配置监听
    private void setListener(){
        // 添加监听
        rbVerify.setOnClickListener(this);
        rbFollowup.setOnClickListener(this);
        rbSigned.setOnClickListener(this);
        rbReturned.setOnClickListener(this);
        errorWidget.setOnReLoadClickListener(this);
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
                updateData(false, intRbType);
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
            updateData(false, intRbType);
        }
    };

    // 初始化报备数据
    private void initData(boolean isshowLoad, String url) {
        if (NetWorkState.isNetworkAvailable(mContext)) {
            GetReportRequest reportRequest = new GetReportRequest(url);
            reportRequest.setRequestParms(currentPage, Constants.pageSize);
            reportRequest.setReturnDataClick(mContext, isshowLoad, 0, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    MyReportEntity entity = (MyReportEntity) obj;
                    if (entity != null) {
                        if (entity.getList() != null && entity.getList().size() > 0) {
                            mPtrFrame.setVisibility(View.VISIBLE);
                            errorWidget.setVisibility(View.GONE);

                            dataList.addAll(entity.getList());
                            reportAdapter.setData(dataList);
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
                                errorWidget.setVisibility(View.VISIBLE);
                                errorWidget.dataLoadError();
                            } else {
                                footview.changeStatus(footview.LOADED);
                                mListView.setOnScrollListener(null);
                                booFooter = false;
                            }
                        }
                    }
                }
            });
        } else {
            mPtrFrame.setVisibility(View.GONE);
            errorWidget.setVisibility(View.VISIBLE);
            errorWidget.netWorkError();
        }

    }

    // 切换Tab时，更新报备数据
    private void updateData(boolean isshowLoad, int rbType) {
        this.intRbType = rbType;
        switch (intRbType) {
            case 1:
                rbVerify.setChecked(true);
                initData(isshowLoad, WebUrl.searchReportVerifying);
                break;
            case 2:
                rbFollowup.setChecked(true);
                initData(isshowLoad, WebUrl.searchReportFollowing);
                break;
            case 3:
                rbSigned.setChecked(true);
                initData(isshowLoad, WebUrl.searchReportSignedUp);
                break;
            case 4:
                rbReturned.setChecked(true);
                initData(isshowLoad, WebUrl.searchReportBack);
                break;
        }
    }

    // 监听的方法
    @Override
    public void onClick(View view) {
        initList(); // 切换的时候重新组建数据
        switch (view.getId()) {
            case R.id.myReport_rb_verifing:
                updateData(true, 1);
                break;
            case R.id.myReport_rb_followuping:
                updateData(true, 2);
                break;
            case R.id.myReport_rb_signed:
                updateData(true, 3);
                break;
            case R.id.myReport_rb_returned:
                updateData(true, 4);
                break;
        }
    }

    @Override
    public void OnReLoadData() {
        currentPage = 0;
        updateData(true, intRbType);
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
        CallServer.getInstance().cancelBySign(mContext);
    }

}
