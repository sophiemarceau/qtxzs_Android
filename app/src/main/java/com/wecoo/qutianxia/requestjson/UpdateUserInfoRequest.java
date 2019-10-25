package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.UserInfoEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 修改个人信息
 **/

public class UpdateUserInfoRequest extends BaseRequest {

    public UpdateUserInfoRequest() {
        super(WebUrl.updateUserSalesmanInfo);
    }

    // 设置请求的参数 昵称   头像   公司   职位

    public void setRequestParms(String us_nickname, String us_photo, String us_company, String us_jobtitle) {
        request.add("us_nickname", us_nickname);
        request.add("_us_photo", us_photo);
        request.add("_us_company", us_company);
        request.add("_us_jobtitle", us_jobtitle);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, true, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                        boolean entity = jsonObject.getBoolean(getresult);
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
