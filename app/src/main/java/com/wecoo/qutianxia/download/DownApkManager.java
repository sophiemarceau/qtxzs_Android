package com.wecoo.qutianxia.download;

import android.app.Activity;
import android.text.TextUtils;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.manager.AppFolderManager;
import com.wecoo.qutianxia.models.AppInfoEntity;
import com.wecoo.qutianxia.requestjson.GetAppVersionRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.utils.AppInfoUtil;
import com.wecoo.qutianxia.utils.FileUtil;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadRequest;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.ServerError;
import com.yolanda.nohttp.error.StorageReadWriteError;
import com.yolanda.nohttp.error.StorageSpaceNotEnoughError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;

import java.io.File;
import java.util.Locale;

/**
 * Created by mwl on 2016/12/2.
 * 下载管理
 */

public class DownApkManager {

    //    private static DownApkManager instance = null;
    private Activity mContext;
    private String appFolder;
    private String apkName = "wecoo_qtxzs_App.apk";
    // 下载请求.
    private DownloadRequest mDownloadRequest;
    //
    private DownloadProgrssBar downloadProgrssBar;

//    public static DownApkManager getInstance(Activity mContext) {
//        if (instance == null) {
//            instance = new DownApkManager(mContext);
//        }
//        return instance;
//    }

    public DownApkManager(Activity mContext) {
        this.mContext = mContext;
        appFolder = AppFolderManager.getInstance().getApkCacheFolder();
    }

