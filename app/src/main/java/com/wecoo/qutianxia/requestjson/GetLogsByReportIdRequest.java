package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.MyReportEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 根据报备 id 获取跟进记录
 **/

public class GetLogsByReportIdRequest extends BaseRequest {

    public GetLogsByReportIdRequest() {
        super(WebUrl.searchCustomerReportLogs);
    }

    // 设置请求的参数
    public void setRequestParms(String report_id) {
        request.add("report_id", report_id);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, boolean isShowLoad, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, isShowLoad, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        MyReportEntity entity = JSONObject.parseObject(SucceedData, MyReportEntity.class);
                        dataClick.onReturnData(what, entity);
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
