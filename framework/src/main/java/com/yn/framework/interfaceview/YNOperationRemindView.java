package com.yn.framework.interfaceview;

import android.content.Context;

import com.yn.framework.activity.YNCommonActivity;

/**
 * Created by youjiannuo on 16/1/8.
 */
public interface YNOperationRemindView {

    void showLoadDataNullView();

    void closeLoadDataNullView();

    void showProgressDialog();

    void showLoadFailDialog();

    void closeLoadFailDialog();

    boolean isShowLoadFailDialog();

    void closeProgressDialog();

    void setOnErrorReLoadListener(YNCommonActivity.OnErrorReLoadListener l);

    Context getContext();


}
