package com.yn.framework.review.manager;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;

import com.yn.framework.R;
import com.yn.framework.activity.YNCommonActivity;
import com.yn.framework.review.OnCheckParams;

/**
 * Created by youjiannuo on 16/4/26.
 */
public class YNClickBack implements OnBackListener {

    private OnCheckParams[] mOnCheckParams;
    private OnBackListener mOnBackListener;
    private YNCommonActivity mActivity;
    private String mClickViewId;
    private int mHttpSuccess = 0;

    public YNClickBack(Context context, String clickViewId, TypedArray array) {
        mActivity = (YNCommonActivity) context;
        mHttpSuccess = array.getInteger(R.styleable.YNView_httpSuccess, 0);
        mClickViewId = clickViewId;
    }

    public void setOnBackListener(OnBackListener l) {
        mOnBackListener = l;
    }

    @Override
    public boolean checkParams() {
        if (mOnCheckParams == null) {
            mOnCheckParams = Util.getClickTextViews(mActivity.getShowView(), mClickViewId);
        }
        if (mOnCheckParams == null) return false;
        for (OnCheckParams params : mOnCheckParams) {
            if (params.checkParams()) {
                return true;
            }
        }
        if (mOnBackListener != null) {
            if (mOnBackListener.checkParams()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String[] getHttpValue() {
        String[] extra = new String[0];
        if (mOnBackListener != null) {
            extra = mOnBackListener.getHttpValue();
            if (extra == null || extra.length == 0) {
                extra = new String[mOnCheckParams.length];
                for (int i = 0; i < mOnCheckParams.length; i++) {
                    extra[i] = mOnCheckParams[i].getTextString();
                }
            }
        }

        return extra;
    }

    @Override
    public Object[] getTitleAndMsgValue() {
        if (mOnBackListener != null) {
            return mOnBackListener.getTitleAndMsgValue();
        }
        return new Object[0];

    }

    @Override
    public String[] getButtonString() {
        if (mOnBackListener != null) {
            return mOnBackListener.getButtonString();
        }
        return new String[0];
    }

    @Override
    public boolean onItemClick(View view, int position, Object data) {
        if (mOnBackListener != null) {
            return mOnBackListener.onItemClick(view, position, data);
        }
        return false;
    }

    @Override
    public void onHttpSuccess(View view, int position, Object data) {
        if (mOnBackListener != null) {
            mOnBackListener.onHttpSuccess(view, position, data);
        }
        dealEnd(mActivity, mHttpSuccess);
    }

    private static void finish(Activity activity) {
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void onHttpFail(View view, int position, Object data) {
        if (mOnBackListener != null) {
            mOnBackListener.onHttpFail(view, position, data);
        }
    }

    public static void dealEnd(Activity activity, int httpSuccess) {
        switch (httpSuccess) {
            case 2:
                finish(activity);
                break;
            case 2 | 4:
                finish(activity);
                break;
        }
    }


}
