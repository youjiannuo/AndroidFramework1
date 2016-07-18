package com.yn.test;

import com.yn.framework.system.*;

/**
 * Created by youjiannuo on 16/7/18
 */
public class MyApplication extends YNApplication {
    @Override
    public String getHost() {
        return BuildConfig.HOST;
    }

    @Override
    public boolean getEnvironment() {
        return BuildConfig.ENVIRONMENT;
    }

    @Override
    public String getApplicationId() {
        return BuildConfig.APPLICATION_ID;
    }

    @Override
    public String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }
}
