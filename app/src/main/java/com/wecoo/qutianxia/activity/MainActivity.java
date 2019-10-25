package com.wecoo.qutianxia.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.BaseActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.download.DownApkManager;
import com.wecoo.qutianxia.fragment.FindFragment;
import com.wecoo.qutianxia.fragment.HomeFragment;
import com.wecoo.qutianxia.fragment.MyFragment;
import com.wecoo.qutianxia.fragment.ProjectFragment;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.utils.AppInfoUtil;
import com.wecoo.qutianxia.utils.DoubleActionHelper;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.LoginOkDialogView;

import cn.jpush.android.api.JPushInterface;

/**
 * 主Activity  （包含4个模块   首页、悬赏、发现、我的）
 **/
public class MainActivity extends BaseActivity {

    private String texts[] = {"首页", "悬赏", "发现", "我的"};
    private int imageButton[] = {R.drawable.main_tab_rb_home, R.drawable.main_tab_rb_project, R.drawable.main_tab_rb_find, R.drawable.main_tab_rb_user};
    private Class fragmentArray[] = {HomeFragment.class, ProjectFragment.class, FindFragment.class, MyFragment.class};
    private static FragmentTabHost fragmentTabHost;
    //    private DownloadProgrssBar downloadProgrssBar;
    private LoginOkDialogView loginOkDialog = null;
    private DownApkManager downApkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        AppManager.getAppManager().addActivity(this);
        SPUtils.put(MainActivity.this, Configs.CurrentTab, 0);

        checkUpdataApp();
        initView();
        setPushAlias();
        checkFirstLogin();

    }

    // 判断是否是第一次登陆
    private void checkFirstLogin() {
        if (!(boolean) SPUtils.get(MainActivity.this, Configs.IsFristLogin, false)) {
            loginOkDialog = new LoginOkDialogView(MainActivity.this);
            loginOkDialog.show();
        }
    }

    // 检查升级
    private void checkUpdataApp() {
        downApkManager = new DownApkManager(MainActivity.this);
        downApkManager.checkUpdataApp(false);
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void setPushAlias() {
        JPushInterface.init(getApplicationContext());
        String IMEI = AppInfoUtil.getInstance().getDeviceId();
        if (!TextUtils.isEmpty(IMEI)) {
            JPushInterface.setAliasAndTags(getApplicationContext(), IMEI, null);
        }
    }

    private void initView() {
        // 实例化tabhost
        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.main_tab_framelayout);
        fragmentTabHost.getTabWidget().setDividerDrawable(null);
        for (int i = 0; i < fragmentArray.length; i++) {
            //对Tab按钮添加标记和图片
            TabSpec tabSpec = fragmentTabHost.newTabSpec(texts[i]).setIndicator(getTabItemView(i));
            //添加Fragment
            fragmentTabHost.addTab(tabSpec, fragmentArray[i], null);
//            //设置背景(必须在addTab之后，由于需要子节点（底部菜单按钮）否则会出现空指针异常)
            fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.white);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainActivity");
        MobclickAgent.onResume(this);
        int index = (int) SPUtils.get(MainActivity.this, Configs.CurrentTab, 0);
        if (fragmentTabHost != null) {
            if (index >= 4) {
                fragmentTabHost.setCurrentTab(0);
            } else {
                fragmentTabHost.setCurrentTab(index);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainActivity");
        MobclickAgent.onPause(this);
        if (loginOkDialog != null) {
            loginOkDialog.dismiss();
            loginOkDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallServer.getInstance().cancelBySign(MainActivity.this);
        if (downApkManager != null) {
            downApkManager.onDestroy();
        }
    }

    // 设置FragmentTabHost 的索引
    public static void setCurrentTab(int index) {
        if (fragmentTabHost != null) {
            fragmentTabHost.setCurrentTab(index);
        }
    }

    private View getTabItemView(int index) {
        if ((texts != null) && (texts.length > index)) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_buttom_view, null);
            TextView tabItemView = (TextView) view.findViewById(R.id.tab_buttom_tvView);
            CharSequence text = texts[index];
            if (!TextUtils.isEmpty(text)) {
                tabItemView.setText(text);
            }
            ImageView tabItemImg = (ImageView) view.findViewById(R.id.tab_buttom_ivView);
            tabItemImg.setImageResource(imageButton[index]);
            return view;
        }
        return null;
    }

    // 返回键回调
    @Override
    public void onBackPressed() {
        mDoubleHelper.action();
    }

    /**
     * 实现再按一次退出
     **/
    private DoubleActionHelper mDoubleHelper = new DoubleActionHelper(new Runnable() {
        @Override
        public void run() {
            ToastUtil.showShort(MainActivity.this, getString(R.string.press_again_will_exit));
        }
    }, new Runnable() {
        @Override
        public void run() {
//            AppManager.getAppManager().AppExit(MainActivity.this);
            MainActivity.super.onBackPressed();
        }
    }, 1500);

}
