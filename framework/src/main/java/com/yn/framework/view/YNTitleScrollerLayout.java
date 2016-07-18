package com.yn.framework.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yn.framework.R;
import com.yn.framework.animation.Animation;
import com.yn.framework.system.ContextManager;
import com.yn.framework.system.SystemUtil;
import com.yn.framework.system.ViewUtil;

import java.util.Date;

/**
 * Created by youjiannuo on 16/1/24.
 */
public class YNTitleScrollerLayout extends FrameLayout implements View.OnClickListener {

    private LinearLayout mTitleLayout;
    private HorizontalScrollView mScrollView;
    private View mLineView;
    private int mShowMax = 0;
    //目前选中的item
    private int mSelect = 0;
    private ValueAnimator mAnimator;
    private OnSelectItemListener mOnSelectItemListener;
    private long mTime = new Date().getTime();
    private float mLineStartX = 0;
    private View mBottomLineView;

    public YNTitleScrollerLayout(Context context) {
        super(context);
        initView();
    }

    public YNTitleScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_title_scroller, this);
        mTitleLayout = (LinearLayout) findViewById(R.id.titleLayout);
        mLineView = findViewById(R.id.line);
        mScrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
        mBottomLineView = findViewById(R.id.bottomLine);
    }

    public void showBottomLine() {
        if (mBottomLineView != null) {
            mBottomLineView.setVisibility(VISIBLE);
        }
    }

    public void setOnSelectItemListener(OnSelectItemListener l) {
        mOnSelectItemListener = l;
    }

    public void setParams(String values[], int showMaxItem) {
        setParams(values, showMaxItem, null);
    }

    public void setParams(String values[], int showMaxItem, ParamsStyle paramsStyle) {
        mShowMax = showMaxItem;
        createTitleView(values, paramsStyle);
    }

    public void setCurrentItem(int index) {
        move(index);
    }

    public void createTitleView(String[] titles) {
        createTitleView(titles, null);
    }

    public void createTitleView(String[] titles, ParamsStyle paramsStyle) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        int width = SystemUtil.getPhoneScreenWH(getContext())[0] / mShowMax;
        if (mTitleLayout != null) {
            mTitleLayout.removeAllViews();
        }
        for (int i = 0; i < titles.length; i++) {
            String value = titles[i];
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
            ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.item_title_view, null);
            TextView textView = (TextView) viewGroup.findViewById(R.id.value);
            textView.setText(value);
            if (i == 0) {
                textView.setTextColor(getResources().getColor(R.color.red));
            }

            if (paramsStyle != null) {
                textView.setTextSize(paramsStyle.fontSize);
            }
            viewGroup.setTag(i);
            mTitleLayout.addView(viewGroup, params);
            viewGroup.setOnClickListener(this);
        }

        LayoutParams lineParams = (LayoutParams) mLineView.getLayoutParams();
        lineParams.width = width;
        if (paramsStyle != null) {
            lineParams.height = SystemUtil.dipTOpx(paramsStyle.lineHeight);
            mLineView.setBackgroundColor(paramsStyle.lineColor);
        }
        mLineView.setLayoutParams(lineParams);
    }


    public void move(int index) {
        if (index >= mTitleLayout.getChildCount() || index == mSelect) return;

        final ViewGroup viewGroup = (ViewGroup) mTitleLayout.getChildAt(index);
        int itemLeft = ViewUtil.getScreenInfo(viewGroup, (View) mLineView.getParent()).x;
        float size = itemLeft - mLineView.getX() - mScrollView.getScrollX();
        final int start = (int) mLineView.getX();
        mLineView.setVisibility(View.VISIBLE);
        for (int i = 0; i < mTitleLayout.getChildCount(); i++) {
            //隐藏选中的线
            mTitleLayout.getChildAt(i).findViewById(R.id.lineBottom).setVisibility(View.GONE);
        }
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        mAnimator = Animation.valueAnimator(0, size, 100, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                LayoutParams params = (LayoutParams) mLineView.getLayoutParams();
                params.leftMargin = start + (int) value;
                mLineView.setLayoutParams(params);
            }
        });
        mAnimator.addListener(new Animation.AnimationObjectListener() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                viewGroup.findViewById(R.id.lineBottom).setVisibility(View.VISIBLE);
//                mLineView.setVisibility(View.GONE);
            }
        });
        changeItem(index);
    }

    private void changeItem(int index) {
        setItemColor(index, getResources().getColor(R.color.red));
        setItemColor(mSelect, 0xFF9B9B9B);
        mSelect = index;
    }

    private void setItemColor(int index, int color) {
        ViewGroup viewGroup = (ViewGroup) mTitleLayout.getChildAt(index);
        TextView textView = (TextView) viewGroup.findViewById(R.id.value);
        textView.setTextColor(color);
    }

    public void setLineStartX() {
        mLineView.setVisibility(VISIBLE);
        LayoutParams params = (LayoutParams) mLineView.getLayoutParams();
        mLineStartX = params.leftMargin;
    }

    public void setMoveLineX(double addX) {
        //隐藏选中的线
        dealBottomLine(mSelect, GONE);
        mLineView.setVisibility(VISIBLE);
        int value = (int) (mLineStartX + mLineView.getWidth() * addX);
        if (value < 0) return;
        LayoutParams params = (LayoutParams) mLineView.getLayoutParams();
        params.leftMargin = value;
        mLineView.setLayoutParams(params);

    }

    private void dealBottomLine(int index, int visible) {
        if (mSelect < mTitleLayout.getChildCount()) {
            ViewGroup viewGroup = (ViewGroup) mTitleLayout.getChildAt(index);
            viewGroup.findViewById(R.id.lineBottom).setVisibility(visible);
        }

    }


    @Override
    public void onClick(View v) {
        Object obj = v.getTag();
        if (obj instanceof Integer) {
            long time = new Date().getTime() - mTime;
            if (mSelect != (Integer) obj && time > 130) {
                move((Integer) obj);
                if (mOnSelectItemListener != null) {
                    mOnSelectItemListener.onSelectItemClick((Integer) obj);
                }
                mTime = new Date().getTime();
            }
        }
    }


    public interface OnSelectItemListener {
        void onSelectItemClick(int index);
    }

    public static class ParamsStyle {
        public int fontSize = 12;
        public int selectFontColor = Color.RED;
        public int normalFontColor = ContextManager.getContext().getResources().getColor(R.color.red);
        public int lineColor = Color.RED;
        public int lineHeight = 1;
    }

}
