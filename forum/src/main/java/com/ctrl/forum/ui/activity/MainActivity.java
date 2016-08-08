package com.ctrl.forum.ui.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.ctrl.forum.R;
import com.ctrl.forum.base.MyApplication;
import com.ctrl.forum.customview.CircleRadioView;
import com.ctrl.forum.dao.EditDao;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.entity.NavigationBar;
import com.ctrl.forum.ui.fragment.InvitationFragment;
import com.ctrl.forum.ui.fragment.MyFragment;
import com.ctrl.forum.ui.fragment.PlotFragment;
import com.ctrl.forum.ui.fragment.RimFragment;
import com.ctrl.forum.ui.fragment.StroeFragment;
import com.ctrl.forum.utils.BitmapUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends ToolBarActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    @InjectView(R.id.ll_rb)
    RadioGroup ll_rb;

    private MemberInfo memberInfo;
    private EditDao edao;
    public static Boolean isFrist = false;
    private CircleRadioView rb1;//帖子按钮
    private CircleRadioView rb2;//商城按钮
    private CircleRadioView rb3;//小区按钮
    private CircleRadioView rb4;//周边按钮
    private CircleRadioView rb5;//我  按钮1
    private InvitationFragment invitationFragment;
    private StroeFragment storeFragment;
    private PlotFragment plotFragment;
    private RimFragment rimFragment;
    private MyFragment myFragment;
    private FragmentManager fragmentManager;
    private ArrayList<Drawable> listDrawable;
    private ArrayList<Drawable> listDrawable2;
    private ArrayList<NavigationBar> listNavigation;
    private Drawable drawable;
    private Drawable drawable2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ll_rb.setVisibility(View.VISIBLE);
            if (msg.what == 2) {
                if (rb1.isChecked()) {
                    drawable.setBounds(0, 0, 50, 50);
                    rb1.setCompoundDrawables(null, listDrawable.get(0), null, null);
                } else {
                    drawable2.setBounds(0, 0, 50, 50);
                    rb1.setCompoundDrawables(null, listDrawable2.get(0), null, null);
                }
                if (rb2.isChecked()) {
                    drawable.setBounds(0, 0, 50, 50);
                    rb2.setCompoundDrawables(null, listDrawable.get(1), null, null);
                } else {
                    drawable2.setBounds(0, 0, 50, 50);
                    rb2.setCompoundDrawables(null, listDrawable2.get(1), null, null);
                }
                if (rb3.isChecked()) {
                    drawable.setBounds(0, 0, 50,50);
                    rb3.setCompoundDrawables(null, listDrawable.get(2), null, null);
                } else {
                    drawable2.setBounds(0, 0, 50, 50);
                    rb3.setCompoundDrawables(null, listDrawable2.get(2), null, null);
                }
                if (rb4.isChecked()) {
                    drawable.setBounds(0, 0, 50, 50);
                    rb4.setCompoundDrawables(null, listDrawable.get(3), null, null);
                } else {
                    drawable2.setBounds(0, 0, 50, 50);
                    rb4.setCompoundDrawables(null, listDrawable2.get(3), null, null);
                }
                if (rb5.isChecked()) {
                    drawable.setBounds(0, 0, 50, 50);
                    rb5.setCompoundDrawables(null, listDrawable.get(4), null, null);
                    rb5.setNum(10);
                } else {
                    drawable2.setBounds(0, 0, 50, 50);
                    rb5.setCompoundDrawables(null, listDrawable2.get(4), null, null);
                    rb5.setNum(15);
                }
            }
            if (msg.what == 1) {
                int size = listNavigation.size();
                if (size == 5) {
                    for (int i = 0; i < 5; i++) {
                        drawable = listDrawable.get(i);
                        drawable2 = listDrawable2.get(i);
                        switch (i) {
                            case 0:
                                //设置默认的fragment
                                setDefaultFragment();
                                rb1.setText(listNavigation.get(0).getKindName());
                                if (rb1.isChecked()) {
                                    drawable.setBounds(0, 0, 50, 50);
                                    rb1.setCompoundDrawables(null, drawable, null, null);
                                } else {
                                    drawable2.setBounds(0, 0, 50, 50);
                                    rb1.setCompoundDrawables(null, listDrawable2.get(0), null, null);
                                }
                                break;
                            case 1:
                                rb2.setText(listNavigation.get(1).getKindName());
                                if (rb2.isChecked()) {
                                    drawable.setBounds(0, 0, 50, 50);
                                    rb2.setCompoundDrawables(null, drawable, null, null);
                                } else {
                                    drawable2.setBounds(0, 0, 50, 50);
                                    rb2.setCompoundDrawables(null, listDrawable2.get(1), null, null);
                                }
                                break;
                            case 2:
                                rb3.setText(listNavigation.get(2).getKindName());
                                if (rb3.isChecked()) {
                                    drawable.setBounds(0, 0, 50, 50);
                                    rb3.setCompoundDrawables(null, drawable, null, null);
                                } else {
                                    drawable2.setBounds(0, 0, 50, 50);
                                    rb3.setCompoundDrawables(null, listDrawable2.get(2), null, null);
                                }
                                break;
                            case 3:
                                rb4.setText(listNavigation.get(3).getKindName());
                                if (rb4.isChecked()) {
                                    drawable.setBounds(0, 0, 50, 50);
                                    rb4.setCompoundDrawables(null, drawable, null, null);
                                } else {
                                    drawable2.setBounds(0, 0, 50, 50);
                                    rb4.setCompoundDrawables(null, listDrawable2.get(3), null, null);
                                }
                                break;
                            case 4:
                                rb5.setText(listNavigation.get(4).getKindName());
                                if (rb5.isChecked()) {
                                    drawable.setBounds(0, 0, 50, 50);
                                    rb5.setCompoundDrawables(null, drawable, null, null);
                                    //rb5.setNum(16);
                                } else {
                                    drawable2.setBounds(0, 0, 50, 50);
                                    rb5.setCompoundDrawables(null, listDrawable2.get(4), null, null);
                                    //rb5.setNum(203); //角标，消息的数量
                                }
                                break;
                        }
                    }
                }
            }
            showProgress(false);
        }


    };
    private MyBroadcastReciver myBroadCasrReciver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        edao = new EditDao(this);
        myBroadCasrReciver=new MyBroadcastReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.message");
        this.registerReceiver(myBroadCasrReciver, intentFilter);
        showProgress(true);
        initView();
        initData();
        MyApplication.getInstance().addActivity(this);
        if (Arad.preferences.getString("memberId")!=null && !Arad.preferences.getString("memberId").equals("")){
            getNet();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadCasrReciver);
    }

    private void initData() {
        listNavigation = (ArrayList<NavigationBar>) getIntent().getSerializableExtra("listNagationBar");
        listDrawable = new ArrayList<>();
        listDrawable2 = new ArrayList<>();
      /*  Resources res = getResources();
        for (int i = 0; i < listNavigation.size(); i++) {
            if (listNavigation.get(i).getCommentCode().equals("0")) {
                drawable2 = res.getDrawable(R.drawable.guangchang_gray);
                listDrawable2.add(drawable2);
            }
            if (listNavigation.get(i).getCommentCode().equals("1")) {
                drawable2 = res.getDrawable(R.drawable.shangcheng_gray);
                listDrawable2.add(drawable2);
            }
            if (listNavigation.get(i).getCommentCode().equals("2")) {
                drawable2 = res.getDrawable(R.drawable.xiaoqu_gray);
                listDrawable2.add(drawable2);
            }
            if (listNavigation.get(i).getCommentCode().equals("3")) {
                drawable2 = res.getDrawable(R.drawable.zhoubian_gray);
                listDrawable2.add(drawable2);
            }
            if (listNavigation.get(i).getCommentCode().equals("4")) {
                drawable2 = res.getDrawable(R.drawable.my_gray);
                listDrawable2.add(drawable2);
            }
        }*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < listNavigation.size(); i++) {
                    drawable = BitmapUtils.getDrawable(listNavigation.get(i).getKindIconSelected());
                    drawable2 = BitmapUtils.getDrawable(listNavigation.get(i).getKindIcon());
                    listDrawable.add(drawable);
                    listDrawable2.add(drawable2);
                }
                mHandler.sendEmptyMessage(1);
            }
        }).start();
    }

    private void initView() {
        ll_rb.setVisibility(View.GONE);

        rb1 = (CircleRadioView) findViewById(R.id.rb_1);
        rb2 = (CircleRadioView) findViewById(R.id.rb_2);
        rb3 = (CircleRadioView) findViewById(R.id.rb_3);
        rb4 = (CircleRadioView) findViewById(R.id.rb_4);
        rb5 = (CircleRadioView) findViewById(R.id.rb_5);
        rb1.setOnClickListener(this);
        rb2.setOnClickListener(this);
        rb3.setOnClickListener(this);
        rb4.setOnClickListener(this);
        rb5.setOnClickListener(this);
        rb1.setOnCheckedChangeListener(this);
        rb2.setOnCheckedChangeListener(this);
        rb3.setOnCheckedChangeListener(this);
        rb4.setOnCheckedChangeListener(this);
        rb5.setOnCheckedChangeListener(this);

    }

    private void setDefaultFragment() {
        rb1.setChecked(true);
        setTabSelection(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_1:
                //  rb1.setChecked(true);
                //  mHandler.sendEmptyMessage(1);
                setTabSelection(0);
                break;
            case R.id.rb_2:
                //    rb2.setChecked(true);
                //   mHandler.sendEmptyMessage(1);
                setTabSelection(1);
                break;
            case R.id.rb_3:
                //  rb3.setChecked(true);
                //   mHandler.sendEmptyMessage(1);
                setTabSelection(2);
                break;
            case R.id.rb_4:
                //   rb4.setChecked(true);
                // mHandler.sendEmptyMessage(1);
                setTabSelection(3);
                break;
            case R.id.rb_5:
                //  rb5.setChecked(true);
                //  mHandler.sendEmptyMessage(1);
                setTabSelection(4);
                break;
        }


    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {
        fragmentManager = getSupportFragmentManager();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (listNavigation.get(0).getCommentCode().equals("0")) {
                    if (invitationFragment == null) {
                        // invitationFragment，则创建一个并添加到界面上
                        invitationFragment = InvitationFragment.newInstance();
                        transaction.add(R.id.content, invitationFragment);
                    } else {
                        // 如果MessageFragment不为空，则直接将它显示出来
                        transaction.show(invitationFragment);
                    }
                }
                if (listNavigation.get(0).getCommentCode().equals("1")) {
                    if (storeFragment == null) {
                        // 如果ContactsFragment为空，则创建一个并添加到界面上
                        storeFragment = StroeFragment.newInstance();
                        transaction.add(R.id.content, storeFragment);
                    } else {
                        // 如果ContactsFragment不为空，则直接将它显示出来
                        transaction.show(storeFragment);
                    }
                }
                if(Arad.preferences.getString("memberId")==null){

                }
                if (listNavigation.get(0).getCommentCode().equals("2")) {
                    if (plotFragment == null) {
                        // 如果NewsFragment为空，则创建一个并添加到界面上
                        plotFragment = PlotFragment.newInstance();
                        Bundle bundle = new Bundle();
                        bundle.putString("str", "小区");
                        plotFragment.setArguments(bundle);
                        transaction.add(R.id.content, plotFragment);
                    } else {
                        // 如果NewsFragment不为空，则直接将它显示出来
                        transaction.show(plotFragment);
                    }
                }
                if (listNavigation.get(0).getCommentCode().equals("3")) {
                    if (rimFragment == null) {
                        // 如果SettingFragment为空，则创建一个并添加到界面上
                        rimFragment = RimFragment.newInstance();
                        transaction.add(R.id.content, rimFragment);
                    } else {
                        // 如果SettingFragment不为空，则直接将它显示出来
                        transaction.show(rimFragment);
                    }
                }
                if (listNavigation.get(0).getCommentCode().equals("4")) {
                    if (myFragment == null) {
                        // 如果SettingFragment为空，则创建一个并添加到界面上
                        myFragment = MyFragment.newInstance();
                        transaction.add(R.id.content, myFragment);
                    } else {
                        // 如果SettingFragment不为空，则直接将它显示出来
                        transaction.show(myFragment);

                    }
                }
                break;
            case 1:
                if (listNavigation.get(1).getCommentCode().equals("0")) {
                    if (invitationFragment == null) {
                        // invitationFragment，则创建一个并添加到界面上
                        invitationFragment = InvitationFragment.newInstance();
                        transaction.add(R.id.content, invitationFragment);
                    } else {
                        // 如果MessageFragment不为空，则直接将它显示出来
                        transaction.show(invitationFragment);
                    }
                }
                if (listNavigation.get(1).getCommentCode().equals("1")) {
                    if (storeFragment == null) {
                        // 如果ContactsFragment为空，则创建一个并添加到界面上
                        storeFragment = StroeFragment.newInstance();
                        transaction.add(R.id.content, storeFragment);
                    } else {
                        // 如果ContactsFragment不为空，则直接将它显示出来
                        transaction.show(storeFragment);
                    }
                }
                if (listNavigation.get(1).getCommentCode().equals("2")) {
                    if (plotFragment == null) {
                        // 如果NewsFragment为空，则创建一个并添加到界面上
                        plotFragment = PlotFragment.newInstance();
                        Bundle bundle = new Bundle();
                        bundle.putString("str", "小区");
                        plotFragment.setArguments(bundle);
                        transaction.add(R.id.content, plotFragment);
                    } else {
                        // 如果NewsFragment不为空，则直接将它显示出来
                        transaction.show(plotFragment);
                    }
                }
                if (listNavigation.get(1).getCommentCode().equals("3")) {
                    if (rimFragment == null) {
                        // 如果SettingFragment为空，则创建一个并添加到界面上
                        rimFragment = RimFragment.newInstance();
                        transaction.add(R.id.content, rimFragment);
                    } else {
                        // 如果SettingFragment不为空，则直接将它显示出来
                        transaction.show(rimFragment);
                    }
                }
                if (listNavigation.get(1).getCommentCode().equals("4")) {
                    if (myFragment == null) {
                        // 如果SettingFragment为空，则创建一个并添加到界面上
                        myFragment = MyFragment.newInstance();
                        transaction.add(R.id.content, myFragment);
                    } else {
                        // 如果SettingFragment不为空，则直接将它显示出来
                        transaction.show(myFragment);
                    }
                }
                break;
            case 2:
                if (listNavigation.get(2).getCommentCode().equals("0")) {
                    if (invitationFragment == null) {
                        // invitationFragment，则创建一个并添加到界面上
                        invitationFragment = InvitationFragment.newInstance();
                        transaction.add(R.id.content, invitationFragment);
                    } else {
                        // 如果MessageFragment不为空，则直接将它显示出来
                        transaction.show(invitationFragment);
                    }
                }
                if (listNavigation.get(2).getCommentCode().equals("1")) {
                    if (storeFragment == null) {
                        // 如果ContactsFragment为空，则创建一个并添加到界面上
                        storeFragment = StroeFragment.newInstance();
                        transaction.add(R.id.content, storeFragment);
                    } else {
                        // 如果ContactsFragment不为空，则直接将它显示出来
                        transaction.show(storeFragment);
                    }
                }
                if (listNavigation.get(2).getCommentCode().equals("2")) {
                    if (plotFragment == null) {
                        // 如果NewsFragment为空，则创建一个并添加到界面上
                        plotFragment = PlotFragment.newInstance();
                        Bundle bundle = new Bundle();
                        bundle.putString("str", "小区");
                        plotFragment.setArguments(bundle);
                        transaction.add(R.id.content, plotFragment);
                    } else {
                        // 如果NewsFragment不为空，则直接将它显示出来
                        transaction.show(plotFragment);
                    }
                }
                if (listNavigation.get(2).getCommentCode().equals("3")) {
                    if (rimFragment == null) {
                        // 如果SettingFragment为空，则创建一个并添加到界面上
                        rimFragment = RimFragment.newInstance();
                        transaction.add(R.id.content, rimFragment);
                    } else {
                        // 如果SettingFragment不为空，则直接将它显示出来
                        transaction.show(rimFragment);
                    }
                }
                if (listNavigation.get(2).getCommentCode().equals("4")) {
                    if (myFragment == null) {
                        // 如果SettingFragment为空，则创建一个并添加到界面上
                        myFragment = MyFragment.newInstance();
                        transaction.add(R.id.content, myFragment);
                    } else {
                        // 如果SettingFragment不为空，则直接将它显示出来
                        transaction.show(myFragment);
                    }
                }
                break;
            case 3:
                if (listNavigation.get(3).getCommentCode().equals("0")) {
                    if (invitationFragment == null) {
                        // invitationFragment，则创建一个并添加到界面上
                        invitationFragment = InvitationFragment.newInstance();
                        transaction.add(R.id.content, invitationFragment);
                    } else {
                        // 如果MessageFragment不为空，则直接将它显示出来
                        transaction.show(invitationFragment);
                    }
                }
                if (listNavigation.get(3).getCommentCode().equals("1")) {
                    if (storeFragment == null) {
                        // 如果ContactsFragment为空，则创建一个并添加到界面上
                        storeFragment = StroeFragment.newInstance();
                        transaction.add(R.id.content, storeFragment);
                    } else {
                        // 如果ContactsFragment不为空，则直接将它显示出来
                        transaction.show(storeFragment);
                    }
                }
                if (listNavigation.get(3).getCommentCode().equals("2")) {
                    if (plotFragment == null) {
                        // 如果NewsFragment为空，则创建一个并添加到界面上
                        plotFragment = PlotFragment.newInstance();
                        Bundle bundle = new Bundle();
                        bundle.putString("str", "小区");
                        plotFragment.setArguments(bundle);
                        transaction.add(R.id.content, plotFragment);
                    } else {
                        // 如果NewsFragment不为空，则直接将它显示出来
                        transaction.show(plotFragment);
                    }
                }
                if (listNavigation.get(3).getCommentCode().equals("3")) {
                    if (rimFragment == null) {
                        // 如果SettingFragment为空，则创建一个并添加到界面上
                        rimFragment = RimFragment.newInstance();
                        transaction.add(R.id.content, rimFragment);
                    } else {
                        // 如果SettingFragment不为空，则直接将它显示出来
                        transaction.show(rimFragment);
                    }
                }
                if (listNavigation.get(3).getCommentCode().equals("4")) {
                    if (myFragment == null) {
                        // 如果SettingFragment为空，则创建一个并添加到界面上
                        myFragment = MyFragment.newInstance();
                        transaction.add(R.id.content, myFragment);
                    } else {
                        // 如果SettingFragment不为空，则直接将它显示出来
                        transaction.show(myFragment);
                    }
                }
                break;
            case 4:
                if (listNavigation.get(4).getCommentCode().equals("0")) {
                    if (invitationFragment == null) {
                        // invitationFragment，则创建一个并添加到界面上
                        invitationFragment = InvitationFragment.newInstance();
                        transaction.add(R.id.content, invitationFragment);
                    } else {
                        // 如果MessageFragment不为空，则直接将它显示出来
                        transaction.show(invitationFragment);
                    }
                }
                if (listNavigation.get(4).getCommentCode().equals("1")) {
                    if (storeFragment == null) {
                        // 如果ContactsFragment为空，则创建一个并添加到界面上
                        storeFragment = StroeFragment.newInstance();
                        transaction.add(R.id.content, storeFragment);
                    } else {
                        // 如果ContactsFragment不为空，则直接将它显示出来
                        transaction.show(storeFragment);
                    }
                }
                if (listNavigation.get(4).getCommentCode().equals("2")) {
                    if (plotFragment == null) {
                        // 如果NewsFragment为空，则创建一个并添加到界面上
                        plotFragment = PlotFragment.newInstance();
                        Bundle bundle = new Bundle();
                        bundle.putString("str", "小区");
                        plotFragment.setArguments(bundle);
                        transaction.add(R.id.content, plotFragment);
                    } else {
                        // 如果NewsFragment不为空，则直接将它显示出来
                        transaction.show(plotFragment);
                    }
                }
                if (listNavigation.get(4).getCommentCode().equals("3")) {
                    if (rimFragment == null) {
                        // 如果SettingFragment为空，则创建一个并添加到界面上
                        rimFragment = RimFragment.newInstance();
                        transaction.add(R.id.content, rimFragment);
                    } else {
                        // 如果SettingFragment不为空，则直接将它显示出来
                        transaction.show(rimFragment);
                    }
                }
                if (listNavigation.get(4).getCommentCode().equals("4")) {
                    if (myFragment == null) {
                        // 如果SettingFragment为空，则创建一个并添加到界面上
                        myFragment = MyFragment.newInstance();
                        transaction.add(R.id.content, myFragment);
                    } else {
                        // 如果SettingFragment不为空，则直接将它显示出来
                        transaction.show(myFragment);
                    }
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (invitationFragment != null) {
            transaction.hide(invitationFragment);
        }
        if (storeFragment != null) {
            transaction.hide(storeFragment);
        }
        if (plotFragment != null) {
            transaction.hide(plotFragment);
        }
        if (rimFragment != null) {
            transaction.hide(rimFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (rb1.isChecked()) {
                if(listNavigation.get(0).getCommentCode().equals("1")){
                    rb1.setTextColor(Color.parseColor("#ce1010"));
                }
                listDrawable.get(0).setBounds(0, 0, 50, 50);
                rb1.setCompoundDrawables(null, listDrawable.get(0), null, null);
        } else {
            if(listNavigation.get(0).getCommentCode().equals("1")){
                rb1.setTextColor(Color.parseColor("#949494"));
            }
            drawable2.setBounds(0, 0, 50, 50);
            rb1.setCompoundDrawables(null, listDrawable2.get(0), null, null);
        }
        if (rb2.isChecked()) {
            if(listNavigation.get(1).getCommentCode().equals("1")){
                rb2.setTextColor(Color.parseColor("#ce1010"));
            }
            listDrawable.get(1).setBounds(0, 0, 50, 50);
            rb2.setCompoundDrawables(null, listDrawable.get(1), null, null);
        } else {
            if(listNavigation.get(1).getCommentCode().equals("1")){
                rb2.setTextColor(Color.parseColor("#949494"));
            }
            drawable2.setBounds(0, 0, 50, 50);
            rb2.setCompoundDrawables(null, listDrawable2.get(1), null, null);
        }
        if (rb3.isChecked()) {
            if(listNavigation.get(2).getCommentCode().equals("1")){
                rb3.setTextColor(Color.parseColor("#ce1010"));
            }
            listDrawable.get(2).setBounds(0, 0, 50, 50);
            rb3.setCompoundDrawables(null, listDrawable.get(2), null, null);
        } else {
            if(listNavigation.get(2).getCommentCode().equals("1")){
                rb3.setTextColor(Color.parseColor("#949494"));
            }
            drawable2.setBounds(0, 0, 50,50);
            rb3.setCompoundDrawables(null, listDrawable2.get(2), null, null);
        }
        if (rb4.isChecked()) {
            if(listNavigation.get(3).getCommentCode().equals("1")){
                rb4.setTextColor(Color.parseColor("#ce1010"));
            }
            if(listDrawable.get(3)!=null)
            listDrawable.get(3).setBounds(0, 0, 50, 50);
            rb4.setCompoundDrawables(null, listDrawable.get(3), null, null);
        } else {
            if(listNavigation.get(3).getCommentCode().equals("1")){
                rb4.setTextColor(Color.parseColor("#949494"));
            }
            drawable2.setBounds(0, 0, 50, 50);
            rb4.setCompoundDrawables(null, listDrawable2.get(3), null, null);
        }
        if (rb5.isChecked()) {
            if(listNavigation.get(4).getCommentCode().equals("1")){
                rb5.setTextColor(Color.parseColor("#ce1010"));
            }
            listDrawable.get(4).setBounds(0, 0, 50, 50);
            rb5.setCompoundDrawables(null, listDrawable.get(4), null, null);
        } else {
            if(listNavigation.get(4).getCommentCode().equals("1")){
                rb5.setTextColor(Color.parseColor("#949494"));
            }
            drawable2.setBounds(0, 0, 50, 50);
            rb5.setCompoundDrawables(null, listDrawable2.get(4), null, null);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton(AlertDialog.BUTTON_POSITIVE,"确定", listener);
            isExit.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", listener);
            // 显示对话框
            isExit.show();

        }

        return false;

    }

    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                   /* Arad.preferences.clear();
                    Arad.preferences.flush();*/
                    MyApplication.getInstance().exit();
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        isFrist = true;
        if (!Arad.preferences.getString("memberId").equals("")){
            getNet();
        }
    }

    public class MyBroadcastReciver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getExtras().getString("num");
            switch (num){
                case "num":
                    //MessageUtils.showShortToast(context, "num");
                    getNet();
                    break;
                case "message":
                    //MessageUtils.showShortToast(context, "message");
                    getNet();
                    break;
                case "back":
                    int number = Integer.valueOf(intent.getExtras().getString("number"));
                    for (int i=0;i<listNavigation.size();i++){
                        if (listNavigation.get(i).getCommentCode().equals("4")){
                            CircleRadioView radioView = (CircleRadioView) ll_rb.getChildAt(i);
                            radioView.setNum(number);
                        }
                    }
                    break;
                /*case "cashCoupon": //现金劵
                    //MessageUtils.showShortToast(context, "cashCoupon");
                    getNet();
                    break;
                case "coupon": //优惠劵
                    //MessageUtils.showShortToast(context, "coupon");
                    getNet();
                    break;*/
                default:
                    break;
            }
        }
    }

    public void getNet(){
        edao.getVipInfo(Arad.preferences.getString("memberId"));
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==1){
            memberInfo = edao.getMemberInfo();
           // String coupons =  memberInfo.getCouponsNum();//现金劵
            //String redenvelopNum = memberInfo.getRedenvelopeNum();//优惠劵
            //String messageCount = memberInfo.getMessageCount();//消息通知

            /*int coupons = 0;
            int redenvelopNum = 0;*/
            int messageCount = 0;
            /*if (memberInfo.getCouponsNum()!=null && !memberInfo.getCouponsNum().equals("")){
                coupons = Integer.parseInt(memberInfo.getCouponsNum());
            }else{
                coupons=0;
            }
            if (memberInfo.getRedenvelopeNum()!=null && !memberInfo.getRedenvelopeNum().equals("")){
                redenvelopNum = Integer.parseInt(memberInfo.getRedenvelopeNum());
            }else{
                redenvelopNum = 0;
            }*/
            if ( memberInfo.getMessageCount()!=null && ! memberInfo.getMessageCount().equals("")){
                messageCount = Integer.parseInt(memberInfo.getMessageCount());
            }else{
                messageCount = 0;
            }

            /*
            Log.e("coupons=========",coupons+"");
            Log.e("redenvelopNum=========",redenvelopNum+"");
            Log.e("messageCount=========",messageCount+"");*/
            //int num =coupons+redenvelopNum+messageCount;

            for (int i=0;i<listNavigation.size();i++){
                if (listNavigation.get(i).getCommentCode().equals("4")){
                    CircleRadioView radioView = (CircleRadioView) ll_rb.getChildAt(i);
                    radioView.setNum(messageCount);
                }
            }
        }
    }
}
