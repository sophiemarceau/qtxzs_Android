package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl on 2016/10/25.
 * 我的数据
 */

public class MyDataEntity implements Serializable {

    private int imgResources;
    private int titleResources;
    private String dataNum;
    private Class<?> calssName;

    public MyDataEntity() {
        super();
    }

    public MyDataEntity(int imgResources, int titleResources, String dataNum, Class<?> calssName) {
        super();
        this.imgResources = imgResources;
        this.titleResources = titleResources;
        this.dataNum = dataNum;
        this.calssName = calssName;
    }

    public int getImgRouse() {
        return imgResources;
    }

    public void setImgRouse(int imgRouse) {
        this.imgResources = imgRouse;
    }

    public int getTitleContent() {
        return titleResources;
    }

    public void setTitleContent(int titleContent) {
        this.titleResources = titleContent;
    }


    public String getDataNum() {
        return dataNum;
    }

    public void setDataNum(String dataNum) {
        this.dataNum = dataNum;
    }

    public Class<?> getCalssName() {
        return calssName;
    }

    public void setCalssName(Class<?> calssName) {
        this.calssName = calssName;
    }
}
