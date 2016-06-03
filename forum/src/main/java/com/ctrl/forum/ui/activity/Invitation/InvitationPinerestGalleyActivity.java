package com.ctrl.forum.ui.activity.Invitation;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AndroidUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.entity.Post2;
import com.ctrl.forum.entity.PostImage;
import com.ctrl.forum.photo.zoom.PhotoView;
import com.ctrl.forum.photo.zoom.ViewPagerFixed;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 这个是用于瀑布流影集样式进行图片浏览时的界面
 *
 * @author king
 * @version 2014年10月18日  下午11:47:53
 * @QQ:595163260
 */
public class InvitationPinerestGalleyActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.tv_image_remark)//照片备注
    TextView tv_image_remark;
    @InjectView(R.id.tv_image_tel)//电话连接
    TextView tv_image_tel;
    @InjectView(R.id.iv_gallery_back)//返回按钮
    ImageView iv_gallery_back;

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

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int arg0 = (int) msg.obj;
                    tv_titile.setText((arg0 + 1) + "/" + listViews.size());
                 //   tv_image_remark.setText(post.getPostImgList().get(arg0).getRemark());
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private List<PostImage> listPostImage;
    private PopupWindow popupWindow_share;


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
        idao.requesPostDetail(id,Arad.preferences.getString("memberId"));
    }

    private void initView() {
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
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 3) {
            post = idao.getPost2();
            listPostImage=idao.getListPostImage();
            user = idao.getUser();
            listViews = new ArrayList<>();
           // listViews2 = new ArrayList<>();
           if(listPostImage==null)return;
            Log.i("tag","imagesize---"+listPostImage.size());
            for (int i = 0; i <listPostImage.size(); i++) {
                ImageView view = new ImageView(this);
               // TextView view2=new TextView(this);
                Arad.imageLoader.load(listPostImage.get(i).getImg()).placeholder(R.mipmap.default_error).into(view);
               // view2.setText(post.getPostImgList().get(i).getRemark());
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                listViews.add(view);
              //  listViews2.add(view2);
            }
            tv_titile.setText(1 + "/" + listViews.size());
            adapter = new MyPageAdapter(listViews);
            pager.setAdapter(adapter);
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

                break;
            case R.id.iv_pinerest_gallery_share:
                showSharePopuwindow(iv_pinerest_gallery_share);
                break;
            case R.id.iv_pinerest_gallery_pinglun:
                Intent intent=new Intent(InvitationPinerestGalleyActivity.this,InvitationCommentDetaioActivity.class);
                intent.putExtra("id",post.getId());
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
                        showDialog();
                        return true;
                    }
                });


            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        private void showDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(InvitationPinerestGalleyActivity.this);
            builder.setMessage("保存到本地");
            builder.setPositiveButton("确定", null);
            builder.show();
        }


        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }


}
