package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.wecoo.qutianxia.utils.LogUtil;
import com.yolanda.nohttp.rest.Response;

/**
 * 添加项目浏览记录
 **/

public class AddProjectRecordRequest extends BaseRequest {

    public AddProjectRecordRequest() {
        super(WebUrl.addProjectBrowsingRecord);
    }

    // 设置请求的参数
    public void setRequestParms(String project_id) {
        request.add("project_id", project_id);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, int what) {
        CallServer.getInstance().add(context, false, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                LogUtil.e("项目浏览记录成功");
            }

            @Override
            public void onRequestFailed(int what, Response<String> FailedData) {

            }
        });
    }
}
