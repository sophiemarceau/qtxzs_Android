package com.wecoo.qutianxia.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wecoo on 2016/10/28.
 * 我的客户数据
 */

public class MyCustomEntity implements Serializable {

    private int current_page;// 当前页
    private int total_count;// 总页数
    private ArrayList<CustomInfoEntity> list;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public ArrayList<CustomInfoEntity> getList() {
        return list;
    }

    public void setList(ArrayList<CustomInfoEntity> list) {
        this.list = list;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

}
