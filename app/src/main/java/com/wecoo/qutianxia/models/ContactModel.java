package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by wecoo on 2017/2/20.
 * 人脉列表Model
 */

public class ContactModel implements Serializable {

    private String user_id_str;// 用户id
    private String self_contribution_sum; // 价钱数量
    private String self_report_count; // 用户报备数
    private String sil_id; // 人脉id
    private String user_nickname; // 用户昵称
    private String user_tel; // 用户电话
    private int level; // 用户级别
    private String contribution_sum; // 收益金额
    private String describe; // 收益描述
    private String sal_createdtime; // 收益描述

    public String getUser_id_str() {
        return user_id_str;
    }

    public void setUser_id_str(String user_id_str) {
        this.user_id_str = user_id_str;
    }

    public String getSelf_contribution_sum() {
        return self_contribution_sum;
    }

    public void setSelf_contribution_sum(String self_contribution_sum) {
        this.self_contribution_sum = self_contribution_sum;
    }

    public String getSelf_report_count() {
        return self_report_count;
    }

    public void setSelf_report_count(String self_report_count) {
        this.self_report_count = self_report_count;
    }

    public String getSil_id() {
        return sil_id;
    }

    public void setSil_id(String sil_id) {
        this.sil_id = sil_id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getContribution_sum() {
        return contribution_sum;
    }

    public void setContribution_sum(String contribution_sum) {
        this.contribution_sum = contribution_sum;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getSal_createdtime() {
        return sal_createdtime;
    }

    public void setSal_createdtime(String sal_createdtime) {
        this.sal_createdtime = sal_createdtime;
    }
}
