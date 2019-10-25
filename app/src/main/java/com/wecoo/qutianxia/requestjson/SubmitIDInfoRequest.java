package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 业务员提交实名认证信息
 **/

public class SubmitIDInfoRequest extends BaseRequest {

    public SubmitIDInfoRequest() {
        super(WebUrl.submitIDInfo);
    }

    // 设置请求的参数
    public void setRequestParms(String us_realname, String us_id_number, String us_id_photo) {
        request.add("us_realname", us_realname);
        request.add("us_id_number", us_id_number);
        request.add("us_id_photo", us_id_photo);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, false, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                        boolean result = jsonObject.getBoolean(getresult);
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
