package com.wecoo.qutianxia.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.ApplyProgressAdapter;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.ApplyRecordEntity;
import com.wecoo.qutianxia.models.ApplyRecordModel;
import com.wecoo.qutianxia.requestjson.GetWithdrawProgressRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.refreshload.PtrWecooFrameLayout;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget;
import com.wecoo.qutianxia.widget.LoadDataErrorWidget.OnReLoadClickListener;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by mwl on 2017/01/19.
 * 申请提现进度
 */

public class ApplyProgressActivity extends TitleBarActivity implements OnReLoadClickListener {

    private final String mPageName = "ApplyProgressActivity";
    private Context mContext = ApplyProgressActivity.this;
    private PtrWecooFrameLayout mPtrFrame;
    private ListView mListView;
    private LoadDataErrorWidget errorWidget;
    private ApplyProgressAdapter adapter;
    private List<ApplyRecordModel> dataList;
    private String swa_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyprogress_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.apply_record_progress), None);
        // 申请记录的ID
        swa_id = getIntent().getStringExtra("swa_id");

        initList();
        initView();
    }

    // 初始化List
    private void initList() {
        dataList = new ArrayList<ApplyRecordModel>();
    }

    //初始化View
    private void initView() {
        mPtrFrame = (PtrWecooFrameLayout) findViewById(R.id.ApplyProgress_PtrFrameLayout);
        mListView = (ListView) findViewById(R.id.ApplyProgress_listView);
        errorWidget = (LoadDataErrorWidget) findViewById(R.id.ApplyProgress_loaddataView);
        adapter = new ApplyProgressAdapter(mContext, dataList);
        mListView.setAdapter(adapter);

        //setListener
        setListener();
    }

    // 设置监听
    private void setListener() {
        errorWidget.setOnReLoadClickListener(this);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (!NetWorkState.isNetworkAvailable(mContext)) {
                    mPtrFrame.refreshComplete();
                    ToastUtil.showShort(mContext, getString(R.string.load_data_nonetwork));
                    return;
                }
                initList();
                getApplyProgressData(false);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
        //
        initList();
        getApplyProgressData(true);
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

    // 网络刷新
    @Override
    public void OnReLoadData() {
        initList();
        getApplyProgressData(true);
    }

    // 获取申请进度数据
    private void getApplyProgressData(boolean isshowLoad) {
        if (NetWorkState.isNetworkAvailable(mContext)) {
            GetWithdrawProgressRequest progressRequest = new GetWithdrawProgressRequest();
            progressRequest.setRequestParms(swa_id);
            progressRequest.setReturnDataClick(mContext, isshowLoad, 0, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    ApplyRecordEntity entity = (ApplyRecordEntity) obj;
                    if (entity != null) {
                        if (entity.getList() != null && entity.getList().size() > 0) {
                            mPtrFrame.setVisibility(View.VISIBLE);
                            errorWidget.setVisibility(View.GONE);

                            dataList.addAll(entity.getList());
                            adapter.setData(dataList);
                        } else {
                            mPtrFrame.setVisibility(View.GONE);
                            errorWidget.setVisibility(View.VISIBLE);
                            errorWidget.dataLoadError();
                        }
                        mPtrFrame.refreshComplete();
                    }
                }
            });
        } else {
            mPtrFrame.setVisibility(View.GONE);
            errorWidget.setVisibility(View.VISIBLE);
            errorWidget.netWorkError();
        }
    }
}
