package com.yn.framework.remind;

import android.app.Activity;
import android.app.ProgressDialog;


/**
 * 等待进度条
 *
 * @author Administrator
 */
public class FhfProgressDialog {

    private ProgressDialog mProgressDialog = null;
    final private Activity mActivity;

    private ProgressDialog getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }

        return mProgressDialog;
    }

    /**
     * @param message
     */
    public void setMessage(String message) {
        getProgressDialog().setMessage(message);
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        getProgressDialog().setTitle(title);
    }

    public FhfProgressDialog(Activity activity) {

        this.mActivity = activity;
    }

    public void show(String text) {
        getProgressDialog().setMessage(text);
        getProgressDialog().show();
    }

    public void close() {
        getProgressDialog().dismiss();
    }

    public boolean isShow() {
        return getProgressDialog().isShowing();
    }

}
