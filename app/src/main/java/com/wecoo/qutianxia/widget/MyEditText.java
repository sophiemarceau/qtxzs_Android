package com.wecoo.qutianxia.widget;

import android.content.Context;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * Created by mwl on 2017/1/18.
 * 自定义EditText
 */

public class MyEditText extends EditText {

    public MyEditText(Context context) {
        super(context);
        setImeOptions(EditorInfo.IME_ACTION_NEXT);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 设置为true，表明激活该控件
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    // 焦点
    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
    }

    // 光标定位
    public void setText(String strText) {
        if (!TextUtils.isEmpty(strText)) {
            setText(strText);
            setSelection(strText.length());
        }
    }

    // 光标定位
    @Override
    public void setSelection(int index) {
        super.setSelection(index);
    }

}
