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
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.SetUpWithdrawPwdRequest;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.LogUtil;
import com.wecoo.qutianxia.utils.MD5Util;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.ToastUtil;

/**
 * Created by mwl on 2017/03/30.
 * 取现设置密码
 */

public class SetPasswordActivity extends TitleBarActivity implements ReturnDataClick {

    private final String mPageName = "SetPasswordActivity";
    private Context mContext;
    private EditText editOldPass, editNewpass, editResetpass;
    private String strOldPass, strNewpass, strResetpass;
    private Button btnCommit;
    private String ActivityStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpassword_layout);
        AppManager.getAppManager().addActivity(this);

        mContext = SetPasswordActivity.this;

        // 页面类型，用来判断是设置密码还是修改密码
        ActivityStatus = getIntent().getStringExtra("ActivityStatus");

        initActionBar(this);

        initView();
    }

    private void initView() {
        editOldPass = (EditText) findViewById(R.id.setPass_edit_oldPass);
        editNewpass = (EditText) findViewById(R.id.setPass_edit_newpass);
        editResetpass = (EditText) findViewById(R.id.setPass_edit_resetpass);
        btnCommit = (Button) findViewById(R.id.setPass_btn_commit);
        TextView txt_newpass = (TextView)findViewById(R.id.setPass_txt_newpass);
        TextView txt_resetpass = (TextView)findViewById(R.id.setPass_txt_resetpass);

        // set Title
        if (TextUtils.isEmpty(ActivityStatus)) {
            setBanner(Left, getString(R.string.setPassword), None);
        } else {
            setBanner(Left, getString(R.string.updatePassword), None);
            SPUtils.put(mContext, Configs.isIntentMyFrgment, false);
            findViewById(R.id.setPass_llview_oldPass).setVisibility(View.VISIBLE);
            txt_newpass.setText("设置新密码");
            txt_resetpass.setText("重复新密码");
        }

        // setListener
        btnCommit.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (TextUtils.isEmpty(ActivityStatus)) {
                    setWithdrawPass();
                } else {
                    setWithdrawPass();
                }
            }
        });
    }

    private void setWithdrawPass() {
        strOldPass = editOldPass.getText().toString().trim();
        strNewpass = editNewpass.getText().toString().trim();
        strResetpass = editResetpass.getText().toString().trim();
        if (!TextUtils.isEmpty(ActivityStatus)) {
            if (TextUtils.isEmpty(strOldPass)) {
                ToastUtil.showShort(mContext, "请输入当前提现密码");
                return;
            }
        }
        if (TextUtils.isEmpty(strNewpass) || strNewpass.length() < 6) {
            ToastUtil.showShort(mContext, "请输入6位数字");
            return;
        }
        if (TextUtils.isEmpty(strResetpass)) {
            ToastUtil.showShort(mContext, "请重复输入密码");
            return;
        }
        if (!strNewpass.equals(strResetpass)) {
            ToastUtil.showShort(mContext, "两次输入的支付密码必须相同");
            return;
        }
        if (!NetWorkState.isNetworkAvailable(mContext)) {
            ToastUtil.showShort(mContext, getString(R.string.load_data_nonetwork));
            return;
        }
        if (strNewpass.equals(strResetpass)) {
            SetUpWithdrawPwdRequest pwdRequest;
            if (TextUtils.isEmpty(ActivityStatus)) {
                // 设置密码
                pwdRequest = new SetUpWithdrawPwdRequest(WebUrl.setUpWithdrawPwd);
                pwdRequest.setRequestParms(strNewpass, strResetpass);
                pwdRequest.setReturnDataClick(mContext, 0, this);
            } else {
                pwdRequest = new SetUpWithdrawPwdRequest(WebUrl.modifyWithdrawPwd);
                pwdRequest.setRequestParms(strOldPass, strNewpass, strResetpass);
                pwdRequest.setReturnDataClick(mContext, 1, this);
            }
        } else {
            ToastUtil.showShort(mContext, "两次输入的支付密码必须相同");
        }
    }

    @Override
    public void onReturnData(int what, Object obj) {
        if (obj == null) return;
        switch (what) {
            case 0:
                boolean setPassFlag = (boolean) obj;
                if (setPassFlag) {
                    if (TextUtils.isEmpty(getIntent().getStringExtra("AuthenticationType"))) {
                        openActivity(mContext, WithdrawalsActivity.class);
                    } else {
                        openActivity(mContext, AlipayCashActivity.class);
                    }
                    finish();
                }
                break;
            case 1:
                boolean UpdatePassFlag = (boolean) obj;
                if (UpdatePassFlag) {
                    SPUtils.put(mContext, Configs.isIntentMyFrgment, true);
                    Intent intent = new Intent(mContext, CommitSuccessActivity.class);
                    intent.putExtra(CommitSuccessActivity.TYPE, Constants.SETPASS);
                    intent.putExtra(CommitSuccessActivity.TITLE, "修改成功");
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }
                break;
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
