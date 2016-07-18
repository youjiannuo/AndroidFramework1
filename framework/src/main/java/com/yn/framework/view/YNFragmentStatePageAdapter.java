package com.yn.framework.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yn.framework.activity.BaseFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by youjiannuo on 16/1/24.
 */
public abstract class YNFragmentStatePageAdapter<T> extends FragmentStatePagerAdapter {

    Map<Integer, BaseFragment> mBaseFragments;
    private int mApiCount = 0;


    public YNFragmentStatePageAdapter(FragmentManager fm) {
        super(fm);
        mBaseFragments = new HashMap<>();
    }

    public BaseFragment getBaseFragment(int index) {
        if (index < getCount()) {
            return mBaseFragments.get(index);
        }
        return null;
    }

    @Override
    public Fragment getItem(int i) {
        BaseFragment fragment = mBaseFragments.get(i);
        if (fragment != null && !fragment.isAdded()) {
            return fragment;
        }
        fragment = (BaseFragment) newFragment(i);
        setData((T) fragment, i);
        mBaseFragments.put(i, fragment);
        return fragment;
    }



    public abstract void setData(T fragment, int index);

    public abstract Object newFragment(int i);

    public void notifyDataSetChangedApi() {
        if (mBaseFragments == null) {
            return;
        }
        mApiCount = 0;
        for (Map.Entry<Integer, BaseFragment> entry : mBaseFragments.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().onNetwork();
            }
        }
    }
}
