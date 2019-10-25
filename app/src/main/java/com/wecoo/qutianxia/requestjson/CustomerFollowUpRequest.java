package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.CustomerFollowUpEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 客户跟进请求
 **/

public class CustomerFollowUpRequest extends BaseRequest {

    public CustomerFollowUpRequest() {
        super(WebUrl.searchCustomerFollowUpInfo);
    }

    // 设置请求的参数  user_id
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
                        CustomerFollowUpEntity entity = JSONObject.parseObject(SucceedData,CustomerFollowUpEntity.class);
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
