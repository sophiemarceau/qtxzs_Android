package com.wecoo.qutianxia.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * Created by mwl on 2017/1/10.
 * 二维码工具类
 */
public class QRCodeUtils {

    private static QRCodeUtils qrInstance = null;
    private static int IMAGE_HALFWIDTH = 50;//宽度值，影响中间图片大小

    public static QRCodeUtils getInstance() {
        if (qrInstance == null) {
            qrInstance = new QRCodeUtils();
        }
        return qrInstance;
    }

    /**
     * 生成二维码
     *
     * @param text            需要生成二维码的文字、网址等
     * @param size            需要生成二维码的大小
     * @param colorCode       二维码颜色值(如:0xff000000)
     * @param colorBackground 二维码背景颜色值(如:0xffffffff)
     * @return bitmap
     */
    public Bitmap createQRCode(String text, int size, int colorCode, int colorBackground) {
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (bitMatrix.get(x, y)) {
//                        pixels[y * size + x] = 0xff000000;
                        pixels[y * size + x] = colorCode;
                    } else {
//                        pixels[y * size + x] = 0xffffffff;
                        pixels[y * size + x] = colorBackground;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成带logo的二维码，logo默认为二维码的1/5
     *
     * @param text            需要生成二维码的文字、网址等
     * @param size            需要生成二维码的大小（）
     * @param mBitmap         logo文件
     * @param colorCode       二维码颜色值(如:0xff000000)
     * @param colorBackground 二维码背景颜色值(如:0xffffffff)
     * @return bitmap 带logo的二维码
     */
    public Bitmap createQRCodeWithLogo(String text, int size, Bitmap mBitmap, int colorCode, int colorBackground) {
        try {
            IMAGE_HALFWIDTH = size / 10;
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            /*
             * 设置容错级别，默认为ErrorCorrectionLevel.L
             * 因为中间加入logo所以建议你把容错级别调至H,否则可能会出现识别不了
             */
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, size, size, hints);

            int width = bitMatrix.getWidth();//矩阵高度
            int height = bitMatrix.getHeight();//矩阵宽度
            int halfW = width / 2;
            int halfH = height / 2;

            Matrix m = new Matrix();
            float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
            float sy = (float) 2 * IMAGE_HALFWIDTH
                    / mBitmap.getHeight();
            m.setScale(sx, sy);
            //设置缩放信息
            //将logo图片按martix设置的信息缩放
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                    mBitmap.getWidth(), mBitmap.getHeight(), m, false);

            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                            && y > halfH - IMAGE_HALFWIDTH
                            && y < halfH + IMAGE_HALFWIDTH) {
                        //该位置用于存放图片信息
                        //记录图片每个像素信息
                        pixels[y * width + x] = mBitmap.getPixel(x - halfW
                                + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                    } else {
                        if (bitMatrix.get(x, y)) {
//                            pixels[y * size + x] = 0xff000000;
                            pixels[y * size + x] = colorCode;
                        } else {
//                            pixels[y * size + x] = 0xffffffff;
                            pixels[y * size + x] = colorBackground;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
