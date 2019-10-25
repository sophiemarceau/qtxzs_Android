package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.activity.PubWebViewActivity;
import com.wecoo.qutianxia.models.StringModels;

import java.util.List;

/**
 * Created by mwl on 16/4/23.
 * 项目详情中锦囊的数据适配
 */
public class SilkListAdapter extends BaseListAdapter<StringModels> {

    public SilkListAdapter(Context context, List<StringModels> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.info_silk_textview, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final StringModels model = getItem(position);
        if (model != null) {
            holder.tvTitle.setText(model.getJinNangTit());
            holder.tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MobclickAgent.onEvent(mContext, "SilkListOnclick");
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getJinNangUrl()));
                    Intent intent = new Intent(mContext, PubWebViewActivity.class);
                    intent.putExtra(PubWebViewActivity.JNShare,PubWebViewActivity.JNShare);
                    intent.putExtra(PubWebViewActivity.WebUrl,model.getJinNangUrl());
                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvTitle;
        ViewHolder(View view){
            tvTitle = (TextView) view.findViewById(R.id.project_silkbag_txtchild);
        }
    }
}
