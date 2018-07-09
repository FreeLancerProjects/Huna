package com.semicolon.criuse.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    public ViewPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context=context;
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
    }

    public void AddFragments(List<Fragment> fragmentList)
    {
        this.fragmentList.addAll(fragmentList);
    }
    public void AddTitle(List<String> titleList)
    {
        this.titleList.addAll(titleList);
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
