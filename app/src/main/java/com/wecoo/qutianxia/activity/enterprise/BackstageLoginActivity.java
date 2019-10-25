package com.wecoo.qutianxia.activity.enterprise;

import android.os.Bundle;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.utils.AppInfoUtil;

/**
 * Created by wecoo on 2017/6/2.
 * 登录企业后台网页版
 */

public class BackstageLoginActivity extends TitleBarActivity {

    private final String mPageName = "BackstageLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backstage_login_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.backstageLogin), None);

        findViewById(R.id.BackStageLogin_txt_CustomService)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppInfoUtil.onCallPhone(BackstageLoginActivity.this, "400-900-1135");
                    }
                });
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
}
