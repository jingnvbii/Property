package com.ctrl.forum.ui.activity.Invitation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.customview.ShareDialog;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 帖子列表  瀑布流详情页 activity
 * Created by Administrator on 2016/4/11.
 */
public class InvitationPinterestDetailActivity extends AppToolBarActivity implements View.OnClickListener ,PlatformActionListener {
      @InjectView(R.id.iv_share)//分享
      ImageView iv_share;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_invitation_pinterest_detail);
       ButterKnife.inject(this);
       initView();
       ShareSDK.initSDK(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK();
    }

    private void initView() {
        iv_share.setOnClickListener(this);
    }


    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.jiantou_left_white);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {
        TextView tv_title = getmTitle();
        tv_title.setTextColor(getResources().getColor(R.color.text_black));
        return "帖子列表";
    }


    @Override
    public boolean setupToolBarRightButton(ImageView rightButton) {
        rightButton.setImageResource(R.mipmap.edit);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent_edit=new Intent(InvitationPinterestDetailActivity.this,ImageDetailActivity.class);
                startActivity(intent_edit);
                AnimUtil.intentSlidIn(InvitationPinterestDetailActivity.this);*/
            }
        });
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_share:
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
                      qq.setPlatformActionListener(InvitationPinterestDetailActivity.this); // 设置分享事件回调
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
                        wechat.setPlatformActionListener(InvitationPinterestDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        wechat.share(sp);
                    }
                });


                break;
        }

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
