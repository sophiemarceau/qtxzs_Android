package com.wecoo.qutianxia.requestset;

import com.yolanda.nohttp.rest.Response;

/**
 * Created by mwl on 2016/11/2.
 * 网络请求的回掉
 */

public interface CallServerInterface<T> {

    void onRequestSucceed(int what, String SucceedData);
    void onRequestFailed(int what, Response<T> FailedData);
}
