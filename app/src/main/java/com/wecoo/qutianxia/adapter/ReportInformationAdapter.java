package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.ReportProgressActivity;
import com.wecoo.qutianxia.listener.NoDoubleClickListener;
import com.wecoo.qutianxia.models.CustomerFollowUpModel;
import com.wecoo.qutianxia.utils.DensityUtil;

import java.util.List;

/**
 * Created by mwl on 2017/04/21.
 * 已推荐客户的适配
 */

public class ReportInformationAdapter extends BaseListAdapter<CustomerFollowUpModel> {

    public ReportInformationAdapter(Context context, List<CustomerFollowUpModel> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.report_information_item_view, null);

            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.bindHolder(i, getItem(i));
        return view;
    }

    boolean indexType = true;
    private class ViewHolder {
        View topView, ViewClick;
        TextView txtType, txtTitle, txtStatus, txtTime, txtNote;

        public ViewHolder(View view) {
            ViewClick = view.findViewById(R.id.reportInformation_ll_click);
            topView = view.findViewById(R.id.reportInformation_topView);
            txtType = (TextView) view.findViewById(R.id.reportInformation_txt_type);
            txtTitle = (TextView) view.findViewById(R.id.reportInformation_txt_title);
            txtStatus = (TextView) view.findViewById(R.id.reportInformation_txt_status);
            txtTime = (TextView) view.findViewById(R.id.reportInformation_txt_createtime);
            txtNote = (TextView) view.findViewById(R.id.reportInformation_txt_note);
        }

        void bindHolder(int position, final CustomerFollowUpModel model) {
            if (model == null) return;
            if (position == 0 && model.getViewType() == 1) {
                txtStatus.setText("查看");
                txtTitle.setText("渠天下客服");
                topView.setVisibility(View.VISIBLE);
                setdrawRight(txtType, R.mipmap.icon_report_feedback, "平台反馈");
            } else {
                txtStatus.setText(model.getReportStatusName());
                txtTitle.setText(model.getProjectName());
                if (position == 0) {
                    indexType = false;
                    topView.setVisibility(View.VISIBLE);
                    setdrawRight(txtType, R.mipmap.icon_report_follow_up, "企业跟进");
                } else if (indexType && position == 1){
                    topView.setVisibility(View.VISIBLE);
                    setdrawRight(txtType, R.mipmap.icon_report_follow_up, "企业跟进");
                } else {
                    topView.setVisibility(View.GONE);
                }
            }
            txtTime.setText(model.getCrl_createdtime());
            txtNote.setText(model.getCrl_note());
            ViewClick.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    MobclickAgent.onEvent(mContext, "ReportProgressOnlick");
                    Intent intent = new Intent(mContext, ReportProgressActivity.class);
                    intent.putExtra("questType", txtTitle.getText().toString());
                    intent.putExtra("report_id", model.getReport_id());
                    mContext.startActivity(intent);
                }
            });
        }

        private void setdrawRight(TextView txt, int drawId, String content) {
            txt.setText(content);
            Drawable drawLeft = ContextCompat.getDrawable(mContext, drawId);
            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示  
            drawLeft.setBounds(0, 0, DensityUtil.dp2px(mContext, 14), DensityUtil.dp2px(mContext, 14));
            txt.setCompoundDrawables(drawLeft, null, null, null);
        }
    }

}
