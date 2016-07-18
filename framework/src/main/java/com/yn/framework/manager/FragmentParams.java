package com.yn.framework.manager;

import android.support.v4.app.FragmentTransaction;

public class FragmentParams {
    public Class[] cls;
    public int contentViewId;
    public int[] contentViewIds;
    public int mFirstSelectIndex = 0;
    public OnFragmentListener mOnFragmentListener;
    public int FragmentTransactionAnimation = FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
    public int enterAnimation = 0;
    public int exitAnimation = 0;
}