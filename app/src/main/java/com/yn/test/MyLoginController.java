package com.yn.test;

import com.yn.framework.activity.YNCommonActivity;
import com.yn.framework.controller.BackTask;
import com.yn.framework.data.JSON;
import com.yn.framework.data.UserSharePreferences;
import com.yn.framework.login.LoginController;
import com.yn.framework.system.StringUtil;

/**
 * Created by youjiannuo on 16/7/18
 */
public class MyLoginController extends LoginController {

    public MyLoginController(Object methodObj, YNCommonActivity activity) {
        super(methodObj, activity);
    }

    /**
     * 需要实现这个方法,因为Token失效了,那么将会自动调用该方法,
     * 在登陆的时候,需要使用这个方法来登陆
     *
     * @param id     账号
     * @param pwd    密码
     * @param openid 第三方登陆的openId
     */
    @Override
    public void login(String id, String pwd, String openid) {
        super.login(id, pwd, openid);
        if (StringUtil.isEmpty(openid)) {
            UserSharePreferences.saveIdAndPwd(id, pwd);
            sendMessage(R.array.yn_login_id, id, pwd);
        } else {
            UserSharePreferences.saveOpenId(openid);
            sendMessage(R.array.yn_login_third, openid);
        }
    }


    /**
     * 上述方式成功以后会回调下述方法
     *
     * @param object   回调的方法
     * @param backTask 任务
     * @return 返回的数据将会反射给, 调用这个对象的Activity.onSuccess(String)
     */
    @Override
    public Object visitSuccess(Object object, BackTask backTask) {
        JSON json = new JSON(object.toString());
        //请把token保存起来
        UserSharePreferences.saveToken(json.getString("token"));
        //设置方法,将会把信息回调ActivityLogin方法
        return object.toString();
    }

    /**
     * 发送Http失败
     *
     * @param obj      失败的信息
     * @param backTask
     * @return
     */
    @Override
    public Object visitFail(Object obj, BackTask backTask) {
        JSON json = new JSON(obj.toString());
        if ("1".equals(json.getString("status"))) {
            //账号和密码错误,请加入这些,以便框架调用
            //因为框架里面有自动登陆功能
            if (mOnPwdErrorListener != null) {
                mOnPwdErrorListener.onError();
            }
        }
        return super.visitFail(obj, backTask);
    }
}
