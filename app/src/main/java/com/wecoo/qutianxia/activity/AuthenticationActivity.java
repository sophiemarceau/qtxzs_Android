package com.wecoo.qutianxia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.manager.AppFolderManager;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.UserIdInfoEntity;
import com.wecoo.qutianxia.requestjson.GetIDInfoRequest;
import com.wecoo.qutianxia.requestjson.IsWithdrawPwdRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.SubmitIDInfoRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.utils.FileUtil;
import com.wecoo.qutianxia.utils.StringUtil;
import com.wecoo.qutianxia.utils.ToastUtil;

import java.io.File;

/**
 * Created by mwl on 2016/10/25.
 * 取现身份认证
 */

public class AuthenticationActivity extends TitleBarActivity implements OnClickListener, ReturnDataClick {

    private final String mPageName = "AuthenticationActivity";
    private Context mContext = AuthenticationActivity.this;
    private LinearLayout llAlert;// 弹层的布局
    // 姓名   身份证号
    private EditText editName, editIDNumber;
    private String strName, strIDNumber, strPhoto;
    // 上传说明    事例描述
    private TextView txtExplain, txtDescribe;
    // 提交信息    上传照片
    private Button btnCommit, btnUpload;
    // 上传后的照片    事例的照片
    private ImageView imgUpload;
    // 用户是否设置过提现密码
    private boolean isWithdrawPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.authentication), None);

        initView();
    }

    // 初始化View
    private void initView() {
        llAlert = (LinearLayout) findViewById(R.id.authentication_linear_alert);
        editName = (EditText) findViewById(R.id.authentication_edit_name);
        editIDNumber = (EditText) findViewById(R.id.authentication_edit_idNum);
        txtExplain = (TextView) findViewById(R.id.authentication_txt_Explain);
        txtDescribe = (TextView) findViewById(R.id.authentication_txt_Describe);
        btnCommit = (Button) findViewById(R.id.authentication_btn_Commit);
        btnUpload = (Button) findViewById(R.id.authentication_btn_uploadPic);
        imgUpload = (ImageView) findViewById(R.id.authentication_img_Upload);

        String explain = "1、请填写姓名、身份证号码（确保填写的信息与证件一致），并上传本人手持身份证的正面合影；\n\n" +
                "2、请确保五官、证件内容清晰可见。我们将在3个工作日内完成审核。";
        txtExplain.setText(explain);
        String describe = "1、请上传本人手持身份证的正面合影；\n\n" + "2、请确保五官、证件内容清晰可见。";
        txtDescribe.setText(describe);
        // 添加监听
        btnCommit.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        imgUpload.setOnClickListener(this);
        llAlert.setOnClickListener(this);

        getUserSalesmanIDInfoDto();
    }

    private void getUserSalesmanIDInfoDto() {
        // 获取业务员实名认证信息
        GetIDInfoRequest getIDInfoRequest = new GetIDInfoRequest();
        getIDInfoRequest.setReturnDataClick(mContext, 0, this);
        // 获取用户是否设置过提现密码
        IsWithdrawPwdRequest pwdRequest = new IsWithdrawPwdRequest();
        pwdRequest.setReturnDataClick(mContext, false, 2, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.authentication_btn_Commit:
                submitIDInfo();
                break;
            case R.id.authentication_btn_uploadPic:
                showDialog();
                break;
            case R.id.authentication_img_Upload:
                llAlert.setVisibility(View.VISIBLE);
                llAlert.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.in_from_buttom));
                break;
            case R.id.authentication_linear_alert:
                llAlert.setVisibility(View.GONE);
                llAlert.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.out_to_buttom));
                break;
        }
    }

    // 业务员提交实名认证信息
    private void submitIDInfo() {
        strName = editName.getText().toString().trim();
        strIDNumber = editIDNumber.getText().toString().trim();
        if (TextUtils.isEmpty(strName) || strName.length() < 2) {
            ToastUtil.showShort(mContext, "请正确填写姓名");
            return;
        }
        if (TextUtils.isEmpty(strIDNumber) || !StringUtil.IsIDcard(strIDNumber)) {
            ToastUtil.showShort(mContext, "请正确填写身份证号");
            return;
        }
        if (TextUtils.isEmpty(strPhoto)) {
            ToastUtil.showShort(mContext, "身份证照片不能为空");
            return;
        }
        SubmitIDInfoRequest idInfoRequest = new SubmitIDInfoRequest();
        idInfoRequest.setRequestParms(strName, strIDNumber, strPhoto);
        idInfoRequest.setReturnDataClick(mContext, 1, this);
    }

    // 显示拍照
    private void showDialog() {
        llAlert.setVisibility(View.GONE);
        llAlert.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.out_to_buttom));
        Intent intent = new Intent(mContext, SelectPhotoActivity.class);
        intent.putExtra(SelectPhotoActivity.TYPE, 2);
        startActivityForResult(intent, SelectPhotoActivity.requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SelectPhotoActivity.requestCode) {
                if (data != null){
                    strPhoto = (String) data.getSerializableExtra("picUrl");
                    File file = (File) data.getSerializableExtra("PicFile");
                    imageManager.loadLocalImage(file, imgUpload);
                }
//                Bundle extras = data.getExtras();
//                if (extras != null) {
//                    strPhoto = extras.getString(SelectPhotoActivity.PIC_URL);
//                    Serializable file = extras.getSerializable(SelectPhotoActivity.PIC_URL);
//                    imageManager.loadUrlImage(strPhoto, imgUpload, R.color.default_pics);
//                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (llAlert.getVisibility() == View.VISIBLE) {
            llAlert.setVisibility(View.GONE);
            llAlert.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.out_to_buttom));
        } else {
            super.onBackPressed();
        }
    }

    // 请求的回调
    @Override
    public void onReturnData(int what, Object obj) {
        if (obj == null) return;
        switch (what) {
            case 0:
                UserIdInfoEntity entity = (UserIdInfoEntity) obj;
                strName = entity.getUs_realname();
                if (!TextUtils.isEmpty(strName)) {
                    editName.setText(strName);
                }
                strIDNumber = entity.getUs_id_number();
                if (!TextUtils.isEmpty(strIDNumber)) {
                    editIDNumber.setText(strIDNumber);
                }
                strPhoto = entity.getUs_id_photo();
                if (!TextUtils.isEmpty(strPhoto)) {
                    imageManager.loadUrlImage(strPhoto, imgUpload);
                }
                break;
            case 1:
                boolean booFlag = (boolean) obj;
                if (booFlag) {
                    if (isWithdrawPwd) {
                        if (TextUtils.isEmpty(getIntent().getStringExtra("AuthenticationType"))) {
                            Intent intent = new Intent(mContext, WithdrawalsActivity.class);
                            if (!TextUtils.isEmpty(getIntent().getStringExtra("swa_id"))) {
                                intent.putExtra("swa_id", getIntent().getStringExtra("swa_id"));
                            }
                            startActivity(intent);
//                        openActivity(mContext, WithdrawalsActivity.class);
                        } else {
                            Intent intent = new Intent(mContext, AlipayCashActivity.class);
                            if (!TextUtils.isEmpty(getIntent().getStringExtra("swa_id"))) {
                                intent.putExtra("swa_id", getIntent().getStringExtra("swa_id"));
                            }
                            startActivity(intent);
//                        openActivity(mContext, AlipayCashActivity.class);
                        }
                    } else {
                        Intent intent = new Intent(mContext, SetPasswordActivity.class);
                        if (!TextUtils.isEmpty(getIntent().getStringExtra("AuthenticationType"))) {
                            intent.putExtra("AuthenticationType", getIntent().getStringExtra("AuthenticationType"));
                        }
                        startActivity(intent);
                    }
                    finish();
                }
                break;
            case 2:
                isWithdrawPwd = (boolean) obj;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtil.deleteDirectory(AppFolderManager.getInstance().getTempFolder());
        CallServer.getInstance().cancelBySign(mContext);
    }
}
