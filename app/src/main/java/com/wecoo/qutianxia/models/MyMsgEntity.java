package com.wecoo.qutianxia.models;

import com.wecoo.qutianxia.base.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mwl on 2016/11/7.
 * 消息的实体类
 */

public class MyMsgEntity extends BaseBean{

    private ArrayList<MsgModels> list;

    public ArrayList<MsgModels> getList() {
        return list;
    }

    public void setList(ArrayList<MsgModels> list) {
        this.list = list;
    }

    public class MsgModels implements Serializable{
        private String msg_id;
        private String msg_title;
        private String user_id_receiver;
        private String msg_content;
        private String msg_createdtime;
        private String msg_page_to; // 0 无跳转   1| 105  id为105，跳转报备进度    2  账户明细

        public String getMsg_id() {
            return msg_id;
        }

        public void setMsg_id(String msg_id) {
            this.msg_id = msg_id;
        }

        public String getMsg_title() {
            return msg_title;
        }

        public void setMsg_title(String msg_title) {
            this.msg_title = msg_title;
        }

        public String getUser_id_receiver() {
            return user_id_receiver;
        }

        public void setUser_id_receiver(String user_id_receiver) {
            this.user_id_receiver = user_id_receiver;
        }

        public String getMsg_content() {
            return msg_content;
        }

        public void setMsg_content(String msg_content) {
            this.msg_content = msg_content;
        }

        public String getMsg_createdtime() {
            return msg_createdtime;
        }

        public void setMsg_createdtime(String msg_createdtime) {
            this.msg_createdtime = msg_createdtime;
        }

        public String getMsg_page_to() {
            return msg_page_to;
        }

        public void setMsg_page_to(String msg_page_to) {
            this.msg_page_to = msg_page_to;
        }
    }

}
