package com.ctrl.forum.base;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.ui.activity.Invitation.InvitationDetailFromPlatformActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationPinterestDetailActivity;
import com.ctrl.forum.ui.activity.StartActivity;
import com.ctrl.forum.ui.activity.mine.MineOrderActivity;
import com.ctrl.forum.ui.activity.mine.MineOrderManageActivity;
import com.ctrl.forum.ui.activity.mine.MineXianJuanActivity;
import com.ctrl.forum.ui.activity.mine.MineYouJuanActivity;
import com.ctrl.forum.ui.activity.rim.ExampleUtil;
import com.ctrl.forum.ui.activity.store.StoreCommodityDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreShopDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
    String targetId;
    String sourceType;
    String msgId,companyId;
    Intent ii = null;
    Bundle bundle = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String registrationID =  JPushInterface.getRegistrationID(context);

        //通知栏下方的图标能变化,但字体颜色不会适配(太长也不行)
       /* CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(context,
                R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);  // 指定定制的 Notification Layout
        builder.statusBarDrawable = R.mipmap.tr_white;      //指定最顶层状态栏小图标
        builder.layoutIconDrawable =R.mipmap.logo;   //指定下拉状态栏时显示的通知图标
        JPushInterface.setPushNotificationBuilder(2, builder);
        JPushInterface.setDefaultPushNotificationBuilder(builder);*/

        //字体颜色会适配,但下拉出现的框的图片也是顶部状态栏的图片
        /*BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
        builder.statusBarDrawable = R.mipmap.logo;
        JPushInterface.setPushNotificationBuilder(2, builder);
        JPushInterface.setDefaultPushNotificationBuilder(builder);*/

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        }
        else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息的ID: " + notifactionId);
           // processCustomMessage(context, bundle);
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            bundle.getString(JPushInterface.EXTRA_APP_KEY);

            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.e("extras=========",extras);
            // {"messageKey":"9","targetId":"5b088e45a3af40adae27c38eb268f052","sourceType":"1"}

            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject object = new JSONObject(extras);
                    messageKey = object.optString("messageKey");
                    sourceType = object.optString("sourceType");
                    targetId = object.optString("targetId");
                    msgId= object.optString("msgId");
                    companyId = object.optString("companyId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);

            Intent itt = new Intent();
            itt.setAction("com.message");
            itt.putExtra("num", "num");
            context.sendBroadcast(itt, null);

            //程序运行在前端收不到消息,程序运行在后端可以收到自定义消息
           /* if (!Arad.preferences.getString("memberId").equals("")) {
                if (!isBackground(context) && !isFrom(messageKey)) {//程序运行在前台，通知类型不是评论帖子，帖子收到回复
                    if (!Arad.preferences.getBoolean("isSet")) {
                        if (Arad.preferences.getBoolean("replyComments")) {//评论回复
                            setNavti(context, "您收到一条新消息", message, messageKey);
                        } else {
                            if (Arad.preferences.getBoolean("systemNotification")) {//系统通知
                                setNavti(context, "系统通知", message, messageKey);}
                        }
                    }else {setNavti(context, "您收到一条新消息", message, messageKey);}
                }else {setNavti(context, "您收到一条新消息", message, messageKey);}
            }*/
            if (!Arad.preferences.getString("memberId").equals("")) {
                if (Arad.preferences.getBoolean("isSet")) {
                    if (Arad.preferences.getBoolean("replyComments")) {//评论回复
                        setNavti(context, "您收到一条新消息", message, messageKey);
                    } else {
                        if (Arad.preferences.getBoolean("systemNotification")) {//系统通知
                            setNavti(context, "系统通知", message, messageKey);}
                        }
                }else {
                    setNavti(context, "您收到一条新消息", message, messageKey);
                }
            }
        }
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
           /* BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
            builder.statusBarDrawable = R.mipmap.logo;
            JPushInterface.setPushNotificationBuilder(1, builder);*/

            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知:"+bundle.getString(JPushInterface.EXTRA_MESSAGE));
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            // 在这里可以做些统计，或者做些其他工作.

            Intent itt = new Intent();
            itt.setAction("com.message");
            itt.putExtra("num", "num");
            context.sendBroadcast(itt, null);

        }
        else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
           /* if (!Arad.preferences.getString("memberId").equals("")) {
                Intent i = new Intent(context, MineMessageActivity.class);  //自定义打开的界面
                //i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }else{
                Intent i = new Intent(context, MainActivity.class);  //自定义打开的界面
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }*/

            bundle.getString(JPushInterface.EXTRA_APP_KEY);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            // {"messageKey":"9","targetId":"5b088e45a3af40adae27c38eb268f052","sourceType":"1"}
            Log.e("extras============",extras);

            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject object = new JSONObject(extras);
                    messageKey = object.optString("messageKey");
                    sourceType = object.optString("sourceType");
                    targetId = object.optString("targetId");
                    msgId= object.optString("msgId");
                    companyId = object.optString("companyId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);

            Log.e("messageKey===========", messageKey);
            Log.e("messageKey===========",messageKey);
            startAiti(messageKey,context);
        } else {
            Log.d("TAG", "Unhandled intent - " + intent.getAction());
        }
    }

    private void setNavti(Context context,String title,String content,String state) {
        // 得到通知消息的管理器对象，负责管理 Notification 的发送与清除消息等
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建Notification对象 参数分别代表 通知栏 中显示的图标 显示的标题 显示的时间
        if (Arad.preferences.getBoolean("notificationShow") || !Arad.preferences.getBoolean("isSet")){//通知显示消息详情
            notification = new Notification(R.mipmap.logo,content,System.currentTimeMillis());
        }else{
            notification = new Notification(R.mipmap.logo,title,System.currentTimeMillis());
        }

        if (Arad.preferences.getBoolean("vibration") || !Arad.preferences.getBoolean("isSet")){//振动
            notification.defaults = Notification.DEFAULT_VIBRATE;
            //notification.defaults = Notification.DEFAULT_SOUND;
        }

        if (!Arad.preferences.getBoolean("isSet")){//声音,振动
            notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE ;
        }else{
            if (Arad.preferences.getBoolean("vibration")&& Arad.preferences.getBoolean("voice")){
                notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE ;
            }else{
                if (Arad.preferences.getBoolean("vibration")){//振动
                    notification.defaults = Notification.DEFAULT_VIBRATE;
                }
                if (Arad.preferences.getBoolean("voice")){//声音
                    notification.defaults = Notification.DEFAULT_SOUND;
                }
            }
        }

        // 设置在通知栏中点击后Notification自动消失.
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        //设置点击后转跳的新activity
        if (state!=null) {
            switch (state) {
                case "1": //1.用户下单支付成功<通知商家>：跳转到
                    ii = new Intent(context, MineOrderManageActivity.class);
                    ii.putExtra("msgId",msgId);
                    ii.putExtra("companyId",companyId);
                    break;
                case "2"://2：商家发货<通知买家>
                    ii = new Intent(context, MineOrderActivity.class);
                    ii.putExtra("msgId",msgId);
                    break;
                case "3"://3：买家领取优惠券<通知买家>
                    ii = new Intent(context, MineYouJuanActivity.class);
                    ii.putExtra("msgId",msgId);
                    break;
                case "4"://4：商家赠送现金券给买家<通知买家>
                    ii = new Intent(context, MineXianJuanActivity.class);
                    ii.putExtra("msgId",msgId);
                    break;
                case "5"://5：会员发布帖子<通知会员>
                    //intent = new Intent(context, MineMessageActivity.class);
                    jumpActiv(context);
                    break;
                case "6"://6：已发布帖子需要审核<通知会员>
                    //intent = new Intent(context, MineMessageActivity.class);
                    jumpActiv(context);
                    break;
                case "7"://7：帖子被赞<通知发帖人>
                    //intent = new Intent(context, MineMessageActivity.class);
                    jumpActiv(context);
                    break;
                case "8"://8：帖子收到评论
                    //intent = new Intent(context, MineMessageActivity.class);
                    jumpActiv(context);
                    break;
                case "9"://9：帖子评论收到回复
                    //intent = new Intent(context, MineMessageActivity.class);
                    jumpActiv(context);
                    break;
                case "10"://后台推送帖子
                    //intent = new Intent(context, MineMessageActivity.class);
                    jumpActiv(context);
                    break;
                case "11"://后台推送商品
                    ii = new Intent(context, StoreCommodityDetailActivity.class);
                    ii.putExtra("id",targetId);
                    ii.putExtra("msgId",msgId);
                    break;
                case "12"://后台推送店铺
                    ii = new Intent(context, StoreShopDetailActivity.class);
                    ii.putExtra("id",targetId);
                    ii.putExtra("msgId",msgId);
                    break;
                case "13"://后台推送通知
                    break;
                default:
                    break;
            }
            if (ii!=null)
                ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        //通过bundle可以带一些数据过去 这里将字符串传递了过去
        /*bundle = new Bundle();
        bundle.putString("name", "从Notification转跳过来的");
        intent.putExtras(bundle);*/

        //设置通知栏中显示的内容
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                R.string.app_name,ii, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, title,
                content, contentIntent);
        mManager.notify(0,notification);
    }

    /**
     * 需要权限:android.permission.GET_TASKS
     * @param context
     * @return
     */
    public boolean isApplicationBroughtToBackground(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            //Debug.i(TAG, "topActivity:" + topActivity.flattenToString());
            //Debug.f(TAG, "topActivity:" + topActivity.flattenToString());
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
    public boolean isFrom(String messageKey){
       if (messageKey.equals("5")){
           return true;
       }
       if (messageKey.equals("6")){
           return true;
       }
       if (messageKey.equals("7")){
           return true;
       }
       if (messageKey.equals("8")){
           return true;
       }
       if (messageKey.equals("9")){
           return true;
       }
       return false;
   }

    public void jumpActiv(Context context){
        switch (sourceType){
            case "1"://app
                ii = new Intent(context, InvitationPinterestDetailActivity.class);
                ii.putExtra("id",targetId);
                ii.putExtra("msgId",msgId);
                break;
            case "0"://pingtai
                ii = new Intent(context, InvitationDetailFromPlatformActivity.class);
                ii.putExtra("id",targetId);
                ii.putExtra("msgId",msgId);
                break;
        }
    }

   /* //如果没有从状态栏中删除ICON，且继续调用addIconToStatusbar,则不会有任何变化.如果将notification中的resId设置不同的图标，则会显示不同的图标
    private void addIconToStatusbar(int resId,Context context){
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = new Notification();
        //常驻状态栏的图标
        n.icon = resId;
        // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
        n.flags |= Notification.FLAG_ONGOING_EVENT;
        // 表明在点击了通知栏中的"清除通知"后，此通知不清除， 经常与FLAG_ONGOING_EVENT一起使用
        n.flags |= Notification.FLAG_NO_CLEAR;
        PendingIntent pi = PendingIntent.getActivity(this, 0, context.getIntent(), 0);
        n.contentIntent = pi;
        n.setLatestEventInfo(this, getString(R.string.flow), "10M/30M", pi);
        nm.notify(NOTIFICATION_ID_ICON, n);
    }*/

    /*private void deleteIconToStatusbar(){
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_ID_ICON);
    }*/

    public void startAiti(String state,Context context){
        //设置点击后转跳的新activity
        if (state!=null) {
            switch (state) {
                case "1": //1.用户下单支付成功<通知商家>：跳转到
                    ii = new Intent(context, MineOrderManageActivity.class);
                    ii.putExtra("msgId",msgId);
                    ii.putExtra("companyId",companyId);
                    break;
                case "2"://2：商家发货<通知买家>
                    ii = new Intent(context, MineOrderActivity.class);
                    ii.putExtra("msgId",msgId);
                    break;
                case "3"://3：买家领取优惠券<通知买家>
                    ii = new Intent(context, MineYouJuanActivity.class);
                    ii.putExtra("msgId",msgId);
                    break;
                case "4"://4：商家赠送现金券给买家<通知买家>
                    ii = new Intent(context, MineXianJuanActivity.class);
                    ii.putExtra("msgId",msgId);
                    break;
                case "5"://5：会员发布帖子<通知会员>
                    //intent = new Intent(context, MineMessageActivity.class);
                    jumpActiv(context);
                    break;
                case "6"://6：已发布帖子需要审核<通知会员>
                    //intent = new Intent(context, MineMessageActivity.class);
                    jumpActiv(context);
                    break;
                case "7"://7：帖子被赞<通知发帖人>
                    //intent = new Intent(context, MineMessageActivity.class);
                    jumpActiv(context);
                    break;
                case "8"://8：帖子收到评论
                    //intent = new Intent(context, MineMessageActivity.class);
                    jumpActiv(context);
                    break;
                case "9"://9：帖子评论收到回复
                    //intent = new Intent(context, MineMessageActivity.class);
                    jumpActiv(context);
                    break;
                case "10"://后台推送帖子
                    //intent = new Intent(context, MineMessageActivity.class);
                    jumpActiv(context);
                    break;
                case "11"://后台推送商品
                    ii = new Intent(context, StoreCommodityDetailActivity.class);
                    ii.putExtra("id",targetId);
                    ii.putExtra("msgId",msgId);
                    break;
                case "12"://后台推送店铺
                    ii = new Intent(context, StoreShopDetailActivity.class);
                    ii.putExtra("id",targetId);
                    ii.putExtra("msgId",msgId);
                    break;
                case "13"://后台推送通知
                    if (isBackground(context)){
                        //正确的
                        MyApplication.clearActivity();
                        ii = new Intent(context, StartActivity.class);

                       /* //更改的
                        ii = new Intent(context, MainActivity.class);
                        ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //context.startActivity(ii);*/
                        return;
                    }
                    break;
                default:
                    break;
            }
            if (ii!=null) {
                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(ii);
            }
        }
    }

}
