package com.yn.framework.system;

import android.app.Activity;
import android.app.Application;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by youjiannuo on 15/7/1.
 */
public abstract class YNApplication extends Application {

    private YNUncaughtExceptionHandler mYNUncaughtExceptionHandler = null;
    private Map<String, Activity> mActivity = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        ContextManager.setContext(this);
        //获得全局异常
        mYNUncaughtExceptionHandler = YNUncaughtExceptionHandler.init();
        mYNUncaughtExceptionHandler.setContent(getApplicationContext());

        BuildConfig.ENVIRONMENT = getEnvironment();
        BuildConfig.APPLICATION_ID = getApplicationId();
        BuildConfig.HOST = getHost();
        BuildConfig.VERSION_NAME = getVersionName();
    }

    public Activity getActivity(Class cls) {
        return mActivity.get(cls.getName());
    }

    public void setActivity(Activity activity) {
        mActivity.put(activity.getClass().getName(), activity);
    }

    public void clear(Activity nowActivity) {
        for (Activity activity : mActivity.values()) {
            if (activity != null && activity != nowActivity) {
                activity.finish();
            }
        }
        if (nowActivity != null) {
            nowActivity.finish();
        }
        mActivity.clear();
    }

    public void addOnUnCaughtExceptionListener(OnUnCaughtExceptionListener l) {
        if (mYNUncaughtExceptionHandler != null)
            mYNUncaughtExceptionHandler.addOnUnCaughtExceptionListener(l);
    }

    public void removeOnUnCaughtExceptionListener(OnUnCaughtExceptionListener l) {
        if (mYNUncaughtExceptionHandler != null)
            mYNUncaughtExceptionHandler.removeOnUnCaughtExceptionListener(l);
    }

    /**
     * get host
     */
    public abstract String getHost();

    /**
     * get environment
     */
    public abstract boolean getEnvironment();

    public abstract String getApplicationId();

    public abstract String getVersionName();
}
