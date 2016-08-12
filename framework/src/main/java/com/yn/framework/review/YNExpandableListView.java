package com.yn.framework.review;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.yn.framework.R;
import com.yn.framework.activity.BaseFragment;
import com.yn.framework.activity.YNCommonActivity;
import com.yn.framework.data.JSON;
import com.yn.framework.review.manager.OnBackListener;
import com.yn.framework.review.manager.YNController;
import com.yn.framework.review.manager.YNManager;
import com.yn.framework.system.StringUtil;
import com.yn.framework.view.YJNExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youjiannuo on 16/7/4.
 */
public class YNExpandableListView extends YJNExpandableListView<String, String>
        implements YNHttpOperation, YNController.OnHttpDataListener, OnYNOperation {

    //子节点
    private int mChildLayout = -1;
    //父类节点
    private int mGroupLayout = -1;
    //子节点的Name
    private String mChildName = "";
    //父亲节点
    private String mDataName = "";
    //发送httpId
    private int mListHttp = -1;

    //获取一次请求
    private boolean mFirstHttp = true;
    private YNController mController = null;
    private boolean mShowError = false;
    private YNListView.OnBindListener mBindListener;
    private OnHttpListener mOnHttpListener;
    private OnBackListener mOnBackListener;
    private String mClickKeys[];
    private int mLoadMore = 10;
    //最多显示的条数
    private int mShowListNum = -1;
    //groupId
    private List<Integer> mGroupId = new ArrayList<>();
    //childId
    private List<Integer> mChildId = new ArrayList<>();

    public YNExpandableListView(Context context) {
        super(context);
    }

    public YNExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.YJNExpandableListView);
        mGroupLayout = typedArray.getResourceId(R.styleable.YJNExpandableListView_group_layout, -1);
        mChildLayout = typedArray.getResourceId(R.styleable.YJNExpandableListView_child_layout, -1);
        mChildName = typedArray.getString(R.styleable.YJNExpandableListView_child_name);
        typedArray.recycle();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.YNView);
        mDataName = array.getString(R.styleable.YNView_set_data_name);
        mListHttp = array.getResourceId(R.styleable.YNView_list_http, -1);
        mLoadMore = array.getInt(R.styleable.YNView_list_load_more, 10);
        String key = array.getString(R.styleable.YNView_onClickKey);
        if (!StringUtil.isEmpty(key)) {
            mClickKeys = key.split(",");
        }
        array.recycle();
    }

    @Override
    public View newGroupView(String groupData, int groupPosition, boolean isExpanded, View convertView, ViewGroup parent, boolean isChild) {
        return getView(mGroupLayout);
    }

    @Override
    public View setGroupViewData(String groupData, int groupPosition, boolean isExpanded, View convertView, ViewGroup parent, boolean isChild) {
        setDataName(convertView, groupData, mGroupId, groupPosition);
        setGroupData(groupData, groupPosition, isExpanded, convertView, parent, isChild);
        return convertView;
    }

    protected void setGroupData(String groupData, int groupPosition, boolean isExpanded, View convertView, ViewGroup parent, boolean isChild) {

    }


    @Override
    public View newChildView(String childData, int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return getView(mChildLayout);
    }

    @Override
    public View setChildViewData(String childData, int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        setDataName(convertView, childData, mChildId, groupPosition);
        return convertView;
    }

    private void setDataName(View view, String data, List<Integer> viewId, int position) {
        if (viewId == null || viewId.size() == 0) {
            YNManager.getResourceId((ViewGroup) view, viewId);
        }
        if (viewId == null) return;
        for (int i = 0; i < viewId.size(); i++) {
            OnYNOperation operation = (OnYNOperation) view.findViewById(viewId.get(i));
            operation.setPosition(position);
            operation.setData(new JSON(data));
            if (operation.getOnClick() != 0 && mOnBackListener != null) {
                operation.setOnBackListener(mOnBackListener);
            }
        }
        if (mBindListener != null) {
            mBindListener.onBindView(view, position, data);
        }

    }


    @Override
    public void setOnHttpListener(OnHttpListener l) {
        mOnHttpListener = l;
    }

    @Override
    public void showErrorView() {
        mShowError = true;
    }

    public YNController startHttp(String... values) {
        return startHttp((BaseFragment) null, values);
    }

    @Override
    public int getPageNumber() {
        return mLoadMore;
    }

    public YNController startHttp(BaseFragment baseFragment, String... values) {
        if (!(getAdapter() != null && mFirstHttp)) {
            if (mController == null) {
                if (baseFragment == null) {
                    mController = new YNController((YNCommonActivity) getContext(), this);
                } else {
                    mController = new YNController(baseFragment, this);
                }
            }
            mController.setDataName(mDataName);
            mController.setChildName(mChildName);
            mController.setOnHttpDataListener(this);
            mController.showError(mShowError);
            mController.setExpandListView(true);
            mController.setDataName(mDataName);
            mController.setShowListNum(mShowListNum);
            mController.getList(mListHttp, mLoadMore, getValues(values));
        }
        return mController;
    }


    public void startHttp(YNController controller, String... values) {
        mController = controller;
        controller.setOnHttpDataListener(this);
        controller.showError(mShowError);
        controller.setDataName(mDataName);
        controller.setShowListNum(mShowListNum);
        controller.getList(mListHttp, mLoadMore, getValues(values));
    }

    @Override
    public void onHttpData(Object data) {
        if (mOnHttpListener != null) {
            mOnHttpListener.onHttpSuccess(data, this);
        }
    }

    @Override
    public void setData(Object obj) {
        JSON json;
        if (obj instanceof JSON) {
            json = (JSON) obj;
        } else {
            json = new JSON(obj.toString());
        }
//        if (!StringUtil.isEmpty(mDataName)) {
//            List<String> list = setAdapter(json.getString(mDataName));
//
//            if (getList().size() == 0 && list.size() == 0) {
//                ((YNCommonActivity) getContext()).showLoadDataNullView();
//            }
//        }
    }

    @Override
    public int getType() {
        return 1;
    }

    public void setOnBackListener(OnBackListener l) {
        mOnBackListener = l;
    }

    @Override
    public void setPosition(int index) {

    }

    @Override
    public int getOnClick() {
        return 0;
    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public OnYNOperation[] getYNOperation() {
        return new OnYNOperation[0];
    }

    @Override
    public void setYNOperation(OnYNOperation[] operations) {

    }

    @Override
    public YNController getYNController() {
        return mController;
    }

    public YNController getController() {
        return mController;
    }

    private String[] getValues(String values[]) {
        if (mClickKeys == null || mClickKeys.length == 0) {
            return values;
        }
        String data[] = new String[mClickKeys.length + values.length];
        Intent intent = ((Activity) getContext()).getIntent();
        for (int i = 0; i < mClickKeys.length; i++) {
            data[i] = intent.getStringExtra(mClickKeys[i]);
        }
        for (int i = mClickKeys.length; i < data.length; i++) {
            data[i] = values[i];
        }
        return data;
    }


}

