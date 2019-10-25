package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.ContactProfitAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.manager.ScreenResultManager;
import com.wecoo.qutianxia.models.ContactEntity;
import com.wecoo.qutianxia.models.ContactModel;
import com.wecoo.qutianxia.models.ScreenEntity;
import com.wecoo.qutianxia.models.ScreenModel;
import com.wecoo.qutianxia.requestjson.GetContProfitSumRequest;
import com.wecoo.qutianxia.requestjson.GetContactProfitRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.ColorUtil;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.refreshload.PtrFooterView;
import com.wecoo.qutianxia.view.refreshload.PtrWecooFrameLayout;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;
import com.wecoo.qutianxia.widget.ScreenTypeView;
import com.wecoo.qutianxia.widget.ScreenTypeView.OnScreenListener;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by mwl on 2017/2/18.
 * 人脉收益
 */

public class ContactProfitActivity extends TitleBarActivity implements OnScreenListener,
        OnReLoadClickListener, ReturnDataClick {

    private final String mPageName = "ContactProfitActivity";
    private Context mContext = ContactProfitActivity.this;
    private ScreenTypeView screenView;
    private List<ScreenEntity> contactList;// 人脉筛选数据
    private List<ScreenEntity> typesList; // 类型筛选数据
    private List<ScreenEntity> timeList; //时间筛选数据
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private ContactProfitAdapter adapter;
    private LoadDataErrorWidget loadErrorWidget;
    private TextView txt_Cumulative;// 累计赏金显示
    // 分页加载数据
    private List<ContactModel> dataList;
    private int currentPage = 0;
    private String sil_level, sal_partner_income_kind, sal_createdtime;
    private boolean isScreen = false;
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactprofit_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.contacts_Profit), None);

        //
        initList();
        initData();
        initView();
    }

    private void initList() {
        currentPage = 0;
        dataList = new ArrayList<ContactModel>();
    }

    // 初始化筛选数据
    private void initData() {
        contactList = ModelManager.getInstance().getScreenContactData();
        typesList = ModelManager.getInstance().getTypesData();
        timeList = ModelManager.getInstance().getTimesData("赏金时间");
    }

    // 初始化View
    private void initView() {
        screenView = (ScreenTypeView) findViewById(R.id.contactProfit_screenTypeView);
        txt_Cumulative = (TextView) findViewById(R.id.contactProfit_txt_Cumulative);
        String cumulative = "当前筛选收益: " + "0.00" + " 元";
        txt_Cumulative.setText(ColorUtil.getTextColor(cumulative, 8, (cumulative.length() - 2)));

        // Refresh
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.ContactProfit_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.ContactProfit_listView);
        loadErrorWidget = (LoadDataErrorWidget) findViewById(R.id.ContactProfit_loaddataView);
        //
        adapter = new ContactProfitAdapter(mContext, dataList);
        mListView.setAdapter(adapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);
        //
        assert screenView != null;
        screenView.setData(contactList, typesList, timeList);
        screenView.setScreenListener(this);
        // setListener
        setListener();
        CheckNet();
    }

    // 添加监听
    private void setListener() {
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
                getGetProfitData(false);
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
            getGetProfitData(false);
        }
    };

    // 检查网络
    private void CheckNet() {
        if (NetWorkState.isNetworkAvailable(mContext)) {
            mPtrFrame.setVisibility(View.VISIBLE);
            loadErrorWidget.setVisibility(View.GONE);
            getGetProfitSum();
            getGetProfitData(true);
        } else {
            mPtrFrame.setVisibility(View.GONE);
            loadErrorWidget.setVisibility(View.VISIBLE);
            loadErrorWidget.netWorkError();
        }
    }

    // 获取收益总额
    private void getGetProfitSum() {
        GetContProfitSumRequest sumRequest = new GetContProfitSumRequest();
        GetContProfitSumRequest.ProfitSumParms parms = new GetContProfitSumRequest.ProfitSumParms();
        parms._sil_level = sil_level;
        parms._sal_partner_income_kind = sal_partner_income_kind;
        parms._sal_createdtime = sal_createdtime;
        sumRequest.setRequestParms(parms);
        sumRequest.setReturnDataClick(mContext, 1, this);
    }

    // 获取收益列表
    private void getGetProfitData(boolean isshowLoad) {
        GetContactProfitRequest profitRequest = new GetContactProfitRequest();
        GetContactProfitRequest.ContactProfitParms parms = new GetContactProfitRequest.ContactProfitParms();
        parms._currentPage = currentPage;
        parms._pageSize = Constants.pageSize;
        parms._sil_level = sil_level;
        parms._sal_partner_income_kind = sal_partner_income_kind;
        parms._sal_createdtime = sal_createdtime;
        profitRequest.setRequestParms(parms);
        profitRequest.setReturnDataClick(mContext, isshowLoad, 0, this);
    }

    // 筛选确定按钮
    @Override
    public void onScreenResult(Object object) {
        List<ScreenModel> resultList = (List<ScreenModel>) object;
        if (resultList != null) {
            if (resultList.size() > 0) {
                isScreen = true;
                for (int i = 0; i < resultList.size(); i++) {
                    ScreenModel model = resultList.get(i);
                    if (model.getId() == 0) {
                        if (model.getPosition() == 0) {
                            sil_level = null;
                        } else {
                            sil_level = model.getCode();
                        }
                    } else if (model.getId() == 1) {
                        if (model.getPosition() == 0) {
                            sal_partner_income_kind = null;
                        } else {
                            sal_partner_income_kind = model.getCode();
                        }
                    } else if (model.getId() == 2) {
                        if (model.getPosition() == 0) {
                            sal_createdtime = null;
                        } else {
                            sal_createdtime = model.getCode();
                        }
                    }
                }
            }
        }
        initList();
        getGetProfitData(true);
        getGetProfitSum();
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
        CallServer.getInstance().cancelBySign(mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenResultManager.getManager().cleanAll();
    }

    @Override
    public void onBackPressed() {
        if (screenView.isShowing()) {
            screenView.hide();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void OnReLoadData() {
        initList();
        CheckNet();
    }

    // 请求成功回调
    @Override
    public void onReturnData(int what, Object obj) {
        switch (what) {
            case 0:
                ContactEntity entity = (ContactEntity) obj;
                if (entity != null) {
                    List<ContactModel> dataL = entity.getList();
                    if (dataL != null && dataL.size() > 0) {
                        mPtrFrame.setVisibility(View.VISIBLE);
                        loadErrorWidget.setVisibility(View.GONE);

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
                            loadErrorWidget.setVisibility(View.VISIBLE);
                            if (isScreen) {
                                loadErrorWidget.loadErrorOnlyTxt("没有符合筛选条件的收益流水，请重新筛选！");
                            } else {
                                loadErrorWidget.loadErrorOnlyTxt("您当前的人脉收益为0，通知您的人脉尽快去推荐吧！");
                            }
                        } else {
                            footview.changeStatus(footview.LOADED);
                            mListView.setOnScrollListener(null);
                            booFooter = false;
                        }
                    }
                }
                break;
            case 1:
                String resultSum = (String) obj;
                if (!TextUtils.isEmpty(resultSum)) {
                    String cumulative = "当前筛选收益: " + resultSum + " 元";
                    txt_Cumulative.setText(ColorUtil.getTextColor(cumulative, 8, (cumulative.length() - 2)));
                }
                break;
        }
    }
}
