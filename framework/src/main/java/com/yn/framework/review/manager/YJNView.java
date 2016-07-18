package com.yn.framework.review.manager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.yn.framework.R;
import com.yn.framework.activity.BaseFragment;
import com.yn.framework.activity.YNCommonActivity;
import com.yn.framework.data.JSON;
import com.yn.framework.remind.RemindAlertDialog;
import com.yn.framework.remind.ToastUtil;
import com.yn.framework.system.BuildConfig;
import com.yn.framework.system.StringUtil;

/**
 * Created by youjiannuo on 16/3/17.
 */
public class YJNView implements RemindAlertDialog.OnKeyListener,
        RemindAlertDialog.OnClickListener {

    private String mDataKey;
    private String mDataKeys;
    private String mOnClickKey;
    private int mOnClick = 0;
    private int onClickValue;
    private View mView;
    private Object mData;
    private Context mContext;
    private BaseFragment mBaseFragment;
    private OnBackListener mOnBackListener;
    private Class mClass;
    private int mPosition = 0;
    private RemindAlertDialog mRemindAlertDialog;
    private int mIndex;
    private String mClickID;
    private int mHttpSuccess = 0;
    //是否显示错误信息
    private boolean mShowErrorView = false;
    //是否进度条
    private boolean mShowProgress = true;
    //发送http请求
    private YNController mYNController;
    private String mUmengKey = "";

    public YJNView(View view, Context context, AttributeSet attrs) {
        this(view, context, attrs, null);
    }

    public YJNView(View view, Context context, AttributeSet attrs, TypedArray array) {
        if (array == null)
            array = context.obtainStyledAttributes(attrs, R.styleable.YNView);
        mDataKey = array.getString(R.styleable.YNView_set_data_name);
        mDataKeys = array.getString(R.styleable.YNView_set_data_names);
        mOnClick = array.getInt(R.styleable.YNView_onClick, 0);
        mOnClickKey = array.getString(R.styleable.YNView_onClickKey);
        onClickValue = array.getResourceId(R.styleable.YNView_onClickValue, 0);
        mClickID = array.getString(R.styleable.YNView_onClickValueViewId);
        mHttpSuccess = array.getInt(R.styleable.YNView_httpSuccess, 0);
        mUmengKey = array.getString(R.styleable.YNView_umeng_key);
        String className = array.getString(R.styleable.YNView_back_class);
        mView = view;
        mContext = context;
        try {
            if (!StringUtil.isEmpty(className)) {
                mClass = Class.forName(className);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (!StringUtil.isEmpty(mClickID)) {
            mOnBackListener = new YNClickBack(mContext, mClickID, array);
            setOnBackListener(mOnBackListener);
        } else if (mOnClick != 0) {
            setOnBackListener(new OnYNBackListener());
        }
        array.recycle();
    }

    public void setOnClick(int onClick) {
        mOnClick = onClick;
    }

    public void setBaseFragment(BaseFragment fragment) {
        mBaseFragment = fragment;
    }

    public void setData(Object data) {
        mData = data;
    }

    public void setHttpId(int http) {
        onClickValue = http;
    }

    public void setOnBackListener(OnBackListener l) {
        setOnBackListener(l, mPosition);
    }

    public void setOnBackListener(final OnBackListener l, final int index) {
        if (mOnBackListener != null && mOnBackListener instanceof YNClickBack && mOnBackListener != l) {
            ((YNClickBack) mOnBackListener).setOnBackListener(l);
        } else {
            mOnBackListener = l;
        }
        mIndex = index;
        if (l != null) {
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStartClick();
                    if (!StringUtil.isEmpty(mUmengKey)) {
                        ((YNCommonActivity) mContext).addUmengClickStatistics(mUmengKey);
                    }
                }
            });
            if (mOnClick == 0) {
                mView.setClickable(false);
            } else {
                mView.setClickable(true);
            }
        }
    }

    //确定发送
    public void onStartClick() {
        if (mOnBackListener.checkParams()) return;
        String buttons[] = mOnBackListener.getButtonString();
        Object msg[] = mOnBackListener.getTitleAndMsgValue();
        if (buttons.length != 0 && msg.length != 0) {
            showRemindBox(buttons, msg[1], (String) msg[0], -1, -1);
        } else if (mOnBackListener.onItemClick(mView, mIndex, mData)) {

        } else {
            startClick();
        }
    }

    public YNController startClick(OnBackListener l) {
        mOnBackListener = l;
        return startClick();
    }

    private YNController startClick() {
        String codeValues[] = mOnBackListener.getHttpValue();
        String values[];
        String keys[] = null;
        if (mOnClickKey != null && mOnClickKey.length() != 0) {
            keys = mOnClickKey.split(",");
        }
        if (keys != null && keys.length != 0) {
            JSON json;
            if (mData == null) {
                if (!BuildConfig.ENVIRONMENT) {
                    if (mView.getId() == -1) {
                        ToastUtil.showFailMessage("请给YNLinearLayout标签里面一个Id");
                    } else {
                        ToastUtil.showFailMessage("请在YNLinearLayout标签里面设置app:type=click");
                    }
                }
                return new YNController((YNCommonActivity) mContext);
            }
            if (mData instanceof JSON) {
                json = (JSON) mData;
            } else {
                json = new JSON(mData.toString());
            }
            values = new String[keys.length + (codeValues == null ? 0 : codeValues.length)];
            for (int i = 0; i < keys.length; i++) {
                String s = json.getStrings(keys[i]);
                if (!StringUtil.isEmpty(s)) {
                    values[i] = s;
                } else {
                    values[i] = ((YNCommonActivity) mContext).getIntentString(keys[i]);
                }
            }

            for (int i = keys.length; codeValues != null && i < values.length; i++) {
                values[i] = codeValues[i - keys.length];
            }
            codeValues = values;
        }

        if (mOnClick == 1) {
            getYNController();
            //发送http请求
            mYNController.sendMessage("onSuccessResult", onClickValue, codeValues);
        } else if (mOnClick == 2) {
            //activity 跳转
            ActivityIntent.startActivity(onClickValue, mContext, codeValues);
            YNClickBack.dealEnd((Activity) mContext, mHttpSuccess);
        }
        return mYNController;
    }

    public void setShowProgress(boolean is) {
        mShowProgress = is;
    }

    public void onSuccessResult(String result) {
        if (mOnBackListener != null) {
            mOnBackListener.onHttpSuccess(mView, mPosition, result);
        }
        closeTopProgress();
    }

    public void getYNController() {
        if (mYNController == null) {
            if (mBaseFragment != null) {
                mYNController = new YNController(mBaseFragment, YJNView.this);
            } else {
                mYNController = new YNController(YJNView.this, (YNCommonActivity) mContext);
            }
        }
        mYNController.setShowProgress(mShowProgress);
        mYNController.showError(mShowErrorView);
    }

    private void closeTopProgress() {
        if (mBaseFragment != null) {
            mBaseFragment.closeTopProgress();
        } else {
            ((YNCommonActivity) mContext).closeTopProgress();
        }
    }

    public void onFailResult(String result) {
        if (mOnBackListener != null) {
            mOnBackListener.onHttpFail(mView, mPosition, result);
        }
        closeTopProgress();
    }


    protected void showRemindBox(String[] button, Object message, String title, int icon, int type) {
        if (((Activity) mContext).isFinishing()) return;
        if (mRemindAlertDialog == null) {
            mRemindAlertDialog = new RemindAlertDialog(mContext);
            mRemindAlertDialog.setOnKeyListener(this);
        }
        mRemindAlertDialog.setType(type);
        try {
            mRemindAlertDialog.show(button, title, message, icon, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showErrorView(boolean is) {
        mShowErrorView = is;
    }

    public Object getData() {
        return mData;
    }

    public String getDataKey() {
        return mDataKey;
    }

    public String getDataKeys() {
        if (!StringUtil.isEmpty(mDataKeys)) {
            return mDataKeys;
        }
        return "";
    }

    public String[] getSetDataNames() {
        return getSetDataNames(getDataKey(), getDataKeys().split(","));
    }

    public static String[] getSetDataNames(String setDataName, String[] setDataNames) {
        String keys[] = new String[0];
        if (!StringUtil.isEmpty(setDataName)) {
            keys = setDataName.split("\\.");
        } else if (setDataNames != null && setDataNames.length != 0) {
            for (int i = 0; i < setDataNames.length; i++) {
                String key[] = getSetDataNames(setDataNames[i], null);
                if (key != null && key.length != 0) {
                    keys = key;
                    break;
                }
            }
        }
        return keys;
    }

    public int getOnClick() {
        return mOnClick;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    @Override
    public void onRemindItemClick(int position, int type) {
        if (position == RemindAlertDialog.RIGHTBUTTON) {
            startClick(mOnBackListener);
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, int type, KeyEvent event) {
        return false;
    }

}
