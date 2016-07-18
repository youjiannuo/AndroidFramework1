package com.yn.framework.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.FragmentManager;

import com.yn.framework.activity.BaseFragment;

/**
 * Created by youjiannuo on 16/2/23.
 */
public abstract class YNFragmentStateClassPageAdapter<T> extends YNFragmentStatePageAdapter<T> {

    private Class[] mClss = null;
    private Class mCls = null;

    public YNFragmentStateClassPageAdapter(FragmentManager fm) {
        super(fm);
        Object object = getFragmentClass();
        if (object instanceof Class) {
            mCls = (Class) object;
        } else {
            mClss = (Class[]) object;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public BaseFragment newFragment(int i) {
        if (mCls != null) {
            try {
                return (BaseFragment) mCls.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        try {
            return (BaseFragment) mClss[i].newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        if (mClss != null) {
            return mClss.length;
        }
        return 0;
    }

    public abstract Object getFragmentClass();

}
