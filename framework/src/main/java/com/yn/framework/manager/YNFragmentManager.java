package com.yn.framework.manager;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by youjiannuo on 16/2/16.
 */
public class YNFragmentManager {

    private FragmentActivity mActivity;
    private Map<Integer, Fragment> mFragments = new HashMap<>();
    private FragmentParams mParams;
    private int mSelectIndex = 0;


    public YNFragmentManager(FragmentActivity activity) {
        mActivity = activity;
    }

    public void setFragmentParams(FragmentParams params) {
        this.mParams = params;
        addFragment(0);
    }

    private Fragment addFragment(int index) {
        if (mActivity == null || mParams == null) return null;
        if (mParams.cls == null) return null;

        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        Fragment fragment = newFragment(mParams.cls[index]);
        if (fragment == null) return null;
        if (mParams.mOnFragmentListener != null) {
            mParams.mOnFragmentListener.setFragmentData(fragment, index);
        }
        mFragments.put(index, fragment);
        int id = mParams.contentViewIds == null ? mParams.contentViewId : mParams.contentViewIds[index];
        transaction.add(id, fragment);
        dealFragment(transaction, index);
        transaction.commitAllowingStateLoss();
        return fragment;
    }

    private void dealFragment(FragmentTransaction transaction, int index) {
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
        return switchFragment(mSelectIndex - 1);
    }

    public Fragment switchNextFragment() {
        return switchFragment(mSelectIndex + 1);
    }

    public Fragment switchFragment(int index) {
        if (index < 0) return null;
        mSelectIndex = index;
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();

        if (mParams.exitAnimation != 0 && mParams.enterAnimation != 0) {
            transaction.setCustomAnimations(mParams.enterAnimation, mParams.exitAnimation);
        } else {
            transaction.setTransition(mParams.FragmentTransactionAnimation);
        }

        Fragment fragment = mFragments.get(index);
        if (fragment != null) {
            dealFragment(transaction, index);
            transaction.commit();
        } else {
            fragment = addFragment(index);
        }

        return fragment;
    }

    public Fragment getBaseFragment(int index) {
        if (mFragments != null) {
            return mFragments.get(index);
        }
        return null;
    }

    public int getSelectIndex() {
        return mSelectIndex;
    }

    public void clear(){
        if (mActivity != null && mActivity.getSupportFragmentManager() != null && mActivity.getSupportFragmentManager().beginTransaction() != null) {
            FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
            for (Fragment fragment : mFragments.values()){
                transaction.remove(fragment);
            }
            transaction.commit();
        }
    }

    public interface OnFragmentListener {
        void setFragmentData(Fragment fragment, int index);
    }

}