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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.semicolon.criuse.R;

public class Fragment_Home extends Fragment{
    private static String TAG = "TAG";
    private String user_type="";
    private ImageView grocery_image;
    private TextView tv_grocery_name;
    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
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
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            user_type = bundle.getString(TAG);
        }

        grocery_image = view.findViewById(R.id.grocery_image);
        tv_grocery_name = view.findViewById(R.id.tv_grocery_name);
        progBar = view.findViewById(R.id.progBar);
        recView = view.findViewById(R.id.recView);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(view.getContext(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new GridLayoutManager(view.getContext(),3);
        recView.setLayoutManager(manager);
    }
}
