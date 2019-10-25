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
import com.wecoo.qutianxia.utils.ColorUtil;

import java.util.List;

/**
 * Created by mwl on 2017/05/17.
 * 项目搜索的适配
 */

public class SearchAdapter extends BaseListAdapter<ProjectModels> {

    public SearchAdapter(Context context, List<ProjectModels> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.project_listview_item, null);

            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final ProjectModels models = getItem(i);
        if (models != null) {
            if (models.getIs_hot() == 1){
                holder.txtNote.setVisibility(View.VISIBLE);
                holder.txtNote.setText("热门");
            }else if (models.getIs_newest() == 1){
                holder.txtNote.setVisibility(View.VISIBLE);
                holder.txtNote.setText("最新");
            }else {
                holder.txtNote.setVisibility(View.GONE);
            }
            mImageManager.loadUrlImage(models.getProject_pic_square(), holder.imgIcon);
            holder.txtTitle.setText(models.getProject_name());
            holder.txtDesc.setText(models.getProject_slogan());
            String SignCommission = "签约佣金" + models.getProject_commission_first() + "元";
            holder.txtNumbers.setText(ColorUtil.getTextColor(SignCommission, 4, (SignCommission.length() - 1)));
            String singularization = mContext.getString(R.string.singularization) + models.getProject_signed_count() + "单";
            holder.txtCounts.setText(ColorUtil.getTextColor(singularization, 3, (singularization.length() - 1)));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MobclickAgent.onEvent(mContext, "ProjectAdapterList");
                    Intent intent = new Intent(mContext, ProjectInfoActivity.class);
                    intent.putExtra(ProjectInfoActivity.P_TITLE, models.getProject_name());
                    intent.putExtra(ProjectInfoActivity.P_ID, models.getProject_id());
                    mContext.startActivity(intent);
                }
            });
        }
        return view;
    }

    private class ViewHolder {
        ImageView imgIcon;
        TextView txtTitle, txtDesc, txtNumbers, txtCounts,txtNote;

        public ViewHolder(View view) {
            imgIcon = (ImageView) view.findViewById(R.id.project_item_imageIcon);
            txtTitle = (TextView) view.findViewById(R.id.project_item_txtTitle);
            txtDesc = (TextView) view.findViewById(R.id.project_item_txtdesc);
            txtNumbers = (TextView) view.findViewById(R.id.project_item_txtNumbers);
            txtCounts = (TextView) view.findViewById(R.id.project_item_txtCounts);
            txtNote = (TextView) view.findViewById(R.id.project_item_txtNote);
        }
    }
}
