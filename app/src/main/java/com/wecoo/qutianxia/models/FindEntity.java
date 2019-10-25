package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl on 2016/10/24.
 * 发现的实体类
 */

public class FindEntity implements Serializable{


    private String imgUrl;
    private String content;
    private int status;

    public FindEntity() {
    }

    public FindEntity(String imgUrl, String content, int status) {
        this.imgUrl = imgUrl;
        this.content = content;
        this.status = status;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
