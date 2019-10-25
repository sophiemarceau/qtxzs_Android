package com.wecoo.qutianxia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.manager.AppFolderManager;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.manager.ImageManager;
import com.wecoo.qutianxia.models.UserIdInfoEntity;
import com.wecoo.qutianxia.requestjson.GetIDInfoRequest;
import com.wecoo.qutianxia.requestjson.GetPictureRequest;
import com.wecoo.qutianxia.requestjson.IsWithdrawEnableRequest;
import com.wecoo.qutianxia.requestjson.IsWithdrawPwdRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.utils.BitmapUtils;
import com.wecoo.qutianxia.utils.FileUtil;
import com.wecoo.qutianxia.utils.LogUtil;
import com.wecoo.qutianxia.utils.ScreenUtil;
import com.wecoo.qutianxia.utils.ToastUtil;

import java.io.File;

/**
 * Created by mwl on 2016/10/25.
 * 选择拍照和相册
 */

public class SelectPhotoActivity extends TitleBarActivity implements View.OnClickListener {

    private final String mPageName = "SelectPhotoActivity";
    private Context mContext = SelectPhotoActivity.this;
    private View viewClose, viewBackGround;
    private LinearLayout lineatView;
    private TextView txtTakePhoto, txtPickPhoto, txtCancel;
    // 拍照的FIle
    private File cameraFile;
    private final int CAMERA = 101, GALLERY = 102, CROP = 103;
    // 需要裁剪的类型（上个页面传来的值）   请求回来的图片链接           ActType 上个Activity来源(目前只有我的赏金设置)
    public static String TYPE = "cropType", ActType = "ActType";
    public static int requestCode = 1001;
    private int cropType;// 0是修改资料  1是投诉建议   2是实名认证
    private int PIC_BIG_PX = 800, PIC_NOMAL_PX = 700, PIC_SMALL_PX = 600, PIC_MINSMALL_PX = 470;
    public Uri imageUri;
    private String ActivityType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setavatar_dialogview);
        AppManager.getAppManager().addActivity(this);

        ActivityType = getIntent().getStringExtra(ActType);
        initView();
    }

    // 设置弹框的高度 获取控件的ID
    private void initView() {
        cropType = getIntent().getIntExtra(TYPE, 0);// 裁剪的类型
        cameraFile = new File(AppFolderManager.getInstance().getTempFolder(), ImageManager.CAMERA_PIC); // 拍照生成的文件
        imageUri = Uri.fromFile(new File(AppFolderManager.getInstance().getTempFolder(), ImageManager.SAVE_PATH));
        // 设置弹出层在底部
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.PopupAnimation);
        // 设置弹出层
        lineatView = (LinearLayout) findViewById(R.id.selectPhoto_linear_view);
        lineatView.setLayoutParams(new LayoutParams(ScreenUtil.getScreenWidth(this), LayoutParams.MATCH_PARENT));
        // 获取ID
        viewClose = findViewById(R.id.setAvatar_viewClose);
        viewBackGround = findViewById(R.id.setAvatar_viewBackGround);
        txtTakePhoto = (TextView) findViewById(R.id.setAvatar_txt_takephoto);
        txtPickPhoto = (TextView) findViewById(R.id.setAvatar_txt_pickphoto);
        txtCancel = (TextView) findViewById(R.id.setAvatar_txt_cancel);
        if (!TextUtils.isEmpty(ActivityType)) {
            txtTakePhoto.setText(getString(R.string.Alipay_Withdrawals));
            txtPickPhoto.setText(getString(R.string.Bankcard_Withdrawals));
            txtCancel.setText(getString(R.string.cancle_Withdrawals));
        }
        // 添加监听
        txtTakePhoto.setOnClickListener(this);
        txtPickPhoto.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        viewClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setAvatar_txt_takephoto:
                if (TextUtils.isEmpty(ActivityType)) {
                    takePhoto();
                } else {
                    isWithdrawEnable(0);
                }
                break;
            case R.id.setAvatar_txt_pickphoto:
                if (TextUtils.isEmpty(ActivityType)) {
                    pickPhoto();
                } else {
                    isWithdrawEnable(1);
                }
                break;
            case R.id.setAvatar_txt_cancel:
            case R.id.setAvatar_viewClose:
                finish();
                break;
        }
    }

    // 当前业务员是否可申请提现
    private void isWithdrawEnable(final int type) {
        IsWithdrawEnableRequest enableRequest = new IsWithdrawEnableRequest();
        enableRequest.setReturnDataClick(mContext, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                if (obj != null && "1".equals(String.valueOf(obj))) {
                    getIdInfoData(type);
                }
            }
        });
    }

    //
    private void getIdInfoData(final int type) {
        // 获取业务员实名认证信息
        GetIDInfoRequest getIDInfoRequest = new GetIDInfoRequest();
        getIDInfoRequest.setReturnDataClick(mContext, 1, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                UserIdInfoEntity infoEntity = (UserIdInfoEntity) obj;
                if (infoEntity != null) {
                    int authentication = infoEntity.getUs_id_status_code();
                    switch (authentication) {
                        case 1:
                        case 2:
                            isWithdrawPwd(type);
                            break;
                        default:
                            // 跳转实名认证
                            Intent intent = new Intent(mContext, AuthenticationActivity.class);
                            if (type == 0) {
                                intent.putExtra("AuthenticationType", "支付宝提现");
                            }
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                            break;
                    }
                }
            }
        });
    }

    // 判断是否设置提现密码
    private void isWithdrawPwd(final int type) {
        IsWithdrawPwdRequest pwdRequest = new IsWithdrawPwdRequest();
        pwdRequest.setReturnDataClick(mContext, true, 2, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                if (obj != null) {
                    boolean isWithdrawPwd = (boolean) obj;
                    if (isWithdrawPwd) {
                        if (type == 0) {
                            // 跳转支付宝
                            openActivity(mContext, AlipayCashActivity.class);
                        } else {
                            // 跳转个人银行
                            openActivity(mContext, WithdrawalsActivity.class);
                        }
                    } else {
                        // 跳转设置支付密码
                        Intent intent = new Intent(mContext, SetPasswordActivity.class);
                        if (type == 0) {
                            intent.putExtra("AuthenticationType", "支付宝提现");
                        }
                        startActivity(intent);
                    }
                }
                finish();
            }
        });
    }

    // 拍照
    private void takePhoto() {
        Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
        startActivityForResult(intent_camera, CAMERA);
    }

    // 相册
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GALLERY);
    }

    // 回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA:
                    if (cameraFile.exists()) {
                        int i = BitmapUtils.readPictureDegree(cameraFile.getAbsolutePath());
                        LogUtil.e("图片旋转角度" + i);
                        if (Math.abs(i) >= 90) {
                            try {
                                Bitmap bitmap1 = BitmapFactory.decodeFile(cameraFile.getAbsolutePath());
                                Bitmap bitmap2 = BitmapUtils.toRotate90(bitmap1, i);
                                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap2, null, null));
                                startPhotoZoom(uri);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            startPhotoZoom(Uri.fromFile(cameraFile));
                        }
                    }
                    break;
                case GALLERY:
                    if (data != null) {
                        startPhotoZoom(data.getData());
                    }
                    break;
                case CROP:
                    if (imageUri != null) {
                        File cropFile = new File(AppFolderManager.getInstance().getTempFolder(), ImageManager.SAVE_PATH);
                        if (cropFile.exists()) {
                            Bitmap newBitmap = null;
                            switch (cropType) {
                                case 0:
                                case 1:
                                    newBitmap = BitmapUtils.decode(cropFile.getAbsolutePath(),
                                            PIC_NOMAL_PX / 2, PIC_NOMAL_PX / 2, Bitmap.Config.RGB_565);
                                    break;
                                case 2:
                                    newBitmap = BitmapUtils.decode(cropFile.getAbsolutePath(),
                                            PIC_BIG_PX / 2, PIC_SMALL_PX / 2, Bitmap.Config.RGB_565);
                                    break;
                                case 3:
                                    newBitmap = BitmapUtils.decode(cropFile.getAbsolutePath(),
                                            PIC_BIG_PX / 2, PIC_MINSMALL_PX / 2, Bitmap.Config.RGB_565);
                                    break;
                            }
                            if (newBitmap != null) {
                                final File newFile = ImageManager.saveBitmapFile(newBitmap, ImageManager.SAVE_PATH);
                                if (newFile != null) {
                                    viewClose.setBackgroundResource(R.color.blank_alpha_40);
                                    viewBackGround.setBackgroundResource(R.color.blank_alpha_40);
                                    upDownloadPhoto(newFile);
                                }
                            }
                        } else {
                            ToastUtil.showShort(mContext, "照片获取失败，请重新上传");
                        }
                    }
                    break;
            }
        }
    }

    private void upDownloadPhoto(final File file) {
        GetPictureRequest request;
        if (cropType == 1) {  // 投诉建议的请求
            request = new GetPictureRequest(WebUrl.uploadPic);
        } else if (cropType == 3) {  // 企业上传照片的请求
            request = new GetPictureRequest(WebUrl.uploadCompFile);
        } else {  // 头像和身份证上传照片的请求
            request = new GetPictureRequest(WebUrl.uploadPhoto);
        }
        request.setRequestParms(file);
        request.setReturnDataClick(mContext, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                FileUtil.deleteFile(cameraFile.getAbsolutePath());
                String strPhoto = (String) obj;
                // 返回到上个页面的值
                Intent intent = new Intent();
                switch (cropType) {
                    case 0:
                        intent.setClass(mContext, UserManagerActivity.class);
                        break;
                    case 1:
                        intent.setClass(mContext, ComplaintActivity.class);
                        break;
                    case 2:
                        intent.setClass(mContext, AuthenticationActivity.class);
                        break;
                    case 3:
                        intent.setClass(mContext, SettledActivity.class);
                        break;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("picUrl", strPhoto);
                bundle.putSerializable("PicFile", file);
                intent.putExtras(bundle);
//                intent.putExtra(PIC_URL, strPhoto);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    //裁剪图片
    public void startPhotoZoom(Uri uri) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");//调用Android系统自带的一个图片剪裁页面,
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");//进行修剪
            intent.putExtra("scale", true);
            // aspectX aspectY 是宽高的比例
            switch (cropType) {
                case 0:
                case 1:
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    // outputX outputY 是裁剪图片宽高
                    intent.putExtra("outputX", PIC_NOMAL_PX);
                    intent.putExtra("outputY", PIC_NOMAL_PX);
                    break;
                case 2:
                    intent.putExtra("aspectX", 4);
                    intent.putExtra("aspectY", 3);
                    // outputX outputY 是裁剪图片宽高
                    intent.putExtra("outputX", PIC_BIG_PX);
                    intent.putExtra("outputY", PIC_SMALL_PX);
                    break;
                case 3:
                    intent.putExtra("aspectX", 16);
                    intent.putExtra("aspectY", 9);
                    // outputX outputY 是裁剪图片宽高
                    intent.putExtra("outputX", PIC_BIG_PX);
                    intent.putExtra("outputY", PIC_MINSMALL_PX);
                    break;
            }
            intent.putExtra("noFaceDetection", true);// 取消人脸识别
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
            startActivityForResult(intent, CROP);
        } catch (Exception ex){
            ex.printStackTrace();
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
        CallServer.getInstance().cancelBySign(mContext);
    }
}
