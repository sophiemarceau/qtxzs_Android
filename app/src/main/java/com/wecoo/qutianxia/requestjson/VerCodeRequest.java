package com.wecoo.qutianxia.requestjson;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseBean;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.wecoo.qutianxia.utils.LogUtil;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.yolanda.nohttp.rest.Response;

/**
 * 获取验证码
 **/

public class VerCodeRequest extends BaseRequest {

    public VerCodeRequest() {
        super(WebUrl.sendvalidatecodesms);
    }

    // 设置请求的参数
    public void setRequestParms(String tel) {
        request.add("tel", tel);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what/*, final ReturnDataClick dataClick*/) {
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
