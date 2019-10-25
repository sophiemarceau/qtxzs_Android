package com.wecoo.qutianxia.base;

import com.wecoo.qutianxia.application.WKApplication;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.requestjson.WebUrl;
import com.wecoo.qutianxia.utils.AppInfoUtil;
import com.wecoo.qutianxia.utils.LogUtil;
import com.wecoo.qutianxia.utils.SPUtils;
import com.wecoo.qutianxia.utils.SSLContextUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;

import java.io.InputStream;
import java.io.Serializable;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by mwl on 2016/11/2.
 * 请求的基类
 */

public class BaseRequest implements Serializable {

    // 创建请求
    public Request<String> request;
    // 返回的 单值
    public String getresult = "result";
    // 返回的 对象
    public String getdto = "dto";
    // 返回的 列表
    public String getlist = "list";

    private SSLSocketFactory socketFactory;

    public BaseRequest(String url) {
        request = NoHttp.createStringRequest(WebUrl.root_Url + url, RequestMethod.POST);
//        httpsNoVerify();
        httpsVerify(WebUrl.root_Url);
        request.add("qtx_auth", (String) SPUtils.get(WKApplication.getAppContext(), Configs.qtx_auth, ""));
        request.add("android_Id", AppInfoUtil.getInstance().getTokenM2());
        request.add("source", 1);
    }

    /**
     * Https请求，带证书。
     */
    private void httpsVerify(String url) {
        if (url.startsWith("https://")) {
            try {
                InputStream is = WKApplication.getInstance().getAssets().open("srca.cer");
                socketFactory = SSLContextUtil.getSSLSocketFactory(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (socketFactory != null){
                request.setSSLSocketFactory(socketFactory);
                //  忽略IP（证书颁发者，正式证书需要注释）
                request.setHostnameVerifier(SSLContextUtil.HOSTNAME_VERIFIER);
            }
        }
    }

    /**
     * Https请求，不带证书。
     */
    private void httpsNoVerify() {
        SSLContext sslContext = SSLContextUtil.getDefaultSLLContext();
        if (sslContext != null)
            request.setSSLSocketFactory(sslContext.getSocketFactory());
        request.setHostnameVerifier(SSLContextUtil.HOSTNAME_VERIFIER);
//        request(0, httpsRequest, this, false, true);
    }

//    /**
//     * 解析数据 得到 T 集合
//     */
//    public <T> T getObject(String jsonString, Class<T> type) {
//        T t = null;
//        try {
//            Gson gson = new Gson();
//            t = gson.fromJson(jsonString, type);
//        } catch (JsonSyntaxException e) {
//            e.printStackTrace();
//        }
//        return t;
//    }
//
//    /**
//     * 解析数据 得到 一个List<T>集合
//     */
//    public <T> List<T> getList(String jsonString, Class<T> type) {
//        List<T> list = new ArrayList<T>();
//        try {
//            Gson gson = new Gson();
//            list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
//            }.getType());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return list;
//    }
}
