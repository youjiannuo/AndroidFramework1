package com.yn.framework.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.yn.framework.activity.YNCommonActivity;
import com.yn.framework.interfaceview.YNOperationRemindView;
import com.yn.framework.R;

import java.util.Date;

/**
 * Created by youjiannuo on 16/1/7.
 */
public class YNFrameLayout extends FrameLayout implements YNOperationRemindView, View.OnClickListener {


    //加载出问题了
    protected FrameLayout mDataFailFrameLayout = null;
    //加载的数据为空
    protected View mLoadDataNullView = null;
    //加载文件
    protected FrameLayout mProgress = null;
    //监听
    protected YNCommonActivity.OnErrorReLoadListener mOnErrorReLoadListener;
    //操作
    private YNOperationRemindView mYNOperationRemindView;


    public YNFrameLayout(Context context) {
        super(context);
        initView();
    }

    public YNFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.y_view_operation_remind, this);
    }

    public void setYNOperationRemindView(YNOperationRemindView l) {
        mYNOperationRemindView = l;
    }

    @Override
    public void showLoadDataNullView() {
        getLoadDataNullView().setVisibility(View.VISIBLE);
    }

    @Override
    public void closeLoadDataNullView() {
        getLoadDataNullView().setVisibility(View.GONE);
    }

    @Override
    public void showProgressDialog() {
        Activity activity = (Activity) getContext();
        if (activity == null || activity.isFinishing()) return;
        closeLoadFailDialog();
        getProgress().setVisibility(View.VISIBLE);
        View v = mProgress.getChildAt(0);
        Animation mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fhf_progress);
        LinearInterpolator lin = new LinearInterpolator();
        mAnimation.setInterpolator(lin);
        v.startAnimation(mAnimation);
        //屏蔽不可以点击1
        mProgress.setSelected(false);
        mProgress.setOnClickListener(this);
    }

    @Override
    public final void closeProgressDialog() {
        getProgress().setVisibility(View.GONE);
        mProgress.setSelected(false);
    }

    @Override
    public void setOnErrorReLoadListener(YNCommonActivity.OnErrorReLoadListener l) {
        mOnErrorReLoadListener = l;
    }

    @Override
    public void showLoadFailDialog() {
        getDataFailFrameLayout().setVisibility(View.VISIBLE);
        mDataFailFrameLayout.setOnClickListener(this);
    }


    @Override
    public void closeLoadFailDialog() {
        closeLoadFailView();
        if (mYNOperationRemindView != null) {
            mYNOperationRemindView.closeLoadFailDialog();
        }
    }

    @Override
    public boolean isShowLoadFailDialog() {
        return getDataFailFrameLayout().getVisibility() == View.VISIBLE;
    }

    public void closeLoadFailView() {
        getDataFailFrameLayout().setVisibility(View.GONE);
    }

    public FrameLayout getDataFailFrameLayout() {
        return mDataFailFrameLayout != null ? mDataFailFrameLayout : (mDataFailFrameLayout = (FrameLayout) findViewById(R.id.fail));
    }

    public View getLoadDataNullView() {
        return mLoadDataNullView != null ? mLoadDataNullView : (mLoadDataNullView = findViewById(R.id.upLoadNull));
    }

    public FrameLayout getProgress() {
        return mProgress != null ? mProgress : (mProgress = (FrameLayout) findViewById(R.id.progress1));
    }

    /**
     * 如果加载数据错误，
     */
    protected void onReLoadDataFromNetwork() {
        closeLoadFailDialog();
//        showLoadNewDataDialog();
        showProgressDialog();
        if (mOnErrorReLoadListener != null) {
            mOnErrorReLoadListener.onErrorReLoad();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mDataFailFrameLayout) {
            long time = mDataFailFrameLayout.getTag() == null ? 0 : (long) mDataFailFrameLayout.getTag();
            long nowTime = new Date().getTime();
            if (nowTime - time > 500) {
                onReLoadDataFromNetwork();
                mDataFailFrameLayout.setTag(nowTime);
            }
        }
    }
}
