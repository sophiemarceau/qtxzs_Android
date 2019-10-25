package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.enterprise.BackstageLoginActivity;
import com.wecoo.qutianxia.adapter.MyProjectAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.base.TitleBarActivity.RightCallbackListener;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.ProjectEntity;
import com.wecoo.qutianxia.models.ProjectModels;
import com.wecoo.qutianxia.requestjson.GetMyProjectRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.AppInfoUtil;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.refreshload.PtrFooterView;
import com.wecoo.qutianxia.view.refreshload.PtrWecooFrameLayout;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnCallPhoneListener;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by mwl on 2017/06/01.
 * 我的项目
 */

public class MyProjectActivity extends TitleBarActivity implements RightCallbackListener,
        OnReLoadClickListener {

    private final String mPageName = "MyProjectActivity";
    private Context mContext = MyProjectActivity.this;
    // 页数
    private int currentPage = 0;
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private MyProjectAdapter mAdapter;
    private LoadDataErrorWidget loadDataView;
    private List<ProjectModels> projectList;
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myproject_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.my_Project), R.mipmap.icon_my_project_pc);
        setRightCallbackListener(this);

        initList();
        initView();
    }

    private void initList() {
        currentPage = 0;
        projectList = new ArrayList<ProjectModels>();
    }

    private void initView() {
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.myProject_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.myProject_listView);
        loadDataView = (LoadDataErrorWidget) findViewById(R.id.myProject_loaddataView);
        mAdapter = new MyProjectAdapter(mContext, projectList);
        mListView.setAdapter(mAdapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);

        //setListener
        setListener();
    }

    // 添加监听
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
                getProjectData(false);
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
            getProjectData(false);
        }
    };

    // 初始化报备数据
    private void getProjectData(boolean isshowLoad) {
        if (!NetWorkState.isNetworkAvailable(mContext)) {
            mPtrFrame.setVisibility(View.GONE);
            loadDataView.setVisibility(View.VISIBLE);
            loadDataView.dataLoadError();
            return;
        }
        GetMyProjectRequest projectRequest = new GetMyProjectRequest();
        projectRequest.setRequestParms(currentPage, Constants.pageSize);
        projectRequest.setReturnDataClick(mContext, isshowLoad, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                ProjectEntity entity = (ProjectEntity) obj;
                if (entity != null){
                    if (entity.getList() != null && entity.getList().size() > 0) {
                        mPtrFrame.setVisibility(View.VISIBLE);
                        loadDataView.setVisibility(View.GONE);

                        projectList.addAll(entity.getList());
                        mAdapter.setData(projectList);
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
                    }else {
                        if (currentPage == 0) {
                            mPtrFrame.setVisibility(View.GONE);
                            loadDataView.setVisibility(View.VISIBLE);
                            loadDataView.dataLoadErrorCallPhone();
                            loadDataView.setmCallPhoneListener(new OnCallPhoneListener() {
                                @Override
                                public void OnCallPhone() {
                                    AppInfoUtil.onCallPhone(mContext, "400-900-1135");
                                }
                            });
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

    // 重新加载数据
    @Override
    public void OnReLoadData() {
        initList();
        getProjectData(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);

        initList();
        getProjectData(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    // 登录后台企业网页版
    @Override
    public void onRightCallback(View view) {
        openActivity(mContext, BackstageLoginActivity.class);
    }

}
