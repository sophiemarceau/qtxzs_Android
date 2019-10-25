package com.wecoo.qutianxia.models;

import com.wecoo.qutianxia.base.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mwl on 2016/11/7.
 * 奖励活动的实体类
 */

public class MyRewardEntity extends BaseBean{

    private ArrayList<RewardModel> list;

    public ArrayList<RewardModel> getList() {
        return list;
    }

    public void setList(ArrayList<RewardModel> list) {
        this.list = list;
    }

    public class RewardModel implements Serializable{

        private String activity_createdtime;
        private String activity_desc;
        private String activity_enddate;
        private String activity_id;
        private String activity_kind;  // 活动类型 1.报备奖励  2.考察奖励  3.签约奖励  4.邀请奖励   5.新手奖励
        private int activity_kind_code;
        private String activity_modifiedtime;
        private String activity_name;
        private String activity_per_sum;
        private String activity_rule_invite;
        private String activity_rule_project;
        private String activity_startdate;
        private String activity_status;// 活动状态  1.正常  2.已暂停  3.已结束

        public String getActivity_createdtime() {
            return activity_createdtime;
        }

        public void setActivity_createdtime(String activity_createdtime) {
            this.activity_createdtime = activity_createdtime;
        }

        public String getActivity_desc() {
            return activity_desc;
        }

        public void setActivity_desc(String activity_desc) {
            this.activity_desc = activity_desc;
        }

        public String getActivity_enddate() {
            return activity_enddate;
        }

        public void setActivity_enddate(String activity_enddate) {
            this.activity_enddate = activity_enddate;
        }

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }

        public String getActivity_kind() {
            return activity_kind;
        }

        public void setActivity_kind(String activity_kind) {
            this.activity_kind = activity_kind;
        }

        public int getActivity_kind_code() {
            return activity_kind_code;
        }

        public void setActivity_kind_code(int activity_kind_code) {
            this.activity_kind_code = activity_kind_code;
        }

        public String getActivity_modifiedtime() {
            return activity_modifiedtime;
        }

        public void setActivity_modifiedtime(String activity_modifiedtime) {
            this.activity_modifiedtime = activity_modifiedtime;
        }

        public String getActivity_name() {
            return activity_name;
        }

        public void setActivity_name(String activity_name) {
            this.activity_name = activity_name;
        }

        public String getActivity_per_sum() {
            return activity_per_sum;
        }

        public void setActivity_per_sum(String activity_per_sum) {
            this.activity_per_sum = activity_per_sum;
        }

        public String getActivity_rule_invite() {
            return activity_rule_invite;
        }

        public void setActivity_rule_invite(String activity_rule_invite) {
            this.activity_rule_invite = activity_rule_invite;
        }

        public String getActivity_rule_project() {
            return activity_rule_project;
        }

        public void setActivity_rule_project(String activity_rule_project) {
            this.activity_rule_project = activity_rule_project;
        }

        public String getActivity_startdate() {
            return activity_startdate;
        }

        public void setActivity_startdate(String activity_startdate) {
            this.activity_startdate = activity_startdate;
        }

        public String getActivity_status() {
            return activity_status;
        }

        public void setActivity_status(String activity_status) {
            this.activity_status = activity_status;
        }
    }

}
