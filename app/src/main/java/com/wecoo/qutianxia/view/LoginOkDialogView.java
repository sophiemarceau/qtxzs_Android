package com.wecoo.qutianxia.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.MyWanzhuanActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.utils.SPUtils;

/**
 * Created by mwl on 2016/10/28.
 * 登录成功提示框
 */

public class LoginOkDialogView {

    private Dialog dialog;

    public LoginOkDialogView(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.loginok_dialog_view, null);
        dialog = new Dialog(context, R.style.Dialog_No_Board);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        dialog.addContentView(view, params);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = context.getResources().getDisplayMetrics().widthPixels; // 设置宽度
        dialog.getWindow().setAttributes(lp);

        view.findViewById(R.id.loginok_dialog_txtWanzhuan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.put(context, Configs.IsFristLogin, true);
                dismiss();
                Intent intent = new Intent(context, MyWanzhuanActivity.class);
                context.startActivity(intent);
            }
        });

    }
    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
