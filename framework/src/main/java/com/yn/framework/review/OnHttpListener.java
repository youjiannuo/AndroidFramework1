package com.yn.framework.review;

import android.view.View;

/**
 * Created by youjiannuo on 16/4/20.
 */
public interface OnHttpListener {
    @Deprecated
    void onHttpSuccess(Object obj);
    @Deprecated
    void onHttpFail(Object obj);

    void onHttpSuccess(Object obj, View view);

    void onHttpFail(Object obj, View view);

}
