package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.ApplyRecordEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 获取申请提现进度列表
 **/

public class GetWithdrawProgressRequest extends BaseRequest {

    public GetWithdrawProgressRequest() {
        super(WebUrl.getSalesmanWithdrawingApplicationLogList);
    }

    // 设置请求的参数
    public void setRequestParms(String swa_id) {
        request.add("swa_id", swa_id); // 提现id
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, final boolean isshowLoad, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, isshowLoad, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        ApplyRecordEntity entity = JSONObject.parseObject(SucceedData, ApplyRecordEntity.class);
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
