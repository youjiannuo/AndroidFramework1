package com.yn.framework.model;


import com.yn.framework.controller.BackTask;

/**
 * Created by youjiannuo on 15/11/18.
 * 数据缓存
 */
public class CacheModel {
    private Object model;
    private String cacheString;
    private BackTask task;

    public String getCacheString() {
        return cacheString;
    }

    public void setCacheString(String cacheString) {
        this.cacheString = cacheString;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public BackTask getTask() {
        return task;
    }

    public void setTask(BackTask task) {
        this.task = task;
    }
}
