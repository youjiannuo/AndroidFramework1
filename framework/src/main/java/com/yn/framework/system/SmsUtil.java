package com.yn.framework.system;

import android.widget.EditText;

import com.yn.framework.data.DataUtil;
import com.yn.framework.data.YNSharedPreferences;
import com.yn.framework.review.YNTimerTextView;

import java.util.Date;

/**
 * Created by youjiannuo on 16/4/20.
 */
public class SmsUtil {
    private static final String FILE = "SMS";
    private Class mCls;

    public SmsUtil(EditText editText, YNTimerTextView timerTextView, Class cls) {
        mCls = cls;
        editText.setText(getPhone());
        long time = DataUtil.getLong(getTime());
        int size = (int) (new Date().getTime() - time);
        if (time != 0 && size < 60000) {
            timerTextView.startTime(size);
        }else {
            clear();
        }
    }

    public void save(String token, String phone, long time) {
        String name = mCls.getName();
        YNSharedPreferences.saveInfo(name + "_time", time + "", FILE);
        YNSharedPreferences.saveInfo(name + "_token", token, FILE);
        YNSharedPreferences.saveInfo(name + "_phone", phone, FILE);
    }

    public String getToken() {
        String name = mCls.getName();
        return YNSharedPreferences.getInfo(name + "_token", FILE);
    }

    public String getTime() {
        String name = mCls.getName();
        return YNSharedPreferences.getInfo(name + "_time", FILE);
    }

    public String getPhone() {
        String name = mCls.getName();
        return YNSharedPreferences.getInfo(name + "_phone", FILE);
    }

    public void clear() {
        String name = mCls.getName();
        YNSharedPreferences.saveInfo(name + "_time", "", FILE);
        YNSharedPreferences.saveInfo(name + "_token", "", FILE);
        YNSharedPreferences.saveInfo(name + "_phone", "", FILE);
    }


}
