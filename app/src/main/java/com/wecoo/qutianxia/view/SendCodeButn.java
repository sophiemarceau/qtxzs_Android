package com.wecoo.qutianxia.view;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.wecoo.qutianxia.R;

/**
 * Created by mwl on 2016/10/25.
 * 发送验证码按钮
 */

public class SendCodeButn extends CountDownTimer {
    public static final int TIME_COUNT = 60000;//时间防止从59s开始显示（以倒计时600s为例子）
    private TextView btnSendCode;
    private int endStrRid;
    private int normalColor, timingColor;//未计时的文字颜色，计时期间的文字颜色

    /**
     * 参数 millisInFuture         倒计时总时间（如60S，120s等）
     * 参数 countDownInterval    渐变时间（每次倒计1s）
     * 参数 btn               点击的按钮(因为Button是TextView子类，为了通用我的参数设置为TextView）
     * 参数 endStrRid   倒计时结束后，按钮对应显示的文字
     */
    public SendCodeButn (long millisInFuture, long countDownInterval, TextView btnSendCode, int endStrRid) {
        super(millisInFuture, countDownInterval);
        this.btnSendCode = btnSendCode;
        this.endStrRid = endStrRid;
    }

    /**
     *参数上面有注释
     */
    public SendCodeButn (TextView btnSendCode, int endStrRid) {
        super(TIME_COUNT, 1000);
        this.btnSendCode = btnSendCode;
        this.endStrRid = endStrRid;
    }

    public SendCodeButn (TextView btnSendCode) {
        super(TIME_COUNT, 1000);
        this.btnSendCode = btnSendCode;
        this.endStrRid = R.string.sendcode;
    }

    // 计时完毕时触发
    @Override
    public void onFinish() {
        if(normalColor > 0){
            btnSendCode.setTextColor(normalColor);
        }
        btnSendCode.setText(endStrRid);
        btnSendCode.setEnabled(true);
        btnSendCode.setBackgroundResource(R.mipmap.btn_sendcode_red);
    }

    // 计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
        if(timingColor > 0){
            btnSendCode.setTextColor(Color.GRAY);
        }
        btnSendCode.setEnabled(false);
        btnSendCode.setTextColor(Color.GRAY);
        btnSendCode.setBackgroundResource(R.mipmap.btn_sendcode_gray);
        btnSendCode.setText(millisUntilFinished / 1000 + "秒可重发");
    }

}
