package com.wecoo.qutianxia.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wecoo on 2016/11/1.
 * 图片处理的工具类
 */
public class BitmapUtils {
    /**
     * 用option.sampleSize进行解码加载图片。<br>
     * 首先会读取图片的大小，然后根据minWidth和minHeight，计算缩放级别，在不失真的情况下进行缩放
     *
     * @param path          图片的路径
     * @param minWidth      最小宽度，为0时返回原图
     * @param minHeight     最小高度，为0时返回原图
     * @param channelConfig 图片的加载质量，用RGB_565会节省加载需要的内存，是ARGB_8888的1/2内存使用量。设置成null则按照默认加载
     */
    public static Bitmap decode(String path, final int minWidth,
                                final int minHeight, Bitmap.Config channelConfig) {
        // 长和宽一个是0，则返回原图
        if (minWidth * minHeight <= 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (channelConfig != null) {
                options.inPreferredConfig = channelConfig;
            }
            return BitmapFactory.decodeFile(path, options);
        }

        // 读取图片的宽高
        BitmapFactory.Options options = getBitmapOption(path);
        // 计算SampleSize
        options.inSampleSize = (int) calculateInSampleSize(options, minWidth,
                minHeight);

        // 用SampleSize进行解码
        if (channelConfig != null) {
            options.inPreferredConfig = channelConfig;
        }

        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length,
                                         Bitmap.Config channelConfig, int minWidth, int minHeight) {
        // 长和宽一个是0，则返回原图
        if (minWidth * minHeight <= 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (channelConfig != null) {
                options.inPreferredConfig = channelConfig;
            }
            return BitmapFactory.decodeByteArray(data, offset, length, options);
        }

        // 读取图片的宽高
        BitmapFactory.Options options = getBitmapOption(data, offset, length);
        // 计算SampleSize
        options.inSampleSize = (int) calculateInSampleSize(options, minWidth,
                minHeight);
        // 用SampleSize进行解码
        if (channelConfig != null) {
            options.inPreferredConfig = channelConfig;
        }

        return BitmapFactory.decodeByteArray(data, offset, length, options);
    }

