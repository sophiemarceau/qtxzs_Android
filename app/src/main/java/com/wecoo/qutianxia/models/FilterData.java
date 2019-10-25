package com.wecoo.qutianxia.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mwl on 16/4/23.
 * 项目的排序和分类数组
 */
public class FilterData implements Serializable {

    private List<FilterEntity> sorts;
    private List<FilterEntity> filters;

    public List<FilterEntity> getSorts() {
        return sorts;
    }

    public void setSorts(List<FilterEntity> sorts) {
        this.sorts = sorts;
    }

    public List<FilterEntity> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterEntity> filters) {
        this.filters = filters;
    }
}
