package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.MyReportEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 我的报备的请求
 **/

public class GetReportRequest extends BaseRequest {

    public GetReportRequest(String url) {
        super(url);
    }

    // 设置请求的参数
    public void setRequestParms(int currentPage,int pageSize) {
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
                        MyReportEntity reportEntity = JSONObject.parseObject(SucceedData,MyReportEntity.class);
                        dataClick.onReturnData(what, reportEntity);
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
