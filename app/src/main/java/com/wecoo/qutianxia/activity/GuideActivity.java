package com.wecoo.qutianxia.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.GuideAdapter;
import com.wecoo.qutianxia.base.BaseActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.utils.DensityUtil;
import com.wecoo.qutianxia.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 **/
public class GuideActivity extends BaseActivity implements OnPageChangeListener, OnClickListener {

    private final String mPageName = "GuideActivity";
    private ViewPager viewPager;
    private GuideAdapter guideAdapter;
    private LinearLayout indicator_layout;// 导航条布局
    private Button butnStart = null;
    private View viewPager1, viewPager2, viewPager3;
    private List<View> viewList = null;
    private ImageView imageView;
    private ImageView[] imageViews;// 导航条的数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_layout);

        initPager();
        AppManager.getAppManager().addActivity(this);
    }

    /**
     * 初始滑动Pager
     **/
    private void initPager() {
        viewPager = (ViewPager) findViewById(R.id.guide_viewpager);
        indicator_layout = (LinearLayout) findViewById(R.id.guide_indicator_layout);
        viewPager.setOnPageChangeListener(this);
        initView();
    }

    /**
     * 初始滑动View
     **/
    private void initView() {
        viewList = new ArrayList<View>();
        viewPager1 = LayoutInflater.from(GuideActivity.this).inflate(R.layout.activity_guide_pager1, null);
        viewList.add(viewPager1);
        viewPager2 = LayoutInflater.from(GuideActivity.this).inflate(R.layout.activity_guide_pager2, null);
        viewList.add(viewPager2);
        viewPager3 = LayoutInflater.from(GuideActivity.this).inflate(R.layout.activity_guide_pager3, null);
        viewList.add(viewPager3);
        butnStart = (Button) viewPager3.findViewById(R.id.pager_butn_start);
        butnStart.setOnClickListener(this);
        initIndicator(viewList);
    }

    /**
     * 添加导航圆点
     **/
    private void initIndicator(List<View> viewLists) {
        if (viewLists != null && viewLists.size() > 1) {
            imageViews = new ImageView[viewLists.size()];
            // 设定导航样式小图标
            for (int i = 0; i < viewLists.size(); i++) {
                imageView = new ImageView(this);
                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(DensityUtil.dp2px(GuideActivity.this, 7f), DensityUtil.dp2px(GuideActivity.this, 7f));
                ll.setMargins(DensityUtil.dp2px(GuideActivity.this, 4f), 0, DensityUtil.dp2px(GuideActivity.this, 4f), 0);
                imageView.setLayoutParams(ll);
                // 把n个创建的小图标存到imageViews[View]中
                imageViews[i] = imageView;
                if (i == 0) {
                    // 默认选中图片
                    imageViews[i]
                            .setBackgroundResource(R.mipmap.guidepager_index_true);
                } else {
                    imageViews[i]
                            .setBackgroundResource(R.mipmap.guidepager_index_false);
                }
                indicator_layout.addView(imageViews[i]);// 分别添加到导航中LinearLayout条中
            }
        }

        guideAdapter = new GuideAdapter(viewLists);
        viewPager.setAdapter(guideAdapter);
    }

    @Override
    public void onClick(View view) {
        SPUtils.put(GuideActivity.this, Configs.IsInstall, true);
        openActivity(GuideActivity.this, LoginActivity.class);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // pager选择时变化
        if (position == viewList.size() - 1) {
            indicator_layout.setVisibility(View.INVISIBLE);
        } else {
            indicator_layout.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[position % viewList.size()]
                    .setBackgroundResource(R.mipmap.guidepager_index_true);
            if (position % viewList.size() != i) {
                imageViews[i]
                        .setBackgroundResource(R.mipmap.guidepager_index_false);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
}
