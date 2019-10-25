package com.wecoo.qutianxia.models;

import com.wecoo.qutianxia.base.BaseBean;

import java.util.List;

/**
 * Created by mwl on 2017/01/19.
 * 提现记录
 */

public class ApplyRecordEntity extends BaseBean {

    private List<ApplyRecordModel> list;

    public List<ApplyRecordModel> getList() {
        return list;
    }

    public void setList(List<ApplyRecordModel> list) {
        this.list = list;
    }
}
