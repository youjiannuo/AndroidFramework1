package com.yn.framework.view;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.yn.framework.review.YNEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by youjiannuo on 16/4/15.
 */
public class YNAmountEditText extends YNEditText {
    public YNAmountEditText(Context context) {
        super(context);
        initView();
    }

    public YNAmountEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public YNAmountEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        setFilters(new InputFilter[]{new CashierInputFilter()});
    }

    @Override
    public boolean checkParams() {
        return false;
    }

    @Override
    public String getTextString() {
        return null;
    }

    class CashierInputFilter implements InputFilter {
        Pattern mPattern;

        //输入的最大金额
        private static final int MAX_VALUE = Integer.MAX_VALUE;
        //小数点后的位数
        private static final int POINTER_LENGTH = 2;

        private static final String POINTER = ".";

        private static final String ZERO = "0";

        public CashierInputFilter() {
            mPattern = Pattern.compile("([0-9]|\\.)*");
        }

        /**
         * @param source 新输入的字符串
         * @param start  新输入的字符串起始下标，一般为0
         * @param end    新输入的字符串终点下标，一般为source长度-1
         * @param dest   输入之前文本框内容
         * @param dstart 原内容起始坐标，一般为0
         * @param dend   原内容终点坐标，一般为dest长度-1
         * @return 输入内容
         */
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String sourceText = source.toString();
            String destText = dest.toString();

            //验证删除等按键
            if (TextUtils.isEmpty(sourceText)) {
                return "";
            }

            Matcher matcher = mPattern.matcher(source);
            //已经输入小数点的情况下，只能输入数字
            if (destText.contains(POINTER)) {
                if (!matcher.matches()) {
                    return "";
                } else {
                    if (POINTER.equals(source)) {  //只能输入一个小数点
                        return "";
                    }
                }

                //验证小数点精度，保证小数点后只能输入两位
                int index = destText.indexOf(POINTER);
                int length = dend - index;

                if (length > POINTER_LENGTH) {
                    return dest.subSequence(dstart, dend);
                }
            } else {
                //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点和0
                if (!matcher.matches()) {
                    return "";
                } else {
                    if ((POINTER.equals(source) || ZERO.equals(source)) && TextUtils.isEmpty(destText)) {
                        return "";
                    }
                }
            }

            //验证输入金额的大小
            double sumText = Double.parseDouble(destText + sourceText);
            if (sumText > MAX_VALUE) {
                return dest.subSequence(dstart, dend);
            }

            return dest.subSequence(dstart, dend) + sourceText;
        }
    }

}
