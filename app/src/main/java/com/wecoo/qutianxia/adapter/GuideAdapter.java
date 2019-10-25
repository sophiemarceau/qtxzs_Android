package com.wecoo.qutianxia.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by mwl on 2016/10/20.
 * 引导页的适配
 */
public class GuideAdapter extends PagerAdapter {
    private List<View> mViewLists = null;

    public GuideAdapter(List<View> viewLists) {
        // TODO Auto-generated constructor stub
        mViewLists = viewLists;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewLists.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViewLists.get(position);
        container.addView(view, 0);//
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return (mViewLists == null ? 0 : mViewLists.size());
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }
}
