package com.semicolon.criuse.Fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.R;

public class FragmentClientNotification extends Fragment{
    private static String TAG="TAG";
    private String user_type="";
    private HomeActivity homeActivity;
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private SwipeRefreshLayout swipe;
    private ProgressBar progressBar;
    private LinearLayout ll_no_notification;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_notification,container,false);
        initView(view);

        return view;
    }

    public static FragmentClientNotification getInstance(String user_type)
    {
        FragmentClientNotification fragment_setting = new FragmentClientNotification();
        Bundle bundle = new Bundle();
        bundle.putString(TAG,user_type);
        fragment_setting.setArguments(bundle);
        return  fragment_setting;
    }
    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            user_type = bundle.getString(TAG);
        }

        swipe = view.findViewById(R.id.swipe);
        progressBar = view.findViewById(R.id.progBar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        swipe.setColorSchemeResources(R.color.colorPrimary,R.color.red,R.color.yellow,R.color.green);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }
}
