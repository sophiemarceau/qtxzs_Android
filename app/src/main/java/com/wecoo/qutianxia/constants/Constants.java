package com.wecoo.qutianxia.constants;

/**
 * Created by mwl on 2016/10/19.
 * 公共参数
 */
public class Constants {

    // 每页条数
    public static int pageSize = 10;

    // 请求成功的状态
    public static int QUESTSUCCESS = 1;
    // 用户失效，重新登录
    public static int QUESTLOGIN = -1;
    // 跳转的一些参数配置
    public final static int WITHDRAWALS = 1; // 申请取现成功
    public final static int ADDREPORT = 2;   // 添加报备成功
    public final static int COMPLAINT = 3;   // 添加投诉建议成功
    public final static int SETPASS = 4;   // 设置提现密码成功
    public final static int RESETPWD = 5;   // 重置提现密码成功
    public final static int SignUpforMoney = 6;   // 签约打款操作
    public final static int ExamineProjectSuccess = 7;   // 审核推荐项目操作

    // 状态
    public final static int normal = 1;   // 正常
    public final static int paused = 2;   // 已暂停
    public final static int ended = 3;   // 已结束
    public final static int activeShare = 4;   // （活动）邀请好友可分享
}
