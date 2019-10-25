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
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.models.BankInfoEntity;
import com.wecoo.qutianxia.models.FilterEntity;
import com.wecoo.qutianxia.requestjson.ApplyWithdrawRequest;
import com.wecoo.qutianxia.requestjson.LastWithdrawalRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.VerCodeByUseridRequest;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.utils.LogUtil;
import com.wecoo.qutianxia.utils.StringUtil;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.DialogView;
import com.wecoo.qutianxia.view.ItemSelectAction;
import com.wecoo.qutianxia.view.ItemSelectAction.OnSelectListener;
import com.wecoo.qutianxia.view.SendCodeButn;
import com.wecoo.qutianxia.view.SetPswDialogView;
import com.wecoo.qutianxia.view.SetPswDialogView.DialogCallback;
import com.wecoo.qutianxia.view.wheelcity.ChooseCityInterface;
import com.wecoo.qutianxia.view.wheelcity.SelectAdressUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwl on 2016/10/25.
 * 账户提现
 */

public class WithdrawalsActivity extends TitleBarActivity implements View.OnClickListener {

    private final String mPageName = "WithdrawalsActivity";
    private Context mContext = WithdrawalsActivity.this;
    private ImageView imgPrompt;// 提示
    private Button btnCommit, btnSendCode;
    // 收款人姓名   银行名称    卡号   提现金额
    private EditText editName, editBankName, editCard, editMoneyNum, editCode;   // 验证码
    private TextView txt_bank, txt_bankAdress, txtTel; // 收款人银行    开户行地区    手机号
    private String strName, strBank, strBankName, strCard, strNum, strCode;
    private List<FilterEntity> entityList = new ArrayList<FilterEntity>();
    private int index = 0;
    // 二级地址code码
    private String strAdress;
    // 发送验证码
    private SendCodeButn btnTimer;
    // 申请提现的进度id
    private String swa_id;
    // 提现密码提示框
    private SetPswDialogView pswDialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.Bankcard_Withdrawals), None);

        swa_id = getIntent().getStringExtra("swa_id");

        initData();
        initView();
    }

    // 初始化数据
    private void initData() {
        entityList = ModelManager.getInstance().getLookupBankAll(mContext);
    }

    // 初始化View
    private void initView() {
        btnCommit = (Button) findViewById(R.id.withdrawls_btn_commit);
        editName = (EditText) findViewById(R.id.withdrawals_edit_name);
        editBankName = (EditText) findViewById(R.id.withdrawals_edit_bankName);
        editCard = (EditText) findViewById(R.id.withdrawals_edit_bankCard);
        editMoneyNum = (EditText) findViewById(R.id.withdrawals_edit_moneyNum);
        txt_bank = (TextView) findViewById(R.id.withdrawals_txt_bank);
        txt_bankAdress = (TextView) findViewById(R.id.withdrawals_txt_bankAdress);
        imgPrompt = (ImageView) findViewById(R.id.withdrawals_img_Prompt);
        editCode = (EditText) findViewById(R.id.withdrawals_edit_code);
        txtTel = (TextView) findViewById(R.id.withdrawals_text_tel);
        btnSendCode = (Button) findViewById(R.id.withdrawals_btn_sendCode);
        // 输入框的监听
        editCard.addTextChangedListener(textWatcher);
        editMoneyNum.addTextChangedListener(new TextWatcher() {
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

        btnTimer = new SendCodeButn(btnSendCode);// 发送验证码的按钮
        // 添加监听
        btnCommit.setOnClickListener(this);
        txt_bank.setOnClickListener(this);
        txt_bankAdress.setOnClickListener(this);
        btnSendCode.setOnClickListener(this);
        imgPrompt.setOnClickListener(this);

        // 获取上一次取现记录
        getLastWithdrawalRecord();
    }

    // 获取上一次提现记录（获取上一次提现银行卡信息）
    private void getLastWithdrawalRecord() {
        LastWithdrawalRequest lastrequest;
        if (!TextUtils.isEmpty(swa_id)) {
            lastrequest = new LastWithdrawalRequest(WebUrl.getSalesmanWithdrawingApplicationDto);
            lastrequest.setRequestParms(swa_id);
        } else {
            lastrequest = new LastWithdrawalRequest(WebUrl.getLastWithdrawalRecordByType);
            lastrequest.setRequestParms(1);
        }
        lastrequest.setReturnDataClick(mContext, 1, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                BankInfoEntity entity = (BankInfoEntity) obj;
                if (entity != null) {
                    strBank = entity.getSwa_bank();
                    getBankIndex(strBank);
                    if (!TextUtils.isEmpty(entity.getUs_realname())) {
                        editName.setText(entity.getUs_realname());
                        editName.setEnabled(false);
                    }
                    txt_bank.setText(entity.getSwa_bank_name());
                    editBankName.setText(entity.getSwa_bank_fullname());
                    if (!TextUtils.isEmpty(swa_id)) {
                        editMoneyNum.setEnabled(false);
                        editMoneyNum.setText(entity.getSwa_sum_str());
                    }
                    strAdress = entity.getSwa_card_area();
//                    txt_bankAdress.setText(AsyncAdress.getAdressIntance(mContext).getCityAgent(strAdress));
                    txt_bankAdress.setText(entity.getSwa_card_area_name());
                    txtTel.setText(entity.getUser_login());
                    String Swa_card_no = entity.getSwa_card_no();
                    if (!TextUtils.isEmpty(Swa_card_no)) {
                        StringBuilder sBuilder = new StringBuilder(Swa_card_no);
                        int length = Swa_card_no.length() / 4 + Swa_card_no.length();
                        for (int i = 0; i < length; i++) {
                            if (i % 5 == 0) {
                                sBuilder.insert(i, " ");
                            }
                        }
                        sBuilder.deleteCharAt(0);
                        editCard.setText(sBuilder.toString());
                    }
                }
            }
        });
    }

    // 获取银行的索引
    private void getBankIndex(String code) {
        if (TextUtils.isEmpty(code)) return;
        if (entityList != null) {
            for (int i = 0; i < entityList.size(); i++) {
                if (code.equals(entityList.get(i).getCode())) {
                    index = i;
                }
            }
        }
    }

    // 编辑框的监听
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            LogUtil.d("WithdrawalsActivity", "beforeTextChanged = " + charSequence);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            LogUtil.d("WithdrawalsActivity", "onTextChanged = " + charSequence);
            if (i2 > 1) {
                i2 = 1;
            }
            if (i2 == 1) {
                int length = charSequence.toString().length();
                if (length == 4 || length == 9 || length == 14 || length == 19) {
                    editCard.setText(charSequence + " ");
                    editCard.setSelection(editCard.getText().toString().length());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            LogUtil.d("WithdrawalsActivity", "editable = " + editable.toString());
        }
    };

    // 监听方法
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.withdrawls_btn_commit:
                editToString(null);
                break;
            case R.id.withdrawals_txt_bank:
                if (entityList == null || entityList.size() < 1) {
                    initData();
                    return;
                }
                ItemSelectAction selectAction = new ItemSelectAction(mContext, entityList, index);
                selectAction.show();
                selectAction.setSelectListener(new OnSelectListener() {
                    @Override
                    public void onSelectData(FilterEntity entity) {
                        index = entity.id;
                        strBank = entity.getCode();
                        txt_bank.setText(entity.getName());
                    }
                });
                break;
            case R.id.withdrawals_txt_bankAdress:
                SelectAdressUtil cityUtil = new SelectAdressUtil();
                cityUtil.createDialog(mContext, strAdress, new ChooseCityInterface() {
                    @Override
                    public void sure(String[] newCityArray) {
                        txt_bankAdress.setText(newCityArray[0] + " " + newCityArray[1] + " " + newCityArray[2]);
                        strAdress = newCityArray[3];
                    }
                });
                break;
            case R.id.withdrawals_img_Prompt:
                String msg = "为了保证您的资金安全，您的银行卡注册姓名必须与实名认证姓名一致！";
                new DialogView(mContext).createDialog(msg, true,
                        new DialogView.DialogCallback() {
                            @Override
                            public void onSureClick() {

                            }
                        });
                break;
            case R.id.withdrawals_btn_sendCode:
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
    // 处理double
