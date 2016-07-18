package com.yn.framework.view;

import android.support.v4.widget.SwipeRefreshLayout;

import java.util.List;

/**
 * Created by youjiannuo on 15/10/19.
 */
public interface YNOperationListView<T> {

    public void setAdapter(List<T> data);

    public void addData(List<T> data);

    public void addData(T data, int index);

    public void addData(T data);

    void addReData(List<T> data);

    void remove(T data);

    public void removes(List<T> data);

    public void removeAll();

    public void notifyDataSetChanged();

    void setOnLoadMoreListener(BucketListAdapter.LoadMoreListener l);

    void setRefreshListener(SwipeRefreshLayout.OnRefreshListener l);

    void setIsLoadMore(boolean is);

    boolean getIsLoadMore();

    void setIsRefresh(boolean is);

    boolean getIsRefresh();

    void closeRefresh();

    void closeLoadMore();

    void openLoadMore();

    void disableRefresh();

    void enableRefresh();

    int getSize();

    int getPageNumber();
}
