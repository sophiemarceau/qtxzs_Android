package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.ReportInformationAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.CustomerFollowUpEntity;
import com.wecoo.qutianxia.models.CustomerFollowUpModel;
import com.wecoo.qutianxia.models.ReportInfoModel;
import com.wecoo.qutianxia.requestjson.CustomerFollowUpRequest;
import com.wecoo.qutianxia.requestjson.GetReportInfoRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.refreshload.PtrWecooFrameLayout;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by mwl on 2016/10/25.
 * 推荐客户信息
 */

public class ReportInfoActivity extends TitleBarActivity implements View.OnClickListener,
        OnReLoadClickListener {

    private final String mPageName = "ReportInfoActivity";
    private Context mContext;
    // Tab   基本信息    跟进信息
    private View viewReportInfo, viewFollowUp;
    private RadioButton rb_basic, rb_information;
    // 客户名字   电话号码  备注
    private EditText editName, editPhone, editRemarks;
    // 意向行业    地域选择   投资预算    计划时间
    private TextView txtIndustry, txtRegion, txtBudget, txtPlanTime;
    // 说明
    private TextView txtContentTop, txtContentButtom;
    private LinearLayout llCommit;
    private String report_id;
    private int butnStatus;// 按钮状态   1，基本信息   2，跟进信息
    // 列表
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private LoadDataErrorWidget errorWidget;
    private List<CustomerFollowUpModel> FollowUpDataList;
    private ReportInformationAdapter adapter;
    // 客户基本信息点击状态       跟进信息点击状态
    private boolean customerInfoOnclickStatus = true, followUpOnclickStatus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportinfo_layout);
        AppManager.getAppManager().addActivity(this);
        mContext = ReportInfoActivity.this;

        initActionBar(this);
        setBanner(Left, getString(R.string.reportinfo), None);

        report_id = getIntent().getStringExtra("report_id");
        butnStatus = getIntent().getIntExtra("butnStatus", 1);

        initList();
        initView();
    }

    // 初始化数据列表
    private void initList() {
        FollowUpDataList = new ArrayList<>();
    }

    private void initView() {
        // 获取控件
        rb_basic = (RadioButton) findViewById(R.id.reportInfo_rb_basic);
        rb_information = (RadioButton) findViewById(R.id.reportInfo_rb_information);
        viewReportInfo = findViewById(R.id.reportInfo_view);
        viewFollowUp = findViewById(R.id.reportInfo_FollowUpView);
        if (viewReportInfo != null) {
            editName = (EditText) viewReportInfo.findViewById(R.id.inputCustom_edit_name);
            editPhone = (EditText) viewReportInfo.findViewById(R.id.inputCustom_edit_tel);
            txtIndustry = (TextView) viewReportInfo.findViewById(R.id.inputCustom_txt_industry);
            txtRegion = (TextView) viewReportInfo.findViewById(R.id.inputCustom_txt_region);
            txtBudget = (TextView) viewReportInfo.findViewById(R.id.inputCustom_txt_budget);
            txtPlanTime = (TextView) viewReportInfo.findViewById(R.id.inputCustom_txt_planTime);
            editRemarks = (EditText) viewReportInfo.findViewById(R.id.inputCustom_edit_remarks);
            txtContentTop = (TextView) viewReportInfo.findViewById(R.id.inputCustom_txt_contentsTop);
            txtContentButtom = (TextView) viewReportInfo.findViewById(R.id.inputCustom_txt_contentsButtom);
            llCommit = (LinearLayout) viewReportInfo.findViewById(R.id.input_ll_saveandCommit);
            LinearLayout llInfo = (LinearLayout) viewReportInfo.findViewById(R.id.input_ll_commitInfo);
            llInfo.setVisibility(View.GONE);
        }

        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.FollowUp_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.FollowUp_listView);
        errorWidget = (LoadDataErrorWidget) findViewById(R.id.FollowUp_loaddataView);

        // setAdapter
        adapter = new ReportInformationAdapter(mContext, FollowUpDataList);
        mListView.setAdapter(adapter);
        // 设置控件  
        txtRegion.setCompoundDrawables(null, null, null, null);
        editName.setEnabled(false);
        editPhone.setEnabled(false);
        editRemarks.setEnabled(false);
        txtContentTop.setVisibility(View.GONE);
        txtContentButtom.setVisibility(View.GONE);
        llCommit.setVisibility(View.GONE);

        setListener();
        changeButnStatus();
    }

    // 按钮的状态
    private void changeButnStatus() {
        if (butnStatus == 2) {
            rb_information.setChecked(true);
            viewReportInfo.setVisibility(View.GONE);
            viewFollowUp.setVisibility(View.VISIBLE);
            initListData(true);
        } else {
            rb_basic.setChecked(true);
            viewReportInfo.setVisibility(View.VISIBLE);
            viewFollowUp.setVisibility(View.GONE);
            initData();
        }
    }

    // setListener
    private void setListener(){
        rb_basic.setOnClickListener(this);
        rb_information.setOnClickListener(this);
        errorWidget.setOnReLoadClickListener(this);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (!NetWorkState.isNetworkAvailable(mContext)) {
                    mPtrFrame.refreshComplete();
                    ToastUtil.showShort(mContext, getString(R.string.load_data_nonetwork));
                    return;
                }
                initList();
                initListData(false);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reportInfo_rb_basic:
                viewReportInfo.setVisibility(View.VISIBLE);
                viewFollowUp.setVisibility(View.GONE);
                if (customerInfoOnclickStatus) {
                    initData();
                }
                break;
            case R.id.reportInfo_rb_information:
                viewReportInfo.setVisibility(View.GONE);
                viewFollowUp.setVisibility(View.VISIBLE);
                if (followUpOnclickStatus) {
                    initListData(true);
                }
                break;
        }
    }

    private void initData() {
        if (!TextUtils.isEmpty(report_id)) {
            GetReportInfoRequest infoRequest = new GetReportInfoRequest();
            infoRequest.setRequestParms(report_id);
            infoRequest.setReturnDataClick(this, 0, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    ReportInfoModel infoModel = (ReportInfoModel) obj;
                    if (infoModel != null) {
                        customerInfoOnclickStatus = false;
                        editName.setText(infoModel.getReport_customer_name());
                        editPhone.setText(infoModel.getReport_customer_tel());
                        txtIndustry.setText(infoModel.getReport_customer_industry_name());
                        txtRegion.setText(infoModel.getReport_customer_area_agent_name());
                        txtBudget.setText(infoModel.getReport_customer_budget_name());
                        txtPlanTime.setText(infoModel.getReport_customer_startdate_name());
                        editRemarks.setText(infoModel.getReport_customer_note());
                    }
                }
            });
        }
    }

    private void initListData(boolean isshowLoad) {
        if (!NetWorkState.isNetworkAvailable(mContext)) {
            mPtrFrame.setVisibility(View.GONE);
            errorWidget.setVisibility(View.VISIBLE);
            errorWidget.netWorkError();
            return;
        }
        CustomerFollowUpRequest followUpRequest = new CustomerFollowUpRequest();
        followUpRequest.setRequestParms(report_id);
        followUpRequest.setReturnDataClick(mContext, isshowLoad, 1, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                CustomerFollowUpEntity entity = (CustomerFollowUpEntity) obj;
                if (entity != null) {
                    followUpOnclickStatus = false;
                    FollowUpDataList = new ArrayList<>();
                    CustomerFollowUpModel model = entity.getPlatformFeedbackLogDto();
                    // 两种类型的数据
                    if (model != null && !TextUtils.isEmpty(model.getCrl_createdtime())) {
                        model.setViewType(1);
                        FollowUpDataList.add(entity.getPlatformFeedbackLogDto());
                    }
                    FollowUpDataList.addAll(entity.getEnterpriseFollowUpDtos());
                    if (FollowUpDataList.size() > 0) {
                        mPtrFrame.setVisibility(View.VISIBLE);
                        errorWidget.setVisibility(View.GONE);

                        adapter.setData(FollowUpDataList);
                    } else {
                        mPtrFrame.setVisibility(View.GONE);
                        errorWidget.setVisibility(View.VISIBLE);
                        errorWidget.dataLoadError();
                    }
                    mPtrFrame.refreshComplete();
                }
            }
        });
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
        CallServer.getInstance().cancelBySign(this);
    }

    @Override
    public void OnReLoadData() {
        initListData(true);
    }
}
