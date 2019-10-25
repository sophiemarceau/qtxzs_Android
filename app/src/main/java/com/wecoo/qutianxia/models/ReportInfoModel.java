package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl on 2016/11/25.
 * 报备详情数据
 */

public class ReportInfoModel implements Serializable {

    private String report_id;
    private String report_customer_name;
    private String report_customer_tel;
    private String report_customer_industry_name;
    private String report_customer_area_agent_name;
    private String report_customer_area_agent;
    private String report_customer_budget_name;
    private String report_customer_startdate_name;
    private String report_customer_note;

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getReport_customer_name() {
        return report_customer_name;
    }

    public void setReport_customer_name(String report_customer_name) {
        this.report_customer_name = report_customer_name;
    }

    public String getReport_customer_tel() {
        return report_customer_tel;
    }

    public void setReport_customer_tel(String report_customer_tel) {
        this.report_customer_tel = report_customer_tel;
    }

    public String getReport_customer_industry_name() {
        return report_customer_industry_name;
    }

    public void setReport_customer_industry_name(String report_customer_industry_name) {
        this.report_customer_industry_name = report_customer_industry_name;
    }

    public String getReport_customer_area_agent_name() {
        return report_customer_area_agent_name;
    }

    public void setReport_customer_area_agent_name(String report_customer_area_agent_name) {
        this.report_customer_area_agent_name = report_customer_area_agent_name;
    }

    public String getReport_customer_area_agent() {
        return report_customer_area_agent;
    }

    public void setReport_customer_area_agent(String report_customer_area_agent) {
        this.report_customer_area_agent = report_customer_area_agent;
    }

    public String getReport_customer_budget_name() {
        return report_customer_budget_name;
    }

    public void setReport_customer_budget_name(String report_customer_budget_name) {
        this.report_customer_budget_name = report_customer_budget_name;
    }

    public String getReport_customer_startdate_name() {
        return report_customer_startdate_name;
    }

    public void setReport_customer_startdate_name(String report_customer_startdate_name) {
        this.report_customer_startdate_name = report_customer_startdate_name;
    }

    public String getReport_customer_note() {
        return report_customer_note;
    }

    public void setReport_customer_note(String report_customer_note) {
        this.report_customer_note = report_customer_note;
    }
}
