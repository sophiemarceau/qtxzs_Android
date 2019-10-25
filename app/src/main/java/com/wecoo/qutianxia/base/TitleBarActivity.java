package com.wecoo.qutianxia.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wecoo.qutianxia.R;
import com.wecoo.qutianxia.utils.DensityUtil;

/**
 * Created by wecoo on 2016/10/27.
 * 控制标题栏的Activity
 */

public class TitleBarActivity extends BaseActivity {

    protected static int None = 0, Left = 1, Right = 2;
    public TextView txtLeft, txtCenter, txtRight = null;
    private ImageView imgLeft, imgRight = null;
    private RightCallbackListener callbackListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 输入框不会默认弹出软键盘 **/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    // 添加头部标题栏
    public void initActionBar(Activity context) {
        View mTopView = context.findViewById(R.id.top_View);
        txtLeft = (TextView) mTopView.findViewById(R.id.title_text_left);
        txtCenter = (TextView) mTopView.findViewById(R.id.title_textView_center);
        txtRight = (TextView) mTopView.findViewById(R.id.title_text_right);
        imgLeft = (ImageView) mTopView.findViewById(R.id.title_image_leftBack);
        imgRight = (ImageView) mTopView.findViewById(R.id.title_image_right);

        mTopView.findViewById(R.id.title_fl_right).setOnClickListener(mClickListener);
        mTopView.findViewById(R.id.title_fl_left).setOnClickListener(mClickListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    // 控件监听
    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_fl_left:
                    /**隐藏软键盘**/
                    View keyView = getWindow().peekDecorView();
                    if (keyView != null) {
                        InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    onBackPressed();
                    break;
                case R.id.title_fl_right:
                    if (callbackListener != null) {
                        callbackListener.onRightCallback(view);
                    }
                    break;
            }
        }
    };

    /**
     * @param LeftTitle    左边的textview
     * @param TitleContext 中间的textview
     * @param RightTitle   右边的textview
     */
    public void setBanner(Object LeftTitle, String TitleContext, Object RightTitle) {
        if (txtCenter != null) {
            txtCenter.setText(TitleContext);
        }
        if (LeftTitle instanceof Integer) {
            if ((Integer) LeftTitle != 0) {
                imgLeft.setVisibility(View.VISIBLE);
            } else {
                imgRight.setVisibility(View.INVISIBLE);
            }
        }
        if (LeftTitle instanceof String) {
            txtLeft.setVisibility(View.VISIBLE);
            txtLeft.setText((String) LeftTitle);
        }
        if (RightTitle instanceof Integer) {
            if ((Integer) RightTitle != 0) {
                imgRight.setVisibility(View.VISIBLE);
                if ((Integer) RightTitle != Right) {
                    imgRight.setImageResource((Integer) RightTitle);
                }
            } else {
                imgRight.setVisibility(View.INVISIBLE);
            }
        }
        if (RightTitle instanceof String) {
            txtRight.setVisibility(View.VISIBLE);
            txtRight.setText((String) RightTitle);
            if ("分享".equals(RightTitle)) {
                Drawable drawShare = ContextCompat.getDrawable(this, R.mipmap.icon_invitation_share);
                // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示  
                drawShare.setBounds(0, 0, DensityUtil.dp2px(this, 18), DensityUtil.dp2px(this, 18));
                txtRight.setCompoundDrawables(null, null, drawShare, null);
                txtRight.setCompoundDrawablePadding(10);
            }
        }
    }

    public interface RightCallbackListener {
        void onRightCallback(View view);
    }

    // 标题右边的监听
    protected void setRightCallbackListener(RightCallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

}
