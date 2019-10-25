package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl on 2016/11/9.
 * 客户信息
 */

public class CustomInfoEntity implements Serializable {

    private String customer_area_agent_name;
    private String customer_area_agent;
    private String customer_budget;
    private String customer_industry;
    private String customer_name;
    private String customer_note;
    private String customer_startdate;
    private String customer_tel;
    private String customer_id;
    private String user_id;
    private int customer_islocked;// 0 为锁定   ；1 锁定中

    public String getCustomer_area_agent_name() {
        return customer_area_agent_name;
    }

    public void setCustomer_area_agent_name(String customer_area_agent_name) {
        this.customer_area_agent_name = customer_area_agent_name;
    }

    public String getCustomer_area_agent() {
        return customer_area_agent;
    }

    public void setCustomer_area_agent(String customer_area_agent) {
        this.customer_area_agent = customer_area_agent;
    }

    public String getCustomer_budget() {
        return customer_budget;
    }

    public void setCustomer_budget(String customer_budget) {
        this.customer_budget = customer_budget;
    }

    public String getCustomer_industry() {
        return customer_industry;
    }

    public void setCustomer_industry(String customer_industry) {
        this.customer_industry = customer_industry;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_note() {
        return customer_note;
    }

    public void setCustomer_note(String customer_note) {
        this.customer_note = customer_note;
    }

    public String getCustomer_startdate() {
        return customer_startdate;
    }

    public void setCustomer_startdate(String customer_startdate) {
        this.customer_startdate = customer_startdate;
    }

    public String getCustomer_tel() {
        return customer_tel;
    }

    public void setCustomer_tel(String customer_tel) {
        this.customer_tel = customer_tel;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getCustomer_islocked() {
        return customer_islocked;
    }

    public void setCustomer_islocked(int customer_islocked) {
        this.customer_islocked = customer_islocked;
    }
}
