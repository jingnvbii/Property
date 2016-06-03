package com.ctrl.forum.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.ui.fragment.InvitationFragment;
import com.ctrl.forum.ui.fragment.MyFragment;
import com.ctrl.forum.ui.fragment.PlotFragment;
import com.ctrl.forum.ui.fragment.RimFragment;
import com.ctrl.forum.ui.fragment.StroeFragment;

public class MainActivity extends AppToolBarActivity implements View.OnClickListener {
    private RadioButton rb1;//帖子按钮
    private RadioButton rb2;//商城按钮
    private RadioButton rb3;//小区按钮
    private RadioButton rb4;//周边按钮
    private RadioButton rb5;//我  按钮
    private InvitationFragment invitationFragment;
    private StroeFragment storeFragment;
    private PlotFragment plotFragment;
    private RimFragment rimFragment;
    private MyFragment myFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        rb1 = (RadioButton) findViewById(R.id.rb_1);
        rb2 = (RadioButton) findViewById(R.id.rb_2);
        rb3 = (RadioButton) findViewById(R.id.rb_3);
        rb4 = (RadioButton) findViewById(R.id.rb_4);
        rb5 = (RadioButton) findViewById(R.id.rb_5);
        rb1.setOnClickListener(this);
        rb2.setOnClickListener(this);
        rb3.setOnClickListener(this);
        rb4.setOnClickListener(this);
        rb5.setOnClickListener(this);
        //设置默认的fragment
        setDefaultFragment();

    }

    private void setDefaultFragment() {
        rb1.setChecked(true);
        setTabSelection(0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_1:
                setTabSelection(0);
                break;
            case R.id.rb_2:
                setTabSelection(1);
                break;
            case R.id.rb_3:
                setTabSelection(2);
                break;
            case R.id.rb_4:
                setTabSelection(3);
                break;
            case R.id.rb_5:
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
                if (invitationFragment == null) {
                    // invitationFragment，则创建一个并添加到界面上
                    invitationFragment = InvitationFragment.newInstance();
                    transaction.add(R.id.content, invitationFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(invitationFragment);
                }
                break;
            case 1:
                if (storeFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    storeFragment = StroeFragment.newInstance();
                    transaction.add(R.id.content, storeFragment);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(storeFragment);
                }
                break;
            case 2:
                if (plotFragment == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    plotFragment = PlotFragment.newInstance();
                    transaction.add(R.id.content, plotFragment);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(plotFragment);
                }
                break;
            case 3:
                if (rimFragment == null) {
                    // 如果SettingFragment为空，则创建一个并添加到界面上
                    rimFragment = RimFragment.newInstance();
                    transaction.add(R.id.content, rimFragment);
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    transaction.show(rimFragment);
                }
                break;
            case 4:
                if (myFragment == null) {
                    // 如果SettingFragment为空，则创建一个并添加到界面上
                    myFragment = MyFragment.newInstance();
                    transaction.add(R.id.content, myFragment);
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    transaction.show(myFragment);
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


}
