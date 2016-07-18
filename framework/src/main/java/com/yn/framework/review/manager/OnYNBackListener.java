package com.yn.framework.review.manager;

import android.view.View;

/**
 * Created by youjiannuo on 16/3/17.
 */
public class OnYNBackListener implements OnBackListener {
    @Override
    public boolean checkParams() {
        return false;
    }

    @Override
    public String[] getHttpValue() {
        return new String[0];
    }

    @Override
    public Object[] getTitleAndMsgValue() {
        return new Object[0];
    }

    @Override
    public String[] getButtonString() {
        return new String[0];
    }

    @Override
    public boolean onItemClick(View view, int position, Object data) {
        return false;
    }

    @Override
    public void onHttpSuccess(View view, int position, Object data) {

    }

    @Override
    public void onHttpFail(View view, int position, Object data) {

    }
}
