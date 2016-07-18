package com.yn.framework.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yn.framework.interfaceview.YNDispatchTouchEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youjiannuo on 15/8/10.
 * 创建该方法是为了解决自动播放banner，加强banner滑动更佳的流程
 */
public class YNSwipeRefreshLayout extends SwipeRefreshLayout implements YNDispatchTouchEventListener, SwipeRefreshLayout.OnRefreshListener {

    private List<OnRefreshListener> mOnRefreshListener = new ArrayList<>();

    private OnDispatchTouchEvent mOnDispatchTouchEvent = null;

    public YNSwipeRefreshLayout(Context context) {
        super(context);
    }

    public YNSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnDispatchTouchEvent(OnDispatchTouchEvent l) {
        mOnDispatchTouchEvent = l;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean is = false;
        if (mOnDispatchTouchEvent != null) {
            is = mOnDispatchTouchEvent.onDispatchTouchEvent(ev);
        }
        if (!is) {
            return super.dispatchTouchEvent(ev);
        }
        return true;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        if (listener == null) return;
        mOnRefreshListener.add(listener);
        super.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        for (int i = 0; i < mOnRefreshListener.size(); i++) {
            mOnRefreshListener.get(i).onRefresh();
        }
    }

    public interface OnDispatchTouchEvent {
        boolean onDispatchTouchEvent(MotionEvent event);
    }

}
