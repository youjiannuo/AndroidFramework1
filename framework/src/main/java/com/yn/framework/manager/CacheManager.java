package com.yn.framework.manager;


import com.google.gson.Gson;
import com.yn.framework.controller.BackTask;
import com.yn.framework.data.MyGson;
import com.yn.framework.data.YNSharedPreferences;
import com.yn.framework.model.CacheModel;

/**
 * Created by youjiannuo on 15/11/18.
 * 主要是为了缓存数据管理器
 */
public enum CacheManager {

    CACHE_MANAGER;

    private String FILE_NAME = "cacheData";

    public void saveData(CacheModel model) {
        if (model == null) return;
        checkout(model.getTask());
        String value = new Gson().toJson(model);
        YNSharedPreferences.saveInfo(model.getTask().cacheKey, value, FILE_NAME);
    }

    public CacheModel getData(BackTask task) {
        checkout(task);
        String value = YNSharedPreferences.getInfo(task.getCacheKey(), FILE_NAME);
        return new MyGson().fromJson(value, CacheModel.class);
    }


    private void checkout(BackTask task) {
        if (task == null) {
            throw new NullPointerException("BackTask is not null");
        }
        if (task.getCacheKey() == null || task.getCacheKey().length() == 0) {
            throw new NullPointerException("BackTask.cacheKey is not null or \"\"");
        }

    }

}
