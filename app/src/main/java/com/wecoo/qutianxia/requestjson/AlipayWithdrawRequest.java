package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.wecoo.qutianxia.utils.MD5Util;
import com.yolanda.nohttp.rest.Response;

/**
 * 支付宝申请提现的请求
 **/

public class AlipayWithdrawRequest extends BaseRequest {

    public AlipayWithdrawRequest(String url) {
        super(url);
    }

    // 设置请求的参数
    public void setRequestParms(AlipayWithdrawParms parms) {
        request.add("swa_name", parms.swa_name);
        request.add("swa_alipay_account", parms.swa_alipay_account);
        request.add("swa_sum", parms.swa_sum);
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

    public static class AlipayWithdrawParms {
        public String swa_name; // 收款人姓名
        public String swa_alipay_account;  // 收款人支付宝账户
        public String swa_sum;  // 提现金额
        public String verifyCode;  // 验证码
        public String swa_id;  // 提现id
        public String us_withdraw_pwd;  // 提现密码
    }
}
