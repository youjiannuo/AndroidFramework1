package com.yn.framework.review.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.yn.framework.R;
import com.yn.framework.activity.YNCommonActivity;
import com.yn.framework.remind.ToastUtil;
import com.yn.framework.system.BuildConfig;
import com.yn.framework.system.ContextManager;
import com.yn.framework.system.StringUtil;


/**
 * Created by youjiannuo on 16/3/17.
 */
public class ActivityIntent {

    public static void startActivity(int params, Context context, String values[]) {
        String[] args = context.getResources().getStringArray(params);
        String className = "";
        String result = "";
        String keys[] = new String[0];
        String fixedValues[] = new String[0];
        for (String arg : args) {
            if (arg.contains("class:")) {
                className = get(arg);
            } else if (arg.contains("key:")) {
                String s = get(arg);
                keys = s.split(",");
            } else if (arg.contains("value:")) {
                String s = get(arg);
                fixedValues = s.split(",");
            } else if (arg.contains("result:")) {
                result = get(arg);
            }
        }

        values = getValue(fixedValues, values);
        if ("web".equals(className)) {
            if (keys.length == 0) {
                keys = new String[]{"url"};
            }
            //打开网页
            className = ContextManager.getString(R.string.yn_web_class);
        } else {
            className = BuildConfig.APPLICATION_ID + className;
        }
        if (((YNCommonActivity) context).startCheckoutActivity(className)) {
            return;
        }
        try {
            Intent intent = new Intent(context, Class.forName(className));
            if (keys.length != values.length) {
                throw new NullPointerException("keys.size() != value.size()");
            }
            for (int i = 0; i < values.length; i++) {
                intent.putExtra(keys[i], values[i]);
            }
            if (StringUtil.isEmpty(result)) {
                context.startActivity(intent);
            } else {
                ((Activity) context).startActivityForResult(intent, 0x11);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            if (!BuildConfig.ENVIRONMENT) {
                ToastUtil.showNormalMessage("跳转Activity，设置的Class有问题");
            }
        }
    }

    private static String[] getValue(String values1[], String values2[]) {
        if (values1 == null && values2 == null) return new String[0];
        else if (values1 == null) return values2;
        else if (values2 == null) return values1;
        String values[] = new String[values2.length + values1.length];
        for (int i = 0; i < values1.length; i++) {
            values[i] = values1[i];
        }
        for (int i = 0; i < values2.length; i++) {
            values[i + values1.length] = values2[i];
        }
        return values;
    }

    private static String get(String arg) {
        int i = arg.indexOf(":");
        return arg.substring(i + 1, arg.length());
    }

}
