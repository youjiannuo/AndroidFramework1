package com.yn.framework.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yn.framework.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public abstract class BucketListAdapter<T> extends BaseAdapter {

    private List<T> mObjects;
    private final Object mLock = new Object();
    private boolean mNotifyOnChange = true;


    public interface LoadMoreListener {
        public void onLoadMore();
    }

    private static final String TAG = "BucketListAdapter";
    private static final int TYPE_COUNT = 2;
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_LOAD_MORE = 1;

    private final boolean DEBUG = false;

    private LoadMoreListener mListener;
    private View mLoadMoreView;
    private boolean mLoadMoreEnabled;
    private Context mContext;
    private int mBucketSize;
    private ArrayList<T> mOriginalValues;

    /**
     * Basic constructor, takes an Activity context and the list of elements.
     * Assumes a 1 column view by default.
     *
     * @param ctx      The Activity context.
     * @param elements The list of elements to present.
     */
    public BucketListAdapter(Context ctx, List<T> elements) {
        this(ctx, elements, 1);
        
    }

    /**
     * Extended constructor, takes an Activity context, the list of elements and
     * the exact number of columns.
     *
     * @param ctx        The Activity context.
     * @param elements   The list of elements to present.
     * @param bucketSize The exact number of columns.
     */
    public BucketListAdapter(Context ctx, List<T> elements, int bucketSize) {
        super();
        mObjects = elements;
        mContext = ctx;
        mBucketSize = bucketSize;
    }

    public void setLoadMoreListener(LoadMoreListener listener) {
        mListener = listener;
        mLoadMoreEnabled = true;
    }

    public boolean isLoadMoreEnabled() {
        return mLoadMoreEnabled;
    }

    public void enableLoadMore() {
        mLoadMoreEnabled = true;
        notifyDataSetChanged();
    }

    public void disableLoadMore() {
        mLoadMoreEnabled = false;
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return mObjects;
    }

//    /**
//     * Calculates the required number of columns based on the actual screen
//     * width (in DIP) and the given minimum element width (in DIP).
//     *
//     * @param minBucketElementWidthDip
//     *            The minimum width in DIP of an element.
//     */
//    public void enableAutoMeasure(float minBucketElementWidthDip) {
//        float screenWidth = getScreenWidthInDip();
//
//        if (minBucketElementWidthDip >= screenWidth) {
//            mBucketSize = 1;
//        } else {
//            mBucketSize = (int) (screenWidth / minBucketElementWidthDip);
//        }
//    }

    public int getBucketSize() {
        return mBucketSize;
    }

    @Override
    public final int getCount() {
        int count = getBucketCount();
        if (mLoadMoreEnabled) {
            count++;
        }
        return count;
    }

    private int getBucketCount() {
        return (getElementCount() + mBucketSize - 1) / mBucketSize;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getElementCount() {
        return mObjects.size();
    }

    public T getElement(int position) {
        return mObjects.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position == getBucketCount() && mLoadMoreEnabled) {
            if (mListener != null) {
                mListener.onLoadMore();
            }
            return getLoadMoreView();
        }

        if (convertView == getLoadMoreView()) {
            convertView = null;
        }

        LinearLayout bucket;
        if (convertView != null) {
            bucket = (LinearLayout) convertView;

            if (DEBUG) {
                Log.i(TAG, "Reusing bucket view");
            }
        } else {
            bucket = newBucket();

            if (DEBUG) {
                Log.i(TAG, "Instantiating new bucket view");
            }
        }

        fillBucket(position, bucket);

        return bucket;
    }

    private void fillBucket(int bucketPosition, LinearLayout bucket) {
        int j = 0;
        int elementStartPosition = bucketPosition * mBucketSize;
        int elementEndPosition = elementStartPosition + mBucketSize;
        for (int elementPosition = elementStartPosition; elementPosition < elementEndPosition; elementPosition++) {
            FrameLayout bucketElementFrame;
            if (j < bucket.getChildCount()) {
                bucketElementFrame = (FrameLayout) bucket.getChildAt(j);

                if (DEBUG) {
                    Log.i(TAG, "Reusing bucketElementFrame view");
                }

                if (elementPosition < getElementCount()) {
                    View currentConvertView = bucketElementFrame.getChildAt(0);
                    View current = getBucketElement(elementPosition, getElement(elementPosition), currentConvertView);
                    if (currentConvertView == null) {
                        bucketElementFrame.addView(current);
                    }

                    if (DEBUG) {
                        Log.i(TAG, "Reusing element view");
                    }
                    if (currentConvertView != null) {
                        currentConvertView.setVisibility(View.VISIBLE);
                    }
                } else {
                    View currentConvertView = bucketElementFrame.getChildAt(0);
                    if (currentConvertView != null) {
                        currentConvertView.setVisibility(View.GONE);
                    }
                }
            } else {
                bucketElementFrame = new FrameLayout(mContext);
                bucketElementFrame.setLayoutParams(new LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1));

                if (elementPosition < getElementCount()) {
                    View current = getBucketElement(elementPosition, getElement(elementPosition), null);
                    bucketElementFrame.addView(current);
                }

                bucket.addView(bucketElementFrame);
            }
            j++;
        }
    }

    private LinearLayout newBucket() {
        LinearLayout bucket;
        bucket = new LinearLayout(mContext);
        bucket.setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        bucket.setOrientation(LinearLayout.HORIZONTAL);
        return bucket;
    }

    /**
     * Extending classes should return a bucket-element with this method. Each
     * row in the list contains bucketSize total elements.
     *
     * @param position    The absolute, global position of the current item.
     * @param convertView The old view to reuse, if possible.
     * @return The View that should be presented in the corresponding bucket.
     */
    protected View getBucketElement(int position, T element, View convertView) {
        if (convertView == null) {
            convertView = newView(position, element);
        }
        bindView(convertView, position, element);
        return convertView;
    }

    protected abstract View newView(int position, T element);

    protected abstract void bindView(View view, int position, T element);

