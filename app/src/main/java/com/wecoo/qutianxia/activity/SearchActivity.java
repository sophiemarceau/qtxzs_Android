package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.SearchAdapter;
import com.wecoo.qutianxia.base.BaseActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.listener.NoDoubleClickListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.models.FilterData;
import com.wecoo.qutianxia.models.FilterEntity;
import com.wecoo.qutianxia.models.ProjectEntity;
import com.wecoo.qutianxia.models.ProjectModels;
import com.wecoo.qutianxia.requestjson.GetSearchDataRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.ColorUtil;
import com.wecoo.qutianxia.utils.KeyBoardUtil;
import com.wecoo.qutianxia.utils.LogUtil;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.FilterView;
import com.wecoo.qutianxia.view.refreshload.PtrFooterView;
import com.wecoo.qutianxia.view.refreshload.PtrWecooFrameLayout;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by mwl on 2017/04/25.
 * 项目的搜索页面
 */

public class SearchActivity extends BaseActivity implements OnReLoadClickListener {

    private final String mPageName = "SearchActivity";
    private Context mContext;
    private View llView;
    private EditText editName;
    private ImageView ivClose;
    private TextView tvCancel, txtCount;
    public static int filterStatus = -1;
    private FilterData filterData; // 排序分类数据
    protected FilterView filterView; // 排序分类的View
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private PtrFooterView footview;//加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;
    private SearchAdapter sAdapter;
    private LoadDataErrorWidget errorWidget;
    // 排序的索引   行业的code
    private String strSort = "5", strCode = null;
    private int currentPage = 0;
    // 搜索的数据
    private List<ProjectModels> searchList;
    private boolean searchFlag = false;// 点击搜索之后，刷新加载才起作用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        AppManager.getAppManager().addActivity(this);

        mContext = SearchActivity.this;

        initList();
        initView();
    }

    private void initList() {
        currentPage = 0;
        searchList = new ArrayList<ProjectModels>();
    }

    // 初始化View
    private void initView() {
        llView = findViewById(R.id.search_content_view);
        editName = (EditText) findViewById(R.id.search_edit_projectName);
        ivClose = (ImageView) findViewById(R.id.search_right_ivClose);
        tvCancel = (TextView) findViewById(R.id.search_right_txCancel);
        txtCount = (TextView) findViewById(R.id.search_txtCount);
        //
        filterView = (FilterView) findViewById(R.id.search_top_filter);
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.search_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.search_listView);
        errorWidget = (LoadDataErrorWidget) findViewById(R.id.search_LoadDataError);
        sAdapter = new SearchAdapter(mContext, searchList);
        mListView.setAdapter(sAdapter);
        // 初始化加载更多View
        footview = new PtrFooterView(mContext);
        // 设置刚进来的时候不可以刷新和加载
