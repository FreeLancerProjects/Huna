package com.semicolon.criuse.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.criuse.R;

public class Fragment_Client_Register extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_register,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
    }

    public static Fragment_Client_Register getInstance()
    {
        Fragment_Client_Register fragment = new Fragment_Client_Register();
        return fragment;
    }
}
