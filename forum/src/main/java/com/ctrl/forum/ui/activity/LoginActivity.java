package com.ctrl.forum.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.MyApplication;
import com.ctrl.forum.dao.LoginDao;
import com.ctrl.forum.entity.Data;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.ui.activity.mine.MineUpdatepwdActivity;
import com.ctrl.forum.ui.activity.rim.ExampleUtil;
import com.ctrl.forum.utils.InputMethodUtils;
import com.mob.tools.utils.UIHandler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 登录 activity
 * Created by Eric on 2015/11/23.
 * */
public class LoginActivity extends AppToolBarActivity implements View.OnClickListener,PlatformActionListener,Handler.Callback{

    private TextView tv_register;//注册按钮
    private EditText et_username;//用户名
    private TextView et_pass_word;//密码
    private TextView tv_login;//登录
    private TextView tv_forget;//忘记密码
    private ImageView iv_weibo;//微博
    private ImageView iv_qqzone;//qq空间
    private ImageView iv_weixin;//微信
    private LoginDao ldao;
    private MemberInfo memberInfo;

    @InjectView(R.id.iv_back)
    ImageView iv_back;
    @InjectView(R.id.ll_all)
    LinearLayout ll_all;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private String latitude,lontitude;
    private String address;

    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR= 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private String deviceImei="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.inject(this);
        ShareSDK.initSDK(this);
        initView();

        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数1
        initLocation();
        mLocationClient.start();

