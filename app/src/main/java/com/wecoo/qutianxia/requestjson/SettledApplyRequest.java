package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.wecoo.qutianxia.utils.MD5Util;
import com.yolanda.nohttp.rest.Response;

/**
 * 企业账号申请
 **/

public class SettledApplyRequest extends BaseRequest {

    public SettledApplyRequest() {
        super(WebUrl.createCompany);
    }

    // 设置请求的参数
    public void setRequestParms(SettledApplyParms parms) {
        request.add("staff_name", parms.staff_name);
        request.add("staff_login", parms.staff_login);
        request.add("staff_password", MD5Util.MD5Encode(parms.staff_password));
        request.add("company_name", parms.company_name);
        request.add("company_area", parms.company_area);
        request.add("company_industry", parms.company_industry);
        request.add("company_contact", parms.company_contact);
        request.add("company_tel", parms.company_tel);
        request.add("company_desc", parms.company_desc);
        request.add("_company_license", parms.company_license);
        request.add("_company_card", parms.company_card);
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

    public static class SettledApplyParms {
        public String staff_name; // 姓名
        public String staff_login; // 登录名
        public String staff_password;  // 登录密码
        public String company_name;  // 企业名称
        public String company_area;  // 企业地区（第三级 code 码）
        public String company_industry;  // 所属行业（主行业 code 码）
        public String company_contact;  // 企业联系人
        public String company_tel;  // 企业联系人电话
        public String company_desc;  // 企业简介
        public String company_license;  // 企业营业执照图片
        public String company_card;  // 联系人名片
    }
}
