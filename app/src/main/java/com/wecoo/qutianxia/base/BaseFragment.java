package com.wecoo.qutianxia.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wecoo.qutianxia.application.WKApplication;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.widget.LoadProgressWidget;

/**
 * Created by mwl on 2016/10/20.
 * Fragment 的基类
 */
public abstract class BaseFragment extends Fragment {

    private LoadProgressWidget loadProgress = null;
    public WKApplication application;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (WKApplication) getActivity().getApplication();
        initData(application);
    }

    // 初始化一些固定数据
    private void initData(WKApplication application) {
        if (application.IndustryList == null) {
            application.IndustryList = ModelManager.getInstance().getLookupIndustryAll(getActivity());
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
    public void onDestroy() {
        super.onDestroy();
        closeLoadingDialog();
    }
}
