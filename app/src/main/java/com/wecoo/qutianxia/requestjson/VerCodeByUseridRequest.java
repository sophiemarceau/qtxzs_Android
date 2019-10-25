package com.wecoo.qutianxia.requestjson;

import android.content.Context;
import android.text.TextUtils;

import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.wecoo.qutianxia.utils.LogUtil;
import com.yolanda.nohttp.rest.Response;

/**
 * 根据用户 ID 获取验证码
 **/

public class VerCodeByUseridRequest extends BaseRequest {

    public VerCodeByUseridRequest() {
        super(WebUrl.sendValidateCodeSmsByUserId);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what) {
        CallServer.getInstance().add(context, false, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (!TextUtils.isEmpty(SucceedData)){
                    LogUtil.i("SucceedData = ", SucceedData);
                }
            }

            @Override
            public void onRequestFailed(int what, Response<String> FailedData) {

            }
        });
    }

}
