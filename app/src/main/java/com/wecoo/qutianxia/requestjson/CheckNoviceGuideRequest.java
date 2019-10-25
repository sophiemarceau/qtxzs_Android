package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 检查是否显示新手引导
 **/

public class CheckNoviceGuideRequest extends BaseRequest {

    public CheckNoviceGuideRequest() {
        super(WebUrl.isShowNewbieGuide);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, false, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {// {"flag":1,"data":{"dto":{"projectId":128,"isShowNewbieGuide":1}}}
                        JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                        String dtos = jsonObject.getString("dto");
                        JSONObject jsonDto = JSONObject.parseObject(dtos);
                        // isShowNewbieGuide   String（是否显示“新手引导”项标识，“1”显示，“0”不显示；）
                        String isShowNewbieGuide = jsonDto.getString("isShowNewbieGuide");
                        String projectId = jsonDto.getString("projectId");
                        dataClick.onReturnData(what, isShowNewbieGuide + "@" + projectId);
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
