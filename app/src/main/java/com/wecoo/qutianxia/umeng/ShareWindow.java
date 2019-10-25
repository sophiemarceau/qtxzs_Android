package com.wecoo.qutianxia.umeng;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.QRCodeActivity;
import com.wecoo.qutianxia.manager.ImageManager;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.LogUtil;
import com.wecoo.qutianxia.utils.QRCodeUtils;
import com.wecoo.qutianxia.utils.ToastUtil;

import java.io.File;
import java.util.List;

/**
 * Created by mwl on 2016/11/17.
 * 分享的弹层
 */

public class ShareWindow implements View.OnClickListener {

    private Activity mActivity;
    private Dialog mWindow;
    private View txtWX_Share, txtwxCircle_Share, txtQrCode_Share,
            txtQQ_Share, txtwxQzone_Share, txtCancel;
    //    private ProgressDialog dialog;
    //    private UMImage image;
    private String shareContent, shareTitle/*, shareUrl*/;
    private UMWeb web;
    private UMImage imageurl, imagelocal;

    public ShareWindow(Activity activity) {
        this.mActivity = activity;
        View view = LayoutInflater.from(activity).inflate(R.layout.share_window_view, null);

        txtWX_Share = view.findViewById(R.id.share_to_wx_session);
        txtwxCircle_Share = view.findViewById(R.id.share_to_wxCircle);
        txtQrCode_Share = view.findViewById(R.id.share_to_qrCode);
        txtQQ_Share = view.findViewById(R.id.share_to_qq);
        txtwxQzone_Share = view.findViewById(R.id.share_to_qzone);
        txtCancel = view.findViewById(R.id.share_to_cancel);

        mWindow = new Dialog(activity, R.style.Dialog_No_Board);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mWindow.addContentView(view, params);

        Window dialogWindow = mWindow.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.PopupAnimation);
        mWindow.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = mWindow.getWindow().getAttributes();
        lp.width = activity.getResources().getDisplayMetrics().widthPixels; // 设置宽度
        mWindow.getWindow().setAttributes(lp);

