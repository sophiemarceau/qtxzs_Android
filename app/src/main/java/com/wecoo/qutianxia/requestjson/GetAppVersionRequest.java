package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.AppInfoEntity;
import com.wecoo.qutianxia.models.BannerEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;

/**
 * 获取App新版本
 **/

public class GetAppVersionRequest extends BaseRequest {

    public GetAppVersionRequest() {
        super(WebUrl.checkForUpdates);
    }

    // 设置请求的参数
    public void setRequestParms(String version) {
        request.add("version", version); // 当前客户端版本号（格式如2.3.1；初始值1.0.0）
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, false, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        AppInfoEntity entity = JSONObject.parseObject(SucceedData, AppInfoEntity.class);
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
}
