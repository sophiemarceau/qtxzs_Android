package com.wecoo.qutianxia.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wecoo on 2017/5/22.
 * 切除圆角
 */

public class RoundCornersImageView extends ImageView {

    private float radiusX;
    private float radiusY;

    public RoundCornersImageView(Context context) {
        super(context);
        init();
    }

    public RoundCornersImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundCornersImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        radiusX = 20;
        radiusY = 20;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        Rect rect = new Rect(0, 0, getWidth(), getHeight());
        RectF rectF = new RectF(rect);
        path.addRoundRect(rectF, radiusX, radiusY, Path.Direction.CCW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
