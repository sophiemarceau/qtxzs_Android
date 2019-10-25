package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.ContactAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.base.TitleBarActivity.RightCallbackListener;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.manager.ScreenResultManager;
import com.wecoo.qutianxia.models.ContactEntity;
import com.wecoo.qutianxia.models.ContactModel;
import com.wecoo.qutianxia.models.ScreenEntity;
import com.wecoo.qutianxia.models.ScreenModel;
import com.wecoo.qutianxia.requestjson.GetContactListRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.ColorUtil;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.SortPopWindow;
import com.wecoo.qutianxia.view.SortPopWindow.OnSortClick;
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
 * 人脉列表
 */

public class ContactListActivity extends TitleBarActivity implements RightCallbackListener,
        OnScreenListener, OnReLoadClickListener, ReturnDataClick {

    private final String mPageName = "ContactListActivity";
    private Context mContext = ContactListActivity.this;
    private ScreenTypeView screenView;
    private List<ScreenEntity> contactList;// 人脉筛选数据
    private List<ScreenEntity> contributionList; // 贡献筛选数据
    private List<ScreenEntity> timeList; //时间筛选数据
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private ContactAdapter adapter;
    private LoadDataErrorWidget loadErrorWidget;
    private TextView txt_Cumulative;// 累计赏金显示
    private SortPopWindow sortPopWindow;
    // 分页加载数据
    private List<ContactModel> dataList;
    private int currentPage = 0;
    private String sil_level, contribution_type, contribution_sum, invitation_count,
            report_count, signedup_count, sil_createdtime, sortType, sort_desc_or_asc;
    private boolean isScreen = false; // 筛选
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactlist_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.contacts_list), getString(R.string.sort_order));
        setRightCallbackListener(this);

        //
        initList();
        initData();
        initView();
    }

    // 初始化List
    private void initList() {
        currentPage = 0;
        dataList = new ArrayList<ContactModel>();
    }

    // 初始化筛选数据
    private void initData() {
        contactList = ModelManager.getInstance().getScreenContactData();
        contributionList = ModelManager.getInstance().getContributionData();
        timeList = ModelManager.getInstance().getTimesData("注册时间");
    }

    // 初始化View
    private void initView() {
        screenView = (ScreenTypeView) findViewById(R.id.contactList_screenTypeView);
        txt_Cumulative = (TextView) findViewById(R.id.contactList_txt_Cumulative);
        String cumulative = "当前筛选人数: " + "0" + " 人";
        txt_Cumulative.setText(ColorUtil.getTextColor(cumulative, 8, (cumulative.length() - 2)));
        // Refresh
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.ContactList_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.ContactList_listView);
        loadErrorWidget = (LoadDataErrorWidget) findViewById(R.id.ContactList_loaddataView);
        // setAdapter
        adapter = new ContactAdapter(mContext, dataList);
        mListView.setAdapter(adapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);
        //
        screenView.setTextContent(null, "贡献", null);
        screenView.setData(contactList, contributionList, timeList);
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
                getContactListData(false);
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
            getContactListData(false);
        }
    };

    // 检查网络
    private void CheckNet() {
        if (NetWorkState.isNetworkAvailable(mContext)) {
            mPtrFrame.setVisibility(View.VISIBLE);
            loadErrorWidget.setVisibility(View.GONE);
            getContactListData(true);
        } else {
            mPtrFrame.setVisibility(View.GONE);
            loadErrorWidget.setVisibility(View.VISIBLE);
            loadErrorWidget.netWorkError();
        }
    }

    // 获取邀请列表数据
    private void getContactListData(boolean isshowLoad) {
        GetContactListRequest listRequest = new GetContactListRequest();
        GetContactListRequest.ContactListParms parms = new GetContactListRequest.ContactListParms();
        parms._currentPage = currentPage;
        parms._pageSize = Constants.pageSize;
        parms._sil_level = sil_level;
        parms._contribution_type = contribution_type;
        parms._contribution_sum = contribution_sum;
        parms._invitation_count = invitation_count;
        parms._report_count = report_count;
        parms._signedup_count = signedup_count;
        parms._sil_createdtime = sil_createdtime;
        parms._sortType = sortType;
        parms._sort_desc_or_asc = sort_desc_or_asc;
        listRequest.setRequestParms(parms);
        listRequest.setReturnDataClick(mContext, isshowLoad, 0, this);
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
        sortPopWindow = null;
        ScreenResultManager.getManager().cleanAll();
    }

    @Override
    public void onBackPressed() {
        if (screenView != null && screenView.isShowing()) {
            screenView.hide();
        } else if (sortPopWindow != null && sortPopWindow.isShowing()) {
            sortPopWindow.disMissPop();
        } else {
            super.onBackPressed();
        }
    }

    // 排序方式
    @Override
    public void onRightCallback(View view) {
        if (screenView != null && screenView.isShowing()) {
            screenView.hide();
        }
        if (sortPopWindow == null) {
            sortPopWindow = new SortPopWindow(ContactListActivity.this);
        }
        sortPopWindow.showPopupWindow(view);
        sortPopWindow.setOnSortClick(new OnSortClick() {
            @Override
            public void onSortResult(String text) {
                sortType = "1";
                sort_desc_or_asc = text;
                initList();
                getContactListData(true);
            }

            @Override
            public void onSortTimeResult(String text) {
                sortType = "2";
                sort_desc_or_asc = text;
                initList();
                getContactListData(true);
            }
        });
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
                        if (model.getParentId() == 0) {
                            contribution_type = model.getCode();
                        } else if (model.getParentId() == 1) {
                            if (model.getPosition() == 0) {
                                contribution_sum = null;
                            } else {
                                contribution_sum = model.getCode();
                            }
                        } else if (model.getParentId() == 2) {
                            if (model.getPosition() == 0) {
                                invitation_count = null;
                            } else {
                                invitation_count = model.getCode();
                            }
                        } else if (model.getParentId() == 3) {
                            if (model.getPosition() == 0) {
                                report_count = null;
                            } else {
                                report_count = model.getCode();
                            }
                        } else if (model.getParentId() == 4) {
                            if (model.getPosition() == 0) {
                                signedup_count = null;
                            } else {
                                signedup_count = model.getCode();
                            }
                        }
                    } else if (model.getId() == 2) {
                        if (model.getPosition() == 0) {
                            sil_createdtime = null;
                        } else {
                            sil_createdtime = model.getCode();
                        }
                    }
                }
            }
        }
        initList();
        getContactListData(true);
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
                ContactEntity entity = (ContactEntity) obj;
                if (entity != null) {
                    String cumulative = "当前筛选人数: " + entity.getTotal_count() + " 人";
                    txt_Cumulative.setText(ColorUtil.getTextColor(cumulative, 8, (cumulative.length() - 2)));
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
                                loadErrorWidget.loadErrorOnlyTxt("没有符合筛选条件的用户，请重新筛选！");
                            } else {
                                loadErrorWidget.loadErrorOnlyTxt("您的人脉数为0，请先邀请用户成为您的下级人脉吧！");
                            }
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