        //
//        dialog = new ProgressDialog(mActivity);
        // 设置监听
        txtWX_Share.setOnClickListener(this);
        txtwxCircle_Share.setOnClickListener(this);
        txtQrCode_Share.setOnClickListener(this);
        txtQQ_Share.setOnClickListener(this);
        txtwxQzone_Share.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
    }

    // 是否显示扫码按钮
    public void setView(boolean status) {
        if (status) {
            txtQrCode_Share.setVisibility(View.VISIBLE);
        } else {
            txtQrCode_Share.setVisibility(View.GONE);
        }
    }

    private String shareType;

    public void setShareType(String ShareType) {
        this.shareType = ShareType;
    }

    public void setShareImgData(File imgId, String shareTitle) {
        if (imgId.exists()) {
            imagelocal = new UMImage(mActivity, imgId);
        }
        this.shareTitle = shareTitle;
    }

    public void setShareData(Object imgId, String shareTitle, String shareContent, String shareUrl) {
        web = new UMWeb(shareUrl);
        if (imgId != null) {
            if (imgId instanceof Integer) {
                Integer url = (Integer) imgId;
//                if (url != 0){

                imageurl = new UMImage(mActivity, url);
//                }
            }
            if (imgId instanceof String) {
                String url = (String) imgId;
                imageurl = new UMImage(mActivity, url);
            }
            if (imgId instanceof Bitmap) {
                Bitmap bmp = (Bitmap) imgId;
                imageurl = new UMImage(mActivity, bmp);
            }
            if (imgId instanceof File) {
                File file = (File) imgId;
                imageurl = new UMImage(mActivity, file);
            }
            web.setThumb(imageurl);
        }
        web.setTitle(shareTitle);
        web.setDescription(shareContent);
        this.shareTitle = shareTitle;
        this.shareContent = shareContent;
//        this.shareUrl = shareUrl;
    }

    public void show() {
        if (mWindow != null && !mWindow.isShowing()) {
            mWindow.show();
        }
    }

    public void dismiss() {
        if (mWindow != null && mWindow.isShowing()) {
            mWindow.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_to_wx_session:
                MobclickAgent.onEvent(mActivity, "share_to_wx");
                setPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.share_to_wxCircle:
//                UmengTool.checkWx(mActivity);
                MobclickAgent.onEvent(mActivity, "share_to_wxCircle");
                setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.share_to_qq:
                MobclickAgent.onEvent(mActivity, "share_to_qq");
                setPlatform(SHARE_MEDIA.QQ);
                break;
            case R.id.share_to_qzone:
                MobclickAgent.onEvent(mActivity, "share_to_qzone");
                setPlatform(SHARE_MEDIA.QZONE);
                break;
            case R.id.share_to_qrCode:
                MobclickAgent.onEvent(mActivity, "share_to_qrCode");
                try {
                    Bitmap localBitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.ic_launcher);
                    Bitmap newBmp = QRCodeUtils.getInstance().createQRCodeWithLogo(Defaultcontent.shareFriendurl, 300,
                            localBitmap, 0xff000000, 0xffffffff);
                    if (newBmp != null) {
                        ImageManager.saveBitmapFile(newBmp, ImageManager.QRCODE_PATH);
                        mActivity.startActivity(new Intent(mActivity, QRCodeActivity.class));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                dismiss();
                break;
            case R.id.share_to_cancel:
                MobclickAgent.onEvent(mActivity, "share_to_cancel");
                dismiss();
                break;
        }
    }

    private void setPlatform(SHARE_MEDIA platform) {
        if (platform == SHARE_MEDIA.QQ || platform == SHARE_MEDIA.QZONE) {
            if (!UMShareAPI.get(mActivity).isInstall(mActivity, SHARE_MEDIA.QQ) && !isQQClientAvailable(mActivity)) {
                ToastUtil.showShort(mActivity, "您还没有安装QQ，请先安装QQ客户端");
            } else {
                doShare(platform);
            }
        } else if (platform == SHARE_MEDIA.WEIXIN || platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
            if (!UMShareAPI.get(mActivity).isInstall(mActivity, SHARE_MEDIA.WEIXIN) && !isWeixinAvilible(mActivity)) {
                ToastUtil.showShort(mActivity, "您还没有安装微信，请先安装微信客户端");
            } else {
                doShare(platform);
            }
        }
        dismiss();
    }

    private void doShare(SHARE_MEDIA platform) {
        if (!NetWorkState.isNetworkAvailable(mActivity)) {
            ToastUtil.showShort(mActivity, mActivity.getString(R.string.load_data_nonetwork));
        } else if (TextUtils.isEmpty(shareTitle)) {
            ToastUtil.showShort(mActivity, "分享失败，内容为空");
        } else {
            if ("QRCodeActivity".equals(shareType)) {
                new ShareAction(mActivity)
                        .withMedia(imagelocal)
                        .setPlatform(platform)
                        .setCallback(shareListener)
                        .share();
            } else if ("MyWanzhuanActivity".equals(shareType) && platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
                new ShareAction(mActivity)
                        .withText(shareContent)
                        .withMedia(web)
                        .setPlatform(platform)
                        .setCallback(shareListener)
                        .share();
            } else {
                new ShareAction(mActivity)
                        .withText(shareContent)
                        .withMedia(web)
                        .setPlatform(platform)
                        .setCallback(shareListener)
                        .share();
            }
        }
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    private boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    private static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                LogUtil.e("pn = " + pn);
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    // Umeng 分享的回掉
    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
//            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
//            SocializeUtils.safeCloseDialog(dialog);
            ToastUtil.showShort(mActivity, "分享成功了");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            SocializeUtils.safeCloseDialog(dialog);
            ToastUtil.showShort(mActivity, "分享失败了");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            SocializeUtils.safeCloseDialog(dialog);
            ToastUtil.showShort(mActivity, "分享取消了");
        }
    };
}
