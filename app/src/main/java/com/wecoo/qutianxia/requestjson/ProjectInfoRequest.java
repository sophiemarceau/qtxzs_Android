package com.wecoo.qutianxia.requestjson;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.ProjectModels;
import com.wecoo.qutianxia.models.StringModels;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.wecoo.qutianxia.utils.LogUtil;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取项目详情数据
 **/

public class ProjectInfoRequest extends BaseRequest {

    public ProjectInfoRequest() {
        super(WebUrl.getProjectDto);
    }

    // 设置请求的参数
    public void setRequestParms(String project_id) {
        request.add("project_id", project_id);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, false, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                        ProjectModels models = JSONObject.parseObject(jsonObject.getString(getdto), ProjectModels.class);
                        String msgs = models.getProject_message();
                        if (!TextUtils.isEmpty(msgs)) {
                            JSONArray arr = JSONArray.parseArray(msgs);
                            List<StringModels> msgList = new ArrayList<StringModels>();
                            for (int i = 0; i < arr.size(); i++) {
                                JSONObject temp = (JSONObject) arr.get(i);
                                StringModels model1 = new StringModels();
                                model1.setMsg(temp.getString("msg"));
                                msgList.add(model1);
                            }
                            models.setBroadMsgs(msgList);
                        }
                        String jingNs = models.getProject_silk_bag();
                        if (!TextUtils.isEmpty(jingNs)) {
                            JSONArray arr = JSONArray.parseArray(jingNs);
                            List<StringModels> jNList = new ArrayList<StringModels>();
                            for (int i = 0; i < arr.size(); i++) {
                                JSONObject temp = (JSONObject) arr.get(i);
                                StringModels model2 = new StringModels();
                                model2.setJinNangTit(temp.getString("jinNangTit"));
                                model2.setJinNangUrl(temp.getString("jinNangUrl"));
                                jNList.add(model2);
                            }
                            models.setJingNs(jNList);
                        }
                        dataClick.onReturnData(what, models);
                    } catch (Exception ex) {
                        if (ex instanceof JSONException) {
                            LogUtil.e("解析异常");
                        } else {
                            ex.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onRequestFailed(int what, Response<String> FailedData) {

            }
        });
    }
}
