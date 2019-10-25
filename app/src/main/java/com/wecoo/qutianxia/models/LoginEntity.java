package com.wecoo.qutianxia.models;

import com.wecoo.qutianxia.base.BaseBean;

/**
 * Created by mwl on 2016/11/7.
 * 登录的Model
 */

public class LoginEntity extends BaseBean {

    private String user_login;// 用户登录账号
    private String qtx_auth;// 用户登录标识
    private String user_id; // 用户ID
    private String user_kind;//业务员

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getQtx_auth() {
        return qtx_auth;
    }

    public void setQtx_auth(String qtx_auth) {
        this.qtx_auth = qtx_auth;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_kind() {
        return user_kind;
    }

    public void setUser_kind(String user_kind) {
        this.user_kind = user_kind;
    }
}
