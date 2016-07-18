package com.yn.framework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.yn.framework.R;
import com.yn.framework.exception.YNClickException;
import com.yn.framework.remind.RemindAlertDialog;

/**
 * Created by youjiannuo on 16/3/3.
 */
public abstract class YJNDeleteAndDragListView<T> extends YJNListView<T> implements RemindAlertDialog.OnClickListener {

    //删除
    public static final int LIST_DELETE = 0;
    //拖动
    public static final int LIST_DRAG = 1;
    //正常
    public static final int LIST_NORMAL = -1;

    private int mType = LIST_NORMAL;

    private RemindAlertDialog mRemindAlerDialog;

    private OnOperationListListener mOnOpeartionListener;
    private boolean mUpOrDownStateChanged;
    private T mT;

    public YJNDeleteAndDragListView(Context context) {
        super(context);
    }


    public YJNDeleteAndDragListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setType(int type) {
        mType = type;
        notifyDataSetChanged();
    }

    @Override
    public final View createView(int position, T data) {
        ViewGroup viewGroup = (ViewGroup) getView(R.layout.item_list_delete_and_drag_view, MATCH_PARENT, WRAP_CONTENT);
        ViewGroup v1 = (ViewGroup) viewGroup.findViewById(R.id.content);
        v1.addView(newView(position, data));
        return viewGroup;
    }

    @Override
    protected View initView(View view, int position, final T data) {
        setDeleteView(view, View.GONE, data);
        if (mType == LIST_DELETE) {
            setDeleteView(view, View.VISIBLE, data);
        } else if (mType == LIST_DRAG) {

        }

        view.setOnLongClickListener(new View.OnLongClickListener() {
            T t = data;

            @Override
            public boolean onLongClick(View v) {
                mT = t;
                showDelete(t);
                return false;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            T t = data;

            @Override
            public void onClick(View v) {
                try {
                    if (mOnOpeartionListener != null) {
                        mOnOpeartionListener.onItemClick(t);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new YNClickException(e).throwException();
                }
            }
        });
        return super.initView(view, position, data);
    }

    protected View getDeleteView(View view) {
        return view.findViewById(R.id.delete);
    }

    protected void setDeleteView(View v, int visible, T t) {
        getDeleteView(v).setVisibility(visible);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setOnOpeartionListListener(OnOperationListListener l) {
        mOnOpeartionListener = l;
    }

    public abstract View newView(int position, T t);

    protected void showDelete(T t) {
        if (mRemindAlerDialog == null) {
            mRemindAlerDialog = new RemindAlertDialog(getContext(), new int[]{R.string.hfh_is_quxiao, R.string.ok}, -1, R.string.hfh_is_delete, this);
        }
        mRemindAlerDialog.show();
    }

    @Override
    public void onRemindItemClick(int position, int type) {
        if (position == RemindAlertDialog.RIGHTBUTTON) {
            mOnOpeartionListener.onDelete(mT);
        }
    }

    public interface OnOperationListListener<T> {
        void onDelete(T t);

        void onItemClick(T t);
    }


}
