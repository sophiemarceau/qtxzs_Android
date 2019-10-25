package com.wecoo.qutianxia.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.base.TitleBarActivity.RightCallbackListener;
import com.wecoo.qutianxia.fragment.DescripWvFragment;
import com.wecoo.qutianxia.fragment.IntroduceWvFragment;
import com.wecoo.qutianxia.fragment.ListViewFragment;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.manager.HandlerManager;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.models.ProjectModels;
import com.wecoo.qutianxia.models.ProjectSignedEntity;
import com.wecoo.qutianxia.models.StringModels;
import com.wecoo.qutianxia.requestjson.AddFollowRequest;
import com.wecoo.qutianxia.requestjson.AddProjectRecordRequest;
import com.wecoo.qutianxia.requestjson.CancelFollowRequest;
import com.wecoo.qutianxia.requestjson.CheckFollowRequest;
import com.wecoo.qutianxia.requestjson.GetSignedDtoRequest;
import com.wecoo.qutianxia.requestjson.IsReportAllowedRequest;
import com.wecoo.qutianxia.requestjson.ProjectInfoRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.umeng.Defaultcontent;
import com.wecoo.qutianxia.umeng.ShareWindow;
import com.wecoo.qutianxia.utils.LogUtil;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.DialogFucengView;
import com.wecoo.qutianxia.widget.DragTopLayout;

import java.util.ArrayList;
import java.util.List;

import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerManager;

/**
 * Created by mwl on 2016/10/25.
 * 项目详情
 */

public class ProjectInfoActivity extends TitleBarActivity implements RightCallbackListener, View.OnClickListener {

    private final String mPageName = "ProjectInfoActivity";
    private Context mContext = ProjectInfoActivity.this;
    // 上个页面传来的 Key  Title  和  项目id
    public static String P_TITLE = "title", P_ID = "id";
    // Value  Title  和  项目id
    private String title = "", pid = "", IndustryCode = "";
    private boolean isFollow = false;    // 是否关注状态

    // 滑动切换的操作控件
    @SuppressLint("StaticFieldLeak")
    private static DragTopLayout dragLayout;
    private ModelPagerAdapter adapter;
    private ViewPager viewPager;
    private PagerSlidingTabStrip pagerSlidingTabStrip;

    //   占位 view    签约单数的view   佣金范围的view    关闭广播的Img  详情广播的 view    锦囊
    private View topView, viewNum, viewPrice, imgViewClose;
    //    广播的View
    private LinearLayout broadcastView;
    private View viewFengexian;

    // 关注   项目报备
    private Button btnFollow, btnReportCustom;
    // 标题   描述  成单数  赏金以及价格
    private TextView txtTitle, txtDesc, txtNum, txtYJ, txtPrice;
    private int signed_count = 0;       // 成单数量
    private String commission_note = "";// 赏金说明
//    private List<ProjectSignedEntity> signedList; // 成单列表

    // 消息动画
    private Animation anim_in, anim_out;
    private LinearLayout MsgsLayout;
    private Handler mHandler;
    private boolean runFlag = true;
    private int index = 0;
    // 首页进入详情（弹层）
    private LinearLayout llButtom;
    private TextView txtReport;
    private String project_pic_square;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectinfo_layout);
        AppManager.getAppManager().addActivity(this);

        title = getIntent().getStringExtra(P_TITLE);
        pid = getIntent().getStringExtra(P_ID);

        initActionBar(this);
        setBanner(Left, title, Right);
        setRightCallbackListener(this);

        initView();
        isFollow();
