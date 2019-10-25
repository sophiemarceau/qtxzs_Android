package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.ContactInfoAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.manager.HandlerManager;
import com.wecoo.qutianxia.models.ContactDetailEntity;
import com.wecoo.qutianxia.models.ContactEntity;
import com.wecoo.qutianxia.models.ContactModel;
import com.wecoo.qutianxia.requestjson.GetContactDetailRequest;
import com.wecoo.qutianxia.requestjson.GetContactInfoRequest;
import com.wecoo.qutianxia.requestjson.GetUserTelRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.AppInfoUtil;
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
 * Created by mwl on 2017/2/18.
 * 人脉用户详情
 */

public class ContactDetailActivity extends TitleBarActivity implements View.OnClickListener,
        OnReLoadClickListener, ReturnDataClick {

    private final String mPageName = "ContactDetailActivity";
    private Context mContext = ContactDetailActivity.this;
    private View llSuperior;
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private ContactInfoAdapter infoAdapter;
    private LoadDataErrorWidget loadErrorWidget;
    // 用户昵称 用户手机号 用户级别
    private TextView txtName, txtTel, txtLevel, txtSuperiorTel;
    // 本人赏金   累计赏金    本人邀请  累计邀请
    private TextView txtSelfSum, txtTotalSum, txtSelfInvit, txtTotalInvit;
    // 本人报备   累计报备   本人签约   累计签约
    private TextView txtSelfReport, txtTotalReport, txtSelfSigned, txtTotalSigned;
    private ImageView ivCall;
    private String sil_id, parent_sil_id,user_id_str;
    // 分页加载数据
    private List<ContactModel> dataList;
    private int currentPage = 0;
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactinfo_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.contacts_user_info), None);

        initList();
        initView();
    }

    // 初始化List
    private void initList() {
        currentPage = 0;
        dataList = new ArrayList<ContactModel>();
    }

    private void initView() {
        // headerView1
        View headerView1 = LayoutInflater.from(mContext).inflate(R.layout.contactinfo_headerview1, null);
        txtName = (TextView) headerView1.findViewById(R.id.contactInfo_header_txtName);
        txtTel = (TextView) headerView1.findViewById(R.id.contactInfo_header_txtTel);
        txtLevel = (TextView) headerView1.findViewById(R.id.contactInfo_header_txtLevel);
        ivCall = (ImageView) headerView1.findViewById(R.id.contactInfo_header_ivCall);
        // headerView2
        View headerView2 = LayoutInflater.from(mContext).inflate(R.layout.contactinfo_headerview2, null);
        txtSelfSum = (TextView) headerView2.findViewById(R.id.contactInfo_header_txtSelfSum);
        txtTotalSum = (TextView) headerView2.findViewById(R.id.contactInfo_header_txtTotalSum);
        txtSelfInvit = (TextView) headerView2.findViewById(R.id.contactInfo_header_txtSelfInvit);
        txtTotalInvit = (TextView) headerView2.findViewById(R.id.contactInfo_header_txtTotalInvit);
        // headerView3
        View headerView3 = LayoutInflater.from(mContext).inflate(R.layout.contactinfo_headerview3, null);
        txtSelfReport = (TextView) headerView3.findViewById(R.id.contactInfo_header_txtSelfReport);
        txtTotalReport = (TextView) headerView3.findViewById(R.id.contactInfo_header_txtTotalReport);
        txtSelfSigned = (TextView) headerView3.findViewById(R.id.contactInfo_header_txtSelfSigned);
        txtTotalSigned = (TextView) headerView3.findViewById(R.id.contactInfo_header_txtTotalSigned);
        // headerView4
        View headerView4 = LayoutInflater.from(mContext).inflate(R.layout.contactinfo_headerview4, null);
        llSuperior = headerView4.findViewById(R.id.contactInfo_header_llSuperior);
        txtSuperiorTel = (TextView) headerView4.findViewById(R.id.contactInfo_header_txtParentTel);

        // Refresh
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.ContactDetail_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.ContactDetail_listView);
        loadErrorWidget = (LoadDataErrorWidget) findViewById(R.id.ContactDetail_loaddataView);

        // add HeadrView
        mListView.addHeaderView(headerView1);
        mListView.addHeaderView(headerView2);
        mListView.addHeaderView(headerView3);
        mListView.addHeaderView(headerView4);
        // setAdapter
        infoAdapter = new ContactInfoAdapter(mContext, dataList);
        mListView.setAdapter(infoAdapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);

        setListener();
    }

    // 添加监听
    private void setListener() {
        ivCall.setOnClickListener(this);
        loadErrorWidget.setOnReLoadClickListener(this);
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
                getDetailData();
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
            getInfoListData(false);
        }
    };

    // 检查网络
    private void CheckNet() {
        if (NetWorkState.isNetworkAvailable(mContext)) {
            mPtrFrame.setVisibility(View.VISIBLE);
            loadErrorWidget.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getDetailData();
                }
            }).start();
        } else {
            mPtrFrame.setVisibility(View.GONE);
            loadErrorWidget.setVisibility(View.VISIBLE);
            loadErrorWidget.netWorkError();
        }
    }

    // 获取人脉详情信息
    private void getDetailData() {
        GetContactDetailRequest detailRequest = new GetContactDetailRequest();
        detailRequest.setRequestParms(sil_id);
        detailRequest.setReturnDataClick(mContext, 0, this);
    }

    // 获取人脉详情下的动态
    private void getInfoListData(boolean isshowLoad) {
        GetContactInfoRequest infoListRequest = new GetContactInfoRequest();
        infoListRequest.setRequestParms(sil_id, currentPage, 10);
        infoListRequest.setReturnDataClick(mContext, isshowLoad, 1, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contactInfo_header_ivCall:
                MobclickAgent.onEvent(mContext, "contactInfo_CallOnclick");
                if (NetWorkState.isNetworkAvailable(mContext)) {
                    getUserTel();
                } else {
                    ToastUtil.showShort(mContext, getString(R.string.load_data_nonetwork));
                }
                break;
            case R.id.contactInfo_header_llSuperior:
                MobclickAgent.onEvent(mContext, "contactInfo_SuperiorOnclick");
                Intent intent = new Intent(mContext, ContactDetailActivity.class);
                intent.putExtra("sil_id", parent_sil_id);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
        }
    }

    // 获取用户手机号
    private void getUserTel() {
        GetUserTelRequest userTelRequest = new GetUserTelRequest();
        userTelRequest.setRequestParms(user_id_str);
        userTelRequest.setReturnDataClick(mContext, 2, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sil_id = getIntent().getStringExtra("sil_id");
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
        initList();
        CheckNet();
        showLoadingDialog(mContext, "正在加载...");
        HandlerManager.getHandlerDelayed().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeLoadingDialog();
            }
        }, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeLoadingDialog();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
        CallServer.getInstance().cancelBySign(mContext);
    }

    @Override
    public void OnReLoadData() {
        initList();
        CheckNet();
    }

    @Override
    public void onReturnData(int what, Object obj) {
        switch (what) {
            case 0:
                ContactDetailEntity detailEntity = (ContactDetailEntity) obj;
                if (detailEntity != null) {
                    txtLevel.setVisibility(View.VISIBLE);
                    if (detailEntity.getLevel() == 1) {
                        txtLevel.setText("一级人脉");
                        txtLevel.setBackgroundResource(R.drawable.txt_level_red_bg);
                        ivCall.setVisibility(View.VISIBLE);
                        llSuperior.setVisibility(View.GONE);
                    } else if (detailEntity.getLevel() == 2) {
                        txtLevel.setText("二级人脉");
                        txtLevel.setBackgroundResource(R.drawable.txt_level_yellow_bg);
                        ivCall.setVisibility(View.GONE);
                        llSuperior.setVisibility(View.VISIBLE);
                        llSuperior.setOnClickListener(this);
                        txtSuperiorTel.setText(detailEntity.getParent_user_nickname() + "  "
                                + detailEntity.getParent_user_tel());
                        parent_sil_id = detailEntity.getParent_sil_id();
                    } else if (detailEntity.getLevel() == 3) {
                        txtLevel.setText("三级人脉");
                        txtLevel.setBackgroundResource(R.drawable.txt_level_blue_bg);
                        ivCall.setVisibility(View.GONE);
                        llSuperior.setVisibility(View.VISIBLE);
                        llSuperior.setOnClickListener(this);
                        txtSuperiorTel.setText(detailEntity.getParent_user_nickname() + "  "
                                + detailEntity.getParent_user_tel());
                        parent_sil_id = detailEntity.getParent_sil_id();
                    }
                    user_id_str = detailEntity.getUser_id_str();
                    txtName.setText(detailEntity.getUser_nickname());
                    txtTel.setText(detailEntity.getUser_tel());
                    txtSelfSum.setText(detailEntity.getSil_self_contribution_sum());
                    txtTotalSum.setText(detailEntity.getSil_total_contribution_sum());
                    txtSelfInvit.setText(detailEntity.getSil_self_invitation_count());
                    txtTotalInvit.setText(detailEntity.getSil_total_invitation_count());
                    txtSelfReport.setText(detailEntity.getSil_self_report_count());
                    txtTotalReport.setText(detailEntity.getSil_total_report_count());
                    txtSelfSigned.setText(detailEntity.getSil_self_signedup_count());
                    txtTotalSigned.setText(detailEntity.getSil_total_signedup_count());
                    // 请求列表
                    getInfoListData(false);
                }
                break;
            case 1:
                ContactEntity entity = (ContactEntity) obj;
                if (entity != null) {
                    List<ContactModel> dataL = entity.getList();
                    if (dataL != null && dataL.size() > 0) {
                        dataList.addAll(dataL);
                        infoAdapter.setData(dataList);
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
                        if (currentPage > 0) {
                            footview.changeStatus(footview.LOADED);
                            mListView.setOnScrollListener(null);
                            booFooter = false;
                        }
                    }
                }
                break;
            case 2:
                final String strTel = (String) obj;
                if (!TextUtils.isEmpty(strTel)) {
                    AppInfoUtil.onCallPhone(mContext,strTel);
                }
                break;
        }
    }
}
