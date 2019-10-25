package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 添加跟进记录
 * 签约打款
 **/

public class AddFollowUpRecordRequest extends BaseRequest {

    public AddFollowUpRecordRequest(String url) {
        super(url);
    }

    // 设置请求的参数
    public void setRequestParms(String report_id, String crl_note) {
        request.add("report_id", report_id);
        request.add("crl_note", crl_note);
        request.add("note", crl_note);
    }
    // 设置请求的参数
    public void setRequestParms(String report_id, String areaCode, String crl_note) {
        request.add("report_id", report_id);
        request.add("areaCode", areaCode);
        request.add("crl_note", crl_note);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, true, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                        String result = jsonObject.getString(getresult);
                        dataClick.onReturnData(what, result);
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
