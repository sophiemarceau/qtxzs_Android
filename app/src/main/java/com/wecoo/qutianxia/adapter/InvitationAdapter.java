package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.InvitationDetailActivity;
import com.wecoo.qutianxia.models.InvitationModel;
import com.wecoo.qutianxia.utils.LogUtil;

import java.util.List;

/**
 * Created by mwl
 * 我的邀请数据适配
 */
public class InvitationAdapter extends BaseListAdapter<InvitationModel> {

    private int viewType = 1;

    public InvitationAdapter(Context context, List<InvitationModel> list, int viewType) {
        super(context, list);
        this.viewType = viewType;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.invitation_item_view, null);

            holder = new ViewHolder(convertView, viewType);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final InvitationModel model = getData().get(position);
        if (model != null) {
            holder.tvName.setText(model.getUs_nickname() + "  " + model.getUs_tel());
            // 处理不同颜色的字体
            String desc = model.getBeInviterSalesman_date() + "  " + model.getBeInviterSalesman_describe();
            int start = desc.indexOf("@");
            int end = desc.lastIndexOf("@");
            if (start > 0) {
                SpannableString spanStr = new SpannableString(desc.replace("@", ""));
                spanStr.setSpan(new ForegroundColorSpan(Color.RED), start, (end - 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvDescv.setText(spanStr);
            } else {
                holder.tvDescv.setText(desc);
            }
            holder.tvStatus.setText(model.getBeInviterSalesman_status());
            /*     以上是邀请列表数展示          */
            holder.tvDescribe.setText(model.getBeInviterSalesman_describe());
            if (!TextUtils.isEmpty(model.getBeInviterSalesman_balance())) {
                holder.tvPrice.setText("+" + model.getBeInviterSalesman_balance());
            }
            holder.tvDate.setText(model.getBeInviterSalesman_date());
            if (viewType == 1) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, InvitationDetailActivity.class);
                        intent.putExtra(InvitationDetailActivity.SLL_ID, model.getSil_id());
                        mContext.startActivity(intent);
                    }
                });
            }
        }
        return convertView;
    }

    private class ViewHolder {
        // 邀请列表
        LinearLayout viewTop;
        TextView tvName, tvDescv, tvStatus;
        // 邀请用户详情
        LinearLayout viewButtom;
        TextView tvDescribe, tvPrice, tvDate;

        public ViewHolder(View convertView, int viewType) {
            viewTop = (LinearLayout) convertView.findViewById(R.id.invitation_item_llViewTop);
            tvName = (TextView) convertView.findViewById(R.id.invitation_item_txtName);
            tvDescv = (TextView) convertView.findViewById(R.id.invitation_item_txtDesc);
            tvStatus = (TextView) convertView.findViewById(R.id.invitation_item_txtStatus);
            viewButtom = (LinearLayout) convertView.findViewById(R.id.invitation_item_llViewButtom);
            tvDescribe = (TextView) convertView.findViewById(R.id.invitation_item_txtDescribe);
            tvPrice = (TextView) convertView.findViewById(R.id.invitation_item_txtPrice);
            tvDate = (TextView) convertView.findViewById(R.id.invitation_item_txtDate);
            if (viewType == 1) {
                viewTop.setVisibility(View.VISIBLE);
                viewButtom.setVisibility(View.GONE);
            } else {
                viewTop.setVisibility(View.GONE);
                viewButtom.setVisibility(View.VISIBLE);
            }
        }
    }
}
