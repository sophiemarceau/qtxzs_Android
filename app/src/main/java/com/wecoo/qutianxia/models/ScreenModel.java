package com.wecoo.qutianxia.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mwl on 2017/2/20.
 * 筛选分类的Model
 */

public class ScreenModel implements Serializable {

    private int id;// 按钮的索引
    private int parentId;// 父列表的索引
    private int position;// 本身的索引
    private boolean isSured = false;// 是否点击确定(默认否)
    private String name;
    private String code;
    private int type;// 状态  1 选中   0 未选中

    public ScreenModel() {
        super();
    }

    public ScreenModel(String name, String code, int type) {
        super();
        this.name = name;
        this.code = code;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isSured() {
        return isSured;
    }

    public void setSured(boolean sured) {
        isSured = sured;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
