package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.ApplyProgressActivity;
import com.wecoo.qutianxia.models.ApplyRecordModel;
import com.wecoo.qutianxia.utils.ColorUtil;

import java.util.List;

/**
 * Created by mwl on 2017/01/19.
 * 申请记录的数据适配
 */
public class ApplyRecordAdapter extends BaseListAdapter<ApplyRecordModel> {

    public ApplyRecordAdapter(Context context, List<ApplyRecordModel> list) {
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

        final ApplyRecordModel entity = getItem(position);
        if (entity != null) {
            String name = "提现申请金额: " + entity.getSwa_sum_str();
            holder.tvName.setText(ColorUtil.getTextColor(name,8,name.length()));
            holder.tvContent.setText(entity.getSwa_createdtime());
            holder.txtStatus.setText(entity.getSwa_status_name());
            holder.txtStatus.setBackgroundColor(Color.TRANSPARENT);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MobclickAgent.onEvent(mContext, "Balance_ApplyProgressOnclick");
                    Intent intent = new Intent(mContext,ApplyProgressActivity.class);
                    intent.putExtra("swa_id",entity.getSwa_id());
                    mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvName, tvContent, txtStatus;

        ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.report_item_txtName);
            tvContent = (TextView) view.findViewById(R.id.report_item_txtContent);
            txtStatus = (TextView) view.findViewById(R.id.report_item_txtLookProgress);
        }
    }
}
