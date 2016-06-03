package com.ctrl.forum.loopview;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.ctrl.forum.base.MyApplication;


/**
 * @类名： UIUtils
 * @创建者： 史翔宇
 * @创建时间：2015年11月18日
 * @描述： UI工具类
 */
public class UIUtils {
    public static Context getContext() {
        return MyApplication.getContext();
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static String getPackageName() {
        return getContext().getPackageName();
    }

    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    public static Handler getMainHandler() {
        return MyApplication.getMainHander();
    }

    public static long getMainThreadId() {
        return MyApplication.getMainThreadId();
    }

    public static void post(Runnable task) {
        //判断是否是在主线程中指执行的
        //获得执行该方法的线程的id
        int currentThreadId = android.os.Process.myTid();
        if (getMainThreadId() == currentThreadId) {
            //在主线程中执行的
            task.run();
        } else {
            //在子线程中执行的
            getMainHandler().post(task);
        }

    }

    /**
     * dip 转 px
     *
     * @param dip
     * @return
     */
    public static int dip2px(int dip) {
        //
        // 公式： dp = px / (dpi / 160) px = dp * (dpi / 160)
        // dp = px / denisity
        // px = dp * denisity;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (dip * density + 0.5f);
    }

    /**
     * px 转 dip
     *
     * @param px
     * @return
     */
    public static int px2dip(int px) {
        // dp = px / denisity
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (px / density + 0.5f);
    }

    /**
     * 执行需要延时的任务
     */
    public static void postDelayed(Runnable task, int delayed) {
        getMainHandler().postDelayed(task, delayed);
    }

    /**
     * 移除任务
     *
     * @param task
     */
    public static void removeCallbacks(Runnable task) {
        getMainHandler().removeCallbacks(task);
    }
}
