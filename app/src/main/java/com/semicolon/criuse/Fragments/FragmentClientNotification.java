package com.semicolon.criuse.Fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.Activities.ClientNotificationDetailsActivity;
import com.semicolon.criuse.Adapters.Client_Notification_Adapter;
import com.semicolon.criuse.Models.Client_Notification_Model;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentClientNotification extends Fragment{
    private static String TAG="TAG";
    private String user_id="";
    private HomeActivity homeActivity;
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private SwipeRefreshLayout swipe;
    private ProgressBar progressBar;
    private LinearLayout ll_no_notification;
    private List<Client_Notification_Model> client_notification_modelList;
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
        client_notification_modelList = new ArrayList<>();
        homeActivity = (HomeActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            user_id = bundle.getString(TAG);
        }

        swipe = view.findViewById(R.id.swipe);
        ll_no_notification = view.findViewById(R.id.ll_no_notification);
        progressBar = view.findViewById(R.id.progBar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        adapter = new Client_Notification_Adapter(getActivity(),client_notification_modelList,this);
        recView.setAdapter(adapter);
        swipe.setColorSchemeResources(R.color.colorPrimary,R.color.red,R.color.yellow,R.color.green);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        homeActivity.HideFab();
        getData();
    }

    private void getData() {
        Log.e("user_id",user_id);
        Api.getServices().getClientNotifications(user_id)
                .enqueue(new Callback<List<Client_Notification_Model>>() {
                    @Override
                    public void onResponse(Call<List<Client_Notification_Model>> call, Response<List<Client_Notification_Model>> response) {
                        if (response.isSuccessful())
                        {
                            swipe.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);
                            if (response.body().size()>0){
                                ll_no_notification.setVisibility(View.GONE);
                                client_notification_modelList.clear();
                                client_notification_modelList.addAll(response.body());
                                adapter.notifyDataSetChanged();
                            }else
                                {
                                    ll_no_notification.setVisibility(View.VISIBLE);
                                }

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Client_Notification_Model>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(homeActivity,getString(R.string.something_error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void setItem(Client_Notification_Model client_notification_model)
    {
        Intent intent = new Intent(getActivity(), ClientNotificationDetailsActivity.class);
        intent.putExtra("data",client_notification_model);
        getActivity().startActivity(intent);
    }

}
