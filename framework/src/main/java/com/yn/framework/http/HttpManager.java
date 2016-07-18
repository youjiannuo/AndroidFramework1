package com.yn.framework.http;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by youjiannuo on 16/4/25.
 * 管理请求发送过猛
 */
public enum HttpManager {

    HTTP_MANAGER;

    Map<String, Long> mMap = new HashMap<>();

    public boolean checkoutHttp(HttpExecute.NetworkTask task) throws Exception {
        if (task.notCheckout) {
            return true;
        }
        String key = getMapString(task);
        Long time = mMap.get(key);
        if (time == null) {
            mMap.put(key, new Date().getTime());
            return true;
        } else {
            long nowTime = new Date().getTime();
            if (nowTime - time > 100) {
                mMap.put(key, nowTime);
                return true;
            }
        }
        return false;
    }

    private String getMapString(HttpExecute.NetworkTask task) {
        String result = task.url;
        if (task.backTask != null) {
            result += task.backTask.cacheKey + task.backTask.method + task.backTask.callInterface;
        }
        if (task.keys == null || task.values == null || task.keys.length == 0 || task.values.length == 0) {
            return result;
        }
        for (int i = 0; i < task.keys.length; i++) {
            result += task.keys[i] + task.values[i];
        }
        return result;
    }

}
