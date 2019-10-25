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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.ContactAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.manager.HandlerManager;
import com.wecoo.qutianxia.models.ContactEntity;
import com.wecoo.qutianxia.models.ContactModel;
import com.wecoo.qutianxia.models.InvitationEntity;
import com.wecoo.qutianxia.requestjson.GetContactListRequest;
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
 * Created by mwl on 2017/2/17.
 * 我的人脉
 */

public class MyContactsActivity extends TitleBarActivity implements OnClickListener,
        OnReLoadClickListener, ReturnDataClick/*, RightCallbackListener */{

    private final String mPageName = "MyContactsActivity";
    private Activity mContext = MyContactsActivity.this;
    private View VAllContact, VProfit;
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private ContactAdapter adapter;
    private LoadDataErrorWidget loadErrorWidget;
    private Button btnShare;
    // Header View
    private String strqrCode = "";
    private ImageView imgPhoto;
    // 全部人脉   人脉收益
    private TextView txtNum, txtPrice;
    // 用户昵称   邀请码    复制   查看规则
    private TextView txtUserName, txtYQCode, txtCopy, txtLookRule;
    // 没有数据显示H5
    private LinearLayout llNodata;
    private WebView wvNodata;
    // 分页加载数据
    private List<ContactModel> dataList;
    private int currentPage = 0;
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.my_contacts), None);

        initList();
        initView();

        MobclickAgent.onEvent(mContext, "MyContactsOnlick");
    }

    // 初始化List
    private void initList() {
        currentPage = 0;
        dataList = new ArrayList<ContactModel>();
    }

    // 初始化View
    private void initView() {
        llNodata = (LinearLayout) findViewById(R.id.contacts_ll_nodata);
        wvNodata = (WebView) findViewById(R.id.contacts_wv_nodata);
        // headerView1
        View headerView1 = LayoutInflater.from(mContext).inflate(R.layout.invitation_header_view1, null);
        imgPhoto = (ImageView) headerView1.findViewById(R.id.user_image_Photo);
        txtUserName = (TextView) headerView1.findViewById(R.id.user_txt_Name);
        txtYQCode = (TextView) headerView1.findViewById(R.id.user_txt_Code);
        txtCopy = (TextView) headerView1.findViewById(R.id.userCode_txt_Copy);
        // headerView2
        View headerView2 = LayoutInflater.from(mContext).inflate(R.layout.invitation_header_view2, null);
        VAllContact = headerView2.findViewById(R.id.YQheader_yqNum_ll);
        VProfit = headerView2.findViewById(R.id.YQheader_Price_ll);
        txtNum = (TextView) headerView2.findViewById(R.id.YQheader_txt_yqNum);
        txtPrice = (TextView) headerView2.findViewById(R.id.YQheader_txt_yqPrice);
        TextView txtAllContact = (TextView) headerView2.findViewById(R.id.headerView_txt_all);
        TextView txtProfit = (TextView) headerView2.findViewById(R.id.headerView_txt_profit);
        txtAllContact.setText(getString(R.string.contacts_all));
        txtProfit.setText(getString(R.string.contacts_Profit));
        // headerView3
        View headerView3 = LayoutInflater.from(mContext).inflate(R.layout.invitation_header_view3, null);
        txtLookRule = (TextView) headerView3.findViewById(R.id.YQheader_txt_rule);
        txtLookRule.setVisibility(View.VISIBLE);
        txtLookRule.setText("查看合伙人规则 >>");

        // setWebview
        wvNodata.getSettings().setUseWideViewPort(true); //将图片调整到适合WebView的大小
        wvNodata.getSettings().setLoadWithOverviewMode(true); //自适应屏幕
        wvNodata.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); //不使用缓存
        wvNodata.loadUrl(WebUrl.partnernull);
        wvNodata.setWebViewClient(new WebViewClient());
        // 分享
        btnShare = (Button) findViewById(R.id.contacts_btn_share);
        // Refresh
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.myContact_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.myContact_listView);
        loadErrorWidget = (LoadDataErrorWidget) findViewById(R.id.myContact_loaddataView);
        // add HeadrView
