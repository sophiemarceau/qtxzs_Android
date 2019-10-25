package com.wecoo.qutianxia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.base.TitleBarActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.manager.AppFolderManager;
import com.wecoo.qutianxia.manager.AppManager;
import com.wecoo.qutianxia.requestjson.FeedbackRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.utils.FileUtil;
import com.wecoo.qutianxia.utils.ToastUtil;

import java.io.File;

/**
 * Created by mwl on 2016/10/25.
 * 投诉建议
 */

public class ComplaintActivity extends TitleBarActivity implements View.OnClickListener {

    private final String mPageName = "ComplaintActivity";
    private Context mContext = ComplaintActivity.this;
    //    private FrameLayout flTwo,flThree,flFour;
    private View vOne, vTwo, vThree, vFour;
    private ImageView imgOne, imgTwo, imgThree, imgFour;
    private String strimgOne = "", strimgTwo = "", strimgThree = "", strimgFour = "";
    private EditText editContent;
    private Button btnnCommit;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_layout);
        AppManager.getAppManager().addActivity(this);

        initActionBar(this);
        setBanner(Left, getString(R.string.my_complaint), None);

        initView();

        MobclickAgent.onEvent(mContext, "ComplaintOnlick");
    }

    private void initView() {
//        flTwo = (FrameLayout) findViewById(R.id.complaint_frameLayout_two);
//        flThree = (FrameLayout) findViewById(R.id.complaint_frameLayout_three);
//        flFour = (FrameLayout) findViewById(R.id.complaint_frameLayout_four);
        vOne = findViewById(R.id.complaint_img_closeOne);
        vTwo = findViewById(R.id.complaint_img_closeTwo);
        vThree = findViewById(R.id.complaint_img_closeThree);
        vFour = findViewById(R.id.complaint_img_closeFour);
        imgOne = (ImageView) findViewById(R.id.complaint_image_one);
        imgTwo = (ImageView) findViewById(R.id.complaint_image_two);
        imgThree = (ImageView) findViewById(R.id.complaint_image_three);
        imgFour = (ImageView) findViewById(R.id.complaint_image_four);
        editContent = (EditText) findViewById(R.id.complaint_edit_content);
        btnnCommit = (Button) findViewById(R.id.complaint_butn_commit);

        // 添加监听
        vOne.setOnClickListener(this);
        vTwo.setOnClickListener(this);
        vThree.setOnClickListener(this);
        vFour.setOnClickListener(this);
        imgOne.setOnClickListener(this);
        imgTwo.setOnClickListener(this);
        imgThree.setOnClickListener(this);
        imgFour.setOnClickListener(this);
        btnnCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.complaint_image_one:
                index = 1;
                showDialog();
                break;
            case R.id.complaint_image_two:
                index = 2;
                showDialog();
                break;
            case R.id.complaint_image_three:
                index = 3;
                showDialog();
                break;
            case R.id.complaint_image_four:
                index = 4;
                showDialog();
                break;
            case R.id.complaint_img_closeOne:
                strimgOne = "";
                imgOne.setImageResource(R.mipmap.icon_upload_img);
                break;
            case R.id.complaint_img_closeTwo:
//                flTwo.setVisibility(View.INVISIBLE);
                strimgTwo = "";
                imgTwo.setImageResource(R.mipmap.icon_upload_img);
                break;
            case R.id.complaint_img_closeThree:
//                flThree.setVisibility(View.INVISIBLE);
                strimgThree = "";
                imgThree.setImageResource(R.mipmap.icon_upload_img);
                break;
            case R.id.complaint_img_closeFour:
//                flFour.setVisibility(View.INVISIBLE);
                strimgFour = "";
                imgFour.setImageResource(R.mipmap.icon_upload_img);
                break;
            case R.id.complaint_butn_commit:
                MobclickAgent.onEvent(mContext, "ComplaintCommitOnlick");
                updateFeedBack();
                break;
        }
    }

    // 投诉建议
    private void updateFeedBack() {
        String strContent = editContent.getText().toString().trim();
        if (TextUtils.isEmpty(strContent) || strContent.length() < 10) {
            ToastUtil.showShort(mContext, "请尽量详细的描述您的问题");
            return;
        }
        FeedbackRequest feedbackRequest = new FeedbackRequest();
        FeedbackRequest.FeedbackParms parms = new FeedbackRequest.FeedbackParms();
        parms.feedback_content = strContent;
        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(strimgOne)) {
            stringBuilder.append(strimgOne + "^");
        }
        if (!TextUtils.isEmpty(strimgTwo)) {
            stringBuilder.append(strimgTwo + "^");
        }
        if (!TextUtils.isEmpty(strimgThree)) {
            stringBuilder.append(strimgThree + "^");
        }
        if (!TextUtils.isEmpty(strimgFour)) {
            stringBuilder.append(strimgFour);
        }
        if (TextUtils.isEmpty(stringBuilder)) {
            parms._feedback_pics = null;
        } else {
            parms._feedback_pics = stringBuilder.toString();
        }
        feedbackRequest.setRequestParms(parms);
        feedbackRequest.setReturnDataClick(mContext, 0, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                editContent.setText("");
                Intent intent = new Intent(mContext, CommitSuccessActivity.class);
                intent.putExtra(CommitSuccessActivity.TITLE, getString(R.string.my_complaint));
                intent.putExtra(CommitSuccessActivity.TYPE, Constants.COMPLAINT);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showDialog() {
        Intent intent = new Intent(mContext, SelectPhotoActivity.class);
        intent.putExtra(SelectPhotoActivity.TYPE, 1);
        startActivityForResult(intent, SelectPhotoActivity.requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SelectPhotoActivity.requestCode) {
                if (data != null) {
                    String strPhoto = (String) data.getSerializableExtra("picUrl");
                    File file = (File) data.getSerializableExtra("PicFile");
                    upDownloadPhoto(index, strPhoto, file);
                }
            }
        }
    }

    // 获取照片
    private void upDownloadPhoto(int what, String strPhoto,File file) {
        switch (what) {
            case 1:
                strimgOne = strPhoto;
                imageManager.loadLocalImage(file, imgOne); //把图片显示在ImageView控件上
                break;
            case 2:
                strimgTwo = strPhoto;
                imageManager.loadLocalImage(file, imgTwo); //把图片显示在ImageView控件上
                break;
            case 3:
                strimgThree = strPhoto;
                imageManager.loadLocalImage(file, imgThree); //把图片显示在ImageView控件上
                break;
            case 4:
                strimgFour = strPhoto;
                imageManager.loadLocalImage(file, imgFour); //把图片显示在ImageView控件上
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
    protected void onDestroy() {
        super.onDestroy();
        FileUtil.deleteDirectory(AppFolderManager.getInstance().getTempFolder());
        CallServer.getInstance().cancelBySign(mContext);
    }
}
