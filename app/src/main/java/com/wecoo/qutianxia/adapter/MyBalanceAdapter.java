package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.models.MyBalanceEntity;
import com.wecoo.qutianxia.models.MyMsgEntity;

import java.util.List;

/**
 * Created by mwl on 16/4/23.
 * 余额中变动明细的数据适配
 */
public class MyBalanceAdapter extends BaseListAdapter<MyBalanceEntity.BalanceModels> {

    public MyBalanceAdapter(Context context, List<MyBalanceEntity.BalanceModels> list) {
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

        MyBalanceEntity.BalanceModels model = getItem(position);
        if (model != null) {
            if (model.getSal_addsubflag_code() == 1) {
//                holder.tvNum.setTextColor(Color.RED);
                holder.tvNum.setText(" + " + model.getSal_sum_str());
            } else {
//                holder.tvNum.setTextColor(Color.GREEN);
                holder.tvNum.setText(" - "+model.getSal_sum_str());
            }
            holder.tvDesc.setText(model.getSal_desc());
            holder.tvTime.setText(model.getSal_createdtime());
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
