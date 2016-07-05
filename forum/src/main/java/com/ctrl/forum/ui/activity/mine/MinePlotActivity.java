package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;

import com.ctrl.forum.R;
import com.ctrl.forum.ui.activity.MainActivity;
import com.ctrl.forum.ui.fragment.PlotFragment;

/**
 * 个人中心--我的小区
 */
public class MinePlotActivity extends ActionBarActivity {
    private FrameLayout fragment;
    private FragmentManager fragmentManager;
    private PlotFragment plotFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_plot);
        fragment = (FrameLayout) findViewById(R.id.fragment);

        fragmentManager = getSupportFragmentManager();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        plotFragment = PlotFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("str", "我");
        plotFragment.setArguments(bundle);
        transaction.add(R.id.fragment, plotFragment);
        transaction.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MainActivity.isFrist = true;
    }
}
