package com.wecoo.qutianxia.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.utils.BitmapUtils;
import com.wecoo.qutianxia.utils.LogUtil;
import com.wecoo.qutianxia.view.GlideCircleTransform;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mwl on 2016/10/24.
 * 图片管理
 */

public class ImageManager {

    private Context mContext;
    private static String JPEN_TYPE = ".png";
    // 裁剪完成后保存的照片
    public static final String SAVE_PATH = "user_save" + JPEN_TYPE;
    // 拍照
    public static final String CAMERA_PIC = "camera_pic" + JPEN_TYPE;
    // 截屏保存的图片
    public static final String SCREENSHOT_PATH = "screenShot_pic" + JPEN_TYPE;
    // 保存生成的二维码
    public static final String QRCODE_PATH = "qrcode_pic" + JPEN_TYPE;
    // 项目资源
//    private static final String ANDROID_RESOURCE = "android.resource://";

    public ImageManager(Context context) {
        this.mContext = context;
    }

    // 将资源ID转为Uri
//    private Uri resourceIdToUri(int resourceId) {
//        return Uri.parse(ANDROID_RESOURCE + mContext.getPackageName() + File.separator + resourceId);
//    }

    // 加载网络图片
    public void loadUrlImage(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.default_picture_bg)
                .error(R.drawable.default_picture_bg)
                .crossFade()
                .priority(Priority.NORMAL)
//                .skipMemoryCache(true) //跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
//              DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像
//              DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
//              DiskCacheStrategy.NONE 什么都不缓存
//              .centerCrop()
                .into(imageView);
    }

    // 加载网络图片(自定义默认图)
    public void loadUrlImage(String url, ImageView imageView, int imgId) {
        Glide.with(mContext)
                .load(url)
                .placeholder(imgId)
                .error(imgId)
                .crossFade()
//                .skipMemoryCache(true) //跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    // 加载图片（不缓存）
    public void loadUrlImageNoCaChe(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .placeholder(R.color.default_pics)
                .error(R.color.default_pics)
                .crossFade()
                .skipMemoryCache(true) //跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    // 加载本地图片
    public void loadLocalImage(File file, ImageView imageView) {
        Glide.with(mContext)
                .load(file)
                .placeholder(R.color.default_pics)
                .error(R.color.default_pics)
                .crossFade()
                .skipMemoryCache(true) //跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    // 加载网络圆型图片
    public void loadCircleImage(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.default_photo)
                .error(R.drawable.default_photo)
                .crossFade()
                .skipMemoryCache(true) //跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideCircleTransform(mContext))
                .into(imageView);
    }

    // 加载本地圆型图片
    public void loadCircleLocalImage(File file, ImageView imageView) {
        Glide.with(mContext)
                .load(file)
                .placeholder(R.color.default_pics)
                .error(R.color.default_pics)
                .crossFade()
                .skipMemoryCache(true) // 跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)// 跳过磁盘缓存
                .transform(new GlideCircleTransform(mContext))
                .into(imageView);
    }

    public interface BitmapToFileListener {
        void onBitmapToFile(File file);
    }

    /**
     * 把View绘制到Bitmap上
     *
     * @param comBitmap 需要绘制的View
     * @param width     该View的宽度
     * @param height    该View的高度
     * @return 返回Bitmap对象
     */
    public void getViewBitmap(View comBitmap, int width, int height, BitmapToFileListener listener) {
        if (comBitmap != null) {
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            c.drawColor(Color.WHITE);
            /** 如果不设置canvas画布为白色，则生成透明 */
            comBitmap.draw(c);
            Bitmap Zoombitmap = BitmapUtils.getZoomImage(bitmap, width / 2, height / 2);
            if (Zoombitmap != null) {
                listener.onBitmapToFile(saveBitmapFile(Zoombitmap, SCREENSHOT_PATH));
            }
        }
    }

    /**
     * Bitmap对象保存图片文件
     * bitmap ： 对象
     * fileName ： 指定文件名称
     * compress ： 压缩率
     */

    public static File saveBitmapFile(Bitmap bitmap, String fileName) {
        File newFile = new File(AppFolderManager.getInstance().getTempFolder(), fileName);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile));
            //compress 是压缩率，表示压缩compress%; 如果不压缩是100，表示压缩率为0
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.e("保存图片文件成功");
        return newFile;
    }


}
