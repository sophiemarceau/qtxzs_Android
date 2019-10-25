package com.wecoo.qutianxia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.base.TitleBarActivity.RightCallbackListener;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.models.FilterEntity;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.SettledApplyRequest;
import com.wecoo.qutianxia.utils.AppInfoUtil;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.StringUtil;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.ItemSelectAction;
import com.wecoo.qutianxia.view.wheelcity.ChooseCityInterface;
import com.wecoo.qutianxia.view.wheelcity.SelectAdressUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwl on 2017/06/01.
 * 企业入驻
 */

public class SettledActivity extends TitleBarActivity implements View.OnClickListener, RightCallbackListener {

    private final String mPageName = "SettledActivity";
    private Context mContext = SettledActivity.this;
    private TextView txt_UserTel;// 用户手机号
    private TextView txtAdress, txtIndustry;// 企业地区    所属行业
    // 姓名    登录用户名     登录密码
    private EditText editName, editLoginName, editLoginPsw;
    // 企业名称      企业紧急联系人     联系人手机号
    private EditText editBusinesName, editBusinesPersion, editBusinesPersionPhone;
    // 企业描述
    private EditText editBusinesDesc;
    private Button btnCommit;
    // 登录密码显示隐藏开关      营业执照     个人名片
    private ImageView imgPsw, imgBusinessLicense, imgBusinessCard;
    // 密码是否显示
    private boolean isPswShow = false;
    private int int_Industry = 0;
    // 上传图片类型    1是营业执照     2是个人名片
    private int updownloadType = 1;
    // 以下是字符串
    private String strAdress, strIndustry;
    private String strName, strLoginName, streditLoginPsw;
    private String strBusinesName, strBusinesPersion, strPersionPhone, strBusinesDesc;
    private String strimgBusinessLicense, strimgBusinessCard;
    private FrameLayout flBusinessLicense, flBusinessCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settled_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.settled_Apply), R.mipmap.btn_enterprise_review_customer_service);
        setRightCallbackListener(this);

        initView();
    }

    private void initView() {
        flBusinessLicense = (FrameLayout) findViewById(R.id.Settled_fl_BusinessLicense);
        flBusinessCard = (FrameLayout) findViewById(R.id.Settled_fl_BusinessCard);
        //TextView
        txt_UserTel = (TextView) findViewById(R.id.Settled_txt_UserTel);
        txtAdress = (TextView) findViewById(R.id.Settled_txt_Adress);
        txtIndustry = (TextView) findViewById(R.id.Settled_txt_Industry);
        //EditText
        editName = (EditText) findViewById(R.id.Settled_edit_Name);
        editLoginName = (EditText) findViewById(R.id.Settled_edit_LoginName);
        editLoginPsw = (EditText) findViewById(R.id.Settled_edit_LoginPsw);
        editBusinesName = (EditText) findViewById(R.id.Settled_edit_BusinesName);
        editBusinesPersion = (EditText) findViewById(R.id.Settled_edit_BusinesPersion);
        editBusinesPersionPhone = (EditText) findViewById(R.id.Settled_edit_PersionTel);
        editBusinesDesc = (EditText) findViewById(R.id.Settled_edit_BusinesDesc);
        // ImageView
        imgPsw = (ImageView) findViewById(R.id.Settled_img_LoginPsw);
        imgBusinessLicense = (ImageView) findViewById(R.id.Settled_img_BusinessLicense);
        imgBusinessCard = (ImageView) findViewById(R.id.Settled_img_BusinessCard);
        // Button
        btnCommit = (Button) findViewById(R.id.Settled_butn_commit);

        // setListener
        setListener();
        getUserTel();
    }

    // 获取用户手机号
    private void getUserTel() {
        String user_Tel = (String) SPUtils.get(mContext, Configs.user_Tel, "");
        if (!TextUtils.isEmpty(user_Tel)) {
            txt_UserTel.setText(user_Tel);
        }
        txt_UserTel.setFocusable(true);
        txt_UserTel.setFocusableInTouchMode(true);
        txt_UserTel.requestFocus();
    }

    // 设置监听
    private void setListener() {
        imgPsw.setOnClickListener(this);
        imgBusinessLicense.setOnClickListener(this);
        imgBusinessCard.setOnClickListener(this);
        txtAdress.setOnClickListener(this);
        txtIndustry.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
        editLoginPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    imgPsw.setVisibility(View.VISIBLE);
                } else {
                    imgPsw.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // 添加监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Settled_img_LoginPsw:
                changeLoginPsw();
                break;
            case R.id.Settled_txt_Adress:
                selectAdress();
                break;
            case R.id.Settled_txt_Industry:
                selectIndustry();
                break;
            case R.id.Settled_img_BusinessLicense:
                updownloadType = 1;
                updownloadBusiness();
                break;
            case R.id.Settled_img_BusinessCard:
                updownloadType = 2;
                updownloadBusiness();
                break;
            case R.id.Settled_butn_commit:
                viewToString();
                break;
        }
    }

    // 改变登录密码
    private void changeLoginPsw() {
        if (isPswShow) {
            isPswShow = false;
            imgPsw.setImageResource(R.mipmap.btn_enterprise_review_hidden_password);
            // 设置为隐藏的密码
            editLoginPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            isPswShow = true;
            imgPsw.setImageResource(R.mipmap.btn_enterprise_review_display_password);
            // 设置为可见的密码
            editLoginPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    // 选择企业地区
    private void selectAdress() {
        SelectAdressUtil cityUtil = new SelectAdressUtil();
        cityUtil.createDialog(mContext, strAdress, new ChooseCityInterface() {
            @Override
            public void sure(String[] newCityArray) {
                txtAdress.setText(newCityArray[0] + " " + newCityArray[1] + " " + newCityArray[2]);
                strAdress = newCityArray[3];
            }
        });
    }

    // 选择意向行业
    private void selectIndustry() {
        if (application.IndustryList == null || application.IndustryList.size() < 3) {
            application.IndustryList = ModelManager.getInstance().getLookupIndustryAll(mContext);
        } else {
            List<FilterEntity> indusList = new ArrayList<FilterEntity>();
            FilterEntity entityT = new FilterEntity();
            entityT.setName("综合行业");
            entityT.setCode("99999");
            indusList.add(entityT);
            indusList.addAll(application.IndustryList);
            ItemSelectAction selectAction = new ItemSelectAction(mContext, indusList, int_Industry);
            selectAction.show();
            selectAction.setSelectListener(new ItemSelectAction.OnSelectListener() {
                @Override
                public void onSelectData(FilterEntity entity) {
                    int_Industry = entity.id;
                    strIndustry = entity.getCode();
                    txtIndustry.setText(entity.getName());
                }
            });
        }
    }

    // 上传营业执照   &&   个人名片
    private void updownloadBusiness() {
        Intent intent = new Intent(mContext, SelectPhotoActivity.class);
        intent.putExtra(SelectPhotoActivity.TYPE, 3);
        startActivityForResult(intent, SelectPhotoActivity.requestCode);
    }

    // view 转化  字符串
    private void viewToString() {
        strName = editName.getText().toString();
        strLoginName = editLoginName.getText().toString();
        streditLoginPsw = editLoginPsw.getText().toString();
        strBusinesName = editBusinesName.getText().toString();
        strBusinesPersion = editBusinesPersion.getText().toString();
        strPersionPhone = editBusinesPersionPhone.getText().toString();
        strBusinesDesc = editBusinesDesc.getText().toString();
        if (TextUtils.isEmpty(strName)) {
            ToastUtil.showShort(mContext, "请输入您的姓名");
        } else if (TextUtils.isEmpty(strLoginName)) {
            ToastUtil.showShort(mContext, "请输入登录用户名");
        } else if (strLoginName.length() < 4) {
            ToastUtil.showShort(mContext, "用户名必须为4-16位字母和数字");
        } else if (!StringUtil.isLoginUser(strLoginName)) {
            ToastUtil.showShort(mContext, "用户名必须输入字母、数字,以字母开头");
        } else if (TextUtils.isEmpty(streditLoginPsw)) {
            ToastUtil.showShort(mContext, "请输入登录密码");
        } else if (streditLoginPsw.length() < 6) {
            ToastUtil.showShort(mContext, "必须为6-20位登录密码");
        } else if (TextUtils.isEmpty(strBusinesName)) {
            ToastUtil.showShort(mContext, "请输入企业名称");
        } else if (TextUtils.isEmpty(strAdress)) {
            ToastUtil.showShort(mContext, "请选择所属地区");
        } else if (TextUtils.isEmpty(strIndustry)) {
            ToastUtil.showShort(mContext, "请选择所属行业");
        } else if (TextUtils.isEmpty(strBusinesPersion)) {
            ToastUtil.showShort(mContext, "请输入联系人姓名");
        } else if (TextUtils.isEmpty(strPersionPhone)) {
            ToastUtil.showShort(mContext, "请输入联系人手机号");
        } else if (!StringUtil.isMobile(strPersionPhone)) {
            ToastUtil.showShort(mContext, "请输入正确的手机号");
        } else if (TextUtils.isEmpty(strBusinesDesc)) {
            ToastUtil.showShort(mContext, "请输入企业简介");
        } else if (TextUtils.isEmpty(strimgBusinessLicense) && TextUtils.isEmpty(strimgBusinessCard)) {
            ToastUtil.showShort(mContext, "营业执照与个人名片需要至少上传一种");
        } else {
            commitData();
        }

    }

    // commit   Data
    private void commitData() {
        SettledApplyRequest applyRequest = new SettledApplyRequest();
        SettledApplyRequest.SettledApplyParms parms = new SettledApplyRequest.SettledApplyParms();
        parms.staff_name = strName;
        parms.staff_login = strLoginName;
        parms.staff_password = streditLoginPsw;
        parms.company_name = strBusinesName;
        parms.company_area = strAdress;
        parms.company_industry = strIndustry;
        parms.company_contact = strBusinesPersion;
        parms.company_tel = strPersionPhone;
        parms.company_desc = strBusinesDesc;
        parms.company_license = strimgBusinessLicense;
        parms.company_card = strimgBusinessCard;
        applyRequest.setRequestParms(parms);
        applyRequest.setReturnDataClick(mContext, 1, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                openActivity(mContext, MyProjectActivity.class);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SelectPhotoActivity.requestCode) {
                if (data != null) {
                    if (updownloadType == 2) {
                        strimgBusinessCard = (String) data.getSerializableExtra("picUrl");
                        File file = (File) data.getSerializableExtra("PicFile");
                        imageManager.loadLocalImage(file, imgBusinessCard);
                        flBusinessCard.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
                    } else {
                        strimgBusinessLicense = (String) data.getSerializableExtra("picUrl");
                        File file = (File) data.getSerializableExtra("PicFile");
                        imageManager.loadLocalImage(file, imgBusinessLicense);
                        flBusinessLicense.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
                    }
                }
            }
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

    // 客服电话
    @Override
    public void onRightCallback(View view) {
        AppInfoUtil.onCallPhone(mContext, "400-900-1135");
    }

}
