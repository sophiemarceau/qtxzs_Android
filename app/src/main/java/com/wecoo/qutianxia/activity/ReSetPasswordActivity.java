package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.listener.NoDoubleClickListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.BankInfoEntity;
import com.wecoo.qutianxia.models.UserInfoEntity;
import com.wecoo.qutianxia.requestjson.GetUserInfoRequest;
import com.wecoo.qutianxia.requestjson.ReSetPwdRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.VerCodeByUseridRequest;
import com.wecoo.qutianxia.utils.MD5Util;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.SendCodeButn;

/**
 * Created by mwl on 2017/03/30.
 * 取现重置密码
 */

public class ReSetPasswordActivity extends TitleBarActivity{

    private final String mPageName = "ReSetPasswordActivity";
    private Context mContext;
    // 真实姓名    身份证号    密码    重复密码    验证码
    private EditText editName,editIDNum,editPass,editResetPass,editCode;
    // 电话
    private TextView txtTel;
    private Button btnSendCode,btnCommit;
    // 发送验证码
    private SendCodeButn btnTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpass_layout);
        AppManager.getAppManager().addActivity(this);

        mContext = ReSetPasswordActivity.this;

        initActionBar(this);
        setBanner(Left, getString(R.string.reSetPassword), None);
        SPUtils.put(mContext, Configs.isIntentMyFrgment,false);

        initView();
    }

    private void initView() {
        editName = (EditText) findViewById(R.id.reSetPass_edit_name);
        editIDNum = (EditText) findViewById(R.id.reSetPass_edit_idNumber);
        editPass = (EditText) findViewById(R.id.reSetPass_edit_pass);
        editResetPass = (EditText) findViewById(R.id.reSetPass_edit_resetPass);
        editCode = (EditText) findViewById(R.id.reSetPass_edit_code);
        txtTel = (TextView) findViewById(R.id.reSetPass_txt_tel);
        btnSendCode = (Button) findViewById(R.id.reSetPass_btn_sendCode);
        btnCommit = (Button) findViewById(R.id.reSetPass_btn_commit);

        //发送验证码的按钮
        btnTimer = new SendCodeButn(btnSendCode);
        // 添加监听
        btnSendCode.setOnClickListener(clickListener);
        btnCommit.setOnClickListener(clickListener);

        //
        getUserInfo();
    }

    // 获取用户实名信息
    private void getUserInfo() {
        GetUserInfoRequest infoRequest = new GetUserInfoRequest();
        infoRequest.setReturnDataClick(mContext, 1, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                UserInfoEntity entity = (UserInfoEntity) obj;
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getUs_realname())) {
                        editName.setText(entity.getUs_realname());
                        editName.setEnabled(false);
                    }
                    txtTel.setText(entity.getUs_tel());
                }
            }
        });
    }

    private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()){
                case R.id.reSetPass_btn_sendCode:
                    sendCode();
                    btnTimer.start();
                    break;
                case R.id.reSetPass_btn_commit:
                    commitPass();
                    break;
            }
        }
    };

    // 发送验证码
    private void sendCode() {
        VerCodeByUseridRequest request = new VerCodeByUseridRequest();
        request.setReturnDataClick(mContext, 0);
    }

    private void commitPass(){
        String strIdNum = editIDNum.getText().toString().trim();
        String strPass = editPass.getText().toString().trim();
        String strResetPass = editResetPass.getText().toString().trim();
        String strCode = editCode.getText().toString().trim();
        if (TextUtils.isEmpty(strIdNum)){
            ToastUtil.showShort(mContext,"请输入身份证号");
            return;
        }
        if (TextUtils.isEmpty(strPass) || strPass.length() < 6){
            ToastUtil.showShort(mContext,"请输入6位数字");
            return;
        }
        if (TextUtils.isEmpty(strResetPass)){
            ToastUtil.showShort(mContext,"请重复输入密码");
            return;
        }
        if (TextUtils.isEmpty(strCode)){
            ToastUtil.showShort(mContext,"请输入验证码");
            return;
        }
        if (!strPass.equals(strResetPass)){
            ToastUtil.showShort(mContext,"两次输入的支付密码必须相同");
            return;
        }

        ReSetPwdRequest reSetPwdRequest = new ReSetPwdRequest();
        reSetPwdRequest.setRequestParms(strIdNum,strPass,strResetPass,strCode);
        reSetPwdRequest.setReturnDataClick(mContext, 2, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                if (obj == null) return;
                boolean flag = (boolean) obj;
                if (flag){
                    SPUtils.put(mContext, Configs.isIntentMyFrgment,true);
                    Intent intent = new Intent(mContext,CommitSuccessActivity.class);
                    intent.putExtra(CommitSuccessActivity.TYPE, Constants.RESETPWD);
                    intent.putExtra(CommitSuccessActivity.TITLE,"重置成功");
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
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
}
