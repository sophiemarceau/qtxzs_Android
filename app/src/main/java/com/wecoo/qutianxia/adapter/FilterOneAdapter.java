package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.models.FilterEntity;

import java.util.List;

/**
 * Created by mwl on 16/4/23.
 * 项目的排序适配
 */
public class FilterOneAdapter extends BaseListAdapter<FilterEntity> {

    private FilterEntity selectedEntity;

    public FilterOneAdapter(Context context) {
        super(context);
    }

    public FilterOneAdapter(Context context, List<FilterEntity> list) {
        super(context, list);
    }

    public void setSelectedEntity(FilterEntity filterEntity) {
        this.selectedEntity = filterEntity;
        for (FilterEntity entity : getData()) {
            entity.setSelected(entity.getName().equals(selectedEntity.getName()));
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_filter_one, null);
            holder = new ViewHolder();
            holder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FilterEntity entity = getItem(position);
        holder.tvTitle.setText(entity.getName());
        if (entity.isSelected()) {
            holder.tvTitle.setTextColor(ContextCompat.getColor(mContext,R.color.wecoo_theme_color));
        } else {
            holder.tvTitle.setTextColor(ContextCompat.getColor(mContext,R.color.wecoo_gray4));
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
    }
}
