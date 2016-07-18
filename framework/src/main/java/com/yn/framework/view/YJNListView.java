package com.yn.framework.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yn.framework.exception.YNCreateViewException;
import com.yn.framework.exception.YNSetViewDataException;
import com.yn.framework.system.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by youjiannuo on 15/9/7.
 */
public abstract class YJNListView<T> extends ListView implements ListViewInterface<T>, YNOperationListView<T> {

    //标题
    public static final int LIST_TITLE = 1;

    protected int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    protected int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;


    protected Adapter mAdapter = null;
    //刷新控件
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected boolean mIsRefresh = false;
    protected boolean mIsLoadMore = false;
    //加载更多的回调
    protected BucketListAdapter.LoadMoreListener mLoadMoreListener;

    private ViewGroup mView;

    //需要添加浮动标题
    private List<Title> mTitles;
    private int mHead = 0;
    //获取
    protected OnItemClickListener<T> mOnItemClickListener;

    public YJNListView(Context context) {
        super(context);
        initView();
    }

    public YJNListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    public YJNListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        initBounceListView();
    }

    protected void initView() {

        setDividerHeight(0);
        setSelector(new BitmapDrawable());
        setCacheColorHint(0x00000000);
        setOnScrollListener(
                new OnScrollListener() {
//                    private boolean scrollFlag = false;// 标记是否滑动
//                    private int lastVisibleItemPosition;// 标记上次滑动位置

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
//                        if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                            scrollFlag = true;
//                        } else {
//                            scrollFlag = false;
//                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (mTitles == null || mTitles.size() == 0) return;
//                        //下滑
//                        boolean mTouchUp = false; //上滑
//                        if (firstVisibleItem > lastVisibleItemPosition) {
//                            //上滑
//                            mTouchUp = false;
//                        }
//                        if (firstVisibleItem < lastVisibleItemPosition) {
//                            mTouchUp = true;
//                        }
//                        if (firstVisibleItem != lastVisibleItemPosition) {
//                            lastVisibleItemPosition = firstVisibleItem;
//                        }
                        boolean is = false;
                        Title title = null;

                        for (int i = mTitles.size() - 1; i >= 0; i--) {
                            if (firstVisibleItem - mHead >= mTitles.get(i).position) {
                                is = true;
                                title = mTitles.get(i);
                                break;
                            }
                        }
                        if (mShowIndex > firstVisibleItem - mHead) {
                            if (mShowView != null) {
                                mShowView.setVisibility(GONE);
                                mShowView = null;
                            }
                        }
//
                        if (is) {
                            ViewGroup mViewGroup = mViewMap.get(title.layoutId);
                            if (mViewGroup == null) {
                                mViewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(title.layoutId, null);
                                mView = (ViewGroup) getParent();
                                while (isCheck(mView)) {
                                    mView = (ViewGroup) mView.getParent();
                                }
                                ViewUtil.ScreenInfo screenInfo = ViewUtil.getScreenInfo(YJNListView.this, mView);
                                if (mView instanceof RelativeLayout) {
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                                    params.topMargin = screenInfo.y;
                                    mView.addView(mViewGroup, params);
                                } else {
                                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                                    params.topMargin = screenInfo.y;
                                    mView.addView(mViewGroup, params);
                                }
                                mViewMap.put(title.layoutId, mViewGroup);
                                mViewGroup.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                            }

                            if (mViewGroup == mShowView && mShowIndex == title.position) {
                                return;
                            }
                            if (mShowView != null) {
                                mShowView.setVisibility(GONE);
                            }
                            mViewGroup.setVisibility(VISIBLE);
                            mShowView = mViewGroup;
                            mShowIndex = title.position;

                            if (isResetTitle()) {
                                setTitle(title.position, mViewGroup, title.t);
                            }
                        }

                    }
                });
    }


    @Override
    public void setAdapter(List<T> data) {
        if (mAdapter == null) {
            mAdapter = new Adapter(getContext(), data, getColumn());
            setAdapter(mAdapter);
            if (data.size() >= getPageNumber())
                setOnLoadMoreListener(mLoadMoreListener);
        } else {
            addReData(data);
        }
    }

    public void addData(List<T> data) {
        if (mAdapter == null) {
            setAdapter(data);
        } else {
            mAdapter.getList().addAll(data);
            notifyDataSetChanged();
            if (data.size() >= getPageNumber())
                setOnLoadMoreListener(mLoadMoreListener);
        }
    }

    public int getColumn() {
        return 1;
    }

    public void addData(T data) {
        mAdapter.add(data);
    }

    @Override
    public void addData(T data, int index) {

    }

    @Override
    public void addReData(List<T> data) {
        mAdapter.getList().clear();
        notifyDataSetChanged();
        mAdapter.getList().addAll(data);
        notifyDataSetChanged();
        if (data.size() >= getPageNumber())
            setOnLoadMoreListener(mLoadMoreListener);
    }

    public void remove(T data) {
        mAdapter.getList().remove(data);
        notifyDataSetChanged();
    }

    public void removes(List<T> data) {
        mAdapter.getList().removeAll(data);
        notifyDataSetChanged();
    }

    public void removeAll() {
        mAdapter.getList().clear();
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    //打开下拉刷新
    public void enableRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    //关闭刷新更多，当得到数据可以调用这个方法，刷新结束
    public void closeRefresh() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 500);
        }

    }

    //禁用下来刷新
    public void disableRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    public void openLoadMore() {
        if (mAdapter != null) {
            mAdapter.enableLoadMore();
        }
    }

    public void closeLoadMore() {
        if (mAdapter != null) {
            mAdapter.disableLoadMore();
        }
    }


    @Override
    public void setOnLoadMoreListener(BucketListAdapter.LoadMoreListener l) {
        if (isLoadMore()) {
            if (l != null) {
                mLoadMoreListener = l;
                if (mAdapter != null)
                    mAdapter.setLoadMoreListener(l);
            }
        }
    }


    public List<T> getList() {
        if (mAdapter != null) {
            return mAdapter.getList() == null ? (List<T>) new ArrayList<>() : mAdapter.getList();
        }
        return new ArrayList<>();
    }

    public T getItem(int index) {
        return mAdapter.getList().get(index);
    }

    @Override
    public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener l) {
        ViewParent viewParent = getParent();
        while (!(viewParent instanceof YNFrameWork)) {
            if (viewParent instanceof SwipeRefreshLayout) {
                mSwipeRefreshLayout = (SwipeRefreshLayout) viewParent;
                mSwipeRefreshLayout.setOnRefreshListener(l);
                mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
                break;
            }
            if (viewParent == null) {
                break;
            }
            viewParent = viewParent.getParent();
        }
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

    class Adapter extends BucketListAdapter<T> {


        public Adapter(Context ctx, List<T> elements, int i) {
            super(ctx, elements, i);
        }


        @Override
        protected View newView(int position, T element) {
            try {
                return createView(position, element);
            } catch (Exception e) {
                e.printStackTrace();
                new YNCreateViewException(e).throwException();
            }
            return new TextView(getContext());
        }

        @Override
        protected void bindView(View view, int position, T element) {

            try {
                view = initView(view, position, element);
                if (view != null) {
                    setViewData(view, position, element);
                }
            } catch (Exception e) {
                e.printStackTrace();
                new YNSetViewDataException(e).throwException();
            }
        }
    }

    protected View initView(View view, int position, T data) {
        return view;
    }

    protected void setViewTitleVisible(View view, int showViewId, int value, int hideViewId) {

    }


    public int getPageNumber() {
        return 10;
    }

    @Override
    public int getSize() {
        if (mAdapter == null) return 0;
        return mAdapter.getList().size();
    }

    //是否需要下载更多的控件
    protected boolean isLoadMore() {
        return true;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    protected View getView(int resId, int width, int height) {
        View v = LayoutInflater.from(getContext()).inflate(resId, null);
        LayoutParams params = new LayoutParams(width, height);
        v.setLayoutParams(params);
        return v;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isShowAllView()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public boolean isShowAllView() {
        return false;
    }

    private boolean mShow = false;
    private Map<Integer, ViewGroup> mViewMap = new HashMap<>();
    private ViewGroup mShowView;
    private int mShowIndex = -1;


    private boolean isCheck(View view) {
        if (view instanceof YNRelativeLayout) {
            return false;
        } else if (view instanceof YNFrameWork) {
            return false;
        }
        return true;
    }


    protected boolean isResetTitle() {
        return true;
    }

    /**
     * 添加需要标题浮动窗口
     *
     * @param position
     * @param t
     */
    protected void addFloatTitle(String key, int position, T t, int layoutId) {
        if (key == null) {
            throw new NullPointerException("key is not null");
        }
        Title title = new Title();
        title.position = position;
        title.t = t;
        title.layoutId = layoutId;
        title.key = key;
        if (mTitles == null) mTitles = new ArrayList<>();
        boolean is = false;
        for (int i = 0; i < mTitles.size(); i++) {
            if (mTitles.get(i).key.equals(key)) {
                mTitles.get(i).t = t;
                mTitles.get(i).position = position;
                is = true;
                break;
            }
        }
        if (!is) {
            mTitles.add(title);
        }
    }


    @Override
    public void addHeaderView(View v) {
        super.addHeaderView(v);
        //添加了head
        mHead = 1;
    }

    protected void setTitle(int position, ViewGroup view, T t) {
    }

    public void setOnItemClickListener(OnItemClickListener<T> l) {
        mOnItemClickListener = l;
    }

    class Title {
        int position;
        T t;
        int layoutId;
        String key = "";
    }


    public interface OnItemClickListener<T> {
        void onItemClick(T t);
    }

}
