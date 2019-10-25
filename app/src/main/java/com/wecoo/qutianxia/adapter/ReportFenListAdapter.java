package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.models.ReportFenListEntity;

import java.util.List;

/**
 * Created by mwl on 16/4/23.
 * 报备质量分变动明细的数据适配
 */
public class ReportFenListAdapter extends BaseListAdapter<ReportFenListEntity.ReportFenModel> {

    public ReportFenListAdapter(Context context, List<ReportFenListEntity.ReportFenModel> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.balancelist_item_view, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ReportFenListEntity.ReportFenModel model = getItem(position);
        if (model != null) {
            if (model.getSrl_addsubflag() == 1) {
//                holder.tvNum.setTextColor(Color.RED);
                holder.tvNum.setText(" + " + model.getSrl_number());
            } else {
//                holder.tvNum.setTextColor(Color.GREEN);
                holder.tvNum.setText(" - "+model.getSrl_number());
            }
            holder.tvDesc.setText(model.getSrl_desc());
            holder.tvTime.setText(model.getSrl_createdtime());
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvNum, tvDesc, tvTime;

        ViewHolder(View view) {
            tvNum = (TextView) view.findViewById(R.id.balancelist_item_num);
            tvDesc = (TextView) view.findViewById(R.id.balancelist_item_desc);
            tvTime = (TextView) view.findViewById(R.id.balancelist_item_time);
        }
    }
}
