package com.wecoo.qutianxia.models;

import com.wecoo.qutianxia.base.BaseBean;

import java.util.ArrayList;

/**
 * Created by mwl on 2016/10/24.
 * 项目列表的实体类
 */

public class ProjectEntity extends BaseBean{

    private ArrayList<ProjectModels> list;

    public ArrayList<ProjectModels> getList() {
        return list;
    }

    public void setList(ArrayList<ProjectModels> list) {
        this.list = list;
    }

}
