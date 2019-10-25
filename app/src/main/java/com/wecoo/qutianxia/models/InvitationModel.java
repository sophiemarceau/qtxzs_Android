package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl
 * 邀请列表实体类
 */

public class InvitationModel implements Serializable {

    private String beInviterSalesman_status; // 状态
    private String beInviterSalesman_date;// 时间
    private String beInviterSalesman_describe;// 描述
    private String beInviterSalesman_balance;// 赏金
    private String user_id;
    private String us_photo;
    private String us_nickname;// 昵称
    private String us_tel; // 电话
    private String sil_id; // 邀请ID

    public String getBeInviterSalesman_status() {
        return beInviterSalesman_status;
    }

    public void setBeInviterSalesman_status(String beInviterSalesman_status) {
        this.beInviterSalesman_status = beInviterSalesman_status;
    }

    public String getBeInviterSalesman_date() {
        return beInviterSalesman_date;
    }

    public void setBeInviterSalesman_date(String beInviterSalesman_date) {
        this.beInviterSalesman_date = beInviterSalesman_date;
    }

    public String getBeInviterSalesman_describe() {
        return beInviterSalesman_describe;
    }

    public void setBeInviterSalesman_describe(String beInviterSalesman_describe) {
        this.beInviterSalesman_describe = beInviterSalesman_describe;
    }

    public String getBeInviterSalesman_balance() {
        return beInviterSalesman_balance;
    }

    public void setBeInviterSalesman_balance(String beInviterSalesman_balance) {
        this.beInviterSalesman_balance = beInviterSalesman_balance;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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

    public String getUs_tel() {
        return us_tel;
    }

    public void setUs_tel(String us_tel) {
        this.us_tel = us_tel;
    }

    public String getSil_id() {
        return sil_id;
    }

    public void setSil_id(String sil_id) {
        this.sil_id = sil_id;
    }
}
