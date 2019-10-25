package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl on 2017/01/19.
 * 提现记录的Model
 */

public class ApplyRecordModel implements Serializable {

    private String user_id;  // 用户ID
    private String swa_id;// 记录id
    private String swa_sum;// 申请金额
    private int swa_status;// 申请状态
    private String swa_createdtime;// 申请时间
    private String swa_status_name;// 申请状态名称
    private String swa_sum_str;// 申请金额
    private String swal_opertype_name;// 申请进度描述
    private String swal_id;// 申请进度id
    private int swal_opertype;// 提现操作日志类型 1，提交了提现申请；2，实名认证通过审核；3，实名认证失败；4，打款失败；5，重新提交；6，已经成功打款
    private String swal_createdtime;// 申请进度时间
    private String swa_type_str;// 申请类型（String）
    private int link;// 是否可修改信息 (0 不可修改  1可修改)
    private int swa_type;// 申请类型(int 1银行卡 2支付宝)
    private String swal_desc;// 申请描述
    private String us_realname;// 用户真实姓名
    private String account;// 申请账号

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSwa_id() {
        return swa_id;
    }

    public void setSwa_id(String swa_id) {
        this.swa_id = swa_id;
    }

    public String getSwa_sum() {
        return swa_sum;
    }

    public void setSwa_sum(String swa_sum) {
        this.swa_sum = swa_sum;
    }

    public int getSwa_status() {
        return swa_status;
    }

    public void setSwa_status(int swa_status) {
        this.swa_status = swa_status;
    }

    public String getSwa_createdtime() {
        return swa_createdtime;
    }

    public void setSwa_createdtime(String swa_createdtime) {
        this.swa_createdtime = swa_createdtime;
    }

    public String getSwa_status_name() {
        return swa_status_name;
    }

    public void setSwa_status_name(String swa_status_name) {
        this.swa_status_name = swa_status_name;
    }

    public String getSwa_sum_str() {
        return swa_sum_str;
    }

    public void setSwa_sum_str(String swa_sum_str) {
        this.swa_sum_str = swa_sum_str;
    }

    public String getSwal_opertype_name() {
        return swal_opertype_name;
    }

    public void setSwal_opertype_name(String swal_opertype_name) {
        this.swal_opertype_name = swal_opertype_name;
    }

    public String getSwal_id() {
        return swal_id;
    }

    public void setSwal_id(String swal_id) {
        this.swal_id = swal_id;
    }

    public int getSwal_opertype() {
        return swal_opertype;
    }

    public void setSwal_opertype(int swal_opertype) {
        this.swal_opertype = swal_opertype;
    }

    public String getSwal_createdtime() {
        return swal_createdtime;
    }

    public void setSwal_createdtime(String swal_createdtime) {
        this.swal_createdtime = swal_createdtime;
    }

    public String getSwa_type_str() {
        return swa_type_str;
    }

    public void setSwa_type_str(String swa_type_str) {
        this.swa_type_str = swa_type_str;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }

    public int getSwa_type() {
        return swa_type;
    }

    public void setSwa_type(int swa_type) {
        this.swa_type = swa_type;
    }

    public String getSwal_desc() {
        return swal_desc;
    }

    public void setSwal_desc(String swal_desc) {
        this.swal_desc = swal_desc;
    }

    public String getUs_realname() {
        return us_realname;
    }

    public void setUs_realname(String us_realname) {
        this.us_realname = us_realname;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
