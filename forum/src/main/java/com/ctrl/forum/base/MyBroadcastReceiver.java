package com.ctrl.forum.base;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.ui.activity.mine.MineMessageActivity;
import com.ctrl.forum.ui.activity.mine.MineOrderActivity;
import com.ctrl.forum.ui.activity.mine.MineOrderManageActivity;
import com.ctrl.forum.ui.activity.mine.MineXianJuanActivity;
import com.ctrl.forum.ui.activity.mine.MineYouJuanActivity;
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
    String messageKey;
    Intent intent = null;
    Bundle bundle = null;

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
            bundle.getString(JPushInterface.EXTRA_APP_KEY);

            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject object = new JSONObject(extras);
                    messageKey = object.getString("messageKey");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);

            Intent itt = new Intent();
            itt.setAction("com.message");
            itt.putExtra("num", "num");
            context.sendBroadcast(itt,null);

            if (Arad.preferences.getBoolean("replyComments")){//评论回复
                setNavti(context, "您收到一条新消息", message, messageKey);
            }else{
                if (Arad.preferences.getBoolean("systemNotification")){//系统通知
                    setNavti(context, "系统通知", message, messageKey);
                }
            }
        }
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            // 在这里可以做些统计，或者做些其他工作.
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

    private void setNavti(Context context,String title,String content,String state) {
        // 得到通知消息的管理器对象，负责管理 Notification 的发送与清除消息等
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建Notification对象 参数分别代表 通知栏 中显示的图标 显示的标题 显示的时间
        if (Arad.preferences.getBoolean("notificationShow")){//通知显示消息详情
            notification = new Notification(R.mipmap.logo,content,System.currentTimeMillis());
        }else{
            notification = new Notification(R.mipmap.logo,title,System.currentTimeMillis());
        }

        //12
        //notification.icon = R.mipmap.image_default;

        if (Arad.preferences.getBoolean("voice")){//声音
            notification.defaults = Notification.DEFAULT_SOUND;
        }
        if (Arad.preferences.getBoolean("vibration")){//振动
            notification.defaults = Notification.DEFAULT_VIBRATE;
        }

        // 设置在通知栏中点击后Notification自动消失.
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        //设置点击后转跳的新activity
        if (state!=null) {
            switch (state) {
                case "1": //1.用户下单支付成功<通知商家>：跳转到
                    intent = new Intent(context, MineOrderManageActivity.class);
                    break;
                case "2"://2：商家发货<通知买家>
                    intent = new Intent(context, MineOrderActivity.class);
                    break;
                case "3"://3：买家领取优惠券<通知买家>
                    intent = new Intent(context, MineYouJuanActivity.class);
                    break;
                case "4"://4：商家赠送现金券给买家<通知买家>
                    intent = new Intent(context, MineXianJuanActivity.class);
                    break;
                case "5"://5：会员发布帖子<通知会员>
                    intent = new Intent(context, MineMessageActivity.class);
                    break;
                case "6"://6：已发布帖子需要审核<通知会员>
                    intent = new Intent(context, MineMessageActivity.class);
                    break;
                case "7"://7：帖子被赞<通知发帖人>
                    intent = new Intent(context, MineMessageActivity.class);
                    break;
                case "8"://8：帖子收到评论
                    intent = new Intent(context, MineMessageActivity.class);
                    break;
                case "9"://9：帖子评论收到回复
                    intent = new Intent(context, MineMessageActivity.class);
                    break;
                default:
                    break;
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //通过bundle可以带一些数据过去 这里将字符串传递了过去
        /*bundle = new Bundle();
        bundle.putString("name", "从Notification转跳过来的");
        intent.putExtras(bundle);*/

        //设置通知栏中显示的内容
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, title,
                content, contentIntent);
        mManager.notify(0,notification);
    }

}
