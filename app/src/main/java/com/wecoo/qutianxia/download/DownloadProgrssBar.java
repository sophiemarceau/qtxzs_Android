package com.wecoo.qutianxia.download;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wecoo.qutianxia.R;

/**
 * Created by mwl on 2016/12/2.
 * 下载弹出框
 */

public class DownloadProgrssBar implements View.OnClickListener {

    private Dialog dialog;
    private View download_ll_view;
    // 下载状态、进度、大小、提示、稍后更新
    private TextView txtStatus, txtProgress, txtSize,txtMessage,txtUpdate_later;
    private TextView txt_Updatemessage;
    private ProgressBar pBar;
    private DownLoadListener loadListener;

    DownloadProgrssBar(Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.appupdata_dialog_view, null);
        download_ll_view = view.findViewById(R.id.download_ll_view);
        txtStatus = (TextView) view.findViewById(R.id.download_txt_status);
        txtProgress = (TextView) view.findViewById(R.id.download_txt_progress);
        txtSize = (TextView) view.findViewById(R.id.download_txt_size);
        txtMessage = (TextView) view.findViewById(R.id.download_txt_message);
        txt_Updatemessage = (TextView) view.findViewById(R.id.download_txt_Updatemessage);
        txtUpdate_later = (TextView) view.findViewById(R.id.download_txt_update_later);
        TextView txtUpdate_now = (TextView) view.findViewById(R.id.download_txt_update_now);
        pBar = (ProgressBar) view.findViewById(R.id.download_progressBar);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.Dialog_No_Board);
        dialog.setContentView(view);
//        dialog.getWindow().setWindowAnimations(R.style.dialogAnimation);
        dialog.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域关闭Dialog
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = context.getResources().getDisplayMetrics().widthPixels; // 设置宽度
        lp.height = context.getResources().getDisplayMetrics().heightPixels; // 设置高度
        dialog.getWindow().setAttributes(lp);

        // 设置滚动
        txt_Updatemessage.setMovementMethod(new ScrollingMovementMethod());
        // 监听
        txtUpdate_later.setOnClickListener(this);
        txtUpdate_now.setOnClickListener(this);
    }

    //status  0，提示弹层； 1,下载弹层；
    void setDialogStatus(int status) {
        if (status == 0) {
            txtProgress.setVisibility(View.GONE);
            txtSize.setVisibility(View.GONE);
            pBar.setVisibility(View.GONE);
        } else {
            txtProgress.setVisibility(View.VISIBLE);
            txtSize.setVisibility(View.VISIBLE);
            pBar.setVisibility(View.VISIBLE);
            txtMessage.setVisibility(View.GONE);
            txt_Updatemessage.setVisibility(View.GONE);
            download_ll_view.setVisibility(View.GONE);
        }
    }

    //result  1，用户可选更新； 2,强制更新；
    void setDownType(int result) {
        if (result == 1) {
            txtUpdate_later.setVisibility(View.VISIBLE);
        } else {
            txtUpdate_later.setVisibility(View.GONE);
        }
    }

    void setTxtStatus(String text) {
        if (!TextUtils.isEmpty(text)) {
            txtStatus.setText(text);
        }
    }

    void setTxtMessage(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            txtMessage.setVisibility(View.VISIBLE);
            txtMessage.setText(msg);
        }
    }

    void setTxtUpdateMessage(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            txt_Updatemessage.setVisibility(View.VISIBLE);
            txt_Updatemessage.setText(msg);
        }
    }

    void setTxtSize(String text) {
        if (!TextUtils.isEmpty(text)) {
            txtSize.setText(text);
        }
    }

    void setProgress(int progress) {
        pBar.setProgress(progress);
        txtProgress.setText(progress + "%");
    }

    // 显示
    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    // 隐藏
    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    void setLoadListener(DownLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_txt_update_later:
                dismiss();
                break;
            case R.id.download_txt_update_now:
                if (loadListener != null) {
                    loadListener.OnSureClick();
                }
                break;
        }
    }

    public interface DownLoadListener {
        void OnSureClick();
    }
}
