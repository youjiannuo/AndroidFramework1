package com.yn.framework.review.manager;

import android.view.View;

/**
 * Created by youjiannuo on 16/3/17.
 */
public class YNListenerManager implements View.OnClickListener {

    private String mOnClick;
    private Object mMethod;
    private Object mData;

    public YNListenerManager(String onClick, Object method, View view) {
        mOnClick = onClick;
        mMethod = method;
        view.setOnClickListener(this);
    }

    public void setData(Object data, int position) {
        mData = data;
    }


    @Override
    public void onClick(View v) {

    }
}
