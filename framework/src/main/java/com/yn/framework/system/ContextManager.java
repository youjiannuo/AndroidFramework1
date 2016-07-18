package com.yn.framework.system;

import android.content.Context;

public class ContextManager {

    private static Context context = null;

    protected static void setContext(Context context) {
        ContextManager.context = context;
    }

    public static Context getContext() {
        return context;
    }

    public static String getString(int stringId) {
        return context.getResources().getString(stringId);
    }

    public static String[] getArrayString(int stringId) {
        return context.getResources().getStringArray(stringId);
    }


}
