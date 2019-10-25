package com.wecoo.qutianxia.manager;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by mwl on 2017/1/12.
 * 更改Glide缓存位置
 */

public class GlideCache implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
        //设置图片的显示格式
        glideBuilder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
        //设置磁盘缓存目录（和创建的缓存目录相同）
        String downloadDirectoryPath = AppFolderManager.getInstance().getGlideCacheFolder();
        //设置缓存的大小为20M
        int cacheSize = 1024 * 1024 * 20;
        glideBuilder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, cacheSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
