package com.ctrl.forum.ui.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.customview.ShareDialog;
import com.ctrl.forum.customview.XListView;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.ui.activity.Invitation.InvitationDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreCommodityDetailActivity;
import com.ctrl.forum.ui.activity.store.StoreShopListVerticalStyleActivity;
import com.ctrl.forum.ui.adapter.PlotListViewFriendStyleAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 个人中心---我的发帖
 */
public class MineQueryPostActivity extends AppToolBarActivity implements View.OnClickListener, XListView.IXListViewListener ,PlatformActionListener {
    @InjectView(R.id.lv_content)
    PullToRefreshListView lv_content;

    private List<Post> posts;
    private int PAGE_NUM =1;
    //private MineQueryPostAdapter mineQueryPostAdapter;
    private InvitationDao invitationDao;
    //private String ids = "";
    static String title;
    private PlotListViewFriendStyleAdapter adapter;


    private PopupWindow popupWindow;
    private PopupWindow popupWindow_share;
    private ShareDialog shareDialog;
    private InvitationDao idao;
    private Map<Integer,Boolean> isAdd = new HashMap<>();
    private Map<Integer,Integer> text = new HashMap<>();
    private Activity activity;
    private PopupWindow mPopupWindow;
    private String memberId ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_query_post);
        ButterKnife.inject(this);

        invitationDao = new InvitationDao(this);
        idao = new InvitationDao(this);

        if (getIntent().getStringExtra("id")!=null && !getIntent().getStringExtra("id").equals("")){
            memberId = getIntent().getStringExtra("id");
            title = "他的发帖";
        }else{
            memberId = Arad.preferences.getString("memberId");
            title = getResources().getString(R.string.my_post);
        }
        invitationDao.queryMyPostList(memberId,PAGE_NUM+"",Constant.PAGE_SIZE+"");

        //mineQueryPostAdapter = new MineQueryPostAdapter(this);
        adapter = new PlotListViewFriendStyleAdapter(this);
        adapter.setOnLove(this);

        adapter.setOnShare(this);
        adapter.setOnMoreDialog(this);

        lv_content.setAdapter(adapter);
        activity = this;

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (posts != null) {
                    posts.clear();
                    PAGE_NUM = 1;
                }
                invitationDao.queryMyPostList(memberId, PAGE_NUM + "", Constant.PAGE_SIZE + "");

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (posts != null) {
                    PAGE_NUM += 1;
                    invitationDao.queryMyPostList(memberId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if (posts.get(position-1)!=null) {
                    if (posts.get(position-1).getContentType()!=null){
                        String type = posts.get(position-1).getContentType();
                        switch (type){
                            case "0":
                                intent = new Intent(getApplicationContext(), InvitationDetailActivity.class);
                                intent.putExtra("id", posts.get(position - 1).getId());
                                intent.putExtra("reportid", posts.get(position - 1).getReporterId());
                                startActivity(intent);
                                break;
                            case "1":
                                Uri uri = Uri.parse(posts.get(position - 1).getArticleLink());
                                intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                break;
                            case "2":
                                intent = new Intent(getApplicationContext(), StoreCommodityDetailActivity.class);
                                intent.putExtra("id", posts.get(position-1).getLinkItemId());
                                startActivity(intent);
                                break;
                            case "3":
                                intent = new Intent(getApplicationContext(), StoreShopListVerticalStyleActivity.class);
                                intent.putExtra("id", posts.get(position - 1).getLinkItemId());
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        });

        adapter.setOnItemClickListener(new PlotListViewFriendStyleAdapter.OnItemClickListener() {
            @Override
            public void onItemZanClick(PlotListViewFriendStyleAdapter.ViewHolder v) {
                int position = v.getPosition();
                int tex = posts.get(position).getPraiseNum();

                if (isAdd.get(position)==null) {
                    if (posts.get(position).getPraiseState().equals("0")) {
                        isAdd.put(position, true);
                    } else {
                        isAdd.put(position, false);
                    }
                }

                if (text.get(position)==null){
                    text.put(position,tex);
                }

                if (posts.get(position).getPraiseState()!=null) {
                    if (isAdd.get(position)) {
                        idao.requesZambia("add", posts.get(position).getId(),memberId
                                ,posts.get(position).getTitle(),posts.get(position).getContent());
                        v.tv_friend_style_zan_num.setText((text.get(position) + 1) + "");
                        text.put(position,text.get(position) + 1);
                        v.iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue_shixin);
                        //MessageUtils.showShortToast(getApplicationContext(), "点赞成功");
                        isAdd.put(position,false);
                    } else {
                        idao.requesZambia("reduce", posts.get(position).getId(),memberId
                                , posts.get(position).getTitle(), posts.get(position).getContent());
                        //MessageUtils.showShortToast(getApplicationContext(), "取消点赞");
                        v.tv_friend_style_zan_num.setText((text.get(position) - 1) + "");
                        text.put(position, text.get(position)-1);
                        v.iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue);
                        isAdd.put(position,true);
                    }
                }
            }
        });
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {return title;}

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==18){
            posts = invitationDao.getMinePost();
            if (posts!=null){
                adapter.setList(posts);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }

    /*
* 举报请求
* */
    public void requestJuBao(String postId,String reportId,PopupWindow popupWindow){
        idao.requePostReport(postId, "", reportId, memberId);
        mPopupWindow=popupWindow;
    }
    /*
 * 屏蔽作者请求
 * */
    public void requeMemberBlackListAdd(String reportId,PopupWindow popupWindow){
        idao.requeMemberBlackListAdd(memberId, reportId);
        mPopupWindow=popupWindow;
    }

    private void showShareDialog(View v) {
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

    //更多的弹出框
    private void showMoreDialog(View v,int position, final Post post) {

        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_friend_style_more, null);
        final TextView tv_friend_style_jubao = (TextView)contentView.findViewById(R.id.tv_friend_style_jubao);//举报
        TextView tv_friend_style_pinbi_zuozhe = (TextView)contentView.findViewById(R.id.tv_friend_style_pinbi_zuozhe);//屏蔽作者
        TextView tv_friend_style_cancel = (TextView)contentView.findViewById(R.id.tv_friend_style_cancel);//取消
        tv_friend_style_pinbi_zuozhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requeMemberBlackListAdd(post.getReporterId(), popupWindow);
            }
        });
        tv_friend_style_jubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestJuBao(post.getId(), post.getReporterId(), popupWindow);
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
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 设置好参数之后再show
        //popupWindow.showAtLocation(this.findViewById(R.id.framelayout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onClick(View v) {
        Object id = v.getTag();
        switch (v.getId()){
            case R.id.rl_friend_style_share:
                //showShareDialog(this.getView());
                int position = (int) id;
                clickShare(posts.get(position));
                break;
            case R.id.rl_friend_style_more:
                int position1 = (int)id;
                showMoreDialog(v, position1, posts.get(position1));
                break;
            default:
                break;
        }
    }

    public String setTitle(Post post){
        if (post.getTitle()==null || post.getTitle().equals("")){
            return "莱州人";
        }else{
            return post.getTitle();
        }
    }

    public void setBimp(Platform.ShareParams sp,Post post){
        if (post.getPostImgList()==null || post.getPostImgList().size()==0){
            Log.e("bimap==========", "1235");
            //设为图片路径
            sp.setImagePath(Constant.SHARE_IMAGE_PATH);
            //sp.setImageData(BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo));
        }else{
            sp.setImageUrl(post.getPostImgList().get(0).getImg());//网络图片rul
        }
    }

    public void clickShare(final Post post){
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
                /*sp.setTitle("烟台项目");
                sp.setText("欢迎加入");

                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul*/

                sp.setTitle(setTitle(post));
                sp.setText(post.getContent());
                setBimp(sp, post);//设置标题与图片
                //网友点进链接后，可以看到分享的详情
                sp.setTitleUrl(Constant.SHARE_INVITION_URL + post.getId());
                //3、非常重要：获取平台对象
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(MineQueryPostActivity.this); // 设置分享事件回调
                // 执行分享
                qq.share(sp);
            }

        });

        shareDialog.setWeixinButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Platform.ShareParams sp = new Platform.ShareParams();
                sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
               /* sp.setTitle("烟台项目");  //分享标题
                sp.setText("欢迎加入");   //分享文本
                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul*/

                sp.setTitle(setTitle(post));
                setBimp(sp, post);//设置标题与图片
                sp.setText(post.getContent());
                sp.setUrl(Constant.SHARE_INVITION_URL+ post.getId());
                //3、非常重要：获取平台对象
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(MineQueryPostActivity.this); // 设置分享事件回调
                // 执行分享
                wechat.share(sp);
            }
        });
        shareDialog.setSinaWeiBoButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2、设置分享内容
                Platform.ShareParams sp = new Platform.ShareParams();
                //sp.setText("我是新浪微博分享文本，啦啦啦~http://uestcbmi.com/"); //分享文本
               /* sp.setText("我是新浪微博分享文本，啦啦啦~"+Constant.SHARE_INVITION_URL+post.getId());
                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                sp.setUrl(Constant.SHARE_INVITION_URL+ post.getId());*/

                sp.setTitle(setTitle(post));
                setBimp(sp, post);//设置标题与图片
                sp.setText(setTitle(post)+Constant.SHARE_INVITION_URL + post.getId());
                //3、非常重要：获取平台对象
                Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                sinaWeibo.setPlatformActionListener(MineQueryPostActivity.this); // 设置分享事件回调
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
                /*sp.setTitle("我是朋友圈分享标题");  //分享标题
                sp.setText("我是朋友圈分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                sp.setUrl(Constant.SHARE_INVITION_URL+ post.getId());*/

                sp.setTitle(setTitle(post));
                setBimp(sp, post);//设置标题与图片
                sp.setText(setTitle(post) + Constant.SHARE_INVITION_URL + post.getId());
                //3、非常重要：获取平台对象
                Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                wechatMoments.setPlatformActionListener(MineQueryPostActivity.this); // 设置分享事件回调
                // 执行分享
                wechatMoments.share(sp);
            }
        });
        shareDialog.setTecentWeiBoButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2、设置分享内容
                Platform.ShareParams sp = new Platform.ShareParams();
               /* sp.setTitle("我是腾讯微博分享标题");  //分享标题
                //sp.setText("我是腾讯微博分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                sp.setText("我是腾讯微博分享文本，啦啦啦~" + Constant.SHARE_INVITION_URL + post.getId());*/

                setBimp(sp, post);//设置标题与图片
                sp.setText(setTitle(post)+Constant.SHARE_INVITION_URL + post.getId());
                //3、非常重要：获取平台对象
                Platform tecentWeibo = ShareSDK.getPlatform(TencentWeibo.NAME);
                tecentWeibo.setPlatformActionListener(MineQueryPostActivity.this); // 设置分享事件回调
                // 执行分享
                tecentWeibo.share(sp);
            }
        });

        shareDialog.setEmailButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2、设置分享内容
                Platform.ShareParams sp = new Platform.ShareParams();
                /*sp.setTitle("我是邮件分享标题");  //分享标题
                sp.setText("我是邮件分享文本，啦啦啦~"+Constant.SHARE_INVITION_URL+post.getId());
                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                sp.setUrl(Constant.SHARE_INVITION_URL+ post.getId());*/

                setBimp(sp, post);//设置标题与图片
                sp.setText(setTitle(post)+Constant.SHARE_INVITION_URL + post.getId());
                //3、非常重要：获取平台对象
                Platform emailName = ShareSDK.getPlatform(Email.NAME);
                emailName.setPlatformActionListener(MineQueryPostActivity.this); // 设置分享事件回调
                // 执行分享
                emailName.share(sp);

            }
        });
        shareDialog.setDuanXinButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //2、设置分享内容
                Platform.ShareParams sp = new Platform.ShareParams();
               /* sp.setTitle("我是短信分享标题");  //分享标题
                sp.setText("我是短信分享文本，啦啦啦~"+Constant.SHARE_INVITION_URL+post.getId());
                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                sp.setUrl(Constant.SHARE_INVITION_URL+ post.getId());*/

                setBimp(sp, post);//设置标题与图片
                sp.setText(setTitle(post)+Constant.SHARE_INVITION_URL + post.getId());
                //3、非常重要：获取平台对象
                Platform shortMessage = ShareSDK.getPlatform(ShortMessage.NAME);
                shortMessage.setPlatformActionListener(MineQueryPostActivity.this); // 设置分享事件回调
                // 执行分享
                shortMessage.share(sp);
            }
        });
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
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
                    Toast.makeText(MineQueryPostActivity.this, "分享失败啊", Toast.LENGTH_LONG).show();
                    break;
                case 9:
                    Toast.makeText(MineQueryPostActivity.this, "已取消", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void onRestart() {
        super.onRestart();
        if (posts != null) {
            posts.clear();
        }
       /* if (memberId.equals(Arad.preferences.getString("memberId"))) {
            title = "他的发帖";
        } else {
            title = getResources().getString(R.string.my_post);
        }*/
        invitationDao.queryMyPostList(memberId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
    }
}
