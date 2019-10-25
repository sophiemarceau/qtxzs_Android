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
 * Created by mwl on 17/6/6.
 * 跟进记录的数据适配
 */
public class FollowUpRecordAdapter extends BaseListAdapter<ReportModel> {

    public FollowUpRecordAdapter(Context context, List<ReportModel> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.followup_record_item_view, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.tvNote.setTextColor(ContextCompat.getColor(mContext,R.color.wecoo_theme_color));
            holder.vTop.setVisibility(View.INVISIBLE);
            if (getCount() == 1) {
                holder.vButtom.setVisibility(View.INVISIBLE);
            } else {
                holder.vButtom.setVisibility(View.VISIBLE);
            }
            holder.imgStatus.setImageResource(R.mipmap.icon_timeaxis_red);
        } else if (position == getCount() - 1) {
            holder.tvNote.setTextColor(ContextCompat.getColor(mContext,R.color.wecoo_gray5));
            holder.vTop.setVisibility(View.VISIBLE);
            holder.vButtom.setVisibility(View.INVISIBLE);
            holder.imgStatus.setImageResource(R.mipmap.icon_timeaxis_gray);
        } else {
            holder.tvNote.setTextColor(ContextCompat.getColor(mContext,R.color.wecoo_gray5));
            holder.vTop.setVisibility(View.VISIBLE);
            holder.vButtom.setVisibility(View.VISIBLE);
            holder.imgStatus.setImageResource(R.mipmap.icon_timeaxis_gray);
        }
        ReportModel entity = getItem(position);
        if (entity != null) {
            holder.bindData(entity);
        }

        return convertView;
    }

    private class ViewHolder {
        View vTop, vButtom;
        ImageView imgStatus;
        TextView tvNote, tvName, tvTime;

        ViewHolder(View view) {
            vTop = view.findViewById(R.id.followUp_Record_view_top);
            vButtom = view.findViewById(R.id.followUp_Record_view_buttom);
            imgStatus = (ImageView) view.findViewById(R.id.followUp_Record_img_status);
            tvNote = (TextView) view.findViewById(R.id.followUp_Record_txt_note);
            tvName = (TextView) view.findViewById(R.id.followUp_Record_text_name);
            tvTime = (TextView) view.findViewById(R.id.followUp_Record_text_time);
        }

        void bindData(ReportModel entity) {
            tvNote.setText(entity.getCrl_note());
            tvName.setText(entity.getSi_name() + entity.getKindName());
            tvTime.setText(entity.getCrl_createdtime());
        }
    }
}
