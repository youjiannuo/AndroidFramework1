package com.yn.framework.review;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.yn.framework.R;
import com.yn.framework.activity.BaseFragment;
import com.yn.framework.activity.YNCommonActivity;
import com.yn.framework.data.JSON;
import com.yn.framework.data.MyGson;
import com.yn.framework.review.manager.OnBackListener;
import com.yn.framework.review.manager.YNController;
import com.yn.framework.review.manager.YNManager;
import com.yn.framework.system.StringUtil;
import com.yn.framework.system.SystemUtil;
import com.yn.framework.view.YJNListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youjiannuo on 16/3/16.
 */
public class YNListView extends YJNListView<String> implements YNHttpOperation, OnYNOperation, YNController.OnHttpDataListener {

    int mValueLayout;
    int mTitleValue;
    int mHeadValue;
    int mFooterValue;
    protected List<Integer> mRes = null;
    int mLineColors = 0xFFdedede;
    float mLineHeight = 1;
    boolean mLineVisible = true;
    int mBackground = R.drawable.hfh_border_gray_bg_white_click;
    private int mCol = 1;
    private OnBackListener mOnBackListener;
    int mListHttp = 0;
    int mLoadMore = 10;
    boolean mShowAllView = false;
    boolean mShowLoadOver = true;
    int mItemHeight = WRAP_CONTENT;
    private String mDataName = "";
    private String mClickKeys[];
    private int mShowListNum = -1; //显示条数
    //head佈局
    protected View mHeadView;
    protected View mFootView;

    //获取一次请求
    private boolean mFirstHttp = true;
    private YNController mController = null;
    private boolean mShowError = false;
    private OnBindListener mBindListener;
    private OnHttpListener mOnHttpListener;

    public YNListView(Context context) {
        super(context);
    }

