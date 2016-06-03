package com.ctrl.forum.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.GridViewForScrollView;
import com.ctrl.forum.customview.ListViewForScrollView;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.PostImage;
<<<<<<< HEAD
import com.ctrl.forum.ui.activity.Invitation.InvitationPullDownActivity;
import com.ctrl.forum.ui.activity.mine.MineCommentActivity;
import com.ctrl.forum.utils.SysUtils;
=======
import com.ctrl.forum.ui.activity.Invitation.InvitationCommentDetaioActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationPullDownActivity;
import com.ctrl.forum.utils.SysUtils;
import com.ctrl.forum.utils.TimeUtils;
>>>>>>> origin/master

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

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


    public InvitationListViewFriendStyleAdapter(Activity context) {
        this.mcontext = context;
        this.wh = (SysUtils.getScreenWidth(mcontext)- SysUtils.Dp2Px(mcontext, 99)) / 3;
    }

    public void setList(List<Post> list) {
        this.mPostList = list;
        notifyDataSetChanged();
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
        holder.tv_friend_style_content.setText(post.getTitle());
        holder.tv_friend_style_name.setText(post.getMemberName());
<<<<<<< HEAD
//        holder.tv_friend_style_time.setText(TimeUtils.date(Long.parseLong(post.getPublishTime()))+"   "+post.getLocationName());
        holder.tv_friend_style_zan_num.setText(post.getPraiseNum()+"");
        holder.tv_friend_style_pinglun_num.setText(post.getCommentNum()+"");
        holder.tv_friend_style_share_num.setText(post.getShareNum() + "");

        //设置等级头像
        if(post.getMemberLevel()!=null){
            int level = Integer.parseInt(post.getMemberLevel());
            holder.iv_friend_style_levlel.setImageResource(R.mipmap.vip_icon+level);
        }

=======
        holder.tv_friend_style_time.setText(TimeUtils.date(Long.parseLong(post.getPublishTime()))+"   "+post.getLocationName());
        holder.tv_friend_style_zan_num.setText(post.getPraiseNum()+"");
        holder.tv_friend_style_pinglun_num.setText(post.getCommentNum()+"");
        holder.tv_friend_style_share_num.setText(post.getShareNum() + "");
        if(post.getMemberLevel()!=null){
            switch (post.getMemberLevel()){
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
        }
>>>>>>> origin/master
        Arad.imageLoader.load(post.getImgUrl()).placeholder(R.mipmap.default_error).into(holder.iv_friend_style_title_photo);
        if(post.getPostReplyList()!=null) {
            if (post.getPostReplyList().size() <= 3) {
                List<String> listStr = new ArrayList<>();
                for (int i = 0; i < post.getPostReplyList().size(); i++) {
                    if (post.getPostReplyList().get(i).getContentType().equals("0")) {
                        listStr.add(post.getPostReplyList().get(i).getMemberName()+":   " + post.getPostReplyList().get(i).getReplyContent());
                    }
                    if (post.getPostReplyList().get(i).getContentType().equals("1")) {
                        listStr.add(post.getPostReplyList().get(i).getMemberName()+":   " + "    [图片]");
                    }
                    if (post.getPostReplyList().get(i).getContentType().equals("2")) {
                        listStr.add(post.getPostReplyList().get(i).getMemberName()+":   " + "    [语音]");
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mcontext, R.layout.spinner_layout, listStr);
                holder.lv_friend_style_reply.setAdapter(adapter);
               // holder.tv_friend_style_shengyu_pinglun.setVisibility(View.GONE);
                holder.tv_friend_style_shengyu_pinglun.setText("查看其他更多评论...");
            } else {
                List<String> listStr = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    if (post.getPostReplyList().get(i).getContentType().equals("0")) {
                        listStr.add(post.getPostReplyList().get(i).getMemberName() +":   "+ post.getPostReplyList().get(i).getReplyContent());
                    }
                    if (post.getPostReplyList().get(i).getContentType().equals("1")) {
                        listStr.add(post.getPostReplyList().get(i).getMemberName()+":   " + "    [图片]");
                    }
                    if (post.getPostReplyList().get(i).getContentType().equals("2")) {
                        listStr.add(post.getPostReplyList().get(i).getMemberName()+":   " + "    [语音]");
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mcontext,  R.layout.spinner_layout, listStr);
                holder.lv_friend_style_reply.setAdapter(adapter);
                holder.tv_friend_style_shengyu_pinglun.setVisibility(View.VISIBLE);
                holder.tv_friend_style_shengyu_pinglun.setText("查看其他   " + (post.getPostReplyList().size() - 3) + "    评论");
            }
        }

        // 是否含有图片
        if (post.getPostImgList() != null && post.getPostImgList().size() != 0) {
            holder.rl4.setVisibility(View.VISIBLE);
            initInfoImages(holder.gv_images,post.getPostImgList());
        } else {
            holder.rl4.setVisibility(View.GONE);
        }
        
        holder.rl_friend_style_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog(v,position,post);
            }
        });

        holder.rl_friend_style_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog(v);
            }
        });
        holder.rl_friend_style_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                Intent intent=new Intent(mcontext, MineCommentActivity.class);