//    DecimalFormat df = new DecimalFormat("######0.00");

    // 输入框内容转化为字符串，并且判断
    private void editToString(String withdraw_pwd) {
        strName = editName.getText().toString().trim();
        strBankName = editBankName.getText().toString().trim();
        strCard = editCard.getText().toString().replace(" ", "").trim();
        strNum = editMoneyNum.getText().toString().trim();
        strCode = editCode.getText().toString().trim();

        if (TextUtils.isEmpty(strName)) {
            ToastUtil.showShort(mContext, getString(R.string.payee_name) + "不能为空");
        } else if (TextUtils.isEmpty(strBank)) {
            ToastUtil.showShort(mContext, getString(R.string.payee_bank) + "不能为空");
        } else if (TextUtils.isEmpty(strBankName)) {
            ToastUtil.showShort(mContext, getString(R.string.bank_name) + "不能为空");
        } else if (TextUtils.isEmpty(strCard)) {
            ToastUtil.showShort(mContext, getString(R.string.bank_card) + "不能为空");
        } else if (TextUtils.isEmpty(strNum)) {
            ToastUtil.showShort(mContext, getString(R.string.withdrawals_money) + "不能为空");
        } else if (!StringUtil.strToDou(strNum)) {
            ToastUtil.showShort(mContext, getString(R.string.withdrawals_moneymore));
        } else if (TextUtils.isEmpty(strAdress)) {
            ToastUtil.showShort(mContext, getString(R.string.payee_bankAdress) + "不能为空");
        } else if (TextUtils.isEmpty(strCode)) {
            ToastUtil.showShort(mContext, "验证码不能为空");
        } else {
            applyWithdraw(withdraw_pwd);
        }
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

    // 申请提现
    private void applyWithdraw(String withdraw_pwd) {
        ApplyWithdrawRequest applyWithdrawRequest;
        if (!TextUtils.isEmpty(swa_id)) {
            applyWithdrawRequest = new ApplyWithdrawRequest(WebUrl.updateApplyWithdrawByCard);
        } else {
            applyWithdrawRequest = new ApplyWithdrawRequest(WebUrl.applyWithdrawByCard);
        }
        ApplyWithdrawRequest.ApplyWithdrawParms parms = new ApplyWithdrawRequest.ApplyWithdrawParms();
        parms.swa_id = getIntent().getStringExtra("swa_id");
        parms.swa_name = strName;
        parms.swa_bank = strBank;
        parms.swa_bank_fullname = strBankName;
        parms.swa_card_no = strCard;
        parms.swa_card_area = strAdress;
        parms.verifyCode = strCode;
        parms.swa_sum = strNum;
        parms.us_withdraw_pwd = withdraw_pwd;
        applyWithdrawRequest.setRequestParms(parms);
        applyWithdrawRequest.setReturnDataClick(mContext, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                String result = (String) obj;
                if (!TextUtils.isEmpty(result)) {
                    if ("10000".equals(result)) { // 提现时没有提现密码
                        showPassDialog();
                    } else if ("99999".equals(result)) { // 提现时提现密码错误
                        if (pswDialogView != null) {
                            pswDialogView.setContent(2);
                        }
                    } else {
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
}
