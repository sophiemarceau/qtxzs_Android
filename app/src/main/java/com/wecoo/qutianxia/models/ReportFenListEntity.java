package com.wecoo.qutianxia.models;

import com.wecoo.qutianxia.base.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mwl on 2016/11/7.
 * 报备质量分变动的实体类
 */

public class ReportFenListEntity extends BaseBean{

    private ArrayList<ReportFenModel> list;

    public ArrayList<ReportFenModel> getList() {
        return list;
    }

    public void setList(ArrayList<ReportFenModel> list) {
        this.list = list;
    }

    public class ReportFenModel implements Serializable{

        private String srl_id;   // 记录id
        private int srl_type;// 类型：1通过报备   2退回报备   3签约     4由“通过报备”退回“报备待审核”
        private String srl_type_name;// 标题
        private int srl_addsubflag;// 1，增加   2减少
        private String srl_number;// 质量分
        private String srl_effective_rate_after;// 增减操作后的质量分
        private String srl_desc;// 描述
        private String srl_createdtime;// 创建时间

        public String getSrl_id() {
            return srl_id;
        }

        public void setSrl_id(String srl_id) {
            this.srl_id = srl_id;
        }

        public int getSrl_type() {
            return srl_type;
        }

        public void setSrl_type(int srl_type) {
            this.srl_type = srl_type;
        }

        public String getSrl_type_name() {
            return srl_type_name;
        }

        public void setSrl_type_name(String srl_type_name) {
            this.srl_type_name = srl_type_name;
        }

        public int getSrl_addsubflag() {
            return srl_addsubflag;
        }

        public void setSrl_addsubflag(int srl_addsubflag) {
            this.srl_addsubflag = srl_addsubflag;
        }

        public String getSrl_number() {
            return srl_number;
        }

        public void setSrl_number(String srl_number) {
            this.srl_number = srl_number;
        }

        public String getSrl_effective_rate_after() {
            return srl_effective_rate_after;
        }

        public void setSrl_effective_rate_after(String srl_effective_rate_after) {
            this.srl_effective_rate_after = srl_effective_rate_after;
        }

        public String getSrl_desc() {
            return srl_desc;
        }

        public void setSrl_desc(String srl_desc) {
            this.srl_desc = srl_desc;
        }

        public String getSrl_createdtime() {
            return srl_createdtime;
        }

        public void setSrl_createdtime(String srl_createdtime) {
            this.srl_createdtime = srl_createdtime;
        }
    }

}
