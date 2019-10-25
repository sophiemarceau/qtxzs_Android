package com.wecoo.qutianxia.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.InvitationAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.InvitationEntity;
import com.wecoo.qutianxia.models.InvitationModel;
import com.wecoo.qutianxia.requestjson.GetInvitionListRequest;
import com.wecoo.qutianxia.requestjson.GetInvitionUserInfoRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.umeng.Defaultcontent;
import com.wecoo.qutianxia.umeng.ShareWindow;
import com.wecoo.qutianxia.utils.DensityUtil;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.StringUtil;
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
 * 我的邀请
 */

public class MyInvitationActivity extends TitleBarActivity implements OnClickListener,
        OnReLoadClickListener, ReturnDataClick/*, RightCallbackListener */{

    private final String mPageName = "MyInvitationActivity";
    private Activity mContext = MyInvitationActivity.this;
    private View headerView1, headerView2, headerView3, VFoot;
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private InvitationAdapter adapter;
    private LoadDataErrorWidget loadErrorWidget;
    private Button btnShare;
    // Header View
    private String strqrCode = "";
    private ImageView imgPhoto;
    // 用户昵称   邀请码    复制   邀请人数  邀请活动赏金    查看规则
    private TextView txtUserName, txtYQCode, txtCopy, txtNum, txtPrice, txtLookRule;
    // Footer View
    private WebView ruleWeb;
    // 分页加载数据
    private List<InvitationModel> dataList;
    private int currentPage = 0;
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.my_invitation), None);

        initList();
        initView();

        MobclickAgent.onEvent(mContext, "MyInvitationOnlick");
    }

    // 初始化List
    private void initList() {
        currentPage = 0;
        dataList = new ArrayList<InvitationModel>();
    }

    // 初始化View
    private void initView() {
        // headerView1
        headerView1 = LayoutInflater.from(mContext).inflate(R.layout.invitation_header_view1, null);
        imgPhoto = (ImageView) headerView1.findViewById(R.id.user_image_Photo);
        txtUserName = (TextView) headerView1.findViewById(R.id.user_txt_Name);
        txtYQCode = (TextView) headerView1.findViewById(R.id.user_txt_Code);
        txtCopy = (TextView) headerView1.findViewById(R.id.userCode_txt_Copy);
        // headerView2
        headerView2 = LayoutInflater.from(mContext).inflate(R.layout.invitation_header_view2, null);
        txtNum = (TextView) headerView2.findViewById(R.id.YQheader_txt_yqNum);
        txtPrice = (TextView) headerView2.findViewById(R.id.YQheader_txt_yqPrice);
        // headerView3
        headerView3 = LayoutInflater.from(mContext).inflate(R.layout.invitation_header_view3, null);
        txtLookRule = (TextView) headerView3.findViewById(R.id.YQheader_txt_rule);

        // footer View
        VFoot = LayoutInflater.from(mContext).inflate(R.layout.invitation_foot_view, null);
        // WebView
        ruleWeb = (WebView) VFoot.findViewById(R.id.YQfoot_webView_rule);
        ruleWeb.getSettings().setJavaScriptEnabled(true);
        ruleWeb.getSettings().setLoadWithOverviewMode(true); //自适应屏幕
        ruleWeb.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); //不使用缓存
        ruleWeb.loadUrl(WebUrl.guizeinvitation);
        ruleWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ruleWeb.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }
        });
        // Button
        btnShare = (Button) findViewById(R.id.invitation_btn_share);
        // Refresh
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.Invitation_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.Invitation_listView);
        loadErrorWidget = (LoadDataErrorWidget) findViewById(R.id.Invitation_loaddataView);
        // add HeadrView and FootView
        mListView.addHeaderView(headerView1, null, false);
        mListView.addHeaderView(headerView2, null, false);
        mListView.addHeaderView(headerView3, null, false);
        mListView.addFooterView(VFoot, null, false);

        // setAdapter
        adapter = new InvitationAdapter(mContext, dataList, 1);
        mListView.setAdapter(adapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);
        //
        setDrawLeft();
        // setListener
        setListener();
        CheckNet();
    }

    // 设置左边图标
    private void setDrawLeft() {
        Drawable drawNum = ContextCompat.getDrawable(mContext, R.mipmap.icon_invitation_num);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示  
        drawNum.setBounds(0, 0, DensityUtil.dp2px(mContext, 24), DensityUtil.dp2px(mContext, 24));
        txtNum.setCompoundDrawables(drawNum, null, null, null);

        Drawable drawPrice = ContextCompat.getDrawable(mContext, R.mipmap.icon_invitation_reward);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示  
        drawPrice.setBounds(0, 0, DensityUtil.dp2px(mContext, 24), DensityUtil.dp2px(mContext, 24));
        txtPrice.setCompoundDrawables(drawPrice, null, null, null);
    }

    // 检查网络
    private void CheckNet() {
        if (NetWorkState.isNetworkAvailable(mContext)) {
            btnShare.setBackgroundResource(R.drawable.login_btn_onclick_bg);
            btnShare.setEnabled(true);
            mPtrFrame.setVisibility(View.VISIBLE);
            loadErrorWidget.setVisibility(View.GONE);
            getInvitationData();
//            getInvitationListData(true);
        } else {
            btnShare.setBackgroundResource(R.mipmap.btn_login_gray);
            btnShare.setEnabled(false);
            mPtrFrame.setVisibility(View.GONE);
            loadErrorWidget.setVisibility(View.VISIBLE);
            loadErrorWidget.netWorkError();
        }
    }

    // 获取邀请用户数据
    private void getInvitationData() {
        GetInvitionUserInfoRequest InvitionUserInfoRequest = new GetInvitionUserInfoRequest(WebUrl.getInvitationUserInfo);
        InvitionUserInfoRequest.setReturnDataClick(mContext, false, 0, this);
    }

    // 获取邀请列表数据
    private void getInvitationListData(boolean isshowLoad) {
        GetInvitionListRequest listRequest = new GetInvitionListRequest(WebUrl.getInvitationList);
        listRequest.setRequestParms(null, currentPage, Constants.pageSize);
        listRequest.setReturnDataClick(mContext, isshowLoad, 1, this);
    }

    // 添加监听
    private void setListener() {
        btnShare.setOnClickListener(this);
        txtCopy.setOnClickListener(this);
        txtLookRule.setOnClickListener(this);
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
                getInvitationData();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userCode_txt_Copy:
                MobclickAgent.onEvent(mContext, "InvitationCopyOnclick");
                try {
                    StringUtil.copy(mContext, strqrCode);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case R.id.YQheader_txt_rule:
                MobclickAgent.onEvent(mContext, "InvitationLookRuleOnclick");
                Intent intent = new Intent(mContext, PubWebViewActivity.class);
                intent.putExtra(PubWebViewActivity.WebUrl, WebUrl.guizeinvitation);
                startActivity(intent);
                break;
            case R.id.invitation_btn_share:
                MobclickAgent.onEvent(mContext, "invitation_btn_share");
                ShareWindow shareWindow = new ShareWindow(mContext);
                shareWindow.setView(true);
                shareWindow.setShareData(R.mipmap.share_friend_icon, Defaultcontent.shareFriendtitle,
                        Defaultcontent.shareFriendtext, Defaultcontent.shareFriendurl);
                shareWindow.show();
                break;
        }
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
                    SPUtils.put(mContext, Configs.user_Photo, entity.getUs_photo());
                    SPUtils.put(mContext, Configs.user_Name, entity.getUs_nickname());
                    imageManager.loadCircleImage(entity.getUs_photo(), imgPhoto);
                    txtUserName.setText(entity.getUs_nickname());
                    strqrCode = entity.getUser_id();
                    txtCopy.setVisibility(View.VISIBLE);
                    txtYQCode.setText("邀请码: " + strqrCode);
                    txtNum.setText(entity.getInvitationNum() + "人");
                    txtPrice.setText(entity.getReward_balance() + "元");
                }
                getInvitationListData(true);
                break;
            case 1:
                if (entity != null) {
                    List<InvitationModel> dataL = entity.getList();
                    if (dataL != null && dataL.size() > 0) {
                        txtLookRule.setVisibility(View.VISIBLE);
                        mListView.removeFooterView(VFoot);

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
                            txtLookRule.setVisibility(View.GONE);
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

//    @Override
//    public void onRightCallback(View view) {
//        // 发提醒
//        openActivity(mContext, SendReminderActivity.class);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
        UMShareAPI.get(this).release();
        CallServer.getInstance().cancelBySign(mContext);
    }
}