//        listView.addHeaderView(VHeader);
        //这样可以让HeaderView不可点击
        mListView.addHeaderView(headerView1);
        mListView.addHeaderView(headerView2);
        mListView.addHeaderView(headerView3);
        // setAdapter
        adapter = new ContactAdapter(mContext, dataList);
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
            if (wvNodata != null) {
                wvNodata.reload();
            }
            getContactInfoData();
        } else {
            btnShare.setBackgroundResource(R.mipmap.btn_login_gray);
            btnShare.setEnabled(false);
            mPtrFrame.setVisibility(View.GONE);
            loadErrorWidget.setVisibility(View.VISIBLE);
            loadErrorWidget.netWorkError();
        }
    }

    // 获取邀请用户数据
    private void getContactInfoData() {
        GetInvitionUserInfoRequest contactUserInfoRequest
                = new GetInvitionUserInfoRequest(WebUrl.getMyConnectionCountAndContributionSum);
        contactUserInfoRequest.setReturnDataClick(mContext, false, 0, this);
    }

    // 获取邀请列表数据
    private void getgetContactListData(boolean isshowLoad) {
        GetContactListRequest listRequest = new GetContactListRequest();
        listRequest.setRequestParms(currentPage, 10);
        listRequest.setReturnDataClick(mContext, isshowLoad, 1, this);
    }

    // 添加监听
    private void setListener() {
        VAllContact.setOnClickListener(this);
        VProfit.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        txtCopy.setOnClickListener(this);
        txtLookRule.setOnClickListener(this);
        mListView.setOnScrollListener(loadMoreListener);
        loadErrorWidget.setOnReLoadClickListener(this);
        // 下拉刷新
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (!NetWorkState.isNetworkAvailable(mContext)) {
                    mPtrFrame.refreshComplete();
                    ToastUtil.showShort(mContext, getString(R.string.load_data_nonetwork));
                    return;
                }
                initList();
                getContactInfoData();
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
            getgetContactListData(false);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userCode_txt_Copy:
                try {
                    StringUtil.copy(mContext, strqrCode);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case R.id.YQheader_yqNum_ll:
                openActivity(mContext, ContactListActivity.class);
                break;
            case R.id.YQheader_Price_ll:
                openActivity(mContext, ContactProfitActivity.class);
                break;
            case R.id.YQheader_txt_rule:// 查看规则
                Intent intent = new Intent(mContext, PubWebViewActivity.class);
                intent.putExtra(PubWebViewActivity.WebUrl, WebUrl.guizepartner);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case R.id.contacts_btn_share:
                MobclickAgent.onEvent(mContext, "contacts_btn_share");
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
        switch (what) {
            case 0:
                InvitationEntity infoEntity = (InvitationEntity) obj;
                if (infoEntity != null) {
                    SPUtils.put(mContext, Configs.user_Photo, infoEntity.getUs_photo());
                    SPUtils.put(mContext, Configs.user_Name, infoEntity.getUs_nickname());
                    imageManager.loadCircleImage(infoEntity.getUs_photo(), imgPhoto);
                    txtUserName.setText(infoEntity.getUs_nickname());
                    strqrCode = infoEntity.getUser_id();
                    txtCopy.setVisibility(View.VISIBLE);
                    txtYQCode.setText("邀请码: " + strqrCode);
                    txtNum.setText(infoEntity.getInvitationNum() + "人");
                    txtPrice.setText(infoEntity.getReward_balance() + "元");
                    // 请求列表
                    if ("0".equals(infoEntity.getInvitationNum())) {
                        mPtrFrame.setVisibility(View.GONE);
                        llNodata.setVisibility(View.VISIBLE);
                    } else {
                        getgetContactListData(false);
                    }
                }
                break;
            case 1:
                ContactEntity entity = (ContactEntity) obj;
                if (entity != null) {
                    List<ContactModel> dataL = entity.getList();
                    if (dataL != null && dataL.size() > 0) {
                        mPtrFrame.setVisibility(View.VISIBLE);
                        llNodata.setVisibility(View.GONE);

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
                            llNodata.setVisibility(View.VISIBLE);
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
//
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
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
