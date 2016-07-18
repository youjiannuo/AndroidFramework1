package com.yn.framework.http;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.yn.framework.R;
import com.yn.framework.activity.YNCommonActivity;
import com.yn.framework.controller.BaseController;
import com.yn.framework.data.UserSharePreferences;
import com.yn.framework.feedmission.BaseFeedMission;
import com.yn.framework.interfaceview.YNOperationRemindView;
import com.yn.framework.login.LoginController;
import com.yn.framework.remind.ToastUtil;
import com.yn.framework.system.BuildConfig;
import com.yn.framework.system.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youjiannuo on 16/2/18.
 * token失效管理
 */
public enum TokenFailureManager {


    TOKEN_FAILURE_MANAGER;

    //是否已经发送的网络请求
    private boolean isSendLogin = false;
    private boolean isReLoad = false;

    private List<TokenParams> mNetworkTask = new ArrayList<>();
    //重新登陆的失效
    private List<TokenParams> mFailNetworkTask = new ArrayList<>();

    public boolean setTokenParams(TokenParams params) {
        if (checkParams(params.activity, params.YNOperationRemindView)) return false;
        //添加需要从新发送的请求
        mNetworkTask.add(params);
        return setTokenParams(params.YNOperationRemindView, params.activity);
    }

    private boolean setTokenParams(YNOperationRemindView mYNOperationRemindView, final YNCommonActivity mActivity) {

        //token失效
        LoginController loginController = new LoginController(this, mActivity);
        String id = UserSharePreferences.getId();
        String pwd = UserSharePreferences.getPwd();
        String openId = UserSharePreferences.getOpenId();
        if ((!StringUtil.isEmpty(id) && !StringUtil.isEmpty(pwd)) || !StringUtil.isEmpty(openId)) {
            if (!isSendLogin) {
                isSendLogin = true;
                loginController.login(id, pwd, openId);
            }
        } else {
            mNetworkTask.clear();
            Class cls;
            try {
                cls = Class.forName(mActivity.getString(R.string.yn_login_class));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return true;
            }
            Intent intent = new Intent(mActivity, cls);
            mActivity.startActivity(intent);
            mYNOperationRemindView.closeProgressDialog();
            return true;
        }

        loginController.setOnPwdErrorListener(new LoginController.OnPwdErrorListener() {
            @Override
            public void onError() {
                isReLoad = true;
                //密码错误跳过去重新登录
//                UserSharePreferences.clear();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Intent intent = mActivity.getPackageManager().getLaunchIntentForPackage(mActivity.getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mActivity.startActivity(intent);
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }, 100);

            }
        });
        return false;
    }

    public boolean checkParams(YNCommonActivity mActivity, YNOperationRemindView mYNOperationRemindView) {
        if (mActivity == null || mActivity.isFinishing() || mYNOperationRemindView == null) {
            return true;
        }
        return false;
    }

    //登陆成功
    public void onLoginSuccess(Boolean is) {
        isSendLogin = false;
        if (is) {
            //登陆成功
            while (mNetworkTask.size() != 0) {
                //重新发送请求
                TokenParams params = mNetworkTask.get(0);
                if (params == null || params.baseController == null || params.task == null)
                    continue;
                new BaseFeedMission(params.baseController).sendMessage(params.task);
                mNetworkTask.remove(0);
            }
        } else if (!isReLoad) {
            mFailNetworkTask.clear();
            mFailNetworkTask.addAll(mNetworkTask);
            mNetworkTask.clear();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    while (mFailNetworkTask.size() != 0) {
                        setTokenParams(mFailNetworkTask.get(0));
                        mFailNetworkTask.remove(0);
                    }
                }
            }, 3000);
        }
        if (!BuildConfig.ENVIRONMENT) {
            //重新发送请求
            if (is) {
                ToastUtil.showNormalMessage("重新登陆成功");
            } else {
                if (!isReLoad)
                    ToastUtil.showNormalMessage("重新登陆失败，3秒后重新登陆...");
            }
        }

        isReLoad = false;
    }

    public static class TokenParams {
        public HttpExecute.NetworkTask task;
        public YNCommonActivity activity;
        public YNOperationRemindView YNOperationRemindView;
        public BaseController baseController;
    }

}
