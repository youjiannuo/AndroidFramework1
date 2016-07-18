package com.yn.framework.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.yn.framework.exception.YNInitDataException;
import com.yn.framework.exception.YNInitSetDataViewException;
import com.yn.framework.exception.YNInitTopBarException;
import com.yn.framework.exception.YNInitViewException;
import com.yn.framework.interfaceview.YNOperationRemindView;
import com.yn.framework.remind.RemindAlertDialog;
import com.yn.framework.view.NavigationBarView;
import com.yn.framework.view.YNFrameLayout;
import com.yn.framework.R;


/**
 * Created by youjiannuo on 16/1/17
 */
public abstract class BaseFragment extends Fragment implements
        YNOperationRemindView, View.OnClickListener, RemindAlertDialog.OnClickListener,
        RemindAlertDialog.OnKeyListener, YNCommonActivity.OnErrorReLoadListener {

    protected ViewGroup mFrameLayout;
    protected YNFrameLayout mYNFrameLayout;
    protected View mShowView;
    //提醒按钮宽
    protected RemindAlertDialog mRemindAlertDialog;
    protected NavigationBarView mBarView;
    //是否第一次执行
    private boolean isFirstOnResume;
    private YNCommonActivity.OnErrorReLoadListener mOnErrorReLoadListener;
    private int mTopProgress = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mFrameLayout == null) {
            mShowView = getShowView();
            if (mShowView == null) {
                mShowView = inflater.inflate(getViewResource(), null);
            }
            mFrameLayout = (ViewGroup) inflater.inflate(R.layout.y_fragment_framework_view, null);
            mYNFrameLayout = (YNFrameLayout) mFrameLayout.findViewById(R.id.hfhFrameLayout);
            mBarView = (NavigationBarView) mFrameLayout.findViewById(R.id.barView);
            mBarView.setVisibility(View.GONE);
            ((ViewGroup) mFrameLayout.findViewById(R.id.frameLayout1)).addView(mShowView);
            setOnErrorReLoadListener(null);
            mYNFrameLayout.setYNOperationRemindView(this);
            try {
                initTopBar();
            } catch (Exception e) {
                e.printStackTrace();
                new YNInitTopBarException(e).throwException();
            }
            try {
                initView();
            } catch (Exception e) {
                e.printStackTrace();
                new YNInitViewException(e).throwException();
            }
            try {
                setViewData();
            } catch (Exception e) {
                e.printStackTrace();
                new YNInitSetDataViewException(e).throwException();
            }
            try {
                initData();
            } catch (Exception e) {
                e.printStackTrace();
                new YNInitDataException(e).throwException();
            }

        } else {
            ViewParent parentView = mFrameLayout.getParent();
            if (parentView != null) {
                ((ViewGroup) parentView).removeView(mFrameLayout);
            }
        }

        return mFrameLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void dealBarView() {
        mBarView.getLeftView().setOnClickListener(this);
        mBarView.getRightView().setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstOnResume) {
            onNotFirstResume();
        } else {
            onFirstResume();
            isFirstOnResume = true;
        }
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onNetwork();
                onOtherOperating();
            }
        }, 500);
    }



    //第一次被执行
    protected void onFirstResume() {

    }

    //不是第一次执行
    protected void onNotFirstResume() {

    }

    public void showTopProgress() {
        if (mBarView != null) {
            mTopProgress++;
            if (mBarView.getVisibility() == View.VISIBLE) {
                mBarView.showTopProgress();
            } else {
                ((YNCommonActivity) getContext()).showTopProgress();
            }
        }
    }

    public void closeTopProgress() {
        mTopProgress--;
        if (mBarView != null && mTopProgress <= 0) {
            if (mBarView.getVisibility() == View.VISIBLE) {
                mBarView.closeTopProgress();
            } else {
                if (getContext() != null) {
                    ((YNCommonActivity) getContext()).closeTopProgress();
                }
            }
            mTopProgress = 0;
        }
    }


    public void onOtherOperating() {

    }



    public abstract int getViewResource();

    public View getShowView() {
        return null;
    }

    protected void initTopBar() {
        dealBarView();
    }

    protected void initView() {
    }

    protected void initData() {

    }

    protected void setViewData() {

    }

    public void onNetwork() {

    }

    protected View findViewById(int resId) {
        return mFrameLayout.findViewById(resId);
    }


    public void onFromActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void showBarTopView() {
        if (mBarView != null) {
            mBarView.setVisibility(View.VISIBLE);
        }
    }

    public void showRemindBox(int[] buttonId, int messageId, int titleId) {
        showRemindBox(buttonId, messageId, titleId, -1);
    }

    protected void showRemindBox(int[] buttonId, int messageId, int titleId, int type) {
        String button[] = new String[buttonId.length];
        String message = messageId == -1 ? "" : getString(messageId);
        String title = titleId == -1 ? "" : getString(titleId);
        for (int i = 0; i < button.length; i++) {
            button[i] = getString(buttonId[i]);
        }
        showRemindBox(button, message, title, type);
    }

    protected void showRemindBox(String[] button, String message, String title) {
        showRemindBox(button, message, title, -1, -1);
    }

    protected void showRemindBox(String[] button, String message, String title, int type) {
        showRemindBox(button, message, title, -1, type);
    }

    protected void showRemindBox(String[] button, String message, String title, int icon, int type) {
        if (getActivity() == null || getActivity().isFinishing()) return;

        if (mRemindAlertDialog == null) {
            mRemindAlertDialog = new RemindAlertDialog(getContext());
            mRemindAlertDialog.setOnKeyListener(this);
        }
        mRemindAlertDialog.setType(type);
        try {
            mRemindAlertDialog.show(button, title, message, icon, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    //显示UI
//
//    /**
//     * 是否立马显示
//     *
//     * @param nowShow
//     */
//    public void showView(boolean nowShow) {
//        if (nowShow || mFirstShow) {
//            mFrameLayout.setVisibility(View.VISIBLE);
//        } else {
//            mFrameLayout.setVisibility(View.GONE);
//        }
//        mFirstShow = true;
//    }

    @Override
    public void showLoadDataNullView() {
        if (mYNFrameLayout != null) {
            mYNFrameLayout.showLoadDataNullView();
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, int type, KeyEvent event) {
        return false;
    }

    @Override
    public void onRemindItemClick(int position, int type) {
        // TODO Auto-generated method stub
        if (position == RemindAlertDialog.LEFTBUTTON) {
            onRemindBoxLeftButtonClick(type);
        } else if (position == RemindAlertDialog.CENTERBUTTON) {
            onRemindBoxCenterButtonClick(type);
        } else if (position == RemindAlertDialog.RIGHTBUTTON) {
            onRemindBoxRightButtonClick(type);
        }
    }

    /**
     * 消息选择宽的左边按钮
     */
    protected void onRemindBoxLeftButtonClick(int type) {

    }

    /**
     * 消息选择宽的中间按钮
     */
    protected void onRemindBoxCenterButtonClick(int type) {

    }

    /**
     * 消息选择框的右边按钮
     */
    protected void onRemindBoxRightButtonClick(int type) {

    }


    @Override
    public void closeLoadDataNullView() {
        if (mYNFrameLayout != null) {
            mYNFrameLayout.closeLoadDataNullView();
        }
    }

    @Override
    public void showProgressDialog() {
        if (mYNFrameLayout != null) {
            mYNFrameLayout.showProgressDialog();
        }
    }

    @Override
    public void showLoadFailDialog() {
        if (mYNFrameLayout != null) {
            mYNFrameLayout.showLoadFailDialog();
        }
    }

    @Override
    public boolean isShowLoadFailDialog() {
        return mYNFrameLayout.isShowLoadFailDialog();
    }

    @Override
    public void closeLoadFailDialog() {
        mYNFrameLayout.closeLoadFailView();
    }

    @Override
    public void closeProgressDialog() {
        if (mYNFrameLayout != null) {
            mYNFrameLayout.closeProgressDialog();
        }
    }

    @Override
    public void setOnErrorReLoadListener(YNCommonActivity.OnErrorReLoadListener l) {
        if (mYNFrameLayout != null) {
            mOnErrorReLoadListener = l;
            mYNFrameLayout.setOnErrorReLoadListener(this);
        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onClick(View v) {
        if (v == mBarView.getRightView()) {
            onClickRightView(v);
        } else if (v == mBarView.getLeftView()) {
            onClickLeftView(v);
        }
    }

    public void onClickRightView(View v) {
    }

    public void onClickLeftView(View v) {
    }

    protected String[] getStringArray(int s) {
        try {
            return getContext().getResources().getStringArray(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]{};
    }

    public void setCurrentItem(int index, boolean is) {

    }

    public void onErrorReLoad() {
        if (mOnErrorReLoadListener != null) {
            mOnErrorReLoadListener.onErrorReLoad();
        }
    }

    public void addUmengClick(String key) {
        ((YNCommonActivity) getContext()).addUmengClickStatistics(key);
    }

}
