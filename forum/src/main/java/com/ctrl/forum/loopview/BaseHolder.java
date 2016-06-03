package com.ctrl.forum.loopview;

import android.content.Context;
import android.view.View;

/**
 * ClassName : BaseHolder
 * Author   : 史翔宇
 * Time     : 2015/11/19
 * Desc     :item view 对应的 View的持有者的基类
 */
public abstract class BaseHolder<T> {

    //提供不具体的view，让子类去实现具体的
    protected Context mContext;
    protected View mRootView;//根视图
    protected T mData;//数据

    public BaseHolder(Context context){
        mContext=context;
        mRootView=initView();
        //设置标记
        mRootView.setTag(this);
    }

    /**
     * 实现view的布局
     * @return
     */
    protected abstract View initView();

    /**
     * 让子类根据数据来刷新自己的视图
     *
     * @param data
     */
    protected abstract void refreshUI(T data);

    /**
     * 获取根布局
     *
     * @return
     */
    public View getRootView()
    {
        return mRootView;
    }

    public void setData(T data)
    {
        // 保存数据
        this.mData = data;

        // 通过数据来改变UI显示
        refreshUI(data);
    }
}
