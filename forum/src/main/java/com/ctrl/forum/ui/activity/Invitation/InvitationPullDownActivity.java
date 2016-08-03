package com.ctrl.forum.ui.activity.Invitation;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.MyApplication;
import com.ctrl.forum.customview.CustomViewPager;
import com.ctrl.forum.customview.XListView;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Category;
import com.ctrl.forum.entity.Invitation_listview;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.ThirdKind;
import com.ctrl.forum.ui.activity.LoginActivity;
import com.ctrl.forum.ui.adapter.ExpandableListViewAllCategroyAdapter;
import com.ctrl.forum.ui.adapter.JasonViewPagerAdapter;
import com.ctrl.forum.ui.fragment.InvitationPullDownHaveThirdKindFragment;
import com.ctrl.forum.ui.fragment.InvitationPullDownHaveThirdKindPinterestStyleFragment;

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
 * 帖子列表 三级分类下拉页面 activity
 * Created by jason on 2016/4/8
 */
public class InvitationPullDownActivity extends ToolBarActivity implements View.OnClickListener, XListView.IXListViewListener ,PlatformActionListener{
    @InjectView(R.id.viewpager_invitation_pull_down)
    CustomViewPager viewpager_invitation_pull_down;
    @InjectView(R.id.lay)
    LinearLayout layout;
    @InjectView(R.id.iv_pull_down)//向下箭头
            ImageView iv_pull_down;
    @InjectView(R.id.horizontalScrollView_pull)
    HorizontalScrollView horizontalScrollView;

    @InjectView(R.id.iv_pull_back)//返回
    ImageView iv_pull_back;
    @InjectView(R.id.iv_pul_release)//发布
    ImageView iv_pul_release;
    @InjectView(R.id.iv_pul_search)//搜索
    ImageView iv_pul_search;
    @InjectView(R.id.tv_pull_title)//标题
    TextView tv_pull_title;


    public int mCurrentCheckedRadioLeft;
    private int PAGE_NUM = 1;
    private RadioGroup myRadioGroup;
    private int width;
    DisplayMetrics dm;


    private List<ThirdKind> kindList;
    private List<Invitation_listview> list;
    private InvitationDao idao;
    private String channelId;
    private List<Category> listCategory;
    private List<Post> listPost;

   // private SparseArray<Fragment> fragments = new SparseArray<>();
   List<Fragment> fragments=new ArrayList<>();
    private String styleType;
    private PopupWindow popupWindow;
    private ExpandableListViewAllCategroyAdapter elvAdapter;
    private ExpandableListView elv_pull_down;
    private List<Category> listCategory2;
    private List<RadioButton> listRadioButton=new ArrayList<>();
    private JasonViewPagerAdapter viewPagerAdapter;
    private String listCategoryId;
    private String listCategoryId2;
    private InvitationPullDownHaveThirdKindFragment invitationPullDownHaveThirdKindFragment;
    private InvitationPullDownHaveThirdKindPinterestStyleFragment invitationPullDownHaveThirdKindPinterestStyleFragment;
    private GridView gridView;
    private int groupPosition1;
    private String thirdKindId;
    private PopupWindow mPopupWindow;
    private int mGroupPostion;
    private int mChildrenPosition;

    public static boolean isFromSelcet=false;
    public static boolean isFromSearch=false;
    private String keyword;

    //手指上下滑动时的最小速度11
    private static final int YSPEED_MIN = 1000;

    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 50;

    //手指向上滑或下滑时的最小距离
    private static final int YDISTANCE_MIN = 100;

    //记录手指按下时的横坐标。
    private float xDown;

    //记录手指按下时的纵坐标。
    private float yDown;

    //记录手指移动时的横坐标。
    private float xMove;

