package com.wecoo.qutianxia.models;

import com.wecoo.qutianxia.base.BaseBean;

import java.util.ArrayList;

/**
 * Created by mwl on 2016/11/7.
 * 项目的分类行业数据
 */

public class LookupEntity extends BaseBean {

    private ArrayList<FilterEntity> result;

    public ArrayList<FilterEntity> getResult() {
        return result;
    }

    public void setResult(ArrayList<FilterEntity> result) {
        this.result = result;
    }
}