        Context context = getWindow().getContext();
        TelephonyManager telephonemanage = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        deviceImei = telephonemanage.getDeviceId();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(myListener);
        mLocationClient = null;
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=100000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
       // option.setOpenGps(true);//可选，默认false,设置是否使用gps
       // option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
       // option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
       // option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            latitude = location.getLatitude() + "";
            lontitude = location.getLongitude() + "";
        }
    }

    /**
     * 初始化方法
     * */
    private void initView(){
       tv_register=(TextView)findViewById(R.id.tv_register);
       et_username=(EditText)findViewById(R.id.et_username);
       et_pass_word=(TextView)findViewById(R.id.et_pass_word);
       tv_login=(TextView)findViewById(R.id.tv_login);
       tv_forget=(TextView)findViewById(R.id.tv_forget);
       iv_weibo=(ImageView)findViewById(R.id.iv_weibo);
       iv_qqzone=(ImageView)findViewById(R.id.iv_qqzone);
       iv_weixin=(ImageView)findViewById(R.id.iv_weixin);

       tv_register.setOnClickListener(this);
       tv_login.setOnClickListener(this);
       tv_forget.setOnClickListener(this);
       iv_weibo.setOnClickListener(this);
       iv_qqzone.setOnClickListener(this);
       iv_weixin.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        ll_all.setOnClickListener(this);

        ldao=new LoginDao(this);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==0){
            memberInfo=ldao.getMemberInfo();
            Arad.preferences.putString("memberId", memberInfo.getId());
            Arad.preferences.putString("nickName", memberInfo.getMemberName());
            Arad.preferences.putString("mobile", memberInfo.getMobile());
            Arad.preferences.putString("point", memberInfo.getPoint());
            Arad.preferences.putString("remark", memberInfo.getRemark());
            Arad.preferences.putString("memberLevel", memberInfo.getMemberLevel());

            if (memberInfo.getImgUrl()==null){
                Arad.preferences.putString("imgUrl","");
            }else{
                Arad.preferences.putString("imgUrl", memberInfo.getImgUrl());
            }

            Arad.preferences.putString("imgUrl", memberInfo.getImgUrl());
            Arad.preferences.putString("address", memberInfo.getAddress());
            Arad.preferences.putString("companyId", memberInfo.getCompanyId());
            Arad.preferences.putString("communityName", memberInfo.getCommunityName());
            Arad.preferences.putString("communityId",memberInfo.getCommunityId());
            Arad.preferences.putString("isShielded",memberInfo.getIsShielded());
            Arad.preferences.putString("latitude", latitude);
            Arad.preferences.putString("lontitude", lontitude);
            Arad.preferences.putString("address", address);
            Arad.preferences.flush();

            setAlias();

            Data data= ldao.getData();
            if (data!=null){
                int handleDay = data.getHandleDay();
                if (handleDay==0){
                    Intent intent02=new Intent(this,MainActivity.class);
                    intent02.putExtra("listNagationBar", (Serializable) ldao.getListNavigationBar());
                    startActivity(intent02);
                    AnimUtil.intentSlidIn(this);
                }else{
                   MessageUtils.showShortToast(this,"您的设备已被拉黑"+handleDay+"天");
                }
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        if(errorNo.equals("002")){
            MessageUtils.showShortToast(this, "账号或密码错误");
        }
    }

    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    MyApplication.getInstance().exit();
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_register :
                Intent intent01=new Intent(this,RegisterActivity.class);
                startActivity(intent01);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.tv_login :
                if(checkInput()){
                    ldao.requestLogin(et_username.getText().toString().trim(), et_pass_word.getText().toString().trim(),deviceImei, "1");
                }
                break;
            case R.id.tv_forget :
                startActivity(new Intent(this, MineUpdatepwdActivity.class));
                break;
            case R.id.iv_weibo :
                authorize(new SinaWeibo(this));
                break;
            case R.id.iv_qqzone :
                authorize(new QQ(this));
                break;
            case R.id.iv_weixin :
                authorize(new Wechat(this));
                break;
            case R.id.ll_all:
                InputMethodUtils.hide(LoginActivity.this);
                break;
        }

    }

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final String TAG = "JPush";
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

           // ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
            //ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

    private void setAlias(){
        String alias = Arad.preferences.getString("memberId");
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            Toast.makeText(LoginActivity.this,"格式不对", Toast.LENGTH_SHORT).show();
            return;
        }
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private void authorize(Platform plat) {
        if(plat.isValid()) {
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                UIHandler.sendEmptyMessage(MSG_USERID_FOUND,this);
                login(plat.getName(), userId, null);
                return;
            }
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(true);
        plat.showUser(null);
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
            login(platform.getName(), platform.getDb().getUserId(), res);
        }
        System.out.println(res);
        System.out.println("------User Name ---------" + platform.getDb().getUserName());
        System.out.println("------User ID ---------" + platform.getDb().getUserId());
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    private void login(String plat, String userId, HashMap<String, Object> userInfo) {
        Message msg = new Message();
        msg.what = MSG_LOGIN;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }
    public boolean handleMessage(Message msg) {
        switch(msg.what) {
            case MSG_USERID_FOUND: {
                Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_LOGIN: {

                String text = getString(R.string.logining, msg.obj);
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                System.out.println("---------------");

//				Builder builder = new Builder(this);
//				builder.setTitle(R.string.if_register_needed);
//				builder.setMessage(R.string.after_auth);
//				builder.setPositiveButton(R.string.ok, null);
//				builder.create().show();
            }
            break;
            case MSG_AUTH_CANCEL: {
                Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
                System.out.println("-------MSG_AUTH_CANCEL--------");
            }
            break;
            case MSG_AUTH_ERROR: {
                Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
                System.out.println("-------MSG_AUTH_ERROR--------");
            }
            break;
            case MSG_AUTH_COMPLETE: {
                Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
                System.out.println("--------MSG_AUTH_COMPLETE-------");
            }
            break;
        }
        return false;
    }

    private boolean checkInput(){
          if(TextUtils.isEmpty(et_username.getText().toString().trim())){
              MessageUtils.showShortToast(this,"用户名不可为空");
              return false;
          }
          if(TextUtils.isEmpty(et_pass_word.getText().toString().trim())){
              MessageUtils.showShortToast(this,"密码不可为空");
              return false;
          }

        return true;
    }




}
