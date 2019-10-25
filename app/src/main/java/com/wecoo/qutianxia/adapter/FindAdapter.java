package com.wecoo.qutianxia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.manager.ImageManager;
import com.wecoo.qutianxia.models.FindEntity;

import java.util.List;

/**
 * Created by mwl on 2016/10/24.
 * 发现的适配
 */

public class FindAdapter extends BaseAdapter {

//    private Context context;
    private List<FindEntity> findEntities;
    private LayoutInflater inflater;
    private ImageManager imageManager;

    public FindAdapter(Context context, List<FindEntity> findEntities){
//        this.context = context;
        this.findEntities = findEntities;
        inflater = LayoutInflater.from(context);
        imageManager = new ImageManager(context);
    }

    @Override
    public int getCount() {
        return findEntities == null ? 0 : findEntities.size();
    }

    @Override
    public Object getItem(int i) {
        return findEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = inflater.inflate(R.layout.find_listview_item,null);
            holder = new ViewHolder();
            holder.imgIcon = (ImageView)view.findViewById(R.id.find_item_imageIcon);
            holder.txtTitle = (TextView)view.findViewById(R.id.find_item_txtTitle);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        FindEntity entity = findEntities.get(i);
        if (entity != null){
            imageManager.loadUrlImage(entity.getImgUrl(), holder.imgIcon);
            holder.txtTitle.setText(entity.getContent());
//            if (1 == entity.getStatus()){
//
//            }else {
//
//            }
        }
        return view;
    }

    private class ViewHolder{
        ImageView imgIcon;
        TextView txtTitle;
    }
}
