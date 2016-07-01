package com.ctrl.forum.base;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ctrl.forum.R;
import com.ctrl.forum.ui.activity.mine.MineMessageActivity;
import com.ctrl.forum.ui.activity.mine.MineSettingActivity;
import com.ctrl.forum.ui.activity.rim.ExampleUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 广播接受者
 * Created by Administrator on 2016/6/22.
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyBroadcastReceiver extends BroadcastReceiver{
    private static final String TAG = "JPush";

    NotificationManager mManager = null;
    Notification notification =null;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String registrationID =  JPushInterface.getRegistrationID(context);

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        }
        else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
           // processCustomMessage(context, bundle);
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            setNavti(context, title,message);
            mManager.notify(0, notification);
        }
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            // 在这里可以做些统计，或者做些其他工作
        }
        else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent i = new Intent(context, MineMessageActivity.class);  //自定义打开的界面
            i.putExtras(bundle);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            context.startActivity(i);
        } else {
            Log.d("TAG", "Unhandled intent - " + intent.getAction());
        }
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        if (MineSettingActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(MineSettingActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MineSettingActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (null != extraJson && extraJson.length() > 0) {
                        msgIntent.putExtra(MineSettingActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                }

            }
            context.sendBroadcast(msgIntent);
        }
    }

    private void setNavti(Context context,String title,String content) {
        // 得到通知消息的管理器对象，负责管理 Notification 的发送与清除消息等
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建Notification对象 参数分别代表 通知栏 中显示的图标 显示的标题 显示的时间
        notification = new Notification(R.mipmap.image_default,
                "Android专业开发群", System.currentTimeMillis());

        // 设置在通知栏中点击后Notification自动消失
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        //设置点击后转跳的新activity
        Intent intent = new Intent(context, MineSettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);

        //通过bundle可以带一些数据过去 这里将字符串传递了过去
        Bundle bundle = new Bundle();
        bundle.putString("name", "从Notification转跳过来的");
        intent.putExtras(bundle);

        //设置通知栏中显示的内容
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, "Android专业开发群",
                "QQ群号 164257885", contentIntent);
    }

}
