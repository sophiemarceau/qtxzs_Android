package com.wecoo.qutianxia.base;

import java.io.Serializable;

/**
 * Created by mwl on 2016/10/20.
 * 实体类的返回值
 */
public class BaseBean implements Serializable{

    private int flag;	//状态码 （0，失败；1，成功）
    private String data;	// json 数据
    private String msg;	// 错误信息
    private int current_page;// 当前页
    private int total_count;// 数据总数

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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
}