//    protected float getScreenWidthInDip() {
//        WindowManager wm = mContext.();
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
//        int screenWidth_in_pixel = dm.widthPixels;
//        float screenWidth_in_dip = screenWidth_in_pixel / dm.density;
//
//        return screenWidth_in_dip;
//    }

    private View getLoadMoreView() {
        if (mLoadMoreView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View frameLayout = layoutInflater.inflate(R.layout.y_bucket_progress_bar, null);
            mLoadMoreView = frameLayout;
        }
        return mLoadMoreView;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getBucketCount()) {
            return TYPE_LOAD_MORE;
        } else {
            return TYPE_NORMAL;
        }
    }

    public void add(T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(object);
            } else {
                mObjects.add(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addAll(Collection<? extends T> collection) {

        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(collection);
            } else {
                mObjects.addAll(collection);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();


    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    public void addAll(T... items) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.addAll(mOriginalValues, items);
            } else {
                Collections.addAll(mObjects, items);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    public void insert(T object, int index) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(index, object);
            } else {
                mObjects.add(index, object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.remove(object);
            } else {
                mObjects.remove(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
            } else {
                mObjects.clear();
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     *                   in this adapter.
     */
    public void sort(Comparator<? super T> comparator) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.sort(mOriginalValues, comparator);
            } else {
                Collections.sort(mObjects, comparator);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    /**
     * Control whether methods that change the list ({@link #add},
     * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
     * {@link #notifyDataSetChanged}.  If set to false, caller must
     * manually call notifyDataSetChanged() to have the changes
     * reflected in the attached view.
     * <p>
     * The default is true, and calling notifyDataSetChanged()
     * resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will
     *                       automatically call {@link
     *                       #notifyDataSetChanged}
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }
}
