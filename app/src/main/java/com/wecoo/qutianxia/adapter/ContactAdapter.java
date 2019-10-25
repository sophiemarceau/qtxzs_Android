package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.ContactDetailActivity;
import com.wecoo.qutianxia.models.ContactModel;

import java.util.List;

/**
 * Created by mwl on 2016/2/20.
 * 人脉列表
 */
public class ContactAdapter extends BaseListAdapter<ContactModel> {

    public ContactAdapter(Context context, List<ContactModel> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_item_view, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ContactModel model = getItem(position);
        if (model != null) {
            if (model.getLevel() == 1) {
                holder.ivList.setImageResource(R.mipmap.icon_list_one);
            } else if (model.getLevel() == 2) {
                holder.ivList.setImageResource(R.mipmap.icon_list_two);
            } else if (model.getLevel() == 3) {
                holder.ivList.setImageResource(R.mipmap.icon_list_three);
            }
            holder.tvNane.setText(model.getUser_nickname() + "   " + model.getUser_tel());
            holder.tvDate.setText("通过审核的推荐客户数 " + model.getSelf_report_count() + " 条");
            holder.tvNum.setText(model.getSelf_contribution_sum() + "元");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, ContactDetailActivity.class);
                    intent.putExtra("sil_id", model.getSil_id());
                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView ivList;
        TextView tvNane, tvDate, tvNum;

        ViewHolder(View view) {
            ivList = (ImageView) view.findViewById(R.id.contact_item_ivList);
            tvNane = (TextView) view.findViewById(R.id.contact_item_tvName);
            tvDate = (TextView) view.findViewById(R.id.contact_item_tvdate);
            tvNum = (TextView) view.findViewById(R.id.contact_item_tvNum);
        }
    }
}
