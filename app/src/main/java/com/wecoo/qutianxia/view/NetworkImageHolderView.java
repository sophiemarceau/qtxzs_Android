package com.wecoo.qutianxia.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.models.BannerEntity;

/**
 * Created by mwl on 2016/12/27.
 * ConvenientBanner 轮播的View
 */

class NetworkImageHolderView implements Holder<BannerEntity> {

    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, BannerEntity entity) {
        Glide.with(context)
                .load(entity.getAd_pic())
                .placeholder(R.drawable.default_picture_bg)
                .error(R.drawable.default_picture_bg)
                .priority(Priority.HIGH)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }
}
