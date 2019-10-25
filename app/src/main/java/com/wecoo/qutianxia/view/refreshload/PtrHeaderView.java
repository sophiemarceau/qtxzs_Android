package com.wecoo.qutianxia.view.refreshload;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.utils.DateUtil;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by wecoo on 2017/5/23.
 * 列表的下拉刷新
 */

public class PtrHeaderView extends LinearLayout implements PtrUIHandler {

    protected RotateAnimation mFlipAnimation;
    protected RotateAnimation mReverseFlipAnimation;
    protected TextView mTitleTextView;// 刷新文本
    private TextView mLastUpdateTextView;// 刷新时间
    private View mRotateView;// 箭头图标
    private View mProgressBar;// 进度条
    private boolean mShouldShowLastUpdate;// 是否显示更新时间

    public PtrHeaderView(Context context) {
        super(context);
        initViews();
    }

    public PtrHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PtrHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    // 初始化View
    protected void initViews() {
        buildAnimation();
        View header = LayoutInflater.from(getContext()).inflate(R.layout.pull_to_refresh_header_view, this);

        mTitleTextView = (TextView) header.findViewById(R.id.ptr_refresh_header_txtTitle);
        mLastUpdateTextView = (TextView) header.findViewById(R.id.ptr_refresh_header_txtDate);
        mRotateView = header.findViewById(R.id.ptr_refresh_header_rotate_img);
        mProgressBar = header.findViewById(R.id.ptr_refresh_header_progressbar);

        resetView();
    }

    // 初始化动画
    protected void buildAnimation() {
        mFlipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(200);
        mFlipAnimation.setFillAfter(true);

        mReverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(200);
        mReverseFlipAnimation.setFillAfter(true);
    }

    // 重置view
    private void resetView() {
        hideRotateView();
        mProgressBar.setVisibility(INVISIBLE);
    }

    private void hideRotateView() {
        mRotateView.clearAnimation();
        mRotateView.setVisibility(INVISIBLE);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        mShouldShowLastUpdate = true;
        resetView();
        tryUpdateLastUpdateTime();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mShouldShowLastUpdate = true;
        tryUpdateLastUpdateTime();

        mProgressBar.setVisibility(INVISIBLE);

        mRotateView.setVisibility(VISIBLE);
        mTitleTextView.setVisibility(VISIBLE);
        mTitleTextView.setText(getContext().getString(R.string.pull_to_refresh));
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mShouldShowLastUpdate = false;
        hideRotateView();
        mProgressBar.setVisibility(VISIBLE);
        mTitleTextView.setVisibility(VISIBLE);
        mTitleTextView.setText(getContext().getString(R.string.refreshing));

        tryUpdateLastUpdateTime();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        hideRotateView();
        mProgressBar.setVisibility(INVISIBLE);

        mTitleTextView.setVisibility(VISIBLE);
        mTitleTextView.setText(getContext().getString(R.string.refresh_succeed));

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromBottomUnderTouch(frame);
                if (mRotateView != null) {
                    mRotateView.clearAnimation();
                    mRotateView.startAnimation(mReverseFlipAnimation);
                }
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromTopUnderTouch(frame);
                if (mRotateView != null) {
                    mRotateView.clearAnimation();
                    mRotateView.startAnimation(mFlipAnimation);
                }
            }
        }
    }

    private void crossRotateLineFromTopUnderTouch(PtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
            mTitleTextView.setVisibility(VISIBLE);
            mTitleTextView.setText(getContext().getString(R.string.release_to_refresh));
        }
    }

    private void crossRotateLineFromBottomUnderTouch(PtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
            mTitleTextView.setVisibility(VISIBLE);
            mTitleTextView.setText(getContext().getString(R.string.pull_to_refresh));
        }
    }

    private void tryUpdateLastUpdateTime() {
        if (!mShouldShowLastUpdate) {
            mLastUpdateTextView.setVisibility(GONE);
        } else {
            String time = DateUtil.getRefreshDate();
            if (TextUtils.isEmpty(time)) {
                mLastUpdateTextView.setVisibility(GONE);
            } else {
                mLastUpdateTextView.setVisibility(VISIBLE);
                mLastUpdateTextView.setText(time);
            }
        }
    }

}
