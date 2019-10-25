package com.wecoo.qutianxia.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mwl on 2017/2/20.
 * 筛选分类的Model
 */

public class ScreenEntity implements Serializable {

    private String title;
    private List<ScreenModel> list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ScreenModel> getList() {
        return list;
    }

    public void setList(List<ScreenModel> list) {
        this.list = list;
    }

}
