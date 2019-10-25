package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl on 2016/10/31.
 * 报备数据
 */

public class ReportModel implements Serializable{

    private String projectName;  // 项目名字
    private String project_id; // 项目id
    private String report_createdtime;  // 报备创建时间
    private String report_customer_name;  // 报备客户的名字
    private String report_id;  // 报备id
    private String report_id_str;  // 报备id（加密）
    private String report_status;  // 报备状态
    private String crl_createdtime;
    private String crl_id;   // 报备跟踪记录id （报备进度）
    private String crl_kind;
    private String crl_note;
    private String kindName;
    private String report_customer_tel;
    private String si_name;
    private String staff_id;
    private String project_industry_name;// 行业名称
    private LatestCustomerReportLogDto latestCustomerReportLogDto;
    //
//    private String report_customer_name;


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getReport_createdtime() {
        return report_createdtime;
    }

    public void setReport_createdtime(String report_createdtime) {
        this.report_createdtime = report_createdtime;
    }

    public String getReport_customer_name() {
        return report_customer_name;
    }

    public void setReport_customer_name(String report_customer_name) {
        this.report_customer_name = report_customer_name;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getReport_id_str() {
        return report_id_str;
    }

    public void setReport_id_str(String report_id_str) {
        this.report_id_str = report_id_str;
    }

    public String getReport_status() {
        return report_status;
    }

    public void setReport_status(String report_status) {
        this.report_status = report_status;
    }

    public String getCrl_createdtime() {
        return crl_createdtime;
    }

    public void setCrl_createdtime(String crl_createdtime) {
        this.crl_createdtime = crl_createdtime;
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

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public String getReport_customer_tel() {
        return report_customer_tel;
    }

    public void setReport_customer_tel(String report_customer_tel) {
        this.report_customer_tel = report_customer_tel;
    }

    public String getSi_name() {
        return si_name;
    }

    public void setSi_name(String si_name) {
        this.si_name = si_name;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getProject_industry_name() {
        return project_industry_name;
    }

    public void setProject_industry_name(String project_industry_name) {
        this.project_industry_name = project_industry_name;
    }

    public LatestCustomerReportLogDto getLatestCustomerReportLogDto() {
        return latestCustomerReportLogDto;
    }

    public void setLatestCustomerReportLogDto(LatestCustomerReportLogDto latestCustomerReportLogDto) {
        this.latestCustomerReportLogDto = latestCustomerReportLogDto;
    }

    public class LatestCustomerReportLogDto implements Serializable{
        private String crl_note; // 沟通记录内容

        public String getCrl_note() {
            return crl_note;
        }

        public void setCrl_note(String crl_note) {
            this.crl_note = crl_note;
        }
    }
}
