package com.wecoo.qutianxia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwl on 2016/11/17.
 * 报备质量分中刻度
 */

public class ReportCircleView extends View {

    private RingViewAnim mRingViewAnim;
    private int animDuration = 2500;// 动画时间

    private Paint ringPaint;    // 点 滑动的弧线
    private Paint ringProgerssPaint;  // 中间的阴影
    private Paint scalePaint;    // 长刻度
    private Paint scalePaint_s;  // 短刻度

    private Paint textPaint;
    private Paint progerssPaint;
    private Paint pointPaint;

    private float startAngle = -200f;
    private float sweepAngle = 220f;

    private float pointAngle_show = -200f;

    private int sWidthringPaint = 0;    // 点 滑动的弧线宽度
    private int sWidthprogerssPaint = 0; // 中间的宽度
    private int progerssPaintSize = 0; // 中间显示文字的大小
    private int pointShadowLayer = 0;
    private int pointPaintSize = 0;    // 点的大小

    private List<String> valueNameList = new ArrayList<>();

    private RectF fring;
    private RectF fprogerss;
    private RectF fprogerssPath;
    private Path path;
    private int mValue = 0,maxLength = 150;
    //    private String mShowValue = "";
    Path pathPoint = new Path();

    public ReportCircleView(Context context) {
        this(context, null);
    }

    public ReportCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReportCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRingViewAnim = new RingViewAnim();
        mRingViewAnim.setDuration(animDuration);

        progerssPaintSize = dip2px(getContext(), 50f);
        int textPaintSize = dip2px(getContext(), 10f); // 刻度文字的大小
        sWidthringPaint = dip2px(getContext(), 1.0f);
        sWidthprogerssPaint = dip2px(getContext(), 10f);
        pointPaintSize = dip2px(getContext(), 2.0f);
        pointShadowLayer = dip2px(getContext(), 2.5f);

        ringPaint = new Paint();
        ringPaint.setAntiAlias(true);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(sWidthringPaint);
        ringPaint.setColor(Color.argb(200, 255, 255, 255));


        ringProgerssPaint = new Paint();
        ringProgerssPaint.setAntiAlias(true);
        ringProgerssPaint.setStyle(Paint.Style.STROKE);
        ringProgerssPaint.setStrokeWidth(sWidthprogerssPaint);
        ringProgerssPaint.setColor(Color.argb(90, 255, 255, 255));


        scalePaint = new Paint();
        scalePaint.setAntiAlias(true);
        scalePaint.setStyle(Paint.Style.STROKE);
        scalePaint.setStrokeWidth(sWidthprogerssPaint);
        scalePaint.setColor(Color.argb(70, 255, 255, 255));


        scalePaint_s = new Paint();
        scalePaint_s.setAntiAlias(true);
        scalePaint_s.setStyle(Paint.Style.STROKE);
        scalePaint_s.setStrokeWidth(sWidthprogerssPaint / 2);
        scalePaint_s.setColor(Color.argb(80, 255, 255, 255));


        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(textPaintSize);

        progerssPaint = new Paint();
        progerssPaint.setAntiAlias(true);
        progerssPaint.setStyle(Paint.Style.FILL);
        progerssPaint.setColor(Color.WHITE);
        progerssPaint.setTextSize(progerssPaintSize);


        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (fring == null)
            fring = new RectF(sWidthringPaint + pointShadowLayer, sWidthringPaint + pointShadowLayer,
                    getMeasuredWidth() - sWidthringPaint - pointShadowLayer, getMeasuredWidth() - sWidthringPaint - pointShadowLayer);
        if (fprogerss == null)
            fprogerss = new RectF(sWidthprogerssPaint * 2, sWidthprogerssPaint * 2, getMeasuredWidth() - sWidthprogerssPaint * 2, getMeasuredWidth() - sWidthprogerssPaint * 2);

        if (fprogerssPath == null)
            fprogerssPath = new RectF(sWidthprogerssPaint * 2 + sWidthprogerssPaint,
                    sWidthprogerssPaint * 2 + sWidthprogerssPaint,
                    getMeasuredWidth() - sWidthprogerssPaint * 2 - sWidthprogerssPaint,
                    getMeasuredWidth() - sWidthprogerssPaint * 2 - sWidthprogerssPaint);
        if (path == null) {
            path = new Path();
            path.addOval(fprogerssPath, Path.Direction.CW);
        }
        canvas.drawArc(fring, startAngle - 10, sweepAngle + 20, false, ringPaint);
        canvas.drawArc(fprogerss, startAngle - 20, sweepAngle + 40, false, ringProgerssPaint);


