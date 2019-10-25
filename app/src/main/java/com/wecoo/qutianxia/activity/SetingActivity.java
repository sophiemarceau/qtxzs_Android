package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.download.DownApkManager;
import com.wecoo.qutianxia.listener.NoDoubleClickListener;
import com.wecoo.qutianxia.manager.AppFolderManager;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.manager.HandlerManager;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.utils.FileUtil;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.view.DialogView;
import com.wecoo.qutianxia.view.DialogView.DialogCallback;

import java.io.File;

/**
 * Created by mwl on 2016/10/25.
 * 设置
 */

public class SetingActivity extends TitleBarActivity {

    private final String mPageName = "SetingActivity";
    private Context mContext;
    private TextView txtAccount, txtClean, txtSize, txtExit, txtCheckApp;
    private DownApkManager downApkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting_layout);
        AppManager.getAppManager().addActivity(this);

        mContext = SetingActivity.this;

        initActionBar(this);
        setBanner(Left, getString(R.string.my_set), None);

        initView();
        cleanCache();

        MobclickAgent.onEvent(SetingActivity.this, "SetingOnclick");
    }

    private void initView() {
        txtAccount = (TextView) findViewById(R.id.accountSet_txt_reset);
        txtClean = (TextView) findViewById(R.id.setting_txt_cacheClean);
        txtSize = (TextView) findViewById(R.id.setting_txt_cacheSize);
        txtExit = (TextView) findViewById(R.id.setting_txt_exitlogin);
        txtCheckApp = (TextView) findViewById(R.id.Setting_txt_checkApp);
        FrameLayout flCache = (FrameLayout) findViewById(R.id.setting_fl_cache);

        //
        assert flCache != null;
        flCache.setVisibility(View.VISIBLE);
        txtExit.setVisibility(View.VISIBLE);
        txtCheckApp.setVisibility(View.VISIBLE);
        // 添加监听
        txtAccount.setOnClickListener(clickListener);
        txtClean.setOnClickListener(clickListener);
        txtExit.setOnClickListener(clickListener);
        txtCheckApp.setOnClickListener(clickListener);
    }

    private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View view) {
            switch (view.getId()) {
                case R.id.accountSet_txt_reset:
                    openActivity(mContext, AccountSecurityActivity.class);
                    break;
                case R.id.setting_txt_cacheClean:
                    MobclickAgent.onEvent(mContext, "cacheCleanOnlick");
                    if ("0B".equals(txtSize.getText().toString().trim())) return;
                    new DialogView(mContext).createDialog("确认清除缓存吗？", false, new DialogCallback() {
                        @Override
                        public void onSureClick() {
                            try {
                                boolean cleanSdcardFlag = FileUtil.getInstance().deleteFolder(SdcardFile.toString());
                                boolean cleanimageCacheDirFlag = FileUtil.getInstance().deleteFolder(imageCacheDir.toString());
                                boolean cleanwvCacheDirFlag = FileUtil.getInstance().deleteFolder(wvCacheDir.toString());
                                if (cleanSdcardFlag || cleanimageCacheDirFlag || cleanwvCacheDirFlag) {
                                    txtSize.setText(getCachedSizeStr());
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    break;
                case R.id.setting_txt_exitlogin:
                    MobclickAgent.onEvent(mContext, "exitloginOnlick");
                    new DialogView(mContext).createDialog("确认退出当前账号吗？", false, new DialogCallback() {
                        @Override
                        public void onSureClick() {
                            ModelManager.getInstance().loginOutApp(mContext);
                            SPUtils.put(mContext, Configs.qtx_auth, "");
                            SPUtils.put(mContext, Configs.user_id, "");
                            SPUtils.put(mContext, Configs.user_Tel, "");
                            startActivity(new Intent(mContext, LoginActivity.class));
                            finish();
                        }
                    });
                    break;
                case R.id.Setting_txt_checkApp:
                    checkUpdataApp();
                    break;
            }
        }
    };

    // 检查升级
    private void checkUpdataApp() {
        downApkManager = new DownApkManager(SetingActivity.this);
        downApkManager.checkUpdataApp(true);
    }

    // 清楚缓存
    private void cleanCache() {
        HandlerManager.getHandlerThread().post(new Runnable() {
            @Override
            public void run() {
                String cacheSize = getCachedSizeStr();
                if (cacheSize.startsWith(".00")) {
                    txtSize.setText("0B");
                } else {
                    txtSize.setText(cacheSize);
                }
            }
        });
    }

    // sd卡缓存文件  图片缓存    webview缓存
    private File SdcardFile, imageCacheDir, wvCacheDir;

    /**
     * `
     * 得到缓存大小
     */
    private String getCachedSizeStr() {
        String foldersize = "0B";
        try {
            SdcardFile = AppFolderManager.getInstance().getSdcardFile();
            long fileSize = 0;
            if (SdcardFile != null && SdcardFile.exists()) {
                fileSize += FileUtil.getInstance().getFolderSize(SdcardFile);
            }
            // Glide 默认缓存位置
            imageCacheDir = new File(getCacheDir().getAbsolutePath() + AppFolderManager.IMAGE_CACHE_PATH);
            if (imageCacheDir.exists()) {
                fileSize += FileUtil.getInstance().getFolderSize(imageCacheDir);
            }
            // WebView 默认缓存位置
            wvCacheDir = new File(getCacheDir().getAbsolutePath() + AppFolderManager.WEBVIEW_CACHE_PATH);
            if (wvCacheDir.exists()) {
                fileSize += FileUtil.getInstance().getFolderSize(wvCacheDir);
            }
            foldersize = FileUtil.getInstance().formatFileSize(fileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foldersize;
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
        if (downApkManager != null) {
            downApkManager.onDestroy();
        }
    }
}
