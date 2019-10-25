package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.ReportInfoActivity;
import com.wecoo.qutianxia.models.ReportModel;

import java.util.List;

/**
 * Created by mwl on 16/4/23.
 * 报备的数据适配
 */
public class ReportAdapter extends BaseListAdapter<ReportModel> {

    public ReportAdapter(Context context, List<ReportModel> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.report_listitem_view, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ReportModel entity = getItem(position);
        if (entity != null) {
            holder.tvName.setText(entity.getReport_customer_name() + "  " + entity.getReport_customer_tel());
            holder.tvContent.setText(entity.getProject_industry_name());
            holder.viewLookInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MobclickAgent.onEvent(mContext, "ReportInfoOnlick");
                    Intent intent = new Intent(mContext,ReportInfoActivity.class);
                    intent.putExtra("report_id",entity.getReport_id());
                    intent.putExtra("butnStatus",1);
                    mContext.startActivity(intent);
                }
            });
            holder.viewLookProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MobclickAgent.onEvent(mContext, "ReportInfoOnlick");
                    Intent intent = new Intent(mContext,ReportInfoActivity.class);
                    intent.putExtra("report_id",entity.getReport_id());
                    intent.putExtra("butnStatus",2);
                    mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvName, tvContent;
        View viewLookInfo, viewLookProgress;
        ViewHolder(View view){
            tvName = (TextView) view.findViewById(R.id.report_item_txtName);
            tvContent = (TextView) view.findViewById(R.id.report_item_txtContent);
            viewLookInfo = view.findViewById(R.id.report_item_llLookInfo);
            viewLookProgress = view.findViewById(R.id.report_item_txtLookProgress);
        }
    }
}
