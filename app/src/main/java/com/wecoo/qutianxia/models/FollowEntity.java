package com.wecoo.qutianxia.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mwl on 2016/10/25.
 * 关注的数据
 */

public class FollowEntity implements Serializable {

    private int current_page;
    private int total_count;
    private ArrayList<FollowModels> list;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public ArrayList<FollowModels> getList() {
        return list;
    }

    public void setList(ArrayList<FollowModels> list) {
        this.list = list;
    }

    public class FollowModels implements Serializable {

        private String pcr_id; // 关注id
        private String project_id; // 项目id
        private String pcr_createdtime; // 关注创建时间
        private String projectSignedCount; // 成单数量
        private String project_commission_second; // 最高金额
        private String project_name; // 项目名称
        private String project_pic; // 项目图片
        private String project_slogan; // 项目说明
        private int project_status; // 项目状态  1.正常  2.已暂停  3.已结束

        public String getPcr_id() {
            return pcr_id;
        }

        public void setPcr_id(String pcr_id) {
            this.pcr_id = pcr_id;
        }

        public String getProject_id() {
            return project_id;
        }

        public void setProject_id(String project_id) {
            this.project_id = project_id;
        }

        public String getPcr_createdtime() {
            return pcr_createdtime;
        }

        public void setPcr_createdtime(String pcr_createdtime) {
            this.pcr_createdtime = pcr_createdtime;
        }

        public String getProjectSignedCount() {
            return projectSignedCount;
        }

        public void setProjectSignedCount(String projectSignedCount) {
            this.projectSignedCount = projectSignedCount;
        }

        public String getProject_commission_second() {
            return project_commission_second;
        }

        public void setProject_commission_second(String project_commission_second) {
            this.project_commission_second = project_commission_second;
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

        public String getProject_slogan() {
            return project_slogan;
        }

        public void setProject_slogan(String project_slogan) {
            this.project_slogan = project_slogan;
        }

        public int getProject_status() {
            return project_status;
        }

        public void setProject_status(int project_status) {
            this.project_status = project_status;
        }
    }

}
