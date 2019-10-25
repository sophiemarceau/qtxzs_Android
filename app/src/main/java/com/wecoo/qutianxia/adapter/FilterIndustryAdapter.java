package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.models.FilterEntity;

import java.util.List;

/**
 * Created by mwl on 17/5/10.
 * 项目的全部行业适配
 */
public class FilterIndustryAdapter extends BaseListAdapter<FilterEntity> {

    private FilterEntity selectedEntity;

    public FilterIndustryAdapter(Context context, List<FilterEntity> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.industry_list_item_view, null);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FilterEntity entity = getItem(position);
        holder.bindHolderData(entity);

        return convertView;
    }

    private class ViewHolder {
        ImageView ivIcon, ivHot;
        TextView tvTitle;

        ViewHolder(View view) {
            ivIcon = (ImageView) view.findViewById(R.id.industry_listItem_view_iv);
            tvTitle = (TextView) view.findViewById(R.id.industry_listItem_view_tv);
            ivHot = (ImageView) view.findViewById(R.id.industry_listItem_view_ivHot);
        }

        void bindHolderData(FilterEntity entity) {
            if (entity != null) {
                tvTitle.setText(entity.getName());
                if (TextUtils.isEmpty(entity.getIcon_android())) {
                    ivIcon.setImageResource(entity.getIconUrl());
                } else {
                    mImageManager.loadUrlImage(entity.getIcon_android(), ivIcon);
                }
                if ("1".equals(entity.getIs_hot())) {
                    ivHot.setVisibility(View.VISIBLE);
                } else {
                    ivHot.setVisibility(View.GONE);
                }
                if (entity.isSelected()) {
                    tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_theme_color));
                } else {
                    tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_gray6));
                }
            }
        }
    }
}
