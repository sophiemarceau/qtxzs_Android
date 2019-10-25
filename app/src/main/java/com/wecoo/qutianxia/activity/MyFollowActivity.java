package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuStateChangeListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.FollowTouchAdapter;
import com.wecoo.qutianxia.adapter.FollowTouchAdapter.RefreshUIByCancelListener;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.FollowEntity;
import com.wecoo.qutianxia.requestjson.MyFollowRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.DensityUtil;
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
 * 我的关注
 */

public class MyFollowActivity extends TitleBarActivity implements OnReLoadClickListener {

    private final String mPageName = "MyFollowActivity";
    private Context mContext = MyFollowActivity.this;
    private PtrWecooFrameLayout mPtrFrame;
    private SwipeMenuListView menuListView;
    private FollowTouchAdapter mAdapter;
    private LoadDataErrorWidget loadDataView;
    private List<FollowEntity.FollowModels> dataList;
    private int currentPage = 0;
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.my_follow), None);

        initList();
        initView();

        MobclickAgent.onEvent(mContext, "MyFollowOnlick");
    }

    // 初始化List
    private void initList() {
        currentPage = 0;
        dataList = new ArrayList<FollowEntity.FollowModels>();
    }

    private void initView() {
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.mFollow_PtrFrameLayout);
        menuListView = (SwipeMenuListView) findViewById(R.id.mFollow_SwipeMenuListView);
        loadDataView = (LoadDataErrorWidget) findViewById(R.id.follow_loadDataView);

        mAdapter = new FollowTouchAdapter(mContext, dataList);
        menuListView.setAdapter(mAdapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);

        setListener();
        initData(true);
    }

    private void setListener() {
        // 重新加载监听
        loadDataView.setOnReLoadClickListener(this);
        //    滑动监听
        menuListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(R.color.wecoo_theme_color);
                // set item width
                openItem.setWidth(DensityUtil.dp2px(mContext,80));
                // set item title
                openItem.setTitle("取消关注");
                // set item title fontsize
                openItem.setTitleSize(16);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };
        // set creator
        menuListView.setMenuCreator(creator);
        // step 2. listener item click event
        menuListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                if (index == 0){
                    mAdapter.deleteData(position, new RefreshUIByCancelListener() {
                        @Override
                        public void onRefreshUIByDelete() {
                            dataList.remove(position);
                            mAdapter.notifyDataSetChanged();
                            if (dataList.size() > 0 && dataList.size() < 10){
                                footview.changeStatus(footview.LOADED);
                                menuListView.setOnScrollListener(null);
                                booFooter = false;
                            }else if (dataList.size() == 0){
                                mPtrFrame.setVisibility(View.GONE);
                                loadDataView.setVisibility(View.VISIBLE);
                                loadDataView.dataLoadError();
                            }
                        }
                    });
                }
                return false;
            }
        });
        // set SwipeListener
        menuListView.setOnSwipeListener(new OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start(滑动开始)
                mPtrFrame.setEnabled(false);
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end(滑动结束)
            }
        });
        // set MenuStateChangeListener
        menuListView.setOnMenuStateChangeListener(new OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
                //  (滑动menu显示)
                mPtrFrame.setEnabled(false);
            }

            @Override
            public void onMenuClose(int position) {
                //  (滑动menu隐藏)
                mPtrFrame.setEnabled(true);
            }
        });
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
                initData(false);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, menuListView, header);
            }
        });
        // 加载更多
        menuListView.setOnScrollListener(loadListener);
    }

    // 加载更多
    private LoadMoreListener loadListener = new LoadMoreListener() {
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

    // 获取关注的数据
    private void initData(boolean isshowLoad) {
        if (NetWorkState.isNetworkAvailable(mContext)) {
            MyFollowRequest followRequest = new MyFollowRequest();
            followRequest.setRequestParms(currentPage, Constants.pageSize);
            followRequest.setReturnDataClick(mContext, isshowLoad, 0, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    FollowEntity entity = (FollowEntity) obj;
                    if (entity != null) {
                        if (entity.getList() != null && entity.getList().size() > 0) {
                            mPtrFrame.setVisibility(View.VISIBLE);
                            loadDataView.setVisibility(View.GONE);

                            dataList.addAll(entity.getList());
                            mAdapter.setData(dataList);
                            if (currentPage == 0) {
                                mPtrFrame.refreshComplete();
                                if (isShowFoot) {
                                    menuListView.addFooterView(footview);
                                    isShowFoot = false;
                                }
                            }
                            if (entity.getList().size() == Constants.pageSize) {
                                if (booFooter) return;
                                footview.changeStatus(footview.LOADING);
                                menuListView.setOnScrollListener(loadListener);
                                booFooter = true;
                            } else {
                                footview.changeStatus(footview.LOADED);
                                menuListView.setOnScrollListener(null);
                                booFooter = false;
                            }
                        } else {
                            if (currentPage == 0) {
                                mPtrFrame.setVisibility(View.GONE);
                                loadDataView.setVisibility(View.VISIBLE);
                                loadDataView.dataLoadError();
                            } else {
                                footview.changeStatus(footview.LOADED);
                                menuListView.setOnScrollListener(null);
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
