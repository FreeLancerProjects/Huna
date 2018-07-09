package com.semicolon.criuse.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.criuse.R;

public class Fragment_Supermarkets extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supermarkets,container,false);
        return  view;
    }

    public static Fragment_Supermarkets getInstance()
    {
        Fragment_Supermarkets fragment = new Fragment_Supermarkets();
        return fragment;
    }
}
