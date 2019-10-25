package com.wecoo.qutianxia.activity.enterprise;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.listener.NoDoubleClickListener;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.manager.HandlerManager;
import com.wecoo.qutianxia.requestjson.AddFollowUpRecordRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.utils.ToastUtil;

/**
 * Created by wecoo on 2017/6/2.
 * 添加沟通记录
 */

public class AddRecordActivity extends TitleBarActivity {

    private final String mPageName = "AddRecordActivity";
    private EditText editRemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine_project_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, "添加沟通记录", None);

        initView();
    }

    private void initView() {
        editRemark = (EditText) findViewById(R.id.examine_edit_addRemark);
        Button btnCommit = (Button) findViewById(R.id.examine_btn_commit);
        //
        editRemark.setHint("请填写本次沟通内容，不超过200字");
        editRemark.setVisibility(View.VISIBLE);
        btnCommit.setOnClickListener(clickListener);
    }

    // 提交
    private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            commitData();
        }
    };

    // 提交数据
    private void commitData() {
        String report_id = getIntent().getStringExtra("report_id");
        String strRemark = editRemark.getText().toString();
        if (TextUtils.isEmpty(strRemark)) {
            ToastUtil.showShort(this, "请填写备注");
            return;
        }
        AddFollowUpRecordRequest recordRequest = new AddFollowUpRecordRequest(WebUrl.addCustomerReportLog);
        recordRequest.setRequestParms(report_id, strRemark);
        recordRequest.setReturnDataClick(AddRecordActivity.this, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                ToastUtil.showShort(AddRecordActivity.this, "添加记录成功");
                HandlerManager.getHandlerDelayed().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 300);
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
