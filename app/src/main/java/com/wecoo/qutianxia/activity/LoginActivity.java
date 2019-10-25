package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.BaseActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.LoginEntity;
import com.wecoo.qutianxia.requestjson.LoginRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.VerCodeRequest;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.StringUtil;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.SendCodeButn;

/**
 * Created by mwl on 2016/10/25.
 * 登陆
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, ReturnDataClick, TextWatcher {

    private final String mPageName = "LoginActivity";
    private Context mContext = LoginActivity.this;
    private Button btnLogin, btnSendCode;
    // 手机号   验证码    邀请码
    private EditText editPhone, editCode, editYQcode;
    private String mobiePhone, verification_code, invitation_code;
    // 发送验证码
    private SendCodeButn btnTimer;
    // 请求验证码码的状态    登录的状态
    private final int  loginWhat = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        AppManager.getAppManager().addActivity(this);

        initView();
    }

    private void initView() {
        btnLogin = (Button) findViewById(R.id.login_btn_login);
        btnSendCode = (Button) findViewById(R.id.login_btn_sendCode);
        editPhone = (EditText) findViewById(R.id.login_edit_mobieNum);
        editCode = (EditText) findViewById(R.id.login_edit_code);
        editYQcode = (EditText) findViewById(R.id.login_edit_yqcode);

        btnTimer = new SendCodeButn(btnSendCode);// 发送验证码的按钮
        btnLogin.setBackgroundResource(R.mipmap.btn_login_gray);
        btnLogin.setEnabled(false);

        editPhone.addTextChangedListener(this);
        editCode.addTextChangedListener(this);
        btnLogin.setOnClickListener(this);
        btnSendCode.setOnClickListener(this);
        findViewById(R.id.login_txt_privacy).setOnClickListener(this);
    }

    // view 转化为String
    private void viewToString() {
        mobiePhone = editPhone.getText().toString().trim();
        verification_code = editCode.getText().toString().trim();
        invitation_code = editYQcode.getText().toString().trim();
    }

    // 创建登录请求
    private void requestLogin() {
        LoginRequest loginRequest = new LoginRequest();
        LoginRequest.LoginParms parms = new LoginRequest.LoginParms();
        parms.user_login = mobiePhone;
        parms.verification_code = verification_code;
        parms._invitation_code = invitation_code;
        loginRequest.setRequestParms(parms);
        loginRequest.setReturnDataClick(mContext, loginWhat, this);

    }

    // 创建获取验证码请求
    private void requestGetCode() {
        VerCodeRequest requestJson = new VerCodeRequest();
        requestJson.setRequestParms(mobiePhone);
        requestJson.setReturnDataClick(mContext, 0);
    }

    @Override
    public void onClick(View view) {
        viewToString();
        switch (view.getId()) {
            case R.id.login_txt_privacy:
                Intent intent = new Intent(mContext, PubWebViewActivity.class);
                intent.putExtra(PubWebViewActivity.WebUrl, WebUrl.disclaimer);
                startActivity(intent);
                break;
            case R.id.login_btn_login:
                SPUtils.put(mContext, Configs.IsUpdateUserInfo, true); // 设置需要刷新用户信息
                if (NetWorkState.isNetworkAvailable(mContext)) { // 判断网络
                    if (TextUtils.isEmpty(mobiePhone) || !StringUtil.isMobile(mobiePhone)) {
                        ToastUtil.showShort(mContext, "请输入正确的手机号");
                        return;
                    } else if (TextUtils.isEmpty(verification_code)) {
                        ToastUtil.showShort(mContext, "请输入验证码");
                        return;
                    }
                    requestLogin();
                } else {
                    ToastUtil.showShort(mContext, getString(R.string.load_data_nonetwork));
                }
                break;
            case R.id.login_btn_sendCode:
                if (NetWorkState.isNetworkAvailable(mContext)) { // 判断网络
                    if (TextUtils.isEmpty(mobiePhone) || !StringUtil.isMobile(mobiePhone)) {
                        ToastUtil.showShort(mContext, "请输入正确的手机号");
                        return;
                    }
                    requestGetCode();
                    btnTimer.start();
                } else {
                    ToastUtil.showShort(mContext, getString(R.string.load_data_nonetwork));
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().AppExit(LoginActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        editPhone.setText("");
//        editCode.setText("");
//        editYQcode.setText("");
        CallServer.getInstance().cancelBySign(mContext);
    }

    @Override
    public void onReturnData(int what, Object obj) {
        switch (what) {
            case loginWhat:
                LoginEntity entity = (LoginEntity) obj;
                if (entity != null) {
                    SPUtils.put(mContext, Configs.qtx_auth, entity.getQtx_auth());
                    SPUtils.put(mContext, Configs.user_id, entity.getUser_id());
                    SPUtils.put(mContext, Configs.CurrentTab, 0);
                    openActivity(mContext, MainActivity.class);
                    finish();
                }
                break;
        }
    }

    // 以下是 EditText 变化监听
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editPhone.getText().toString().length() > 10 && editCode.getText().toString().length() > 3) {
            btnLogin.setEnabled(true);
            btnLogin.setBackgroundResource(R.mipmap.btn_login_red);
        } else {
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundResource(R.mipmap.btn_login_gray);
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
