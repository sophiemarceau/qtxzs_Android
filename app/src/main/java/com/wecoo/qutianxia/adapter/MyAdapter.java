package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.models.MyDataEntity;

import java.util.List;

/**
 * Created by mwl on 16/4/23.
 * 我的数据适配
 */
public class MyAdapter extends BaseListAdapter<MyDataEntity> {

    public MyAdapter(Context context, List<MyDataEntity> list) {
        super(context, list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_listview_item, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MyDataEntity entity = getItem(position);
        if (entity != null) {
            if (R.string.my_settled == entity.getTitleContent() ||
                    R.string.my_Project == entity.getTitleContent()) {
                holder.vHead.setVisibility(View.VISIBLE);
                holder.vFoot.setVisibility(View.GONE);
            } else if (R.string.my_bunos == entity.getTitleContent()) {
                holder.vFoot.setVisibility(View.VISIBLE);
                holder.vHead.setVisibility(View.VISIBLE);
            } else {
                holder.vFoot.setVisibility(View.GONE);
                holder.vHead.setVisibility(View.GONE);
            }
            holder.txtNum.setText(entity.getDataNum());
            holder.tvTitle.setText(mContext.getString(entity.getTitleContent()));
            holder.ivImage.setImageResource(entity.getImgRouse());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, entity.getCalssName());
                    mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    private class ViewHolder {
        View vHead, vFoot;
        ImageView ivImage;
        TextView tvTitle, txtNum;

        public ViewHolder(View convertView) {
            vHead = convertView.findViewById(R.id.my_item_headview);
            vFoot = convertView.findViewById(R.id.my_item_footview);
            ivImage = (ImageView) convertView.findViewById(R.id.my_item_image);
            tvTitle = (TextView) convertView.findViewById(R.id.my_item_text);
            txtNum = (TextView) convertView.findViewById(R.id.my_item_textNum);
        }
    }
}
