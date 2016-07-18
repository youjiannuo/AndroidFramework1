package com.yn.framework.system;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;


public class SmsReceiver extends BroadcastReceiver {

    private Class<?> mClass = null;

    public void setClass(Class<?> cs) {
        mClass = cs;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        Bundle bundle = intent.getExtras();
        Object o = bundle.get("pdus");
        if (o == null) return;
        Object[] objects = (Object[]) o;
        for (Object obj : objects) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String body = smsMessage.getDisplayMessageBody();
            if (!body.contains("慧理财") && !body.contains("融宝")) {
                continue;
            }
            Intent in = new Intent(context.getApplicationContext(), mClass);
            in.putExtra("code", getCode(body));
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(in);
            break;
        }

    }

    public static String getCode(String result) {
        String a = "";
        for (int i = 0; i < result.length(); i++) {
            char c = result.charAt(i);
            if (c >= '0' && c <= '9') {
                a += c;
            } else {
                if (a.length() == 6) {
                    return a;
                }
            }
        }
        return "";
    }

    public static SmsReceiver instance(Context context, Class cls) {
        SmsReceiver smsReceiver = new SmsReceiver();
        smsReceiver.setClass(cls);
        IntentFilter filter = new IntentFilter();
        filter.setPriority(997);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        context.registerReceiver(smsReceiver, filter);
        return smsReceiver;
    }


}
