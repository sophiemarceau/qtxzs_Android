package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.requestjson.GetReportLockTimeRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.view.AddCustomView;
import com.wecoo.qutianxia.view.AddCustomView.OnAddCustomListener;

/**
 * Created by mwl on 2016/10/25.
 * 客户信息/ 添加客户
 */

public class CustomInfoActivity extends TitleBarActivity implements OnAddCustomListener {

    private final String mPageName = "CustomInfoActivity";
    private Context mContext = CustomInfoActivity.this;
    public static String TITLEBAR = "titlebar",CUSTOMER_ID = "customer_id"/*,ISLock = "isLock"*/;
    private String strIntent = "", customer_id = ""/*,isLock = ""*/;
    private LinearLayout linearLayout;
    private AddCustomView addCustomView;
    // 报备锁定天数
    private int reportLockTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custominfo_layout);
        AppManager.getAppManager().addActivity(this);

        strIntent = getIntent().getStringExtra(TITLEBAR);
        customer_id = getIntent().getStringExtra(CUSTOMER_ID);
//        isLock = getIntent().getStringExtra(ISLock);

        initActionBar(this);

        initView();

        getReportLockTime();
    }

    private void initView() {
        setBanner(Left, strIntent, None);
        linearLayout = (LinearLayout) findViewById(R.id.Report_layout_custominfo);
        addCustomView = new AddCustomView(mContext);
        addCustomView.setCustomInfoData(customer_id);
        addCustomView.setData(application.IndustryList, application.BudgetList, application.PlanTimeList);
        if (getString(R.string.add_customer).equals(strIntent)) {
            addCustomView.setStatus(addCustomView.addCustom);
        } else if (getString(R.string.custominfo).equals(strIntent)) {
            addCustomView.setStatus(addCustomView.customInfo);
        }
        linearLayout.addView(addCustomView);
        addCustomView.setAddCustomListener(this);
    }

    @Override
    public void addCustomStatus(Object obj) {
        String result = (String) obj;
        if (!TextUtils.isEmpty(result)) {
            this.finish();
        }
    }
    // 获取报备锁定天数
    private void getReportLockTime() {
        GetReportLockTimeRequest lockTimeRequest = new GetReportLockTimeRequest();
        lockTimeRequest.setReturnDataClick(mContext, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                if (obj != null) {
                    reportLockTime = (int) obj;
                }
            }
        });
    }
    @Override
    public void addReportStatus(Object obj) {
        String result = (String) obj;
        if (!TextUtils.isEmpty(result)) {
            Intent intent = new Intent(mContext, CommitSuccessActivity.class);
            intent.putExtra(CommitSuccessActivity.TYPE, Constants.ADDREPORT);
            intent.putExtra(CommitSuccessActivity.DAYCOUNT, reportLockTime);
            intent.putExtra(CommitSuccessActivity.TITLE, getString(R.string.reportcustom));
            startActivity(intent);
            this.finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallServer.getInstance().cancelBySign(mContext);
    }
}
