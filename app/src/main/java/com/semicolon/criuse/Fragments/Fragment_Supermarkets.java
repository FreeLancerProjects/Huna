package com.semicolon.criuse.Fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.Adapters.SuperMarketAdapter;
import com.semicolon.criuse.Models.ItemsModel;
import com.semicolon.criuse.Models.SuperMarketModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Api;
import com.semicolon.criuse.Services.Tags;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Supermarkets extends Fragment {
    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private TextView tv_no_cat;
    private List<SuperMarketModel> superMarketModelList;
    private HomeActivity homeActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supermarkets,container,false);
        initView(view);
        return  view;
    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        superMarketModelList = new ArrayList<>();
        tv_no_cat = view.findViewById(R.id.tv_no_cat);
        progBar = view.findViewById(R.id.progBar);
        recView = view.findViewById(R.id.recView);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(view.getContext(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new GridLayoutManager(view.getContext(),3);
        recView.setLayoutManager(manager);
        adapter = new SuperMarketAdapter(getActivity(),superMarketModelList,this);
        recView.setAdapter(adapter);

        getData();
    }

    private void getData() {
        Api.getServices()
                .getSuperMarketCategories()
                .enqueue(new Callback<List<SuperMarketModel>>() {
                    @Override
                    public void onResponse(Call<List<SuperMarketModel>> call, Response<List<SuperMarketModel>> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);
                            superMarketModelList.clear();
                            superMarketModelList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                            if (superMarketModelList.size()>0)
                            {
                                tv_no_cat.setVisibility(View.GONE);

                            }else
                                {
                                    tv_no_cat.setVisibility(View.VISIBLE);

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SuperMarketModel>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setPos(int pos,String title,String id_categories,String name,String phone,String cost,String market_id_fk)
    {
        List<ItemsModel> itemsModelList = new ArrayList<>();
        List<SuperMarketModel.SubProduct> subProductList = superMarketModelList.get(pos).getSubProductList();
        for (SuperMarketModel.SubProduct subProduct:subProductList)
        {

            ItemsModel itemsModel = new ItemsModel("",id_categories,subProduct.getId_product(),subProduct.getProduct_price(),"0",market_id_fk, Tags.isAdmin_supermarket,subProduct.getProduct_image(),subProduct.getProduct_title(),title,name,phone,cost,subProduct.getProduct_price());
            itemsModelList.add(itemsModel);
        }
        homeActivity.Hide_Navbottom();
        homeActivity.savePos();
        Fragment_Subdepartment fragment_subdepartment = Fragment_Subdepartment.getInstance(itemsModelList,title);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer,fragment_subdepartment).commit();
    }
    public static Fragment_Supermarkets getInstance()
    {
        Fragment_Supermarkets fragment = new Fragment_Supermarkets();
        return fragment;
    }
}
