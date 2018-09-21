package com.semicolon.huna.Fragments;

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
import android.widget.ImageView;

import com.semicolon.huna.Activities.HomeActivity;
import com.semicolon.huna.Adapters.ViewPagerAdapter;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Tags;

import java.util.ArrayList;
import java.util.List;

public class FragmentMyOrderContainer extends Fragment {
    private static final String USER_TYPE="1";
    private static final String USER_ID="2";
    private TabLayout tab;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private Context context;
    private ImageView back;
    private HomeActivity homeActivity;
    private String user_id="",user_type="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myorders_container,container,false);
        context = view.getContext();
        initView(view);
        return view;
    }

    private void initView(View view) {
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            user_id = bundle.getString(USER_ID);
            user_type = bundle.getString(USER_TYPE);
        }
        homeActivity = (HomeActivity) getActivity();
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        back = view.findViewById(R.id.back);
        tab = view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);
        AddFragments(user_type);
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
                p.setMargins(15, 0, 15, 0);

            }else if (i==this.tab.getTabCount()-1)
            {
                p.setMargins(15, 0, 15, 0);

            }
            tab.requestLayout();

            homeActivity.HideFab();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.setNavPosition();
            }
        });

    }

    private List<String> AddTitle() {
        titleList.add(getString(R.string.current));
        titleList.add(getString(R.string.previous));
        return titleList;

    }

    private List<Fragment> AddFragments(String user_type) {
        if (user_type.equals(Tags.user_type_client))
        {
            fragmentList.add(Fragment_Client_Current_Order.getInstance(user_id));
            fragmentList.add(Fragment_Client_Previous_Order.getInstance(user_id));
        }else if (user_type.equals(Tags.user_type_driver)||user_type.equals(Tags.user_type_grocery))
        {
            fragmentList.add(Fragment_Driver_Grocery_Current_Order.getInstance(user_id));
            fragmentList.add(Fragment_Driver_Grocery_Previous_Order.getInstance(user_id));
        }


        return fragmentList;
    }

    public static FragmentMyOrderContainer getInstance(String user_type,String user_id)
    {
        Bundle bundle = new Bundle();
        FragmentMyOrderContainer fragment = new FragmentMyOrderContainer();
        bundle.putString(USER_ID,user_id);
        bundle.putString(USER_TYPE,user_type);
        fragment.setArguments(bundle);
        return fragment;
    }
}
