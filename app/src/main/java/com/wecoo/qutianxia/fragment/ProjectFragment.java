package com.wecoo.qutianxia.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.AddReportActivity;
import com.wecoo.qutianxia.activity.SearchActivity;
import com.wecoo.qutianxia.adapter.ProjectAdapter;
import com.wecoo.qutianxia.base.BaseFragment;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.LoadMoreListener;
import com.wecoo.qutianxia.listener.NoDoubleClickListener;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.models.FilterData;
import com.wecoo.qutianxia.models.FilterEntity;
import com.wecoo.qutianxia.models.ProjectEntity;
import com.wecoo.qutianxia.models.ProjectModels;
import com.wecoo.qutianxia.requestjson.GetProjectDataRequest;
import com.wecoo.qutianxia.requestjson.IsReportAllowedRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.FilterView;
import com.wecoo.qutianxia.view.FilterView.OnFilterClickListener;
import com.wecoo.qutianxia.view.FilterView.OnItemSortClickListener;
import com.wecoo.qutianxia.view.FilterView.OnItemTypeClickListener;
import com.wecoo.qutianxia.view.refreshload.PtrFooterView;
import com.wecoo.qutianxia.view.refreshload.PtrWecooFrameLayout;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by mwl on 2016/10/20.
 * 项目
 */
public class ProjectFragment extends BaseFragment implements OnReLoadClickListener {

