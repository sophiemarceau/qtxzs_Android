package com.wecoo.qutianxia.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.application.WKApplication;
import com.wecoo.qutianxia.listener.BackGestureListener;
import com.wecoo.qutianxia.manager.ImageManager;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.widget.LoadProgressWidget;

/**
 * Created by mwl on 2016/10/19.
 * Activity 基类
 */
public class BaseActivity extends AppCompatActivity {

    public WKApplication application;
    private LoadProgressWidget loadProgress = null;
    public ImageManager imageManager;// 图片管理
    /** 手势监听 */
    private GestureDetector mGestureDetector;
    /** 是否需要监听手势关闭功能 */
    private boolean mNeedBackGesture = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideTitleBar();
        initGestureDetector();

        application = (WKApplication) WKApplication.getAppContext();
        initDefaultData(application);
        imageManager = new ImageManager(application);
    }

    private void initGestureDetector() {
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(getApplicationContext(),
                    new BackGestureListener(this));
        }
    }
    /*
     * 设置是否进行手势监听
     */
    public void setNeedBackGesture(boolean mNeedBackGesture){
        this.mNeedBackGesture = mNeedBackGesture;
    }

    // 初始化一些固定数据
    private void initDefaultData(WKApplication application) {
        if (application.IndustryList == null) {
            application.IndustryList = ModelManager.getInstance().getLookupIndustryAll(this);
        }
        if (application.BudgetList == null) {
            application.BudgetList = ModelManager.getInstance().getBudget();
        }
        if (application.PlanTimeList == null) {
            application.PlanTimeList = ModelManager.getInstance().getPlanTime();
        }
    }

    /**
     * 显示加载条
     */
    public void showLoadingDialog(Context context, String msg) {
        if (loadProgress == null) {
            loadProgress = new LoadProgressWidget(context);
            loadProgress.createDialog();
            loadProgress.setMessage(msg);
        }
        loadProgress.showDialog();
    }

    /**
     * 关闭加载条
     */
    public void closeLoadingDialog() {
        if (loadProgress != null) {
            loadProgress.closeDialog();
            loadProgress = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeLoadingDialog();
    }

    /**
     * 点击空白处隐藏软键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isHideInput(v, ev)) {
                assert v != null;
                hideSoftInput(v.getWindowToken());
            }
        }
        if(mNeedBackGesture){
            return mGestureDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     */
    public boolean isHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     */
    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏TitleBar
     **/
    protected void hideTitleBar() {
        try {
            ActionBar actionbar = getSupportActionBar();
            if (actionbar != null) {
                actionbar.hide();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Activity跳转
     **/
    public void openActivity(Context ConFrom, Class<?> ClsTo) {
        Intent intent = new Intent(ConFrom, ClsTo);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
    }

}
