package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.ContactEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 获取我的人脉收益
 **/

public class GetContactProfitRequest extends BaseRequest {

    public GetContactProfitRequest() {
        super(WebUrl.searchConnectionContributionDtos);
    }

    // 设置请求的参数
    public void setRequestParms(ContactProfitParms parms) {
        request.add("_currentPage", parms._currentPage);
        request.add("_pageSize", parms._pageSize);
        request.add("_sil_level", parms._sil_level);
        request.add("_sal_partner_income_kind", parms._sal_partner_income_kind);
        request.add("_sal_createdtime", parms._sal_createdtime);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, boolean isshowLoad, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, isshowLoad, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        ContactEntity entity = JSONObject.parseObject(SucceedData, ContactEntity.class);
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

    public static class ContactProfitParms {
        public int _currentPage; // 当前页码
        public int _pageSize; // 每页条数
        // 人脉  (1, "一级人脉"), (2, "二级人脉"), (3, "三级人脉");
        public String _sil_level;
        // 类型  (1, "报备通过"), (2, "签约成功"), (3, "奖励活动");
        public String _sal_partner_income_kind;
        // 时间  ("1", "近7天"), ("2", "近1个月"), ("3", "近3个月");
        public String _sal_createdtime;
    }
}
