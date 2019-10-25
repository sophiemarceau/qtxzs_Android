package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 添加报备
 **/

public class AddReportRequest extends BaseRequest {

    public AddReportRequest() {
        super(WebUrl.addCustomerReport);
    }

    // 设置请求的参数
    public void setRequestParms(AddReportParms parms) {
        request.add("_project_id", parms.project_id);
        request.add("report_customer_name", parms.report_customer_name);
        request.add("report_customer_tel", parms.report_customer_tel);
        request.add("_report_customer_industry", parms.report_customer_industry);
        request.add("report_customer_area_agent", parms.report_customer_area_agent);
        request.add("report_customer_budget", parms.report_customer_budget);
        request.add("report_customer_startdate", parms.report_customer_startdate);
        request.add("_report_customer_note", parms._report_customer_note);
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

    public static class AddReportParms {
        public String project_id; // 项目Id，直接报备/按类目报备时，此参数空
        public String report_customer_name; // 报备客户姓名
        public String report_customer_tel;  // 报备客户手机号
        public String report_customer_industry;  // 报备意向行业code；程序读取数据字典
        public String report_customer_area_agent;  // 报备代理区域code；程序读取数据字典
        public String report_customer_budget;  // 报备投资预算；1，5-10万；2，10-30万；3，30-50万；4，50万以上
        public String report_customer_startdate;  // 报备计划启动时间；1，2周以内；2，1个月以内；3，3个月以内；4，更久
        public String _report_customer_note;  // 报备备注说明
    }
}
