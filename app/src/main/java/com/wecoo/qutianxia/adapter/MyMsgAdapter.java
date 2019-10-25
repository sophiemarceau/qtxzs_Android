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

import java.util.List;

/**
 * Created by mwl on 16/4/23.
 * 消息的数据适配
 */
public class MyMsgAdapter extends BaseListAdapter<MyMsgEntity.MsgModels> {

    public MyMsgAdapter(Context context, List<MyMsgEntity.MsgModels> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.msglist_item_view, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MyMsgEntity.MsgModels model = getItem(position);
        if (model != null) {
            holder.tvTime.setText(model.getMsg_createdtime());
            holder.tvTitle.setText(model.getMsg_title());
            holder.tvContent.setText(model.getMsg_content());
            if ("0".equals(model.getMsg_page_to())) {
                holder.tvLook.setVisibility(View.GONE);
            } else {
                holder.tvLook.setVisibility(View.VISIBLE);
            }

            holder.tvLook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MobclickAgent.onEvent(mContext, "Mymsg_lookinfoOnclick");
                    String page_to = model.getMsg_page_to().trim();
                    Intent intent = new Intent();
                    if (page_to.startsWith("1")){
                        String report_id = page_to.substring(2,page_to.length());
                        if (!TextUtils.isEmpty(report_id)){
                            intent.setClass(mContext, ReportInfoActivity.class);
                            intent.putExtra("report_id",report_id);
                            intent.putExtra("butnStatus",2);
                            mContext.startActivity(intent);
                        }
                    }else if ("2".equals(page_to)){
                        intent.setClass(mContext, MyBalanceActivity.class);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvTime, tvTitle, tvContent;
        View tvLook;

        ViewHolder(View view) {
            tvTime = (TextView) view.findViewById(R.id.msglist_item_txtTime);
            tvTitle = (TextView) view.findViewById(R.id.msglist_item_txtTitle);
            tvContent = (TextView) view.findViewById(R.id.msglist_item_txtContent);
            tvLook = view.findViewById(R.id.msglist_item_llLook);
        }
    }
}
