package com.semicolon.huna.Fragments;

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

import com.semicolon.huna.Activities.Driver_Grocery_Notification_Details_Activity;
import com.semicolon.huna.Activities.HomeActivity;
import com.semicolon.huna.Adapters.Driver_Notification_Adapter;
import com.semicolon.huna.Models.Driver_Grocery_Notification_Model;
import com.semicolon.huna.Models.ResponseModel;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Api;
import com.semicolon.huna.Services.Tags;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Driver_Notification extends Fragment{
    private static String TAG="TAG";
    private String user_id="";
    private HomeActivity homeActivity;
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private SwipeRefreshLayout swipe;
    private ProgressBar progressBar;
    private LinearLayout ll_no_notification;
    private List<Driver_Grocery_Notification_Model> notification_modelList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_notification,container,false);
        initView(view);
        getData();
        return view;
    }



    public static Fragment_Driver_Notification getInstance(String user_id)
    {
        Fragment_Driver_Notification fragment_setting = new Fragment_Driver_Notification();
        Bundle bundle = new Bundle();
        bundle.putString(TAG,user_id);
        fragment_setting.setArguments(bundle);
        return  fragment_setting;
    }
    private void initView(View view) {

        notification_modelList = new ArrayList<>();

        homeActivity = (HomeActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            user_id = bundle.getString(TAG);
        }

        swipe = view.findViewById(R.id.swipe);
        progressBar = view.findViewById(R.id.progBar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        ll_no_notification = view.findViewById(R.id.ll_no_notification);
        recView.setLayoutManager(manager);
        adapter = new Driver_Notification_Adapter(getActivity(),notification_modelList,this);
        recView.setAdapter(adapter);
        swipe.setColorSchemeResources(R.color.colorPrimary,R.color.red,R.color.yellow,R.color.green);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        homeActivity.HideFab();
    }
    private void getData() {
        Api.getServices().getDriver_Notifications(user_id)
                .enqueue(new Callback<List<Driver_Grocery_Notification_Model>>() {
                    @Override
                    public void onResponse(Call<List<Driver_Grocery_Notification_Model>> call, Response<List<Driver_Grocery_Notification_Model>> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful())
                        {
                            if (response.body().size()>0)
                            {
                                notification_modelList.clear();
                                notification_modelList.addAll(response.body());
                                adapter.notifyDataSetChanged();

                                ll_no_notification.setVisibility(View.GONE);

                            }else {
                                ll_no_notification.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Driver_Grocery_Notification_Model>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(homeActivity,R.string.something_error, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    public void setItem(Driver_Grocery_Notification_Model item)
    {
        Intent intent = new Intent(getContext(), Driver_Grocery_Notification_Details_Activity.class);
        intent.putExtra("data",item);
        getActivity().startActivity(intent);
    }


    public void sendAccept(int pos,String id_delivery_order,String bill_num_fk,String id_client_fk)
    {
        Api.getServices()
                .driverReplyOrder(user_id,id_delivery_order, Tags.send_accept_order,bill_num_fk,id_client_fk)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            Log.e("respons33",response.body().getSuccess_action()+"");

                            if (response.body().getSuccess_action()==1)
                            {
                                notification_modelList.remove(pos);
                                adapter.notifyItemRemoved(pos);
                                if (notification_modelList.size()==0)
                                {
                                    ll_no_notification.setVisibility(View.VISIBLE);
                                }else
                                {
                                    ll_no_notification.setVisibility(View.GONE);

                                }

                                Toast.makeText(homeActivity, R.string.rep_don, Toast.LENGTH_LONG).show();
                            }else if (response.body().getSuccess_action()==0)
                            {
                                Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void sendRefuse(int pos,String id_delivery_order,String bill_num_fk,String id_client_fk)
    {
        Api.getServices()
                .driverReplyOrder(user_id,id_delivery_order, Tags.send_refuse_order,bill_num_fk,id_client_fk)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            Log.e("respons444",response.body().getSuccess_action()+"");

                            if (response.body().getSuccess_action()==1)
                            {
                                notification_modelList.remove(pos);
                                adapter.notifyItemRemoved(pos);
                                if (notification_modelList.size()==0)
                                {
                                    ll_no_notification.setVisibility(View.VISIBLE);
                                }else
                                    {
                                        ll_no_notification.setVisibility(View.GONE);

                                    }
                                Toast.makeText(homeActivity, R.string.rep_don, Toast.LENGTH_LONG).show();

                            }else if (response.body().getSuccess_action()==0)
                            {
                                Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
