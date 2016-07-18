package com.yn.framework.controller;


import com.yn.framework.data.UserSharePreferences;

public class BackTask {
    public int callInterface = -1;
    public String method;
    /**
     * 如果需要缓存数据，就在这里面设置key,如何设置为null或者""，就不会自动缓存数据
     * 注意！！由于缓存数据都是存储在同一个文件，所以在设置这个Key的时候，不可以有相同，
     * 如果设置相同的会造成读取缓存数据错误。建议使用url作为表示，后面添加别的作为参数
     */
    public String cacheKey = null;
    public String cls = null;
    //是否需要Toast
    public boolean isToast = true;
    //是否需要加载等待进度条
    public boolean isProgress = true;
    //是否获取了缓存数据
    public boolean isGetCache = false;

    public static BackTask build(int callInterface) {
        return build(callInterface, null);
    }

    public static BackTask build(int callInterface, String method) {
        return build(callInterface, method, null, null);
    }

    public static BackTask build(int callInterface, String method, String cacheKey) {
        return build(callInterface, method, cacheKey, String.class);
    }

    public static BackTask build(int callInterface, String method, String cacheKey, Class cls) {
        BackTask backTask = new BackTask();
        backTask.callInterface = callInterface;
        backTask.method = method;
        if (cls != null) {
            backTask.cls = cls.getName();
        }
        if (cacheKey != null && cacheKey.length() != 0) {
            String data = UserSharePreferences.getId();
            backTask.cacheKey = data + cacheKey;
        }
        return backTask;
    }

    public String getCacheKey() {
        return cacheKey;
    }


    public int getCallInterface() {
        return callInterface;
    }

    public void setCallInterface(int callInterface) {
        this.callInterface = callInterface;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


}