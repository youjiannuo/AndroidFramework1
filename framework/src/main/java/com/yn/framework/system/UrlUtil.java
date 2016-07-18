package com.yn.framework.system;

import com.yn.framework.data.UserSharePreferences;

/**
 * Created by youjiannuo on 16/7/15
 */
public class UrlUtil {


    public static String getUrl(String url) {
        return BuildConfig.HOST + url;
    }

    public static String getRedirectUrl(String url) {
        String token = UserSharePreferences.getToken();
        if (url.contains("token=")) {
            return url;
        }
        if (url.contains("?")) {
            url += "&token=" + token + "&version=" + BuildConfig.VERSION_NAME;
        } else {
            url += "?token=" + token + "&version=" + BuildConfig.VERSION_NAME;
        }
        return url;
    }

}
