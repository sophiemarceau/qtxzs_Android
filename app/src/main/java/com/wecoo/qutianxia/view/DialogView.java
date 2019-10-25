package com.wecoo.qutianxia.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wecoo.qutianxia.R;

/**
 * Created by mwl on 2016/10/28.
 * 提示框
 */

public class DialogView {

    private Context mContext;

    public DialogView(Context context) {
        this.mContext = context;
    }

    // 创建Dialog  isShowButtom (是否显示另一种布局)
    public void createDialog(String message, boolean isShowButtom,final DialogCallback dialogCallback) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.appupdata_dialog_view, null);
        TextView txtTitle = (TextView) view.findViewById(R.id.download_txt_status);
        TextView txtMessage = (TextView) view.findViewById(R.id.download_txt_message);
        TextView txtCancel = (TextView) view.findViewById(R.id.download_txt_update_later);
        TextView txtSure = (TextView) view.findViewById(R.id.download_txt_update_now);
        LinearLayout llTop = (LinearLayout) view.findViewById(R.id.dialogView_ll_top);
        LinearLayout llButtom = (LinearLayout) view.findViewById(R.id.dialogView_ll_buttom);
        TextView txtMsg = (TextView) view.findViewById(R.id.dialogView_txt_message);
        Button btnIknow = (Button) view.findViewById(R.id.dialogView_btn_iKnow);

        if (isShowButtom){
            llTop.setVisibility(View.GONE);
            llButtom.setVisibility(View.VISIBLE);
        }
        // 定义Dialog布局和参数
        final Dialog dialog = new Dialog(mContext, R.style.Dialog_No_Board);
        dialog.setContentView(view);
//        dialog.getWindow().setWindowAnimations(R.style.PopupAnimation);

        txtTitle.setText("温馨提示");
        txtMessage.setText(message);
        txtMsg.setText(message);
        txtCancel.setText(mContext.getString(R.string.cancel));
        txtSure.setText(mContext.getString(R.string.sure));

        btnIknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        txtSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (dialogCallback != null) {
                    dialogCallback.onSureClick();
                }
            }
        });
        dialog.show();
    }

    public interface DialogCallback {
        void onSureClick();
    }

}
