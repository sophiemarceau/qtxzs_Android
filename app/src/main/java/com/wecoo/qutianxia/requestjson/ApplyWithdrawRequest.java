package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.wecoo.qutianxia.utils.MD5Util;
import com.yolanda.nohttp.rest.Response;

/**
 * 银行卡申请提现的请求
 **/

public class ApplyWithdrawRequest extends BaseRequest {

    public ApplyWithdrawRequest(String url) {
        super(url);
    }

    // 设置请求的参数
    public void setRequestParms(ApplyWithdrawParms parms) {
        request.add("swa_name", parms.swa_name);
        request.add("swa_bank", parms.swa_bank);
        request.add("swa_bank_fullname", parms.swa_bank_fullname);
        request.add("swa_card_no", parms.swa_card_no);
        request.add("swa_sum", parms.swa_sum);
        request.add("swa_card_area", parms.swa_card_area);
        request.add("verifyCode", parms.verifyCode);
        request.add("swa_id", parms.swa_id);
        request.add("us_withdraw_pwd", MD5Util.MD5Encode(parms.us_withdraw_pwd));
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, false, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    if ("10000".equals(SucceedData)) {
                        dataClick.onReturnData(what, "10000");
                    } else if ("99999".equals(SucceedData)) {
                        dataClick.onReturnData(what, "99999");
                    } else {
                        try {
                            JSONObject jsonObject = JSONObject.parseObject(SucceedData);
                            String result = jsonObject.getString(getresult);
                            dataClick.onReturnData(what, result);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onRequestFailed(int what, Response<String> FailedData) {

            }
        });
    }

    public static class ApplyWithdrawParms {
        public String swa_name; // 收款人姓名
        public String swa_bank;  // 收款人银行code 编码
        public String swa_bank_fullname;  // 开户行名称
        public String swa_card_no;  // 银行卡卡号
        public String swa_sum;  // 提现金额
        public String swa_card_area;  // 开户行地区
        public String verifyCode;  // 开户行地区
        public String swa_id;  // 提现id
        public String us_withdraw_pwd;  // 提现密码
    }
}
