package com.wecoo.qutianxia.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.HomeAdapter;
import com.wecoo.qutianxia.base.BaseFragment;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.models.BannerEntity;
import com.wecoo.qutianxia.models.ProjectEntity;
import com.wecoo.qutianxia.models.ProjectModels;
import com.wecoo.qutianxia.requestjson.GetBannerRequest;
import com.wecoo.qutianxia.requestjson.GetHomeDataRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.ColorUtil;
import com.wecoo.qutianxia.utils.DensityUtil;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.HomeheaderView;
import com.wecoo.qutianxia.view.refreshload.PtrFooterView;
import com.wecoo.qutianxia.view.refreshload.PtrWecooFrameLayout;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static com.wecoo.qutianxia.R.color.wecoo_theme_color;

/**
 * Created by mwl on 2016/10/20.
 * 首页
 */
public class HomeFragment extends BaseFragment implements OnReLoadClickListener, ReturnDataClick {

    private final String mPageName = "HomeFragment";
    private View rootView, topView;
    private TextView txtTitle;
    private final int BannerWhat = 0, HomeListWhat = 1;
    private int currentPage = 0;
    private HomeheaderView headerAdView; // 首页头部视图
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private PtrFooterView footview;//加载更多的View
    //  是否添加Footer   加载更多是否可用
    private boolean isShowFoot = true, booFooter = true;
    private LoadDataErrorWidget errorWidget;
    private HomeAdapter homeAdapter;
    private List<BannerEntity> bannerList;// 轮播图数据
    private List<ProjectModels> datalist;// 首页推荐项目数据
    // 标题栏渐变的显示
    private boolean isScrollIdle = true; // ListView是否在滑动
    private float adViewHeight = 158; // 广告视图的高度
    private float adViewTopSpace; // 广告视图距离顶部的距离


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home_layout, container, false);
            initList();
            initView();
        }
        return rootView;
    }

    // 初始化List
    private void initList() {
        currentPage = 0;
        bannerList = new ArrayList<BannerEntity>();
        datalist = new ArrayList<ProjectModels>();
    }

    private void initView() {
        // 头部导航
        topView = rootView.findViewById(R.id.top_View);
        txtTitle = (TextView) topView.findViewById(R.id.title_textView_center);
        txtTitle.setText(getResources().getString(R.string.home));

        mPtrFrame = (PtrWecooFrameLayout) rootView.findViewById(R.id.home_PtrFrameLayout);
        // listview
        mListView = (ListView) rootView.findViewById(R.id.home_listView);
        errorWidget = (LoadDataErrorWidget) rootView.findViewById(R.id.home_LoadDataError);
        // 初始化加载更多View
        footview = new PtrFooterView(getActivity());

        setListener();
        initData(true);
    }

    // 设置监听
    private void setListener() {
        // 重新加载数据
        errorWidget.setOnReLoadClickListener(this);
        // listView 滚动监听
        mListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                isScrollIdle = (i == OnScrollListener.SCROLL_STATE_IDLE);
                if (booFooter) {
                    switch (i) {
                        // 判断滚动到底部且不滚动时，执行加载
                        case OnScrollListener.SCROLL_STATE_IDLE:
                            if (absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
                                if (!NetWorkState.isNetworkAvailable(getActivity())) {
                                    footview.changeStatus(footview.NOTNET);
                                    return;
                                } else {
                                    footview.changeStatus(footview.LOADING);
                                }
                                currentPage++;
                                getHomeListData(false);
                            }
                            break;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (isScrollIdle && adViewTopSpace < 0) return;
                if (headerAdView != null) {
                    adViewTopSpace = DensityUtil.px2dp(getActivity(), headerAdView.getTop());
                    adViewHeight = DensityUtil.px2dp(getActivity(), headerAdView.getHeights());
                }
                // 处理标题栏颜色渐变
                handleTitleBarColorEvaluate();
            }
        });

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
                initData(false);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }
        });
    }

    // 处理标题栏颜色渐变
    private void handleTitleBarColorEvaluate() {
        float fraction;
        if (adViewTopSpace > 0) {
            fraction = 1f - adViewTopSpace * 1f / 60;
            if (fraction < 0f) fraction = 0f;
            topView.setAlpha(fraction);
            return;
        }

        float space = Math.abs(adViewTopSpace) * 1f;
        fraction = space / (adViewHeight - 50);// 50 是标题栏的高度
        if (fraction < 0f) fraction = 0f;
        if (fraction > 1f) fraction = 1f;
        topView.setAlpha(1f);

        if (fraction >= 1f) {
            topView.setBackgroundColor(ContextCompat.getColor(getActivity(), wecoo_theme_color));
            txtTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        } else {
            topView.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(getActivity(), fraction, R.color.transparent, wecoo_theme_color));
            txtTitle.setTextColor(ColorUtil.getNewColorByStartEndColor(getActivity(), fraction, R.color.transparent, R.color.white));
        }
    }

    // 设置广告数据
    private void setView(List<BannerEntity> bannerList) {
        if (headerAdView == null) {
            headerAdView = new HomeheaderView(getActivity());
            headerAdView.fillView(bannerList, mListView);
        } else {
            headerAdView.resetHeaderView(bannerList);
        }

        // 设置ListView数据
        homeAdapter = new HomeAdapter(getActivity(), datalist);
        mListView.setAdapter(homeAdapter);
    }

    // 获取首页数据
    private void initData(boolean isshowLoad) {
        if (NetWorkState.isNetworkAvailable(getActivity())) {
            getBannerData(isshowLoad);
        } else {
            mPtrFrame.setVisibility(View.GONE);
            errorWidget.setVisibility(View.VISIBLE);
            errorWidget.netWorkError();
        }
    }

    // 获取Banner数据
    private void getBannerData(boolean isshowLoad) {
        GetBannerRequest bannerRequest = new GetBannerRequest();
        bannerRequest.setReturnDataClick(getActivity(), isshowLoad, BannerWhat, this);
    }

    // 获取首页列表数据
    private void getHomeListData(boolean isshowLoad) {
        GetHomeDataRequest dataRequest = new GetHomeDataRequest();
        dataRequest.setRequestParms(currentPage, Constants.pageSize);
        dataRequest.setReturnDataClick(getActivity(), isshowLoad, HomeListWhat, this);
    }

    @Override
    public void onReturnData(int what, Object obj) {
        switch (what) {
            case BannerWhat:
                bannerList = (List<BannerEntity>) obj;
                if (bannerList != null && bannerList.size() > 0) {
                    setView(bannerList);
                }
                getHomeListData(false);
                break;
            case HomeListWhat:
                ProjectEntity entity = (ProjectEntity) obj;
                if (entity != null) {
                    if (entity.getList() != null && entity.getList().size() > 0) {
                        mPtrFrame.setVisibility(View.VISIBLE);
                        errorWidget.setVisibility(View.GONE);

                        datalist.addAll(entity.getList());
                        homeAdapter.setData(datalist);
                        if (currentPage == 0) {
                            mPtrFrame.refreshComplete();// 刷新完成
                            if (isShowFoot) {
                                mListView.addFooterView(footview);
                                isShowFoot = false;
                            }
                        }
                        if (entity.getList().size() == Constants.pageSize) {
                            if (booFooter) return;
                            footview.changeStatus(footview.LOADING);
                            booFooter = true;
                        } else {
                            footview.changeStatus(footview.LOADED);
                            booFooter = false;
                        }
                    } else {
                        if (currentPage == 0) {
                            if (bannerList != null && bannerList.size() > 0) {
                                return;
                            } else {
                                mPtrFrame.setVisibility(View.GONE);
                                errorWidget.setVisibility(View.VISIBLE);
                                errorWidget.dataLoadError();
                            }
                        } else {
                            footview.changeStatus(footview.LOADED);
                            booFooter = false;
                        }
                    }
                }
                break;
        }
    }

    // 无网变有网时点击刷新
    @Override
    public void OnReLoadData() {
        initList();
        initData(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        SPUtils.put(getActivity(), Configs.CurrentTab, 0);
        MobclickAgent.onPageStart(mPageName);
        if (NetWorkState.isNetworkAvailable(getActivity())) {
            topView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
            txtTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.transparent));
        } else {
            topView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.wecoo_theme_color));
            txtTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        }
        if (headerAdView != null) {
            if (bannerList != null && bannerList.size() > 1) {
                headerAdView.resume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        if (headerAdView != null) {
            headerAdView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CallServer.getInstance().cancelBySign(getActivity());
    }

}
