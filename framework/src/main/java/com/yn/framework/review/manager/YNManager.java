package com.yn.framework.review.manager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yn.framework.data.JSON;
import com.yn.framework.review.OnYNOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youjiannuo on 16/3/16.
 */
public class YNManager {


    private ViewGroup mViewGroup;

    private JSON mJson;

    public YNManager(ViewGroup viewGroup, JSON json) {
        mViewGroup = viewGroup;
        mJson = json;
    }

    public void setData(int position) {
        int N = mJson.size() > mViewGroup.getChildCount() ? mViewGroup.getChildCount() : mJson.size();
        for (int i = 0; i < N; i++) {
            View view = mViewGroup.getChildAt(i);
            if (view instanceof OnYNOperation) {
                OnYNOperation operation = (OnYNOperation) view;
                operation.setPosition(position);
                operation.setData(new JSON(mJson.getRowString(i)));
            }
        }
    }

    public void setStartData(int position) {
        List<Integer> res = new ArrayList<>();
        getResourceId(mViewGroup, res);
        for (int i = 0; i < res.size(); i++) {
            OnYNOperation onYNOperation = ((OnYNOperation) mViewGroup.findViewById(res.get(i)));
            onYNOperation.setPosition(position);
            onYNOperation.setData(mJson);
        }
    }

    /**
     * 获取需要设置Data的Id
     *
     * @param viewGroup
     * @param res
     */
    public static void getResourceId(ViewGroup viewGroup, List<Integer> res) {
        addId(viewGroup, res);
        if (viewGroup instanceof ListView) {
            return;
        }
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                getResourceId((ViewGroup) view, res);
            }
            addId(view, res);
        }
    }

    private static void addId(View view, List<Integer> res) {
        if (view instanceof OnYNOperation) {
            int type = ((OnYNOperation) view).getType();
            if (type == 1 || type == 3) {
                if (view.getId() != -1)
                    res.add(view.getId());
            }
        }
    }

}
