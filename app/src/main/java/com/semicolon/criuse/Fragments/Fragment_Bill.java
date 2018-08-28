package com.semicolon.criuse.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.Adapters.Bill_Adapter;
import com.semicolon.criuse.Models.Bill_Model;
import com.semicolon.criuse.Models.ItemsModel;
import com.semicolon.criuse.Models.ResponseModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Api;
import com.semicolon.criuse.Share.Common;
import com.semicolon.criuse.SingleTones.ItemsSingleTone;
import com.semicolon.criuse.SingleTones.Location_Order_SingleTone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Bill extends Fragment {
    private static String TAG="LIST";
    private List<Bill_Model> bill_modelList;
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private TextView tv_total;
    private Button btn_send;
    private int total=0;
    private Location_Order_SingleTone location_order_singleTone;
    private ItemsSingleTone itemsSingleTone;
    private List<ItemsModel> miniMarketItemList;
    private List<ItemsModel> superMarketItemList;
    private ProgressDialog progressDialog;
    private HomeActivity homeActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Bill getInstance(List<Bill_Model> bill_modelList)
    {
        Fragment_Bill fragment = new Fragment_Bill();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, (Serializable) bill_modelList);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        bill_modelList = new ArrayList<>();
        miniMarketItemList = new ArrayList<>();
        superMarketItemList = new ArrayList<>();
        tv_total = view.findViewById(R.id.tv_total);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        btn_send = view.findViewById(R.id.btn_send);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            bill_modelList = (List<Bill_Model>) bundle.getSerializable(TAG);

            Log.e("ssssddsds",bill_modelList.size()+"");
            for (Bill_Model bill_model:bill_modelList)
            {
                total =total+ bill_model.getSubtotal()+bill_model.getDelivery();
            }


            tv_total.setText(total+" "+getString(R.string.sar));
            adapter = new Bill_Adapter(getActivity(),bill_modelList);
            recView.setAdapter(adapter);

            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog = Common.CreateProgressDialog2(getActivity(),getString(R.string.sending_order));
                    location_order_singleTone = Location_Order_SingleTone.getInstance();
                    LatLng latLng = location_order_singleTone.getOrderLocation();
                    String address = location_order_singleTone.getAddress();
                    itemsSingleTone = ItemsSingleTone.getInstance();
                    miniMarketItemList = itemsSingleTone.getMinimarketItemsList();
                    superMarketItemList= itemsSingleTone.getSupermarketItemsList();
                    List<ItemsModel> superMarketList = new ArrayList<>();
                    List<ItemsModel> miniMarketList = new ArrayList<>();

                    Log.e("orderlat",latLng.latitude+"_");
                    Log.e("orderlng",latLng.longitude+"_");
                    Log.e("orderaddress",address+"_");

                    if (miniMarketItemList.size()>0&&superMarketItemList.size()>0)
                    {
                        for (ItemsModel itemsModel:superMarketItemList)
                        {
                            itemsModel.setUser_google_lat(latLng.latitude);
                            itemsModel.setUser_google_long(latLng.longitude);
                            itemsModel.setUser_address(address);
                            superMarketList.add(itemsModel);
                        }

                        for (ItemsModel itemsModel:miniMarketItemList)
                        {
                            itemsModel.setUser_google_lat(latLng.latitude);
                            itemsModel.setUser_google_long(latLng.longitude);
                            itemsModel.setUser_address(address);
                            miniMarketList.add(itemsModel);
                        }



                        sendOrders(superMarketList,miniMarketItemList);

                    }else if (miniMarketItemList.size()>0)
                    {


                        for (ItemsModel itemsModel:miniMarketItemList)
                        {
                            itemsModel.setUser_google_lat(latLng.latitude);
                            itemsModel.setUser_google_long(latLng.longitude);
                            itemsModel.setUser_address(address);
                            miniMarketList.add(itemsModel);
                        }

                        sendOrderToMiniMarket(miniMarketList);
                    }else if (superMarketItemList.size()>0)
                    {
                        for (ItemsModel itemsModel:superMarketItemList)
                        {
                            itemsModel.setUser_google_lat(latLng.latitude);
                            itemsModel.setUser_google_long(latLng.longitude);
                            itemsModel.setUser_address(address);
                            superMarketList.add(itemsModel);
                        }
                        sendOrderToSuperMarket(superMarketList);
                    }



                }
            });
        }
    }

    private void sendOrderToMiniMarket(List<ItemsModel> itemsModelList)
    {
        Api.getServices().sendOrderToMiniMarket(itemsModelList)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess_object()==1)
                            {
                                progressDialog.dismiss();
                                ClearData();
                                Toast.makeText(getActivity(), R.string.succ_send, Toast.LENGTH_LONG).show();

                            }else
                                {
                                    progressDialog.dismiss();

                                    Toast.makeText(getActivity(), R.string.failed, Toast.LENGTH_LONG).show();
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendOrderToSuperMarket(List<ItemsModel> itemsModelList)
    {
        Api.getServices().sendOrderToSuperMarket(itemsModelList)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess_object()==1)
                            {
                                progressDialog.dismiss();
                                ClearData();

                                Toast.makeText(getActivity(), R.string.succ_send, Toast.LENGTH_LONG).show();

                            }else if (response.body().getSuccess_object()==2)
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), R.string.no_driver, Toast.LENGTH_LONG).show();


                            }else
                            {
                                progressDialog.dismiss();

                                Toast.makeText(getActivity(), R.string.failed, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progressDialog.dismiss();

                        Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void  sendOrders(List<ItemsModel> miniMarketItemList,List<ItemsModel>superMarketItemList)
    {
        Api.getServices().sendOrderToMiniMarket(miniMarketItemList)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess_object()==1)
                            {

                                Api.getServices().sendOrderToSuperMarket(superMarketItemList)
                                        .enqueue(new Callback<ResponseModel>() {
                                            @Override
                                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                                if (response.isSuccessful())
                                                {
                                                    if (response.body().getSuccess_object()==1)
                                                    {
                                                        progressDialog.dismiss();
                                                        ClearData();

                                                        Toast.makeText(getActivity(), R.string.succ_send, Toast.LENGTH_LONG).show();

                                                    }else
                                                    {
                                                        progressDialog.dismiss();

                                                        Toast.makeText(getActivity(), R.string.failed, Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                                Log.e("Error",t.getMessage());
                                                Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }else
                            {
                                progressDialog.dismiss();

                                Toast.makeText(getActivity(), R.string.failed, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progressDialog.dismiss();

                        Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void ClearData()
    {
        itemsSingleTone.clear();
        homeActivity.fragmentManager.popBackStack();
        homeActivity.navigateToHomeFragment();
    }
}
