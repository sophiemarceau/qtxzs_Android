package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.MyMsgEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.wecoo.qutianxia.utils.LogUtil;
import com.yolanda.nohttp.rest.Response;

/**
 * 设置消息为已读的请求
 **/

public class SetMsgReadedRequest extends BaseRequest {

    public SetMsgReadedRequest() {
        super(WebUrl.updateSysMsgToRead);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what) {
        CallServer.getInstance().add(context, false, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                LogUtil.d("请求成功，消息置为已读");
            }

            @Override
            public void onRequestFailed(int what, Response<String> FailedData) {

            }
        });
    }

}
