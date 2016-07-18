package com.yn.framework.review.manager;

import android.content.Context;

import com.yn.framework.system.ContextManager;

/**
 * Created by youjiannuo on 16/4/26.
 */
public class YNResourceUtil {
    
    public static int getLayoutId(String paramString) {
        Context paramContext = ContextManager.getContext();
        return paramContext.getResources().getIdentifier(paramString, "layout",
                paramContext.getPackageName());
    }

    public static int getStringId(String paramString) {
        Context paramContext = ContextManager.getContext();
        return paramContext.getResources().getIdentifier(paramString, "string",
                paramContext.getPackageName());
    }

    public static int getDrawableId(String paramString) {
        Context paramContext = ContextManager.getContext();
        return paramContext.getResources().getIdentifier(paramString,
                "drawable", paramContext.getPackageName());
    }

    public static int getStyleId(String paramString) {
        Context paramContext = ContextManager.getContext();
        return paramContext.getResources().getIdentifier(paramString,
                "style", paramContext.getPackageName());
    }

    public static int getId(String paramString) {
        Context paramContext = ContextManager.getContext();
        return paramContext.getResources().getIdentifier(paramString, "id", paramContext.getPackageName());
    }

    public static int getColorId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,
                "color", paramContext.getPackageName());
    }

    public static int getArrayId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,
                "array", paramContext.getPackageName());
    }

}
