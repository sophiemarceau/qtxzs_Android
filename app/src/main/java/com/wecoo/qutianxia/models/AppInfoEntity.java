package com.wecoo.qutianxia.models;

import java.io.Serializable;

/**
 * Created by mwl on 2016/11/28.
 * 升级和启动的广告实体
 */

public class AppInfoEntity implements Serializable {

    private int result;//0，已是最新版本； 1，用户可选更新； 2,强制更新；
    private String url;   // App升级版本安装地址
    private String pic;   // （图片地址）
    private String pic_url; // （跳转的Url；空时，不做跳转）
    private String version; // 最新版本号
    private String update_content; // 升级内容
    private String project_id; // 项目id

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUpdate_content() {
        return update_content;
    }

    public void setUpdate_content(String update_content) {
        this.update_content = update_content;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }


}
