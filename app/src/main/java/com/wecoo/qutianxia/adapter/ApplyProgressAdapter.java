package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.AlipayCashActivity;
import com.wecoo.qutianxia.activity.AuthenticationActivity;
import com.wecoo.qutianxia.activity.WithdrawalsActivity;
import com.wecoo.qutianxia.models.ApplyRecordModel;
import com.wecoo.qutianxia.utils.ColorUtil;

import java.util.List;

/**
 * Created by mwl on 2017/01/19
 * 提现进度的数据适配
 */
public class ApplyProgressAdapter extends BaseListAdapter<ApplyRecordModel> {

    public ApplyProgressAdapter(Context context, List<ApplyRecordModel> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.reportprogress_item_view, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.vTop.setVisibility(View.INVISIBLE);
            if (getCount() == 1) {
                holder.vButtom.setVisibility(View.INVISIBLE);
            } else {
                holder.vButtom.setVisibility(View.VISIBLE);
            }
            holder.imgStatus.setImageResource(R.mipmap.icon_timeaxis_red);
        } else if (position == getCount() - 1) {
            holder.vTop.setVisibility(View.VISIBLE);
            holder.vButtom.setVisibility(View.INVISIBLE);
            holder.imgStatus.setImageResource(R.mipmap.icon_timeaxis_gray);
        } else {
            holder.vTop.setVisibility(View.VISIBLE);
            holder.vButtom.setVisibility(View.VISIBLE);
            holder.imgStatus.setImageResource(R.mipmap.icon_timeaxis_gray);
        }
        final ApplyRecordModel entity = getItem(position);
        if (entity != null) {
            // 1，提交了提现申请；2，实名认证通过审核；3，实名认证失败；4，打款失败；5，重新提交；6，已经成功打款
            String opertype_name = entity.getSwal_opertype_name();
            switch (entity.getSwal_opertype()) {
                case 1:
                    String name = /*"提交了提现申请: "*/ opertype_name + ": " + entity.getSwa_sum_str();
                    holder.tvName.setText(ColorUtil.getTextColor(name, opertype_name.length() + 2, name.length()));
                    holder.tvUpdate.setVisibility(View.GONE);
                    holder.tvDesc.setVisibility(View.VISIBLE);
                    if (entity.getSwa_type() == 1) {
                        holder.tvDesc.setText("提现方式: 个人银行卡提现");
                    } else {
                        holder.tvDesc.setText("提现方式: 支付宝提现");
                    }
                    break;
                case 2:
//                    holder.tvName.setText("您的实名认证通过审核");
                    holder.tvName.setText(opertype_name);
                    holder.tvUpdate.setVisibility(View.GONE);
                    holder.tvDesc.setVisibility(View.GONE);
                    break;
                case 3:
//                    holder.tvName.setText("您的实名认证失败，去");
                    if (entity.getLink() == 2) {
                        holder.tvName.setText(opertype_name + "，去");
                        holder.tvUpdate.setText("修改认证信息");
                        holder.tvUpdate.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvName.setText(opertype_name);
                        holder.tvUpdate.setVisibility(View.GONE);
                    }
                    holder.tvDesc.setVisibility(View.VISIBLE);
                    holder.tvDesc.setText("原因: " + entity.getSwal_desc());
                    break;
                case 4:
                    // 1 打款失败，修改信息   2  认证失败，修改认证信息
                    if (entity.getLink() == 1) {
//                        holder.tvName.setText("打款失败，去");
                        holder.tvName.setText( opertype_name + "，去");
                        holder.tvUpdate.setText("修改申请信息");
                        holder.tvUpdate.setVisibility(View.VISIBLE);
                    } else {
//                        holder.tvName.setText("打款失败，已修改信息");
                        holder.tvName.setText(opertype_name);
                        holder.tvUpdate.setVisibility(View.GONE);
                    }
                    holder.tvDesc.setVisibility(View.VISIBLE);
                    holder.tvDesc.setText("原因: " + entity.getSwal_desc());
                    break;
                case 5:
                    holder.tvName.setText(opertype_name);
//                    holder.tvName.setText("重新提交了申请");
                    holder.tvUpdate.setVisibility(View.GONE);
                    holder.tvDesc.setVisibility(View.GONE);
                    break;
                case 6:
                    holder.tvName.setText(opertype_name);
//                    holder.tvName.setText("已经成功打款");
                    holder.tvUpdate.setVisibility(View.GONE);
                    holder.tvDesc.setVisibility(View.VISIBLE);
                    holder.tvDesc.setText("打款账号: " + entity.getUs_realname() + "  " + entity.getAccount());
                    break;
                case 7:
                    holder.tvName.setText(opertype_name);
                    holder.tvUpdate.setVisibility(View.GONE);
                    holder.tvDesc.setVisibility(View.GONE);
                    break;
            }
            holder.tvDate.setText(entity.getSwal_createdtime());
            holder.tvUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MobclickAgent.onEvent(mContext, "applyProgress_updateOnclick");
                    // 判断 提现方式
                    if (entity.getLink() == 1) {
                        if (entity.getSwa_type() == 1) {
                            Intent intent = new Intent(mContext, WithdrawalsActivity.class);
                            intent.putExtra("swa_id", entity.getSwa_id());
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, AlipayCashActivity.class);
                            intent.putExtra("swa_id", entity.getSwa_id());
                            mContext.startActivity(intent);
                        }
                    }else if (entity.getLink() == 2){
                        // 跳转实名认证
                        Intent intent = new Intent(mContext, AuthenticationActivity.class);
                        intent.putExtra("swa_id", entity.getSwa_id());
                        if (entity.getSwa_type() == 2){
                            intent.putExtra("AuthenticationType", "支付宝提现");
                        }
                        mContext.startActivity(intent);
                    }
                }
            });
        }

        return convertView;
    }

    private class ViewHolder {
        View vTop, vButtom;
        ImageView imgStatus;
        TextView tvName, tvUpdate, tvDesc, tvDate;

        ViewHolder(View view) {
            vTop = view.findViewById(R.id.reportprogress_view_top);
            vButtom = view.findViewById(R.id.reportprogress_view_buttom);
            imgStatus = (ImageView) view.findViewById(R.id.reportprogress_img_status);
            tvName = (TextView) view.findViewById(R.id.rProgress_text_name);
            tvUpdate = (TextView) view.findViewById(R.id.rProgress_text_update);
            tvDesc = (TextView) view.findViewById(R.id.reportprogress_text_desc);
            tvDesc.setTextColor(ContextCompat.getColor(mContext, R.color.wecoo_gray5));
            tvDate = (TextView) view.findViewById(R.id.reportprogress_text_time);
        }
    }
}
