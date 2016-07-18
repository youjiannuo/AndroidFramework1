package com.yn.framework.http;


import com.yn.framework.controller.BackTask;

public interface HttpVisitCallBack {
    void visitNetworkStart(BackTask backTask);//开始请求

    boolean visitAllNetworkSuccess(Object obj, BackTask backTask);

    void visitNetworkSuccess(Object obj, BackTask backTask);//访问网页成功

    void visitNetworkProgress(int project);

    void visitNetworkFail(BackTask backTask);//访问网络失败

    void visitNetworkFail(Object obj, BackTask backTask);

    void visitNetworkCancel(BackTask backTask);//取消访问

    void visitTokenFailure(HttpExecute.NetworkTask task);

    //设置缓存数据
    boolean getCache(BackTask task);

}