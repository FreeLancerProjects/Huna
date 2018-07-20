package com.semicolon.criuse.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;

public class Fragment_Grocery_Register extends Fragment {
    private LinearLayout ll_login;
    private HomeActivity homeActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_register,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        ll_login = view.findViewById(R.id.ll_login);
        ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayLoginLayout(Tags.grocery_register);
            }
        });
    }

    public static Fragment_Grocery_Register getInstance()
    {
        Fragment_Grocery_Register fragment = new Fragment_Grocery_Register();
        return fragment;
    }
}
