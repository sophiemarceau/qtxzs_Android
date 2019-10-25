package com.wecoo.qutianxia.models;

import com.wecoo.qutianxia.base.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mwl on 2016/11/7.
 * 我的账户变动的实体类
 */

public class MyBalanceEntity extends BaseBean{

    private ArrayList<BalanceModels> list;

    public ArrayList<BalanceModels> getList() {
        return list;
    }

    public void setList(ArrayList<BalanceModels> list) {
        this.list = list;
    }

    public class BalanceModels implements Serializable{

        private String sal_id;
        private String sal_desc;
        private String sal_sum;
        private String sal_sum_str;
        private String sal_createdtime;
        private String sal_addsubflag_name;
        private int sal_addsubflag_code;

        public String getSal_id() {
            return sal_id;
        }

        public void setSal_id(String sal_id) {
            this.sal_id = sal_id;
        }

        public String getSal_desc() {
            return sal_desc;
        }

        public void setSal_desc(String sal_desc) {
            this.sal_desc = sal_desc;
        }

        public String getSal_sum_str() {
            return sal_sum_str;
        }

        public void setSal_sum_str(String sal_sum_str) {
            this.sal_sum_str = sal_sum_str;
        }

        public String getSal_sum() {
            return sal_sum;
        }

        public void setSal_sum(String sal_sum) {
            this.sal_sum = sal_sum;
        }

        public String getSal_createdtime() {
            return sal_createdtime;
        }

        public void setSal_createdtime(String sal_createdtime) {
            this.sal_createdtime = sal_createdtime;
        }

        public String getSal_addsubflag_name() {
            return sal_addsubflag_name;
        }

        public void setSal_addsubflag_name(String sal_addsubflag_name) {
            this.sal_addsubflag_name = sal_addsubflag_name;
        }

        public int getSal_addsubflag_code() {
            return sal_addsubflag_code;
        }

        public void setSal_addsubflag_code(int sal_addsubflag_code) {
            this.sal_addsubflag_code = sal_addsubflag_code;
        }
    }

}
