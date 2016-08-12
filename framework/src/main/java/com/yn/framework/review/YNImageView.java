package com.yn.framework.review;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.yn.framework.R;
import com.yn.framework.data.JSON;
import com.yn.framework.imageLoader.ImageViewNetwork;
import com.yn.framework.imageLoader.NetworkPhotoTask;
import com.yn.framework.review.manager.OnBackListener;
import com.yn.framework.review.manager.YJNView;
import com.yn.framework.system.SystemUtil;

/**
 * Created by youjiannuo on 16/3/16.
 */
public class YNImageView extends ImageViewNetwork implements OnYNOperation {
    private YJNView mYJNView;
    protected String mDataKey;
    private int mHeight;
    private int mWidth;
    private int mPosition;
    private boolean mRounded = false;
    private float mCorners = -1;
    private int mDefaultBitmap = -1;
    private Object mData;

    public YNImageView(Context context) {
        super(context);
    }

    public YNImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.YNView);
        mWidth = (int) array.getDimension(R.styleable.YNView_image_width, -1);
        mHeight = (int) array.getDimension(R.styleable.YNView_image_height, -1);
        int widthSize = array.getInt(R.styleable.YNView_image_width_size, 0);
        if (widthSize != 0) {
            mWidth = SystemUtil.getPhoneScreenWH(context)[0] / widthSize;
        }
        mYJNView = new YJNView(this, context, attrs);
        mDataKey = mYJNView.getDataKey();

        TypedArray array1 = context.obtainStyledAttributes(attrs, R.styleable.YNImageView);
        mRounded = array1.getBoolean(R.styleable.YNImageView_rounded, false);
        mCorners = array1.getDimension(R.styleable.YNImageView_corners, -1);
        mDefaultBitmap = array1.getResourceId(R.styleable.YNImageView_default_bitmap, -1);
        array1.recycle();
        if (mDefaultBitmap != -1){
            setImageResource(mDefaultBitmap);
        }
    }

    @Override
    public void setData(Object obj) {
        if (obj == null) return;
        mData = obj;
        mYJNView.setData(obj);
        String url;
        if (obj instanceof JSON) {
            url = ((JSON) obj).getString(mDataKey);
        } else {
            url = obj.toString();
        }
        NetworkPhotoTask task = NetworkPhotoTask.build();
        task.url = url;
        task.height = mHeight;
        task.width = mWidth;
        task.isSetRounded = mRounded;
        task.roundedCornersSize = (int) mCorners;
        task.startDrawId = mDefaultBitmap;
        setImageParams(task);
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public void setOnBackListener(OnBackListener l) {
        mYJNView.setOnBackListener(l, mPosition);
    }

    @Override
    public void setPosition(int index) {
        mPosition = index;
    }

    @Override
    public int getOnClick() {
        return mYJNView.getOnClick();
    }

    @Override
    public Object getData() {
        return mData;
    }

    @Override
    public OnYNOperation[] getYNOperation() {
        return new OnYNOperation[0];
    }

    @Override
    public void setYNOperation(OnYNOperation[] operations) {

    }


}
