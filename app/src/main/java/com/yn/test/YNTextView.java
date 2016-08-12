package com.yn.test;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by youjiannuo on 16/7/27
 */
public class YNTextView extends TextView {
    public YNTextView(Context context) {
        super(context);
    }

    public YNTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        new android.os.Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        }.sendEmptyMessage(0);
    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println("YNTextView onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        System.out.println("YNTextView onLayout");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        System.out.println("YNTextView  Draw");
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        System.out.println("YNTextView  dispatchDraw");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("YNTextView  onDraw");

    }

}

