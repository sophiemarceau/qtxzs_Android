package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl on 2016/11/11.
 * Banner 数据
 */

public class BannerEntity implements Serializable {

    private String ad_id;  // banner_Id
    private String ad_pic;// banner_图片
    private String ad_position;// banner_索引
    private String ad_status;// banner_状态
    private String ad_url;// banner_跳转地址
    private String ad_activity_name;// banner_名称
    private String ad_note;// banner_描述
    private String project_id; // 项目id

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getAd_pic() {
        return ad_pic;
    }

    public void setAd_pic(String ad_pic) {
        this.ad_pic = ad_pic;
    }

    public String getAd_position() {
        return ad_position;
    }

    public void setAd_position(String ad_position) {
        this.ad_position = ad_position;
    }

    public String getAd_status() {
        return ad_status;
    }

    public void setAd_status(String ad_status) {
        this.ad_status = ad_status;
    }

    public String getAd_url() {
        return ad_url;
    }

    public void setAd_url(String ad_url) {
        this.ad_url = ad_url;
    }

    public String getAd_activity_name() {
        return ad_activity_name;
    }

    public void setAd_activity_name(String ad_activity_name) {
        this.ad_activity_name = ad_activity_name;
    }

    public String getAd_note() {
        return ad_note;
    }

    public void setAd_note(String ad_note) {
        this.ad_note = ad_note;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }
}
