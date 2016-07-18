package com.yn.framework.view;

import android.view.View;

import java.util.List;

/**
 * Created by youjiannuo on 15/9/8.
 */
public interface ListViewInterface<T> {
    //设置数据
    void setAdapter(List<T> data);

    //创建控件
    View createView(int position, T data);

    //初始化数据
    void setViewData(View view, int position, T data);

    //获取的数据的个数
    @Deprecated
    int getItemCount();

}
