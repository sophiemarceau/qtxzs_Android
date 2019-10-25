package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.ReportFenListAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.ReportFenListEntity;
import com.wecoo.qutianxia.requestjson.GetReporteRateListRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
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
 * Created by mwl on 2016/12/07.
 * 报备质量分变动明细
 */

public class ReportFenListActivity extends TitleBarActivity implements OnReLoadClickListener {

    private final String mPageName = "ReportFenListActivity";
    private Context mContext = ReportFenListActivity.this;
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private ReportFenListAdapter listAdapter;
    private LoadDataErrorWidget loadDataView;
    private List<ReportFenListEntity.ReportFenModel> reportFenList;
    private int currentPage = 0;
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybalance_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.change_detailed), None);

        initList();
        initView();
    }

    // 初始化List
    private void initList() {
        currentPage = 0;
        reportFenList = new ArrayList<ReportFenListEntity.ReportFenModel>();
    }

    // 初始化VIew
    private void initView() {
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.myBalance_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.myBalance_listView);
        loadDataView = (LoadDataErrorWidget) findViewById(R.id.balance_loaddataView);
        listAdapter = new ReportFenListAdapter(mContext, reportFenList);
        mListView.setAdapter(listAdapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);

        setListener();
        initData(true);
    }

    // 设置监听器
    private void setListener() {
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
                initData(false);
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
            initData(false);
        }
    };

    // 获取数据
    private void initData(boolean ishowLoad) {
        if (NetWorkState.isNetworkAvailable(mContext)) {
            // 获取消息数据
            GetReporteRateListRequest listRequest = new GetReporteRateListRequest();
            listRequest.setRequestParms(currentPage, Constants.pageSize);
            listRequest.setReturnDataClick(mContext, ishowLoad, 0, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    ReportFenListEntity entitys = (ReportFenListEntity) obj;
                    if (entitys != null) {
                        if (entitys.getList() != null && entitys.getList().size() > 0) {
                            mPtrFrame.setVisibility(View.VISIBLE);
                            loadDataView.setVisibility(View.GONE);

                            reportFenList.addAll(entitys.getList());
                            listAdapter.setData(reportFenList);
                            if (currentPage == 0) {
                                mPtrFrame.refreshComplete();
                                if (isShowFoot) {
                                    mListView.addFooterView(footview);
                                    isShowFoot = false;
                                }
                            }
                            if (entitys.getList().size() == Constants.pageSize) {
                                if (booFooter) return;
                                footview.changeStatus(footview.LOADING);
                                mListView.setOnScrollListener(loadMoreListener);
                                booFooter = true;
                            } else {
                                footview.changeStatus(footview.LOADED);
                                mListView.setOnScrollListener(null);
                                booFooter = false;
                            }if (currentPage == 0) {
                                mPtrFrame.refreshComplete();
                                if (isShowFoot) {
                                    mListView.addFooterView(footview);
                                    isShowFoot = false;
                                }
                            }
                            if (entitys.getList().size() == Constants.pageSize) {
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
        } else {
            mPtrFrame.setVisibility(View.GONE);
            loadDataView.setVisibility(View.VISIBLE);
            loadDataView.netWorkError();
        }
    }

    @Override
    public void OnReLoadData() {
        initList();
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
        CallServer.getInstance().cancelBySign(mContext);
    }

}
