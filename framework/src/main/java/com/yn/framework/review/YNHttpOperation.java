package com.yn.framework.review;

import com.yn.framework.activity.BaseFragment;
import com.yn.framework.review.manager.YNController;

/**
 * Created by youjiannuo on 16/4/20.
 */
public interface YNHttpOperation {
    //获取到http事件
    void setOnHttpListener(OnHttpListener l);

    //是否需要显示加载错误
    void showErrorView();

    YNController startHttp(String... values);

    YNController startHttp(BaseFragment baseFragment, String... values);

    YNController getYNController();
}
