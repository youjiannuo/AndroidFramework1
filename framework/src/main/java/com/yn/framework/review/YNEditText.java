package com.yn.framework.review;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import com.yn.framework.R;
import com.yn.framework.file.IDCard;
import com.yn.framework.remind.ToastUtil;
import com.yn.framework.review.manager.Util;
import com.yn.framework.system.StringUtil;

/**
 * Created by youjiannuo on 16/4/26.
 * 編輯框
 */
public class YNEditText extends EditText implements OnCheckParams {
    //编辑类型
    private int mInputType = 0;
    private String mCheckParameters;


    public YNEditText(Context context) {
        super(context);
    }

    public YNEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.YNView);
        mInputType = array.getInt(R.styleable.YNView_inputType, 0);
        mCheckParameters = array.getString(R.styleable.YNView_checkParameters);
        array.recycle();
    }

    public YNEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean checkParams() {
        String text = getText().toString();
        //身份证
        switch (mInputType) {
            case 1:
                String result = IDCard.IDCardValidate(text);
                if (!StringUtil.isEmpty(result)) {
                    ToastUtil.showFailMessage(result);
                    return true;
                }
                return false;
            default:
                return checkoutParameters(text);
        }
    }

    @Override
    public String getTextString() {
        return getText().toString();
    }

    private boolean checkoutParameters(String text) {
        int length = text.length();
        if (length == 0) {
            ToastUtil.showFailMessage(getHint().toString());
            return true;
        }
        if (StringUtil.isEmpty(mCheckParameters)) return false;
        String resluts[] = mCheckParameters.split(",");
        for (String result : resluts) {
            if (result.contains("max:")) {
                if (!checkoutLength(length, result, -1, "max:")) {
                    return true;
                }
            } else if (result.contains("min:")) {
                if (!checkoutLength(length, result, 1, "min:")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkoutLength(int length, String data, int opeartion, String key) {
        String params[] = data.split(" ");

        if (params.length == 0) return true;
        if (params.length == 1) {
            ToastUtil.showFailMessage("没有添加Commit");
            return false;
        }
        String parameter = params[0];
        int a = Util.getInt(getValue(parameter, key));
        String commit = getValue(params[1], "commit:");
        if (length * opeartion < a * opeartion) {
            ToastUtil.showFailMessage(commit);
            return false;
        }
        return true;
    }


    private String getValue(String data, String key) {
        int index = data.indexOf(key) + key.length();
        if (index == -1) return "";
        return data.substring(index, data.length());
    }


}
