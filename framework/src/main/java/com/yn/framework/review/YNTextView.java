package com.yn.framework.review;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yn.framework.R;
import com.yn.framework.data.JSON;
import com.yn.framework.review.manager.OnBackListener;
import com.yn.framework.review.manager.Util;
import com.yn.framework.review.manager.YJNView;
import com.yn.framework.review.model.ReplaceModel;
import com.yn.framework.system.StringUtil;


/**
 * Created by youjiannuo on 16/3/16.
 */
public class YNTextView extends TextView implements OnYNOperation {

    private String[] mDataKeys;
    private YJNView mYJNView;
    private int mPosition;
    protected String mStartString = "";
    protected String mEndString = "";
    protected String mValue = "";
    private int mType = 1;
    private Object mData;
    private String mNotSetData = ""; //到达某一个字符不需要设置
    private ReplaceModel mPlace[];
//    private int mStartStringColor = 0x0000;
//    private int mStartStringSize = 0;
//    private int mEndStringColor = 0x0000;
//    private int mEndStringSize = 0;

    public YNTextView(Context context) {
        super(context);
    }

    public YNTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.YNView);
        mStartString = array.getString(R.styleable.YNView_startString);
        mEndString = array.getString(R.styleable.YNView_endString);
        mNotSetData = array.getString(R.styleable.YNView_not_set_data);
//        mStartStringColor = array.getColor(R.styleable.YNView_startStringColor, 0x0000);
//        mStartStringSize = array.getIndex(R.styleable.YNView_startStringSize);
//        mEndStringColor = array.getColor(R.styleable.YNView_endStringColor, 0x0000);
//        mEndStringSize = array.getIndex(R.styleable.YNView_endStringSize);
        mPlace = Util.getCodeReplace(array.getString(R.styleable.YNView_replace));
        mYJNView = new YJNView(this, context, attrs, array);
        mDataKeys = mYJNView.getSetDataNames();

        if (mStartString == null) {
            mStartString = "";
        }
        if (mEndString == null) {
            mEndString = "";
        }
        if (!StringUtil.isEmpty(mEndString) || !StringUtil.isEmpty(mStartString)) {
            setTextStartAndEnd(mStartString, defaultValue(), mEndString);
        }
    }


    public YNTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHttpId(int http) {
        mYJNView.setHttpId(http);
    }

    protected String defaultValue() {
        return "";
    }


    @Override
    public void setData(Object obj) {
        mYJNView.setData(obj);
        mData = obj;
        if (obj instanceof String) {
            mValue = obj.toString();
        } else if (mDataKeys != null && mDataKeys.length != 0) {
            JSON json = (JSON) obj;
            mValue = json.getStrings(mDataKeys);
        } else return;
        String startString = mStartString;
        String endString = mEndString;
        if (mPlace != null && mPlace.length != 0) {
            if (mPlace[0].value.equals(mValue.trim())) {
                if (mPlace[1].type == 1) {
                    startString = "";
                    endString = "";
                }
                mValue = mPlace[1].value;
            }
        }
        setText(startString, mValue, endString);
    }

    public String getValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = obj.toString();
        } else if (mDataKeys != null && mDataKeys.length != 0) {
            JSON json = (JSON) obj;
            value = json.getStrings(mDataKeys);
        }
        return value;
    }

    public void setText(String startString, String value, String endString) {
        if (!StringUtil.isEmpty(mNotSetData) && mNotSetData.equals(value)) {
            return;
        }
        setTextStartAndEnd(startString, value, endString);
    }

    public void setTextStartAndEnd(String startString, String value, String endString) {
//        if (mStartStringSize == 0 && mEndStringSize == 0 && mEndStringColor == 0x0000 && mStartStringColor == 0x0000) {
        setText(startString + value + endString);
//        } else {
//            SpannableStringBuilder builder = new SpannableStringBuilder(startString + value + endString);
//
//            if (mStartStringColor != 0x0000) {
//                builder.setSpan(new ForegroundColorSpan(mStartStringColor), 0, startString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            if (mStartStringSize != 0) {
//                builder.setSpan(new AbsoluteSizeSpan(mStartStringSize), 0, startString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            if (mEndStringColor != 0x0000) {
//                builder.setSpan(new ForegroundColorSpan(mEndStringColor), (startString + value).length(), (startString + value + endString).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            if (mStartStringSize != 0) {
//                builder.setSpan(new AbsoluteSizeSpan(mEndStringSize), (startString + value).length(), (startString + value + endString).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//        }
    }

    public void onStartClick() {
        mYJNView.onStartClick();
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public void setOnBackListener(OnBackListener l) {
        mYJNView.setOnBackListener(l, mPosition);
    }

    @Override
    public void setPosition(int index) {
        mPosition = index;
    }

    @Override
    public int getOnClick() {
        return mYJNView.getOnClick();
    }

    @Override
    public Object getData() {
        return mData;
    }
}
