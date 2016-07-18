package com.yn.framework.remind;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.PopupWindow;

import com.yn.framework.R;


/**
 * Created by youjiannuo on 2015/3/6.
 */
public class FloatWindow {

    private int mLayoutId;
    private int mHeight = LayoutParams.WRAP_CONTENT;
    private int mWidth = LayoutParams.WRAP_CONTENT;

    protected Context mContext;
    private PopupWindow mPpWindow;
    private View mChildView;
    private Window mWindow;
    private Dialog mDialog;
    private int mGravity = Gravity.CENTER;
    private View mView;
    private int mAnimation = -1;

    public FloatWindow(View v, Context context, int width, int height, int animation, int gravity) {
        this(v, context, width, height, gravity);
        mAnimation = animation;
    }

    public FloatWindow(View v, Context context, int width, int height, int gravity) {
        this(-1, context, width, height, gravity);
        mView = v;
    }


    public FloatWindow(int layoutId, Context context) {
        this(layoutId, context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public FloatWindow(int layoutId, Context context, int width, int height, int gravity) {
        this(layoutId, context, width, height);
        mGravity = gravity;
    }

    public FloatWindow(int layoutId, Context context, int width, int height, int animation, int gravity) {
        this(layoutId, context, width, height, gravity);
        mAnimation = animation;
    }

    public FloatWindow(int layoutId, Context context, int width, int height) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mHeight = height;
        this.mWidth = width;
    }

    public void setAnimation(int animation) {
        mAnimation = animation;
        if (mWindow != null) {
            mWindow.setWindowAnimations(animation);
        } else if (mPpWindow != null) {
            mPpWindow.setAnimationStyle(animation);
        }
    }

    private void initDialog(boolean isClose) {
        if (mDialog != null) return;

        mDialog = new Dialog(mContext, R.style.dialog);
        //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
        mDialog.setCanceledOnTouchOutside(isClose);
        //调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用
        //				alertDialog.setCancelable(false);
//        mDialog.show();
        mDialog.onWindowFocusChanged(true);

        mWindow = mDialog.getWindow();
        if (mView != null) {
            mWindow.setContentView(mView);
        } else {
            mWindow.setContentView(mLayoutId);
        }
        mChildView = mWindow.getDecorView();
        mWindow.setLayout(mWidth, mHeight);
        mWindow.setGravity(mGravity);
        if (mAnimation != -1) {
            mWindow.setWindowAnimations(mAnimation);
        }
        initView(mChildView);
    }

    protected void initView(View v) {

    }


    @SuppressWarnings("deprecation")
    private void initMppWindow(boolean isClose) {
        if (mPpWindow != null) return;
        mChildView = LayoutInflater.from(mContext).inflate(mLayoutId, null);
        mPpWindow = new PopupWindow(mChildView, mWidth, mHeight, isClose);
        if (mAnimation != -1) {
            mPpWindow.setAnimationStyle(mAnimation);
        }
        // 需要设置一下此参数，点击外边可消失
        mPpWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        mPpWindow.setOutsideTouchable(isClose);
        mPpWindow.setTouchable(isClose);
        mPpWindow.setFocusable(isClose);
    }

    public View getContextView() {
        return mChildView;
    }


//    public void show(View parentView , int)

    public void show(View parentView) {
        show(parentView, true);
    }


    /**
     * 显示浮动窗口
     *
     * @param parentView 需要显示在某一个空间旁边
     */
    public void show(View parentView, boolean isOutsideTouchable) {
        show(parentView, 0, 0, isOutsideTouchable);
    }


    public void show(View parentView, int offX, int offY, boolean isOutsideTouchable) {
        if (parentView == null) {
            show(isOutsideTouchable);
            return;
        }
        initMppWindow(isOutsideTouchable);
        mPpWindow.showAsDropDown(parentView, offX, offY);
    }

    public void setPpWindowListener(PopupWindow.OnDismissListener l) {
        if (mPpWindow != null) {
            mPpWindow.setOnDismissListener(l);
        }
    }

    public void show() {
        show(true);
    }

    public void show(boolean isOutsideTouchable) {
        initDialog(isOutsideTouchable);
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (mContext != null) {
            Activity activity = (Activity) mContext;
            if (activity.isFinishing()) return;
            try {
                if (mPpWindow != null)
                    mPpWindow.dismiss();
                if (mDialog != null) mDialog.cancel();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}