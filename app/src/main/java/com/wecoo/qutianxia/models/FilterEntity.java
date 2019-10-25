package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl on 16/4/23.
 * Key_Value  数据格式
 */
public class FilterEntity implements Serializable {

    public int id;
    private String code;
    private String name;
    private int iconUrl;
    private String icon_android;
    private String is_hot;
    private boolean isSelected;

    public FilterEntity() {
    }

    public FilterEntity(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public FilterEntity(String code, String name, boolean isSelected) {
        this.code = code;
        this.name = name;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public int getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(int iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIcon_android() {
        return icon_android;
    }

    public void setIcon_android(String icon_android) {
        this.icon_android = icon_android;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }
}
