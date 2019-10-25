package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.enterprise.ExamineProjectActivity;
import com.wecoo.qutianxia.activity.enterprise.FollowUpRecordActivity;
import com.wecoo.qutianxia.activity.enterprise.SignUpforMoneyActivity;
import com.wecoo.qutianxia.listener.NoDoubleClickListener;
import com.wecoo.qutianxia.models.ReportModel;
import com.wecoo.qutianxia.requestjson.GetPhoneByReportIdRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.utils.AppInfoUtil;
import com.wecoo.qutianxia.utils.DensityUtil;

import java.util.List;

/**
 * Created by mwl on 17/6/5.
 * 报备审核的数据适配
 */
public class ProjectAuditAdapter extends BaseListAdapter<ReportModel> {

    private int intRbType = 1;

    public ProjectAuditAdapter(Context context, List<ReportModel> list) {
        super(context, list);
    }

    // 设置状态
    public void setIntRbType(int rbType, List<ReportModel> list) {
        this.intRbType = rbType;
        setData(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.projectaudit_listitem_view, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ReportModel model = getItem(position);
        if (model != null) {
            holder.bindData(model);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvName, tvDate, txtDesc;
        // 沟通记录    拨打电话
        View viewChatLog, viewReviewPhone,viewfengexian;
        TextView txtReturned, txtSure;
        String report_id_str = "", report_id = "";
        TextView tvChatLog,tvReviewPhone;
        LinearLayout viewItem;

        ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.projectAudit_item_txtName);
            tvDate = (TextView) view.findViewById(R.id.projectAudit_item_txtDate);
            txtDesc = (TextView) view.findViewById(R.id.projectAudit_item_txtDesc);
            viewChatLog = view.findViewById(R.id.projectAudit_item_ViewChatLog);
            viewReviewPhone = view.findViewById(R.id.projectAudit_item_ViewReviewPhone);
            txtReturned = (TextView) view.findViewById(R.id.projectAudit_item_txtReturned);
            txtSure = (TextView) view.findViewById(R.id.projectAudit_item_txtSure);
            viewfengexian = view.findViewById(R.id.item_fengexian_view);
            tvChatLog = (TextView) view.findViewById(R.id.projectAudit_item_tvChatLog);
            tvReviewPhone = (TextView) view.findViewById(R.id.projectAudit_item_tvReviewPhone);
            viewItem = (LinearLayout) view.findViewById(R.id.projectAudit_ll_viewItem);
        }

        void bindData(ReportModel model) {
            switch (intRbType) {
                case 1:
                    viewItem.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,DensityUtil.dp2px(mContext,60)));
                    viewfengexian.setVisibility(View.GONE);
                    txtReturned.setVisibility(View.VISIBLE);
                    txtSure.setVisibility(View.VISIBLE);
                    txtSure.setText("确认通过");
                    setDrawTop(tvChatLog,R.mipmap.icon_enterprise_review_chat_log);
                    setDrawTop(tvReviewPhone,R.mipmap.icon_enterprise_review_phone);
                    break;
                case 3:
                    viewItem.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,DensityUtil.dp2px(mContext,60)));
                    viewfengexian.setVisibility(View.GONE);
                    txtReturned.setVisibility(View.VISIBLE);
                    txtSure.setVisibility(View.VISIBLE);
                    txtSure.setText("确认签约");
                    setDrawTop(tvChatLog,R.mipmap.icon_enterprise_review_chat_log);
                    setDrawTop(tvReviewPhone,R.mipmap.icon_enterprise_review_phone);
                    break;
                case 6:
                    viewItem.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,DensityUtil.dp2px(mContext,45)));
                    viewfengexian.setVisibility(View.VISIBLE);
                    txtReturned.setVisibility(View.GONE);
                    txtSure.setVisibility(View.GONE);
                    setDrawLeft(tvChatLog,R.mipmap.icon_enterprise_review_chat_log);
                    setDrawLeft(tvReviewPhone,R.mipmap.icon_enterprise_review_phone);
                    break;
                case 7:
                    viewItem.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,DensityUtil.dp2px(mContext,45)));
                    viewfengexian.setVisibility(View.VISIBLE);
                    txtReturned.setVisibility(View.GONE);
                    txtSure.setVisibility(View.GONE);
                    setDrawLeft(tvChatLog,R.mipmap.icon_enterprise_review_chat_log);
                    setDrawLeft(tvReviewPhone,R.mipmap.icon_enterprise_review_phone);
                    break;
            }
            report_id = model.getReport_id();
            report_id_str = model.getReport_id_str();
            tvName.setText("客户姓名：" + model.getReport_customer_name());
            tvDate.setText(model.getReport_createdtime());
            if (model.getLatestCustomerReportLogDto() != null) {
                txtDesc.setVisibility(View.VISIBLE);
                txtDesc.setText(model.getLatestCustomerReportLogDto().getCrl_note());
            }else {
                txtDesc.setVisibility(View.GONE);
            }
            //
            viewChatLog.setOnClickListener(clickListener);
            viewReviewPhone.setOnClickListener(clickListener);
            txtReturned.setOnClickListener(clickListener);
            txtSure.setOnClickListener(clickListener);
        }

        // 设置textview的左边图标
        private void setDrawLeft(TextView txt, int drawId) {
            Drawable drawLeft = ContextCompat.getDrawable(mContext, drawId);
            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示  
            drawLeft.setBounds(0, 0, DensityUtil.dp2px(mContext, 20), DensityUtil.dp2px(mContext, 20));
            txt.setCompoundDrawablePadding(20);
            txt.setCompoundDrawables(drawLeft, null, null, null);
        }

        // 设置textview的上边图标
        private void setDrawTop(TextView txt, int drawId) {
            Drawable drawTop = ContextCompat.getDrawable(mContext, drawId);
            drawTop.setBounds(0, 0, DensityUtil.dp2px(mContext, 20), DensityUtil.dp2px(mContext, 20));
            txt.setCompoundDrawables(null, drawTop, null, null);
        }

        // view监听
        private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                switch (v.getId()) {
                    case R.id.projectAudit_item_ViewChatLog:
                        Intent intent1 = new Intent(mContext, FollowUpRecordActivity.class);
                        intent1.putExtra("intRbType", intRbType);
                        intent1.putExtra("report_id", report_id);
                        intent1.putExtra("report_id_str", report_id_str);
                        mContext.startActivity(intent1);
                        break;
                    case R.id.projectAudit_item_ViewReviewPhone:
                        getPhoneById(report_id_str);
                        break;
                    case R.id.projectAudit_item_txtReturned:
                        Intent intent2 = new Intent(mContext, ExamineProjectActivity.class);
                        intent2.putExtra("viewType", txtReturned.getText().toString());
                        intent2.putExtra("report_id", report_id);
                        mContext.startActivity(intent2);
                        break;
                    case R.id.projectAudit_item_txtSure:
                        Intent intent3 = new Intent();
                        if ("确认签约".equals(txtSure.getText().toString())) {
                            intent3.setClass(mContext, SignUpforMoneyActivity.class);
                        } else {
                            intent3.setClass(mContext, ExamineProjectActivity.class);
                            intent3.putExtra("viewType", txtSure.getText().toString());
                        }
                        intent3.putExtra("report_id", report_id);
                        mContext.startActivity(intent3);
                        break;
                }
            }
        };
    }

    private void getPhoneById(String report_id_str) {
        if (TextUtils.isEmpty(report_id_str)) return;
        GetPhoneByReportIdRequest byReportIdRequest = new GetPhoneByReportIdRequest();
        byReportIdRequest.setRequestParms(report_id_str);
        byReportIdRequest.setReturnDataClick(mContext, 1, new ReturnDataClick() {
            @Override
            public void onReturnData(int what, Object obj) {
                if (obj != null) {
                    String phoneStr = (String) obj;
                    AppInfoUtil.onCallPhone(mContext, phoneStr);
                }
            }
        });
    }

}
