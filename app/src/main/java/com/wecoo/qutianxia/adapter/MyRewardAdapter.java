package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.RewardInfoActivity;
import com.wecoo.qutianxia.models.MyMsgEntity;
import com.wecoo.qutianxia.models.MyRewardEntity;

import java.util.List;

/**
 * Created by mwl on 16/4/23.
 * 奖励活动的数据适配
 */
public class MyRewardAdapter extends BaseListAdapter<MyRewardEntity.RewardModel> {

    public MyRewardAdapter(Context context, List<MyRewardEntity.RewardModel> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.rewardlist_item_view, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MyRewardEntity.RewardModel model = getItem(position);
        if (model != null) {
            holder.tvTitle.setText(model.getActivity_name());
            holder.tvContent.setText("有效期至 " + model.getActivity_enddate());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MobclickAgent.onEvent(mContext, "RewardInfoOnclick");
                    Intent intent = new Intent(mContext, RewardInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("reward", model);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvTitle, tvContent;
        ViewHolder(View view){
            tvTitle = (TextView) view.findViewById(R.id.rewardlist_item_txtTitle);
            tvContent = (TextView) view.findViewById(R.id.rewardlist_item_txtContent);
        }
    }
}
