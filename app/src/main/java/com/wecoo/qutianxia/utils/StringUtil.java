package com.wecoo.qutianxia.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mwl on 2016/10/19.
 * 字符串的统一管理类
 */
public class StringUtil {

    /**
     * 手机号验证
     * 验证通过返回true
     */
    public static boolean isMobile(String strphone) {
        if (TextUtils.isEmpty(strphone))
            return false;
        Pattern pattern = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");// 验证手机号
        Matcher matcher = pattern.matcher(strphone);
        return matcher.matches();
    }

    /**
     * 登录用户名（首字母英文  4-16）
     * 验证通过返回true
     */
    public static boolean isLoginUser(String password) {
        if (TextUtils.isEmpty(password))
            return false;
        Pattern pattern = Pattern.compile("^[a-zA-Z]\\w{3,15}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * 验证输入身份证号
     *
     * @param strId 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsIDcard(String strId) {
        //定义判别用户身份证号的正则表达式（要么是15位全是数字，要么是18位，最后一位可以为字母）  
        String rgx = "^(\\d{15})|^(\\d{17}([0-9]|X|x))$";
        Pattern idNumPattern = Pattern.compile(rgx);
//        Pattern idNumPattern = Pattern.compile("(\\d{15})|(\\d{17}[0-9a-zA-Z])");
        return idNumPattern.matcher(strId).matches();
    }

    /**
     * 网址验证
     * 验证通过返回true
     */
    public static boolean isHttpUrl(String strUrl) {
        if (TextUtils.isEmpty(strUrl))
            return false;
        String regex = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(strUrl);
        return matcher.matches();
    }

    /**
     * Double 比较大小
     */
    public static boolean strToDou(String strNum) {
        if (TextUtils.isEmpty(strNum))
            return false;
        try {
            String obj1 = "0.0";
            Double doub1 = Double.valueOf(strNum);
            Double doub2 = Double.valueOf(obj1);
            return doub1.compareTo(doub2) > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 判断字符截取
     */
    public static String strSub(String strText) {
        if (TextUtils.isEmpty(strText))
            return "";
        if (strText.trim().length() > 5) {
            return strText.substring(0, 5) + "…";
        } else {
            return strText;
        }
    }

    /**
     * 实现文本复制功能
     * @param text
     */
    public static void copy(Context context, String text) {
        // 得到剪贴板管理器
        if (TextUtils.isEmpty(text)) {
            ToastUtil.showShort(context,"内容为空，无法复制");
            return;
        }
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(text.trim());
        ToastUtil.showShort(context,"您已经成功复制邀请码");
    }

    /**
     * 实现粘贴功能
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }
}
