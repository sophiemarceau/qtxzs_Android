package com.wecoo.qutianxia.requestjson;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.wecoo.qutianxia.base.BaseRequest;
import com.wecoo.qutianxia.models.ContactEntity;
import com.wecoo.qutianxia.requestset.CallServer;
import com.wecoo.qutianxia.requestset.CallServerInterface;
import com.yolanda.nohttp.rest.Response;

/**
 * 获取我的人脉列表
 **/

public class GetContactListRequest extends BaseRequest {

    public GetContactListRequest() {
        super(WebUrl.searchConnectionDtos);
    }

    // 设置请求的参数
    public void setRequestParms(int currentPage, int pageSize) {
        request.add("_currentPage", currentPage);
        request.add("_pageSize", pageSize);
    }
    // 设置请求的参数
    public void setRequestParms(ContactListParms parms) {
        request.add("_currentPage", parms._currentPage);
        request.add("_pageSize", parms._pageSize);
        request.add("_sil_level", parms._sil_level);
        request.add("_contribution_type", parms._contribution_type);
        request.add("_contribution_sum", parms._contribution_sum);
        request.add("_invitation_count", parms._invitation_count);
        request.add("_report_count", parms._report_count);
        request.add("_signedup_count", parms._signedup_count);
        request.add("_sortType", parms._sortType);
        request.add("_sil_createdtime", parms._sil_createdtime);
        request.add("_sort_desc_or_asc", parms._sort_desc_or_asc);
    }

    // 返回数据的回掉
    public void setReturnDataClick(final Context context, boolean isshowLoad, int what, final ReturnDataClick dataClick) {
        CallServer.getInstance().add(context, isshowLoad, what, request, new CallServerInterface<String>() {
            @Override
            public void onRequestSucceed(int what, String SucceedData) {
                if (SucceedData != null) {
                    try {
                        ContactEntity entity = JSONObject.parseObject(SucceedData, ContactEntity.class);
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

    public static class ContactListParms {
        public int _currentPage; // 当前页码
        public int _pageSize; // 每页条数
        // 人脉筛选  (1, "一级人脉"), (2, "二级人脉"), (3, "三级人脉");
        public String _sil_level;
        // 贡献类型  ("1", "本人贡献"), ("2", "下级人脉贡献"), ("3", "累计贡献");
        public String _contribution_type;
        // 贡献赏金  ("1", "100以内"), ("2", "100-500"), ("3", "500-5000"), ("4", "5000以上");
        public String _contribution_sum;
        // 贡献邀请人数  ("1", "1-5人"), ("2", "5-15人"), ("3", "15-30人"), ("4", "30以上");
        public String _invitation_count;
        // 贡献通过报备数  ("1", "0"), ("2", "1-5"), ("3", "5-10"), ("4", "10-20"), ("5", "20以上");
        public String _report_count;
        // 贡献签约数  ("1", "0"), ("2", "1-5"), ("3", "5-10"), ("4", "10-20"), ("5", "20以上");
        public String _signedup_count;  //
        // 赏金时间   ("1", "近7天"), ("2", "近1个月"), ("3", "近3个月");
        public String _sil_createdtime;  //
        public String _sortType;  // 排序  ("1", "赏金额度"), ("2", "注册时间");
        public String _sort_desc_or_asc;  // 排序  ("1", "降序"), ("2", "升序")
    }
}
