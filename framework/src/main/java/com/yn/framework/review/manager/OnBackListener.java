package com.yn.framework.review.manager;

import android.view.View;

/**
 * Created by youjiannuo on 16/3/17.
 */
public interface OnBackListener {

    boolean checkParams();

    String[] getHttpValue();

    Object[] getTitleAndMsgValue();

    String[] getButtonString();

    boolean onItemClick(View view, int position, Object data);

    void onHttpSuccess(View view, int position, Object data);

    void onHttpFail(View view, int position, Object data);
}
