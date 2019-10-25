package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.ReportModel;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;

/**
 * 报备进度的请求
 **/

public class GetReportProgressRequest extends BaseRequest {

    public GetReportProgressRequest(String url) {
        super(url);
//        super(WebUrl.searchReportProgress);
    }

    // 设置请求的参数
    public void setRequestParms(String report_id) {
        request.add("report_id", report_id);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, boolean isshowLoad, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, isshowLoad, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                        ArrayList<ReportModel> dataList = (ArrayList<ReportModel>) JSONArray.parseArray(jsonObject.getString(getlist),ReportModel.class);
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
