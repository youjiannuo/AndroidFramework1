package com.yn.framework.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.yn.framework.R;
import com.yn.framework.model.BaseExpandListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youjiannuo on 16/1/13.
 */
public abstract class YJNExpandableListView<P, C> extends ExpandableListView
        implements YNOperationListView<BaseExpandListViewModel> {

    private List<BaseExpandListViewModel> mList;
    private Adapter mAdapter;
    private BucketListAdapter.LoadMoreListener mLoadMoreListener;
    private View mLoadMoreView;
    //刷新控件
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected boolean mIsRefresh = true;
    protected boolean mIsLoadMore = true;
    private LayoutInflater mInflater;

    public YJNExpandableListView(Context context) {
        super(context);
        initView();
    }

    public YJNExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        mInflater = LayoutInflater.from(context);
    }


    private void initView() {
        if (getParent() instanceof SwipeRefreshLayout) {
            mSwipeRefreshLayout = (SwipeRefreshLayout) getParent();
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        }
        setGroupIndicator(null);
        setDividerHeight(0);
    }

    @Override
    public void setAdapter(List<BaseExpandListViewModel> data) {
        if (mAdapter == null) {
            mList = new ArrayList<>();
            mList.addAll(data);
            mAdapter = new Adapter();
            setAdapter(mAdapter);
            if (mList.size() >= getPageNumber())
                setOnLoadMoreListener(mLoadMoreListener);
        } else {
            addReData(data);
        }
        showAllExpandGroup();
    }

    public void showAllExpandGroup() {
        for (int i = 0; i < getSize(); i++) {
            expandGroup(i);
        }
    }

    public View getView(int layoutId) {
        return mInflater.inflate(layoutId, null);
    }

    @Override
    public void addData(List<BaseExpandListViewModel> data) {
        if (mAdapter == null) {
            setAdapter(data);
        } else {
            if (mList == null) {
                mList = new ArrayList<>();
            }
            mList.addAll(data);
            notifyDataSetChanged();
            showAllExpandGroup();
        }
    }

    @Override
    public void addData(BaseExpandListViewModel data) {
        addData(data, getSize());
    }

    public void addData(BaseExpandListViewModel model, int index) {

        if (mAdapter == null) {
            List<BaseExpandListViewModel> data = new ArrayList<>();
            data.add(model);
            setAdapter(data);
        } else {
            if (mList == null) {
                mList = new ArrayList<>();
            }
            mList.add(index, model);
            notifyDataSetChanged();
            showAllExpandGroup();
        }
    }

    @Override
    public void addReData(List<BaseExpandListViewModel> data) {
        mList.clear();
        addData(data);
        if (data.size() >= getPageNumber())
            setOnLoadMoreListener(mLoadMoreListener);
    }

    @Override
    public void remove(BaseExpandListViewModel data) {
        mList.remove(data);
        notifyDataSetChanged();
    }

    public void remove(int from) {
        for (int i = from; i < mList.size(); i++) {
            mList.remove(i);
        }
    }

    @Override
    public void removes(List<BaseExpandListViewModel> data) {
        mList.removeAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void removeAll() {
        mList.clear();
    }

    @Override
    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setIsLoadMore(boolean is) {
        mIsLoadMore = is;
    }

    @Override
    public boolean getIsLoadMore() {
        return mIsLoadMore;
    }

    @Override
    public void setIsRefresh(boolean is) {
        mIsRefresh = is;
    }

    @Override
    public boolean getIsRefresh() {
        return mIsRefresh;
    }

    public void closeRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void closeLoadMore() {
        if (mAdapter != null) {
            mAdapter.disableLoadMore();
        }
    }

    public void openLoadMore() {
        if (mAdapter != null) {
            mAdapter.enableLoadMore();
        }
    }

    //开启下拉刷新
    public void disableRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    //打开下拉刷新
    public void enableRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    @Override
    public void setOnLoadMoreListener(BucketListAdapter.LoadMoreListener l) {
        if (getIsLoadMore()) {
            if (l != null) {
                mLoadMoreListener = l;
            }
        }
    }

    public List<BaseExpandListViewModel> getList() {
        return mList;
    }

    @Override
    public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener l) {
        if (getParent() instanceof SwipeRefreshLayout) {
            mSwipeRefreshLayout = (SwipeRefreshLayout) getParent();
            mSwipeRefreshLayout.setOnRefreshListener(l);
        }
    }

    @Override
    public int getSize() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public int getPageNumber() {
        return 20;
    }

    public abstract View newGroupView(P groupData, int groupPosition, boolean isExpanded, View convertView, ViewGroup parent, boolean isChild);

    public abstract View setGroupViewData(P groupData, int groupPosition, boolean isExpanded, View convertView, ViewGroup parent, boolean isChild);

    public abstract View newChildView(C childData, int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent);

    public abstract View setChildViewData(C childData, int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent);

    class Adapter extends BaseExpandableListAdapter {

        private boolean mLoadMoreEnabled;

        public void enableLoadMore() {
            mLoadMoreEnabled = true;
            notifyDataSetChanged();
        }

        public void disableLoadMore() {
            mLoadMoreEnabled = false;
            notifyDataSetChanged();
        }

        @Override
        public int getGroupCount() {
            if (mList == null) return 0;
            if (mLoadMoreEnabled) {
                return mList.size() + 1;
            }
            return mList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (groupPosition >= mList.size()) return 0;
            BaseExpandListViewModel model = mList.get(groupPosition);
            if (model.getC() == null) return 0;
            return model.getC().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mList.get(groupPosition).getC();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            BaseExpandListViewModel model = mList.get(groupPosition);
            if (model.getC() == null) return null;
            return model.getC().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (groupPosition == mList.size() && mLoadMoreEnabled) {
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore();
                }
                return getLoadMoreView();
            }
            if (convertView == getLoadMoreView()) {
                convertView = null;
            }
            BaseExpandListViewModel model = mList.get(groupPosition);
            P p = (P) model.getP();
            boolean isChild = (model.getC() != null && model.getC().size() != 0);
            if (convertView == null) {
                convertView = newGroupView(p, groupPosition, isExpanded, convertView, parent, isChild);
            }
            setGroupViewData(p, groupPosition, isExpanded, convertView, parent, isChild);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            C c = (C) mList.get(groupPosition).getC().get(childPosition);
            if (convertView == null) {
                convertView = newChildView(c, groupPosition, childPosition, isLastChild, convertView, parent);
            }
            setChildViewData(c, groupPosition, childPosition, isLastChild, convertView, parent);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    protected boolean isOnlyOneChild(int groupPosition) {
        return mList.get(groupPosition).getC().size() == 1;
    }

    protected boolean isLastChild(int groupPosition, int childPosition) {
        if (mList == null) return true;
        BaseExpandListViewModel model = mList.get(groupPosition);
        if (model.getC() == null) return true;
        return model.getC().size() - 1 == childPosition;
    }

    protected boolean isLastGroup(int groupPosition) {
        if (mList == null) return true;
        return mList.size() == groupPosition + 1;
    }

    private View getLoadMoreView() {
        if (mLoadMoreView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            mLoadMoreView = layoutInflater.inflate(R.layout.y_bucket_progress_bar, null);
        }
        return mLoadMoreView;
    }


}
