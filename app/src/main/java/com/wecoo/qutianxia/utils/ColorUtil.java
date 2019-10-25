package com.wecoo.qutianxia.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

/**
 * Created by mwl on 2016/12/27.
 * Listview 滑动，标题渐变的颜色配置
 */

public class ColorUtil {

    // 成新的颜色值
    public static int getNewColorByStartEndColor(Context context, float fraction, int startValue, int endValue) {
        return evaluate(fraction, context.getResources().getColor(startValue), context.getResources().getColor(endValue));
    }

    /**
     * 成新的颜色值
     *
     * @param fraction   颜色取值的级别 (0.0f ~ 1.0f)
     * @param startValue 开始显示的颜色
     * @param endValue   结束显示的颜色
     * @return 返回生成新的颜色值
     */
    private static int evaluate(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }

    // 返回textView不同的字体颜色
    public static SpannableString getTextColor(String text, int frist, int second) {
        if (TextUtils.isEmpty(text)) return null;
        SpannableString spanStr = new SpannableString(text);
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)
//                Spanned.SPAN_INCLUSIVE_EXCLUSIVE(前面包括，后面不包括)
//                Spanned.SPAN_EXCLUSIVE_INCLUSIVE(前面不包括，后面包括)
//                Spanned.SPAN_INCLUSIVE_INCLUSIVE(前后都包括)
        spanStr.setSpan(new ForegroundColorSpan(Color.RED), frist, second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    // 返回textView不同的字体颜色
    public static SpannableString getTextColor(String text, int frist, int second, int color) {
        if (TextUtils.isEmpty(text)) return null;
        SpannableString spanStr = new SpannableString(text);
        spanStr.setSpan(new ForegroundColorSpan(color), frist, second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

}
