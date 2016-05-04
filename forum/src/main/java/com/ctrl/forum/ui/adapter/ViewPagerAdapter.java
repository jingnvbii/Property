package com.ctrl.forum.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

/**
 * 模块切换viewpager adapter
 * Created by apple on 2014-12-15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> fragmentsList = new SparseArray<Fragment>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ViewPagerAdapter(FragmentManager fm, SparseArray<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
    }

    @Override
    public int getCount() {
        return fragmentsList == null ? 0 : fragmentsList.size();
    }

    @Override
    public Fragment getItem(int index) {
        return fragmentsList.get(index);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
