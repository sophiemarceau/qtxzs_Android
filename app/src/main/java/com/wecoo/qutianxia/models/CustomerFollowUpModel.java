package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by wecoo on 2017/5/2.
 * 企业跟进列表
 */

public class CustomerFollowUpModel implements Serializable{

    private int viewType;// ViewType(控制是否显示平台反馈)
    private String crl_id;// 跟进id
    private String crl_kind;// 平台
    private String crl_note;// 描述
    private String crl_createdtime;// 时间
    private String kindName;// 状态
    private String crl_kind_code;//
    private String staff_id;//
    private String report_id;//报备id
    private String si_name;//
    private String report_customer_tel;// 报备客户手机号
    private String projectName;// 项目名称
    private String report_status;// 报备状态code
    private String reportStatusName;// 报备状态

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getCrl_id() {
        return crl_id;
    }

    public void setCrl_id(String crl_id) {
        this.crl_id = crl_id;
    }

    public String getCrl_kind() {
        return crl_kind;
    }

    public void setCrl_kind(String crl_kind) {
        this.crl_kind = crl_kind;
    }

    public String getCrl_note() {
        return crl_note;
    }

    public void setCrl_note(String crl_note) {
        this.crl_note = crl_note;
    }

    public String getCrl_createdtime() {
        return crl_createdtime;
    }

    public void setCrl_createdtime(String crl_createdtime) {
        this.crl_createdtime = crl_createdtime;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public String getCrl_kind_code() {
        return crl_kind_code;
    }

    public void setCrl_kind_code(String crl_kind_code) {
        this.crl_kind_code = crl_kind_code;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getSi_name() {
        return si_name;
    }

    public void setSi_name(String si_name) {
        this.si_name = si_name;
    }

    public String getReport_customer_tel() {
        return report_customer_tel;
    }

    public void setReport_customer_tel(String report_customer_tel) {
        this.report_customer_tel = report_customer_tel;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getReportStatusName() {
        return reportStatusName;
    }

    public void setReportStatusName(String reportStatusName) {
        this.reportStatusName = reportStatusName;
    }

    public String getReport_status() {
        return report_status;
    }

    public void setReport_status(String report_status) {
        this.report_status = report_status;
    }
}
