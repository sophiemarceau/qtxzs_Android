package com.wecoo.qutianxia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.models.MyRewardEntity;
import com.wecoo.qutianxia.umeng.Defaultcontent;
import com.wecoo.qutianxia.umeng.ShareWindow;

/**
 * Created by mwl on 2016/10/25.
 * 奖励活动详情
 */

public class RewardInfoActivity extends TitleBarActivity implements View.OnClickListener {

    private final String mPageName = "RewardInfoActivity";
    private MyRewardEntity.RewardModel infoModel;
    private TextView txtName, txtTime, txtDesc;
    private Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewardinfo_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.my_bunos), None);

        Intent intent = this.getIntent();
        infoModel = (MyRewardEntity.RewardModel) intent.getSerializableExtra("reward");

        initView();
    }

    private void initView() {
        txtName = (TextView) findViewById(R.id.rewardInfo_txt_name);
        txtTime = (TextView) findViewById(R.id.rewardInfo_txt_time);
        txtDesc = (TextView) findViewById(R.id.rewardInfo_txt_desc);
        btnShare = (Button) findViewById(R.id.rewardInfo_btn_share);

        if (infoModel != null) {
            txtName.setText(infoModel.getActivity_name());
            txtTime.setText("有效期至 " + infoModel.getActivity_enddate());
            txtDesc.setText(infoModel.getActivity_desc());
            if (infoModel.getActivity_kind_code() == Constants.activeShare) {
                btnShare.setVisibility(View.VISIBLE);
            } else {
                btnShare.setVisibility(View.GONE);
            }
        }

        btnShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rewardInfo_btn_share:
                ShareWindow shareWindow = new ShareWindow(RewardInfoActivity.this);
                shareWindow.setView(false);
                shareWindow.setShareData(R.mipmap.share_friend_icon, Defaultcontent.shareFriendtitle,
                        Defaultcontent.shareFriendtext, Defaultcontent.shareFriendurl);
                shareWindow.show();
                break;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
