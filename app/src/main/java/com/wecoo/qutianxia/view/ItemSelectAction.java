package com.wecoo.qutianxia.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.models.FilterEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wecoo on 2016/11/7.
 * 单项条件选择
 */

public class ItemSelectAction implements View.OnClickListener, OnValueChangeListener /*Formatter, *//*OnScrollListener*/ {

    private Context mContext;
    private Dialog dialog;
    private LinearLayout dialog_layout;
    private TextView txtCancel, txtSure;
    private NumberPicker mNumberPicker;
    private String[] keys;
    private String[] values;
    private int index = 0;
    private List<FilterEntity> entityList = new ArrayList<FilterEntity>();
    private FilterEntity entity = new FilterEntity();
    private OnSelectListener selectListener;

    public ItemSelectAction(Context context, List<FilterEntity> lists, int index) {
        this.mContext = context;
        this.entityList = lists;
        this.index = index;

        setData();

        View view = LayoutInflater.from(context).inflate(R.layout.itemselect_action_view, null);

        setDialogView(mContext, view);
        initView(view);
    }

    // 设置数据
    private void setData() {
        if (entityList != null && entityList.size() > 0) {
            entity.id = index;
            if (entityList.get(index).getCode() != null) {
                entity.setCode(entityList.get(index).getCode());
            }
            if (entityList.get(index).getName() != null) {
                entity.setName(entityList.get(index).getName());
            }
            keys = new String[entityList.size()];
            values = new String[entityList.size()];
            for (int i = 0; i < entityList.size(); i++) {
                keys[i] = entityList.get(i).getCode();
                values[i] = entityList.get(i).getName();
            }
        }
    }

    private void initView(View view) {
        dialog_layout = (LinearLayout) view.findViewById(R.id.dialog_layout);
        txtCancel = (TextView) view.findViewById(R.id.item_action_txtCancel);
        txtSure = (TextView) view.findViewById(R.id.item_action_txtSure);
        mNumberPicker = (NumberPicker) view.findViewById(R.id.item_action_picker);

        // 控件设置
        txtCancel.setOnClickListener(this);
        txtSure.setOnClickListener(this);
        // picKer 设置
        mNumberPicker.setOnValueChangedListener(this);
        mNumberPicker.setDisplayedValues(values);  // 设置文字
        mNumberPicker.setMaxValue(values.length - 1);// 设置最高
        mNumberPicker.setMinValue(0);// 设置最低
        mNumberPicker.setValue(index);  // 设置默认
        mNumberPicker.setWrapSelectorWheel(false); //取消循环滚动
        mNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止NumberPicker输入
        setNumberPickerDividerColor(mNumberPicker);
    }

    private void setDialogView(Context context, View view) {
        dialog = new Dialog(context, R.style.Dialog_No_Board);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        dialog.addContentView(view, params);
        dialog.show();

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.PopupAnimation);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = context.getResources().getDisplayMetrics().widthPixels; // 设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(mContext.getResources().getColor(R.color.wecoo_gray2)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_action_txtCancel:
                dismiss();
                break;
            case R.id.item_action_txtSure:
                dismiss();
                if (selectListener != null) {
                    selectListener.onSelectData(entity);
                }
                break;
        }
    }

    //    @Override
//    public String format(int value) {
//        String tmpStr = String.valueOf(value);
//        if (value < 10) {
//            tmpStr = "0" + tmpStr;
//        }
//        return tmpStr;
//    }

    public void setSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
    }

    public interface OnSelectListener {
        void onSelectData(FilterEntity entity);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        entity.id = newVal;
        entity.setCode(entityList.get(newVal).getCode());
        entity.setName(entityList.get(newVal).getName());
    }
}