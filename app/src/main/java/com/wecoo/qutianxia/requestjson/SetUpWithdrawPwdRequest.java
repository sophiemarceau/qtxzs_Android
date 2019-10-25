package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.wecoo.qutianxia.utils.MD5Util;
import com.yolanda.nohttp.rest.Response;

/**
 * 设置（修改）提现密码
 **/

public class SetUpWithdrawPwdRequest extends BaseRequest {

    public SetUpWithdrawPwdRequest(String url) {
        super(url);
    }

    // 设置判断密码请求的参数（密码）
//    public void setRequestParms(String us_withdraw_pwd) {
//        request.add("us_withdraw_pwd", MD5Util.MD5Encode(us_withdraw_pwd));
//    }
    // 设置密码请求的参数(密码   重置密码)
    public void setRequestParms(String us_withdraw_pwd,String repeat_us_withdraw_pwd) {
        request.add("us_withdraw_pwd", MD5Util.MD5Encode(us_withdraw_pwd));
        request.add("repeat_us_withdraw_pwd", MD5Util.MD5Encode(repeat_us_withdraw_pwd));
    }
    // 修改密码请求的参数(原密码    新密码   重置密码)
    public void setRequestParms(String us_withdraw_pwd,String new_us_withdraw_pwd,String repeat_us_withdraw_pwd) {
        request.add("us_withdraw_pwd", MD5Util.MD5Encode(us_withdraw_pwd));
        request.add("new_us_withdraw_pwd", MD5Util.MD5Encode(new_us_withdraw_pwd));
        request.add("repeat_us_withdraw_pwd", MD5Util.MD5Encode(repeat_us_withdraw_pwd));
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, true, what, request, new CallServerInterface<String>() {
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
