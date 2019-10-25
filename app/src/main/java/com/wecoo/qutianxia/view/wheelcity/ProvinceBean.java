package com.wecoo.qutianxia.view.wheelcity;

import java.io.Serializable;
import java.util.List;

/**
 * 省
 **/
public class ProvinceBean implements Serializable {

    private List<ProvinceEntity> data;

    public List<ProvinceEntity> getData() {
        return data;
    }

    public void setData(List<ProvinceEntity> data) {
        this.data = data;
    }

    // 省
    class ProvinceEntity extends CountyEntity {
        private List<CityEntity> list;

        public List<CityEntity> getList() {
            return list;
        }

        public void setList(List<CityEntity> list) {
            this.list = list;
        }
    }

}
