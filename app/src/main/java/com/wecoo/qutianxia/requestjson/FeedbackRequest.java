package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 创建投诉/意见反馈
 **/

public class FeedbackRequest extends BaseRequest {

    public FeedbackRequest() {
        super(WebUrl.submitFeedback);
    }

    // 设置请求的参数
    public void setRequestParms(FeedbackParms parms) {
        request.add("feedback_content", parms.feedback_content);
        request.add("_feedback_pics", parms._feedback_pics);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, true, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                        int result = jsonObject.getInteger(getresult);
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

    public static class FeedbackParms {
        public String feedback_content; // 投诉内容
        public String _feedback_pics;  // 投诉图片
    }
}
