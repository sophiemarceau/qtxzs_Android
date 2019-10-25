package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.ProjectInfoActivity;
import com.wecoo.qutianxia.models.ProjectModels;

import java.util.List;

/**
 * Created by mwl on 2016/10/24.
 * 首页的适配
 */

public class HomeAdapter extends BaseListAdapter<ProjectModels> {

    public HomeAdapter(Context context, List<ProjectModels> list) {
        super(context, list);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.home_listitem_view, null);

            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final ProjectModels models = getItem(i);
        if (models != null){
            mImageManager.loadUrlImage(models.getProject_pic_ad(), holder.ivImage);
            holder.tvTitle.setText(models.getProject_name());
            if (models.getProject_commission_first() == models.getProject_commission_second()){
                holder.txtstatus.setText("签约佣金");
            }else {
                holder.txtstatus.setText("最高佣金");
            }
            holder.txtNum.setText(String.valueOf(models.getProject_commission_second()));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MobclickAgent.onEvent(mContext, "HomeAdapterList");
                    Intent intent = new Intent(mContext, ProjectInfoActivity.class);
                    intent.putExtra(ProjectInfoActivity.P_TITLE, models.getProject_slogan());
                    intent.putExtra(ProjectInfoActivity.P_ID, models.getProject_id());
                    mContext.startActivity(intent);
                }
            });
        }
        return view;
    }

    private class ViewHolder {
        ImageView ivImage;
        TextView tvTitle, txtstatus, txtNum;

        public ViewHolder(View convertView) {
            ivImage = (ImageView) convertView.findViewById(R.id.homeList_item_imgIcon);
            tvTitle = (TextView) convertView.findViewById(R.id.homeList_item_txtName);
            txtstatus = (TextView) convertView.findViewById(R.id.homeList_item_txtStatus);
            txtNum = (TextView) convertView.findViewById(R.id.homeList_item_txtNum);
        }
    }
}
