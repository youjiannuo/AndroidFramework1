package com.yn.framework.activity;

import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.yn.framework.review.OnHttpListener;
import com.yn.framework.review.YNHttpOperation;
import com.yn.framework.system.TimeUtil;
import com.yn.framework.view.YNSwipeRefreshLayout;

/**
 * Created by youjiannuo on 16/4/20.
 * hfh,七月
 */
public abstract class YNAutomaticFragment extends BaseFragment implements OnHttpListener, SwipeRefreshLayout.OnRefreshListener {

    //是否已经获取数据
    private boolean mGetInfo = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //刷新
    private int mRefreshNum = 0;
    //等待进度条
    private int mProgressNum = 0;
    //时间器
    private TimeUtil mTimeUtil;
    //是否显示图片
//    private boolean mAnimation = true;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        boolean is = mFrameLayout == null;
//        View view = super.onCreateView(inflater, container, savedInstanceState);
//        if (is) {
//            mShowView.setVisibility(View.GONE);
//        }
//        return view;
//    }

    @Override
    public void onResume() {
        super.onResume();
        sendHttp();
    }

    @Override
    protected void initView() {
        super.initView();
        mTimeUtil = new TimeUtil();
        if (getSwipeRefreshLayoutId() != 0) {
            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(getSwipeRefreshLayoutId());
        }
    }

    @Override
    protected void setViewData() {
        super.setViewData();
        if (mSwipeRefreshLayout != null) {
            if (mSwipeRefreshLayout instanceof YNSwipeRefreshLayout) {
                YNSwipeRefreshLayout ynSwipeRefreshLayout = ((YNSwipeRefreshLayout) mSwipeRefreshLayout);
                ynSwipeRefreshLayout.setOnRefreshListener(this);
            } else {
                mSwipeRefreshLayout.setOnRefreshListener(this);
            }
        }
    }

    protected void sendHttp() {
        if (!mTimeUtil.checkoutTime(500)) {
            return;
        }
        mProgressNum = 0;
        int http[] = getHttpId();
        String values[][] = getHttpValue();
        for (int i = 0; i < http.length; i++) {
            YNHttpOperation httpOperation = (YNHttpOperation) findViewById(http[i]);
            String value[];
            if (i < values.length)
                value = values[i];
            else value = new String[0];
            httpOperation.showErrorView();
            httpOperation.setOnHttpListener(this);
            httpOperation.startHttp(this, value);
        }
    }

    //需要发送的请求
    protected int[] getHttpId() {
        return new int[0];
    }

    protected String[][] getHttpValue() {
        return new String[0][0];
    }

    protected int getSwipeRefreshLayoutId() {
        return 0;
    }

    @Override
    public void onErrorReLoad() {
        super.onErrorReLoad();
        sendHttp();
    }

    @Override
    public void closeProgressDialog() {
        int mHttp[] = getHttpId();
        boolean is = true;
        if (mHttp != null) {
            mProgressNum++;
            if (mProgressNum >= mHttp.length) {
                is = true;
            } else is = false;
        }
        if (is) {
            super.closeProgressDialog();
        }
    }

    @Override
    public void showLoadFailDialog() {
        if (!mGetInfo) {
            super.showLoadFailDialog();
            //将展示View隐藏起来
            mShowView.setVisibility(View.GONE);
        }
    }

    @Override
    public void closeLoadFailDialog() {
        super.closeLoadFailDialog();
        mShowView.setVisibility(View.VISIBLE);
    }

    @Override
    @Deprecated
    public void onHttpSuccess(Object obj) {
        mGetInfo = true;
        mRefreshNum++;
        closeRefresh();
//        showAnimationView();
    }

    @Override
    @Deprecated
    public void onHttpFail(Object obj) {
        mRefreshNum++;
        closeRefresh();
//        showAnimationView();
    }

    @Override
    public void onHttpSuccess(Object obj, View view) {
        mGetInfo = true;
        mRefreshNum++;
        closeRefresh();
//        showAnimationView();
    }

    @Override
    public void onHttpFail(Object obj, View view) {
        mRefreshNum++;
        closeRefresh();
    }

//    //显示UI
//    protected void showAnimationView() {
//        if (mAnimation && mShowView.getVisibility() == View.GONE) {
//            Animation.alpha0To1(mShowView, 100, new Animation.AnimationObjectListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    super.onAnimationStart(animation);
//                    mShowView.setAlpha(0);
//                    mShowView.setVisibility(View.VISIBLE);
//                }
//            });
//            mAnimation = false;
//        }
//    }

    protected void closeRefresh() {

        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSwipeRefreshLayout != null && mRefreshNum >= getHttpId().length) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }, 400);
    }

    @Override
    public void onRefresh() {
        mRefreshNum = 0;
        sendHttp();
    }
}
