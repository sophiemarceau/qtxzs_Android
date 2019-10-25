package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.enterprise.ProjectAuditActivity;
import com.wecoo.qutianxia.constants.Configs;
import com.wecoo.qutianxia.models.ProjectModels;
import com.wecoo.qutianxia.utils.ColorUtil;
import com.wecoo.qutianxia.utils.SPUtils;

import java.util.List;

/**
 * Created by mwl on 2017/06/05.
 * 我的项目的适配
 */

public class MyProjectAdapter extends BaseListAdapter<ProjectModels> {

    public MyProjectAdapter(Context context, List<ProjectModels> dataList) {
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
            mImageManager.loadUrlImage(models.getProject_pic_square(), holder.imgIcon);
            holder.txtIdentity.setText("身份：" + models.getProjectManagerKindName());
            holder.txtTitle.setText(models.getProject_name());
            holder.txtDesc.setText(models.getCompany_name());
            String SignCommission = "推荐" + models.getReportNum() + "条";
            holder.txtNumbers.setText(ColorUtil.getTextColor(SignCommission, 2, (SignCommission.length() - 1)));
            String singularization = "未审核" + models.getReportWaitingAuditingNum() + "条";
            holder.txtCounts.setText(ColorUtil.getTextColor(singularization, 3, (singularization.length() - 1)));
            holder.txtCounts.setTextSize(15);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SPUtils.put(mContext, Configs.project_commission,models.getProject_commission_first());
                    Intent intent = new Intent(mContext, ProjectAuditActivity.class);
                    intent.putExtra("project_id",models.getProject_id());
                    mContext.startActivity(intent);
                }
            });
        }
        return view;
    }

    private class ViewHolder {
        ImageView imgIcon;
        TextView txtTitle, txtDesc, txtNumbers, txtCounts,txtIdentity;

        public ViewHolder(View view) {
            imgIcon = (ImageView) view.findViewById(R.id.project_item_imageIcon);
            txtTitle = (TextView) view.findViewById(R.id.project_item_txtTitle);
            txtDesc = (TextView) view.findViewById(R.id.project_item_txtdesc);
            txtNumbers = (TextView) view.findViewById(R.id.project_item_txtNumbers);
            txtCounts = (TextView) view.findViewById(R.id.project_item_txtCounts);
            txtIdentity = (TextView) view.findViewById(R.id.project_item_txtIdentity);
        }
    }
}
