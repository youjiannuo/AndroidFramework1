package com.yn.framework.manager;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yn.framework.activity.YNCommonActivity;
import com.yn.framework.system.MethodUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youjiannuo on 16/2/18.
 */
public class YNFragmentTransaction extends FragmentTransaction {

    private YNCommonActivity mActivity;

    private List<Fragment> mFragments = new ArrayList<>();

    private List<Fragment> mHaveAddView = new ArrayList<>();
    //选中的Fragment
    private List<Fragment> mShowFragment = new ArrayList<>();
    //隐藏
    private List<Fragment> mHideFragment = new ArrayList<>();

    public YNFragmentTransaction(YNCommonActivity activity) {
        mActivity = activity;
    }

    public void add(int viewId, Class cls) {
        Fragment fragment = newFragment(cls);
        add(viewId, fragment);
    }

    @Override
    public FragmentTransaction add(Fragment fragment, String tag) {
        return null;
    }


    public FragmentTransaction add(int viewId, Fragment fragment) {
        if (fragment == null) return this;

        MethodUtil.setParamValue(fragment, "mActivity", mActivity, Fragment.class);
        ViewGroup viewGroup = (ViewGroup) mActivity.findViewById(viewId);
        fragment.onAttach(mActivity);
        fragment.onCreate(null);
        View view = fragment.onCreateView(LayoutInflater.from(mActivity), null, null);
        if (view != null) {
            MethodUtil.setParamValue(fragment, "mView", view, Fragment.class);
            viewGroup.addView(view);
            boolean is = true;
            for (int i = 0; i < mFragments.size(); i++) {
                if (mFragments.get(i) == fragment) {
                    is = false;
                    break;
                }
            }
            if (is) {
                mFragments.add(fragment);
            }
            mHaveAddView.add(fragment);
        }
        return this;
    }

    @Override
    public FragmentTransaction add(int containerViewId, Fragment fragment, String tag) {
        return null;
    }

    @Override
    public FragmentTransaction replace(int containerViewId, Fragment fragment) {
        return null;
    }

    @Override
    public FragmentTransaction replace(int containerViewId, Fragment fragment, String tag) {
        return null;
    }

    @Override
    public FragmentTransaction remove(Fragment fragment) {
        mFragments.remove(fragment);
        if (fragment != null && fragment.getView() != null) {
            ViewGroup viewGroup = (ViewGroup) fragment.getView().getParent();
            if (viewGroup != null) {
                viewGroup.removeView(fragment.getView());
                MethodUtil.setParamValue(fragment, "mRemoving", true, Fragment.class);
            }
        }
        return this;
    }

    public FragmentTransaction show(Fragment fragment) {
        if (fragment != null && fragment.getView() != null) {
            mShowFragment.add(fragment);
            MethodUtil.setParamValue(fragment, "mHidden", false, Fragment.class);
            MethodUtil.setParamValue(fragment, "mInLayout", true, Fragment.class);
            fragment.getView().setVisibility(View.VISIBLE);
        }
        return this;
    }

    @Override
    public FragmentTransaction detach(Fragment fragment) {
        return null;
    }

    @Override
    public FragmentTransaction attach(Fragment fragment) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public FragmentTransaction setCustomAnimations(int enter, int exit) {
        return null;
    }

    @Override
    public FragmentTransaction setCustomAnimations(int enter, int exit, int popEnter, int popExit) {
        return null;
    }

    @Override
    public FragmentTransaction addSharedElement(View sharedElement, String name) {
        return null;
    }

    @Override
    public FragmentTransaction setTransition(int transit) {
        return null;
    }

    @Override
    public FragmentTransaction setTransitionStyle(int styleRes) {
        return null;
    }

    @Override
    public FragmentTransaction addToBackStack(String name) {
        return null;
    }

    @Override
    public boolean isAddToBackStackAllowed() {
        return false;
    }

    @Override
    public FragmentTransaction disallowAddToBackStack() {
        return null;
    }

    @Override
    public FragmentTransaction setBreadCrumbTitle(int res) {
        return null;
    }

    @Override
    public FragmentTransaction setBreadCrumbTitle(CharSequence text) {
        return null;
    }

    @Override
    public FragmentTransaction setBreadCrumbShortTitle(int res) {
        return null;
    }

    @Override
    public FragmentTransaction setBreadCrumbShortTitle(CharSequence text) {
        return null;
    }

    public FragmentTransaction hide(Fragment fragment) {
        if (fragment != null && fragment.isInLayout()) {
            mShowFragment.remove(fragment);
            mHideFragment.add(fragment);
            MethodUtil.setParamValue(fragment, "mHidden", true, Fragment.class);
            MethodUtil.setParamValue(fragment, "mInLayout", false, Fragment.class);
            if (fragment.getView() != null) {
                fragment.getView().setVisibility(View.GONE);
            }
        }
        return this;
    }

    public int commit() {
        while (mHideFragment.size() != 0) {
            final Fragment fragment = mHideFragment.get(0);
            if (fragment != null) {
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    Fragment f = fragment;

                    @Override
                    public void run() {
                        f.onPause();
                    }
                });

            }
            mHideFragment.remove(fragment);
        }
        for (final Fragment fragment : mShowFragment) {

            if (fragment != null) {
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    Fragment f = fragment;

                    @Override
                    public void run() {
                        f.onStart();
                    }
                });
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    Fragment f = fragment;

                    @Override
                    public void run() {
                        f.onResume();
                    }
                });
            }

        }
        return 0;
    }

    public int commitAllowingStateLoss() {
        //调用createActivity
        while (mHaveAddView.size() != 0) {
            final Fragment fragment = mHaveAddView.get(0);
            if (fragment != null) {
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    Fragment f = fragment;

                    @Override
                    public void run() {
                        f.onActivityCreated(null);
                    }
                });
            }
            mHaveAddView.remove(fragment);
        }
        commit();
        return 0;
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


    public List<Fragment> getFragments() {
        return mFragments == null ? new ArrayList<Fragment>() : mFragments;
    }

    public List<Fragment> getShowFragment() {
        return mShowFragment == null ? new ArrayList<Fragment>() : mShowFragment;
    }

    public void onRemuse() {
        if (mShowFragment != null) {
            for (int i = 0; i < mShowFragment.size(); i++) {
                Fragment fragment = mShowFragment.get(i);
                if (fragment != null) {
                    fragment.onResume();
                }
            }
        }
    }

    public void onPause() {
        if (mShowFragment != null) {
            for (int i = 0; i < mShowFragment.size(); i++) {
                Fragment fragment = mShowFragment.get(i);
                if (fragment != null) {
                    fragment.onPause();
                }
            }
        }
    }

}