    private final String mPageName = "ProjectFragment";
    private View rootView;
    private ImageView imageLeft;
    private TextView btnReport;
    public static int filterStatus = -1;
    private FilterData filterData; // 排序分类数据
    protected FilterView filterView; // 排序分类的View
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private PtrFooterView footview;//加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true,booFooter = true;
    private ProjectAdapter pAdapter;
    private LoadDataErrorWidget errorWidget;
    private List<ProjectModels> dataList;
    // 排序的索引   行业的code
    private String strSort = "5", strCode = null;
    private int currentPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_project_layout, container, false);
            initList();
            initView();
        }
        return rootView;
    }

    // 初始化List
    private void initList() {
        currentPage = 0;
        dataList = new ArrayList<ProjectModels>();
    }

    private void initView() {
        View topView = rootView.findViewById(R.id.top_View);
        TextView txtTitle = (TextView) topView.findViewById(R.id.title_textView_center);
        txtTitle.setText(getResources().getString(R.string.add_project));
        imageLeft = (ImageView) topView.findViewById(R.id.title_image_leftBack);
        imageLeft.setImageResource(R.mipmap.icon_header_search);
        imageLeft.setVisibility(View.VISIBLE);
        btnReport = (TextView) topView.findViewById(R.id.title_text_right);
        btnReport.setVisibility(View.VISIBLE);
        btnReport.setText(getResources().getString(R.string.direct_reporting));

        // 以上是标题栏设置
        filterView = (FilterView) rootView.findViewById(R.id.project_top_filter);
        mPtrFrame = (PtrWecooFrameLayout) rootView.findViewById(R.id.project_PtrFrameLayout);
        mListView = (ListView) rootView.findViewById(R.id.project_listView);
        errorWidget = (LoadDataErrorWidget) rootView.findViewById(R.id.project_LoadDataError);
        pAdapter = new ProjectAdapter(getActivity(), dataList);
        mListView.setAdapter(pAdapter);
        // 初始化加载更多View
        footview = new PtrFooterView(getActivity());

        setListener();
        initData();
        // 项目的数据列表
        getProjectListData(true);
    }

    // 设置监听
    private void setListener() {
        //  搜索     按行业推荐客户
        imageLeft.setOnClickListener(clickListener);
        btnReport.setOnClickListener(clickListener);
        // 加载 设置
        errorWidget.setOnReLoadClickListener(this);
        mListView.setOnScrollListener(loadMoreListener);
        // 刷新
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (!NetWorkState.isNetworkAvailable(getActivity())) {
                    mPtrFrame.refreshComplete();
                    ToastUtil.showShort(getActivity(), getString(R.string.load_data_nonetwork));
                    return;
                }
                initList();
                getProjectListData(false);
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
            if (!NetWorkState.isNetworkAvailable(getActivity())) {
                footview.changeStatus(footview.NOTNET);
                return;
            } else {
                footview.changeStatus(footview.LOADING);
            }
            currentPage++;
            getProjectListData(false);
        }
    };

    // 筛选的数据和监听设置
    private void setFilterListener() {
        filterView.setFilterData(getActivity(), filterData);
        filterView.setOnFilterClickListener(new OnFilterClickListener() {
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
        filterView.setOnItemSortClickListener(new OnItemSortClickListener() {
            @Override
            public void onItemSortClick(FilterEntity entity) {
                filterView.setSortShow(entity.getName());
                strSort = entity.getCode();
                initList();
                getProjectListData(true);
            }
        });
        // 分类的点击
        filterView.setOnItemTypeClickListener(new OnItemTypeClickListener() {
            @Override
            public void onItemTypeClick(FilterEntity entity) {
                filterView.setTypeShow(entity.getName());
                strCode = entity.getCode();
                initList();
                getProjectListData(true);
            }
        });
    }

    // 设置按钮监听
    private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View view) {
            switch (view.getId()) {
                case R.id.title_image_leftBack:
                    MobclickAgent.onEvent(getActivity(), "ProjectSearchOnclick");
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.title_text_right:
                    //  统计按行业报备次数
                    MobclickAgent.onEvent(getActivity(), "ProjectReportOnclick");
                    // 跳转到报备页  当前业务员是否允许报备
                    IsReportAllowedRequest allowedRequest = new IsReportAllowedRequest();
                    allowedRequest.setReturnDataClick(getActivity(), 4, new ReturnDataClick() {
                        @Override
                        public void onReturnData(int what, Object obj) {
                            Intent intent = new Intent(getActivity(), AddReportActivity.class);
                            intent.putExtra(AddReportActivity.TITLEBAR, getString(R.string.direct_reporting));
                            startActivity(intent);
                        }
                    });

                    break;
            }
        }
    };

    // 排序和分类数据
    private void initData() {
        if (application.IndustryList == null
                || application.IndustryList.size() < 3) {
            application.IndustryList = ModelManager.getInstance().getLookupIndustryAll(getActivity());
        }
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

    // 获取项目列表数据
    private void getProjectListData(boolean isshowLoad) {
        if (NetWorkState.isNetworkAvailable(getActivity())) {
            GetProjectDataRequest dataRequest = new GetProjectDataRequest();
            dataRequest.setRequestParms(strSort, strCode, currentPage, Constants.pageSize);
            dataRequest.setReturnDataClick(getActivity(), isshowLoad, 0, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    ProjectEntity entity = (ProjectEntity) obj;
                    if (entity != null) {
                        if (entity.getList() != null && entity.getList().size() > 0) {
                            mPtrFrame.setVisibility(View.VISIBLE);
                            errorWidget.setVisibility(View.GONE);
                            // 刷新数据
                            dataList.addAll(entity.getList());
                            pAdapter.setData(dataList);
                            if (currentPage == 0) {
                                mListView.smoothScrollToPosition(0);
                                mPtrFrame.refreshComplete();// 刷新完成
                                if (isShowFoot) {
                                    mListView.addFooterView(footview);
                                    isShowFoot = false;
                                }
                            }
                            if (entity.getList().size() == Constants.pageSize){
                                if (booFooter) return;
                                footview.changeStatus(footview.LOADING);
                                mListView.setOnScrollListener(loadMoreListener);
                                booFooter = true;
                            }else {
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
                }
            });
        } else {
            mPtrFrame.setVisibility(View.GONE);
            errorWidget.setVisibility(View.VISIBLE);
            errorWidget.netWorkError();
        }
    }

    // 无网变有网时点击刷新
    @Override
    public void OnReLoadData() {
        currentPage = 0;
        initData();
        getProjectListData(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        SPUtils.put(getActivity(), Configs.CurrentTab, 1);
        MobclickAgent.onPageStart(mPageName);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        CallServer.getInstance().cancelBySign(getActivity());
        if (filterView != null && filterView.isShowing()) {
            filterView.resetAllStatus();
            filterStatus = -1;
        }
    }
}
