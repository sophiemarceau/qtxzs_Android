package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.MyMsgAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.MyMsgEntity;
import com.wecoo.qutianxia.requestjson.GetMsgRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.SetMsgReadedRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.refreshload.PtrFooterView;
import com.wecoo.qutianxia.view.refreshload.PtrWecooFrameLayout;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by mwl on 2016/10/25.
 * 我的消息
 */

public class MyMsgActivity extends TitleBarActivity implements OnReLoadClickListener {

    private final String mPageName = "MyMsgActivity";
    private Context mContext = MyMsgActivity.this;
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private MyMsgAdapter msgAdapter;
    private LoadDataErrorWidget loadDataView;
    private ArrayList<MyMsgEntity.MsgModels> msgList;
    private int currentPage = 0;
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.my_msgcenter), None);

        initList();
        initView();
        setMsgReaded();
    }

    // 初始化List
    private void initList() {
        currentPage = 0;
        msgList = new ArrayList<MyMsgEntity.MsgModels>();
    }

    // 初始化VIew
    private void initView() {
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.mymsg_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.mymsg_listView);
        loadDataView = (LoadDataErrorWidget) findViewById(R.id.mymsg_loaddataView);
        msgAdapter = new MyMsgAdapter(mContext, msgList);
        mListView.setAdapter(msgAdapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);

        setListener();
        initData(true);
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

    // 获取消息数据
    private void initData(boolean isshowLoad) {
        if (NetWorkState.isNetworkAvailable(mContext)) {
            GetMsgRequest msgRequest = new GetMsgRequest();
            msgRequest.setRequestParms(currentPage, Constants.pageSize);
            msgRequest.setReturnDataClick(mContext, isshowLoad, 0, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    MyMsgEntity entitys = (MyMsgEntity) obj;
                    if (entitys != null) {
                        if (entitys.getList() != null && entitys.getList().size() > 0) {
                            mPtrFrame.setVisibility(View.VISIBLE);
                            loadDataView.setVisibility(View.GONE);

                            msgList.addAll(entitys.getList());
                            msgAdapter.setData(msgList);
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

    // 设置为已读消息
    private void setMsgReaded() {
        // 设置为已读消息
        SetMsgReadedRequest setMsgReadedRequest = new SetMsgReadedRequest();
        setMsgReadedRequest.setReturnDataClick(mContext, 1);
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
