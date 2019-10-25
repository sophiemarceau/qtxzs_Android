package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.models.CustomInfoEntity;
import com.wecoo.qutianxia.requestjson.DeleteCustomRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;

import java.util.List;

/**
 * Created by mwl on 2016/10/25.
 * 我的客户数据适配
 */

public class MyCustomAdapter extends BaseListAdapter<CustomInfoEntity> {

    private String title;
    public MyCustomAdapter(Context context,List<CustomInfoEntity> list) {
        super(context);
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ItemViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.custom_recycler_item_view, null);

            holder = new ItemViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ItemViewHolder) view.getTag();
        }
        final CustomInfoEntity entity = getItem(position);
        if (entity != null) {
            holder.bindData(entity);
        }
        return view;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;

        ItemViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.myCustom_item_txtname);
        }

        void bindData(CustomInfoEntity rowModel) {
            txtName.setText(rowModel.getCustomer_name());
        }
    }

    // 删除客户
    public void deleteData(final int position,final RefreshUIByDeleteListener deleteListener) {
        if (getData() != null) {
            final DeleteCustomRequest request = new DeleteCustomRequest();
            String customer_id = getItem(position).getCustomer_id();
            request.setRequestParms(customer_id);
            request.setReturnDataClick(mContext, 0, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    if ((boolean) obj) {
                        if (deleteListener != null) {
                            deleteListener.onRefreshUIByDelete();
                        }
                    }
                }
            });
        }
    }

    public interface RefreshUIByDeleteListener {
        void onRefreshUIByDelete();
    }

}
