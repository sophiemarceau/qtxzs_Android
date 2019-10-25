package com.wecoo.qutianxia.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.utils.ScreenUtil;

/**
 * Created by mwl on 2016/10/28.
 * 浮层弹出框
 */

public class DialogFucengView implements View.OnClickListener {

    private Context mContext;
    private Dialog dialog = null;
    private LinearLayout dialog_layout;
    private TextView txtTitle, txtDesc;

    public DialogFucengView(Context context) {
        this.mContext = context;
//        signedList = new ArrayList<ProjectSignedEntity>();

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_fuceng_view, null);
        dialog_layout = (LinearLayout) view.findViewById(R.id.dialog_fuceng_layout);
        txtTitle = (TextView) view.findViewById(R.id.dialog_fuceng_txtTitle);
        txtDesc = (TextView) view.findViewById(R.id.dialog_fuceng_txtDesc);
        dialog_layout.setOnClickListener(this);
        view.findViewById(R.id.dialog_fuceng_imgClose).setOnClickListener(this);

        createDialog(view);
    }

    private void createDialog(View view) {
        // 定义Dialog布局和参数
        dialog = new Dialog(mContext, R.style.Dialog_No_Board);
        dialog.setContentView(view);
        dialog.getWindow().setWindowAnimations(R.style.PopupAnimation);
        dialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog

        // 调整dialog背景大小
        dialog_layout.setLayoutParams(new LayoutParams(ScreenUtil.getScreenWidth(mContext),
                ScreenUtil.getScreenHeight(mContext)));
    }

    // 设置数据
    public void setTitleData(String strTitle) {
        if (strTitle != null) {
            txtTitle.setText(strTitle);
        }
    }

    // 设置数据
    public void setDescData(String strDesc) {
        if (strDesc != null) {
            txtDesc.setText(strDesc);
        }
    }

    // 显示
    public void show() {
        if (dialog != null && !dialog.isShowing()) {
//            dialog_layout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.in_from_buttom));
            dialog.show();
        }
    }

    // 隐藏
    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_fuceng_layout:
            case R.id.dialog_fuceng_imgClose:
                dismiss();
                break;
        }
    }

}
