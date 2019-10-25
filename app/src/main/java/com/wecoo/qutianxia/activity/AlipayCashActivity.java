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
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.BankInfoEntity;
import com.wecoo.qutianxia.requestjson.AlipayWithdrawRequest;
import com.wecoo.qutianxia.requestjson.LastWithdrawalRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.VerCodeByUseridRequest;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.DialogView;
import com.wecoo.qutianxia.view.SendCodeButn;
import com.wecoo.qutianxia.view.SetPswDialogView;
import com.wecoo.qutianxia.view.SetPswDialogView.DialogCallback;

/**
 * Created by mwl on 2017/01/19.
 * 支付宝提现
 */

public class AlipayCashActivity extends TitleBarActivity implements View.OnClickListener {

    private final String mPageName = "AlipayCashActivity";
    private Context mContext = AlipayCashActivity.this;
    private ImageView imgPrompt;// 提示
    // 真实姓名    支付宝账号     提现金额
    private EditText editName, editAlipayNum, editPrice;
    // 收款人手机号
    private TextView txtTel;
    // 手机验证码
    private EditText editCode;
    private Button btnSendCode, btnCommit;
    // 姓名字符串   支付宝字符串   金额字符串   验证码字符串
    private String strName, strAlipayNum, strPrice, strCode;
    // 发送验证码
    private SendCodeButn btnTimer;
    // 申请提现的进度id
    private String swa_id;
    // 提现密码提示框
    private SetPswDialogView pswDialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.Alipay_Withdrawals), None);

        swa_id = getIntent().getStringExtra("swa_id");

        initView();
    }

    private void initView() {
        imgPrompt = (ImageView) findViewById(R.id.alipayCash_img_Prompt);
        editName = (EditText) findViewById(R.id.alipayCash_edit_name);
        editAlipayNum = (EditText) findViewById(R.id.alipayCash_txt_num);
        editPrice = (EditText) findViewById(R.id.alipayCash_edit_price);
        txtTel = (TextView) findViewById(R.id.alipayCash_txt_tel);
        editCode = (EditText) findViewById(R.id.alipayCash_edit_code);
        btnSendCode = (Button) findViewById(R.id.alipayCash_btn_sendCode);
        btnCommit = (Button) findViewById(R.id.alipayCash_btn_commit);

        //发送验证码的按钮
        btnTimer = new SendCodeButn(btnSendCode);
        // 获取支付宝信息
        getAlipayInfo();
        // setListener
        setListener();
    }

    // 设置监听
    private void setListener() {
        imgPrompt.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
        btnSendCode.setOnClickListener(this);
        // 提现金额只能输入两位小数
        editPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    editable.delete(posDot + 3, posDot + 4);
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
        if (pswDialogView != null){
            pswDialogView.hideDialog();
            pswDialogView = null;
        }
        CallServer.getInstance().cancelBySign(mContext);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alipayCash_img_Prompt:
                String msg = "为了保证您的资金安全，您的支付宝账户注册姓名必须与实名认证姓名一致！";
                new DialogView(mContext).createDialog(msg, true,
                        new DialogView.DialogCallback() {
                            @Override
                            public void onSureClick() {

                            }
                        });
                break;
            case R.id.alipayCash_btn_commit:
                editToString(null);
                break;
            case R.id.alipayCash_btn_sendCode:
                sendCode();
                btnTimer.start();
                break;
        }
    }

    // 发送验证码
    private void sendCode() {
        VerCodeByUseridRequest request = new VerCodeByUseridRequest();
        request.setReturnDataClick(mContext, 2);
    }

    // 获取支付宝信息
    private void getAlipayInfo() {
        LastWithdrawalRequest lastrequest;
        if (!TextUtils.isEmpty(swa_id)) {
            lastrequest = new LastWithdrawalRequest(WebUrl.getSalesmanWithdrawingApplicationDto);
            lastrequest.setRequestParms(swa_id);
        } else {
            lastrequest = new LastWithdrawalRequest(WebUrl.getLastWithdrawalRecordByType);
            lastrequest.setRequestParms(2);
        }
        lastrequest.setReturnDataClick(mContext, 1, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                BankInfoEntity entity = (BankInfoEntity) obj;
                if (entity != null) {
                    if (!TextUtils.isEmpty(entity.getUs_realname())) {
                        editName.setText(entity.getUs_realname());
                        editName.setEnabled(false);
                    }
                    editAlipayNum.setText(entity.getSwa_alipay_account());
                    if (!TextUtils.isEmpty(swa_id)) {
                        editPrice.setEnabled(false);
                        editPrice.setText(entity.getSwa_sum_str());
                    }
                    txtTel.setText(entity.getUser_login());
                }
            }
        });
    }

    // 校验支付密码
    private void showPassDialog() {
        pswDialogView = new SetPswDialogView(mContext);
        pswDialogView.createDialog();
        pswDialogView.setClickListener(new DialogCallback() {
            @Override
            public void onSureClick(String flag) {
                if (!TextUtils.isEmpty(flag)) {
                    editToString(flag);
                }
            }
        });
    }

    // 输入框内容转化为字符串，并且判断
    private void editToString(String withdraw_pwd) {
        // 姓名字符串   支付宝字符串   金额字符串   验证码字符串
        strName = editName.getText().toString().trim();
        strAlipayNum = editAlipayNum.getText().toString().trim();
        strPrice = editPrice.getText().toString().trim();
        strCode = editCode.getText().toString().trim();

        if (TextUtils.isEmpty(strName)) {
            ToastUtil.showShort(mContext, getString(R.string.alipay_name) + "不能为空");
        } else if (TextUtils.isEmpty(strAlipayNum)) {
            ToastUtil.showShort(mContext, getString(R.string.alipay_number) + "不能为空");
        } else if (TextUtils.isEmpty(strPrice)) {
            ToastUtil.showShort(mContext, getString(R.string.withdrawals_money) + "不能为空");
        } else if (TextUtils.isEmpty(strCode)) {
            ToastUtil.showShort(mContext, "验证码不能为空");
        } else {
            commitApply(withdraw_pwd);
        }
    }

    // 支付宝提现申请
    private void commitApply(String withdraw_pwd) {
        AlipayWithdrawRequest request;
        if (!TextUtils.isEmpty(swa_id)) {
            request = new AlipayWithdrawRequest(WebUrl.updateApplyWithdrawByAlipay);
        } else {
            request = new AlipayWithdrawRequest(WebUrl.applyWithdrawByAlipay);
        }
        AlipayWithdrawRequest.AlipayWithdrawParms parms = new AlipayWithdrawRequest.AlipayWithdrawParms();
        parms.swa_id = getIntent().getStringExtra("swa_id");
        parms.swa_name = strName;
        parms.swa_alipay_account = strAlipayNum;
        parms.swa_sum = strPrice;
        parms.verifyCode = strCode;
        parms.us_withdraw_pwd = withdraw_pwd;
        request.setRequestParms(parms);
        request.setReturnDataClick(mContext, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                String result = (String) obj;
                if (!TextUtils.isEmpty(result)) {
                    if ("10000".equals(result)) { // 提现时没有提现密码
                        showPassDialog();
                    } else if ("99999".equals(result)) { // 提现时提现密码错误
                        if (pswDialogView != null){
                            pswDialogView.setContent(2);
                        }
                    } else {
                        if (pswDialogView != null){
                            pswDialogView.hideDialog();
                            pswDialogView = null;
                        }
                        Intent intent = new Intent(mContext, CommitSuccessActivity.class);
                        intent.putExtra(CommitSuccessActivity.TYPE, Constants.WITHDRAWALS);
                        intent.putExtra(CommitSuccessActivity.TITLE, getString(R.string.apply_withdrawals));
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
}
