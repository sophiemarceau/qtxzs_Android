package com.wecoo.qutianxia.models;

import com.wecoo.qutianxia.base.BaseBean;

import java.util.ArrayList;

/**
 * Created by mwl
 * 我的邀请实体类
 */

public class InvitationEntity extends BaseBean{

    private String us_nickname;
    private String us_photo;
    private String user_id;
    private String invitationNum;
    private String reward_balance;
    private String us_tel;
    private ArrayList<InvitationModel> list;

    public String getUs_nickname() {
        return us_nickname;
    }

    public void setUs_nickname(String us_nickname) {
        this.us_nickname = us_nickname;
    }

    public String getUs_photo() {
        return us_photo;
    }

    public void setUs_photo(String us_photo) {
        this.us_photo = us_photo;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getInvitationNum() {
        return invitationNum;
    }

    public void setInvitationNum(String invitationNum) {
        this.invitationNum = invitationNum;
    }

    public String getReward_balance() {
        return reward_balance;
    }

    public void setReward_balance(String reward_balance) {
        this.reward_balance = reward_balance;
    }

    public String getUs_tel() {
        return us_tel;
    }

    public void setUs_tel(String us_tel) {
        this.us_tel = us_tel;
    }

    public ArrayList<InvitationModel> getList() {
        return list;
    }

    public void setList(ArrayList<InvitationModel> list) {
        this.list = list;
    }

}
