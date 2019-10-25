package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.UserIdInfoEntity;
import com.wecoo.qutianxia.models.UserInfoEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 获取业务员实名信息
 **/

public class GetIDInfoRequest extends BaseRequest {

    public GetIDInfoRequest() {
        super(WebUrl.getUserSalesmanIDInfoDto);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, false, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                        UserIdInfoEntity entity = JSONObject.parseObject(jsonObject.getString(getdto), UserIdInfoEntity.class);
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
