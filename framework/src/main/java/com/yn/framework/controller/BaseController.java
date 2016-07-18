package com.yn.framework.controller;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.yn.framework.activity.YNCommonActivity;
import com.yn.framework.data.JSON;
import com.yn.framework.exception.YNListInfoException;
import com.yn.framework.exception.YNOtherException;
import com.yn.framework.exception.YNVisitNetworkFailException;
import com.yn.framework.exception.YNVisitNetworkSuccessException;
import com.yn.framework.feedmission.BaseFeedMission;
import com.yn.framework.http.HttpExecute;
import com.yn.framework.http.HttpVisitCallBack;
import com.yn.framework.interfaceview.YNOperationRemindView;
import com.yn.framework.manager.CacheManager;
import com.yn.framework.http.TokenFailureManager;
import com.yn.framework.model.CacheModel;
import com.yn.framework.system.ContextManager;
import com.yn.framework.system.MethodUtil;
import com.yn.framework.system.StringUtil;
import com.yn.framework.view.BucketListAdapter;
import com.yn.framework.view.YNOperationListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youjiannuo on 15/7/6.
 * 这个主要是控制器，主要自动控制缓存数据，上拉刷新，下拉刷新加载更多，匹配ListView
 */
public class BaseController implements HttpVisitCallBack,
        BucketListAdapter.LoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener, YNCommonActivity.OnErrorReLoadListener {

    public YNOperationRemindView mYNOperationRemindView;
    public YNCommonActivity mActivity;

    //列表
    protected YNOperationListView mYNOperationListView = null;

    protected int mTagApi = 0;
    //是否需要显示进度条
    public boolean mShowProgress = true;
    //缓存只加一次
    private boolean mCache = false;

    //开始加载
    public static final int GET_LIST_USER_INFO_START = 1;
    //从新加载数据
    public static final int GET_LIST_USER_INFO_REFRESH = 2;
    //加载更多数据
    public static final int GET_LIST_USER_INFO_LOAD_MORE = 3;
    //开始发送
    protected int PAGE_START = 1;
    //当前加载的页数
    protected int mPage = PAGE_START;
    //反馈的布局
    private int mType;
    protected Object mMethodObj;

    protected Object mParams;

    private BaseFeedMission mBaseFeedMission;

    public BaseController(YNCommonActivity activity) {
        super();
        this.mActivity = activity;
        if (activity != null) {
            mYNOperationRemindView = activity;
        }
        if (mMethodObj == null) {
            mMethodObj = activity;
        }
        initData();
    }

    public BaseController(YNCommonActivity activity, YNOperationListView YNOperationListView) {
        this(activity);
        if (YNOperationListView != null) {
            mYNOperationListView = YNOperationListView;
            mYNOperationListView.setOnLoadMoreListener(this);
            mYNOperationListView.setRefreshListener(this);
            if (activity != null) {
                activity.setOnErrorReLoadListener(this);
            }
        }
    }

    public BaseController(YNOperationRemindView hfhFrameLayout, YNOperationListView YNOperationListView) {
        this((YNCommonActivity) hfhFrameLayout.getContext(), YNOperationListView);
        mYNOperationRemindView = hfhFrameLayout;
        if (YNOperationListView != null) {
            mYNOperationRemindView.setOnErrorReLoadListener(this);
        }
    }

    public BaseController(YNOperationRemindView hfhFrameLayout) {
        mActivity = (YNCommonActivity) hfhFrameLayout.getContext();
        mMethodObj = hfhFrameLayout;
        mYNOperationRemindView = hfhFrameLayout;
    }

    public BaseController(YNOperationRemindView hfhFrameLayout, YNOperationListView YNOperationListView, Object methodObj) {
        this(hfhFrameLayout, YNOperationListView);
        mMethodObj = methodObj;
    }

    public BaseController(YNOperationRemindView hfhFrameLayout, Object methodObj) {
        this(hfhFrameLayout, null, methodObj);
    }


    public BaseController(View v) {
        this(v, (YNOperationListView) null);
    }

    public BaseController(View v, YNOperationListView YNOperationListView) {
        this(((YNCommonActivity) v.getContext()), YNOperationListView);
        mMethodObj = v;
    }

    public BaseController(Object methodObj, YNCommonActivity activity) {
        this(methodObj, activity, null);
    }

    public BaseController(Object methodObj, YNCommonActivity activity, YNOperationListView YNOperationListView) {
        this(activity, YNOperationListView);
        mMethodObj = methodObj;
    }

    protected void initData() {

    }

    @Override
    public void visitNetworkStart(final BackTask backTask) {
        // TODO Auto-generated method stub
        //获取缓存数据
        if (backTask != null) {
            if (backTask.callInterface == GET_LIST_USER_INFO_LOAD_MORE ||
                    backTask.callInterface == GET_LIST_USER_INFO_REFRESH ||
                    backTask.callInterface == GET_LIST_USER_INFO_START) {
                mType = backTask.callInterface;
            }
        }
        if (!getCache(backTask)) {
            if (GET_LIST_USER_INFO_LOAD_MORE != mType && GET_LIST_USER_INFO_REFRESH != mType) {
                //开始访问接口
                try {
                    visitStart(backTask);
                } catch (Exception e) {
                    e.printStackTrace();
                    new YNOtherException(e).throwException();
                }

            }
        }
    }

    @Override
    public boolean visitAllNetworkSuccess(Object obj, BackTask backTask) {
        return false;
    }

    public void visitStart(final BackTask backTask) {
        new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mShowProgress &= backTask.isProgress;
                if (mYNOperationRemindView != null && mShowProgress)
                    mYNOperationRemindView.showProgressDialog();
            }
        }.sendEmptyMessage(0);

    }

    public void setShowProgress(boolean is) {
        mShowProgress = is;
    }

    @Override
    public void visitNetworkSuccess(Object obj, BackTask backTask) {
        // TODO Auto-generated method stub
        if (mYNOperationRemindView != null) {
            mYNOperationRemindView.closeProgressDialog();
        }
        if (mActivity != null) {
            mActivity.closeTopProgress();
        }
        openLoadMoreAndRefresh();
        if (mActivity != null && mActivity.isFinishing()) return;
        Object data;
        if (obj == null) {
            try {
                visitNetworkFail(null, backTask);
            } catch (Exception e) {
                e.printStackTrace();
                new YNVisitNetworkFailException(e).throwException();
            }
            return;
        }
        try {
            data = visitSuccess(obj, backTask);
        } catch (Exception e) {
            e.printStackTrace();
            new YNVisitNetworkSuccessException(e).throwException();
            return;
        }

        saveCache(obj.toString(), null, backTask);
        setData(data, backTask);
    }

    //从缓存里面获取数据，设置到图片上面去
    public void setCacheDataToView(CacheModel model) {
        Object object = null;
        if (model.getCacheString() != null && model.getCacheString().length() != 0) {
            try {
                object = visitSuccess(model.getCacheString(), model.getTask());
            } catch (Exception e) {
                e.printStackTrace();
                new YNVisitNetworkSuccessException(e).throwException();
                return;
            }
        } else if (model.getModel() != null) {
            try {
                object = visitSuccess(model.getModel(), model.getTask());
            } catch (Exception e) {
                e.printStackTrace();
                new YNVisitNetworkSuccessException(e).throwException();
                return;
            }
        }
        if (object != null) {
            if (mActivity != null) {
                mActivity.showTopProgress();
            }
            setData(object, model.getTask());
            if (mYNOperationRemindView != null) {
                mYNOperationRemindView.closeLoadDataNullView();
                mYNOperationRemindView.closeLoadFailDialog();
            }
        } else {
            if (mType != GET_LIST_USER_INFO_REFRESH) {
                visitStart(model.getTask());
            }
        }
    }

    private void setData(Object data, BackTask backTask) {
        int call = -1;
        if (backTask != null) {
            call = backTask.callInterface;
        }
        if ((data instanceof List && (call == GET_LIST_USER_INFO_START ||
                call == GET_LIST_USER_INFO_LOAD_MORE ||
                call == GET_LIST_USER_INFO_REFRESH)) ||
                (mYNOperationListView != null &&
                        (call == GET_LIST_USER_INFO_START ||
                                call == GET_LIST_USER_INFO_LOAD_MORE ||
                                call == GET_LIST_USER_INFO_REFRESH))) {
            List list = (List) data;
            if (data == null) {
                list = new ArrayList();
            }
            addInfo(list, backTask);
        } else if (data != null) {
            backMethodToActivityOrFragment(data, backTask);
        }
        setDataToViewEnd(backTask);
    }

    protected void setDataToViewEnd(BackTask backTask) {

    }

    public Object visitSuccess(Object object, BackTask backTask) {
        return null;
    }


    @Override
    public void visitNetworkProgress(int project) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visitNetworkFail(BackTask backTask) {
        // TODO Auto-generated method stub
        dealVisitNetworkFail(backTask);
    }

    //保存缓存数据
    private void saveCache(String data, Object baseDataModel, BackTask task) {
        if (task == null || task.getCacheKey() == null || task.getCacheKey().length() == 0) return;
        if (mType == GET_LIST_USER_INFO_LOAD_MORE) return;

        SaveCacheRun saveCacheRun = new SaveCacheRun();
        saveCacheRun.baseDataModel = baseDataModel;
        saveCacheRun.cacheString = data;
        saveCacheRun.task = task;
        new Thread(saveCacheRun).start();
    }

    //获取缓存数据
    public boolean getCache(BackTask task) {
        if (mCache) return false;

        //判断列表是都有数据
        if (mYNOperationListView != null && mYNOperationListView.getSize() > 0) {
            return true;
        }
        if (task == null || task.getCacheKey() == null || task.getCacheKey().length() == 0)
            return false;
        if (mType == GET_LIST_USER_INFO_LOAD_MORE || mType == GET_LIST_USER_INFO_REFRESH)
            return false;
        CacheModel model = CacheManager.CACHE_MANAGER.getData(task);
        if (model == null) {
            return false;
        } else {
            String cache = model.getCacheString();
            if (cache == null || cache.length() <= 2) {
                return false;
            }
            setCacheDataToView(model);
        }
        mCache = true;
        task.isGetCache = true;
        return true;
    }

    protected final void backMethodToActivityOrFragment(Object result, BackTask task) {
        boolean is = backMethod(result, task);
        if (!is) {
            if (task.method != null && task.method.length() != 0) {
                MethodUtil.invoke(mMethodObj, task.method, new Object[]{result});
            }
        }
    }

    protected boolean backMethod(Object result, BackTask task) {
        return false;
    }

    protected Object invokeMethod(BackTask task, Object... params) {
        return MethodUtil.invoke(mMethodObj, task.method, params);
    }

    protected Object invokeMethod(BackTask task, Class cl[], Object... params) {
        return MethodUtil.invoke(mMethodObj, task.method, params);
    }

    protected Object invokeMethod(String method, Object... params) {
        return MethodUtil.invoke(mMethodObj, method, params);
    }

    protected Object invokeMethod(String method, Class cl[], Object... params) {
        return MethodUtil.invoke(mMethodObj, method, params);
    }

    @Override
    public void visitNetworkFail(Object obj, BackTask backTask) {
        dealVisitNetworkFail(backTask);
        Object o = null;
        try {
            o = visitFail(obj, backTask);
        } catch (Exception e) {
            e.printStackTrace();
            new YNVisitNetworkFailException(e).throwException();
        }
        if (o != null) {
            backMethodToActivityOrFragment(o, backTask);
        }
    }

    public Object visitFail(Object obj, BackTask backTask) {
        return null;
    }

    protected void dealVisitNetworkFail(BackTask backTask) {
        if (mYNOperationRemindView != null) {
            mYNOperationRemindView.closeProgressDialog();
        }
        if (mActivity != null) {
            mActivity.closeTopProgress();
        }
        openLoadMoreAndRefresh();
        if (mYNOperationListView != null) {
            if (mYNOperationListView.getSize() == 0) {
                mYNOperationListView.closeLoadMore();
            }
            if (backTask.callInterface == GET_LIST_USER_INFO_LOAD_MORE) {
                mPage--;
            }
            if (backTask.callInterface == GET_LIST_USER_INFO_START && mYNOperationListView.getSize() == 0) {
                //加载失败
                if (mYNOperationRemindView != null) {
                    mYNOperationRemindView.closeLoadDataNullView();
                    mYNOperationRemindView.showLoadFailDialog();
                }
            }
        }

    }

    public void reLoadData() {
        getListInfo(PAGE_START, mYNOperationListView.getPageNumber(), GET_LIST_USER_INFO_START);
    }

    @Override
    public void visitNetworkCancel(BackTask backTask) {
        // TODO Auto-generated method stub
        if (mYNOperationRemindView != null) {
            mYNOperationRemindView.closeProgressDialog();
        }
    }

    @Override
    public void visitTokenFailure(HttpExecute.NetworkTask task) {
        TokenFailureManager.TokenParams params = new TokenFailureManager.TokenParams();
        params.activity = mActivity;
        params.task = task;
        params.baseController = this;
        params.YNOperationRemindView = mYNOperationRemindView;
        if (TokenFailureManager.TOKEN_FAILURE_MANAGER.setTokenParams(params)) {
            if (mYNOperationRemindView != null && mYNOperationListView != null && mYNOperationListView.getSize() == 0) {
                mYNOperationRemindView.showLoadDataNullView();
            }
        }
    }

    public void getStartListData(Object obj) {
        mParams = obj;
        getListInfo(PAGE_START, mYNOperationListView.getPageNumber(), GET_LIST_USER_INFO_START);
    }

    /**
     * 在访问获取列表数据的时候，
     *
     * @param page
     * @param pageNumber
     * @param call
     */
    protected void getListInfo(int page, int pageNumber, int call) {

    }

    protected void getStartGetList() {
        try {
            getListInfo(PAGE_START, mYNOperationListView.getPageNumber(), GET_LIST_USER_INFO_START);
        } catch (Exception e) {
            new YNListInfoException(e).throwException();
        }
    }

    private void openLoadMoreAndRefresh() {
        if (mYNOperationListView != null) {
            mYNOperationListView.closeRefresh();
            mYNOperationListView.enableRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (mYNOperationListView == null) return;
        if (mYNOperationListView.getSize() == 0) return;
        //不可以进行下拉刷新
        mYNOperationListView.disableRefresh();
        //加载更多
        loadMore();
    }

    @Override
    public void onRefresh() {
        //关闭加载更多
        if (mYNOperationListView != null)
            mYNOperationListView.closeLoadMore();
        //刷新
        refresh();
    }

    public boolean loadMore() {
        mType = GET_LIST_USER_INFO_LOAD_MORE;
        try {
            getListInfo(++mPage, mYNOperationListView.getPageNumber(), GET_LIST_USER_INFO_LOAD_MORE);
        } catch (Exception e) {
            mPage--;
            new YNListInfoException(e).throwException();
        }

        return false;
    }

    public boolean refresh() {
        mType = GET_LIST_USER_INFO_REFRESH;
        mPage = PAGE_START;
        try {
            getListInfo(mPage, mYNOperationListView.getPageNumber(), GET_LIST_USER_INFO_REFRESH);
        } catch (Exception e) {
            new YNListInfoException(e).throwException();
        }

        return false;
    }

    protected void addInfo(List list, BackTask backTask) {
        if (mYNOperationListView != null && backTask != null) {

            if (backTask.callInterface == GET_LIST_USER_INFO_START) {
                mYNOperationListView.setAdapter(list);
            } else if (backTask.callInterface == GET_LIST_USER_INFO_LOAD_MORE) {
                if (list.size() != 0) {
                    mYNOperationListView.addData(list);
                }
            } else if (backTask.callInterface == GET_LIST_USER_INFO_REFRESH) {
                mYNOperationListView.setAdapter(list);
            }

            if ((list.size() == 0 && (backTask.callInterface == GET_LIST_USER_INFO_REFRESH || backTask.callInterface == GET_LIST_USER_INFO_START))) {
                if (mYNOperationRemindView != null) {
                    mYNOperationRemindView.showLoadDataNullView();
                }
            } else if ((backTask.callInterface == GET_LIST_USER_INFO_REFRESH || backTask.callInterface == GET_LIST_USER_INFO_START)) {
                if (mYNOperationRemindView != null) {
                    mYNOperationRemindView.closeLoadDataNullView();
                }
            }
            if (list.size() < mYNOperationListView.getPageNumber() || mYNOperationListView.getPageNumber() <= 0) {
                mYNOperationListView.closeLoadMore();
            } else {
                mYNOperationListView.openLoadMore();
            }

        }
    }

    @Override
    public void onErrorReLoad() {
        reLoadData();
    }


    class SaveCacheRun implements Runnable {
        public String cacheString;
        public Object baseDataModel;
        public BackTask task;

        @Override
        public void run() {
            CacheModel model = new CacheModel();
            model.setCacheString(cacheString);
            model.setModel(baseDataModel);
            model.setTask(task);
            CacheManager.CACHE_MANAGER.saveData(model);
        }
    }

    public String getCate(String result) {
        try {
            JSON json = new JSON(result);
            return json.getString("cate");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getList(String result) {
        String s;
        JSON json = new JSON(result);
        s = json.getString("list");
        if (!StringUtil.isEmpty(s)) {
            return s;
        }
        return json.getString("stockList");
    }


    protected boolean isGetList(BackTask backTask) {
        return backTask.callInterface == GET_LIST_USER_INFO_START ||
                backTask.callInterface == GET_LIST_USER_INFO_REFRESH ||
                backTask.callInterface == GET_LIST_USER_INFO_LOAD_MORE;
    }

    protected boolean getCall(int array, BackTask task) {
        String arrays[] = ContextManager.getArrayString(array);
        int result = -1;
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].toLowerCase().contains("call")) {
                int index = arrays[i].indexOf(":");
                if (index == -1) {
                    throw new NullPointerException("call must is call:1");
                }
                result = Integer.parseInt(arrays[i].substring(index + 1, arrays[i].length()));
                break;
            }
        }
        return task.callInterface == result;
    }

    public void sendMessage(HttpExecute.NetworkTask task) {
        if (mBaseFeedMission == null) mBaseFeedMission = new BaseFeedMission(this);
        mBaseFeedMission.sendMessage(task);
    }

    public void sendMessage(String backMethod, int param, int call, String... values) {
        if (mBaseFeedMission == null) mBaseFeedMission = new BaseFeedMission(this);
        mBaseFeedMission.sendMessage(backMethod, param, call, values);
    }


    public void sendMessage(int param, int call, String... values) {
        sendMessage("", param, call, values);
    }

    public void sendMessage(int param, String... values) {
        sendMessage("", param, -1, values);
    }

    public void sendMessage(String backMethod, int param, String... values) {
        sendMessage(backMethod, param, -1, values);
    }

}