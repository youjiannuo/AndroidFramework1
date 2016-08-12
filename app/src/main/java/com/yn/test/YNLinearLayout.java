package com.yn.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by youjiannuo on 16/7/27
 */
public class YNLinearLayout extends LinearLayout {

    public YNLinearLayout(Context context) {
        super(context);
    }

    public YNLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("YNLinearLayout onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        System.out.println("YNLinearLayout onLayout");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        System.out.println("YNLinearLayout  Draw");
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        System.out.println("YNLinearLayout  dispatchDraw");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("YNLinearLayout  onDraw");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("djw");

        return true;
    }
}
