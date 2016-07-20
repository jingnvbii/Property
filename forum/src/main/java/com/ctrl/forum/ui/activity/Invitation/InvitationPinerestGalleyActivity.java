package com.ctrl.forum.ui.activity.Invitation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.AndroidUtil;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.ShareDialog;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.entity.Post2;
import com.ctrl.forum.entity.PostImage;
import com.ctrl.forum.photo.zoom.PhotoView;
import com.ctrl.forum.photo.zoom.ViewPagerFixed;
import com.ctrl.forum.ui.activity.LoginActivity;
import com.ctrl.forum.utils.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * 这个是用于瀑布流影集样式进行图片浏览时的界面
 *
 * @author king
 * @version 2014年10月18日  下午11:47:53
 * @QQ:595163260
 */
public class InvitationPinerestGalleyActivity extends ToolBarActivity implements View.OnClickListener ,PlatformActionListener{
    @InjectView(R.id.tv_image_remark)//照片备注
    TextView tv_image_remark;
    @InjectView(R.id.tv_image_tel)//电话连接
    TextView tv_image_tel;
    @InjectView(R.id.tv_pinerest_gallery_zan_num)//赞数
    TextView tv_pinerest_gallery_zan_num;
    @InjectView(R.id.iv_gallery_back)//返回按钮
    ImageView iv_gallery_back;
    @InjectView(R.id.iv_zan)//点赞图片
    ImageView iv_zan;


    @InjectView(R.id.rl_pinerest_gallery_zan)//点赞布局
    RelativeLayout rl_pinerest_gallery_zan;
    @InjectView(R.id.iv_pinerest_gallery_share)//分享
    ImageView iv_pinerest_gallery_share;
    @InjectView(R.id.iv_pinerest_gallery_pinglun)//评论
    ImageView iv_pinerest_gallery_pinglun;

    //获取前一个activity传过来的position
    private int position;
    //当前的位置
    private int location = 0;

    private ArrayList<View> listViews = null;
    private ArrayList<View> listViews2 = null;
    private ArrayList<String> listImageUrl;
    private String id;
    private InvitationDao idao;
    private Post2 post;
    private MemberInfo user;
    private ViewPagerFixed pager;
    private MyPageAdapter adapter;
    private PopupWindow popupWindow;
    private TextView tv_titile;
    private int count;

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int arg0 = (int) msg.obj;
                    tv_titile.setText((arg0 + 1) + "/" + listViews.size());
                 //   tv_image_remark.setText(post.getPostImgList().get(arg0).getRemark());
                    break;