    public YNListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.YNView);
        mValueLayout = array.getResourceId(R.styleable.YNView_layout_value, 0);
        mTitleValue = array.getResourceId(R.styleable.YNView_layout_title, 0);
        mHeadValue = array.getResourceId(R.styleable.YNView_head_value, 0);
        mFooterValue = array.getResourceId(R.styleable.YNView_foot_value, 0);
        mBackground = array.getResourceId(R.styleable.YNView_onItemBackground, mBackground);
        mCol = array.getInt(R.styleable.YNView_list_col, 1);
        mLoadMore = array.getInt(R.styleable.YNView_list_load_more, 10);
        mListHttp = array.getResourceId(R.styleable.YNView_list_http, 0);
        mLineVisible = array.getBoolean(R.styleable.YNView_line_visible, true);
        mShowAllView = array.getBoolean(R.styleable.YNView_list_show_all_view, false);
        mItemHeight = (int) array.getDimension(R.styleable.YNView_list_item_height, WRAP_CONTENT);
        mLineHeight = array.getDimension(R.styleable.YNView_onItemLineHeight, 1);
        mLineColors = array.getColor(R.styleable.YNView_onItemLineColor, mLineColors);
        mDataName = array.getString(R.styleable.YNView_set_data_name);
        mFirstHttp = array.getBoolean(R.styleable.YNView_http_first, true);
        mShowLoadOver = array.getBoolean(R.styleable.YNView_list_show_load_over, true);
        mShowListNum = array.getInt(R.styleable.YNView_list_show_num, -1);
        String key = array.getString(R.styleable.YNView_onClickKey);
        if (!StringUtil.isEmpty(key)) {
            mClickKeys = key.split(",");
        }
        array.recycle();
        if (mHeadValue != 0) {
            mHeadView = LayoutInflater.from(getContext()).inflate(mHeadValue, null);
            addHeaderView(mHeadView);
        }
        if (mFooterValue != 0) {
            addFooterView(LayoutInflater.from(getContext()).inflate(mFooterValue, null));
        }
        if (mShowLoadOver && isLoadMore()) {
            mFootView = LayoutInflater.from(getContext()).inflate(R.layout.y_view_foot_load_over, null);
            addFooterView(mFootView);
            mFootView.setVisibility(View.GONE);
        }
    }

    public YNListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private ViewGroup getFragmentLayout() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        if (mLineVisible) {
            View view = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, (int) mLineHeight);
            view.setLayoutParams(params);
            view.setBackgroundColor(mLineColors);
            layout.addView(view);
        }
        return layout;
    }

    public void setLineVisible(boolean is) {
        mLineVisible = is;
    }

    @Override
    public void closeLoadMore() {
        super.closeLoadMore();
        if (mShowLoadOver && isLoadMore() && getList().size() > 10) {
            if (mFootView != null) {
                mFootView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void openLoadMore() {
        super.openLoadMore();
        if (mFootView != null) {
            mFootView.setVisibility(View.GONE);
        }
    }


    @Override
    protected boolean isLoadMore() {
        return mLoadMore > 0;
    }

    @Override
    public View createView(int position, String data) {
        SystemUtil.printlnInfo("");
        View valueView = LayoutInflater.from(getContext()).inflate(mValueLayout, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, mItemHeight);
        valueView.setLayoutParams(params);
        ViewGroup viewGroup = getFragmentLayout();
        viewGroup.addView(valueView, 0);
        if (mTitleValue != 0) {
            View titleView = LayoutInflater.from(getContext()).inflate(mTitleValue, null);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            titleView.setLayoutParams(titleParams);
            viewGroup.addView(titleView, 1);
        }
        viewGroup.setBackgroundResource(mBackground);
        return viewGroup;
    }


    public void setBindListener(OnBindListener l) {
        mBindListener = l;
    }

    @Override
    public void setViewData(View view, int position, String data) {
        OnYNOperation operations[];
        if (view.getTag() == null) {
            if (mRes == null) {
                mRes = new ArrayList<>();
                YNManager.getResourceId((ViewGroup) view, mRes);
            }
            operations = new OnYNOperation[mRes.size()];
            for (int i = 0; i < mRes.size(); i++) {
                operations[i] = (OnYNOperation) view.findViewById(mRes.get(i));
            }
            view.setTag(operations);
        } else {
            operations = (OnYNOperation[]) view.getTag();
        }
        if (mTitleValue != 0) {
            if (position == 0) {
                view.setClickable(false);
                ((ViewGroup) view).getChildAt(1).setVisibility(View.VISIBLE);
                ((ViewGroup) view).getChildAt(0).setVisibility(View.GONE);
                return;
            } else {
                ((ViewGroup) view).getChildAt(1).setVisibility(View.GONE);
                ((ViewGroup) view).getChildAt(0).setVisibility(View.VISIBLE);
                view.setClickable(true);
            }
        } else view.setClickable(true);
        setClick(view, data);
        for (int i = 0; i < operations.length; i++) {
            OnYNOperation operation = operations[i];
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

    public void setHttpId(int httpId) {
        mListHttp = httpId;
    }

    private void setClick(View view, final String data) {
        if (view != null) {
            if (view instanceof YNLinearLayout) {
                if (((YNLinearLayout) view).getOnClick() != 0) {
                    return;
                }
            }
            view.setOnClickListener(new View.OnClickListener() {
                String s = data;

                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(s);
                    }
                }
            });
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
        if (!StringUtil.isEmpty(mDataName)) {
            List<String> list = setAdapter(json.getString(mDataName));

            if (getList().size() == 0 && list.size() == 0) {
                ((YNCommonActivity) getContext()).showLoadDataNullView();
            }
        }
    }

    @Override
    public int getType() {
        return 1;
    }

    public void setOnBackListener(OnBackListener l) {
        mOnBackListener = l;
    }

    public void changeDataAndNotifyDataSetChanged(String key, String value, int index) {
        changeData(key, value, index);
        notifyDataSetChanged();
    }

    public void changeData(String key, String value, int index) {
        List<String> result = getList();
        if (index >= 0 && index < result.size()) {
            JSON json = new JSON(result.get(index));
            result.remove(index);
            String s = json.changeData(key, value).toString();
            if (!StringUtil.isEmpty(s)) {
                result.add(index, s);
            }
        }
    }


    @Override
    public void setAdapter(List<String> data) {
        super.setAdapter(data);
        if (data.size() == 0 && mFootView != null) {
            mFootView.setVisibility(GONE);
        }
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
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getColumn() {
        return mCol;
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


    public YNController startHttp(BaseFragment baseFragment, String... values) {
        if (!(getAdapter() != null && mFirstHttp)) {
            if (mController == null) {
                if (baseFragment == null) {
                    mController = new YNController((YNCommonActivity) getContext(), this);
                } else {
                    mController = new YNController(baseFragment, this);
                }
            }
            mController.setOnHttpDataListener(this);
            mController.showError(mShowError);
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

    @Override
    public int getPageNumber() {
        if (mLoadMore <= 0) return Integer.MAX_VALUE;
        return mLoadMore;
    }


    public List<String> setAdapter(String data) {
        List<String> list = new MyGson().fromJson(data, new TypeToken<List<String>>() {
        }.getType());
        setAdapter(list);

        return list;
    }


    @Override
    public boolean isShowAllView() {
        return mShowAllView;
    }

    @Override
    public void onHttpData(Object data) {
        if (mOnHttpListener != null) {
            mOnHttpListener.onHttpSuccess(data, this);
        }
    }

    public interface OnBindListener {
        void onBindView(View view, int position, String data);
    }

}
