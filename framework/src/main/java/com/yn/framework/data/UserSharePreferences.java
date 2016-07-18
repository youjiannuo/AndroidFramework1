package com.yn.framework.data;

import com.yn.framework.system.ContextManager;
import com.yn.framework.R;

/**
 * Created by youjiannuo on 16/7/12
 */

public class UserSharePreferences {

    private static String TOKEN_KEY = ContextManager.getString(R.string.yn_save_token_key);
    private static String FILE_INFO = ContextManager.getString(R.string.yn_save_user_info_file);
    private static String USER_ID = ContextManager.getString(R.string.yn_save_name_key);
    private static String USER_PWD = ContextManager.getString(R.string.yn_save_pwd_key);
    private static String OPEN_ID = ContextManager.getString(R.string.yn_open_id);

    //存储token
    public static String getToken() {
        return YNSharedPreferences.getInfo(TOKEN_KEY, FILE_INFO);
    }

    public static void saveToken(String token) {
        YNSharedPreferences.saveInfo(TOKEN_KEY, token, FILE_INFO);
    }

    public static void saveIdAndPwd(String id, String pwd) {
        YNSharedPreferences.saveInfo(USER_ID, id, FILE_INFO);
        YNSharedPreferences.saveInfo(USER_PWD, pwd, FILE_INFO);
    }

    public static void saveOpenId(String openId) {
        YNSharedPreferences.saveInfo(OPEN_ID, openId, FILE_INFO);
    }

    public static String getId() {
        return YNSharedPreferences.getInfo(USER_ID, FILE_INFO);
    }

    public static String getPwd() {
        return YNSharedPreferences.getInfo(USER_PWD, FILE_INFO);
    }

    public static String getOpenId() {
        return YNSharedPreferences.getInfo(OPEN_ID, FILE_INFO);
    }

    //清除缓存
    public static void clear() {
        YNSharedPreferences.clear(FILE_INFO);
    }

}
