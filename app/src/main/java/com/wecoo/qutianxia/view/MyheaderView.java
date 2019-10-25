package com.wecoo.qutianxia.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.MyBalanceActivity;
import com.wecoo.qutianxia.activity.MyMsgActivity;
import com.wecoo.qutianxia.activity.MyReportActivity;
import com.wecoo.qutianxia.activity.MyReportRateActivity;
import com.wecoo.qutianxia.activity.UserManagerActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.manager.ImageManager;
import com.wecoo.qutianxia.models.ListCountEntity;
import com.wecoo.qutianxia.models.MyDataEntity;
import com.wecoo.qutianxia.models.UserInfoEntity;
import com.wecoo.qutianxia.requestjson.GetMsgCountRequest;
import com.wecoo.qutianxia.requestjson.GetRoportCountRequest;
import com.wecoo.qutianxia.requestjson.GetUserInfoRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.utils.SPUtils;

/**
 * Created by mwl on 2016/10/25.
 * 我的上部分
 */

public class MyheaderView extends HeaderViewInterface<MyDataEntity> implements View.OnClickListener, ReturnDataClick {

    private Activity mActiivty;
    private final int userWhat = 0, reportCount = 1, msgCount = 2;
    private UserInfoEntity infoEntity;
    private ImageManager imageManager;// 图片管理
    private ImageView image_Photo;// 头像
    // 消息VIew   余额    报备率    我的报备
    private View viewMsg, viewBalance, viewReportrate, viewReport;
    // 消息数量    账号管理    用户名    电话
    private TextView txtMsgNum, txtUserManager, txtUserName, txtPhone, txtBalance, txtReportrate;
    // 我的报备数量 (核实中   跟进中   考察中   已签约   已退回)
    private TextView txtverifing, txtfollowuping, /*txtinspecting, */txtsigned, txtreturned;
    // 用户身份
    private OnUserIdentityListener identityListener;

    public MyheaderView(Activity context) {
        super(context);
        this.mActiivty = context;
        imageManager = new ImageManager(context);
    }

    @Override
    protected void getView(MyDataEntity myDataEntity, ListView listView) {
        View view = mInflate.inflate(R.layout.my_header_view, listView, false);
        initView(view);
        listView.addHeaderView(view);
    }

    // 初始化View
    private void initView(View view) {
        viewMsg = view.findViewById(R.id.myheader_msg_flView);
        txtMsgNum = (TextView) view.findViewById(R.id.myheader_txt_msgNum);
        txtUserManager = (TextView) view.findViewById(R.id.myheader_txt_userManager);
        txtUserName = (TextView) view.findViewById(R.id.myheader_txtUserName);
        txtPhone = (TextView) view.findViewById(R.id.myheader_txtPhone);
        txtBalance = (TextView) view.findViewById(R.id.myheader_txt_balance);
        txtReportrate = (TextView) view.findViewById(R.id.myheader_txt_reportRate);

        image_Photo = (ImageView) view.findViewById(R.id.user_image_Photo);
        viewBalance = view.findViewById(R.id.my_header_balance_ll);
        viewReportrate = view.findViewById(R.id.my_header_reportrate_ll);
        viewReport = view.findViewById(R.id.my_header_report_ll);
        txtverifing = (TextView) view.findViewById(R.id.myheader_txt_reportverifing);
        txtfollowuping = (TextView) view.findViewById(R.id.myheader_txt_reportfollowuping);
//        txtinspecting = (TextView) view.findViewById(R.id.myheader_txt_reportinspecting);
        txtsigned = (TextView) view.findViewById(R.id.myheader_txt_reportsigned);
        txtreturned = (TextView) view.findViewById(R.id.myheader_txt_reportreturned);

        addListener(view);
    }

    // 添加监听
    private void addListener(View view) {
        viewMsg.setOnClickListener(this);
        txtUserManager.setOnClickListener(this);
        viewBalance.setOnClickListener(this);
        viewReportrate.setOnClickListener(this);
        viewReport.setOnClickListener(this);
        view.findViewById(R.id.myheader_ll_reportverifing).setOnClickListener(this);
        view.findViewById(R.id.myheader_ll_reportfollowuping).setOnClickListener(this);
//        view.findViewById(R.id.myheader_ll_reportinspecting).setOnClickListener(this);
        view.findViewById(R.id.myheader_ll_reportsigned).setOnClickListener(this);
        view.findViewById(R.id.myheader_ll_reportreturned).setOnClickListener(this);
    }

    // 添加数据
    public void addData() {
        getSysMsgUnReadCount();
        getUserDetail();
        getMyCustomerReportCount();
    }

    // 获取系统未读消息
    private void getSysMsgUnReadCount() {
        GetMsgCountRequest msgRequest = new GetMsgCountRequest();
        msgRequest.setReturnDataClick(mActiivty, msgCount, this);
    }

    // 获取个人资料
    private void getUserDetail() {
        GetUserInfoRequest infoRequest = new GetUserInfoRequest();
        infoRequest.setReturnDataClick(mActiivty, userWhat, this);
    }