    // 检查升级
    public void checkUpdataApp(final boolean isToast) {
        String versionName = AppInfoUtil.getInstance().getVersionName(mContext);
        GetAppVersionRequest versionRequest = new GetAppVersionRequest();
        versionRequest.setRequestParms(versionName);
        versionRequest.setReturnDataClick(mContext, 1, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                AppInfoEntity appEntity = (AppInfoEntity) obj;
                if (appEntity != null) {
                    final String appDownUrl = appEntity.getUrl();
                    int status = appEntity.getResult();
                    final String version = appEntity.getVersion();
                    String content = appEntity.getUpdate_content();
                    if (status == 0) {
                        if (isToast) {
                            ToastUtil.showShort(mContext, "当前已是最新版本");
                        }
                    } else {
                        if (downloadProgrssBar == null) {
                            downloadProgrssBar = new DownloadProgrssBar(mContext);
                        }
                        downloadProgrssBar.setDialogStatus(0);
                        downloadProgrssBar.setDownType(status);
                        downloadProgrssBar.setTxtMessage(mContext.getString(R.string.updateapp) + version);
                        if (!TextUtils.isEmpty(content)) {
                            downloadProgrssBar.setTxtUpdateMessage(content);
                        }
                        downloadProgrssBar.show();
                        downloadProgrssBar.setLoadListener(new DownloadProgrssBar.DownLoadListener() {
                            @Override
                            public void OnSureClick() {
//                                downloadProgrssBar.dismiss();
                                if (TextUtils.isEmpty(appDownUrl)) {
                                    ToastUtil.showShort(mContext, mContext.getString(R.string.download_error_url));
                                } else {
                                    startDownload(appDownUrl, version);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 开始下载。
     */
    private void startDownload(String apkdownUrl, String version) {
        if (downloadProgrssBar == null) {
            downloadProgrssBar = new DownloadProgrssBar(mContext);
        }
        downloadProgrssBar.setDialogStatus(1);
        // 开始下载
        if (mDownloadRequest == null || mDownloadRequest.isFinished()) {// 没有开始或者下载完成了，就重新下载。
            /**
             * 这里不传文件名称、不断点续传，则会从响应头中读取文件名自动命名，如果响应头中没有则会从url中截取。
             * 如果使用断点续传的话，一定要指定文件名
             */
            if (!TextUtils.isEmpty(version)) {
                apkName = "wecoo_qtxzs_" + version + "_App.apk";
            }
            // 判断本地是否下载过
            File apkFile = new File(appFolder + File.separator + apkName);
            if (apkFile.exists()) {
                AppInfoUtil.getInstance().instanceApk(mContext, apkFile);
            } else {
                // url 下载地址。
                // fileFolder 保存的文件夹。
                // fileName 文件名。
                // isRange 是否断点续传下载。
                // isDeleteOld 如果发现存在同名文件，是否删除后重新下载，如果不删除，则直接下载成功。
                mDownloadRequest = NoHttp.createDownloadRequest(apkdownUrl, appFolder, apkName, true, true);

                // what 区分下载。
                // downloadRequest 下载请求对象。
                // downloadListener 下载监听。
                CallServer.getDownloadInstance().add(0, mDownloadRequest, downloadListener);
            }

        }
    }

    /**
     * 下载监听的回调
     **/
    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onDownloadError(int what, Exception exception) {
            Logger.e("onDownloadError: ");
            SPUtils.put(mContext, DownloadConfig.downStatus, DownloadConfig.downError);
            String message = mContext.getString(R.string.download_error);
            String messageContent;
            if (exception instanceof ServerError) {
                messageContent = mContext.getString(R.string.download_error_server);
            } else if (exception instanceof NetworkError) {
                messageContent = mContext.getString(R.string.download_error_network);
            } else if (exception instanceof StorageReadWriteError) {
                messageContent = mContext.getString(R.string.download_error_storage);
            } else if (exception instanceof StorageSpaceNotEnoughError) {
                messageContent = mContext.getString(R.string.download_error_space);
            } else if (exception instanceof TimeoutError) {
                messageContent = mContext.getString(R.string.download_error_timeout);
            } else if (exception instanceof UnKnownHostError) {
                messageContent = mContext.getString(R.string.download_error_un_know_host);
            } else if (exception instanceof URLError) {
                messageContent = mContext.getString(R.string.download_error_url);
            } else {
                messageContent = mContext.getString(R.string.download_error_un);
            }
            message = String.format(Locale.getDefault(), message, messageContent);
            downloadProgrssBar.setTxtStatus(message);
        }

        @Override
        public void onStart(int what, boolean isResume, long beforeLength, Headers headers, long allCount) {
            Logger.e("onDownloadStart: " + "beforeLength = " + beforeLength + "allCount = " + allCount);
            SPUtils.put(mContext, DownloadConfig.downStatus, DownloadConfig.downStart);
            SPUtils.put(mContext, DownloadConfig.apkAllSize, allCount);
            int progress = 0;
            if (allCount != 0) {
                progress = (int) (beforeLength * 100 / allCount);
                downloadProgrssBar.setProgress(progress);
                updateProgress(beforeLength);
            }
        }

        @Override
        public void onProgress(int what, int progress, long fileCount) {
            Logger.e("onDownloadonProgress: " + fileCount);
            SPUtils.put(mContext, DownloadConfig.downProgress, progress);
            SPUtils.put(mContext, DownloadConfig.downSize, fileCount);
            downloadProgrssBar.setTxtStatus(mContext.getString(R.string.downloading));
            updateProgress(fileCount);
            downloadProgrssBar.setProgress(progress);
        }

        @Override
        public void onFinish(int what, String filePath) {
            Logger.d("Download finish, file path: " + filePath);
            SPUtils.put(mContext, DownloadConfig.downStatus, DownloadConfig.downFinish);
            downloadProgrssBar.setTxtStatus(mContext.getString(R.string.download_status_finish));
            downloadProgrssBar.dismiss();
            File apkFile = new File(appFolder + File.separator + apkName);
            AppInfoUtil.getInstance().instanceApk(mContext, apkFile);
        }

        @Override
        public void onCancel(int what) {
            Logger.d("DownloadonCancel: ");
            SPUtils.put(mContext, DownloadConfig.downStatus, DownloadConfig.downCancel);
            downloadProgrssBar.setTxtStatus(mContext.getString(R.string.download_status_be_pause));
        }
    };

    //更新下载状态
    private void updateProgress(long downSize) {
        long apkCount = (long) SPUtils.get(mContext, DownloadConfig.apkAllSize, 0L);
        String sProgress = mContext.getString(R.string.download_progress) + FileUtil.getInstance().formatFileSize(downSize);
        if (apkCount != 0) {
            sProgress += " / " + FileUtil.getInstance().formatFileSize(apkCount);
        }
        downloadProgrssBar.setTxtSize(sProgress);
    }

    // 开始下载了，但是任务没有完成，代表正在下载，那么暂停下载。
    private void cancelDownload() {
        if (mDownloadRequest != null && mDownloadRequest.isStarted() && !mDownloadRequest.isFinished()) {
            // 暂停下载。
            mDownloadRequest.cancel();
        }
    }

    // 销毁下载
    public void onDestroy() {
        cancelDownload();
    }
}
