package com.ctrl.forum.base;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.baidu.mapapi.SDKInitializer;
import com.beanu.arad.AradApplication;
import com.beanu.arad.AradApplicationConfig;
import com.beanu.arad.http.HttpConfig;
import com.ctrl.forum.face.FaceConversionUtil;
import com.ctrl.forum.service.LocationService;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 实例化 Arad
 * Created by Eric on 2015/9/25.
 */
public class MyApplication extends AradApplication {
    //public LocationClient mLocationClient = null;

    //运用list来保存们每一个activity是关键
    private static List<Activity> mList = new LinkedList<Activity>();
    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static MyApplication instance;
    public LocationService locationService;

    private static Context mContext;
    private static Thread mMainThread;
    private static long mMainThreadId;
    private static Looper mMainLooper;
    private static Handler mMainHander;

    //构造方法
    //private MyApplication(){}
    //实例化一次
    public synchronized static MyApplication getInstance(){
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    public void removeActivity(Activity activity) {
        mList.remove(activity);
    }
    //关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity:mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    //杀进程
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public static void clearActivity(){
        mList.clear();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        //百度地图初始化
        SDKInitializer.initialize(getApplicationContext());
        //SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + Constant.XUNFEI_APPID); //科大讯飞
        //LocationUtil.getInstance().init(getApplicationContext());
        config.httpConfig = new HttpConfig("succeed");

        new Thread(new Runnable() {
            @Override
            public void run() {
                FaceConversionUtil.getInstace().getFileText(MyApplication.this);
            }
        }).start();

        //应用程序的上下文
        mContext=getApplicationContext();
        //主线程
        mMainThread=Thread.currentThread();
        //主线程Id
        //mMainThreadId=mMainThread.getId();
        mMainThreadId=android.os.Process.myTid();
        mMainLooper=getMainLooper();
        //创建主线程的Handler
        mMainHander=new Handler();
    }

    public static Context getContext()
    {
        return mContext;
    }

    public static Thread getMainThread()
    {
        return mMainThread;
    }

    public static long getMainThreadId()
    {
        return mMainThreadId;
    }

    public static Looper getMainThreadLooper()
    {
        return mMainLooper;
    }

    public static Handler getMainHander()
    {
        return mMainHander;
    }

    @Override
    protected AradApplicationConfig appConfig() {
        return new AradApplicationConfig();
    }

}
