package com.ctrl.forum.ui.activity.Invitation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.entity.ThirdKind;
import com.ctrl.forum.ui.adapter.InvitationPullDownGridViewAdapter;
import com.ctrl.forum.ui.viewpage.CycleViewPager;
import com.ctrl.forum.ui.viewpage.ViewFactory;
import com.ctrl.forum.utils.DemoUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子列表  二级分类下拉页面 activity
 * Created by jason on 2016/4/8
 * */
public class InvitationSecondPullDownActivity extends AppToolBarActivity implements View.OnClickListener{

    @InjectView(R.id.scrollView)
    HorizontalScrollView scrollView;
    @InjectView(R.id.gridView1)
    GridView gridView1;

    @InjectView(R.id.tv_search)//搜索
    TextView tv_search;

    @InjectView(R.id.framelayout)
    FrameLayout framelayout;

    @InjectView(R.id.horizontalScrollView)
    HorizontalScrollView mHorizontalScrollView;//上面的水平滚动控件
    @InjectView(R.id.lay)
    LinearLayout layout;


    private float mCurrentCheckedRadioLeft;//当前被选中的RadioButton距离左侧的距离
    private RadioGroup myRadioGroup;
    private int width;

    DisplayMetrics dm;
    private int NUM = 4; // 每行显示个数
    private int hSpacing = 20;// 水平间距
    private View vhdf;


    private List<ThirdKind>kindList;
    private CycleViewPager cycleViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_pull_down_second);
        ButterKnife.inject(this);
        width = getResources().getDisplayMetrics().widthPixels;
        initData();
        getScreenDen();
        setValue();

        // 三句话 调用轮播广告
        vhdf = getLayoutInflater().inflate(R.layout.viewpage, null);
        cycleViewPager = (CycleViewPager)getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
        ViewFactory.initialize(this, vhdf, cycleViewPager, DemoUtil.cycData());
        framelayout.addView(vhdf);


        initView();

    }

    private void initView() {
        tv_search.setOnClickListener(this);
        myRadioGroup = new RadioGroup(this);
        myRadioGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(myRadioGroup);
        for (int i = 0; i <10; i++) {
            RadioButton radio = new RadioButton(this);
            radio.setBackgroundResource(R.drawable.top_category_selector);
            LinearLayout.LayoutParams l;
           /* if(dao.getCompanyCategoryList().size()==1){
                l = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }else if(dao.getCompanyCategoryList().size()==2){
                l = new LinearLayout.LayoutParams(width/2, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }else if(dao.getCompanyCategoryList().size()==3){
                l = new LinearLayout.LayoutParams(width/3, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }else {*/
                l = new LinearLayout.LayoutParams((width-30)/5, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
           // }
            radio.setLayoutParams(l);
            radio.setGravity(Gravity.CENTER);
          //  radio.setPadding(20, 20, 20, 20);
            radio.setId(i);
            radio.setButtonDrawable(getResources().getDrawable(R.color.white));
            radio.setTextSize(16.0f);
            radio.setTextColor(getResources().getColor(R.color.text_black));//选择器
            radio.setText("分类");//动态设置
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
                mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (width *2)/5, 0);
            }
        });

    }

    private void initData() {
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
             /*   Intent intent=new Intent(getActivity(), InvitationPullDownActivity.class);
                getActivity().startActivity(intent);*/
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
                Intent intent=new Intent(InvitationSecondPullDownActivity.this,InvitationReleaseActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(InvitationSecondPullDownActivity.this);
            }
        });

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                Intent intent=new Intent(this,InvitationDetailActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
        }
    }
}
