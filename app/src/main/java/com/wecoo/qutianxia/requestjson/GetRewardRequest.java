package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.MyRewardEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 奖励活动的请求
 **/

public class GetRewardRequest extends BaseRequest {

    public GetRewardRequest() {
        super(WebUrl.searchActivityDtos4Show);
    }

    // 设置请求的参数
    // 1，正常；2，已暂停；3，已结束
    public void setRequestParms(int activity_status,int currentPage,int pageSize) {
        request.add("activity_status", activity_status);
        request.add("_currentPage", currentPage);
        request.add("_pageSize", pageSize);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, boolean isshowLoad, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, isshowLoad, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        MyRewardEntity entity = JSONObject.parseObject(SucceedData, MyRewardEntity.class);
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
