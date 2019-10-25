package com.wecoo.qutianxia.view.refreshload;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wecoo.qutianxia.R;

/**
 * Created by wecoo on 2017/5/23.
 * 列表的加载更多
 */

public class PtrFooterView extends LinearLayout {

    private TextView mTextStatus;
    private View pBar;
    // 无网   加载中    加载结束无数据
    public final int NOTNET = 0,LOADING = 1, LOADED = 2;

    public PtrFooterView(Context context) {
        super(context);
        initView();
    }

    public PtrFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PtrFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View footer = LayoutInflater.from(getContext()).inflate(R.layout.pull_to_load_footer_view, this);
        mTextStatus = (TextView) footer.findViewById(R.id.ptr_load_footer_txtTitle);
        pBar = footer.findViewById(R.id.ptr_load_footer_progressbar);
    }

    public void changeStatus(int status) {
        switch (status) {
            case NOTNET:
                pBar.setVisibility(View.GONE);
                mTextStatus.setText(getContext().getString(R.string.download_error_network));
                break;
            case LOADING:
                pBar.setVisibility(View.VISIBLE);
                mTextStatus.setText(getContext().getString(R.string.loading));
                break;
            case LOADED:
                pBar.setVisibility(View.GONE);
                mTextStatus.setText(getContext().getString(R.string.load_no_more));
                break;
        }
    }
}
