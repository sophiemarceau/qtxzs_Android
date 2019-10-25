package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.base.TitleBarActivity.RightCallbackListener;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.CustomInfoEntity;
import com.wecoo.qutianxia.requestjson.GetReportLockTimeRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.view.AddReportView;
import com.wecoo.qutianxia.view.AddReportView.OnAddReportListener;

/**
 * Created by mwl on 2016/10/25.
 * 添加（直接）报备
 */

public class AddReportActivity extends TitleBarActivity implements OnAddReportListener, RightCallbackListener {

    private final String mPageName = "AddReportActivity";
    private Context mContext = AddReportActivity.this;
    private LinearLayout linearLayout;
    private AddReportView addReportView;
    public static String TITLEBAR = "titlebar";
    public static String p_ID = "project_id";
    public static String CODE = "IndustryCode";
    public static String CUSTOMINFO = "customInfo";
    private int reportLockTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addreport_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);

        initView(getIntent().getStringExtra(TITLEBAR));
        getReportLockTime();
    }

    // 初始化View
    private void initView(String title) {
        linearLayout = (LinearLayout) findViewById(R.id.addReport_layout_info);
        addReportView = new AddReportView(mContext);
        addReportView.setData(application.IndustryList, application.BudgetList, application.PlanTimeList);
        setBanner(Left, title, getString(R.string.select_customer));
        setRightCallbackListener(this);
        if (getString(R.string.direct_reporting).equals(title)) {
            setBanner(Left, title, None);
            addReportView.setInputStatus(addReportView.PROJECTREPORT);
        } else if (getString(R.string.Recommend_custom).equals(title)) {
            addReportView.setInputStatus(addReportView.REPORTCUSTOM);
            addReportView.setProjectData(getIntent().getStringExtra(p_ID), getIntent().getStringExtra(CODE));
        }
        linearLayout.addView(addReportView);
        addReportView.setAddCustomListener(this);
    }

    // 获取报备锁定天数
    private void getReportLockTime() {
        GetReportLockTimeRequest lockTimeRequest = new GetReportLockTimeRequest();
        lockTimeRequest.setReturnDataClick(mContext, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                if (obj != null) {
                    reportLockTime = (int) obj;
                }
            }
        });
    }

    @Override
    public void addReportStatus(Object obj) {
        String result = (String) obj;
        if (!TextUtils.isEmpty(result)) {
            Intent intent = new Intent(mContext, CommitSuccessActivity.class);
            intent.putExtra(CommitSuccessActivity.TYPE, Constants.ADDREPORT);
            intent.putExtra(CommitSuccessActivity.DAYCOUNT, reportLockTime);
            intent.putExtra(CommitSuccessActivity.TITLE, getString(R.string.Recommend_custom));
            startActivity(intent);
            finish();
        }
    }

    // 选择客户
    @Override
    public void onRightCallback(View view) {
        MobclickAgent.onEvent(mContext, "selectMyCustom");
        Intent intent = new Intent(mContext, MyCustomActivity.class);
        intent.putExtra(TITLEBAR, TITLEBAR);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();//data为B中回传的Intent
            CustomInfoEntity infoEntity = (CustomInfoEntity) bundle.getSerializable(CUSTOMINFO);
            addReportView.setCustomName(infoEntity);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallServer.getInstance().cancelBySign(mContext);
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
