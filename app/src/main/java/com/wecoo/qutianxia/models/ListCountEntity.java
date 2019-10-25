package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl on 2016/11/9.
 * 我的模块中的条目相关数量
 */

public class ListCountEntity implements Serializable {

    // 我的报备显示数量
    private int auditCount;
    private int backCustomerReporCount;
    private int followUpCount;
    private int inspectingCount;
    private int signedUpCount;
    // 我的页面显示数量
    private int activityNum; // 活动条目
    private int customerNum; // 客户条目
    private int projectCollectionRecordNum; // 关注条目
    private int invitationNum; // 邀请条目
    private int connectionCount; // 人脉条目
    private int reportWaitingAuditingNum; // 未审核报备数

    public int getAuditCount() {
        return auditCount;
    }

    public void setAuditCount(int auditCount) {
        this.auditCount = auditCount;
    }

    public int getBackCustomerReporCount() {
        return backCustomerReporCount;
    }

    public void setBackCustomerReporCount(int backCustomerReporCount) {
        this.backCustomerReporCount = backCustomerReporCount;
    }

    public int getFollowUpCount() {
        return followUpCount;
    }

    public void setFollowUpCount(int followUpCount) {
        this.followUpCount = followUpCount;
    }

    public int getInspectingCount() {
        return inspectingCount;
    }

    public void setInspectingCount(int inspectingCount) {
        this.inspectingCount = inspectingCount;
    }

    public int getSignedUpCount() {
        return signedUpCount;
    }

    public void setSignedUpCount(int signedUpCount) {
        this.signedUpCount = signedUpCount;
    }

    public int getActivityNum() {
        return activityNum;
    }

    public void setActivityNum(int activityNum) {
        this.activityNum = activityNum;
    }

    public int getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(int customerNum) {
        this.customerNum = customerNum;
    }

    public int getProjectCollectionRecordNum() {
        return projectCollectionRecordNum;
    }

    public void setProjectCollectionRecordNum(int projectCollectionRecordNum) {
        this.projectCollectionRecordNum = projectCollectionRecordNum;
    }

    public int getInvitationNum() {
        return invitationNum;
    }

    public void setInvitationNum(int invitationNum) {
        this.invitationNum = invitationNum;
    }

    public int getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(int connectionCount) {
        this.connectionCount = connectionCount;
    }

    public int getReportWaitingAuditingNum() {
        return reportWaitingAuditingNum;
    }

    public void setReportWaitingAuditingNum(int reportWaitingAuditingNum) {
        this.reportWaitingAuditingNum = reportWaitingAuditingNum;
    }
}
