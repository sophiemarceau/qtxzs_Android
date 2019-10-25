package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.yolanda.nohttp.rest.Response;

/**
 * 退出登录
 **/

public class LoginOutRequest extends BaseRequest {

    public LoginOutRequest() {
        super(WebUrl.loginOut);
    }


    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what) {
        CallServer.getInstance().add(context, false, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
//                    LogUtil.e("退出APP成功");
                    ToastUtil.showShort(context,"退出登录成功");
                }
            }

            @Override
            public void onRequestFailed(int what, Response<String> FailedData) {

            }
        });
    }

}
