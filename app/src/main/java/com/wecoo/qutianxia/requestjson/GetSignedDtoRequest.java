package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.ProjectSignedEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;

/**
 * Created by mwl on 2016/11/18.
 * 获取项目的成交名单
 */

public class GetSignedDtoRequest extends BaseRequest {

    public GetSignedDtoRequest() {
        super(WebUrl.searchReportListSignedUpDtoList);
    }

    // 设置请求的参数
    public void setRequestParms(String project_id,int currentPage,int pageSize) {
        request.add("project_id", project_id);
        request.add("_currentPage", currentPage);
        request.add("_pageSize", pageSize);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, boolean isshowLoad, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, isshowLoad, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                        ArrayList<ProjectSignedEntity> dataList = (ArrayList<ProjectSignedEntity>) JSONArray.parseArray(jsonObject.getString(getlist),ProjectSignedEntity.class);
                        dataClick.onReturnData(what, dataList);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onRequestFailed(int what, Response<String> FailedData) {

            }
        });
    }
}
