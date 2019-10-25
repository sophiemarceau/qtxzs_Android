package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.MyBalanceActivity;
import com.wecoo.qutianxia.activity.ReportInfoActivity;
import com.wecoo.qutianxia.models.MyMsgEntity;
import com.wecoo.qutianxia.models.ProjectSignedEntity;

import java.util.List;

/**
 * Created by mwl
 * 已签约客户的数据适配
 */
public class SigenedCustomerAdapter extends BaseListAdapter<ProjectSignedEntity> {

    public SigenedCustomerAdapter(Context context, List<ProjectSignedEntity> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sigenedcostomer_listitem_view, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ProjectSignedEntity entity = getItem(position);
        if (entity != null) {
            holder.bindData(entity);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvName, tvAdress;

        ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.signed_item_txtname);
            tvAdress = (TextView) view.findViewById(R.id.signed_item_txtAdress);
        }
        void bindData(ProjectSignedEntity entity) {
            tvName.setText(entity.getReport_customer_name() + "  " + entity.getReport_customer_tel());
            tvAdress.setText("代理地区：" + entity.getReport_customer_area_agent_name());
        }
    }
}
