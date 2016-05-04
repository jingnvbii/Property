package com.ctrl.forum.customview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * ScrollView中的listView高度根据内容而定
 * 使用时  在布局文件中 用此自定义的方法代替 listView
 * 效果: 页面中的listView会自己计算高度, 将所有的item都显示出来
 *
 * <p>设置scrollview的起始位置在顶部</p>
 * scroll_view.smoothScrollTo(0, 20);
 *
 * Created by Eric on 2015/8/17.
 */
public class XListViewForScrollView extends XListView{

    public XListViewForScrollView(Context context) {
        super(context);
    }
    public XListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public XListViewForScrollView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