//        GetSignedData();
        addProjectRecord();
        // 如果是Home键等其他操作导致销毁重建，那么判断是否存在，
        // 如果存在，那么获取上次滚动到状态的index
        // 如果不存在，那么调用初始化方法
        if (null != savedInstanceState) {
            index = savedInstanceState.getInt("currIndex");
        }

    }

    private void initView() {
        //
        topView = findViewById(R.id.projectInfo_viewTop);
        llButtom = (LinearLayout) findViewById(R.id.projectInfo_ll_Buttom);
        txtReport = (TextView) findViewById(R.id.projectInfo_txt_reportCustom);
        boolean isShowBottom = getIntent().getBooleanExtra("isShowBottom", false);
        // 是否显示遮罩层
        if (isShowBottom) {
            llButtom.setVisibility(View.VISIBLE);
        } else {
            llButtom.setVisibility(View.GONE);
        }
        // DragTopLayout
        dragLayout = (DragTopLayout) findViewById(R.id.projectInfo_dragLayout);
        if (dragLayout != null) {
            dragLayout.setOverDrag(false);
            dragLayout.setCaptureTop(true);
        }
        pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.projectInfo_tabStrip);
        viewPager = (ViewPager) findViewById(R.id.projectInfo_ViewPager);

        // View
        viewNum = findViewById(R.id.projectInfo_ll_num);
        viewPrice = findViewById(R.id.projectInfo_ll_price);
        imgViewClose = findViewById(R.id.projectInfo_img_broadcast_close);
        // LinearLayout
        broadcastView = (LinearLayout) findViewById(R.id.projectInfo_ll_broadcast);
        viewFengexian = findViewById(R.id.projectInfo_view_fengexian);
        // 找到装载这个滚动TextView的LinearLayout 
        MsgsLayout = (LinearLayout) findViewById(R.id.projectInfo_msg_layout);

        // TextView
        txtTitle = (TextView) findViewById(R.id.projectInfo_txtTitle);
        txtDesc = (TextView) findViewById(R.id.projectInfo_txtDesc);
        txtNum = (TextView) findViewById(R.id.projectInfo_txtNum);
        txtYJ = (TextView) findViewById(R.id.projectInfo_txtYongjin);
        txtPrice = (TextView) findViewById(R.id.projectInfo_txtPrice);
        // Button
        btnFollow = (Button) findViewById(R.id.projectInfo_btn_Follow);
        btnReportCustom = (Button) findViewById(R.id.projectInfo_btn_reportCustom);

        setListener();
        getDetailData();
    }

    // Handle scroll event from fragments
    public static void onEvent(boolean b) {
        if (dragLayout != null) {
            dragLayout.setTouchMode(b);
        }
    }

    // 设置控件监听
    private void setListener() {
        llButtom.setOnClickListener(this);
        txtReport.setOnClickListener(this);
        viewNum.setOnClickListener(this);
        viewPrice.setOnClickListener(this);
        imgViewClose.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        btnReportCustom.setOnClickListener(this);
    }

    // 关注设置（判断是否   关注   取消关注）
    private void followSet(boolean isFlag) {
        if (isFlag) {
//            Drawable nw_red1 = ContextCompat.getDrawable(mContext,R.mipmap.icon_collect_red);
            Drawable nw_red = getResources().getDrawable(R.mipmap.icon_collect_red);
            nw_red.setBounds(0, 0, nw_red.getMinimumWidth(), nw_red.getMinimumHeight());
            btnFollow.setCompoundDrawables(null, nw_red, null, null);
            btnFollow.setText("已关注");
            btnFollow.setTextColor(getResources().getColor(R.color.wecoo_theme_color));
        } else {
            Drawable nw_gray = getResources().getDrawable(R.mipmap.icon_collect_gray);
            nw_gray.setBounds(0, 0, nw_gray.getMinimumWidth(), nw_gray.getMinimumHeight());
            btnFollow.setCompoundDrawables(null, nw_gray, null, null);
            btnFollow.setText("关注");
            btnFollow.setTextColor(getResources().getColor(R.color.wecoo_gray5));
        }
    }

    // 检查项目是否已经被关注
    private void isFollow() {
        CheckFollowRequest request = new CheckFollowRequest();
        request.setRequestParms(pid);
        request.setReturnDataClick(mContext, 1, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                isFollow = (boolean) obj;
                followSet(isFollow);
            }
        });
    }

    // 获取项目已成单数
