package com.ctrl.forum.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 给图片设置边框
 * Created by Administrator on 2016/5/18.
 */
public class StorkPicView extends ImageView{
    private int co;
    private int borderwidth;

    public StorkPicView(Context context) {
        super(context);
    }
    public StorkPicView(Context context, AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
    }

    public StorkPicView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //设置颜色
    public void setColour(int color){
        co = color;
    }

    //设置边框宽度
    public void setBorderWidth(int width){
        borderwidth = width;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画边框
        Rect rec = canvas.getClipBounds();
        rec.bottom--;
        rec.right--;
        Paint paint = new Paint();
        //设置边框颜色
        paint.setColor(co);
        paint.setStyle(Paint.Style.STROKE);
        //设置边框宽度
        paint.setStrokeWidth(borderwidth);
        canvas.drawRect(rec, paint);
    }
}
