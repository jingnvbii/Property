package com.ctrl.forum.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by Administrator on 2016/5/12.
 */

    public class CustomViewPager extends ViewPager {
        public CustomViewPager(Context context) {
            super(context);
        }

        public CustomViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
            if (v instanceof HorizontalScrollView) {
                return true;
            }
            return super.canScroll(v, checkV, dx, x, y);
        }

    private boolean scrollble = true;



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return true;
        }
        return super.onTouchEvent(ev);
    }


    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
}

