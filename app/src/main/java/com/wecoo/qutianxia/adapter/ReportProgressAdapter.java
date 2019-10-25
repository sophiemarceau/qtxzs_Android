package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.models.ReportModel;

import java.util.List;

/**
 * Created by mwl on 16/4/23.
 * 报备进度的数据适配
 */
public class ReportProgressAdapter extends BaseListAdapter<ReportModel> {

    public ReportProgressAdapter(Context context, List<ReportModel> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.reportprogress_item_view, null);
            holder = new ViewHolder();
            holder.vTop = convertView.findViewById(R.id.reportprogress_view_top);
            holder.vButtom = convertView.findViewById(R.id.reportprogress_view_buttom);
            holder.imgStatus = (ImageView) convertView.findViewById(R.id.reportprogress_img_status);
            holder.tvName = (TextView) convertView.findViewById(R.id.rProgress_text_name);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.reportprogress_text_desc);
            holder.tvName.setTextColor(ContextCompat.getColor(mContext,R.color.wecoo_theme_color));
            holder.tvDesc.setVisibility(View.GONE);
            holder.tvContent = (TextView) convertView.findViewById(R.id.reportprogress_text_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.vTop.setVisibility(View.INVISIBLE);
            if (getCount() == 1) {
                holder.vButtom.setVisibility(View.INVISIBLE);
            } else {
                holder.vButtom.setVisibility(View.VISIBLE);
            }
            holder.imgStatus.setImageResource(R.mipmap.icon_timeaxis_red);
        } else if (position == getCount() - 1) {
            holder.vTop.setVisibility(View.VISIBLE);
            holder.vButtom.setVisibility(View.INVISIBLE);
            holder.imgStatus.setImageResource(R.mipmap.icon_timeaxis_gray);
        } else {
            holder.vTop.setVisibility(View.VISIBLE);
            holder.vButtom.setVisibility(View.VISIBLE);
            holder.imgStatus.setImageResource(R.mipmap.icon_timeaxis_gray);
        }
        ReportModel entity = getItem(position);
        if (entity != null) {
            holder.tvName.setText(entity.getCrl_note());
            holder.tvContent.setText(entity.getCrl_createdtime());
        }

        return convertView;
    }

    private class ViewHolder {
        View vTop, vButtom;
        ImageView imgStatus;
        TextView tvName,tvDesc, tvContent;
    }
}
