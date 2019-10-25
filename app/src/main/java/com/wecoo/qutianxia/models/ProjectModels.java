package com.wecoo.qutianxia.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mwl on 2016/10/24.
 * 项目实体类
 */

public class ProjectModels implements Serializable {

    private String project_id;   //项目id
    private String project_name;
    private String project_pic;
    private String project_pic_square;
    private String project_pic_ad;
    private int is_hot;
    private int is_newest;
    private int project_commission_second; //佣金范围第二个
    private int project_commission_first;  // 佣金范围第一个
    private int project_signed_count;
    private String project_slogan;
    private String project_industry; // 项目所在行业code
    private String project_commission_note;//佣金说明
    private String project_message;    // 项目消息
    private String project_silk_bag;   // 项目锦囊
    private String company_name;   // 公司名称
    private String project_strengths;
    private String project_industry_name;// 项目所在行业
    private String company_id;  // 公司id
    private ProjectExtDto projectExtDto;
    private ProjectDescDto projectDescDto;
    private List<StringModels> broadMsgs;
    private List<StringModels> jingNs;
    // 企业项目中的字段
    private int reportNum;
    private int reportWaitingAuditingNum;
    private String projectManagerKindName;

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_pic() {
        return project_pic;
    }

    public void setProject_pic(String project_pic) {
        this.project_pic = project_pic;
    }

    public String getProject_pic_square() {
        return project_pic_square;
    }

    public void setProject_pic_square(String project_pic_square) {
        this.project_pic_square = project_pic_square;
    }

    public String getProject_pic_ad() {
        return project_pic_ad;
    }

    public void setProject_pic_ad(String project_pic_ad) {
        this.project_pic_ad = project_pic_ad;
    }

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public int getIs_newest() {
        return is_newest;
    }

    public void setIs_newest(int is_newest) {
        this.is_newest = is_newest;
    }

    public int getProject_commission_second() {
        return project_commission_second;
    }

    public void setProject_commission_second(int project_commission_second) {
        this.project_commission_second = project_commission_second;
    }

    public int getProject_commission_first() {
        return project_commission_first;
    }

    public void setProject_commission_first(int project_commission_first) {
        this.project_commission_first = project_commission_first;
    }

    public int getProject_signed_count() {
        return project_signed_count;
    }

    public void setProject_signed_count(int project_signed_count) {
        this.project_signed_count = project_signed_count;
    }

    public String getProject_slogan() {
        return project_slogan;
    }

    public void setProject_slogan(String project_slogan) {
        this.project_slogan = project_slogan;
    }

    public String getProject_industry() {
        return project_industry;
    }

    public void setProject_industry(String project_industry) {
        this.project_industry = project_industry;
    }

    public String getProject_commission_note() {
        return project_commission_note;
    }

    public void setProject_commission_note(String project_commission_note) {
        this.project_commission_note = project_commission_note;
    }

    public String getProject_message() {
        return project_message;
    }

    public void setProject_message(String project_message) {
        this.project_message = project_message;
    }

    public String getProject_silk_bag() {
        return project_silk_bag;
    }

    public void setProject_silk_bag(String project_silk_bag) {
        this.project_silk_bag = project_silk_bag;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getProject_strengths() {
        return project_strengths;
    }

    public void setProject_strengths(String project_strengths) {
        this.project_strengths = project_strengths;
    }

    public String getProject_industry_name() {
        return project_industry_name;
    }

    public void setProject_industry_name(String project_industry_name) {
        this.project_industry_name = project_industry_name;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public ProjectExtDto getProjectExtDto() {
        return projectExtDto;
    }

    public void setProjectExtDto(ProjectExtDto projectExtDto) {
        this.projectExtDto = projectExtDto;
    }

    public ProjectDescDto getProjectDescDto() {
        return projectDescDto;
    }

    public void setProjectDescDto(ProjectDescDto projectDescDto) {
        this.projectDescDto = projectDescDto;
    }

    public List<StringModels> getBroadMsgs() {
        return broadMsgs;
    }

    public void setBroadMsgs(List<StringModels> broadMsgs) {
        this.broadMsgs = broadMsgs;
    }

    public List<StringModels> getJingNs() {
        return jingNs;
    }

    public void setJingNs(List<StringModels> jingNs) {
        this.jingNs = jingNs;
    }

    public class ProjectExtDto implements Serializable {
        //                "pe_collected_count": 0,
//                "pe_browsed_count": 1,
//                "pe_report_count": 0,
//                "pe_signed_count": 0,
//                "project_id": 71
        private int pe_signed_count;

        public int getPe_signed_count() {
            return pe_signed_count;
        }

        public void setPe_signed_count(int pe_signed_count) {
            this.pe_signed_count = pe_signed_count;
        }
    }

    // class  项目详情下方的介绍和政策
    public class ProjectDescDto implements Serializable {

        private String project_policy;// 招商说明（）政策
        private String project_desc;//项目介绍
        private String project_desc_url;
        private String project_policy_url;

        public String getProject_policy() {
            return project_policy;
        }

        public void setProject_policy(String project_policy) {
            this.project_policy = project_policy;
        }

        public String getProject_desc() {
            return project_desc;
        }

        public void setProject_desc(String project_desc) {
            this.project_desc = project_desc;
        }

        public String getProject_policy_url() {
            return project_policy_url;
        }

        public void setProject_policy_url(String project_policy_url) {
            this.project_policy_url = project_policy_url;
        }

        public String getProject_desc_url() {
            return project_desc_url;
        }

        public void setProject_desc_url(String project_desc_url) {
            this.project_desc_url = project_desc_url;
        }
    }

    public int getReportNum() {
        return reportNum;
    }

    public void setReportNum(int reportNum) {
        this.reportNum = reportNum;
    }

    public int getReportWaitingAuditingNum() {
        return reportWaitingAuditingNum;
    }

    public void setReportWaitingAuditingNum(int reportWaitingAuditingNum) {
        this.reportWaitingAuditingNum = reportWaitingAuditingNum;
    }

    public String getProjectManagerKindName() {
        return projectManagerKindName;
    }

    public void setProjectManagerKindName(String projectManagerKindName) {
        this.projectManagerKindName = projectManagerKindName;
    }
}
