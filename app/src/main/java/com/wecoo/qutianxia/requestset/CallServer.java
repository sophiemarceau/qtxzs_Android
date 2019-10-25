package com.wecoo.qutianxia.requestset;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.LoginActivity;
import com.wecoo.qutianxia.base.BaseBean;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.utils.LogUtil;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.ToastUtil;
import com.wecoo.qutianxia.widget.LoadProgressWidget;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadQueue;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

/**
 * Created by mwl on 2016/11/2.
 * 请求队列的单例模式封装
 */

public class CallServer {

    private static CallServer instance = null;
    private int RequestQueueNumber = 3;
    private LoadProgressWidget loadProgress = null;
    // 下载队列.
    private static DownloadQueue downloadQueue;
    /**
     * 请求队列。
     */
    private RequestQueue requestQueue;

    private CallServer() {
        requestQueue = NoHttp.newRequestQueue(RequestQueueNumber);
    }

    /**
     * 请求队列。
     */
    public synchronized static CallServer getInstance() {
        if (instance == null)
            synchronized (CallServer.class) {
                if (instance == null)
                    instance = new CallServer();
            }
        return instance;
    }

    /**
     * 下载队列
     **/
    public static DownloadQueue getDownloadInstance() {
        if (downloadQueue == null)
            downloadQueue = NoHttp.newDownloadQueue(2);
        return downloadQueue;
    }

    /**
     * 添加一个请求到请求队列。
     *
     * @param isshowLoad     是否显示加载条
     * @param what           用来标志请求, 当多个请求使用同一个Listener时, 在回调方法中会返回这个what。
     * @param request        请求对象。
     * @param ServerListener 自定义结果回调对象。
     */
    public <T> void add(final Context context, final boolean isshowLoad, int what, Request<T> request, final CallServerInterface<T> ServerListener) {
        request.setCancelSign(context);
        requestQueue.add(what, request, new OnResponseListener<T>() {
            @Override
            public void onStart(int i) {
                // 开始请求
                if (isshowLoad) {
                    showDialog(context);
                }
            }

            @Override
            public void onSucceed(int i, Response<T> response) {
                // 结束请求 关闭加载框
                if (isshowLoad) {
                    closeDialog();
                }
                // 请求成功
                String result = (String) response.get();
                LogUtil.i("result = ", result);
                try {
                    BaseBean baseBean = JSONObject.parseObject(result, BaseBean.class);
                    if (baseBean.getFlag() == 10000) { // 提现时没有提现密码
                        ServerListener.onRequestSucceed(i, "" + baseBean.getFlag());
                    } else if (baseBean.getFlag() == 99999) { // 提现时提现密码错误
                        ServerListener.onRequestSucceed(i, "" + baseBean.getFlag());
                    } else if (baseBean.getFlag() == Constants.QUESTSUCCESS) { // 执行操作成功
                        ServerListener.onRequestSucceed(i, baseBean.getData());
                    } else if (baseBean.getFlag() == Constants.QUESTLOGIN) {  // 登录失效
                        ToastUtil.showShort(context, "用户失效，请重新登录");
                        SPUtils.put(context, Configs.qtx_auth, "");
                        SPUtils.put(context, Configs.user_id, "");
                        SPUtils.put(context, Configs.user_Tel, "");
                        context.startActivity(new Intent(context, LoginActivity.class));
                    } else {
                        if (!TextUtils.isEmpty(baseBean.getMsg())) {
                            ToastUtil.showShort(context, baseBean.getMsg());
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onFailed(int i, Response<T> response) {
                // 关闭加载框
                if (isshowLoad) {
                    closeDialog();
                }
                // 请求失败
                if (!NetWorkState.isNetworkAvailable(context)) {// 网络不好。
                    ToastUtil.showShort(context, context.getString(R.string.load_data_nonetwork));
                } else if (response.getException() instanceof NetworkError) {
                    ToastUtil.showShort(context, context.getString(R.string.load_data_nonetwork));
                } else if (response.getException() instanceof TimeoutError) {
                    ToastUtil.showShort(context, context.getString(R.string.net_error_timeout));
                }
                ServerListener.onRequestFailed(i, response);
            }

            @Override
            public void onFinish(int i) {
                // 结束请求 关闭加载框
                if (isshowLoad) {
                    closeDialog();
                }
            }
        });
    }

    /**
     * 显示加载条
     */
    private void showDialog(Context context) {
        if (loadProgress == null) {
            loadProgress = new LoadProgressWidget(context);
            loadProgress.createDialog();
        }
        loadProgress.showDialog();
    }

    /**
     * 关闭加载条
     */
    private void closeDialog() {
        if (loadProgress != null) {
            loadProgress.closeDialog();
            loadProgress = null;
        }
    }

    /**
     * 取消这个sign标记的所有请求。
     *
     * @param sign 请求的取消标志。
     */
    public void cancelBySign(Object sign) {
        closeDialog();
        requestQueue.cancelBySign(sign);
    }

    /**
     * 取消队列中所有请求。
     */
    public void cancelAll() {
        closeDialog();
        requestQueue.cancelAll();
    }

}
