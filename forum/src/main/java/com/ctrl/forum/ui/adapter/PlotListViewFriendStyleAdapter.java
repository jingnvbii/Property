package com.ctrl.forum.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.SetMemberLevel;
import com.ctrl.forum.customview.GridViewForScrollView;
import com.ctrl.forum.customview.ImageZoomActivity;
import com.ctrl.forum.customview.ListViewForScrollView;
import com.ctrl.forum.customview.RoundImageView;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.PostImage;
import com.ctrl.forum.ui.activity.Invitation.InvitationCommentDetaioActivity;
import com.ctrl.forum.ui.activity.LoginActivity;
import com.ctrl.forum.ui.activity.mine.MineDetailActivity;
import com.ctrl.forum.utils.SysUtils;
import com.ctrl.forum.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 小区列表样式adapter
 * Created by jason on 2016/4/8.
 */
public class PlotListViewFriendStyleAdapter extends BaseAdapter {
    private Activity mcontext;
    private List<Post> mPostList;
    private LayoutInflater inflter;
    private View.OnClickListener onShare;
    private int wh;
    private FriendGridAdapter friendInfoImgsAdapter;
    private OnItemClickListener mOnItemClickListener;
    private View.OnClickListener onMoreDialog;
    private View.OnClickListener onLove;
    private ImageView zanView;

    public PlotListViewFriendStyleAdapter(Activity context) {
        this.mcontext = context;
        this.wh = (SysUtils.getScreenWidth(mcontext)- SysUtils.Dp2Px(mcontext, 99)) / 3;
    }

    public void setOnLove(View.OnClickListener onLove) {
        this.onLove = onLove;
    }

    public void setOnMoreDialog(View.OnClickListener onMoreDialog) {
        this.onMoreDialog = onMoreDialog;
    }

    public void setOnShare(View.OnClickListener onShare) {
        this.onShare = onShare;
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

        if (post.getTitle()!=null && !post.getTitle().equals("")) {
            holder.tv_friend_style_content.setText(post.getTitle());
        }else{
            holder.tv_friend_style_content.setText(post.getContent());
        }

        holder.tv_friend_style_name.setText(post.getMemberName());

        if (post.getPublishTime()!=null&& !post.getPublishTime().equals("")) {
            if (post.getLocationName()!=null && !post.getLocationName().equals("")) {
                holder.tv_friend_style_time.setText(TimeUtils.date(Long.parseLong(post.getPublishTime())) + "   " + post.getLocationName());
            }else {
                holder.tv_friend_style_time.setText(TimeUtils.date(Long.parseLong(post.getPublishTime())));
            }
        }

        holder.iv_friend_style_title_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,MineDetailActivity.class);
                intent.putExtra("id",post.getReporterId());
                mcontext.startActivity(intent);
            }
        });

        holder.tv_friend_style_zan_num.setText(post.getPraiseNum()+"");
        holder.tv_friend_style_pinglun_num.setText(post.getCommentNum()+"");
        //holder.tv_friend_style_share_num.setText(post.getShareNum() + ""); //分享的数量
        SetMemberLevel.setLevelImage(mcontext,holder.iv_friend_style_levlel,post.getMemberLevel());
        Arad.imageLoader.load(post.getImgUrl()).placeholder(R.mipmap.default_error).into(holder.iv_friend_style_title_photo);

        if(post.getPostReplyList()!=null ) {
            holder.lv_friend_style_reply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                        mcontext.startActivity(new Intent(mcontext, LoginActivity.class));
                        AnimUtil.intentSlidOut(mcontext);
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
                holder.tv_friend_style_shengyu_pinglun.setText("查看其他"+(post.getCommentNum()-post.getPostReplyList().size())+"条评论...");
            }
        }else {
            holder.lv_friend_style_reply.setVisibility(View.GONE);
            holder.tv_friend_style_shengyu_pinglun.setVisibility(View.GONE);
            holder.tv_pinglun_title.setVisibility(View.GONE);
        }

        // 是否含有图片
        if (post.getPostImgList() != null && post.getPostImgList().size() != 0) {
            holder.rl4.setVisibility(View.VISIBLE);
            initInfoImages(holder.gv_images,post.getPostImgList());
        } else {
            holder.rl4.setVisibility(View.GONE);
        }

        holder.rl_friend_style_more.setOnClickListener(onMoreDialog);
        holder.rl_friend_style_more.setTag(position);

        holder.rl_friend_style_share.setOnClickListener(onShare);

        holder.rl_friend_style_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Intent intent=new Intent(mcontext, InvitationCommentDetaioActivity.class);
                intent.putExtra("id",post.getId());
                intent.putExtra("reportid",post.getReporterId());
                mcontext.startActivity(intent);
                AnimUtil.intentSlidIn(mcontext);
            }
        });

        //holder.rl_friend_style_zan.setOnClickListener(onLove);
       // holder.rl_friend_style_zan.setTag(position);

        if(post.getPraiseState()!=null) {
            if (post.getPraiseState().equals("0")) {
                holder.iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue);
            }
            if (post.getPraiseState().equals("1")) {
                holder.iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue_shixin);
            }
        }

        holder.setPosition(position);
        setOnListtener(holder);

        holder.iv_friend_style_title_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post.getReporterId().equals("admin")) {
                    Intent intent = new Intent(mcontext, MineDetailActivity.class);
                    intent.putExtra("id", post.getReporterId());
                    mcontext.startActivity(intent);
                }
            }
        });


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

    public   static class ViewHolder {
        @InjectView(R.id.iv_friend_style_title_photo)//头像
                RoundImageView iv_friend_style_title_photo;
        @InjectView(R.id.tv_friend_style_name)//发帖人
                TextView tv_friend_style_name;
        @InjectView(R.id.tv_friend_style_time)//时间
                TextView tv_friend_style_time;
        @InjectView(R.id.tv_friend_style_content)//内容
                TextView tv_friend_style_content;
        @InjectView(R.id.tv_friend_style_zan_num)//喜欢数量
           public  TextView tv_friend_style_zan_num;
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
        @InjectView(R.id.tv_pinglun_title)
        TextView tv_pinglun_title;
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
