package com.wecoo.qutianxia.models;

import com.wecoo.qutianxia.base.BaseBean;

import java.util.List;

/**
 * Created by wecoo on 2017/2/20.
 * 人脉列表Entity
 */

public class ContactEntity extends BaseBean {

    private String numCount;
    private List<ContactModel> list;

    public String getNumCount() {
        return numCount;
    }

    public void setNumCount(String numCount) {
        this.numCount = numCount;
    }

    public List<ContactModel> getList() {
        return list;
    }

    public void setList(List<ContactModel> list) {
        this.list = list;
    }
}
