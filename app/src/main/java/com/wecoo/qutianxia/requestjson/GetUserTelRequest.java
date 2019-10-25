package com.wecoo.qutianxia.requestjson;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.wecoo.qutianxia.utils.LogUtil;
import com.yolanda.nohttp.rest.Response;

/**
 * 根据用户id获取手机号
 **/

public class GetUserTelRequest extends BaseRequest {

    public GetUserTelRequest() {
        super(WebUrl.getUserTel);
    }

    // 设置请求的参数 (加密后的用户id)
    public void setRequestParms(String user_id_str) {
        request.add("user_id_str", user_id_str);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, false, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (!TextUtils.isEmpty(SucceedData)){
                    JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                    String strTel = jsonObject.getString(getresult);
                    dataClick.onReturnData(what,strTel);
                }
            }

            @Override
            public void onRequestFailed(int what, Response<String> FailedData) {

            }
        });
    }

}
