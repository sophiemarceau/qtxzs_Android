package com.wecoo.qutianxia.activity.enterprise;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.CommitSuccessActivity;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.NoDoubleClickListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.requestjson.AddFollowUpRecordRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.wheelcity.ChooseCityInterface;
import com.wecoo.qutianxia.view.wheelcity.SelectAdressUtil;

/**
 * Created by wecoo on 2017/6/2.
 * 签约打款
 */

public class SignUpforMoneyActivity extends TitleBarActivity {

    private final String mPageName = "SignUpforMoneyActivity";
    private EditText editRemark;
    private TextView txtAdress;
    private String strAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine_project_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, "签约打款", None);

        initView();
    }

    private void initView() {
        View viewSignUpForMoney = findViewById(R.id.examine_fl_topView);
        View viewTopView = findViewById(R.id.examine_ll_SignUpForMoney);
        View viewAdress = findViewById(R.id.SignUp_ll_viewAdress);
        editRemark = (EditText) findViewById(R.id.examine_edit_addRemark);
        txtAdress = (TextView) findViewById(R.id.SignUp_txt_Adress);
        Button btnCommit = (Button) findViewById(R.id.examine_btn_commit);
        TextView txtProject_commission = (TextView) findViewById(R.id.SignUpforMoney_txt_project_commission);
        Integer project_commission = (Integer) SPUtils.get(SignUpforMoneyActivity.this, Configs.project_commission,0);
        txtProject_commission.setText("¥" + project_commission);
        //
        viewSignUpForMoney.setVisibility(View.VISIBLE);
        viewTopView.setVisibility(View.VISIBLE);
        viewAdress.setVisibility(View.VISIBLE);
        editRemark.setVisibility(View.VISIBLE);
        txtAdress.setOnClickListener(clickListener);
        btnCommit.setOnClickListener(clickListener);
    }

    // 提交
    private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.SignUp_txt_Adress:
                    selectAdress();
                    break;
                case R.id.examine_btn_commit:
                    commitData();
                    break;
            }
        }
    };

    // 选择企业地区
    private void selectAdress() {
        SelectAdressUtil cityUtil = new SelectAdressUtil();
        cityUtil.createDialog(this, strAdress, new ChooseCityInterface() {
            @Override
            public void sure(String[] newCityArray) {
                txtAdress.setText(newCityArray[0] + " " + newCityArray[1] + " " + newCityArray[2]);
                strAdress = newCityArray[3];
            }
        });
    }

    // 提交数据
    private void commitData() {
        String report_id = getIntent().getStringExtra("report_id");
        String strRemark = editRemark.getText().toString();
        if (TextUtils.isEmpty(strAdress)) {
            ToastUtil.showShort(this, "请选择签约地区");
        } else if (TextUtils.isEmpty(strRemark)) {
            ToastUtil.showShort(this, "请填写备注");
        } else {
            AddFollowUpRecordRequest recordRequest = new AddFollowUpRecordRequest(WebUrl.applySignedUpAuditing4App);
            recordRequest.setRequestParms(report_id,strAdress,strRemark);
            recordRequest.setReturnDataClick(SignUpforMoneyActivity.this, 0, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    Intent intent = new Intent(SignUpforMoneyActivity.this, CommitSuccessActivity.class);
                    intent.putExtra(CommitSuccessActivity.TYPE, Constants.SignUpforMoney);
                    intent.putExtra(CommitSuccessActivity.TITLE, "签约打款");
                    startActivity(intent);
                    finish();
                }
            });
        }
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
