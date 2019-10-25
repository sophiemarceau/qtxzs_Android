package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl on 2016/11/9.
 * 用户信息
 */

public class UserInfoEntity implements Serializable {

    private String user_id;
//    private String user_id_str;
    private String us_photo; // 用户头像
    private String us_nickname; // 用户昵称
    private String us_realname; // 用户真实姓名
    private String us_company; // 用户公司名称
    private String us_jobtitle; // 用户职位
    private String us_tel; // 用户电话
//    private String user_id_referee; // 推荐用户id
//    private String us_balance; // 用户余额
    private String us_balance_str; // 用户余额
    private String us_report_effective_rate; // 报备质量分 最高150 ，最低 0
//    private String us_createdtime; // 创建时间
    private int us_partner_kind_code;// 用户身份  0：普通用户   1：合伙人身份
    private int isCompanyAccount;//  是否为企业账号:0、企业账号;1、非企业账号；

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

//    public String getUser_id_str() {
//        return user_id_str;
//    }
//
//    public void setUser_id_str(String user_id_str) {
//        this.user_id_str = user_id_str;
//    }

    public String getUs_photo() {
        return us_photo;
    }

    public void setUs_photo(String us_photo) {
        this.us_photo = us_photo;
    }

    public String getUs_nickname() {
        return us_nickname;
    }

    public void setUs_nickname(String us_nickname) {
        this.us_nickname = us_nickname;
    }

    public String getUs_realname() {
        return us_realname;
    }

    public void setUs_realname(String us_realname) {
        this.us_realname = us_realname;
    }

    public String getUs_company() {
        return us_company;
    }

    public void setUs_company(String us_company) {
        this.us_company = us_company;
    }

    public String getUs_jobtitle() {
        return us_jobtitle;
    }

    public void setUs_jobtitle(String us_jobtitle) {
        this.us_jobtitle = us_jobtitle;
    }

    public String getUs_tel() {
        return us_tel;
    }

    public void setUs_tel(String us_tel) {
        this.us_tel = us_tel;
    }

//    public String getUser_id_referee() {
//        return user_id_referee;
//    }
//
//    public void setUser_id_referee(String user_id_referee) {
//        this.user_id_referee = user_id_referee;
//    }
//
//    public String getUs_balance() {
//        return us_balance;
//    }
//
//    public void setUs_balance(String us_balance) {
//        this.us_balance = us_balance;
//    }

    public String getUs_balance_str() {
        return us_balance_str;
    }

    public void setUs_balance_str(String us_balance_str) {
        this.us_balance_str = us_balance_str;
    }

    public String getUs_report_effective_rate() {
        return us_report_effective_rate;
    }

    public void setUs_report_effective_rate(String us_report_effective_rate) {
        this.us_report_effective_rate = us_report_effective_rate;
    }

//    public String getUs_createdtime() {
//        return us_createdtime;
//    }
//
//    public void setUs_createdtime(String us_createdtime) {
//        this.us_createdtime = us_createdtime;
//    }

    public int getUs_partner_kind_code() {
        return us_partner_kind_code;
    }

    public void setUs_partner_kind_code(int us_partner_kind_code) {
        this.us_partner_kind_code = us_partner_kind_code;
    }

    public int getIsCompanyAccount() {
        return isCompanyAccount;
    }

    public void setIsCompanyAccount(int isCompanyAccount) {
        this.isCompanyAccount = isCompanyAccount;
    }
}
