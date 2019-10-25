package com.wecoo.qutianxia.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.manager.AppManager;

/**
 * Created by mwl on 2016/10/25.
 * 各种提交成功
 */

public class CommitSuccessActivity extends TitleBarActivity implements View.OnClickListener {

    private final String mPageName = "CommitSuccessActivity";
    public static String TITLE = "title";
    public static String TYPE = "type";
    public static String DAYCOUNT = "dayCount";//天数

    private int type = 0;
    private String title = "";

    private ImageView imgIcon;
    private TextView txtSuccess, txtContent;
    private Button btnKnow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitsuccess_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);

        type = getIntent().getIntExtra(TYPE, 0);
        title = getIntent().getStringExtra(TITLE);
        initView();
    }

    // 初始化View
    private void initView() {
        setBanner(Left, title, None);
        // 获取控件
        imgIcon = (ImageView) findViewById(R.id.commitsuccess_img_Icon);
        txtSuccess = (TextView) findViewById(R.id.commitsuccess_txt_success);
        txtContent = (TextView) findViewById(R.id.commitsuccess_txt_content);
        btnKnow = (Button) findViewById(R.id.commitsuccess_btn_Know);
        btnKnow.setOnClickListener(this);
        // 控制不同的view显示
        viewShow();
    }

    // View显示
    private void viewShow() {
        switch (type) {
            case Constants.WITHDRAWALS:
                txtContent.setText("我们将在1-3个工作日内完成打款 \n具体参见各银行入账时间");
                break;
            case Constants.ADDREPORT:
                StringBuilder builder = new StringBuilder();
                builder.append("该客户审核确认后，您将获得相应赏金 \n");
                builder.append("相关进展可在“已推荐客户”中查看 \n");
                builder.append(getIntent().getIntExtra(DAYCOUNT, 0));
                builder.append("天内，您的客户将被锁定，不可再次报备给其他项目，达到期限或被退回后，自动解锁 \n");
                builder.append("协助成交，更有额外千元赏金 \n");
                txtContent.setText(builder.toString());
                break;
            case Constants.COMPLAINT:
                txtContent.setText("我们将在5个工作日内与您联系");
                break;
//            case Constants.SETPASS:
//                txtSuccess.setText("提交成功");
//                break;
            case Constants.RESETPWD:
                txtSuccess.setText("重置成功");
                break;
            case Constants.SignUpforMoney:
                txtContent.setVisibility(View.GONE);
                txtSuccess.setText("签约成功");
                break;
            case Constants.ExamineProjectSuccess:
                txtContent.setVisibility(View.GONE);
                txtSuccess.setText("操作成功");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.commitsuccess_btn_Know) {
            finish();
        }
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
