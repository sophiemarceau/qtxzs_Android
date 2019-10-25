package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl on 2016/11/24.
 * 某些列表只包含字符串
 */

public class StringModels implements Serializable{

    private String msg;
    private String jinNangTit;
    private String jinNangUrl;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getJinNangTit() {
        return jinNangTit;
    }

    public void setJinNangTit(String jinNangTit) {
        this.jinNangTit = jinNangTit;
    }

    public String getJinNangUrl() {
        return jinNangUrl;
    }

    public void setJinNangUrl(String jinNangUrl) {
        this.jinNangUrl = jinNangUrl;
    }
}
