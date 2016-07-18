package com.yn.framework.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.yn.framework.system.ContextManager;


/**
 * Created by youjiannuo on 15/10/26.
 */
public class YNSharedPreferences {

    public static void saveInfoBoolean(String key, boolean value, String fileName) {
        SharedPreferences.Editor editor = ContextManager.getContext().getSharedPreferences(fileName, Activity.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getInfoBoolean(String key, String fileName) {
        SharedPreferences share = ContextManager.getContext().getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        return share.getBoolean(key, false);
    }

    public static void saveInfo(String key, String value, String fileName) {
        SharedPreferences.Editor editor = ContextManager.getContext().getSharedPreferences(fileName, Activity.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getInfo(String key, String fileName) {
        SharedPreferences share = ContextManager.getContext().getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        return share.getString(key, "");
    }

    public static void  clear(String fileName) {
        SharedPreferences settings = ContextManager.getContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
    }

}