        for (int i = 0; i < valueNameList.size(); i++) {
            float unit = sweepAngle / (valueNameList.size() - 1);
            float startAngleBig = startAngle + unit * i;
            canvas.drawArc(fprogerss, startAngleBig, 0.5f, false, scalePaint);
            String value = valueNameList.get(i) + "";
            canvas.drawTextOnPath(value, path,
                    (float) (Math.PI * fprogerssPath.width() * ((startAngleBig + 360) % 360) / 360) - getFontlength(textPaint, value) / 2,
                    getFontHeight(textPaint), textPaint);

            if (i < valueNameList.size() - 1) {
                float unitSmall = unit / 6;
                for (int j = 1; j < 6; j++) {
                    canvas.drawArc(fprogerss, startAngleBig + unitSmall * j, 0.2f, false, scalePaint_s);
                }
            }


        }

        if (!isPointer) {
            float xPoint = (float) (getMeasuredWidth() / 2 + (fring.width() / 2) * Math.cos(pointAngle_show * Math.PI / 180));
            float yPoint = (float) (getMeasuredWidth() / 2 + (fring.width() / 2) * Math.sin(pointAngle_show * Math.PI / 180));
//            pointPaint.setShadowLayer(pointShadowLayer, 0, 0, Color.WHITE);//设置阴影
            pointPaint.setColor(Color.argb(80, 255, 255, 255));
            canvas.drawCircle(xPoint, yPoint, pointPaintSize + 8, pointPaint);
            pointPaint.setColor(Color.WHITE);
            canvas.drawCircle(xPoint, yPoint, pointPaintSize, pointPaint);
            progerssPaint.setTextSize(progerssPaintSize);
//            canvas.drawText(mShowValue, getMeasuredWidth() / 2 - getFontlength(progerssPaint, mShowValue) / 2, getMeasuredWidth() / 2, progerssPaint);
        } else {
            float xPoint = (float) (getMeasuredWidth() / 2 + (fring.width() / 3) * Math.cos(pointAngle_show * Math.PI / 180));
            float yPoint = (float) (getMeasuredWidth() / 2 + (fring.width() / 3) * Math.sin(pointAngle_show * Math.PI / 180));

            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, dip2px(getContext(), 5f), pointPaint);
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, dip2px(getContext(), 8f), pointPaint);


            float xPoint2 = (float) (dip2px(getContext(), 5f) * Math.cos((pointAngle_show + 90) * Math.PI / 180));
            float yPoint2 = (float) (dip2px(getContext(), 5f) * Math.sin((pointAngle_show + 90) * Math.PI / 180));


            float xPoint3 = (float) (dip2px(getContext(), 5f) * Math.cos((pointAngle_show - 90) * Math.PI / 180));
            float yPoint3 = (float) (dip2px(getContext(), 5f) * Math.sin((pointAngle_show - 90) * Math.PI / 180));


            pathPoint.reset();
            pathPoint.moveTo(xPoint, yPoint);

            pathPoint.lineTo(getMeasuredWidth() / 2 - xPoint2, getMeasuredWidth() / 2 - yPoint2);

            pathPoint.lineTo(getMeasuredWidth() / 2 - xPoint3, getMeasuredWidth() / 2 - yPoint3);
            pathPoint.close();
            canvas.drawPath(pathPoint, pointPaint);
            progerssPaint.setTextSize(progerssPaintSize * 2 / 3);

//            canvas.drawText(mShowValue, getMeasuredWidth() / 2 - getFontlength(progerssPaint, mShowValue) / 2,
//                    getMeasuredWidth() / 2 + getFontHeight(progerssPaint) * 1.5f, progerssPaint);

            pointPaint.setColor(Color.argb(80, 255, 255, 255));
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, dip2px(getContext(), 12f), pointPaint);
            pointPaint.setColor(Color.WHITE);
        }
        canvas.restore();

    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public float getFontlength(Paint paint, String str) {
        return paint.measureText(str);
    }

    public float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    // 质量分数
    public void setValue(int value) {
        this.mValue = value;
        if (mRingViewAnim != null) {
            clearAnimation();
        }
        startAnimation(mRingViewAnim);
    }

    public void setValueNameList(List<String> list) {
        valueNameList.clear();
        valueNameList.addAll(list);
    }

    private class RingViewAnim extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (valueNameList.size() > 0) {
                pointAngle_show = startAngle + interpolatedTime * sweepAngle / maxLength * mValue;
//                mShowValue = String.valueOf(mValue);
                invalidate();
            }
        }
    }
    boolean isPointer = false;

    public void setPointer(boolean pointer) {
        this.isPointer = pointer;
    }
}
