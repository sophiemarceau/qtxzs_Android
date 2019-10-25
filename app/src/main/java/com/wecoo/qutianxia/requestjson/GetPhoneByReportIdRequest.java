package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 根据报备 id 加密串获取客户的手机号
 **/

public class GetPhoneByReportIdRequest extends BaseRequest {

    public GetPhoneByReportIdRequest() {
        super(WebUrl.getCustomerTelByReportIdStr);
    }

    // 设置请求的参数
    public void setRequestParms(String report_id_str) {
        request.add("report_id_str", report_id_str);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, false, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                        String phoneStr = jsonObject.getString(getresult);
                        dataClick.onReturnData(what, phoneStr);
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
