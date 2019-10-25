package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 取消关注请求
 **/

public class CancelFollowRequest extends BaseRequest {

//    public CancelFollowRequest() {
//        super(WebUrl.cancelProjectCollectionRecord);
//    }

    public CancelFollowRequest(String url) {
        super(url);
    }

    // 设置请求的参数
    public void setRequestParms(String pcr_id) {
        request.add("pcr_id", pcr_id);
        request.add("project_id", pcr_id);
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
