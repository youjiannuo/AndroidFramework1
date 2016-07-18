package com.yn.framework.remind;


import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yn.framework.R;
import com.yn.framework.system.ContextManager;

import java.util.Date;

public class ToastUtil {

    private static String text = "";
    private static long time = 0;


    //show normal message
    private static void showNewNormalMessage(String s, int drawableId) {
        if (s == null || s.trim().length() == 0) return;
        final long t = new Date().getTime();
        if (text.equals(s) && t - time < 2000) {
            //如果小于2秒 就不会有提醒
            return;
        } else {
            text = s;
            time = t;
        }
        View view = LayoutInflater.from(ContextManager.getContext()).inflate(R.layout.y_view_toast, null);
        ((TextView) view.findViewById(R.id.msg)).setText(s);
        ((ImageView) view.findViewById(R.id.icon)).setImageResource(drawableId);
        Toast toast = new Toast(ContextManager.getContext());
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showNewSuccessMessage(String s) {
        showNewNormalMessage(s, R.drawable.hfh_toast_right);
    }

    public static void showNewSuccessMessage(int stringId) {
        showNewSuccessMessage(ContextManager.getString(stringId));
    }

    //show normal message
    public static void showNormalMessage(String s) {
        if (s == null || s.trim().length() == 0) return;
        final long t = new Date().getTime();
        if (text.equals(s) && t - time < 2000) {
            //如果小于2秒 就不会有提醒
            return;
        } else {
            text = s;
            time = t;
        }
//        Toast toast = new Toast(ContextManager.getContext());
//        toast.setGravity(Gravity.CENTER_VERTICAL , 0 , 0);
//        toast.setText(s);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.show();
        try {
            Toast toast = Toast.makeText(ContextManager.getContext(), s, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //show normal message
    public static void showNormalMessage(int stringId) {
        Context context = ContextManager.getContext();
        showNormalMessage(context.getString(stringId));
    }

    //show success message
    public static void showSuccess(String s) {
        showNormalMessage(s);
    }

    //show success message
    public static void showSuccessMessage(int stringId) {
        showNormalMessage(stringId);
    }

    // show fail message
    public static void showFailMessage(String s) {
        showNormalMessage(s);
    }

    // show fail message
    public static void showFailMessage(int stringId) {
        showNormalMessage(stringId);
    }

    public static void showFailMessageHandler(int stringId) {
        showNormalMessageHandler(stringId);
    }

    public static void showNormalMessageHandler(int stringId) {
        Message msg = new Message();
        msg.arg1 = stringId;
        new android.os.Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                showNormalMessage(msg.arg1);
            }
        }.sendMessage(msg);
    }

    public static void showNormalMessageHandler(String stringId) {
        Message msg = new Message();
        msg.obj = stringId;
        new android.os.Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                showNormalMessage(msg.obj.toString());
            }
        }.sendMessage(msg);
    }

}