//    private void GetSignedData() {
//        signedList = new ArrayList<ProjectSignedEntity>();
//        GetSignedDtoRequest signedDtoRequest = new GetSignedDtoRequest();
//        signedDtoRequest.setRequestParms(pid);
//        signedDtoRequest.setReturnDataClick(mContext, 2, new ReturnDataClick() {
//            @Override
//            public void onReturnData(int what, Object obj) {
//                ArrayList<ProjectSignedEntity> dataList = (ArrayList<ProjectSignedEntity>) obj;
//                if (dataList != null) {
//                    signedList.addAll(dataList);
//                }
//            }
//        });
//    }

    // 添加项目浏览记录
    private void addProjectRecord() {
        AddProjectRecordRequest recordRequest = new AddProjectRecordRequest();
        recordRequest.setRequestParms(pid);
        recordRequest.setReturnDataClick(mContext, -1);
    }

    // 获取详情数据
    private void getDetailData() {
//        showLoadingDialog(mContext, "正在加载...");
        ProjectInfoRequest infoRequest = new ProjectInfoRequest();
        infoRequest.setRequestParms(pid);
        infoRequest.setReturnDataClick(mContext, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                ProjectModels models = (ProjectModels) obj;
                if (models != null) {
                    project_pic_square = models.getProject_pic_square();
                    if (TextUtils.isEmpty(title)) {
                        setBanner(Left, models.getProject_name(), Right);
                    }
                    if (TextUtils.isEmpty(title)) {
                        setBanner(Left, models.getProject_name(), Right);
                    }
                    IndustryCode = models.getProject_industry();
                    txtTitle.setText(models.getProject_slogan());
                    txtDesc.setText(models.getProject_strengths());
                    signed_count = models.getProjectExtDto().getPe_signed_count();
                    txtNum.setText(String.valueOf(signed_count));
                    commission_note = models.getProject_commission_note();
                    if (models.getProject_commission_first() == models.getProject_commission_second()) {
//                        txtYJ.setText("签约佣金");
                        txtPrice.setText(models.getProject_commission_second() + "");
                    } else {
//                        txtYJ.setText("签约佣金");
                        txtPrice.setText(models.getProject_commission_first() + "-"
                                + models.getProject_commission_second());
                    }

                    // 设置数据
                    String introduceUrl = models.getProjectDescDto().getProject_desc_url() + pid;
                    String descriptionUrl = models.getProjectDescDto().getProject_policy_url() + pid;
                    ModelManager.getInstance().setIntroduceUrl(introduceUrl);
                    ModelManager.getInstance().setDescriptionUrl(descriptionUrl);
                    ModelManager.getInstance().setJingNs(models.getJingNs());
                    addSilkDate();

                    // 设置广播的数据
                    if (models.getBroadMsgs() != null && models.getBroadMsgs().size() > 0) {
                        addMsgAnimation(models.getBroadMsgs());
                    }

                    HandlerManager.getHandlerDelayed().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            topView.setVisibility(View.GONE);
                        }
                    }, 500);
                }
            }
        });
    }

    // 添加详情    介绍    锦囊 的数据
    private void addSilkDate() {
        List<Fragment> list = new ArrayList<Fragment>();
        Fragment introduceFragment = new IntroduceWvFragment();
        Fragment descripFragment = new DescripWvFragment();
        Fragment listFragment = new ListViewFragment();
        list.add(introduceFragment);
        list.add(descripFragment);
        list.add(listFragment);

        PagerManager factory = new PagerManager();
        for (int i = 0; i < getTabTitles().size(); i++) {
            factory.addFragment(list.get(i), getTabTitles().get(i));
        }
        adapter = new ModelPagerAdapter(getSupportFragmentManager(), factory);
        viewPager.setAdapter(adapter);
        pagerSlidingTabStrip.setViewPager(viewPager);
    }

    // String  data
    private List<String> getTabTitles() {
        List<String> stringList = new ArrayList<>();
        stringList.add("项目介绍");
        stringList.add("招商详情");
        stringList.add("锦囊");
        return stringList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.projectInfo_ll_Buttom:
                if (View.VISIBLE == llButtom.getVisibility()) {
                    llButtom.setVisibility(View.GONE);
                    llButtom.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.out_to_buttom));
                }
                break;
            case R.id.projectInfo_ll_num:
                MobclickAgent.onEvent(mContext, "projectInfoSigned");
                Intent intent = new Intent(mContext, SignedCustomerActivity.class);
                intent.putExtra("pid", pid);
                startActivity(intent);
