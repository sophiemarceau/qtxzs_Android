package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.models.ContactModel;

import java.util.List;

/**
 * Created by mwl on 2016/2/20.
 * 人脉用户详情数据适配
 */
public class ContactInfoAdapter extends BaseListAdapter<ContactModel> {

    public ContactInfoAdapter(Context context, List<ContactModel> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.contactinfo_item_view, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ContactModel model = getItem(position);
        if (model != null) {
            holder.tvNane.setText(model.getDescribe());
            if (!TextUtils.isEmpty(model.getContribution_sum())) {
                holder.tvNum.setVisibility(View.VISIBLE);
                holder.tvNum.setText("+ " + model.getContribution_sum());
            }else {
                holder.tvNum.setVisibility(View.GONE);
            }
            holder.tvDate.setText(model.getSal_createdtime());
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvNane, tvNum, tvDate;

        ViewHolder(View view) {
            tvNane = (TextView) view.findViewById(R.id.contactinfo_item_txtnane);
            tvNum = (TextView) view.findViewById(R.id.contactinfo_item_txtnum);
            tvDate = (TextView) view.findViewById(R.id.contactinfo_item_txtdate);
        }
    }
}