//        llView.setVisibility(View.GONE);
//        filterView.setVisibility(View.GONE);

        setListener();
        initData();
        searchData(false, 5);
    }

    // 设置监听
    private void setListener() {
        //  加载设置
        errorWidget.setOnReLoadClickListener(this);
        //  清空输入内容    取消
        ivClose.setOnClickListener(clickListener);
        tvCancel.setOnClickListener(clickListener);
        // 搜索框的点击
        editName.setOnClickListener(clickListener);
        // 输入键盘的点击
        editName.setOnEditorActionListener(actionListener);
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editName.setCursorVisible(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    ivClose.setVisibility(View.VISIBLE);
                } else {
                    ivClose.setVisibility(View.GONE);
                }
            }
        });
        mListView.setOnScrollListener(scrollListener);
        // 刷新
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (!NetWorkState.isNetworkAvailable(mContext)) {
                    mPtrFrame.refreshComplete();
                    ToastUtil.showShort(mContext, getString(R.string.load_data_nonetwork));
                    return;
                }
                if (!searchFlag) {
                    mPtrFrame.refreshComplete();
                    return;
                }
                initList();
                searchData(false, Constants.pageSize);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }
        });
    }

    // 列表滚动，关闭输入框和加载更多
    private LoadMoreListener scrollListener = new LoadMoreListener() {
        @Override
        public void onLoadMore() {
            if (searchFlag) {
                if (!NetWorkState.isNetworkAvailable(mContext)) {
                    footview.changeStatus(footview.NOTNET);
                    return;
                } else {
                    footview.changeStatus(footview.LOADING);
                }
                currentPage++;
                searchData(false, Constants.pageSize);
            }
        }
    };

    //  初始化排序和行业的数据
    private void initData() {
        if (application.IndustryList == null
                || application.IndustryList.size() < 3) {
            application.IndustryList = ModelManager.getInstance().getLookupIndustryAll(mContext);
        } else {
            for (int i = 0; i < application.IndustryList.size(); i++) {
                if (application.IndustryList.get(i).isSelected()) {
                    application.IndustryList.get(i).setSelected(false);
                }
            }
        }
        // 排序和分类数据
        filterData = new FilterData();
        filterData.setSorts(ModelManager.getInstance().getSortData());
        List<FilterEntity> indusList = new ArrayList<FilterEntity>();
        FilterEntity entityT = new FilterEntity();
        entityT.setName("全部行业");
        entityT.setIconUrl(R.mipmap.icon_industry_all);
        entityT.setCode(null);
        entityT.setSelected(true);
        indusList.add(entityT);
        indusList.addAll(application.IndustryList);
        filterData.setFilters(indusList);

        setFilterListener();
    }

    // 筛选的数据和监听设置
    private void setFilterListener() {
        filterView.setFilterData(mContext, filterData);
        filterView.setOnFilterClickListener(new FilterView.OnFilterClickListener() {
            @Override
            public void onFilterClick(int position) {
                if (filterData.getFilters() == null || filterData.getFilters().size() < 3) {
                    initData();
                }
                if (filterStatus == position && filterView.isShowing()) {
                    filterView.resetAllStatus();
                    filterStatus = -1;
                } else {
                    filterView.showFilterLayout(position);
                    filterStatus = position;
                }
            }
        });
        // 排序的点击
        filterView.setOnItemSortClickListener(new FilterView.OnItemSortClickListener() {
            @Override
            public void onItemSortClick(FilterEntity entity) {
                filterView.setSortShow(entity.getName());
                strSort = entity.getCode();
                initList();
                searchData(true, Constants.pageSize);
            }
        });
        // 分类的点击
        filterView.setOnItemTypeClickListener(new FilterView.OnItemTypeClickListener() {
            @Override
            public void onItemTypeClick(FilterEntity entity) {
                filterView.setTypeShow(entity.getName());
                strCode = entity.getCode();
                initList();
                searchData(true, Constants.pageSize);
            }
        });
    }

    // 搜索查询数据
    private void searchData(boolean isshowLoad, final int pageSize) {
        final String name = editName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            txtCount.setVisibility(View.GONE);
        } else {
            txtCount.setVisibility(View.VISIBLE);
        }
        if (NetWorkState.isNetworkAvailable(mContext)) {
            GetSearchDataRequest dataRequest = new GetSearchDataRequest();
            dataRequest.setRequestParms(strSort, name, strCode, currentPage, pageSize);
            dataRequest.setReturnDataClick(mContext, isshowLoad, 0, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    ProjectEntity entity = (ProjectEntity) obj;
                    if (entity != null) {
                        String start = "搜索到 " + entity.getTotal_count() + " 条 “";
                        String end = "” 相关的数据";
                        String content = start + name + end;
                        txtCount.setText(ColorUtil.getTextColor(content, start.length(), (content.length() - end.length())));
                        if (entity.getList() != null && entity.getList().size() > 0) {
                            mPtrFrame.setVisibility(View.VISIBLE);
                            errorWidget.setVisibility(View.GONE);
                            // 刷新数据
                            searchList.addAll(entity.getList());
                            sAdapter.setData(searchList);
                            if (pageSize == Constants.pageSize) {
                                llView.setVisibility(View.VISIBLE);
                                filterView.setVisibility(View.VISIBLE);
                                if (currentPage == 0) {
                                    mListView.smoothScrollToPosition(0);
                                    mPtrFrame.refreshComplete();
                                    if (isShowFoot) {
                                        mListView.addFooterView(footview);
                                        isShowFoot = false;
                                    }
                                }
                                if (entity.getList().size() == Constants.pageSize) {
                                    mListView.setOnScrollListener(scrollListener);
                                    if (booFooter) return;
                                    footview.changeStatus(footview.LOADING);
                                    booFooter = true;
                                } else {
                                    footview.changeStatus(footview.LOADED);
                                    mListView.setOnScrollListener(null);
                                    booFooter = false;
                                }
                            }else {
                                txtCount.setVisibility(View.VISIBLE);
                                txtCount.setText("推荐项目");
                                mListView.setOnScrollListener(null);
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
                }
            });
        } else {
            mPtrFrame.setVisibility(View.GONE);
            errorWidget.setVisibility(View.VISIBLE);
            errorWidget.netWorkError();
        }
    }

    private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.search_right_ivClose:
                    editName.setText("");
                    ivClose.setVisibility(View.GONE);
                    break;
                case R.id.search_right_txCancel:
                    finish();
                    break;
                case R.id.search_edit_projectName:
                    editName.setCursorVisible(true);
                    if (filterView != null && filterView.isShowing()) {
                        filterView.resetAllStatus();
                        filterStatus = -1;
                    }
                    break;
            }
        }
    };

    @Override
    public void OnReLoadData() {
        currentPage = 0;
        initData();
        searchData(true, Constants.pageSize);
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
        KeyBoardUtil.closeKeybord(editName, mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallServer.getInstance().cancelBySign(mContext);
    }

    // 软键盘按键监听
    private OnEditorActionListener actionListener = new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                searchFlag = true;
                initList();
                searchData(true, Constants.pageSize);
                if (filterView != null && filterView.isShowing()) {
                    filterView.resetAllStatus();
                    filterStatus = -1;
                }
                closeKeybord();
                return true;
            }
            return false;
        }
    };

    // 输入框失去光标
    private void closeKeybord() {
        editName.setCursorVisible(false);
    }
}
