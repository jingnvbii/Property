package com.ctrl.android.yinfeng;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.base.MyApplication;
import com.ctrl.android.yinfeng.customview.SlideShowView;
import com.ctrl.android.yinfeng.dao.AdvertisingDao;
import com.ctrl.android.yinfeng.dao.NoticeDao;
import com.ctrl.android.yinfeng.entity.Advertising;
import com.ctrl.android.yinfeng.ui.adressbook.tree.TreeListActivity;
import com.ctrl.android.yinfeng.ui.cpatrol.CPatrolActivity;
import com.ctrl.android.yinfeng.ui.ereport.EReportActivity;
import com.ctrl.android.yinfeng.ui.gpatrol.GPatrolActivity;
import com.ctrl.android.yinfeng.ui.job.MyJobActivity;
import com.ctrl.android.yinfeng.ui.notice.NoticeListActivity;
import com.ctrl.android.yinfeng.ui.visit.VisitProruptionActivity;
import com.ctrl.android.yinfeng.utils.S;

import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class MainActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.tv_gonggao)//社区公告
    TextView tv_gonggao;
    @InjectView(R.id.tv_gongdan)//我的工单
    TextView tv_gongdan;
    @InjectView(R.id.tv_xuncha)//巡查
    TextView tv_xuncha;
    @InjectView(R.id.tv_xungeng)//巡更
    TextView tv_xungeng;
    @InjectView(R.id.tv_shangbao)//事件上报
    TextView tv_shangbao;
    @InjectView(R.id.tv_fangke)//访客通行
    TextView tv_fangke;
    @InjectView(R.id.tv_wenda)//医疗问答
    TextView tv_wenda;
    @InjectView(R.id.tv_tongxun)//内部通讯录
    TextView tv_tongxun;
   /* @InjectView(R.id.iv_main)//广告位图片
    ImageView iv_main;*/

    @InjectView(R.id.viewpager_advertising)//广告位图片轮播
    SlideShowView viewpager_advertising;



    private NoticeDao dao;

    private long waitTime = 2000;
    private long touchTime = 0;
    public static final int  MAIN_ACTIVITY=101;
    private AdvertisingDao adao;
    private static  final String STF_HOME_TOP="STF_HOME_TOP";
    private List<Advertising> adverstingList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        init();
        dao.requestNoticeUnreadCount(Arad.preferences.getString("communityId"), Arad.preferences.getString("staffId"));
        adao.requestAdvertising(STF_HOME_TOP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //避免重复请求未读消息数量接口
        if (MyApplication.isNotceActivity) {
            dao.requestNoticeUnreadCount(Arad.preferences.getString("communityId"), Arad.preferences.getString("staffId"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(MyApplication.isNotceActivity) {
            MyApplication.isNotceActivity = false;
        }
    }

    private void init() {
        //设置极光推送别名
        setAlias();

        tv_gonggao.setOnClickListener(this);
        tv_gongdan.setOnClickListener(this);
        tv_xuncha.setOnClickListener(this);
        tv_xungeng.setOnClickListener(this);
        tv_fangke.setOnClickListener(this);
        tv_wenda.setOnClickListener(this);
        tv_tongxun.setOnClickListener(this);
        tv_shangbao.setOnClickListener(this);

        dao=new NoticeDao(this);

        adao=new AdvertisingDao(this);


        WindowManager wm=(WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        int width=wm.getDefaultDisplay().getWidth();

        int height=wm.getDefaultDisplay().getHeight();
        Log.i("height","height:"+height);
        Log.i("width", "width:" + width);
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) viewpager_advertising.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = height>>2;// 控件的高强制设成屏幕宽度的一半
        linearParams.width = width;
        Log.i("height", "height:" + width/2);
        viewpager_advertising.setLayoutParams(linearParams); //使设置好的布局参数应用到控件</pre>
        //tv_gonggao.setText("社区公告"+"("+10000+")");
        //dao.requestNoticeUnreadCount(Arad.preferences.getString("communityId"), Arad.preferences.getString("staffId"));
    }


    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias() {
        String alias = Arad.preferences.getString("staffId");

        if(S.isNull(alias)){
            MessageUtils.showShortToast(this,"别名为空");
        }else {

            // 调用 Handler 来异步设置别名
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
        }
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
           // MessageUtils.showShortToast(MainActivity.this,logs);
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private String TAG;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };




    @Override
    public String setupToolBarTitle() {
        return "银丰物业";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            } else {
                finish();
                //System.exit(0);
                MyApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(3==requestCode){
            if(dao.getCount()==0){
                tv_gonggao.setText("社区公告");
            }else {
                tv_gonggao.setText("社区公告" + "(" + dao.getCount() + ")");
            }
        }

        if(666==requestCode){
            adverstingList=adao.getAdvertisingList();
            viewpager_advertising.setAdverstingList(adverstingList);
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        if(errorNo.equals("002")){
            ImageView view =  new ImageView(this);
            view.setBackgroundResource(R.mipmap.pic3);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            viewpager_advertising.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_gonggao :
                Intent intent=new Intent(MainActivity.this, NoticeListActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(MainActivity.this);
                break;
            case R.id.tv_gongdan :
                startActivity(new Intent(MainActivity.this, MyJobActivity.class));
                AnimUtil.intentSlidIn(MainActivity.this);
                break;
            case R.id.tv_xuncha :
                startActivity(new Intent(MainActivity.this, CPatrolActivity.class));
                AnimUtil.intentSlidIn(MainActivity.this);
                break;
            case R.id.tv_xungeng :
                startActivity(new Intent(MainActivity.this, GPatrolActivity.class));
                AnimUtil.intentSlidIn(MainActivity.this);
                break;
            case R.id.tv_shangbao :
                startActivity(new Intent(MainActivity.this, EReportActivity.class));
                AnimUtil.intentSlidIn(MainActivity.this);
                break;
            case R.id.tv_fangke :
                startActivity(new Intent(MainActivity.this, VisitProruptionActivity.class));
                AnimUtil.intentSlidIn(MainActivity.this);
                break;
            case R.id.tv_wenda :
                MessageUtils.showShortToast(this,"该功能暂未开放，敬请期待！");
                break;
            case R.id.tv_tongxun :
                startActivity(new Intent(MainActivity.this, TreeListActivity.class));
                AnimUtil.intentSlidIn(MainActivity.this);
                break;
        }

    }



}
