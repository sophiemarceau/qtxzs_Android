package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.models.ScreenModel;
import com.wecoo.qutianxia.utils.LogUtil;

import java.util.List;

/**
 * Created by mwl on 17/2/20.
 * 人脉列表筛选数据
 */
public class ScreenGridAdapter extends BaseListAdapter<ScreenModel> {

    private OnScreenGridItemListener itemListener;

    ScreenGridAdapter(Context context, List<ScreenModel> list) {
        super(context, list);
    }

    void setItemListener(OnScreenGridItemListener itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.creentype_grid_item, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ScreenModel entity = getItem(position);
        holder.bindData(entity, position);

        return convertView;
    }

    private class ViewHolder {
        TextView tvName;

        ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.creenType_griditem_txt);
        }

        void bindData(ScreenModel entity, final int position) {
            if (entity != null) {
                tvName.setText(entity.getName());
                if (entity.getType() == 1) {
                    tvName.setBackgroundResource(R.drawable.btn_commit_info_bg);
                    tvName.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                } else {
                    tvName.setBackgroundResource(R.drawable.screen_item_gray_bg);
                    tvName.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_gray5));
                }
                tvName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectItem(position);
                        if (itemListener != null) {
                            itemListener.onSelectItem(position);
                        }
                    }
                });
            }
        }
    }

    // 点击某项，其他项设置为不选择
    private void selectItem(int position) {
        for (int i = 0; i < getCount(); i++) {
            if (getItem(i).getType() == 1) {
                getItem(i).setType(0);
            }
        }
        if (position < getCount()) {
            getItem(position).setType(1);
            notifyDataSetChanged();
        }
    }

    // 点击之后的接口
    interface OnScreenGridItemListener {
        void onSelectItem(int position);
    }
}
