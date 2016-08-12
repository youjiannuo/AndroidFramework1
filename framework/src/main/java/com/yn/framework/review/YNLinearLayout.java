package com.yn.framework.review;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.yn.framework.R;
import com.yn.framework.activity.BaseFragment;
import com.yn.framework.activity.YNCommonActivity;
import com.yn.framework.data.JSON;
import com.yn.framework.interfaceview.YNOperationRemindView;
import com.yn.framework.remind.ToastUtil;
import com.yn.framework.review.manager.OnBackListener;
import com.yn.framework.review.manager.YJNView;
import com.yn.framework.review.manager.YNController;
import com.yn.framework.review.manager.YNManager;
import com.yn.framework.system.MethodUtil;


/**
 * Created by youjiannuo on 16/3/16.
 */
public class YNLinearLayout extends LinearLayout implements OnYNOperation, YNHttpOperation {

    private int mType = 0;
    private YJNView mYJNView;
    protected String[] mDataKeys;
    private int mPosition = 0;
    private OnHttpListener mOnHttpListener;
    private boolean mShowError = false;
    private boolean mFirstRun = false;
    private Object mData;
    private YNCommonActivity mActivity;
    private OnYNOperation[] mYNOperations;

    public YNLinearLayout(Context context) {
        super(context);
    }

    public YNLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.YNView);
        mType = array.getInt(R.styleable.YNView_type, 0);
        mYJNView = new YJNView(this, context, attrs, array);
        mDataKeys = mYJNView.getSetDataNames();
        if (context instanceof YNCommonActivity) {
            mActivity = (YNCommonActivity) context;
        }

    }

    @Override
    public void setData(Object obj) {
        if (obj == null) return;

        mData = obj;
        mYJNView.setData(obj);
        if (mType == 3) {
            //将数据设置进来
            return;
        }
        if (mType == 1 && mDataKeys != null && mDataKeys.length != 0) {
            //当前的LinearLayout是数组
            JSON json = (JSON) obj;
            new YNManager(this, new JSON(json.getStrings(mDataKeys))).setData(mPosition);
        } else if (mType == 2) {
            //最开始的LinearLayout
            if (obj instanceof JSON) {
                new YNManager(this, (JSON) obj).setStartData(mPosition , this);
            } else {
                new YNManager(this, new JSON(obj.toString())).setStartData(mPosition,this);
            }
        } else {
            ToastUtil.showFailMessage("请设置app:type=start");
        }

    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public void setOnBackListener(OnBackListener l) {
        mYJNView.setOnBackListener(l);
    }

    @Override
    public void setPosition(int index) {
        mPosition = index;
        if (mYJNView != null) {
            mYJNView.setPosition(index);
        }
    }

    @Override
    public int getOnClick() {
        return mYJNView.getOnClick();
    }

    @Override
    public Object getData() {
        return mData;
    }

    @Override
    public OnYNOperation[] getYNOperation() {
        return mYNOperations;
    }

    @Override
    public void setYNOperation(OnYNOperation[] operation) {
        mYNOperations = operation;
    }

    @Override
    public void showErrorView() {
        mShowError = true;
    }

    @Override
    public YNController startHttp(String... values) {
        return startHttp(null, values);
    }

    public void setOnHttpListener(OnHttpListener l) {
        mOnHttpListener = l;
    }

    @Override
    public YNController startHttp(BaseFragment baseFragment, final String... values) {
        if (mFirstRun) {
            YNOperationRemindView ynOperationRemindView;
            if (baseFragment != null) {
                ynOperationRemindView = baseFragment;
                baseFragment.showTopProgress();
            } else {
                ynOperationRemindView = mActivity;
                mActivity.showTopProgress();
            }
            mYJNView.setShowProgress(ynOperationRemindView.isShowLoadFailDialog());
        }
        //发送的是http请求
        mFirstRun = true;
        mType = 2;
        mYJNView.showErrorView(mShowError);
        mYJNView.setOnClick(1);
        mYJNView.setBaseFragment(baseFragment);
        mYJNView.startClick(new OnBackListener() {
            @Override
            public boolean checkParams() {
                return false;
            }

            @Override
            public String[] getHttpValue() {
                return values;
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
                if (mOnHttpListener != null) {
                    mOnHttpListener.onHttpSuccess(data);
                    mOnHttpListener.onHttpSuccess(data, YNLinearLayout.this);
                }
                mYJNView.setData(data);
                setData(data);
            }

            @Override
            public void onHttpFail(View view, int position, Object data) {
                if (mOnHttpListener != null) {
                    mOnHttpListener.onHttpFail(data);
                    mOnHttpListener.onHttpFail(data, YNLinearLayout.this);
                }
            }
        });
        return null;
    }


    @Override
    public YNController getYNController() {
        return null;
    }

    public void setFirstRun(boolean is) {
        mFirstRun = is;
    }

    public void setHttpId(int httpId) {
        mYJNView.setHttpId(httpId);
    }

    public void setType(int type) {
        mType = type;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        boolean mIsBackGround = MethodUtil.getParam(this, "mBackground") != null;
        if (l != null && !mIsBackGround) {
            super.setBackgroundResource(R.drawable.hfh_border_gray_bg_white_click);
        }
        super.setOnClickListener(l);
    }
}
