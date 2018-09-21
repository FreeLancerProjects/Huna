package com.semicolon.huna.Fragments;

import android.app.ProgressDialog;
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

import com.semicolon.huna.Activities.ClientOrderDetailsActivity;
import com.semicolon.huna.Adapters.Client_Current_Previous_Order_Adapter;
import com.semicolon.huna.Models.ClientOrderModel;
import com.semicolon.huna.Models.ResponseModel;
import com.semicolon.huna.Models.UserModel;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Api;
import com.semicolon.huna.Share.Common;
import com.semicolon.huna.SingleTones.UserSingletone;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Client_Current_Order extends Fragment {
    private static final String USER_ID="ID";
    private String user_id="";
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private ProgressBar progBar;
    private LinearLayout ll_no_order;
    private List<ClientOrderModel> clientOrderModelList;
    private UserSingletone userSingletone;
    private UserModel userModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_order,container,false);
        initView(view);
        return  view;
    }

    private void initView(View view) {
        userSingletone = UserSingletone.getInstance();
        userModel = userSingletone.getUserModel();
        clientOrderModelList = new ArrayList<>();
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
        adapter = new Client_Current_Previous_Order_Adapter(getActivity(),clientOrderModelList,this,"current");
        recView.setAdapter(adapter);
    }
    public static Fragment_Client_Current_Order getInstance(String user_id)
    {
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID,user_id);
        Fragment_Client_Current_Order fragment = new Fragment_Client_Current_Order();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();

    }

    private void getData() {
        clientOrderModelList.clear();
        Api.getServices()
                .getClientCurrentOrder(user_id)
                .enqueue(new Callback<List<ClientOrderModel>>() {
                    @Override
                    public void onResponse(Call<List<ClientOrderModel>> call, Response<List<ClientOrderModel>> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);
                            if (response.body().size()>0)
                            {
                                ll_no_order.setVisibility(View.GONE);
                                clientOrderModelList.addAll(response.body());
                                adapter.notifyDataSetChanged();
                            }
                            else
                                {
                                    ll_no_order.setVisibility(View.VISIBLE);
                                    clientOrderModelList.clear();
                                    adapter.notifyDataSetChanged();
                                }

                        }
                    }

                    @Override
                    public void onFailure(Call<List<ClientOrderModel>> call, Throwable t) {
                        progBar.setVisibility(View.GONE);
                        Log.e("Error",t.getMessage());
                        Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void setItem(ClientOrderModel clientOrderModel)
    {
        Intent intent = new Intent(getActivity(), ClientOrderDetailsActivity.class);
        intent.putExtra("data",clientOrderModel);
        intent.putExtra("fragment_type","current");
        getActivity().startActivity(intent);
    }

    public void EndOrder(ClientOrderModel clientOrderModel,int pos)
    {
       ProgressDialog progressDialog= Common.CreateProgressDialog(getActivity(),getString(R.string.ending_req));

       Api.getServices().endOrder(clientOrderModel.getBill_id(),userModel.getUser_type(),userModel.getUser_id(),clientOrderModel.getId_delivery_user_fk(),clientOrderModel.getRoom_id())
               .enqueue(new Callback<ResponseModel>() {
                   @Override
                   public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                       if (response.isSuccessful())
                       {

                           progressDialog.dismiss();
                           Log.e("success_end",response.body().getSuccess_end()+"");
                           if (response.body().getSuccess_end()==1)
                           {
                               clientOrderModelList.remove(pos);
                               adapter.notifyItemRemoved(pos);

                               if (clientOrderModelList.size()==0)
                               {
                                   ll_no_order.setVisibility(View.VISIBLE);
                               }
                           }else
                               {
                                   Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_LONG).show();

                               }
                       }
                   }

                   @Override
                   public void onFailure(Call<ResponseModel> call, Throwable t) {
                       progressDialog.dismiss();
                       Log.e("Error",t.getMessage());
                       Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_LONG).show();
                   }
               });
    }


}
