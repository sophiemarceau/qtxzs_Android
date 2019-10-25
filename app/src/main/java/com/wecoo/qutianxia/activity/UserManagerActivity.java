package com.wecoo.qutianxia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.manager.AppFolderManager;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.UserInfoEntity;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.UpdateUserInfoRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.utils.FileUtil;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.ToastUtil;

import java.io.File;

/**
 * Created by mwl on 2016/10/25.
 * 用户（账号）管理
 */

public class UserManagerActivity extends TitleBarActivity implements View.OnClickListener {

    private final String mPageName = "UserManagerActivity";
    private Context mContext = UserManagerActivity.this;
    private ImageView imgPhoto;
    private EditText editNickName, editCompany, editJob;
    private String strPhoto, strNickName, strCompany, strJob;
    private Button btnSave;
    private UserInfoEntity infoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermanager_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.user_manager), None);

        Intent intent = this.getIntent();
        infoEntity = (UserInfoEntity) intent.getSerializableExtra("user");

        initView();
    }

    // 初始化View
    private void initView() {
        // 获取控件
        imgPhoto = (ImageView) findViewById(R.id.usermanager_img_photo);
        editNickName = (EditText) findViewById(R.id.usermanager_edit_nickname);
        editCompany = (EditText) findViewById(R.id.usermanager_edit_company);
        editJob = (EditText) findViewById(R.id.usermanager_edit_job);
        btnSave = (Button) findViewById(R.id.usermanager_btn_save);

        imgPhoto.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        initData();
    }

    private void initData() {
        if (infoEntity != null) {
//            Toast.makeText(mContext,infoEntity.getUs_photo(),Toast.LENGTH_LONG).show();
            imageManager.loadCircleImage(infoEntity.getUs_photo(), imgPhoto);
            String nickname = infoEntity.getUs_nickname();
            editNickName.setText(nickname);
            if (!TextUtils.isEmpty(nickname)) {
                editNickName.setSelection(nickname.length());
            }
            editCompany.setText(infoEntity.getUs_company());
            editJob.setText(infoEntity.getUs_jobtitle());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.usermanager_img_photo:
                showDialog();
                break;
            case R.id.usermanager_btn_save:
                MobclickAgent.onEvent(mContext, "usermanager_saveOnclick");
                SPUtils.put(mContext, Configs.IsUpdateUserInfo, true); // 设置需要刷新用户信息
                saveUserInfo();
                break;
        }
    }

    // 保存用户信息
    private void saveUserInfo() {
        strNickName = editNickName.getText().toString().trim();
        strCompany = editCompany.getText().toString().trim();
        strJob = editJob.getText().toString().trim();
        if (TextUtils.isEmpty(strNickName) || strNickName.length() < 2) {
            ToastUtil.showShort(mContext, "请正确填写昵称");
            return;
        }
        UpdateUserInfoRequest updateUserInfoRequest = new UpdateUserInfoRequest();
        updateUserInfoRequest.setRequestParms(strNickName, strPhoto, strCompany, strJob);
        updateUserInfoRequest.setReturnDataClick(mContext, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                boolean flag = (boolean) obj;
                if (flag) {
                    ToastUtil.showShort(mContext, "修改资料成功");
                    finish();
                }
            }
        });
    }

    // 显示拍照
    private void showDialog() {
        Intent intent = new Intent(mContext, SelectPhotoActivity.class);
        intent.putExtra(SelectPhotoActivity.TYPE, 0);
        startActivityForResult(intent, SelectPhotoActivity.requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SelectPhotoActivity.requestCode) {
                if (data != null) {
                    strPhoto = (String) data.getSerializableExtra("picUrl");
                    File file = (File) data.getSerializableExtra("PicFile");
                    imageManager.loadCircleLocalImage(file, imgPhoto);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtil.deleteDirectory(AppFolderManager.getInstance().getTempFolder());
        CallServer.getInstance().cancelBySign(mContext);
    }
}
