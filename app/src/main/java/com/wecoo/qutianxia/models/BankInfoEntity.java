package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl on 2016/11/11.
 * 取现记录数据
 */

public class BankInfoEntity implements Serializable {

    private String staff_id_paid;
    private String swa_bank;         // 银行code
    private String swa_bank_fullname;
    private String swa_bank_name;       // 银行名字
    private String swa_card_no;        // 申请卡号
    private String swa_createdtime;
    private String swa_id;             // 申请id
    private String swa_modifiedtime;
    private String swa_name;
    private String swa_note_failed;
    private String swa_paidtime;
    private String swa_status;    // 申请状态
    private String swa_status_name;
    private String swa_sum;         // 申请金额整数
    private String swa_sum_str;         // 申请金额double
    private String us_realname;         // 申请人真实名字
    private String swa_alipay_account;         // 支付宝账户
    private String user_login;         // 申请人手机号
    private String swa_type;         // 申请类型
    private String swa_card_area;         // 开户行地区
    private String swa_card_area_name;     // 开户行地区

    public String getStaff_id_paid() {
        return staff_id_paid;
    }

    public void setStaff_id_paid(String staff_id_paid) {
        this.staff_id_paid = staff_id_paid;
    }

    public String getSwa_bank() {
        return swa_bank;
    }

    public void setSwa_bank(String swa_bank) {
        this.swa_bank = swa_bank;
    }

    public String getSwa_bank_fullname() {
        return swa_bank_fullname;
    }

    public void setSwa_bank_fullname(String swa_bank_fullname) {
        this.swa_bank_fullname = swa_bank_fullname;
    }

    public String getSwa_bank_name() {
        return swa_bank_name;
    }

    public void setSwa_bank_name(String swa_bank_name) {
        this.swa_bank_name = swa_bank_name;
    }

    public String getSwa_card_no() {
        return swa_card_no;
    }

    public void setSwa_card_no(String swa_card_no) {
        this.swa_card_no = swa_card_no;
    }

    public String getSwa_createdtime() {
        return swa_createdtime;
    }

    public void setSwa_createdtime(String swa_createdtime) {
        this.swa_createdtime = swa_createdtime;
    }

    public String getSwa_id() {
        return swa_id;
    }

    public void setSwa_id(String swa_id) {
        this.swa_id = swa_id;
    }

    public String getSwa_modifiedtime() {
        return swa_modifiedtime;
    }

    public void setSwa_modifiedtime(String swa_modifiedtime) {
        this.swa_modifiedtime = swa_modifiedtime;
    }

    public String getSwa_name() {
        return swa_name;
    }

    public void setSwa_name(String swa_name) {
        this.swa_name = swa_name;
    }

    public String getSwa_note_failed() {
        return swa_note_failed;
    }

    public void setSwa_note_failed(String swa_note_failed) {
        this.swa_note_failed = swa_note_failed;
    }

    public String getSwa_paidtime() {
        return swa_paidtime;
    }

    public void setSwa_paidtime(String swa_paidtime) {
        this.swa_paidtime = swa_paidtime;
    }

    public String getSwa_status() {
        return swa_status;
    }

    public void setSwa_status(String swa_status) {
        this.swa_status = swa_status;
    }

    public String getSwa_status_name() {
        return swa_status_name;
    }

    public void setSwa_status_name(String swa_status_name) {
        this.swa_status_name = swa_status_name;
    }

    public String getSwa_sum() {
        return swa_sum;
    }

    public void setSwa_sum(String swa_sum) {
        this.swa_sum = swa_sum;
    }

    public String getSwa_sum_str() {
        return swa_sum_str;
    }

    public void setSwa_sum_str(String swa_sum_str) {
        this.swa_sum_str = swa_sum_str;
    }

    public String getUs_realname() {
        return us_realname;
    }

    public void setUs_realname(String us_realname) {
        this.us_realname = us_realname;
    }

    public String getSwa_alipay_account() {
        return swa_alipay_account;
    }

    public void setSwa_alipay_account(String swa_alipay_account) {
        this.swa_alipay_account = swa_alipay_account;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getSwa_type() {
        return swa_type;
    }

    public void setSwa_type(String swa_type) {
        this.swa_type = swa_type;
    }

    public String getSwa_card_area() {
        return swa_card_area;
    }

    public void setSwa_card_area(String swa_card_area) {
        this.swa_card_area = swa_card_area;
    }

    public String getSwa_card_area_name() {
        return swa_card_area_name;
    }

    public void setSwa_card_area_name(String swa_card_area_name) {
        this.swa_card_area_name = swa_card_area_name;
    }
}