    //记录手指移动时的纵坐标。
    private float yMove;

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;
    private String categoryId;
    private String categoryName;
    private String showAll;
    private String sstyleType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_pull_down_now);
        ButterKnife.inject(this);
        width = getResources().getDisplayMetrics().widthPixels;
        channelId = getIntent().getStringExtra("channelId");
        elvAdapter = new ExpandableListViewAllCategroyAdapter(this);
        initView();
        MyApplication.getInstance().addActivity(this);
    }

    private void initView() {
        tv_pull_title.setText(getIntent().getStringExtra("channelName"));
        ShareSDK.initSDK(this);
        idao = new InvitationDao(this);
        idao.requesPostCategory(channelId, "1", "0");
        iv_pull_down.setOnClickListener(this);

        iv_pull_back.setOnClickListener(this);
        iv_pul_release.setOnClickListener(this);
        iv_pul_search.setOnClickListener(this);
       // viewpager_invitation_pull_down.setScrollble(false);//禁止viewpager滚动

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 2) {
          //  MessageUtils.showShortToast(this, "获取帖子分类成功");
            listCategory = idao.getListCategory();
            setRadioGruop();
        }
        if (requestCode == 11) {
            MessageUtils.showShortToast(this, "举报成功");
            mPopupWindow.dismiss();
        }
        if (requestCode == 10) {
            MessageUtils.showShortToast(this, "屏蔽作者成功");
            mPopupWindow.dismiss();
        }
        if (requestCode == 4) {
          //  MessageUtils.showShortToast(this, "获取当前频道下所有帖子分类成功");
            listCategory2 = idao.getListCategory();
            showAllCategoryPopupWindow(iv_pull_down);
        }

    }

    private void setRadioGruop() {
        myRadioGroup = new RadioGroup(this);
        myRadioGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(myRadioGroup);
        int newWidth = width / 5;
        for (int i = 0; i < listCategory.size(); i++) {
            RadioButton radio = new RadioButton(this);
          //  radio.setBackgroundResource(R.drawable.top_category_selector);
            LinearLayout.LayoutParams l;
            l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
           /* if (listCategory.size() == 1) {
                l = new LinearLayout.LayoutParams(newWidth, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            } else if (listCategory.size() == 2) {
                l = new LinearLayout.LayoutParams(newWidth, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            } else if (listCategory.size() == 3) {
                l = new LinearLayout.LayoutParams(newWidth, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            } else if (listCategory.size() == 4) {
                l = new LinearLayout.LayoutParams(newWidth, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            } else {
                l = new LinearLayout.LayoutParams(newWidth, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }*/
/*            if (listCategory.size() == 1) {
                l = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            } else if (listCategory.size() == 2) {
                l = new LinearLayout.LayoutParams(width / 2, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            } else if (listCategory.size() == 3) {
                l = new LinearLayout.LayoutParams(width / 3, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            } else if (listCategory.size() == 4) {
                l = new LinearLayout.LayoutParams(width / 4, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            } else {
                l = new LinearLayout.LayoutParams((width - 30) / 5, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }*/
            radio.setLayoutParams(l);
            radio.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            radio.setPadding(10, 10, 20, 10);
            radio.setId(i);
            radio.setButtonDrawable(getResources().getDrawable(R.color.white));
            radio.setTextSize(16.0f);
            radio.setText(listCategory.get(i).getName());//动态设置
            radio.setSingleLine(true);
            radio.setEllipsize(TextUtils.TruncateAt.END);
            radio.setTag(i);
            if (i == 0) {
                radio.setChecked(true);
                categoryId=listCategory.get(0).getId();
                categoryName=listCategory.get(0).getName();
                sstyleType = listCategory.get(0).getStyleType();
                showAll=listCategory.get(0).getShowAll();
            }
            if(radio.isChecked()) {
                radio.setTextColor(getResources().getColor(R.color.text_blue));//选择器
            }else {
                radio.setTextColor(getResources().getColor(R.color.text_black));//选择器
            }
            View view = new View(this);
            LinearLayout.LayoutParams v = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(v);
            //  view.setBackgroundColor(Color.parseColor("#cccccc"));
            listRadioButton.add(radio);
            myRadioGroup.addView(radio);
            myRadioGroup.addView(view);
            styleType = listCategory.get(i).getStyleType();
            if (styleType.equals("3")) {
                invitationPullDownHaveThirdKindPinterestStyleFragment = InvitationPullDownHaveThirdKindPinterestStyleFragment.newInstance(InvitationPullDownActivity.this,listCategory.get(i).getId(),null,null,listCategory.get(i).getShowAll(),channelId);
                fragments.add(invitationPullDownHaveThirdKindPinterestStyleFragment);
            } else {
               invitationPullDownHaveThirdKindFragment = InvitationPullDownHaveThirdKindFragment.newInstance(listCategory.get(i).getId(), listCategory.get(i).getStyleType(),null,null,listCategory.get(i).getShowAll(),channelId);
                fragments.add(invitationPullDownHaveThirdKindFragment);
            }
        }

        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Map<String, Object> map = (Map<String, Object>) group.getChildAt(checkedId).getTag();
                int radioButtonId = group.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                for(int i=0;i<listRadioButton.size();i++){
                    listRadioButton.get(i).setTextColor(getResources().getColor(R.color.text_black));
                }

                rb.setTextColor(getResources().getColor(R.color.text_blue));
                viewpager_invitation_pull_down.setCurrentItem(radioButtonId);//让下方ViewPager跟随上面的HorizontalScrollView切换
                mCurrentCheckedRadioLeft = rb.getLeft();
                horizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (width * 2) / 5, 0);
            }
        });
        viewPagerAdapter = new JasonViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager_invitation_pull_down.setAdapter(viewPagerAdapter);
        viewpager_invitation_pull_down.setOnPageChangeListener(new FragmentOnPageChangeListener());
        viewpager_invitation_pull_down.setCurrentItem(0);
        viewPagerAdapter.setOnReloadListener(new JasonViewPagerAdapter.OnReloadListener() {
            @Override
            public void onReload() {
                fragments = null;
                String keyword1 = keyword;
                List<Fragment> list = new ArrayList<Fragment>();
                if (listCategory2 == null) {
                    for (int i = 0; i < listCategory.size(); i++) {
                        styleType = listCategory.get(i).getStyleType();
                        if (styleType.equals("3")) {
                            list.add(InvitationPullDownHaveThirdKindPinterestStyleFragment.newInstance(InvitationPullDownActivity.this, listCategory.get(i).getId(), thirdKindId, keyword1,listCategory.get(i).getShowAll(),channelId));
                        } else {
                            list.add(InvitationPullDownHaveThirdKindFragment.newInstance(listCategory.get(i).getId(), listCategory.get(i).getStyleType(), thirdKindId, keyword1,listCategory.get(i).getShowAll(),channelId));
                        }
                    }
                } else {
                    for (int i = 0; i < listCategory2.size(); i++) {
                        styleType = listCategory2.get(i).getStyleType();

                        if (styleType.equals("3")) {
                            list.add(InvitationPullDownHaveThirdKindPinterestStyleFragment.newInstance(InvitationPullDownActivity.this, listCategory2.get(i).getId(), thirdKindId, keyword1,listCategory.get(i).getShowAll(),channelId));
                        } else {
                            list.add(InvitationPullDownHaveThirdKindFragment.newInstance(listCategory2.get(i).getId(), listCategory2.get(i).getStyleType(), thirdKindId, keyword1,listCategory.get(i).getShowAll(),channelId));
                        }
                    }
                }
                viewPagerAdapter.setPagerItems(list);
            }
        });
        viewpager_invitation_pull_down.setOffscreenPageLimit(listCategory.size());
    }

    public JasonViewPagerAdapter getAdapter(){
        return viewPagerAdapter;
    }

    /**
     * ViewPager的PageChangeListener(页面改变的监听器)
     */
    private class FragmentOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        /**
         * 滑动ViewPager的时候,让上方的HorizontalScrollView自动切换
         */
        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            RadioButton radioButton = (RadioButton) findViewById(position);
            radioButton.performClick();
            categoryId=listCategory.get(position).getId();
            categoryName=listCategory.get(position).getName();
            mGroupPostion=position;
            sstyleType=listCategory.get(position).getStyleType();
            showAll=listCategory.get(position).getShowAll();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(viewpager_invitation_pull_down.getCurrentItem()==0) {
                    xMove = event.getRawX();
                    yMove = event.getRawY();
                    //滑动的距离
                    int distanceX = (int) (xMove - xDown);
                    int distanceY = (int) (yMove - yDown);
                    //获取顺时速度
                    int ySpeed = getScrollVelocity();
                    //关闭Activity需满足以下条件：
                    //1.x轴滑动的距离>XDISTANCE_MIN
                    //2.y轴滑动的距离在YDISTANCE_MIN范围内
                    //3.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
                    if (distanceX > XDISTANCE_MIN && (distanceY < YDISTANCE_MIN && distanceY > -YDISTANCE_MIN) && ySpeed < YSPEED_MIN) {
                        finish();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     *
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getYVelocity();
        return Math.abs(velocity);
    }

    public void request(int groupPosition,int position,String thirdId){
        mGroupPostion=groupPosition;
        mChildrenPosition=position;
        thirdKindId = thirdId;

        viewpager_invitation_pull_down.setCurrentItem(groupPosition);
        viewPagerAdapter.reLoad();
        popupWindow.dismiss();
    }


  /*  @Override
    public String setupToolBarTitle() {
      *//*  TextView tv_title = getmTitle();
       // tv_title.setText(get);
        tv_title.setTextColor(getResources().getColor(R.color.text_black));*//*
       // return getIntent().getStringExtra("channelName");
        return "";
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
      *//*  leftButton.setImageResource(R.mipmap.jiantou_left);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*//*
        return true;
    }




    @Override
    public boolean setupToolBarRightButton(ImageView rightButton) {
       *//* rightButton.setImageResource(R.mipmap.edit);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Arad.preferences.getString("memberId") == null || Arad.preferences.getString("memberId").equals("")) {
                    startActivity(new Intent(InvitationPullDownActivity.this, LoginActivity.class));
                    return;
                }
                if (Arad.preferences.getString("isShielded").equals("1")) {
                    MessageUtils.showShortToast(InvitationPullDownActivity.this, "您已经被屏蔽，不能发帖");
                }
                if (Arad.preferences.getString("isShielded").equals("0")) {
                    Intent intent = new Intent(InvitationPullDownActivity.this, InvitationReleaseActivity.class);
                    intent.putExtra("channelId", channelId);
                    intent.putExtra("categoryId", categoryId);
                    intent.putExtra("categoryName", categoryName);
                    startActivityForResult(intent, 222);
                    AnimUtil.intentSlidIn(InvitationPullDownActivity.this);
                }

            }
        });*//*

        return true;
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pull_down:
                if(listCategory!=null) {
                    if (listCategory.size() > 0) {
                        listCategory.clear();
                    }
                    idao.requesAllPostCategory(channelId);
                }
                break;
            case R.id.iv_pull_back:
                onBackPressed();
                break;
            case R.id.iv_pul_release:
                if (Arad.preferences.getString("memberId") == null || Arad.preferences.getString("memberId").equals("")) {
                    startActivity(new Intent(InvitationPullDownActivity.this, LoginActivity.class));
                    return;
                }
                if (Arad.preferences.getString("isShielded").equals("1")) {
                    MessageUtils.showShortToast(InvitationPullDownActivity.this, "您已经被屏蔽，不能发帖");
                }
                if (Arad.preferences.getString("isShielded").equals("0")) {
                    Intent intent = new Intent(InvitationPullDownActivity.this, InvitationReleaseActivity.class);
                    intent.putExtra("channelId", channelId);
                    intent.putExtra("categoryId", categoryId);
                    intent.putExtra("categoryName", categoryName);
                    intent.putExtra("styleType",styleType);
                    startActivityForResult(intent, 222);
                    AnimUtil.intentSlidIn(InvitationPullDownActivity.this);
                }
                break;
            case R.id.iv_pul_search:
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationPullDownActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationPullDownActivity.this);
                    return;
                }
                InvitationPullDownActivity.isFromSearch=true;
                Intent intent = new Intent(InvitationPullDownActivity.this, InvitationSearchActivity.class);
                if(showAll.equals("1")){
                    intent.putExtra("channelId",channelId);
                }else {
                    intent.putExtra("channelId",categoryId);
                }
                Log.i("tag","styleType==="+sstyleType);
                intent.putExtra("styleType",sstyleType);
                startActivity(intent);
                AnimUtil.intentSlidIn(InvitationPullDownActivity.this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1111&&resultCode==RESULT_OK){
        }
        if(requestCode==222&&resultCode==RESULT_OK){
            viewPagerAdapter.reLoad();
        }
    }


    public void setKeyword(String keyword){
        this.keyword=keyword;

    }

    private void showAllCategoryPopupWindow(ImageView iv_pull_down) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_all_category, null);
        elv_pull_down = (ExpandableListView) contentView.findViewById(R.id.elv_pull_down);
        //去掉默认的箭头
        elv_pull_down.setGroupIndicator(null);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置SelectPicPopupWindow的View
        popupWindow.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
       // popupWindow.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
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
        popupWindow.showAsDropDown(iv_pull_down);

        elvAdapter.setList(listCategory2);
        elv_pull_down.setAdapter(elvAdapter);
        for(int i = 0; i < elvAdapter.getGroupCount(); i++){

            elv_pull_down.expandGroup(i);

        }
        elv_pull_down.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                groupPosition1=groupPosition;
                listCategoryId2 = listCategory.get(groupPosition).getId();
                viewpager_invitation_pull_down.setCurrentItem(groupPosition);
                popupWindow.dismiss();
                return true;
            }
        });
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    /*
 * 举报请求
 * */
    public void requestJuBao(String postId,String reportId,PopupWindow popupWindow){
        idao.requePostReport(postId,"",reportId, Arad.preferences.getString("memberId"));
        mPopupWindow=popupWindow;
    }
    /*
 * 屏蔽作者请求
 * */
    public void requeMemberBlackListAdd(String reportId,PopupWindow popupWindow){
        idao.requeMemberBlackListAdd(Arad.preferences.getString("memberId"),reportId );
        mPopupWindow=popupWindow;
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
