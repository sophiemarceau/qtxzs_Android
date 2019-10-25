package com.wecoo.qutianxia.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwl on 2017/2/20.
 * 创建JSON数据  并且解析
 */

public class JsonUtils {

    private static JsonUtils jsonUtils = null;
    public static JsonUtils getIntance(){
        if (jsonUtils == null){
            jsonUtils = new JsonUtils();
        }
        return jsonUtils;
    }

    public String getContactJson(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append("{\"title\": \"所在人脉\",\"list\": [{\"name\": \"全部人脉\",\"code\": \"0\",\"type\": \"1\"},{\"name\": \"一级人脉\",\"code\": \"1\",\"type\": \"0\"},{\"name\": \"二级人脉\",\"code\": \"2\",\"type\": \"0\"},{\"name\": \"三级人脉\",\"code\": \"3\",\"type\": \"0\"}]}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
    public String getTypesJson(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append("{\"title\": \"赏金类型\",\"list\": [{\"name\": \"全部类型\",\"code\": \"0\",\"type\": \"1\"},{\"name\": \"报备通过\",\"code\": \"1\",\"type\": \"0\"},{\"name\": \"签约成功\",\"code\": \"2\",\"type\": \"0\"},{\"name\": \"活动奖励\",\"code\": \"3\",\"type\": \"0\"}]}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
    public String getTimesJson(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append("{\"title\": \"赏金时间\",\"list\": [{\"name\": \"全部时间\",\"code\": \"0\",\"type\": \"1\"},{\"name\": \"近7天\",\"code\": \"1\",\"type\": \"0\"},{\"name\": \"近1个月\",\"code\": \"2\",\"type\": \"0\"},{\"name\": \"近3个月\",\"code\": \"3\",\"type\": \"0\"}]}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
    /**
     * 解析数据 得到 T 集合
     */
    public <T> T getObject(String jsonString, Class<T> type) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 解析数据 得到 一个List<T>集合
     */
    public <T> List<T> getList(String jsonString, Class<T> type) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
