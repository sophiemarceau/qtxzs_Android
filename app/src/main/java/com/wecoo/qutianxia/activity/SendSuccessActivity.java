package com.wecoo.qutianxia.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.manager.AppManager;

/**
 * Created by mwl on 2017/2/17.
 * 提醒发送成功
 */

public class SendSuccessActivity extends TitleBarActivity {

    private final String mPageName = "SendSuccessActivity";
    private TextView txtSuccess;
    private Button btnKnow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitsuccess_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.send_reminder), None);

        initView();
    }

    // 初始化View
    private void initView() {
        txtSuccess = (TextView) findViewById(R.id.commitsuccess_txt_success);
        txtSuccess.setText("发送成功");
        btnKnow = (Button) findViewById(R.id.commitsuccess_btn_Know);
        btnKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
