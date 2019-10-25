package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.requestset.CallServer;

/**
 * Created by mwl on 2017/2/17.
 * 我的邀请和我的人脉发提醒
 */

public class SendReminderActivity extends TitleBarActivity implements View.OnClickListener {

    private final String mPageName = "SendReminderActivity";
    private Context mContext = SendReminderActivity.this;
    private TextView txtContent;
    private EditText editContent;
    private Button btnnCommit;
    private boolean isTxtClick; // 文案是否可点击

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_reminder_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.send_reminder), None);
        isTxtClick = getIntent().getBooleanExtra("isTxtClick", false);

        initView();
    }

    private void initView() {
        txtContent = (TextView) findViewById(R.id.sendRemind_txt_content);
        editContent = (EditText) findViewById(R.id.sendRemind_edit_content);
        btnnCommit = (Button) findViewById(R.id.sendRemind_butn_send);

        // 添加监听
        if (isTxtClick) {
            txtContent.setOnClickListener(this);
        } else {
            getContent();
        }
        btnnCommit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendRemind_txt_content:

                break;
            case R.id.sendRemind_butn_send:
                sendContent();
                break;
        }
    }

    // 获取一句话描述
    private void getContent() {

    }

    // 发送提醒
    private void sendContent() {
        openActivity(mContext,SendSuccessActivity.class);
        finish();
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
