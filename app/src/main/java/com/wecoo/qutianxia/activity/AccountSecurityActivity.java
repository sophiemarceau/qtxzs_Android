package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.listener.NoDoubleClickListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.requestjson.IsWithdrawPwdRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.ToastUtil;

/**
 * Created by mwl on 2017/03/30.
 * 账户安全
 */

public class AccountSecurityActivity extends TitleBarActivity {

    private final String mPageName = "AccountSecurityActivity";
    private Context mContext;
    // 修改提现密码     重置提现密码
    private TextView txtUpdate, txtReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting_layout);
        AppManager.getAppManager().addActivity(this);

        mContext = AccountSecurityActivity.this;

        initActionBar(this);
        setBanner(Left, getString(R.string.account_security), None);
        SPUtils.put(mContext, Configs.isIntentMyFrgment,false);

        initView();
    }

    private void initView() {
        txtUpdate = (TextView) findViewById(R.id.accountSet_txt_update);
        txtReset = (TextView) findViewById(R.id.accountSet_txt_reset);
        txtReset.setText("忘记提现密码");
        //
        txtUpdate.setVisibility(View.VISIBLE);
        txtUpdate.setOnClickListener(clickListener);
        txtReset.setOnClickListener(clickListener);
    }

    private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.accountSet_txt_update:
                    isWithdrawPwd(1);
                    break;
                case R.id.accountSet_txt_reset:
                    isWithdrawPwd(2);
                    break;
            }
        }
    };

    private void isWithdrawPwd(final int type) {
        IsWithdrawPwdRequest pwdRequest = new IsWithdrawPwdRequest();
        pwdRequest.setReturnDataClick(mContext, true, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                if (obj != null) {
                    boolean isWithdrawPwd = (boolean) obj;
                    if (isWithdrawPwd) {
                        if (type == 1) {
                            Intent intent = new Intent(mContext, SetPasswordActivity.class);
                            intent.putExtra("ActivityStatus","修改提现密码");
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                        } else if (type == 2) {
                            openActivity(mContext, ReSetPasswordActivity.class);
                        }
                    }else {
                        ToastUtil.showShort(mContext,"请在首次提现时设置提现密码后再进行此操作");
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isIntentMyFrgment = (boolean) SPUtils.get(mContext, Configs.isIntentMyFrgment,false);
        if (isIntentMyFrgment){
            SPUtils.put(mContext, Configs.CurrentTab, 3);
            openActivity(mContext, MainActivity.class);
            this.finish();
        }
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
        CallServer.getInstance().cancelBySign(mContext);
    }

}
