package com.wecoo.qutianxia.models;

import com.wecoo.qutianxia.base.BaseBean;

import java.util.ArrayList;

/**
 * Created by mwl on 2016/11/7.
 * 报备列表
 */

public class MyReportEntity extends BaseBean {

    private ArrayList<ReportModel> list;

    public ArrayList<ReportModel> getList() {
        return list;
    }

    public void setList(ArrayList<ReportModel> list) {
        this.list = list;
    }
}
