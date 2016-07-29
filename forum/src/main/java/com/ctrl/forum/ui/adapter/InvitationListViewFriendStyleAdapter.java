package com.ctrl.forum.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.GridViewForScrollView;
import com.ctrl.forum.customview.ImageZoomActivity;
import com.ctrl.forum.customview.ListViewForScrollView;
import com.ctrl.forum.customview.RoundImageView;
import com.ctrl.forum.customview.ShareDialog;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.PostImage;
import com.ctrl.forum.ui.activity.Invitation.InvitationCommentDetaioActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationPullDownActivity;
import com.ctrl.forum.ui.activity.LoginActivity;
import com.ctrl.forum.utils.SysUtils;
import com.ctrl.forum.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 帖子朋友圈列表样式adapter  adapter
 * Created by jason on 2016/4/8.
 */
public class InvitationListViewFriendStyleAdapter extends BaseAdapter {
    private Activity mcontext;
    private List<Post> mPostList;
    private List<PostImage> mPostImageList;
    private LayoutInflater inflter;

    private int wh;

    private List<PostImage> imageList;
    private FriendGridAdapter friendInfoImgsAdapter;
    private PopupWindow popupWindow;
    private PopupWindow popupWindow_share;
    private ShareDialog shareDialog;
    private InvitationPullDownActivity activity;
    private OnItemClickListener mOnItemClickListener;


    public InvitationListViewFriendStyleAdapter(Activity context) {
        this.mcontext = context;
        this.wh = (SysUtils.getScreenWidth(mcontext)- SysUtils.Dp2Px(mcontext, 99)) /3;
    }

    public void setList(List<Post> list) {
        this.mPostList = list;
        notifyDataSetChanged();
    }

    //定义接口
    public interface OnItemClickListener{
        void onItemZanClick(ViewHolder v);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener ;
    }


