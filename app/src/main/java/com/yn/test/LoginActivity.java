package com.yn.test;

import android.view.View;

import com.yn.framework.activity.YNAutomaticActivity;

/**
 * Created by youjiannuo on 16/7/18
 */
public class LoginActivity extends YNAutomaticActivity {

    private MyLoginController mLoginController;

    @Override
    protected void initView() {
        super.initView();
        mLoginController = new MyLoginController(this, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        //登陆
        mLoginController.login("", "", "");
    }

    /**
     * 这个方法将会被login调用
     * @param data
     */
    public void onSuccess(String data) {

    }


}
