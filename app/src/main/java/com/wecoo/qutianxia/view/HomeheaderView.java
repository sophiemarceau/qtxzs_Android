package com.wecoo.qutianxia.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.MainActivity;
import com.wecoo.qutianxia.activity.MyBalanceActivity;
import com.wecoo.qutianxia.activity.MyFollowActivity;
import com.wecoo.qutianxia.activity.MyInvitationActivity;
import com.wecoo.qutianxia.activity.MyReportActivity;
import com.wecoo.qutianxia.activity.ProjectInfoActivity;
import com.wecoo.qutianxia.activity.PubWebViewActivity;
import com.wecoo.qutianxia.activity.RewardActivity;
import com.wecoo.qutianxia.manager.HandlerManager;
import com.wecoo.qutianxia.models.BannerEntity;
import com.wecoo.qutianxia.requestjson.CheckNoviceGuideRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.utils.DensityUtil;
import com.wecoo.qutianxia.utils.StringUtil;

import java.util.List;

/**
 * Created by mwl on 2016/10/24.
 * 首页的轮播图
 */

public class HomeheaderView extends HeaderViewInterface<List<BannerEntity>> implements View.OnClickListener {

    private View view;
    private ConvenientBanner convenientBanner;
    private LinearLayout llIndexContainer, llNewbieGuide;
    // 活动  我的邀请   我的关注   我的报备
    private TextView btnActive, btnInvitation, btnFollow, btnReport;
    private String projectId = "";

    public HomeheaderView(Activity context) {
        super(context);
    }

    @Override
    protected void getView(List<BannerEntity> list, ListView listView) {
        view = mInflate.inflate(R.layout.home_header_layout, listView, false);
        convenientBanner = (ConvenientBanner) view.findViewById(R.id.home_header_ConvenientBanner);
        llIndexContainer = (LinearLayout) view.findViewById(R.id.home_header_container);
        llNewbieGuide = (LinearLayout) view.findViewById(R.id.home_headerView_newbie_guide);

        btnActive = (TextView) view.findViewById(R.id.home_header_btnActive);
        btnInvitation = (TextView) view.findViewById(R.id.home_header_btnInvitation);
        btnFollow = (TextView) view.findViewById(R.id.home_header_btnFollow);
        btnReport = (TextView) view.findViewById(R.id.home_header_btnReport);

        setDrawTop(btnActive, R.mipmap.icon_homemenu_01);
        setDrawTop(btnInvitation, R.mipmap.icon_homemenu_02);
        setDrawTop(btnFollow, R.mipmap.icon_homemenu_03);
        setDrawTop(btnReport, R.mipmap.icon_homemenu_04);

        btnActive.setOnClickListener(this);
        btnInvitation.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        btnReport.setOnClickListener(this);
        view.findViewById(R.id.home_headerView_Orders_report).setOnClickListener(this);
        view.findViewById(R.id.home_headerView_ReportCustom).setOnClickListener(this);
        view.findViewById(R.id.home_headerView_Withdrawals).setOnClickListener(this);

        //
        dealWithTheView(list);
        //
        listView.addHeaderView(view);

    }

    public void resetHeaderView(List<BannerEntity> list) {
        dealWithTheView(list);
    }

    // 检查是否显示新手引导
    private void isShowNewbieGuide() {
        CheckNoviceGuideRequest guideRequest = new CheckNoviceGuideRequest();
        guideRequest.setReturnDataClick(mContext, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                if (obj != null) {
                    String isShow = (String) obj;
                    if (isShow.indexOf("@") > 0) {
                        // “1”显示，“0”不显示
                        String isShowNewbieGuide = isShow.substring(0, isShow.indexOf("@"));
                        if (isShow.length() - isShowNewbieGuide.length() > 2) {
                            projectId = isShow.substring(isShow.indexOf("@") + 1, isShow.length());
                        }
                        if ("0".equals(isShowNewbieGuide)) {
                            llNewbieGuide.setVisibility(View.GONE);
                        } else {
                            llNewbieGuide.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    // 设置快速入口的上边图标
    private void setDrawTop(TextView txt, int drawId) {
        Drawable drawTop = ContextCompat.getDrawable(mContext, drawId);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示  
        drawTop.setBounds(0, 0, DensityUtil.dp2px(mContext, 56), DensityUtil.dp2px(mContext, 50));
        txt.setCompoundDrawables(null, drawTop, null, null);
    }

    // 添加图片数组
    private void dealWithTheView(final List<BannerEntity> list) {
        if (list != null) {
            convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }
            }, list)//设置需要切换的View
                    .setPointViewVisible(false)
//                    .setPageIndicator(new int[]{R.mipmap.home_pager_index_false, R.mipmap.home_pager_index_true})   //设置指示器圆点
//                    .startTurning(3500) //设置自动切换（同时设置了切换时间间隔）
                    //设置指示器位置（左、中、右）
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            int newPosition = position % list.size();
                            String webUrl = list.get(newPosition).getAd_url();
                            String project_id = list.get(newPosition).getProject_id();
                            MobclickAgent.onEvent(mContext, "HomeHeaderBanner");
                            if (StringUtil.isHttpUrl(webUrl)) {
                                Intent intent = new Intent(mContext, PubWebViewActivity.class);
                                intent.putExtra(PubWebViewActivity.WebUrl, webUrl);
                                mContext.startActivity(intent);
                            }/*else if (!TextUtils.isEmpty(project_id) && !"0".equals(project_id)) {
                                Intent intent = new Intent(mContext, ProjectInfoActivity.class);
                                intent.putExtra(ProjectInfoActivity.P_ID, project_id);
                                mContext.startActivity(intent);
                            }*/
                        }
                    });
            if (list.size() > 1) {
                resume();
                llIndexContainer.setVisibility(View.VISIBLE);
                addIndicatorImageViews(list.size());
                setViewPagerChangeListener(list);
            } else {
                pause();
                llIndexContainer.setVisibility(View.GONE);
            }
        }

