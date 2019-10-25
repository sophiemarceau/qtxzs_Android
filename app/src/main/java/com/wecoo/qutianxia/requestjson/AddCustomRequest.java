package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.models.LoginEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.wecoo.qutianxia.utils.LogUtil;
import com.yolanda.nohttp.rest.Response;

/**
 * 添加客户
 **/

public class AddCustomRequest extends BaseRequest {

    public AddCustomRequest(String url) {
        super(url);
    }

    // 设置请求的参数
    public void setRequestParms(AddCustomParms parms) {
        request.add("customer_id", parms.customer_id);
        request.add("customer_name", parms.customer_name);
        request.add("customer_tel", parms.customer_tel);
        request.add("customer_industry", parms.customer_industry);
        request.add("customer_area_agent", parms.customer_area_agent);
        request.add("customer_budget", parms.customer_budget);
        request.add("customer_startdate", parms.customer_startdate);
        request.add("_customer_note", parms._customer_note);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, true, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                        String result = jsonObject.getString(getresult);
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

    public static class AddCustomParms {
        public String customer_id; // 客户id
        public String customer_name; // 客户姓名
        public String customer_tel;  // 客户手机号
        public String customer_industry;  // 意向行业code；程序读取数据字典
        public String customer_area_agent;  // 代理区域code；程序读取数据字典
        public String customer_budget;  // 投资预算；1，5-10万；2，10-30万；3，30-50万；4，50万以上
        public String customer_startdate;  // 计划启动时间；1，2周以内；2，1个月以内；3，3个月以内；4，更久
        public String _customer_note;  // 备注说明
    }
}
