package com.yn.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by youjiannuo on 16/8/8
 */
public class YNImageView extends ImageView {
    public YNImageView(Context context) {
        super(context);
    }

    public YNImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("YNImageView onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        System.out.println("YNImageView onLayout");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        System.out.println("YNImageView  Draw");
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        System.out.println("YNImageView  dispatchDraw");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("YNImageView  onDraw");
    }


}
