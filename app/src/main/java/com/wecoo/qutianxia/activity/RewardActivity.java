package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.MyRewardAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.MyRewardEntity;
import com.wecoo.qutianxia.requestjson.GetRewardRequest;
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
 * Created by mwl on 2016/10/25.
 * 奖励活动
 */

public class RewardActivity extends TitleBarActivity implements View.OnClickListener, OnReLoadClickListener {

    private final String mPageName = "RewardActivity";
    private Context mContext = RewardActivity.this;
    private RadioButton rbBunosing, rbBunosed;
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private LoadDataErrorWidget errorWidget;
    private MyRewardAdapter rewardAdapter;
    private List<MyRewardEntity.RewardModel> datalist;
    private int activityStutus = 1, currentPage = 0;
    private PtrFooterView footview;// 记载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.my_bunos), None);
        initList();
        initView();

        MobclickAgent.onEvent(mContext, "MyRewardOnlick");
    }

    // List初始化
    private void initList() {
        currentPage = 0;
        datalist = new ArrayList<MyRewardEntity.RewardModel>();
    }

    private void initView() {
        rbBunosing = (RadioButton) findViewById(R.id.myReward_rb_bunosing);
        rbBunosed = (RadioButton) findViewById(R.id.myReward_rb_bunosed);
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.myReward_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.myReward_listView);
        errorWidget = (LoadDataErrorWidget) findViewById(R.id.myReward_loaddataView);

        rewardAdapter = new MyRewardAdapter(mContext, datalist);
        mListView.setAdapter(rewardAdapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);

        setListener();
        initData(true);
    }

    // 添加监听
    private void setListener() {
        rbBunosing.setOnClickListener(this);
        rbBunosed.setOnClickListener(this);
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

    // 获取活动数据
    private void initData(boolean isshowLoad) {
        if (NetWorkState.isNetworkAvailable(mContext)) {
            GetRewardRequest rewardRequest = new GetRewardRequest();
            rewardRequest.setRequestParms(activityStutus, currentPage, Constants.pageSize);
            rewardRequest.setReturnDataClick(mContext, isshowLoad, 0, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    MyRewardEntity entity = (MyRewardEntity) obj;
                    if (entity != null) {
                        if (entity.getList() != null && entity.getList().size() > 0) {
                            mPtrFrame.setVisibility(View.VISIBLE);
                            errorWidget.setVisibility(View.GONE);

                            datalist.addAll(entity.getList());
                            rewardAdapter.setData(datalist);
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

    @Override
    public void onClick(View view) {
        initList();
        switch (view.getId()) {
            case R.id.myReward_rb_bunosing:
                activityStutus = 1;
                initData(true);
                break;
            case R.id.myReward_rb_bunosed:
                MobclickAgent.onEvent(mContext, "Reward_bunosedOnclick");
                activityStutus = 3;
                initData(true);
                break;
        }
    }

    // 网络切换后的点击刷新
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