=======
                Intent intent=new Intent(mcontext, InvitationCommentDetaioActivity.class);
>>>>>>> origin/master
                intent.putExtra("id",post.getId());
                intent.putExtra("reportid",post.getReporterId());
                mcontext.startActivity(intent);
                AnimUtil.intentSlidIn(mcontext);
            }
        });
        holder.tv_friend_style_shengyu_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                Intent intent=new Intent(mcontext, MineCommentActivity.class);
=======
                Intent intent=new Intent(mcontext, InvitationCommentDetaioActivity.class);
>>>>>>> origin/master
                intent.putExtra("id",post.getId());
                intent.putExtra("reportid",post.getReporterId());
                mcontext.startActivity(intent);
                AnimUtil.intentSlidIn(mcontext);
            }
        });


        
        return convertView;
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
                InvitationPullDownActivity activity=(InvitationPullDownActivity)mcontext;
<<<<<<< HEAD
                //请求接口
               // activity.requeMemberBlackListAdd(post.getReporterId(), popupWindow);
=======
                activity.requeMemberBlackListAdd(post.getReporterId(), popupWindow);
>>>>>>> origin/master

            }
        });
        tv_friend_style_jubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvitationPullDownActivity activity=(InvitationPullDownActivity)mcontext;
<<<<<<< HEAD
               // activity.requestJuBao(post.getId(),post.getReporterId(),popupWindow);
=======
                activity.requestJuBao(post.getId(),post.getReporterId(),popupWindow);
>>>>>>> origin/master
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

    static class ViewHolder {
        @InjectView(R.id.iv_friend_style_title_photo)//头像
                ImageView iv_friend_style_title_photo;
        @InjectView(R.id.tv_friend_style_name)//发帖人
                TextView tv_friend_style_name;
        @InjectView(R.id.tv_friend_style_time)//时间
                TextView tv_friend_style_time;
        @InjectView(R.id.tv_friend_style_content)//内容
                TextView tv_friend_style_content;
        @InjectView(R.id.tv_friend_style_zan_num)//喜欢数量
                TextView tv_friend_style_zan_num;
        @InjectView(R.id.tv_friend_style_pinglun_num)//评论数量
                TextView tv_friend_style_pinglun_num;
        @InjectView(R.id.tv_friend_style_share_num)//分享数量
                TextView tv_friend_style_share_num;
        @InjectView(R.id.tv_friend_style_shengyu_pinglun)//查看其它评论
                TextView tv_friend_style_shengyu_pinglun;
        @InjectView(R.id.iv_friend_style_levlel)//用户等级
                ImageView iv_friend_style_levlel;
      //  @InjectView(R.id.rl_friend_style_zan)//点赞
               // RelativeLayout rl_friend_style_zan;
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

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public void initInfoImages(GridViewForScrollView newGridView, final List<PostImage> imgList) {
        int w = 0;
        switch (imgList.size()) {
            case 1:
                w = wh;
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
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, RelativeLayout.LayoutParams.WRAP_CONTENT);
        newGridView.setLayoutParams(lp);
        friendInfoImgsAdapter = new FriendGridAdapter(mcontext, imgList);
        newGridView.setAdapter(friendInfoImgsAdapter);
        newGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(mcontext, "点击了第" + (arg2 + 1) + "张图片", Toast.LENGTH_LONG).show();
            }
        });
    }
}
