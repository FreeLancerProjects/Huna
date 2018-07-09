package com.semicolon.criuse.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.criuse.R;

public class Fragment_Current_Order extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_order,container,false);
        return  view;
    }

    public static Fragment_Current_Order getInstance()
    {
        Fragment_Current_Order fragment = new Fragment_Current_Order();
        return fragment;
    }
}
