package com.wecoo.qutianxia.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wecoo.qutianxia.R;

/**
 * Created by mwl on 2016/11/3.
 * 无网络和无数据的view
 */

public class LoadDataErrorWidget extends FrameLayout {

    private LinearLayout dataErrorLayout = null;
    private LinearLayout netErrorLayout = null;
    private ImageView ivNoData = null;
    private TextView tvNoData = null;
    private View viewCustomService;

    private OnReLoadClickListener mReloadListener;
    private OnCallPhoneListener mCallPhoneListener;

    public LoadDataErrorWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.loaddata_error_view, this);

        viewCustomService = findViewById(R.id.BackStageLogin_ll_viewCustomService);
        TextView txtCustomService = (TextView) findViewById(R.id.BackStageLogin_txt_CustomServices);

        dataErrorLayout = (LinearLayout) findViewById(R.id.loaddata_error_nocontent);
        netErrorLayout = (LinearLayout) findViewById(R.id.loaddata_error_nonetwork);
        ivNoData = (ImageView) findViewById(R.id.loaddata_iv_nodata);
        tvNoData = (TextView) findViewById(R.id.loaddata_tv_nodata);

        netErrorLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mReloadListener != null) {
                    mReloadListener.OnReLoadData();
                }
            }
        });
        txtCustomService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallPhoneListener != null){
                    mCallPhoneListener.OnCallPhone();
                }
            }
        });
    }

    /**
     * 数据加载失败
     */
    public void dataLoadError() {
        if (dataErrorLayout != null) {
            dataErrorLayout.setVisibility(View.VISIBLE);
        }

        if (netErrorLayout != null) {
            netErrorLayout.setVisibility(View.GONE);
        }
    }
    /**
     * 数据加载失败有打电话文案
     */
    public void dataLoadErrorCallPhone() {
        if (dataErrorLayout != null) {
            dataErrorLayout.setVisibility(View.VISIBLE);
            viewCustomService.setVisibility(View.VISIBLE);
            tvNoData.setText("当前线上项目为空，您可以拨打客服电话，\n将会有专属客服为您创建项目");
        }

        if (netErrorLayout != null) {
            netErrorLayout.setVisibility(View.GONE);
        }
    }
    /**
     * 只显示文字
     */
    public void loadErrorOnlyTxt(String text) {
        netErrorLayout.setVisibility(View.GONE);
        dataErrorLayout.setVisibility(View.VISIBLE);
        ivNoData.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(text)){
            tvNoData.setText(text);
        }
        tvNoData.setPadding(0,0,0,500);
        tvNoData.setVisibility(View.VISIBLE);
    }

    /**
     * 网络异常提示
     */
    public void netWorkError() {
        if (dataErrorLayout != null) {
            dataErrorLayout.setVisibility(View.GONE);
        }

        if (netErrorLayout != null) {
            netErrorLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setOnReLoadClickListener(OnReLoadClickListener listener) {
        mReloadListener = listener;
    }

    public void setmCallPhoneListener(OnCallPhoneListener mCallPhoneListener) {
        this.mCallPhoneListener = mCallPhoneListener;
    }

    /**
     * 点击刷新数据
     **/
    public interface OnReLoadClickListener {
        void OnReLoadData();
    }

    // 打电话
    public interface OnCallPhoneListener {
        void OnCallPhone();
    }

}
