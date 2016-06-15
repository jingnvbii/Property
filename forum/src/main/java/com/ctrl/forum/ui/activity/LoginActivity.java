package com.ctrl.forum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.ctrl.forum.dao.LoginDao;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.ui.activity.mine.MineUpdatepwdActivity;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 登录 activity
 * Created by Eric on 2015/11/23.
 * */
public class LoginActivity extends AppToolBarActivity implements View.OnClickListener{

    private TextView tv_register;//注册按钮1
    private EditText et_username;//用户名
    private TextView et_pass_word;//密码
    private TextView tv_login;//登录
    private TextView tv_forget;//忘记密码
    private ImageView iv_weibo;//微博
    private ImageView iv_qqzone;//qq空间
    private ImageView iv_weixin;//微信
    private LoginDao ldao;
    private MemberInfo memberInfo;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private String latitude,lontitude;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.inject(this);
        initView();

        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数1
        initLocation();

        mLocationClient.start();

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
            //Log.i("BaiduLocationApiDem", sb.toString());
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

        ldao=new LoginDao(this);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==0){
            memberInfo=ldao.getMemberInfo();
            Arad.preferences.putString("memberId", memberInfo.getId());
            Arad.preferences.putString("nickName", memberInfo.getNickName());
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

            Arad.preferences.putString("latitude", latitude);
            Arad.preferences.putString("lontitude", lontitude);

            Arad.preferences.putString("address", address);
            Arad.preferences.flush();
          //  MessageUtils.showShortToast(this, "登录成功");
            Intent intent02=new Intent(this,MainActivity.class);
            intent02.putExtra("listNagationBar",(Serializable)ldao.getListNavigationBar());
            startActivity(intent02);
            AnimUtil.intentSlidIn(this);
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        if(errorNo.equals("002")){
            MessageUtils.showShortToast(this, "账号或密码错误");
        }

     /*   if(errorNo.equals("003")){
            MessageUtils.showShortToast(this,"登录成功");
            Intent intent02=new Intent(this,MainActivity.class);
            startActivity(intent02);
            AnimUtil.intentSlidIn(this);
        }*/


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_register :
                Intent intent01=new Intent(this,RegisterActivity.class);
                startActivity(intent01);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.tv_login :
                if(checkInput()){
                    ldao.requestLogin(et_username.getText().toString().trim(), et_pass_word.getText().toString().trim(), "1");
                    //没网的时候可以登陆
                    startActivity(new Intent(this,MainActivity.class));

                }
                break;
            case R.id.tv_forget :
                startActivity(new Intent(this, MineUpdatepwdActivity.class));
                break;
            case R.id.iv_weibo :
                break;
            case R.id.iv_qqzone :
                break;
            case R.id.iv_weixin :
                break;
        }

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
