package com.semicolon.huna.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semicolon.huna.Activities.HomeActivity;
import com.semicolon.huna.Activities.SearchActivity;
import com.semicolon.huna.Adapters.ViewPagerAdapter;
import com.semicolon.huna.R;

import java.util.ArrayList;
import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class Fragment_Home extends Fragment{
    private static String TAG = "TAG";
    private String user_type="";
    private TabLayout tab;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private Context context;
    private TextView searchView;
    private HomeActivity homeActivity;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        context = view.getContext();
        initView(view);
        return view;
    }
    public static Fragment_Home getInstance(String user_type)
    {
        Fragment_Home fragment_home = new Fragment_Home();
        Bundle bundle = new Bundle();
        bundle.putString(TAG,user_type);
        return fragment_home;
    }
    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            user_type = bundle.getString(TAG);
        }

        searchView = view.findViewById(R.id.searchView);
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        tab = view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);
        AddFragments();
        AddTitle();
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),context);
        adapter.AddFragments(fragmentList);
        adapter.AddTitle(titleList);
        pager.setAdapter(adapter);
        tab.setupWithViewPager(pager);
        searchView.setOnClickListener(view1 ->
                {
                    Intent intent = new Intent(context, SearchActivity.class);
                    context.startActivity(intent);
                    Bungee.zoom(context);
                }
        );
        for(int i=0; i < tab.getTabCount(); i++) {
            View tab = ((ViewGroup) this.tab.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            if (i==0)
            {
                p.setMargins(10, 0, 8, 0);

            }else if (i==this.tab.getTabCount()-1)
            {
                p.setMargins(8, 0, 10, 0);

            }
            tab.requestLayout();
        }

        homeActivity.showFab();

    }
    private List<String> AddTitle() {
        titleList.add(getString(R.string.sm));
        titleList.add(getString(R.string.mm));
        return titleList;

    }

    private List<Fragment> AddFragments() {
        fragmentList.add(Fragment_Supermarkets.getInstance());
        fragmentList.add(Fragment_MiniMarkets.getInstance());

        return fragmentList;
    }


}
