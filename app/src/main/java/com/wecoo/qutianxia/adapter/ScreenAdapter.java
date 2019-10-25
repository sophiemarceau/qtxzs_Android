package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.adapter.ScreenGridAdapter.OnScreenGridItemListener;
import com.wecoo.qutianxia.models.ScreenEntity;
import com.wecoo.qutianxia.widget.MyGridView;

import java.util.List;

/**
 * Created by mwl on 17/2/20.
 * 人脉列表筛选数据
 */
public class ScreenAdapter extends BaseListAdapter<ScreenEntity> {

    private OnScreenItemListener itemListener;

    public ScreenAdapter(Context context, List<ScreenEntity> list) {
        super(context, list);
    }

    public void setItemListener(OnScreenItemListener itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.creentype_list_item, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ScreenEntity entity = getItem(position);
        holder.bindData(entity, position);

        return convertView;
    }


    private class ViewHolder {
        TextView tvTitle;
        MyGridView gridView;
        ScreenGridAdapter gridAdapter;

        ViewHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.creenType_item_txtTitle);
            gridView = (MyGridView) view.findViewById(R.id.creenType_item_gridView);
        }

        void bindData(ScreenEntity entity, final int parent) {
            if (entity != null) {
                tvTitle.setText(entity.getTitle());
                gridAdapter = new ScreenGridAdapter(mContext, entity.getList());
                gridView.setAdapter(gridAdapter);
                gridAdapter.setItemListener(new OnScreenGridItemListener() {
                    @Override
                    public void onSelectItem(int position) {
                        if (itemListener != null) {
                            itemListener.onSelectItem(parent, position);
                        }
                    }
                });
            }
        }
    }

    public interface OnScreenItemListener {
        void onSelectItem(int parent, int child);
    }
}
