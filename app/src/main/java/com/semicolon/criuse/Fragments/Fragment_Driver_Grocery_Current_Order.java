package com.semicolon.criuse.Fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.semicolon.criuse.Activities.Driver_Grocery_OrderDetailsActivity;
import com.semicolon.criuse.Adapters.Driver_Grocery_Current_Previous_Order_Adapter;
import com.semicolon.criuse.Models.Driver_Grocery_OrderModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Driver_Grocery_Current_Order extends Fragment {
    private static final String USER_ID="ID";
    private String user_id="";
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private ProgressBar progBar;
    private LinearLayout ll_no_order;
    private List<Driver_Grocery_OrderModel> driver_grocery_orderModelList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_order,container,false);
        initView(view);
        return  view;
    }

    private void initView(View view) {
        driver_grocery_orderModelList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            user_id = bundle.getString(USER_ID);
        }
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        ll_no_order = view.findViewById(R.id.ll_no_order);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        adapter = new Driver_Grocery_Current_Previous_Order_Adapter(getActivity(),driver_grocery_orderModelList,this,"current");
        recView.setAdapter(adapter);
        getData();
    }

    private void getData() {
        driver_grocery_orderModelList.clear();
        Api.getServices()
                .getDriver_Grocery_CurrentOrder(user_id)
                .enqueue(new Callback<List<Driver_Grocery_OrderModel>>() {
                    @Override
                    public void onResponse(Call<List<Driver_Grocery_OrderModel>> call, Response<List<Driver_Grocery_OrderModel>> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);
                            if (response.body().size()>0)
                            {
                                ll_no_order.setVisibility(View.GONE);
                                driver_grocery_orderModelList.addAll(response.body());
                                adapter.notifyDataSetChanged();
                            }
                            else
                            {
                                ll_no_order.setVisibility(View.VISIBLE);

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Driver_Grocery_OrderModel>> call, Throwable t) {
                        progBar.setVisibility(View.GONE);
                        Log.e("Error",t.getMessage());
                        Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();

                    }
                });
    }


    public static Fragment_Driver_Grocery_Current_Order getInstance(String user_id)
    {
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID,user_id);
        Fragment_Driver_Grocery_Current_Order fragment = new Fragment_Driver_Grocery_Current_Order();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setItem(Driver_Grocery_OrderModel driver_grocery_orderModel)
    {
        Intent intent = new Intent(getActivity(), Driver_Grocery_OrderDetailsActivity.class);
        intent.putExtra("data",driver_grocery_orderModel);
        getActivity().startActivity(intent);
    }
}