        // 是否显示新手引导
        isShowNewbieGuide();
    }

    // 添加指示图标
    private void addIndicatorImageViews(int size) {
        llIndexContainer.removeAllViews();
        for (int i = 0; i < size; i++) {
            ImageView iv = new ImageView(mContext);
            LayoutParams lp = new LayoutParams(DensityUtil.dp2px(mContext, 10), DensityUtil.dp2px(mContext, 5));
            if (i != 0) {
                lp.leftMargin = DensityUtil.dp2px(mContext, 5);
            }
            iv.setLayoutParams(lp);
            iv.setEnabled(false);
            iv.setImageResource(R.mipmap.home_pager_index_false);
            if (i == 0) {
                iv.setEnabled(true);
                iv.setImageResource(R.mipmap.home_pager_index_true);
            }
            llIndexContainer.addView(iv);
        }
    }

    // 为ViewPager设置监听器
    private void setViewPagerChangeListener(final List<BannerEntity> ivList) {
        convenientBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (ivList != null && ivList.size() > 0) {
                    int newPosition = position % ivList.size();
                    for (int i = 0; i < ivList.size(); i++) {
                        ImageView iv = (ImageView) llIndexContainer.getChildAt(i);
                        iv.setEnabled(false);
                        iv.setImageResource(R.mipmap.home_pager_index_false);
                        if (i == newPosition) {
                            iv.setEnabled(true);
                            iv.setImageResource(R.mipmap.home_pager_index_true);
                        }
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    // 开始轮播
    public void resume() {
        convenientBanner.startTurning(3500);
    }

    // 暂停轮播
    public void pause() {
        if (convenientBanner.isTurning()) {
            convenientBanner.stopTurning();
        }
    }

    // 跳转
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.home_header_btnActive:
                MobclickAgent.onEvent(mContext, "home_header_btnActive");
                intent.setClass(mContext, RewardActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.home_header_btnInvitation:
                MobclickAgent.onEvent(mContext, "home_header_btnInvitation");
                intent.setClass(mContext, MyInvitationActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.home_header_btnFollow:
                MobclickAgent.onEvent(mContext, "home_header_btnFollow");
                intent.setClass(mContext, MyFollowActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.home_header_btnReport:
                MobclickAgent.onEvent(mContext, "home_header_btnReport");
                intent.setClass(mContext, MyReportActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.home_headerView_Orders_report:
                // 接单报备
                MobclickAgent.onEvent(mContext, "home_headerView_Orders_report");
                MainActivity.setCurrentTab(1);
                break;
            case R.id.home_headerView_ReportCustom:
                MobclickAgent.onEvent(mContext, "home_headerView_Progress_look");
                if (TextUtils.isEmpty(projectId)) {
                    MainActivity.setCurrentTab(1);
                } else {
                    intent.setClass(mContext, ProjectInfoActivity.class);
                    intent.putExtra(ProjectInfoActivity.P_ID, projectId);
                    intent.putExtra("isShowBottom", true);
                }
//                intent.setClass(mContext, MyReportActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.home_headerView_Withdrawals:
                MobclickAgent.onEvent(mContext, "home_headerView_Withdrawals");
                intent.setClass(mContext, MyBalanceActivity.class);
                mContext.startActivity(intent);
                break;
        }
    }

    public int getTop() {
        return view.getTop();
    }

    public int getHeights() {
        return view.getHeight();
    }
}