//                DialogFucengView fucengView1 = new DialogFucengView(mContext);
//                fucengView1.setTitleData("成交名单");
//                fucengView1.setData(signedList);
//                fucengView1.show();
                break;
            case R.id.projectInfo_ll_price:
                MobclickAgent.onEvent(mContext, "projectInfoPriced");
                if (!TextUtils.isEmpty(commission_note)) {
                    DialogFucengView fucengView = new DialogFucengView(mContext);
                    fucengView.setTitleData("赏金说明");
                    fucengView.setDescData(commission_note);
                    fucengView.show();
                }
                break;
            case R.id.projectInfo_btn_Follow:
                MobclickAgent.onEvent(mContext, "projectInfo_btn_Follow");
                if (isFollow) {
                    cancelFollowRequest();
                } else {
                    addFollowRequest();
                }
                break;
            case R.id.projectInfo_txt_reportCustom:
                if (View.VISIBLE == llButtom.getVisibility()) {
                    llButtom.setVisibility(View.GONE);
                    llButtom.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.out_to_buttom));
                }
            case R.id.projectInfo_btn_reportCustom:
                MobclickAgent.onEvent(mContext, "projectInforeportCustom");
                // 当前业务员是否允许报备
                IsReportAllowedRequest allowedRequest = new IsReportAllowedRequest();
                allowedRequest.setReturnDataClick(mContext, 4, new ReturnDataClick() {
                    @Override
                    public void onReturnData(int what, Object obj) {
                        Intent intent = new Intent(mContext, AddReportActivity.class);
                        intent.putExtra(AddReportActivity.TITLEBAR, getString(R.string.Recommend_custom));
                        intent.putExtra(AddReportActivity.p_ID, pid);
                        intent.putExtra(AddReportActivity.CODE, IndustryCode);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.projectInfo_img_broadcast_close:
                MobclickAgent.onEvent(mContext, "projectInforeportClosebroadcast");
                stopEffect();
                viewFengexian.setVisibility(View.VISIBLE);
                broadcastView.setVisibility(View.GONE);
                break;
        }
    }

    // 添加关注
    private void addFollowRequest() {
        AddFollowRequest request = new AddFollowRequest();
        request.setRequestParms(pid);
        request.setReturnDataClick(mContext, 2, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                String prc_id = (String) obj;
                if (!TextUtils.isEmpty(prc_id)) {
                    ToastUtil.showShort(mContext, "关注成功");
                    isFollow = true;
                    followSet(isFollow);
                }
            }
        });
    }

    // 取消关注
    private void cancelFollowRequest() {
        CancelFollowRequest request = new CancelFollowRequest(WebUrl.cancelProjectCollection);
        request.setRequestParms(pid);
        request.setReturnDataClick(mContext, 3, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                boolean cancelFollow = (boolean) obj;
                if (cancelFollow) {
                    ToastUtil.showShort(mContext, "已取消关注");
                    isFollow = false;
                    followSet(isFollow);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        CallServer.getInstance().cancelBySign(mContext);
    }

    // 分享
    @Override
    public void onRightCallback(View view) {
        MobclickAgent.onEvent(mContext, "ProjectInfoShare");
        String shareTitle = txtTitle.getText().toString().trim();
        ShareWindow shareWindow = new ShareWindow(ProjectInfoActivity.this);
        shareWindow.setView(false);
        if (TextUtils.isEmpty(project_pic_square)) {
            shareWindow.setShareData(R.mipmap.share_project_icon, shareTitle,
                    Defaultcontent.shareProjectdtext, Defaultcontent.shareProjecturl + pid);
        } else {
            shareWindow.setShareData(project_pic_square, shareTitle,
                    Defaultcontent.shareProjectdtext, Defaultcontent.shareProjecturl + pid);
        }
        shareWindow.show();
    }

    private void addMsgAnimation(List<StringModels> msgList) {
        broadcastView.setVisibility(View.VISIBLE);
        viewFengexian.setVisibility(View.GONE);
        // 加载进入动画
        anim_in = AnimationUtils.loadAnimation(this, R.anim.in_from_buttom);
        // 加载移除动画
        anim_out = AnimationUtils.loadAnimation(this, R.anim.out_to_top);
        // 根据list的大小，动态创建同样个数的TextView
        for (int i = 0; i < msgList.size(); i++) {
            TextView tvTemp = new TextView(this);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            tvTemp.setLayoutParams(lp);
            tvTemp.setGravity(Gravity.CENTER_VERTICAL);
            tvTemp.setMaxLines(1);
            tvTemp.setTextColor(Color.WHITE);
            tvTemp.setText(msgList.get(i).getMsg());
            tvTemp.setId(i + 1000);
            MsgsLayout.addView(tvTemp);
        }
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        // 移除
                        TextView tvTemp = (TextView) msg.obj;
                        LogUtil.d("out->" + tvTemp.getId());
                        tvTemp.startAnimation(anim_out);
                        tvTemp.setVisibility(View.GONE);
                        break;
                    case 1:
                        // 进入
                        TextView tvTemp2 = (TextView) msg.obj;
                        LogUtil.d("in->" + tvTemp2.getId());
                        tvTemp2.startAnimation(anim_in);
                        tvTemp2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
        // 当消息数据不为空，开始轮换
        startEffect();
    }

    /***
     * 停止动画
     */
    private void stopEffect() {
        runFlag = false;
    }

    /***
     * 启动动画
     */
    private void startEffect() {
        runFlag = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (runFlag) {
                    try {
                        // 每隔2秒轮换一次
                        Thread.sleep(2000);
                        // 至于这里还有一个if(runFlag)判断是为什么？大家自己试验下就知道了
                        if (runFlag) {
                            // 获取第index个TextView开始移除动画
                            TextView tvTemp = (TextView) MsgsLayout.getChildAt(index);
                            mHandler.obtainMessage(0, tvTemp).sendToTarget();
                            if (index < MsgsLayout.getChildCount()) {
                                index++;
                                if (index == MsgsLayout.getChildCount()) {
                                    index = 0;
                                }
                                // index+1个动画开始进入动画
                                tvTemp = (TextView) MsgsLayout
                                        .getChildAt(index);
                                mHandler.obtainMessage(1, tvTemp)
                                        .sendToTarget();
                            }
                        }
                    } catch (InterruptedException e) {
                        // 如果有异常，那么停止轮换。当然这种情况很难发生
                        runFlag = false;
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null) {
//            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
//        }
    }

    // 当页面暂停，那么停止轮换 
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
        closeLoadingDialog();
        stopEffect();
    }

    // 用于保存当前index的,结合onCreate方法 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currIndex", index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
