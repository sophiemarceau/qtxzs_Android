package com.wecoo.qutianxia.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.ReSetPasswordActivity;
import com.wecoo.qutianxia.listener.NoDoubleClickListener;
import com.wecoo.qutianxia.utils.KeyBoardUtil;

/**
 * Created by mwl on 2017/4/1.
 * 提示框
 */

public class SetPswDialogView {

    private Context mContext;
    private Dialog dialog;
    private TextView txtMessage, txtSure;
    private EditText editPass;
    private DialogCallback dialogCallback;

    public SetPswDialogView(Context context) {
        this.mContext = context;
    }

    // 创建Dialog
    public void createDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.inputpass_dialog_view, null);
        txtMessage = (TextView) view.findViewById(R.id.inputpass_txt_msg);
        TextView txtResetPass = (TextView) view.findViewById(R.id.inputpass_txt_resetPass);
        TextView txtCancel = (TextView) view.findViewById(R.id.inputpass_txt_cancle);
        txtSure = (TextView) view.findViewById(R.id.inputpass_txt_sure);
        editPass = (EditText) view.findViewById(R.id.inputpass_edit_pass);

        // 定义Dialog布局和参数
        dialog = new Dialog(mContext, R.style.Dialog_No_Board);
        dialog.setContentView(view);

        editPass.addTextChangedListener(textWatcher);
        txtResetPass.setOnClickListener(clickListener);
        txtCancel.setOnClickListener(clickListener);
//        txtSure.setOnClickListener(clickListener);
        // show Dialog
        dialog.show();
    }

    // 编辑框的监听
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {
                setContent(1);
                txtSure.setOnClickListener(clickListener);
            } else {
                setContent(0);
                txtSure.setOnClickListener(null);
            }
        }
    };

    public void setClickListener(DialogCallback dialogCallback) {
        this.dialogCallback = dialogCallback;
    }

    private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.inputpass_txt_resetPass:
                    Intent intent = new Intent(mContext, ReSetPasswordActivity.class);
                    mContext.startActivity(intent);
                    hideDialog();
                    break;
                case R.id.inputpass_txt_cancle:
                    hideDialog();
                    break;
                case R.id.inputpass_txt_sure:
                    String strPass = editPass.getText().toString().trim();
                    if (strPass.length() < 6) {
                        setContent(2);
                        return;
                    }
                    if (dialogCallback != null) {
                        dialogCallback.onSureClick(strPass);
                    }
                    break;
            }
        }
    };

//    public void setMsg(String message) {
//        if (!TextUtils.isEmpty(message)) {
//            txtMessage.setText(message);
//        }
//    }

    public void setContent(int status) {
        switch (status) {
            case 0:
                txtSure.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_gray4));
                txtSure.setBackgroundColor(ContextCompat.getColor(mContext, R.color.wecoo_gray2));
                break;
            case 1:
                txtSure.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                txtSure.setBackgroundColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
                break;
            case 2:
                txtMessage.setText("您的提现密码输入错误，请重新输入！");
                txtMessage.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
                break;
        }
    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            KeyBoardUtil.closeKeybord(editPass, mContext);
            dialog.dismiss();
            dialog = null;
        }
    }

    public interface DialogCallback {
        void onSureClick(String flag);
    }

}
