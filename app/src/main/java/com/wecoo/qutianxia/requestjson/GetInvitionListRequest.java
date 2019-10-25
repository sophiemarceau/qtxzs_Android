package com.wecoo.qutianxia.requestjson;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.InvitationEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 查询邀请记录列表的请求
 **/

public class GetInvitionListRequest extends BaseRequest {

    public GetInvitionListRequest(String getInvitationList) {
        super(getInvitationList);
    }

    // 设置请求的参数
    public void setRequestParms(String sil_id, int currentPage, int pageSize) {
        request.add("sil_id", sil_id); // 邀请记录Id
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
                        JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                        String dtos = jsonObject.getString("dto");
                        if (TextUtils.isEmpty(dtos)) {
                            InvitationEntity entity = JSONObject.parseObject(SucceedData, InvitationEntity.class);
                            dataClick.onReturnData(what, entity);
                        } else {
                            InvitationEntity entity = JSONObject.parseObject(dtos, InvitationEntity.class);
                            dataClick.onReturnData(what, entity);
                        }
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