    @Override
    public int getCount() {
        return mPostList == null ? 0 : mPostList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPostList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        int type = getItemViewType(position);
        if (convertView == null) {
            inflter = LayoutInflater.from(mcontext);
            convertView = inflter.inflate(R.layout.item_invitation_friend_style, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Post post = mPostList.get(position);
        if(post.getContent()==null||post.getContent().equals("")){
            holder.tv_friend_style_content.setVisibility(View.GONE);
        }
        holder.tv_friend_style_content.setText(post.getContent());
        holder.tv_friend_style_name.setText(post.getMemberName());
        holder.tv_friend_style_time.setText(TimeUtils.dateTime(post.getPublishTime())+"   "+(post.getLocationName()==null?"":post.getLocationName()));
        holder.tv_friend_style_zan_num.setText(post.getPraiseNum()+"");
        holder.tv_friend_style_pinglun_num.setText(post.getCommentNum()+"");
        holder.tv_friend_style_share_num.setText(post.getShareNum() + "");
        if(post.getMemberLevel()!=null){
            switch (post.getMemberLevel()){
                case "0":
                    holder.iv_friend_style_levlel.setImageResource(R.mipmap.vip_icon);
                    break;
                case "1":
                    holder.iv_friend_style_levlel.setImageResource(R.mipmap.vip_icon1);
                    break;
                case "2":
                    holder.iv_friend_style_levlel.setImageResource(R.mipmap.vip_icon2);
                    break;
                case "3":
                    holder.iv_friend_style_levlel.setImageResource(R.mipmap.vip_icon3);
                    break;
                case "4":
                    holder.iv_friend_style_levlel.setImageResource(R.mipmap.vip_icon4);
                    break;
                case "5":
                    holder.iv_friend_style_levlel.setImageResource(R.mipmap.vip_icon5);
                    break;
                case "6":
                    holder.iv_friend_style_levlel.setImageResource(R.mipmap.vip_icon6);
                    break;
                case "7":
                    holder.iv_friend_style_levlel.setImageResource(R.mipmap.vip_icon7);
                    break;
            }
        }else {
            holder.iv_friend_style_levlel.setImageResource(R.mipmap.vip_icon);
        }
        Arad.imageLoader.load(post.getImgUrl()).placeholder(R.mipmap.default_error).resize(300,300).centerCrop().into(holder.iv_friend_style_title_photo);
        if(post.getPostReplyList()!=null) {
            holder.lv_friend_style_reply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                        mcontext.startActivity(new Intent(mcontext, LoginActivity.class));
                        AnimUtil.intentSlidOut((InvitationPullDownActivity)mcontext);
                        return;
                    }
                    Intent intent=new Intent(mcontext, InvitationCommentDetaioActivity.class);
                    intent.putExtra("id",post.getId());
                    intent.putExtra("reportid",post.getReporterId());
                    mcontext.startActivity(intent);
                    AnimUtil.intentSlidIn(mcontext);
                }
            });
            if (post.getPostReplyList().size() <= 3) {
                holder.lv_friend_style_reply.setVisibility(View.VISIBLE);
                holder.tv_pinglun_title.setVisibility(View.VISIBLE);
                FriendStyleRelpyAdapter adapter = new FriendStyleRelpyAdapter(mcontext);
                adapter.setList(post.getPostReplyList());
                holder.lv_friend_style_reply.setAdapter(adapter);
                holder.tv_friend_style_shengyu_pinglun.setVisibility(View.VISIBLE);
                holder.tv_friend_style_shengyu_pinglun.setText("查看其他 "+(post.getCommentNum()-post.getPostReplyList().size())+"条 评论...");
            }
        }else {
            holder.tv_friend_style_shengyu_pinglun.setVisibility(View.GONE);
            holder.lv_friend_style_reply.setVisibility(View.GONE);
            holder.tv_pinglun_title.setVisibility(View.GONE);
        }


        // 是否含有图片
        if (post.getPostImgList() != null) {
            holder.rl4.setVisibility(View.VISIBLE);
            initInfoImages(holder.gv_images,post.getPostImgList());
        } else {
            holder.rl4.setVisibility(View.GONE);
        }
        
        holder.rl_friend_style_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog(v, position, post);

            }
        });

        holder.rl_friend_style_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    mcontext.startActivity(new Intent(mcontext, LoginActivity.class));
                    AnimUtil.intentSlidOut((InvitationPullDownActivity)mcontext);
                    return;
                }
             //   showShareDialog(v);
                activity = (InvitationPullDownActivity) mcontext;
                shareDialog = new ShareDialog(mcontext);
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

                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setTitleUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");  //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform qq = ShareSDK.getPlatform(QQ.NAME);
                        qq.setPlatformActionListener(activity); // 设置分享事件回调
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
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");   //网友点进链接后，可以看到分享的详情

                        //3、非常重要：获取平台对象
                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        wechat.setPlatformActionListener(activity); // 设置分享事件回调
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
                        sinaWeibo.setPlatformActionListener(activity); // 设置分享事件回调
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
                        wechatMoments.setPlatformActionListener(activity); // 设置分享事件回调
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
                        tecentWeibo.setPlatformActionListener(activity); // 设置分享事件回调
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
                        emailName.setPlatformActionListener(activity); // 设置分享事件回调
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
                        shortMessage.setPlatformActionListener(activity); // 设置分享事件回调
                        // 执行分享
                        shortMessage.share(sp);
                    }
                });
            }
        });
        holder.rl_friend_style_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    mcontext.startActivity(new Intent(mcontext, LoginActivity.class));
                    AnimUtil.intentSlidOut((InvitationPullDownActivity)mcontext);
                    return;
                }
                Intent intent=new Intent(mcontext, InvitationCommentDetaioActivity.class);
                intent.putExtra("id",post.getId());
                intent.putExtra("reportid",post.getReporterId());
                mcontext.startActivity(intent);
                AnimUtil.intentSlidIn(mcontext);
            }
        });
        holder.tv_friend_style_shengyu_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    mcontext.startActivity(new Intent(mcontext, LoginActivity.class));
                    AnimUtil.intentSlidOut((InvitationPullDownActivity)mcontext);
                    return;
                }
                Intent intent=new Intent(mcontext, InvitationCommentDetaioActivity.class);
                intent.putExtra("id",post.getId());
                intent.putExtra("reportid",post.getReporterId());
                mcontext.startActivity(intent);
                AnimUtil.intentSlidIn(mcontext);
            }
        });

        if(post.getPraiseState()!=null) {

            if (post.getPraiseState().equals("0")) {
                holder.iv_friend_style_zan_num.setImageResource(R.mipmap.zan_gray_kong);
            }
            if (post.getPraiseState().equals("1")) {
                holder.iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue_shixin);
            }
        }
        holder.setPosition(position);
        setOnListtener(holder);
        return convertView;
    }

    //触发
    protected void setOnListtener(final ViewHolder holder){
        if(mOnItemClickListener != null){

            holder.rl_friend_style_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemZanClick(holder);
                }
            });
        }
    }

    private void showShareDialog(View v) {
        View contentView = LayoutInflater.from(mcontext).inflate(R.layout.share_dialog, null);

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

    private void showMoreDialog(View v,int position, final Post post) {

        View contentView = LayoutInflater.from(mcontext).inflate(R.layout.popupwindow_friend_style_more, null);
        final TextView tv_friend_style_jubao = (TextView)contentView.findViewById(R.id.tv_friend_style_jubao);//举报
        TextView tv_friend_style_pinbi_zuozhe = (TextView)contentView.findViewById(R.id.tv_friend_style_pinbi_zuozhe);//屏蔽作者
        TextView tv_friend_style_cancel = (TextView)contentView.findViewById(R.id.tv_friend_style_cancel);//取消
        tv_friend_style_pinbi_zuozhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    mcontext.startActivity(new Intent(mcontext, LoginActivity.class));
                    AnimUtil.intentSlidOut((InvitationPullDownActivity)mcontext);
                    return;
                }
                InvitationPullDownActivity activity=(InvitationPullDownActivity)mcontext;
                activity.requeMemberBlackListAdd(post.getReporterId(), popupWindow);

            }
        });
        tv_friend_style_jubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    mcontext.startActivity(new Intent(mcontext, LoginActivity.class));
                    AnimUtil.intentSlidOut((InvitationPullDownActivity)mcontext);
                    return;
                }
                InvitationPullDownActivity activity=(InvitationPullDownActivity)mcontext;
                activity.requestJuBao(post.getId(),post.getReporterId(),popupWindow);
            }
        });

        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
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
        popupWindow.showAtLocation(mcontext.findViewById(R.id.framelayout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

  public   static class ViewHolder {
        @InjectView(R.id.iv_friend_style_title_photo)//头像
                RoundImageView iv_friend_style_title_photo;
        @InjectView(R.id.tv_friend_style_name)//发帖人
                TextView tv_friend_style_name;
        @InjectView(R.id.tv_friend_style_time)//时间
                TextView tv_friend_style_time;
        @InjectView(R.id.tv_friend_style_content)//内容
                TextView tv_friend_style_content;
        @InjectView(R.id.tv_pinglun_title)//评论标题
                TextView tv_pinglun_title;
        @InjectView(R.id.tv_friend_style_zan_num)//喜欢数量
            public     TextView tv_friend_style_zan_num;
        @InjectView(R.id.tv_friend_style_pinglun_num)//评论数量
                TextView tv_friend_style_pinglun_num;
        @InjectView(R.id.tv_friend_style_share_num)//分享数量
                TextView tv_friend_style_share_num;
        @InjectView(R.id.tv_friend_style_shengyu_pinglun)//查看其它评论
                TextView tv_friend_style_shengyu_pinglun;
        @InjectView(R.id.iv_friend_style_levlel)//用户等级
                ImageView iv_friend_style_levlel;
       @InjectView(R.id.rl_friend_style_zan)//点赞
             public    RelativeLayout rl_friend_style_zan;
        @InjectView(R.id.rl_friend_style_pinglun)//评论
                RelativeLayout rl_friend_style_pinglun;
        @InjectView(R.id.rl_friend_style_share)//分享
                RelativeLayout rl_friend_style_share;
        @InjectView(R.id.rl_friend_style_more)//更多
                RelativeLayout rl_friend_style_more;
        @InjectView(R.id.lv_friend_style_reply)//回复列表
                ListViewForScrollView lv_friend_style_reply;
        @InjectView(R.id.rl4)//图片布局
                RelativeLayout rl4;
        @InjectView(R.id.gv_images)//图片网格容器
        GridViewForScrollView gv_images;
        @InjectView(R.id.iv_friend_style_zan_num)//点赞
            public  ImageView iv_friend_style_zan_num;
        private int position;

        public void setPosition(int position){
            this.position=position;
        }
        public int getPosition(){
            return position;
        }

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public void initInfoImages(GridViewForScrollView newGridView, final List<PostImage> imgList) {
        int w = 0;
        switch (imgList.size()) {
            case 1:
                w = SysUtils.getScreenWidth(mcontext)/2;
                newGridView.setNumColumns(1);
                break;
            case 2:
            case 4:
                w = 2 * wh + SysUtils.Dp2Px(mcontext, 2);
                newGridView.setNumColumns(2);
                break;
            case 3:
            case 5:
            case 6:
                w = wh * 3 + SysUtils.Dp2Px(mcontext, 2) * 2;
                newGridView.setNumColumns(3);
                break;
            case 7:
            case 8:
            case 9:
                w = wh * 3 + SysUtils.Dp2Px(mcontext, 2) * 2;
                newGridView.setNumColumns(3);
                break;
        }

        final ArrayList<String>imageUrl=new ArrayList<>();
        for(int i=0;i<imgList.size();i++){
            imageUrl.add(imgList.get(i).getImg());
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, RelativeLayout.LayoutParams.WRAP_CONTENT);
        newGridView.setLayoutParams(lp);
        friendInfoImgsAdapter = new FriendGridAdapter(mcontext, imgList);
        newGridView.setAdapter(friendInfoImgsAdapter);
        newGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent=new Intent(mcontext, ImageZoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("imageList",imageUrl);
                bundle.putInt("position",arg2);
                intent.putExtras(bundle);
                mcontext.startActivity(intent);
                AnimUtil.intentSlidIn(mcontext);
            }
        });
    }
}
