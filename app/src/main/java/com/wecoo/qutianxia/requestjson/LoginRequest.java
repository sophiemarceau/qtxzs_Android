package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.LoginEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.wecoo.qutianxia.utils.AppInfoUtil;
import com.wecoo.qutianxia.utils.LogUtil;
import com.yolanda.nohttp.rest.Response;

/**
 * 登录
 **/

public class LoginRequest extends BaseRequest {

    public LoginRequest() {
        super(WebUrl.login);
    }

    // 设置请求的参数
    public void setRequestParms(LoginParms parms) {
        request.add("user_login", parms.user_login);
        request.add("verification_code", parms.verification_code);
        request.add("_invitation_code", parms._invitation_code);
        request.add("machine_identification_code", AppInfoUtil.getInstance().getDeviceId());

        LogUtil.e("");
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, true, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        LoginEntity entity = JSONObject.parseObject(SucceedData, LoginEntity.class);
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

    public static class LoginParms {
        public String user_login;
        public String verification_code;
        public String _invitation_code;
    }
}
