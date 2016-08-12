package com.yn.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by youjiannuo on 16/7/28
 */
public class YNLinearLayout1 extends YNLinearLayout {

    public YNLinearLayout1(Context context) {
        super(context);
    }

    public YNLinearLayout1(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("YNLinearLayout1 onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        System.out.println("YNLinearLayout1 onLayout");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        System.out.println("YNLinearLayout1  Draw");
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        System.out.println("YNLinearLayout1  dispatchDraw");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("YNLinearLayout1  onDraw");
    }


}
