package com.ctrl.forum.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自动计算listView的大小
 * Created by Administrator on 2016/5/31.
 */
public class CountListSize extends ListView {

    public CountListSize(Context context) {
        super(context);
    }

    public CountListSize(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountListSize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2
                , MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
