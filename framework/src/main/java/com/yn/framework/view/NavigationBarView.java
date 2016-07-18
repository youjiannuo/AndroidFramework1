package com.yn.framework.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yn.framework.system.ContextManager;
import com.yn.framework.R;


/**
 * Created by youjiannuo on 15/7/1.
 * <p/>
 * 头部导航栏
 */
public class NavigationBarView extends RelativeLayout {


    private View mRight;
    private View mLeft;
    private TextView mTitle;


    private TextView mTitleRightButtonTop;
    private TextView mTitleLeftButtonTop;
    private Button mTitleRightButton;
    private Button mTitleLeftButton;
    private ImageView mRightImageButton;
    private TextView mRightTextView;


    public NavigationBarView(Context context) {
        super(context);

    }

    public NavigationBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public NavigationBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    public TextView getTitle() {
        return mTitle = (TextView) getView(mTitle, R.id.title);
    }

    public Button getTitleRightButton() {
        mTitleRightButton = (Button) getView(mTitleRightButton, R.id.titleButtonRight);
        if (mTitleRightButton.getTag() == null && mTitleRightButton.getVisibility() == VISIBLE) {
            mTitleRightButton.setTag(getTitleRightButtonTop());
            getTitleRightButtonTop().setVisibility(View.INVISIBLE);
        }
        return mTitleRightButton;
    }

    public void setLeftImage(int resourceImageId) {
        ImageView imageView = (ImageView) getLeftView().findViewById(R.id.leftImageView);
        imageView.setImageResource(resourceImageId);
    }

    public void setLeftText(int resourceStringId) {
        String leftText = "";
        try {
            leftText = getContext().getString(resourceStringId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setLeftText(leftText);
    }

    public void setLeftText(String leftText) {
        TextView textView = (TextView) getLeftView().findViewById(R.id.leftTextView);
        textView.setVisibility(VISIBLE);
        textView.setText(leftText);
    }


    public Button getTitleLeftButton() {
        mTitleLeftButton = (Button) getView(mTitleLeftButton, R.id.titleButtonLeft);
        if (mTitleLeftButton.getTag() == null && mTitleLeftButton.getVisibility() == VISIBLE) {
            mTitleLeftButton.setTag(getTitleLeftButtonTop());
            getTitleLeftButtonTop().setVisibility(View.VISIBLE);
        }
        return mTitleLeftButton;
    }

    public ImageView getRightImageButton() {
        return mRightImageButton = (ImageView) getView(mRightImageButton, R.id.rightImageViewButton);
    }

    public TextView getRightTextView() {
        return mRightTextView = (TextView) getView(mRightTextView, R.id.rightTextView);
    }

    public TextView getTitleRightButtonTop() {
        return mTitleRightButtonTop = (TextView) getView(mTitleRightButtonTop, R.id.titleButtonRightTop);
    }

    public TextView getTitleLeftButtonTop() {
        return mTitleLeftButtonTop = (TextView) getView(mTitleLeftButtonTop, R.id.titleButtonLeftTop);
    }

    public void showTopProgress() {
        findViewById(R.id.topProgress).setVisibility(VISIBLE);
    }

    public void closeTopProgress() {
        findViewById(R.id.topProgress).setVisibility(GONE);
    }

    public void setTitle(String s) {
        getTitle().setText(s);
        showView(mTitle);
    }

    public void setTitle(int stringId) {
        setTitle(ContextManager.getString(stringId));
    }

    public void setTitleRightButton(String s) {
        getTitleRightButton().setText(s);
        showView(mTitleRightButton);
    }

    public void setTitleLeftButton(String s) {
        getTitleLeftButton().setText(s);
        showView(mTitleLeftButton);
        mTitleLeftButton.setVisibility(View.VISIBLE);

    }

    public void setRightImageButton(Drawable drawable) {
        getRightImageButton().setImageDrawable(drawable);
        showView(mRightImageButton);
    }

    public void setRightImageButton(int drawResId) {
        getRightImageButton().setImageResource(drawResId);
        showView(mRightImageButton);
    }


    public void setRightTextView(String s) {
        getRightTextView().setText(s);
        showView(mRightTextView);
    }

    public void addRightChildView(View view) {
        ((ViewGroup) getRightView()).addView(view);
    }

    public void setOnLeftClickListener(OnClickListener l) {
        getLeftView().setOnClickListener(l);
    }

    public void setOnRightClickListener(OnClickListener l) {
        getRightView().setOnClickListener(l);
    }


    public View getRightView() {
        return mRight = getView(mRight, R.id.right);
    }

    public View getLeftView() {
        return mLeft = getView(mLeft, R.id.left);
    }

    public void showRightView() {
        showView(getRightView());
    }

    public void closeRightView() {
        closeView(getRightView());
    }


    /**
     * 关闭左边的控件
     */
    public void closeLeftView() {
        closeView(getLeftView());
    }

    //设置左按钮不可以用
    public void setRightButtonClick(boolean is) {
        getRightView().setClickable(is);
    }

    private void showView(View v) {
        if (v != null) v.setVisibility(View.VISIBLE);
    }

    private void closeView(View v) {
        if (v != null)
            v.setVisibility(View.GONE);
    }

    private View getView(View v, int viewResId) {
        if (v == null) v = findViewById(viewResId);

        return v;
    }


}
