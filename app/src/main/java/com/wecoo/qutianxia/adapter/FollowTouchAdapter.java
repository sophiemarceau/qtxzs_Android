package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.ProjectInfoActivity;
import com.wecoo.qutianxia.constants.Constants;
import com.wecoo.qutianxia.models.FollowEntity;
import com.wecoo.qutianxia.requestjson.CancelFollowRequest;
import com.wecoo.qutianxia.requestjson.ReturnDataClick;
import com.wecoo.qutianxia.requestjson.WebUrl;

import java.util.List;

/**
 * Created by mwl on 2016/10/25.
 * 关注滑动的操作
 */

public class FollowTouchAdapter extends BaseListAdapter<FollowEntity.FollowModels> {

    public FollowTouchAdapter(Context context, List<FollowEntity.FollowModels> list) {
        super(context, list);
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ItemViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.followitem_recycler_row_view, null);

            holder = new ItemViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ItemViewHolder) view.getTag();
        }
        final FollowEntity.FollowModels models = getItem(position);
        if (models != null) {
            holder.bindData(models);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProjectInfoActivity.class);
                intent.putExtra(ProjectInfoActivity.P_TITLE, getItem(position).getProject_name());
                intent.putExtra(ProjectInfoActivity.P_ID, getItem(position).getProject_id());
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtContent;
        TextView txtC1, txtC2, txtC3, txtC4, txtC5;
        ImageView imgStatus;

        ItemViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.follow_item_title);
            txtContent = (TextView) itemView.findViewById(R.id.follow_item_content);
            txtC1 = (TextView) itemView.findViewById(R.id.follow_txt_count1);
            txtC2 = (TextView) itemView.findViewById(R.id.follow_txt_count2);
            txtC3 = (TextView) itemView.findViewById(R.id.follow_txt_count3);
            txtC4 = (TextView) itemView.findViewById(R.id.follow_txt_count4);
            txtC5 = (TextView) itemView.findViewById(R.id.follow_txt_count5);
            imgStatus = (ImageView) itemView.findViewById(R.id.follow_img_projectStatus);
        }

        void bindData(FollowEntity.FollowModels rowModel) {
            txtTitle.setText(rowModel.getProject_name());
            txtContent.setText(rowModel.getProject_slogan());
            txtC1.setText("最高佣金");
            txtC2.setText(rowModel.getProject_commission_second());
            txtC3.setText(mContext.getString(R.string.yuan) + "   "
                    + mContext.getString(R.string.singularization));
            txtC4.setText(rowModel.getProjectSignedCount());
            txtC5.setText("单");
            if (rowModel.getProject_status() == Constants.normal) {
                imgStatus.setVisibility(View.GONE);
            } else {
                imgStatus.setVisibility(View.VISIBLE);
            }
        }
    }

    // 取消关注
    public void deleteData(final int position, final RefreshUIByCancelListener deleteListener) {
        if (getData() != null) {
            String pcr_id = getItem(position).getPcr_id();
            CancelFollowRequest request = new CancelFollowRequest(WebUrl.cancelProjectCollectionRecord);
            request.setRequestParms(pcr_id);
            request.setReturnDataClick(mContext, 3, new ReturnDataClick() {
                @Override
                public void onReturnData(int what, Object obj) {
                    boolean objFlag = (boolean) obj;
                    if (objFlag) {
                        if (deleteListener != null) {
                            deleteListener.onRefreshUIByDelete();
                        }
                    }
                }
            });
        }
    }

    public interface RefreshUIByCancelListener {
        void onRefreshUIByDelete();
    }
}
