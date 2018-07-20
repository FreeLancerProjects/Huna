package com.semicolon.criuse.Fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.semicolon.criuse.R;

public class Fragment_MiniMarkets extends Fragment {
    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minimarkets,container,false);
        initView(view);
        return  view;
    }

    private void initView(View view) {
        progBar = view.findViewById(R.id.progBar);
        recView = view.findViewById(R.id.recView);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(view.getContext(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new GridLayoutManager(view.getContext(),3);
        recView.setLayoutManager(manager);
    }

    public static Fragment_MiniMarkets getInstance()
    {
        Fragment_MiniMarkets fragment = new Fragment_MiniMarkets();
        return fragment;
    }
}
