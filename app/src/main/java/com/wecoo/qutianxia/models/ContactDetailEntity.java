package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by wecoo on 2017/2/20.
 * 人脉详情数据
 */

public class ContactDetailEntity implements Serializable {

    private int level;// 级别
    private String parent_sil_id;// 上一级邀请id
    private String parent_user_nickname;// 上一级昵称
    private String parent_user_tel;// 上一级手机号
    private String sil_self_contribution_sum;// 本人赏金
    private String sil_self_invitation_count;// 本人邀请
    private String sil_self_report_count;// 本人报备
    private String sil_self_signedup_count;// 本人签约
    private String sil_total_contribution_sum;// 累计赏金
    private String sil_total_invitation_count;// 累计邀请
    private String sil_total_report_count;// 累计报备
    private String sil_total_signedup_count;// 累计签约
    private String user_nickname;// 本人昵称
    private String user_tel;// 本人手机号
    private String user_id_str;// 加密的用户ID

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getParent_sil_id() {
        return parent_sil_id;
    }

    public void setParent_sil_id(String parent_sil_id) {
        this.parent_sil_id = parent_sil_id;
    }

    public String getParent_user_nickname() {
        return parent_user_nickname;
    }

    public void setParent_user_nickname(String parent_user_nickname) {
        this.parent_user_nickname = parent_user_nickname;
    }

    public String getParent_user_tel() {
        return parent_user_tel;
    }

    public void setParent_user_tel(String parent_user_tel) {
        this.parent_user_tel = parent_user_tel;
    }

    public String getSil_self_contribution_sum() {
        return sil_self_contribution_sum;
    }

    public void setSil_self_contribution_sum(String sil_self_contribution_sum) {
        this.sil_self_contribution_sum = sil_self_contribution_sum;
    }

    public String getSil_self_invitation_count() {
        return sil_self_invitation_count;
    }

    public void setSil_self_invitation_count(String sil_self_invitation_count) {
        this.sil_self_invitation_count = sil_self_invitation_count;
    }

    public String getSil_self_report_count() {
        return sil_self_report_count;
    }

    public void setSil_self_report_count(String sil_self_report_count) {
        this.sil_self_report_count = sil_self_report_count;
    }

    public String getSil_self_signedup_count() {
        return sil_self_signedup_count;
    }

    public void setSil_self_signedup_count(String sil_self_signedup_count) {
        this.sil_self_signedup_count = sil_self_signedup_count;
    }

    public String getSil_total_contribution_sum() {
        return sil_total_contribution_sum;
    }

    public void setSil_total_contribution_sum(String sil_total_contribution_sum) {
        this.sil_total_contribution_sum = sil_total_contribution_sum;
    }

    public String getSil_total_invitation_count() {
        return sil_total_invitation_count;
    }

    public void setSil_total_invitation_count(String sil_total_invitation_count) {
        this.sil_total_invitation_count = sil_total_invitation_count;
    }

    public String getSil_total_report_count() {
        return sil_total_report_count;
    }

    public void setSil_total_report_count(String sil_total_report_count) {
        this.sil_total_report_count = sil_total_report_count;
    }

    public String getSil_total_signedup_count() {
        return sil_total_signedup_count;
    }

    public void setSil_total_signedup_count(String sil_total_signedup_count) {
        this.sil_total_signedup_count = sil_total_signedup_count;
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

    public String getUser_id_str() {
        return user_id_str;
    }

    public void setUser_id_str(String user_id_str) {
        this.user_id_str = user_id_str;
    }
}
