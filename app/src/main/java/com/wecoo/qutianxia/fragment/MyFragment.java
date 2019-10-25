package com.wecoo.qutianxia.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.MyContactsActivity;
import com.wecoo.qutianxia.activity.MyInvitationActivity;
import com.wecoo.qutianxia.activity.MyProjectActivity;
import com.wecoo.qutianxia.activity.SettledActivity;
import com.wecoo.qutianxia.adapter.MyAdapter;
import com.wecoo.qutianxia.base.BaseFragment;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.manager.ModelManager;
import com.wecoo.qutianxia.models.ListCountEntity;
import com.wecoo.qutianxia.models.MyDataEntity;
import com.wecoo.qutianxia.requestjson.GetUserCountRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.NetWorkState;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.view.MyheaderView;
import com.wecoo.qutianxia.view.MyheaderView.OnUserIdentityListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwl on 2016/10/20.
 * 我的
 */
public class MyFragment extends BaseFragment implements OnUserIdentityListener {

    private final String mPageName = "MyFragment";
    private View rootView;
    private ListView listView;
    private MyheaderView myheaderView;
    private MyAdapter myAdapter;
    private List<MyDataEntity> dataEntityList = new ArrayList<MyDataEntity>();
    private String invationtNum = "0", contactNum = "0",reportWaitingAuditingNum = "";//未审核报备数

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_layout, container, false);
            initData();
            initView(rootView);
            SPUtils.put(getActivity(), Configs.IsUpdateUserInfo, true);
        }
        return rootView;
    }

    // 构建数据
    private void initData() {
        dataEntityList = ModelManager.getInstance().getMyData();
    }

    private void initView(View rootView) {
        listView = (ListView) rootView.findViewById(R.id.my_listview);

        if (myheaderView == null) {
            myheaderView = new MyheaderView(getActivity());
        }
        myheaderView.fillView(new MyDataEntity(), listView);

        myAdapter = new MyAdapter(getActivity(), dataEntityList);
        listView.setAdapter(myAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        SPUtils.put(getActivity(), Configs.CurrentTab, 3);
        MobclickAgent.onPageStart(mPageName);
        if (NetWorkState.isNetworkAvailable(getActivity())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getSalesmanUserRelatedCount();
                    myheaderView.addData();
                }
            }).start();
            myheaderView.setIdentityListener(this);
        } else {
            ToastUtil.showShort(getActivity(), getString(R.string.download_error_network));
        }
//        listView.smoothScrollToPosition(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        CallServer.getInstance().cancelBySign(getActivity());
    }

    // 获取我的相关数量
    private void getSalesmanUserRelatedCount() {
        final GetUserCountRequest userCountRequest = new GetUserCountRequest();
        userCountRequest.setReturnDataClick(getActivity(), 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                ListCountEntity reportEntity = (ListCountEntity) obj;
                if (reportEntity != null) {
                    for (int i = 0; i < dataEntityList.size(); i++) {
                        invationtNum = String.valueOf(reportEntity.getInvitationNum());
                        contactNum = String.valueOf(reportEntity.getConnectionCount());
                        reportWaitingAuditingNum = String.valueOf(reportEntity.getReportWaitingAuditingNum()) + "条未审核";
                        if (i == 0) { // 我的客户数量
                            dataEntityList.get(i).setDataNum(String.valueOf(reportEntity.getCustomerNum()));
                        } else if (i == 1) { // 我的关注数量
                            dataEntityList.get(i).setDataNum(String.valueOf(reportEntity.getProjectCollectionRecordNum()));
                        } else if (i == 2) { // 我的邀请数量
                            if (R.string.my_contacts == dataEntityList.get(i).getTitleContent()) {
                                dataEntityList.get(i).setDataNum(contactNum);
                            } else {
                                dataEntityList.get(i).setDataNum(invationtNum);
                            }
                        } else if (i == 3) { // 未审核报备数
                            if (R.string.my_settled == dataEntityList.get(i).getTitleContent()) {
                                dataEntityList.get(i).setDataNum("");
                            } else {
                                dataEntityList.get(i).setDataNum(reportWaitingAuditingNum);
                            }
                        } else if (i == 4) { // 奖励活动数量
                            dataEntityList.get(i).setDataNum(String.valueOf(reportEntity.getActivityNum()));
                        }
                    }
                    myAdapter.setData(dataEntityList);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CallServer.getInstance().cancelBySign(getActivity());
    }

    // 用户身份   0  我的邀请    ;  1  我的人脉
    @Override
    public void onUserIdentity(int userStatus) {
        for (int i = 0; i < dataEntityList.size(); i++) {
            if (i == 2) { // 我的邀请
                if (userStatus == 1) {
                    dataEntityList.get(2).setImgRouse(R.mipmap.icon_my_contacts);
                    dataEntityList.get(2).setTitleContent(R.string.my_contacts);
                    dataEntityList.get(2).setDataNum(contactNum);
                    dataEntityList.get(2).setCalssName(MyContactsActivity.class);
                } else {
                    dataEntityList.get(2).setImgRouse(R.mipmap.icon_myinvitation);
                    dataEntityList.get(2).setTitleContent(R.string.my_invitation);
                    dataEntityList.get(2).setDataNum(invationtNum);
                    dataEntityList.get(2).setCalssName(MyInvitationActivity.class);
                }
            }
        }
        myAdapter.setData(dataEntityList);
    }

    //   是否为企业账号:0、企业账号;1、非企业账号
    @Override
    public void onIsCompany(int isCompanyAccount) {
        for (int i = 0; i < dataEntityList.size(); i++) {
            if (i == 3) { // 是否为企业账号
                if (isCompanyAccount == 0) {
                    dataEntityList.get(3).setImgRouse(R.mipmap.icon_my_project);
                    dataEntityList.get(3).setTitleContent(R.string.my_Project);
                    dataEntityList.get(3).setDataNum(reportWaitingAuditingNum);
                    dataEntityList.get(3).setCalssName(MyProjectActivity.class);
                } else {
                    dataEntityList.get(3).setImgRouse(R.mipmap.icon_my_settled);
                    dataEntityList.get(3).setTitleContent(R.string.my_settled);
                    dataEntityList.get(3).setDataNum("");
                    dataEntityList.get(3).setCalssName(SettledActivity.class);
                }
            }
        }
        myAdapter.setData(dataEntityList);
    }
}
