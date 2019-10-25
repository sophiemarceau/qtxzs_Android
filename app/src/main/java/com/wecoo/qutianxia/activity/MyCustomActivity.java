package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuStateChangeListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.MyCustomAdapter;
import com.wecoo.qutianxia.adapter.MyCustomAdapter.RefreshUIByDeleteListener;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.base.TitleBarActivity.RightCallbackListener;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.CustomInfoEntity;
import com.wecoo.qutianxia.models.MyCustomEntity;
import com.wecoo.qutianxia.requestjson.MyCustomRequest;
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

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by mwl on 2016/10/25.
 * 我的客户
 */

public class MyCustomActivity extends TitleBarActivity implements RightCallbackListener,
        View.OnClickListener, OnReLoadClickListener {

    private final String mPageName = "MyCustomActivity";
    private Context mContext = MyCustomActivity.this;
    private RadioGroup rgView;
    // 全部    锁定中    未锁定
    private RadioButton rbAll, rbLocking, rbNoLock;
    private String isLock = null;
    private View dataView;
    private LoadDataErrorWidget errorWidget;
    private PtrWecooFrameLayout mPtrFrame;
    private SwipeMenuListView menuListView;
    private MyCustomAdapter customAdapter;
    private ArrayList<CustomInfoEntity> dataList;
    private int currentPage = 0;
    private String titleBar = null;
    private PtrFooterView footview;// 加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycustom_layout);
        AppManager.getAppManager().addActivity(this);

        titleBar = getIntent().getStringExtra(AddReportActivity.TITLEBAR);
        initActionBar(this);
        if (TextUtils.isEmpty(titleBar)) {
            setBanner(Left, getString(R.string.my_customer), getString(R.string.add_customer));
            setRightCallbackListener(this);
        } else {
            setBanner(Left, getString(R.string.select_customer), None);
        }

        initList();
        initView();

        MobclickAgent.onEvent(mContext, "MyCustomOnlick");
    }

    // 初始化List
    private void initList() {
        currentPage = 0;
        dataList = new ArrayList<CustomInfoEntity>();
    }

    // 初始化view
    private void initView() {
        rgView = (RadioGroup) findViewById(R.id.myCustom_rg_view);
        rbAll = (RadioButton) findViewById(R.id.myCustom_rb_all);
        rbLocking = (RadioButton) findViewById(R.id.myCustom_rb_locking);
        rbNoLock = (RadioButton) findViewById(R.id.myCustom_rb_nolock);
        TextView txtContent = (TextView) findViewById(R.id.mycustom_txt_content);
        dataView = findViewById(R.id.mycustom_ll_dataview);
        errorWidget = (LoadDataErrorWidget) findViewById(R.id.mycustom_loadDataView);

        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.mCustom_PtrFrameLayout);
        menuListView = (SwipeMenuListView) findViewById(R.id.mCustom_SwipeMenuListView);

        customAdapter = new MyCustomAdapter(MyCustomActivity.this, dataList);
        menuListView.setAdapter(customAdapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);

        if (!TextUtils.isEmpty(titleBar)) {
            rgView.setVisibility(View.GONE);
            txtContent.setText("被锁定的客户已帮您隐藏");
        } else {
            txtContent.setText(getString(R.string.customcontent));
        }
        setListener();
    }

    // 添加监听
    private void setListener() {
        rbAll.setOnClickListener(this);
        rbLocking.setOnClickListener(this);
        rbNoLock.setOnClickListener(this);
        // 重新加载监听
        errorWidget.setOnReLoadClickListener(this);
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
                openItem.setWidth(DensityUtil.dp2px(mContext,120));
                // set item title
                openItem.setTitle("删除客户");
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
                    customAdapter.deleteData(position, new RefreshUIByDeleteListener() {
                        @Override
                        public void onRefreshUIByDelete() {
                            dataList.remove(position);
                            customAdapter.notifyDataSetChanged();
                            if (dataList.size() > 0 && dataList.size() < 10){
                                footview.changeStatus(footview.LOADED);
                                menuListView.setOnScrollListener(null);
                                booFooter = false;
                            }else if (dataList.size() == 0){
                                dataView.setVisibility(View.GONE);
                                errorWidget.setVisibility(View.VISIBLE);
                                errorWidget.dataLoadError();
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
        // Item 点击监听
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                if (TextUtils.isEmpty(titleBar)) {
                    intent.setClass(mContext, CustomInfoActivity.class);
                    intent.putExtra(CustomInfoActivity.TITLEBAR, getString(R.string.custominfo));
                    intent.putExtra(CustomInfoActivity.CUSTOMER_ID, dataList.get(i).getCustomer_id());
                    startActivity(intent);
                } else {
                    Bundle bundle = new Bundle();
                    intent.setClass(mContext, AddReportActivity.class);
                    bundle.putSerializable(AddReportActivity.CUSTOMINFO, dataList.get(i));
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
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

    //  我的客户数据
    private void initData(boolean isshowLoad) {
        if (NetWorkState.isNetworkAvailable(mContext)) {
            MyCustomRequest customRequest = new MyCustomRequest();
            customRequest.setRequestParms(isLock, currentPage, Constants.pageSize);
            customRequest.setReturnDataClick(mContext, isshowLoad, 0, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    MyCustomEntity entity = (MyCustomEntity) obj;
                    if (entity != null) {
                        if (entity.getList() != null && entity.getList().size() > 0) {
                            dataView.setVisibility(View.VISIBLE);
                            errorWidget.setVisibility(View.GONE);

                            dataList.addAll(entity.getList());
                            customAdapter.setData(dataList);
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
                                dataView.setVisibility(View.GONE);
                                errorWidget.setVisibility(View.VISIBLE);
                                errorWidget.dataLoadError();
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
            dataView.setVisibility(View.GONE);
            errorWidget.setVisibility(View.VISIBLE);
            errorWidget.netWorkError();
        }
    }

//    // 获取报备锁定天数
//    private void getReportLockTime() {
//        GetReportLockTimeRequest lockTimeRequest = new GetReportLockTimeRequest();
//        lockTimeRequest.setReturnDataClick(mContext, 0, new ReturnDataClick() {
//            @Override
//            public void onReturnData(int what, Object obj) {
//                if (obj != null) {
//                    reportLockTime = (int) obj;
//                    String content = getString(R.string.customcontent);
//                    content = String.format(content, reportLockTime);
//                    txtContent.setVisibility(View.VISIBLE);
//                    txtContent.setText(getString(R.string.customcontent));
//                }
//            }
//        });
//    }

    @Override
    public void onClick(View view) {
        initList();
        switch (view.getId()) {
            case R.id.myCustom_rb_all:
                isLock = null;
                initData(true);
                break;
            case R.id.myCustom_rb_locking:
                isLock = "1";
                initData(true);
                break;
            case R.id.myCustom_rb_nolock:
                isLock = "0";
                initData(true);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(titleBar)) {
            initList();
            initData(false);
        } else {
            isLock = "0";
            initData(true);
        }
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

    // 添加客户
    @Override
    public void onRightCallback(View view) {
        MobclickAgent.onEvent(mContext, "MyCustom_AddOnclick");
        Intent intent = new Intent(mContext, CustomInfoActivity.class);
        intent.putExtra(CustomInfoActivity.TITLEBAR, getString(R.string.add_customer));
        startActivity(intent);
    }

    @Override
    public void OnReLoadData() {
        currentPage = 0;
        initData(true);
    }
}
