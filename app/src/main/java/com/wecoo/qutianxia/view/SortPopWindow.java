package com.wecoo.qutianxia.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.utils.DensityUtil;
import com.wecoo.qutianxia.utils.ScreenUtil;

/**
 * Created by mwl on 2017/2/18.
 * 排序方式的popwindow
 */

public class SortPopWindow extends PopupWindow implements View.OnClickListener {

    private Activity mContext;
    private OnSortClick onSortClick;
    private TextView txtrewardQuota, txtregistTime;
    private Drawable drawQuota, drawTime;// 状态图标
    // 排序  0 默认排序  由小到大 、 由大到小 赏金：1，(降序) 2，(升序)
    private int QuotaWhat = 0, TimeWhat = 0;

    public SortPopWindow(Activity context) {
        this.mContext = context;
        View pop_view = LayoutInflater.from(context).inflate(R.layout.popwindow_sort_view, null);
        this.setContentView(pop_view);
        this.setWidth(ScreenUtil.getScreenWidth(mContext));
        this.setHeight(ScreenUtil.getScreenHeight(mContext));
        // 排序Item 监听
        txtrewardQuota = (TextView) pop_view.findViewById(R.id.popView_txt_rewardQuota);
        txtregistTime = (TextView) pop_view.findViewById(R.id.popView_txt_registTime);
        txtrewardQuota.setOnClickListener(this);
        txtregistTime.setOnClickListener(this);
        // 点击其他区域消失
        pop_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                disMissPop();
                return false;
            }
        });
        setQuotaDrawLeft(QuotaWhat);
        setTimeDrawLeft(TimeWhat);
    }

    // 设置右边图标
    private void setQuotaDrawLeft(int status) {
        switch (status) {
            case 0:
                drawQuota = ContextCompat.getDrawable(mContext, R.mipmap.icon_sort_gray_default);
                txtrewardQuota.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_gray5));
                break;
            case 1:
                drawQuota = ContextCompat.getDrawable(mContext, R.mipmap.icon_sort_big_to_small);
                txtrewardQuota.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
                break;
            case 2:
                drawQuota = ContextCompat.getDrawable(mContext, R.mipmap.icon_sort_small_to_big);
                txtrewardQuota.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
                break;
        }
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示  
        drawQuota.setBounds(0, 0, DensityUtil.dp2px(mContext, 10), DensityUtil.dp2px(mContext, 13));
        txtrewardQuota.setCompoundDrawables(null, null, drawQuota, null);
    }

    // 设置右边图标
    private void setTimeDrawLeft(int status) {
        switch (status) {
            case 0:
                drawTime = ContextCompat.getDrawable(mContext, R.mipmap.icon_sort_gray_default);
                txtregistTime.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_gray5));
                break;
            case 1:
                drawTime = ContextCompat.getDrawable(mContext, R.mipmap.icon_sort_small_to_big);
                txtregistTime.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
                break;
            case 2:
                drawTime = ContextCompat.getDrawable(mContext, R.mipmap.icon_sort_big_to_small);
                txtregistTime.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
                break;
        }
        drawTime.setBounds(0, 0, DensityUtil.dp2px(mContext, 10), DensityUtil.dp2px(mContext, 13));
        txtregistTime.setCompoundDrawables(null, null, drawTime, null);
    }

    public void disMissPop() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }

    // 显示POPWindow
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
//            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }

    // 监听
    public void setOnSortClick(OnSortClick onSortClick) {
        this.onSortClick = onSortClick;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.popView_txt_rewardQuota:
                if (QuotaWhat == 1) {
                    QuotaWhat = 2;
                } else {
                    QuotaWhat = 1;
                }
                setQuotaDrawLeft(QuotaWhat);
                TimeWhat = 0;
                setTimeDrawLeft(TimeWhat);
                if (onSortClick != null) {
                    onSortClick.onSortResult(String.valueOf(QuotaWhat));
                }
                if (isShowing()) {
                    dismiss();
                }
                break;
            case R.id.popView_txt_registTime:
                if (TimeWhat == 2) {
                    TimeWhat = 1;
                } else {
                    TimeWhat = 2;
                }
                setTimeDrawLeft(TimeWhat);
                QuotaWhat = 0;
                setQuotaDrawLeft(QuotaWhat);
                if (onSortClick != null) {
                    onSortClick.onSortTimeResult(String.valueOf(TimeWhat));
                }
                if (isShowing()) {
                    dismiss();
                }
                break;
        }
    }

    public interface OnSortClick {
        void onSortResult(String text);
        void onSortTimeResult(String text);
    }
}
