package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.MyBalanceAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.base.TitleBarActivity.RightCallbackListener;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.MyBalanceEntity;
import com.wecoo.qutianxia.requestjson.GetBalanceListRequest;
import com.wecoo.qutianxia.requestjson.GetBalanceNumRequest;
import com.wecoo.qutianxia.requestjson.GetWithdrawRulesRequest;
import com.wecoo.qutianxia.requestjson.GetWithdrawingLimitRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.DialogFucengView;
import com.wecoo.qutianxia.view.refreshload.PtrFooterView;
import com.wecoo.qutianxia.view.refreshload.PtrWecooFrameLayout;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

import java.util.ArrayList;
import java.util.Locale;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by mwl on 2016/10/25.
 * 我的余额
 */

public class MyBalanceActivity extends TitleBarActivity implements View.OnClickListener,
        OnReLoadClickListener, ReturnDataClick, RightCallbackListener {

    private final String mPageName = "MyBalanceActivity";
    private Context mContext = MyBalanceActivity.this;
    private TextView txtTop, txtBalance, txtDesc, txtHelp, txtGuize;
    private Button btnWithdrawals;
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private LoadDataErrorWidget loadDataView;
    private MyBalanceAdapter balanceAdapter;
    private ArrayList<MyBalanceEntity.BalanceModels> balanceList;
    private String balance = "0.00";
    private Integer /*oldNumbers = 0.0, */newNumbers = 0;
    private int currentPage = 0;
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;
    private String strRules;// 提现规则文案

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybalance_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.my_balance), getString(R.string.apply_record));
        setRightCallbackListener(this);

        initList();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
        // 初始化 list
        if (balanceList != null && balanceList.size() > 0) {
            initList();
        }
        GetBalanceNum();
        // 加载数据
        initData(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
        CallServer.getInstance().cancelBySign(mContext);

    }

    // 初始化List
    private void initList() {
        currentPage = 0;
        balanceList = new ArrayList<MyBalanceEntity.BalanceModels>();
    }

    private void getWithdrawingLimit() {
        // 获取提现申请的最低余额限制
        GetWithdrawingLimitRequest limitRequest = new GetWithdrawingLimitRequest();
        limitRequest.setReturnDataClick(mContext, 1, this);
        // 获取业务员实名认证信息
//        GetIDInfoRequest getIDInfoRequest = new GetIDInfoRequest();
//        getIDInfoRequest.setReturnDataClick(mContext, 4, this);
    }

    // 最新余额
    private void GetBalanceNum() {
        GetBalanceNumRequest numRequest = new GetBalanceNumRequest();
        numRequest.setReturnDataClick(mContext, 2, this);
    }

    // 初始化View
    private void initView() {
        // 初始化HeaderView
        View hearView = LayoutInflater.from(mContext).inflate(R.layout.mybalance_header_view, null);
        txtBalance = (TextView) hearView.findViewById(R.id.balance_txt_num);
        txtDesc = (TextView) hearView.findViewById(R.id.balance_txt_withdrawals_desc);
        txtHelp = (TextView) hearView.findViewById(R.id.balance_txt_help);
        txtGuize = (TextView) hearView.findViewById(R.id.balance_txt_topguize);
        txtGuize.setVisibility(View.GONE);
        btnWithdrawals = (Button) hearView.findViewById(R.id.balance_btn_Withdrawals);
        txtTop = (TextView) findViewById(R.id.balance_txt_topDetailed);

        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.myBalance_PtrFrameLayout);
        loadDataView = (LoadDataErrorWidget) findViewById(R.id.balance_loaddataView);
        mListView = (ListView) findViewById(R.id.myBalance_listView);

        mListView.addHeaderView(hearView);

        balanceAdapter = new MyBalanceAdapter(mContext, balanceList);
        mListView.setAdapter(balanceAdapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);

        setListener();
        getWithdrawingLimit();
        GetWithdrawRules();
    }

    // 设置监听器
    private void setListener() {
        txtHelp.setOnClickListener(this);
        btnWithdrawals.setOnClickListener(this);

        loadDataView.setOnReLoadClickListener(this);
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
        mListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (booFooter) {
                    switch (scrollState) {
                        // 判断滚动到底部且不滚动时，执行加载
                        case OnScrollListener.SCROLL_STATE_IDLE:
                            if (absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
                                onLoadMore();
                            }
                            break;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (i >= 1) {
                    txtTop.setVisibility(View.VISIBLE);
                } else {
                    txtTop.setVisibility(View.GONE);
                }
            }
        });
    }

    // 加载更多
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

    // 获取数据
    private void initData(boolean isshowLoad) {
        if (NetWorkState.isNetworkAvailable(mContext)) {
            GetBalanceListRequest balanceRequest = new GetBalanceListRequest();
            balanceRequest.setRequestParms(currentPage, Constants.pageSize);
            balanceRequest.setReturnDataClick(mContext, isshowLoad, 0, this);
        } else {
            mPtrFrame.setVisibility(View.GONE);
            loadDataView.setVisibility(View.VISIBLE);
            loadDataView.netWorkError();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.balance_txt_help:
                MobclickAgent.onEvent(mContext, "MyBalance_helpOnclick");
//                StringBuilder builder = new StringBuilder();
//                builder.append("1、赏金金额大于" + newNumbers + "元方可提现 \n");
//                builder.append("2、每周三为统一提现日 \n");
//                builder.append("3、我们将在三个工作日内完成对账、打款 \n");
//                builder.append("4、具体到账周期，以各银行入账时间为准 \n");
//                builder.append("5、关于个税：根据规定，个人用户每月提现总额超过800元部分，需缴纳个人所得税 \n");
                if (TextUtils.isEmpty(strRules)) {
                    GetWithdrawRules();
                    return;
                }
                DialogFucengView fucengView = new DialogFucengView(mContext);
                fucengView.setTitleData(getString(R.string.balance_help));
                fucengView.setDescData(strRules);
                fucengView.show();
                break;
            case R.id.balance_btn_Withdrawals:
                MobclickAgent.onEvent(mContext, "MyBalance_WithdrawalsOnclick");
                Intent intent = new Intent(mContext, SelectPhotoActivity.class);
                intent.putExtra(SelectPhotoActivity.ActType, mPageName);
                startActivity(intent);
                break;
        }
    }

    // 获取提现规则文案
    private void GetWithdrawRules() {
        GetWithdrawRulesRequest rulesRequest = new GetWithdrawRulesRequest();
        rulesRequest.setReturnDataClick(mContext, 3, this);
    }

    @Override
    public void OnReLoadData() {
        getWithdrawingLimit();
        GetWithdrawRules();
        initList();
        initData(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallServer.getInstance().cancelBySign(mContext);
    }

    @Override
    public void onReturnData(int what, Object obj) {
        switch (what) {
            case 0:  // 数据列表
                MyBalanceEntity entity = (MyBalanceEntity) obj;
                if (entity != null) {
                    if (entity.getList() != null && entity.getList().size() > 0) {
                        txtGuize.setVisibility(View.VISIBLE);
                        mPtrFrame.setVisibility(View.VISIBLE);
                        loadDataView.setVisibility(View.GONE);

                        balanceList.addAll(entity.getList());
                        balanceAdapter.setData(balanceList);
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
                            booFooter = true;
                        } else {
                            footview.changeStatus(footview.LOADED);
                            booFooter = false;
                        }
                    } else {
                        if (currentPage == 0) {
                            mPtrFrame.setVisibility(View.VISIBLE);
                            loadDataView.setVisibility(View.GONE);
                        } else {
                            footview.changeStatus(footview.LOADED);
                            booFooter = false;
                        }
                    }
                }
                break;
            case 1: // 金额下面的文案
                if (obj != null) {
                    newNumbers = (Integer) obj;
                    txtDesc.setText(String.format(Locale.getDefault(),
                            getString(R.string.withdrawals_desc), String.valueOf(newNumbers)));
                }
                break;
            case 2: // 最新金额
                if (obj != null) {
                    balance = (String) obj;
                    txtBalance.setText(getString(R.string.renminbi) + balance);
                } else {
                    if (TextUtils.isEmpty(balance)) {
                        txtBalance.setText(getString(R.string.renminbi) + "0.00");
                    } else {
                        txtBalance.setText(getString(R.string.renminbi) + balance);
                    }
                }
                break;
            case 3:
                if (obj != null) {
                    strRules = (String) obj;
                }
                break;
        }
    }

    // 右上角 申请记录
    @Override
    public void onRightCallback(View view) {
        MobclickAgent.onEvent(mContext, "MyBalance_ApplyRecordOnclick");
        openActivity(mContext, ApplyRecordActivity.class);
    }
}
