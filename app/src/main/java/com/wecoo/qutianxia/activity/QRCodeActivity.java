package com.wecoo.qutianxia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.base.TitleBarActivity.RightCallbackListener;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.manager.AppFolderManager;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.manager.ImageManager;
import com.wecoo.qutianxia.manager.ImageManager.BitmapToFileListener;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.umeng.Defaultcontent;
import com.wecoo.qutianxia.umeng.ShareWindow;
import com.wecoo.qutianxia.utils.FileUtil;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.ToastUtil;

import java.io.File;

/**
 * Created by mwl
 * 生成邀请二维码
 */

public class QRCodeActivity extends TitleBarActivity implements RightCallbackListener {

    private final String mPageName = "QRCodeActivity";
    private LinearLayout ll_shotView;
    private TextView txtName;
    private ImageView imgPic, imgPhoto, img_QR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.invitation_QRcode), "分享");
        setRightCallbackListener(this);

        initView();
    }

    private void initView() {
        ll_shotView = (LinearLayout) findViewById(R.id.qrCode_ll_shotView);
        txtName = (TextView) findViewById(R.id.user_txt_Name);
        imgPic = (ImageView) findViewById(R.id.user_image_DefaultPic);
        imgPhoto = (ImageView) findViewById(R.id.user_image_Photo);
        img_QR = (ImageView) findViewById(R.id.qrCode_image_qr);
        File newFile = new File(AppFolderManager.getInstance().getTempFolder(), ImageManager.QRCODE_PATH);
        if (newFile.exists()) {
            imageManager.loadLocalImage(newFile, img_QR);
        }
        String name = (String) SPUtils.get(QRCodeActivity.this, Configs.user_Name, "");
        String photo = (String) SPUtils.get(QRCodeActivity.this, Configs.user_Photo, "");
        txtName.setText("推荐人: " + name);
        imageManager.loadCircleImage(photo, imgPhoto);
        imageManager.loadUrlImageNoCaChe(WebUrl.img_invitationtop, imgPic);
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
    public void onRightCallback(View view) {
        MobclickAgent.onEvent(QRCodeActivity.this, "QRCodeShare");
        // 判断当前View图片是否在本地，如果有就直接分享，不存在就重新获取
        File picFile = new File(AppFolderManager.getInstance().getTempFolder(), ImageManager.SCREENSHOT_PATH);
        if (picFile.exists()) {
            ShareWindow shareWindow = new ShareWindow(QRCodeActivity.this);
            shareWindow.setShareType(mPageName);
            shareWindow.setView(false);
            shareWindow.setShareImgData(picFile, Defaultcontent.shareFriendtitle);
            shareWindow.show();
        } else {
            int width = ll_shotView.getWidth();
            int height = ll_shotView.getHeight();
            imageManager.getViewBitmap(ll_shotView, width, height, new BitmapToFileListener() {
                @Override
                public void onBitmapToFile(File file) {
                    if (file.exists()) {
                        ShareWindow shareWindow = new ShareWindow(QRCodeActivity.this);
                        shareWindow.setShareType(mPageName);
                        shareWindow.setView(false);
                        shareWindow.setShareImgData(file, Defaultcontent.shareFriendtitle);
                        shareWindow.show();
                    } else {
                        ToastUtil.showShort(QRCodeActivity.this, "当前屏幕保存失败,不能分享");
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtil.deleteDirectory(AppFolderManager.getInstance().getTempFolder());
        UMShareAPI.get(this).release();
    }
}
