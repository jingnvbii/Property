package com.ctrl.forum.ui.activity.Invitation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.customview.RoundImageView;
import com.ctrl.forum.customview.ShareDialog;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Post2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
      @InjectView(R.id.title_image)//用户头像
              RoundImageView title_image;
      @InjectView(R.id.tv_name)//发帖人昵称
      TextView tv_name;
    @InjectView(R.id.iv_levlel)//等级
    ImageView iv_levlel;
    @InjectView(R.id.tv_release_time)//发布时间
    TextView tv_release_time;
    @InjectView(R.id.tv_address)//发布地点
    TextView tv_address;
    @InjectView(R.id.tv_delete)//删除本帖
    TextView tv_delete;
    @InjectView(R.id.tv_introduction)//导语
    TextView tv_introduction;
    @InjectView(R.id.tv_tel)//电话
    TextView tv_tel;
   @InjectView(R.id.ll_tel)//拨打电话
    LinearLayout ll_tel;
    @InjectView(R.id.tv_content)//内容
    TextView tv_content;
    @InjectView(R.id.iv01)//图片
    ImageView iv01;
    @InjectView(R.id.iv02)//图片
    ImageView iv02;
    @InjectView(R.id.iv03)//图片
    ImageView iv03;
    @InjectView(R.id.iv04)//图片
    ImageView iv041;
    @InjectView(R.id.iv05)//图片
    ImageView iv05;
    @InjectView(R.id.iv06)//图片
    ImageView iv06;
    @InjectView(R.id.iv07)//图片
    ImageView iv07;
    @InjectView(R.id.iv08)//图片
    ImageView iv08;
    @InjectView(R.id.iv09)//图片
    ImageView iv09;
    @InjectView(R.id.rl_share)//分享
    RelativeLayout rl_share;
    @InjectView(R.id.rl_pinglun)//评论
    RelativeLayout rl_pinglun;
    @InjectView(R.id.rl_zan)//点赞
    RelativeLayout rl_zan;
    @InjectView(R.id.lv_detail)//评论列表
    PullToRefreshListView lv_detail;

    private ShareDialog shareDialog;
    private InvitationDao idao;
    private Post2 post;

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
        tv_delete.setOnClickListener(this);
        idao=new InvitationDao(this);
        idao.requesPostDetail(getIntent().getStringExtra("id"));
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==8){
            MessageUtils.showShortToast(this,"删除帖子成功");
            finish();
        }
        if(requestCode == 3){
            MessageUtils.showShortToast(this, "获取帖子详情成功");
            post=idao.getPost2();
            Arad.imageLoader.load(idao.getUser().getImgUrl()).placeholder(R.mipmap.round_img).into(title_image);
            tv_name.setText(idao.getUser().getNickName());
          /*  switch (idao.getUser().getMemberLevel()){
                case "1":
                    iv_levlel.setImageResource(R.mipmap.vip_icon1);
                    break;
                case "2":
                    iv_levlel.setImageResource(R.mipmap.vip_icon2);
                    break;
                case "3":
                    iv_levlel.setImageResource(R.mipmap.vip_icon3);
                    break;
                case "4":
                    iv_levlel.setImageResource(R.mipmap.vip_icon4);
                    break;
                case "5":
                    iv_levlel.setImageResource(R.mipmap.vip_icon5);
                    break;
                case "6":
                    iv_levlel.setImageResource(R.mipmap.vip_icon6);
                    break;
                case "7":
                    iv_levlel.setImageResource(R.mipmap.vip_icon7);
                    break;
            }*/
            tv_release_time.setText(post.getPublishTime());
            tv_address.setText(idao.getUser().getAddress());
            tv_introduction.setText(post.getBlurbs());
            tv_tel.setText(idao.getUser().getMobile());
            tv_content.setText(post.getContent());
           /* switch (post.getPostImgList().size()){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;


            }*/



        }
    }



    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.jiantou_left);
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
        return "帖子详情";
    }


    @Override
    public boolean setupToolBarRightButton(ImageView rightButton) {
        rightButton.setImageResource(R.mipmap.more_shixin);
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
            case R.id.tv_delete:
                idao.requesDeltePost(getIntent().getStringExtra("id"));
                break;
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
