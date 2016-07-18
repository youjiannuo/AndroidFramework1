package com.yn.framework.manager;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.yn.framework.activity.YNCommonActivity;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by youjiannuo on 16/2/18.
 */
public class YNMyFragmentManager {

    private FragmentActivity mActivity;
    private Map<Integer, Fragment> mFragments = new HashMap<>();
    private FragmentParams mParams;
    private int mShowIndex = 0;

    public YNMyFragmentManager(FragmentActivity activity) {
        mActivity = activity;
    }

    public void setFragmentParams(FragmentParams params) {
        this.mParams = params;
        for (int i = 1; i < params.cls.length; i++) {
            addCreateFragment(mParams.cls[i], i);
        }
        addFragment(0);
    }

    private Fragment addFragment(int index) {
        if (mActivity == null || mParams == null) return null;
        if (mParams.cls == null) return null;
        return addFragment(mParams.cls[index], index);
    }


    private Fragment addFragment(Class cls, int index) {
        Fragment fragment = addCreateFragment(cls, index);
        ((YNCommonActivity) mActivity).getYNFragmentTransaction().commitAllowingStateLoss();
        return fragment;
    }

    private Fragment addCreateFragment(Class cls, int index) {
        YNFragmentTransaction transaction = ((YNCommonActivity) mActivity).getYNFragmentTransaction();
        Fragment fragment = newFragment(cls);
        if (fragment == null) return null;
        if (mParams.mOnFragmentListener != null) {
            mParams.mOnFragmentListener.setFragmentData(fragment, index);
        }
        mFragments.put(index, fragment);
        int id = mParams.contentViewIds == null ? mParams.contentViewId : mParams.contentViewIds[index];
        transaction.add(id, fragment);
        dealFragment(transaction, index);
        return fragment;
    }


    /**
     * 替代fragment
     *
     * @param cls          需要替代的Fragment
     * @param replaceIndex 替代的位置
     */
    public void replaceFragment(Class cls, int replaceIndex) {
        YNFragmentTransaction transaction = ((YNCommonActivity) mActivity).getYNFragmentTransaction();
        if (mFragments.get(replaceIndex) == null) {
            mParams.cls[replaceIndex] = cls;
            return;
        }
        transaction.remove(mFragments.get(replaceIndex));
        addFragment(cls, replaceIndex);
        switchFragment(getSelectIndex());
    }

    private void dealFragment(YNFragmentTransaction transaction, int index) {
        for (Map.Entry<Integer, Fragment> entry : mFragments.entrySet()) {
            Fragment f = entry.getValue();
            if (entry.getKey() == index) {
                transaction.show(f);
            } else {
                transaction.hide(f);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Fragment newFragment(Class cls) {
        try {
            return (Fragment) cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Fragment switchBackFragment() {
        return switchFragment(mShowIndex - 1);
    }

    public Fragment switchNextFragment() {
        return switchFragment(mShowIndex + 1);
    }


    public Fragment switchFragment(int index) {
        if (index < 0) return null;
        YNFragmentTransaction transaction = ((YNCommonActivity) mActivity).getYNFragmentTransaction();
        Fragment fragment = mFragments.get(index);
        if (fragment != null) {
            dealFragment(transaction, index);
            transaction.commit();
        } else {
            fragment = addFragment(index);
        }
        mShowIndex = index;
        return fragment;
    }

    public Fragment getBaseFragment(int index) {
        if (mFragments != null) {
            return mFragments.get(index);
        }
        return null;
    }

    public int getSelectIndex() {
        return mShowIndex;
    }

    public Fragment getShowFragment() {
        return getBaseFragment(getSelectIndex());
    }

}
