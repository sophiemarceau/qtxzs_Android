package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.BankInfoEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 上一次取现记录
 **/

public class LastWithdrawalRequest extends BaseRequest {

    public LastWithdrawalRequest(String url) {
        super(url);
    }

    // 设置请求的参数  1：银行卡   2  支付宝
    public void setRequestParms(int swa_type) {
        request.add("swa_type", swa_type);
    }

    // 设置请求的参数  swa_id
    public void setRequestParms(String swa_id) {
        request.add("swa_id", swa_id);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, false, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                        BankInfoEntity entity = JSONObject.parseObject(jsonObject.getString(getdto), BankInfoEntity.class);
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