                case 2:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    BitmapUtils.saveImageToGallery(InvitationPinerestGalleyActivity.this,bitmap);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private List<PostImage> listPostImage;
    private PopupWindow popupWindow_share;
    private int praiseNum;
    private ShareDialog shareDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_pinerest_gallery);// 切屏到主界面
        ButterKnife.inject(this);
        initView();
        initData();


    }

    private void initData() {
        id = getIntent().getStringExtra("id");
        idao = new InvitationDao(this);
        idao.requesPostDetail(id, Arad.preferences.getString("memberId"));
    }

    private void initView() {
        ShareSDK.initSDK(this);
        count=0;
        tv_image_tel.setOnClickListener(this);
        tv_titile = (TextView) findViewById(R.id.tv_titile);
        iv_gallery_back.setOnClickListener(this);

        rl_pinerest_gallery_zan.setOnClickListener(this);
        iv_pinerest_gallery_share.setOnClickListener(this);
        iv_pinerest_gallery_pinglun.setOnClickListener(this);

        // 为发送按钮设置文字
        pager = (ViewPagerFixed) findViewById(R.id.gallery01);
        pager.setOnPageChangeListener(pageChangeListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK();
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 14) {
            setResult(203);
            if (count % 2 == 0) {//奇数次点击
                if (post.getZambiastate().equals("0")) {
                    //  MessageUtils.showShortToast(this, "取消点赞成功");
                    praiseNum=post.getPraiseNum()+1;
                    iv_zan.setImageResource(R.mipmap.shoucang_white);
                    tv_pinerest_gallery_zan_num.setText(praiseNum + "");


                }
                if (post.getZambiastate().equals("1")) {
                    //MessageUtils.showShortToast(this, "点赞成功");
                    iv_zan.setImageResource(R.mipmap.zan_white);
                    praiseNum=post.getPraiseNum()-1;
                    tv_pinerest_gallery_zan_num.setText(praiseNum+"");

                }
            }
            if (count % 2 == 1) {//偶数次点击
                if (post.getZambiastate().equals("0")) {
                    iv_zan.setImageResource(R.mipmap.zan_white);
                    //  MessageUtils.showShortToast(this, "点赞成功");
                    praiseNum=post.getPraiseNum();
                    tv_pinerest_gallery_zan_num.setText(praiseNum+"");

                }
                if (post.getZambiastate().equals("1")) {
                    //  MessageUtils.showShortToast(this, "取消点赞成功");
                    iv_zan.setImageResource(R.mipmap.shoucang_white);

                    praiseNum=post.getPraiseNum();
                    tv_pinerest_gallery_zan_num.setText(praiseNum+"");
                }
            }
            count++;
        }

        if (requestCode == 3) {
            post = idao.getPost2();
            listPostImage=idao.getListPostImage();
            user = idao.getUser();
            listViews = new ArrayList<>();
           // listViews2 = new ArrayList<>();
           if(listPostImage==null){
               ImageView view = new ImageView(this);
               // TextView view2=new TextView(this);
              view.setImageResource(R.mipmap.default_error);
               // view2.setText(post.getPostImgList().get(i).getRemark());
               view.setScaleType(ImageView.ScaleType.FIT_XY);
               listViews.add(view);
               tv_titile.setVisibility(View.GONE);
           }else {
               for (int i = 0; i < listPostImage.size(); i++) {
                   ImageView view = new ImageView(this);
                   // TextView view2=new TextView(this);
                   Arad.imageLoader.load(listPostImage.get(i).getImg()).placeholder(R.mipmap.default_error).into(view);
                   // view2.setText(post.getPostImgList().get(i).getRemark());
                   view.setScaleType(ImageView.ScaleType.FIT_XY);
                   listViews.add(view);
                   //  listViews2.add(view2);
               }
           }

            if(post.getZambiastate().equals("0")){
                iv_zan.setImageResource(R.mipmap.zan_white);
            }
            if(post.getZambiastate().equals("1")){
                iv_zan.setImageResource(R.mipmap.shoucang_white);
            }
            tv_titile.setText(1 + "/" + listViews.size());
            adapter = new MyPageAdapter(listViews);
            pager.setAdapter(adapter);
            praiseNum=post.getPraiseNum();
            tv_pinerest_gallery_zan_num.setText(praiseNum+"");
        }
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            location = arg0;
          //  Toast.makeText(InvitationPinerestGalleyActivity.this, "" + arg0, Toast.LENGTH_SHORT).show();
            Message message = myHandler.obtainMessage();
            message.what = 1;
            message.obj = arg0;
            myHandler.sendMessage(message);
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void initListViews(Bitmap bm) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        PhotoView img = new PhotoView(this);
        img.setBackgroundColor(0xff000000);
        img.setImageBitmap(bm);
        img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        listViews.add(img);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_image_tel:
                showPupupWindow();
                break;
            case R.id.rl_pinerest_gallery_zan:
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationPinerestGalleyActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationPinerestGalleyActivity.this);
                    return;
                }
                if (count % 2 == 0) {//奇数次点击
                    if (post.getZambiastate().equals("0")) {
                        idao.requesZambia("add", post.getId(), Arad.preferences.getString("memberId"), "", "");
                    }
                    if (post.getZambiastate().equals("1")) {
                        idao.requesZambia("reduce", post.getId(), Arad.preferences.getString("memberId"), "", "");
                    }
                }
                if (count % 2 == 1) {//偶数次点击
                    if (post.getZambiastate().equals("0")) {
                        idao.requesZambia("reduce", post.getId(), Arad.preferences.getString("memberId"), "", "");
                    }
                    if (post.getZambiastate().equals("1")) {
                        idao.requesZambia("add", post.getId(), Arad.preferences.getString("memberId"), "", "");
                    }
                }

                break;
            case R.id.iv_pinerest_gallery_share:
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationPinerestGalleyActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationPinerestGalleyActivity.this);
                    return;
                }
              //  showSharePopuwindow(iv_pinerest_gallery_share);
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
                        sp.setTitle("烟台项目");
                        sp.setText("欢迎加入");

                        sp.setImageUrl(getIntent().getStringExtra("qrImgUrl"));//网络图片rul
                        sp.setTitleUrl(getIntent().getStringExtra("qrImgUrl"));  //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform qq = ShareSDK.getPlatform(QQ.NAME);
                        qq.setPlatformActionListener(InvitationPinerestGalleyActivity.this); // 设置分享事件回调
                        // 执行分享
                        qq.share(sp);
                    }

                });

                shareDialog.setWeixinButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                        sp.setTitle("烟台项目");  //分享标题
                        sp.setText("欢迎加入");   //分享文本
                        sp.setImageUrl(getIntent().getStringExtra("qrImgUrl"));//网络图片rul
                        sp.setUrl(getIntent().getStringExtra("qrImgUrl"));   //网友点进链接后，可以看到分享的详情

                        //3、非常重要：获取平台对象
                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        wechat.setPlatformActionListener(InvitationPinerestGalleyActivity.this); // 设置分享事件回调
                        // 执行分享
                        wechat.share(sp);
                    }
                });
                shareDialog.setSinaWeiBoButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setText("我是新浪微博分享文本，啦啦啦~http://uestcbmi.com/"); //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        //3、非常重要：获取平台对象
                        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                        sinaWeibo.setPlatformActionListener(InvitationPinerestGalleyActivity.this); // 设置分享事件回调
                        // 执行分享
                        sinaWeibo.share(sp);
                    }
                });

                shareDialog.setPengYouQuanButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                        sp.setTitle("我是朋友圈分享标题");  //分享标题
                        sp.setText("我是朋友圈分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                        wechatMoments.setPlatformActionListener(InvitationPinerestGalleyActivity.this); // 设置分享事件回调
                        // 执行分享
                        wechatMoments.share(sp);
                    }
                });
                shareDialog.setTecentWeiBoButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle("我是腾讯微博分享标题");  //分享标题
                        sp.setText("我是腾讯微博分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform tecentWeibo = ShareSDK.getPlatform(TencentWeibo.NAME);
                        tecentWeibo.setPlatformActionListener(InvitationPinerestGalleyActivity.this); // 设置分享事件回调
                        // 执行分享
                        tecentWeibo.share(sp);
                    }
                });

                shareDialog.setEmailButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle("我是邮件分享标题");  //分享标题
                        sp.setText("我是邮件分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform emailName = ShareSDK.getPlatform(Email.NAME);
                        emailName.setPlatformActionListener(InvitationPinerestGalleyActivity.this); // 设置分享事件回调
                        // 执行分享
                        emailName.share(sp);
                    }
                });
                shareDialog.setDuanXinButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle("我是短信分享标题");  //分享标题
                        sp.setText("我是短信分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform shortMessage = ShareSDK.getPlatform(ShortMessage.NAME);
                        shortMessage.setPlatformActionListener(InvitationPinerestGalleyActivity.this); // 设置分享事件回调
                        // 执行分享
                        shortMessage.share(sp);
                    }
                });
                break;
            case R.id.iv_pinerest_gallery_pinglun:
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationPinerestGalleyActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationPinerestGalleyActivity.this);
                    return;
                }
                Intent intent=new Intent(InvitationPinerestGalleyActivity.this,InvitationCommentDetaioActivity.class);
                intent.putExtra("id",post.getId());
                intent.putExtra("reportid",post.getReporterId());
                startActivity(intent);
                break;
            case R.id.iv_gallery_back:
                onBackPressed();
                break;
        }
    }

    private void showSharePopuwindow(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.share_dialog, null);

        popupWindow_share = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置SelectPicPopupWindow的View
        popupWindow_share.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow_share.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow_share.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow_share.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow_share.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x90000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow_share.setBackgroundDrawable(dw);

        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        popupWindow_share.setTouchable(true);
         /*
        * 设置popupwindow 点击自身消失
        * */
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow_share.isShowing()) {
                    popupWindow_share.dismiss();
                }
            }
        });

        popupWindow_share.setOutsideTouchable(true);

        popupWindow_share.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 设置好参数之后再show
        popupWindow_share.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void showPupupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_tel, null);
        final TextView tv_iamge_phone_num = (TextView)contentView.findViewById(R.id.tv_iamge_phone_num);//电话号码
        TextView tv_image_dial = (TextView)contentView.findViewById(R.id.tv_image_dial);//拨打电话
        TextView tv_image_tel_cancel = (TextView)contentView.findViewById(R.id.tv_image_tel_cancel);//取消

        tv_iamge_phone_num.setText(user.getMobile());
        tv_image_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtil.dial(InvitationPinerestGalleyActivity.this,tv_iamge_phone_num.getText().toString().trim());
            }
        });

        popupWindow = new PopupWindow(contentView,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(dw);

        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        popupWindow.setTouchable(true);
         /*
        * 设置popupwindow 点击自身消失
        * */
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        popupWindow.setOutsideTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 设置好参数之后再show
        popupWindow.showAtLocation(InvitationPinerestGalleyActivity.this.findViewById(R.id.framelayout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }


    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;

        private int size;

        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, final int arg1) {
            try {
                ((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

                listViews.get(arg1 % size).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showDialog(arg1);
                        return true;
                    }
                });


            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        private void showDialog(final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(InvitationPinerestGalleyActivity.this);
            builder.setMessage("保存到本地");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap mBitmap=  getHttpBitmap(listPostImage.get(position).getImg());
                            Message message=myHandler.obtainMessage();
                            message.obj=mBitmap;
                            message.what=2;
                            myHandler.sendMessage(message);

                        }
                    }).start();
                }
            });
            builder.show();
        }




        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    public Bitmap getHttpBitmap(String url)
    {
        Bitmap bitmap = null;
        try
        {
            URL pictureUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) pictureUrl.openConnection();
            InputStream in = con.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(QQ.NAME)) {// 判断成功的平台是不是QQ
            handler.sendEmptyMessage(1);
        } else if (platform.getName().equals(Wechat.NAME)) {//判断成功的平台是不是微信
            handler.sendEmptyMessage(2);
        }else if(platform.getName().equals(SinaWeibo.NAME)){//判断成功的平台是不是新浪微博
            handler.sendEmptyMessage(3);
        }else if(platform.getName().equals(WechatMoments.NAME)){//判断成功平台是不是微信朋友圈
            handler.sendEmptyMessage(4);
        }else if(platform.getName().equals(TencentWeibo.NAME)){//判断成功平台是不是腾讯微博
            handler.sendEmptyMessage(5);
        }else if(platform.getName().equals(Email.NAME)){//判断成功平台是不是邮件
            handler.sendEmptyMessage(6);
        }else if(platform.getName().equals(ShortMessage.NAME)){//判断成功平台是不是短信
            handler.sendEmptyMessage(7);
        }else {
            //
        }

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
        Message msg = new Message();
        msg.what = 8;
        msg.obj = throwable.getMessage();
        handler.sendMessage(msg);

    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(9);
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
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 7:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 8:
                    Toast.makeText(getApplicationContext(), "分享失败啊", Toast.LENGTH_LONG).show();
                    break;
                case 9:
                    Toast.makeText(getApplicationContext(), "已取消", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }

    };


}
