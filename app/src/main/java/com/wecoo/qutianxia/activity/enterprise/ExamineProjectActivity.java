package com.wecoo.qutianxia.activity.enterprise;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.CommitSuccessActivity;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.requestjson.AddFollowUpRecordRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.utils.ToastUtil;

/**
 * Created by wecoo on 2017/6/2.
 * （      审核通过         退回    ）
 */

public class ExamineProjectActivity extends TitleBarActivity implements View.OnClickListener {

    private final String mPageName = "ExamineProjectActivity";
    private String viewType = "", report_id = "";
    // view  控制显示
    private View viewtopView, viewAdoptAndReturn;
    private RadioButton rbOne, rbTwo, rbThree;
    private Button btnCommit;
    private String crl_note = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine_project_layout);
        AppManager.getAppManager().addActivity(this);

        viewType = getIntent().getStringExtra("viewType");
        report_id = getIntent().getStringExtra("report_id");

        initActionBar(this);
        if (viewType.equals("退回")) {
            setBanner(Left, "审核退回", None);
        } else {
            setBanner(Left, "审核通过", None);
        }

        initView();
    }

    // 初始化View
    private void initView() {
        viewtopView = findViewById(R.id.examine_fl_topView);
        viewAdoptAndReturn = findViewById(R.id.examine_ll_AdoptAndReturn);
        rbOne = (RadioButton) findViewById(R.id.examine_rb_one);
        rbTwo = (RadioButton) findViewById(R.id.examine_rb_two);
        rbThree = (RadioButton) findViewById(R.id.examine_rb_three);
        btnCommit = (Button) findViewById(R.id.examine_btn_commit);
        btnCommit.setText("确认通过");

        setView();
        setListener();
    }

    // view 的设置
    private void setView() {
        if (viewType.equals("退回")) {
            rbOne.setText("很遗憾，该用户在后续跟进中，对我品牌已无加盟意向或双方需求不匹配。");
            rbTwo.setText("多时段，多通电话尝试，仍未能联系上该客户。");
            rbThree.setVisibility(View.GONE);
            btnCommit.setText("确认退回");
        }
        viewtopView.setVisibility(View.VISIBLE);
        viewAdoptAndReturn.setVisibility(View.VISIBLE);
    }

    //
    private void setListener() {
        rbOne.setOnClickListener(this);
        rbTwo.setOnClickListener(this);
        rbThree.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.examine_rb_one:
                crl_note = rbOne.getText().toString();
                break;
            case R.id.examine_rb_two:
                crl_note = rbTwo.getText().toString();
                break;
            case R.id.examine_rb_three:
                crl_note = rbThree.getText().toString();
                break;
            case R.id.examine_btn_commit:
                commitData();
                break;
        }
    }

    private void commitData() {
        AddFollowUpRecordRequest recordRequest;
        if (viewType.equals("退回")) {
            if (TextUtils.isEmpty(crl_note)) {
                ToastUtil.showShort(this, "请选择退回原因");
                return;
            }
            recordRequest = new AddFollowUpRecordRequest(WebUrl.sendBack4App);
        } else {
            if (TextUtils.isEmpty(crl_note)) {
                ToastUtil.showShort(this, "请选择审核通过原因");
                return;
            }
            recordRequest = new AddFollowUpRecordRequest(WebUrl.passAuditing4App);

        }
        recordRequest.setRequestParms(report_id, crl_note);
        recordRequest.setReturnDataClick(ExamineProjectActivity.this, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                if (!viewType.equals("退回")) {
                    Intent intent = new Intent(ExamineProjectActivity.this, CommitSuccessActivity.class);
                    intent.putExtra(CommitSuccessActivity.TYPE, Constants.ExamineProjectSuccess);
                    intent.putExtra(CommitSuccessActivity.TITLE, "审核通过");
                    startActivity(intent);
                }
                finish();
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
}
