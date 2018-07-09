package com.semicolon.criuse.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.criuse.Adapters.ViewPagerAdapter;
import com.semicolon.criuse.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentAllGroceriesContainer extends Fragment {
    private TabLayout tab;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_groceries_container,container,false);
        context = view.getContext();
        initView(view);
        return view;
    }

    private void initView(View view) {
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

        for(int i=0; i < tab.getTabCount(); i++) {
            View tab = ((ViewGroup) this.tab.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            if (i==0)
            {
                p.setMargins(5, 0, 8, 0);

            }else if (i==this.tab.getTabCount()-1)
            {
                p.setMargins(0, 0, 5, 0);

            }else
                {
                    p.setMargins(0, 0, 8, 0);

                }
            tab.requestLayout();
        }


    }

    private List<String> AddTitle() {
        titleList.add(getString(R.string.groceries));
        titleList.add(getString(R.string.sm));
       // titleList.add(getString(R.string.mm));
        return titleList;

    }

    private List<Fragment> AddFragments() {
        fragmentList.add(Fragment_Groceries.getInstance());
        fragmentList.add(Fragment_Supermarkets.getInstance());
        //fragmentList.add(Fragment_MiniMarkets.getInstance());

        return fragmentList;
    }

    public static FragmentAllGroceriesContainer getInstance()
    {
        FragmentAllGroceriesContainer fragment = new FragmentAllGroceriesContainer();
        return fragment;
    }
}
