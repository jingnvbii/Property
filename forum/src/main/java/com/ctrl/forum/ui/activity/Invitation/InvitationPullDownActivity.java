package com.ctrl.forum.ui.activity.Invitation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.customview.XListView;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Category;
import com.ctrl.forum.entity.Invitation_listview;
import com.ctrl.forum.entity.ThirdKind;
import com.ctrl.forum.ui.adapter.InvitationListViewAdapter;
import com.ctrl.forum.ui.adapter.InvitationPullDownGridViewAdapter;
import com.ctrl.forum.ui.adapter.PinterestAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 帖子列表 三级分类下拉页面 activity
 * Created by jason on 2016/4/8
 * */
public class InvitationPullDownActivity extends AppToolBarActivity implements View.OnClickListener,XListView.IXListViewListener{

  /*  @InjectView(R.id.scrollView)
    HorizontalScrollView scrollView;
    @InjectView(R.id.gridView1)
    GridViewForScrollView gridView1;

    @InjectView(R.id.tv_search)//搜索
    TextView tv_search;


    @InjectView(R.id.horizontalScrollView)
    HorizontalScrollView mHorizontalScrollView;//上面的水平滚动控件
    @InjectView(R.id.lay)
    LinearLayout layout;*/
/*
    @InjectView(R.id.listview)//下拉列表
            PullToRefreshListViewForScrollView listview;*/

   /* @InjectView(R.id.invitation_list)//瀑布流下拉列表
            XListView invitation_list;
*/


    private float mCurrentCheckedRadioLeft;//当前被选中的RadioButton距离左侧的距离
    private RadioGroup myRadioGroup;
    private int width;
    DisplayMetrics dm;
    private int NUM = 4; // 每行显示个数
    private int hSpacing = 20;// 水平间距



    private List<ThirdKind>kindList;
    private List<Invitation_listview> list;
    private InvitationListViewAdapter invitationAdapter;
    private PinterestAdapter adapter;
    private View headview;
    private TextView tv_search;
    private LinearLayout layout;
    private HorizontalScrollView mHorizontalScrollView;
    private GridView gridView1;
    private PullToRefreshListView listview;
    private InvitationDao idao;
    private String channelId;
    private List<Category> listCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_style_3);
        ButterKnife.inject(this);
        width = getResources().getDisplayMetrics().widthPixels;
      //  headview = LayoutInflater.from(this).inflate(R.layout.fragment_invitation_header, null);
        listview = (PullToRefreshListView) findViewById(R.id.invitation_list);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        View headview = getLayoutInflater().inflate(R.layout.fragment_invitation_header, listview, false);
        headview.setLayoutParams(layoutParams);
        ListView lv = listview.getRefreshableView();
        tv_search=(TextView)headview.findViewById(R.id.tv_search);
        layout=(LinearLayout)headview.findViewById(R.id.lay);
        gridView1=(GridView)headview.findViewById(R.id.gridView1);
        mHorizontalScrollView=(HorizontalScrollView)headview.findViewById(R.id.horizontalScrollView);

        channelId=getIntent().getStringExtra("channelId");


        initView();
        initData();
        getScreenDen();
        setValue();


        lv.addHeaderView(headview);
        //不显示滚动条
        lv.setFastScrollEnabled(false);
        adapter = new PinterestAdapter(this);
        adapter.setList(list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_lv=new Intent(InvitationPullDownActivity.this,InvitationPinerestGalleyActivity.class);
                startActivity(intent_lv);
                AnimUtil.intentSlidIn(InvitationPullDownActivity.this);
            }
        });


    /*    invitation_list.addHeaderView(headview);
        invitation_list.setPullLoadEnable(true);
        invitation_list.setXListViewListener(this);
        adapter = new PinterestAdapter(this);
        adapter.setList(list);
        invitation_list.setAdapter(adapter);
        invitation_list.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(getActivity(), PostDetailActivity.class);
                i.putExtra("forumPostId", dao.getForumPostList().get(position - 2).getId());
                i.putExtra("proprietorId",proprietorId);
                startActivity(i);
                resum = 1;
            }
        });*/
      /*  invitationAdapter=new InvitationListViewAdapter(this);
        invitationAdapter.setList(list);
        listview.setAdapter(invitationAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InvitationPullDownActivity.this, InvitationDetailActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(InvitationPullDownActivity.this);
            }
        });
*/



    }

    private void initView() {
        idao=new InvitationDao(this);
        idao.requesAllPostCategory(channelId);
        tv_search.setOnClickListener(this);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==4){
            MessageUtils.showShortToast(this,"获取当前频道下所有帖子分类成功");
            listCategory = idao.getListCategory();
            setRadioGruop();
        }

    }

    private void setRadioGruop() {
        myRadioGroup = new RadioGroup(this);
        myRadioGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(myRadioGroup);
        for (int i = 0; i <listCategory.size(); i++) {
            RadioButton radio = new RadioButton(this);
            radio.setBackgroundResource(R.drawable.top_category_selector);
            LinearLayout.LayoutParams l;
            if(listCategory.size()==1){
                l = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }else if(listCategory.size()==2){
                l = new LinearLayout.LayoutParams(width/2, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }else if(listCategory.size()==3){
                l = new LinearLayout.LayoutParams(width/3, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }else if(listCategory.size()==4){
                l = new LinearLayout.LayoutParams(width/4, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }else {
            l = new LinearLayout.LayoutParams((width-30)/5, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
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
            //  myRadioGroup.addView(view);
          /*  ConvenientServiceFragment fragment = ConvenientServiceFragment.newInstance(dao.getCompanyCategoryList().get(i).getAroundCompanyList());
            fragments.put(i, fragment);*/
        }
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Map<String, Object> map = (Map<String, Object>) group.getChildAt(checkedId).getTag();
                int radioButtonId = group.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                //  mViewpager.setCurrentItem(radioButtonId);//让下方ViewPager跟随上面的HorizontalScrollView切换
                mCurrentCheckedRadioLeft = rb.getLeft();
                mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (width * 2) / 5, 0);
            }
        });

    }

    private void initData() {
        list=new ArrayList<>();

        for(int i=0;i<20;i++){
            Invitation_listview invitation=new Invitation_listview();
            invitation.setName("汪峰"+i+"便利店");
            list.add(invitation);
        }


        kindList=new ArrayList<>();
        for(int i=0;i<8;i++){
            ThirdKind kind=new ThirdKind();
            kind.setKindName("频道:"+i);
            kindList.add(kind);
        }
    }

    private void setValue() {
        InvitationPullDownGridViewAdapter adapter = new InvitationPullDownGridViewAdapter(this);
        adapter.setList(kindList);
        int count = adapter.getCount();
        int columns = (count % 2 == 0) ? count / 2 : count / 2 + 1;
        gridView1.setAdapter(adapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columns * dm.widthPixels / NUM,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        gridView1.setLayoutParams(params);
        gridView1.setColumnWidth(dm.widthPixels / NUM);
        // gridView.setHorizontalSpacing(hSpacing);
        gridView1.setStretchMode(GridView.NO_STRETCH);
        if (count <= 3) {
            gridView1.setNumColumns(count);
        } else {
            gridView1.setNumColumns(columns);
        }

        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    private void getScreenDen() {
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
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
                Intent intent=new Intent(InvitationPullDownActivity.this,InvitationReleaseActivity.class);
                intent.putExtra("channelId",channelId);
                startActivity(intent);
                AnimUtil.intentSlidIn(InvitationPullDownActivity.this);
            }
        });

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                Intent intent=new Intent(this,InvitationSearchActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
        }
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
