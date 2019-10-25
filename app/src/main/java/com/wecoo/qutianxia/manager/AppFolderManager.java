package com.wecoo.qutianxia.manager;

import com.wecoo.qutianxia.utils.SDCardUtil;

import java.io.File;

/**
 * Created by mwl on 2016/11/1.
 * wecoo 文件夹管理
 */

public class AppFolderManager {

    private File mSdcardPath = null;   // Sd卡路径
    private File mGlideCacheDir = null; // Glide图片缓存的文件夹
    private File mTempDir = null;   // 拍照缓存的文件夹
    private File mApkCacheDir = null;  // 下载apk缓存的文件夹
    private File crashCacheDir = null;   // 崩溃日志缓存的文件夹

    private static AppFolderManager folderManager = null;

    private final String WECOO_FILE_PATH = "wecoo_files";
    public static final String IMAGE_CACHE_PATH = "/image_manager_disk_cache";
    public static final String WEBVIEW_CACHE_PATH = "/webviewCache";
//    private final String NEW_ROOT_PATH = "Android/data/com.wecoo.qutianxia/files";

    public static AppFolderManager getInstance() {
        if (folderManager == null) {
            folderManager = new AppFolderManager();
        }
        return folderManager;
    }

    public AppFolderManager() {
        if ((mSdcardPath == null) && SDCardUtil.isSDCardEnable()) {
            mSdcardPath = new File(SDCardUtil.getSDCardPath(), WECOO_FILE_PATH);
            if (!mSdcardPath.exists()) {
                mSdcardPath.mkdir();
            }
        }
    }

    public File getSdcardFile() {
        return mSdcardPath;
    }

    public String getGlideCacheFolder() {
        if (mSdcardPath != null) {
            if (mGlideCacheDir == null) {
                mGlideCacheDir = new File(mSdcardPath, File.separator + "glideCache");
            }
            if (!mGlideCacheDir.exists()) {
                mGlideCacheDir.mkdirs();
            }
        }
        return (mGlideCacheDir == null) ? null : mGlideCacheDir.getAbsolutePath();
    }

    public String getTempFolder() {
        if (mSdcardPath != null) {
            if (mTempDir == null) {
                mTempDir = new File(mSdcardPath, File.separator + "cameraTemp");
            }
            if (!mTempDir.exists()) {
                mTempDir.mkdirs();
            }
        }
        return (mTempDir == null) ? null : mTempDir.getAbsolutePath();
    }

    public String getCrashFolder() {
        if (mSdcardPath != null) {
            if (crashCacheDir == null) {
                crashCacheDir = new File(mSdcardPath, File.separator + "crashLog");
            }
            if (!crashCacheDir.exists()) {
                crashCacheDir.mkdirs();
            }
        }
        return (crashCacheDir == null) ? null : crashCacheDir.getAbsolutePath();
    }

    public String getApkCacheFolder() {
        if (mSdcardPath != null) {
            if (mApkCacheDir == null) {
                mApkCacheDir = new File(mSdcardPath, File.separator + "apkCache");
            }
            if (!mApkCacheDir.exists()) {
                mApkCacheDir.mkdirs();
            }
        }
        return (mApkCacheDir == null) ? null : mApkCacheDir.getAbsolutePath();
    }

}
