package com.ctrl.forum.ui.activity.Invitation;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.customview.CustomViewPager;
import com.ctrl.forum.customview.XListView;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Category;
import com.ctrl.forum.entity.Invitation_listview;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.ThirdKind;
import com.ctrl.forum.ui.adapter.ExpandableListViewAllCategroyAdapter;
import com.ctrl.forum.ui.adapter.JasonViewPagerAdapter;
import com.ctrl.forum.ui.fragment.InvitationPullDownHaveThirdKindFragment;
import com.ctrl.forum.ui.fragment.InvitationPullDownHaveThirdKindPinterestStyleFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子列表 三级分类下拉页面 activity
 * Created by jason on 2016/4/8
 */
public class InvitationPullDownActivity extends AppToolBarActivity implements View.OnClickListener, XListView.IXListViewListener {
    @InjectView(R.id.viewpager_invitation_pull_down)
    CustomViewPager viewpager_invitation_pull_down;
    @InjectView(R.id.lay)
    LinearLayout layout;
    @InjectView(R.id.iv_pull_down)//向下箭头
            ImageView iv_pull_down;
    @InjectView(R.id.horizontalScrollView_pull)
    HorizontalScrollView horizontalScrollView;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_pull_down_now);
        ButterKnife.inject(this);
        width = getResources().getDisplayMetrics().widthPixels;
        channelId = getIntent().getStringExtra("channelId");
        elvAdapter = new ExpandableListViewAllCategroyAdapter(this);
        initView();
    }

    private void initView() {
        idao = new InvitationDao(this);
        idao.requesPostCategory(channelId, "1", "0");
        iv_pull_down.setOnClickListener(this);
     //   viewpager_invitation_pull_down.setScrollble(false);//禁止viewpager滚动

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
        for (int i = 0; i < listCategory.size(); i++) {
            RadioButton radio = new RadioButton(this);
            radio.setBackgroundResource(R.drawable.top_category_selector);
            LinearLayout.LayoutParams l;
            if (listCategory.size() == 1) {
                l = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            } else if (listCategory.size() == 2) {
                l = new LinearLayout.LayoutParams(width / 2, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            } else if (listCategory.size() == 3) {
                l = new LinearLayout.LayoutParams(width / 3, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            } else if (listCategory.size() == 4) {
                l = new LinearLayout.LayoutParams(width / 4, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            } else {
                l = new LinearLayout.LayoutParams((width - 30) / 5, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }
            radio.setLayoutParams(l);
            radio.setGravity(Gravity.CENTER);
            //  radio.setPadding(20, 20, 20, 20);
            radio.setId(i);
            radio.setButtonDrawable(getResources().getDrawable(R.color.white));
            radio.setTextSize(16.0f);
            radio.setTextColor(getResources().getColor(R.color.text_black));//选择器
            radio.setText(listCategory.get(i).getName());//动态设置
            radio.setSingleLine(true);
            radio.setEllipsize(TextUtils.TruncateAt.END);
            radio.setTag(i);
            if (i == 0) {
                radio.setChecked(true);
            }
            View view = new View(this);
            LinearLayout.LayoutParams v = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(v);
            //  view.setBackgroundColor(Color.parseColor("#cccccc"));
            myRadioGroup.addView(radio);
            myRadioGroup.addView(view);
            styleType = listCategory.get(i).getStyleType();
            if (styleType.equals("3")) {
                invitationPullDownHaveThirdKindPinterestStyleFragment = InvitationPullDownHaveThirdKindPinterestStyleFragment.newInstance(listCategory.get(i).getId(),null);
                fragments.add(invitationPullDownHaveThirdKindPinterestStyleFragment);
            } else {
               invitationPullDownHaveThirdKindFragment = InvitationPullDownHaveThirdKindFragment.newInstance(listCategory.get(i).getId(), listCategory.get(i).getStyleType(),null);
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
                List<Fragment> list = new ArrayList<Fragment>();
                for(int i=0;i<listCategory2.size();i++){
                    styleType=listCategory2.get(i).getStyleType();
                    if (styleType.equals("3")) {
                       list.add(InvitationPullDownHaveThirdKindPinterestStyleFragment.newInstance(listCategory2.get(i).getId(), thirdKindId));
                    } else {
                       list.add(InvitationPullDownHaveThirdKindFragment.newInstance(listCategory2.get(i).getId(), listCategory2.get(i).getStyleType(),thirdKindId));
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
            mGroupPostion=position;

        }
    }


    @Override
    public String setupToolBarTitle() {
        TextView tv_title = getmTitle();
        tv_title.setText("帖子列表");
        tv_title.setTextColor(getResources().getColor(R.color.text_black));
        return "帖子列表";
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


    public void request(int groupPosition,int position,String thirdId){
        mGroupPostion=groupPosition;
        mChildrenPosition=position;
        thirdKindId = thirdId;
        viewpager_invitation_pull_down.setCurrentItem(groupPosition);
        viewPagerAdapter.reLoad();
        popupWindow.dismiss();
    }

    @Override
    public boolean setupToolBarRightButton(ImageView rightButton) {
        rightButton.setImageResource(R.mipmap.edit);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvitationPullDownActivity.this, InvitationReleaseActivity.class);
                intent.putExtra("channelId", channelId);
                startActivity(intent);
                AnimUtil.intentSlidIn(InvitationPullDownActivity.this);
            }
        });

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pull_down:
                if(listCategory.size()>0){
                    listCategory.clear();
                }
                idao.requesAllPostCategory(channelId);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1111&&resultCode==RESULT_OK){
        }
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




}
