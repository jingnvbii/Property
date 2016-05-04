package com.ctrl.android.yinfeng.ui.visit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.Arad;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.customview.ShareDialog;
import com.ctrl.android.yinfeng.customview.TestanroidpicActivity;
import com.ctrl.android.yinfeng.utils.TimeUtil;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class VisitOrderSucessActivity extends AppToolBarActivity implements View.OnClickListener, PlatformActionListener {
    @InjectView(R.id.tv_name)//到访人姓名
            TextView tv_name;
    @InjectView(R.id.tv_number)//通行码
            TextView tv_number;
    @InjectView(R.id.iv_qrImg)//二维码图片
            ImageView iv_qrImg;
    @InjectView(R.id.tv_arrivetime)//到访时间
            TextView tv_arrivetime;
    @InjectView(R.id.tv_send)//发送给访客
            TextView tv_send;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_order_sucess);
        ButterKnife.inject(this);
        ShareSDK.initSDK(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    private void init() {
        tv_send.setOnClickListener(this);
        iv_qrImg.setOnClickListener(this);
        if(getIntent().getStringExtra("gender").equals("0")) {
            tv_name.setText("尊敬的  " + getIntent().getStringExtra("visitorname")+"先生");
        }else {
            tv_name.setText("尊敬的  " + getIntent().getStringExtra("visitorname")+"女士");
        }
        tv_number.setText(getIntent().getStringExtra("visitNum"));
        Arad.imageLoader.load(getIntent().getStringExtra("qrImgUrl")).placeholder(R.mipmap.default_image).into(iv_qrImg);
        tv_arrivetime.setText("到访时间：" + TimeUtil.date(Long.parseLong(getIntent().getStringExtra("arriveTime"))));
    }


    @Override
    public String setupToolBarTitle() {
        return "通行证";
    }


    /**
     * header 左侧按钮
     */
    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_send:
                shareDialog = new ShareDialog(this);
                shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();
                    }
                });
                shareDialog.setQQButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle("银丰员工端");
                        sp.setText("欢迎加入银丰");

                        sp.setImageUrl(getIntent().getStringExtra("qrImgUrl"));//网络图片rul
                        sp.setTitleUrl(getIntent().getStringExtra("qrImgUrl"));  //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform qq = ShareSDK.getPlatform(QQ.NAME);
                        qq.setPlatformActionListener(VisitOrderSucessActivity.this); // 设置分享事件回调
                        // 执行分享
                        qq.share(sp);
                    }

                });

                shareDialog.setWeixinButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                        sp.setTitle("银丰员工端");  //分享标题
                        sp.setText("欢迎加入银丰");   //分享文本
                        sp.setImageUrl(getIntent().getStringExtra("qrImgUrl"));//网络图片rul
                        sp.setUrl(getIntent().getStringExtra("qrImgUrl"));   //网友点进链接后，可以看到分享的详情

                        //3、非常重要：获取平台对象
                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        wechat.setPlatformActionListener(VisitOrderSucessActivity.this); // 设置分享事件回调
                        // 执行分享
                        wechat.share(sp);
                    }
                });
                break;
            case R.id.iv_qrImg:
                scaleImage(getIntent().getStringExtra("qrImgUrl"),iv_qrImg,this);
                break;
        }

    }

    private void scaleImage(String url,ImageView iv,Activity activity){
        Intent intent=new Intent(activity, TestanroidpicActivity.class);
        intent.putExtra("imageurl", url);
        int[] location = new int[2];
        iv.getLocationOnScreen(location);
        intent.putExtra("locationX", location[0]);
        intent.putExtra("locationY", location[1]);
        intent.putExtra("width", iv.getWidth());
        intent.putExtra("height", iv.getHeight());
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(QQ.NAME)) {// 判断成功的平台是不是QQ
            handler.sendEmptyMessage(1);
        } else if (platform.getName().equals(Wechat.NAME)) {
            handler.sendEmptyMessage(2);
    }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
        Message msg = new Message();
        msg.what = 3;
        msg.obj = throwable.getMessage();
        handler.sendMessage(msg);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(4);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                  //  Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;

                case 2:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "分享失败啊", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "已取消", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }

    };
}
