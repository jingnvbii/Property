package com.ctrl.forum.ui.activity.Invitation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.customview.XListView;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Category;
import com.ctrl.forum.entity.Invitation_listview;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.ThirdKind;
import com.ctrl.forum.ui.adapter.InvitationListViewAdapter;
import com.ctrl.forum.ui.adapter.PinterestAdapter;
import com.ctrl.forum.ui.adapter.ViewPagerAdapter;
import com.ctrl.forum.ui.fragment.InvitationPullDownHaveThirdKindFragment;
import com.ctrl.forum.ui.fragment.InvitationPullDownHaveThirdKindPinterestStyleFragment;
import com.ctrl.forum.ui.fragment.InvitationPullDownNoThirdKindFragment;
import com.ctrl.forum.ui.fragment.InvitationPullDownNoThirdKindPinterestStyleFragment;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
    ViewPager viewpager_invitation_pull_down;
    @InjectView(R.id.lay)
    LinearLayout layout;



    private int PAGE_NUM = 1;
    private float mCurrentCheckedRadioLeft;//当前被选中的RadioButton距离左侧的距离
    private RadioGroup myRadioGroup;
    private int width;
    DisplayMetrics dm;
    private int NUM = 4; // 每行显示个数
    private int hSpacing = 20;// 水平间距


    private List<ThirdKind> kindList;
    private List<Invitation_listview> list;
    private InvitationListViewAdapter invitationAdapter;
    private PinterestAdapter adapter;
    private View headview;
    private TextView tv_search;
    private HorizontalScrollView mHorizontalScrollView;
    private GridView gridView1;
    private PullToRefreshListView listview;
    private InvitationDao idao;
    private String channelId;
    private String checkType;
    private List<Category> listCategory;
    private List<Post> listPost;

    private SparseArray<Fragment> fragments = new SparseArray<>();
    private String styleType;
    private boolean isHaveThirdKind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_pull_down_now);
        ButterKnife.inject(this);
        width = getResources().getDisplayMetrics().widthPixels;
        channelId = getIntent().getStringExtra("channelId");
        initView();
    }

    private void initView() {
        idao = new InvitationDao(this);
        idao.requesAllPostCategory(channelId);
        idao.requestPostListByCategory(Arad.preferences.getString("memberId"), channelId, "0", PAGE_NUM, Constant.PAGE_SIZE);

      /*  InvitationPullDownNoThirdKindFragment invitationPullDownNoThirdKindFragment = InvitationPullDownNoThirdKindFragment.newInstance();
        InvitationPullDownNoThirdKindPinterestStyleFragment invitationPullDownNoThirdKindPinterestStyleFragment = InvitationPullDownNoThirdKindPinterestStyleFragment.newInstance();
        InvitationPullDownHaveThirdKindFragment invitationPullDownHaveThirdKindFragment = InvitationPullDownHaveThirdKindFragment.newInstance();
        InvitationPullDownHaveThirdKindPinterestStyleFragment invitationPullDownHaveThirdKindPinterestStyleFragment = InvitationPullDownHaveThirdKindPinterestStyleFragment.newInstance();
        fragments.put(0, invitationPullDownNoThirdKindFragment);
        fragments.put(1, invitationPullDownNoThirdKindPinterestStyleFragment);
        fragments.put(2, invitationPullDownHaveThirdKindFragment);
        fragments.put(3, invitationPullDownHaveThirdKindPinterestStyleFragment);*/

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 4) {
            MessageUtils.showShortToast(this, "获取当前频道下所有帖子分类成功");
            listCategory = idao.getListCategory();
         //   Log.i("tag", "listCategory---" + listCategory.size());
            styleType = listCategory.get(0).getStyleType();
           // Log.i("tag", "styleType---" + styleType);
           // Log.i("tag", "haveThirdKind---" + listCategory.get(0).getCategorylist().size());
            setRadioGruop();
        }
        if (requestCode == 1) {
            MessageUtils.showShortToast(this, "获取帖子列表成功");
            listPost = idao.getListPost();
            Log.i("tag", "listPost---" + listPost.size());
        }

    }

    private void setRadioGruop() {
        myRadioGroup = new RadioGroup(this);
        myRadioGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(myRadioGroup);
        for (int i = 0; i < listCategory.size(); i++) {
            if (listCategory.get(i).getCategorylist().size() > 0) {
                isHaveThirdKind = true;
            }
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
            if (styleType.equals("4")) {
                if (isHaveThirdKind) {
                    InvitationPullDownHaveThirdKindPinterestStyleFragment invitationPullDownHaveThirdKindPinterestStyleFragment = InvitationPullDownHaveThirdKindPinterestStyleFragment.newInstance(listCategory.get(i).getCategorylist());
                    fragments.put(i,invitationPullDownHaveThirdKindPinterestStyleFragment);
                } else {
                    InvitationPullDownNoThirdKindPinterestStyleFragment invitationPullDownNoThirdKindPinterestStyleFragment = InvitationPullDownNoThirdKindPinterestStyleFragment.newInstance();
                    fragments.put(i, invitationPullDownNoThirdKindPinterestStyleFragment);
                }
            } else {
                if (!isHaveThirdKind) {
                    InvitationPullDownNoThirdKindFragment invitationPullDownNoThirdKindFragment = InvitationPullDownNoThirdKindFragment.newInstance();
                    fragments.put(i, invitationPullDownNoThirdKindFragment);
                } else {
                    InvitationPullDownHaveThirdKindFragment invitationPullDownHaveThirdKindFragment = InvitationPullDownHaveThirdKindFragment.newInstance(listCategory.get(i).getCategorylist(),channelId);
                    fragments.put(i, invitationPullDownHaveThirdKindFragment);
                }
            }
        }
        viewpager_invitation_pull_down.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        viewpager_invitation_pull_down.setCurrentItem(0);
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Map<String, Object> map = (Map<String, Object>) group.getChildAt(checkedId).getTag();
                int radioButtonId = group.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                viewpager_invitation_pull_down.setCurrentItem(radioButtonId);//让下方ViewPager跟随上面的HorizontalScrollView切换
                mCurrentCheckedRadioLeft = rb.getLeft();
                mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (width * 2) / 5, 0);
            }
        });

        viewpager_invitation_pull_down.setOnPageChangeListener(new FragmentOnPageChangeListener());
        viewpager_invitation_pull_down.setOffscreenPageLimit(6);


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

        }
    }

    private void initData() {
        list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Invitation_listview invitation = new Invitation_listview();
            invitation.setName("汪峰" + i + "便利店");
            list.add(invitation);
        }


        kindList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ThirdKind kind = new ThirdKind();
            kind.setKindName("频道:" + i);
            kindList.add(kind);
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

    @Override
    public boolean setupToolBarRightButton(ImageView rightButton) {
        rightButton.setImageResource(R.mipmap.edit);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvitationPullDownActivity.this, InvitationReleaseActivity.class);
                intent.putExtra("channelId", channelId);
                intent.putExtra("checkType", listCategory.get(0).getCheckType());
                startActivity(intent);
                AnimUtil.intentSlidIn(InvitationPullDownActivity.this);
            }
        });

        return true;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }


/*    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.listview:
                Intent intent = new Intent(InvitationPullDownActivity.this, InvitationDetailActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(InvitationPullDownActivity.this);
                break;
        }
    }*/
}
