package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl on 2016/12/09.
 * 用户实名认证的信息
 */

public class UserIdInfoEntity implements Serializable {

    private String staff_id_idaudited;   // 认证的ID
    private String us_id_number;   // 身份证号
    private String us_id_photo; // 身份证照片
    private int us_id_status_code;//实名认证审核状态code码  0，未提交；1，审核中；2，通过审核；3，未通过审核
    private String us_id_status_name; // 状态名称
    private String us_realname; // 真实姓名

    public String getStaff_id_idaudited() {
        return staff_id_idaudited;
    }

    public void setStaff_id_idaudited(String staff_id_idaudited) {
        this.staff_id_idaudited = staff_id_idaudited;
    }

    public String getUs_id_number() {
        return us_id_number;
    }

    public void setUs_id_number(String us_id_number) {
        this.us_id_number = us_id_number;
    }

    public String getUs_id_photo() {
        return us_id_photo;
    }

    public void setUs_id_photo(String us_id_photo) {
        this.us_id_photo = us_id_photo;
    }

    public int getUs_id_status_code() {
        return us_id_status_code;
    }

    public void setUs_id_status_code(int us_id_status_code) {
        this.us_id_status_code = us_id_status_code;
    }

    public String getUs_id_status_name() {
        return us_id_status_name;
    }

    public void setUs_id_status_name(String us_id_status_name) {
        this.us_id_status_name = us_id_status_name;
    }

    public String getUs_realname() {
        return us_realname;
    }

    public void setUs_realname(String us_realname) {
        this.us_realname = us_realname;
    }
}