    // 获取报备相关数量
    private void getMyCustomerReportCount() {
        GetRoportCountRequest roportCountRequest = new GetRoportCountRequest();
        roportCountRequest.setReturnDataClick(mActiivty, reportCount, this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.myheader_msg_flView:
                MobclickAgent.onEvent(mContext, "MyMessageNum");
                intent.setClass(mActiivty, MyMsgActivity.class);
                mActiivty.startActivity(intent);
                break;
            case R.id.myheader_txt_userManager:
                MobclickAgent.onEvent(mContext, "UserManager");
                intent.setClass(mActiivty, UserManagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", infoEntity);
                intent.putExtras(bundle);
                mActiivty.startActivity(intent);
                break;
            case R.id.my_header_balance_ll:
                MobclickAgent.onEvent(mContext, "MyBalanceOnclick");
                intent.setClass(mActiivty, MyBalanceActivity.class);
                mActiivty.startActivity(intent);
                break;
            case R.id.my_header_reportrate_ll:
                MobclickAgent.onEvent(mContext, "MyReportRateOnclick");
                intent.setClass(mActiivty, MyReportRateActivity.class);
                mActiivty.startActivity(intent);
                break;
            case R.id.my_header_report_ll:// 已推荐客户
                intent.setClass(mActiivty, MyReportActivity.class);
                intent.putExtra(MyReportActivity.rbType, 1);
                mActiivty.startActivity(intent);
                break;
            case R.id.myheader_ll_reportverifing:
                intent.setClass(mActiivty, MyReportActivity.class);
                intent.putExtra(MyReportActivity.rbType, 1);
                mActiivty.startActivity(intent);
                break;
            case R.id.myheader_ll_reportfollowuping:
                intent.setClass(mActiivty, MyReportActivity.class);
                intent.putExtra(MyReportActivity.rbType, 2);
                mActiivty.startActivity(intent);
                break;
//            case R.id.myheader_ll_reportinspecting:
//                intent.setClass(mActiivty, MyReportActivity.class);
//                intent.putExtra(MyReportActivity.rbType, 3);
//                mActiivty.startActivity(intent);
//                break;
            case R.id.myheader_ll_reportsigned:
                intent.setClass(mActiivty, MyReportActivity.class);
                intent.putExtra(MyReportActivity.rbType, 3);
                mActiivty.startActivity(intent);
                break;
            case R.id.myheader_ll_reportreturned:
                intent.setClass(mActiivty, MyReportActivity.class);
                intent.putExtra(MyReportActivity.rbType, 4);
                mActiivty.startActivity(intent);
                break;
        }
    }

    @Override
    public void onReturnData(int what, Object obj) {
        switch (what) {
            case userWhat:
                infoEntity = (UserInfoEntity) obj;
                if (infoEntity != null) {
                    if ((boolean) SPUtils.get(mContext, Configs.IsUpdateUserInfo, true)) {
                        imageManager.loadCircleImage(infoEntity.getUs_photo(), image_Photo);
                        txtUserName.setText(infoEntity.getUs_nickname());
                        txtPhone.setText(infoEntity.getUs_tel());
                        SPUtils.put(mContext, Configs.user_Tel, infoEntity.getUs_tel()); // 用户ID加密
                        SPUtils.put(mContext, Configs.IsUpdateUserInfo, false); // 设置不可刷新用户信息
                    }
                    txtBalance.setText(mActiivty.getString(R.string.renminbi) + infoEntity.getUs_balance_str());
                    txtReportrate.setText(infoEntity.getUs_report_effective_rate());
                    if (identityListener != null){
                        identityListener.onUserIdentity(infoEntity.getUs_partner_kind_code());
                        identityListener.onIsCompany(infoEntity.getIsCompanyAccount());
                    }
                }
                break;
            case reportCount:
                ListCountEntity reportEntity = (ListCountEntity) obj;
                if (reportEntity != null) {
                    txtverifing.setText(String.valueOf(reportEntity.getAuditCount()));
                    txtfollowuping.setText(String.valueOf(reportEntity.getFollowUpCount()));
//                    txtinspecting.setText(String.valueOf(reportEntity.getInspectingCount()));
                    txtsigned.setText(String.valueOf(reportEntity.getSignedUpCount()));
                    txtreturned.setText(String.valueOf(reportEntity.getBackCustomerReporCount()));
                }
                break;
            case msgCount:
                int resultCount = (int) obj;
                if (resultCount > 0) {
                    txtMsgNum.setVisibility(View.VISIBLE);
                    if (resultCount > 9) {
                        txtMsgNum.setVisibility(View.VISIBLE);
                        txtMsgNum.setText("…");
                    } else {
                        txtMsgNum.setVisibility(View.VISIBLE);
                        txtMsgNum.setText(String.valueOf(resultCount));
                    }
                } else {
                    txtMsgNum.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void setIdentityListener(OnUserIdentityListener identityListener) {
        this.identityListener = identityListener;
    }

    // 用户身份
    public interface OnUserIdentityListener{
        void onUserIdentity(int userStatus);
        void onIsCompany(int isCompanyAccount);
    }
}