    /**
     * 用于获取图片参数，inJustDecodeBounds=false不需要重新设置
     */
    private static BitmapFactory.Options getBitmapOption(byte[] data,
                                                         int offset, int length) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, length, options);
        options.inJustDecodeBounds = false;

        return options;
    }

    /**
     * 用于获取图片参数，inJustDecodeBounds=false不需要重新设置
     */
    private static BitmapFactory.Options getBitmapOption(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;

        return options;
    }

    private static float calculateInSampleSize(BitmapFactory.Options options,
                                               final int reqWidth, final int reqHeight) {
        // 读取图片的宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        float inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // 计算宽高的压缩率
            final float heightRatio = (float) height / (float) reqHeight;
            final float widthRatio = (float) width / (float) reqWidth;

            // 取压缩率较小的作为图片的压缩略，inSampleSize >= 1
            inSampleSize = Math.max(1f, heightRatio < widthRatio ? heightRatio
                    : widthRatio);
        }
        return inSampleSize;
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 对Bitmap图片做反转90度
     *
     * @param img
     * @return
     */
    public static Bitmap toRotate90(Bitmap img, int rotate) {
        Matrix matrix = new Matrix();
        matrix.postRotate(+rotate); /*翻转90度*/
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }

    /**
     * 图片的缩放方法
     *
     * @param bitmap  ：源图片资源
     * @param maxSize ：图片允许最大空间  单位:KB
     * @return
     */
    public static Bitmap getZoomImage(Bitmap bitmap, double maxSize) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        // 单位：从 Byte 换算成 KB
        double currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间,如果大于则压缩,小于则不压缩
        while (currentSize > maxSize) {
            // 计算bitmap的大小是maxSize的多少倍
            double multiple = currentSize / maxSize;
            // 开始压缩：将宽带和高度压缩掉对应的平方根倍
            // 1.保持新的宽度和高度，与bitmap原来的宽高比率一致
            // 2.压缩后达到了最大大小对应的新bitmap，显示效果最好
            bitmap = getZoomImage(bitmap, bitmap.getWidth() / Math.sqrt(multiple), bitmap.getHeight() / Math.sqrt(multiple));
            currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        }
        return bitmap;
    }

    /**
     * bitmap转换成byte数组
     *
     * @param bitmap
     * @param needRecycle
     * @return
     */
    private static byte[] bitmapToByteArray(Bitmap bitmap, boolean needRecycle) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bitmap.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
        return result;
    }

    /**
     * 图片的缩放方法
     *
     * @param orgBitmap ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap getZoomImage(Bitmap orgBitmap, double newWidth, double newHeight) {
        if (null == orgBitmap) {
            return null;
        }
        if (orgBitmap.isRecycled()) {
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            return null;
        }

        // 获取图片的宽和高
        float width = orgBitmap.getWidth();
        float height = orgBitmap.getHeight();
        // 创建操作图片的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(orgBitmap, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }


    /**
     * 将图片按比例压缩成指定大小，替换指定路径图片，并将解码出的数据返回
     *
     * @param srcPath       源文件地址
     * @param dstPath       目标文件地址
     * @param minWidth      最小宽度
     * @param minHeight     最小高度
     * @param channelConfig 图片的加载质量，用RGB_565会节省加载需要的内存，是ARGB_8888的1/2内存使用量。设置成null则按照默认加载
     */
    public static Bitmap resizeBitmap(String srcPath, String dstPath,
                                      final int minWidth, final int minHeight, Bitmap.Config channelConfig) {
        Thread currentThread = Thread.currentThread();
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            return null;
        }

        if (currentThread.isInterrupted()) {
            return null;
        }
        // 读取图片的宽高
        BitmapFactory.Options options = getBitmapOption(srcPath);
        // 如果大小相同，则复制或者不做处理
        if (options.outHeight == minWidth && options.outHeight == minHeight) {
            if (!srcPath.equals(dstPath)) {
                try {
                    copyFile(srcPath, dstPath);
                } catch (IOException e) {
                }
            }
            return BitmapFactory.decodeFile(srcPath);
        }

        if (currentThread.isInterrupted()) {
            return null;
        }

        options.inSampleSize = (int) calculateInSampleSize(options, minWidth,
                minHeight);

        // 用SampleSize进行解码
        if (channelConfig != null) {
            options.inPreferredConfig = channelConfig;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options);

        if (currentThread.isInterrupted()) {
            return null;
        }

        // 计算压缩比例
        float ratio = calculateInSampleSize(options, minWidth, minHeight);
        // 压缩图片
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (options.outWidth / ratio),
                (int) (options.outHeight / ratio), true);
        bitmap.recycle();

        if (currentThread.isInterrupted()) {
            return null;
        }

        // 保存文件
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dstPath);
            final int QUALITY = 100;
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return resizedBitmap;
    }

    /**
     * 文件复制
     */
    private static void copyFile(String src, String dst) throws IOException {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(dst);
        Thread currentThread = Thread.currentThread();
        while (fis.read(buffer) != -1) {
            // 请求中断则停止拷贝
            if (currentThread.isInterrupted()) {
                break;
            }
            fos.write(buffer);
        }
        fos.flush();
        fos.close();
        fis.close();

        // 如果说被中断，则需要删除拷贝的目标文件
        if (currentThread.isInterrupted()) {
            new File(src).delete();
        }
    }

    /**
     * 获得类型为RGB565的BitmapFactory.Options
     */
    public static BitmapFactory.Options getRgb565Option() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return options;
    }

    public static void setImageViewBitmap(ImageView imageView, Bitmap bitmap) {
        imageView.setImageDrawable(null);
        imageView.setImageBitmap(bitmap);
        // Bitmap bitmap = ((BitmapDrawable)
        // imageView.getDrawable()).getBitmap();
        // imageView.setImageDrawable(drawable);
        // if((bitmap != null) && (!bitmap.isRecycled())){
        // // bitmap.recycle();
        // bitmap = null;
        // }
    }

    /**
     * 获取bitmap的大小
     *
     * @param bitmap
     * @return
     */
    @SuppressLint("NewApi")
    public static long getBitmapsize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }


    public static String getImageUriPath(Uri uri, Activity activity) {
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
//			Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
            Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String string = cursor.getString(column_index);
            cursor.close();

            return string;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean saveBitmap(Bitmap bitmap, File dstfile) {
        try {
            FileOutputStream out = new FileOutputStream(
                    dstfile.getAbsolutePath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            out = null;
            bitmap.recycle();
            bitmap = null;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本 和垃圾米手机 的 Uri转换
     *
     * @param imageUri
     * @param context
     * @return
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Uri imageUri, Activity context) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
