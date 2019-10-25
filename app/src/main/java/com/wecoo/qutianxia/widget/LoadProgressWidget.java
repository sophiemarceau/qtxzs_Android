package com.wecoo.qutianxia.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wecoo.qutianxia.R;

/**
 * Created by wecoo on 2016/11/3.
 * 数据加载条
 */

public class LoadProgressWidget extends Dialog {

    private Context context;
    private TextView tvMsg;
    private LoadProgressWidget customDialog = null;

    public LoadProgressWidget(Context context) {
        super(context);
        this.context = context;
    }
    public LoadProgressWidget(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }
    /**
     * 创建进度条
     **/
    public LoadProgressWidget createDialog() {
        customDialog = new LoadProgressWidget(context, R.style.AlertDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.loading_progress_view,null);
        tvMsg = (TextView) view.findViewById(R.id.load_progress_txt_msg);
        customDialog.setContentView(view);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.setCancelable(false);
        customDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return customDialog;
    }

    public void setMessage(String strMessage) {
        if (!TextUtils.isEmpty(strMessage)) {
            tvMsg.setText(strMessage);
        }
    }

    public void showDialog() {
        if (customDialog != null && !customDialog.isShowing()){
            customDialog.show();
        }
    }

    public void closeDialog() {
        if (customDialog != null && customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
