package com.wecoo.qutianxia.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.InvitationAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.InvitationEntity;
import com.wecoo.qutianxia.models.InvitationModel;
import com.wecoo.qutianxia.requestjson.GetInvitionListRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.WebUrl;
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
 * Created by mwl on 2017/1/10.
 * 我的邀请详情
 */

public class InvitationDetailActivity extends TitleBarActivity implements
        OnReLoadClickListener, ReturnDataClick {

    private final String mPageName = "InvitationDetailActivity";
    private Activity mContext = InvitationDetailActivity.this;
    public static String SLL_ID = "sll_id";
    private String sll_id;
    private TextView txtName, txtTel, txtCount;
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private InvitationAdapter adapter;
    private LoadDataErrorWidget errorWidget;
    // 分页加载数据
    private List<InvitationModel> dataList;
    private int currentPage = 0;
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_detail_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.invitation_info), None);
        // 邀请记录的id
        sll_id = getIntent().getStringExtra(SLL_ID);

        initList();
        initView();
    }

    // 初始化List
    private void initList() {
        currentPage = 0;
        dataList = new ArrayList<InvitationModel>();
    }

    // 初始化View
    private void initView() {
        txtName = (TextView) findViewById(R.id.invitationDetail_txt_Name);
        txtTel = (TextView) findViewById(R.id.invitationDetail_txt_tel);
        txtCount = (TextView) findViewById(R.id.invitationDetail_txt_Count);
        // Refresh
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.InvitationDetail_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.InvitationDetail_listView);
        errorWidget = (LoadDataErrorWidget) findViewById(R.id.InvitationDetail_loaddataView);
        // setAdapter
        adapter = new InvitationAdapter(mContext, dataList, 2);
        mListView.setAdapter(adapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);
        // setListener
        setListener();
        CheckNet();
    }

    // 检查网络
    private void CheckNet() {
        if (NetWorkState.isNetworkAvailable(mContext)) {
            mPtrFrame.setVisibility(View.VISIBLE);
            errorWidget.setVisibility(View.GONE);
            getInvitationListData(true);
        } else {
            mPtrFrame.setVisibility(View.GONE);
            errorWidget.setVisibility(View.VISIBLE);
            errorWidget.netWorkError();
        }
    }

    // 获取邀请列表数据
    private void getInvitationListData(boolean isshowLoad) {
        GetInvitionListRequest listRequest = new GetInvitionListRequest(WebUrl.getInvitationDetails);
        listRequest.setRequestParms(sll_id, currentPage, Constants.pageSize);
        listRequest.setReturnDataClick(mContext, isshowLoad, 0, this);
    }

    // 添加监听
    private void setListener() {
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
                getInvitationListData(false);
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
            getInvitationListData(false);
        }
    };

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
    public void OnReLoadData() {
        initList();
        CheckNet();
    }

    @Override
    public void onReturnData(int what, Object obj) {
        InvitationEntity entity = (InvitationEntity) obj;
        switch (what) {
            case 0:
                if (entity != null) {
                    txtName.setText(entity.getUs_nickname());
                    txtTel.setText(entity.getUs_tel());
                    if (!TextUtils.isEmpty(entity.getReward_balance())) {
                        String desc = "累计赏金: " + entity.getReward_balance() + "元";
                        SpannableString spanStr = new SpannableString(desc);
                        spanStr.setSpan(new ForegroundColorSpan(Color.RED), 6, (desc.length() - 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        txtCount.setText(spanStr);
                    }
                    List<InvitationModel> dataL = entity.getList();
                    if (dataL != null && dataL.size() > 0) {
                        mPtrFrame.setVisibility(View.VISIBLE);
                        errorWidget.setVisibility(View.GONE);

                        dataList.addAll(dataL);
                        adapter.setData(dataList);
                        if (currentPage == 0) {
                            mPtrFrame.refreshComplete();
                            if (isShowFoot) {
                                mListView.addFooterView(footview);
                                isShowFoot = false;
                            }
                        }
                        if (dataL.size() == Constants.pageSize) {
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
                break;
        }
    }
}
