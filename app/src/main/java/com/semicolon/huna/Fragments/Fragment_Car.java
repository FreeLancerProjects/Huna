package com.semicolon.huna.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semicolon.huna.Activities.HomeActivity;
import com.semicolon.huna.Adapters.TrolleyAdapter;
import com.semicolon.huna.Models.Bill_Model;
import com.semicolon.huna.Models.ItemsModel;
import com.semicolon.huna.Models.UserModel;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Tags;
import com.semicolon.huna.SharedPreferences.Preferences;
import com.semicolon.huna.SingleTones.ItemsSingleTone;
import com.semicolon.huna.SingleTones.UserSingletone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_Car extends Fragment{
    private static String TAG = "TAG";
    private String user_type="";
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private LinearLayout ll_trolley_container;
    private ItemsSingleTone itemsSingleTone;
    private List<ItemsModel> itemsModelList;
    private Button send_btn;
    private List<ItemsModel> supermarkerItemList;

    private List<ItemsModel> minimarkerItemList;
    private List<List<ItemsModel>> resultList;
    private HomeActivity homeActivity;
    private List<Bill_Model> bill_modelList;
    private List<String> matrket_id;
    private Preferences preferences;
    private UserSingletone userSingletone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Car getInstance(String user_type)
    {
        Fragment_Car fragment_car = new Fragment_Car();
        Bundle bundle = new Bundle();
        bundle.putString(TAG,user_type);
        fragment_car.setArguments(bundle);
        return fragment_car;
    }
    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();

        itemsSingleTone = ItemsSingleTone.getInstance();
        itemsModelList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            user_type = bundle.getString(TAG);
        }
        send_btn = view.findViewById(R.id.send_btn);
        ll_trolley_container = view.findViewById(R.id.ll_trolley_container);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(view.getContext());
        recView.setLayoutManager(manager);

        itemsModelList.addAll(itemsSingleTone.getItemsModelList());

        if (itemsModelList.size()>0)
        {
            adapter  = new TrolleyAdapter(getActivity(),itemsModelList,this);
            recView.setAdapter(adapter);
            ll_trolley_container.setVisibility(View.GONE);
            send_btn.setVisibility(View.VISIBLE);
        }else
            {
                ll_trolley_container.setVisibility(View.VISIBLE);

            }

            send_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    preferences = Preferences.getInstance();
                    userSingletone = UserSingletone.getInstance();
                    String session = preferences.getSession(getActivity());
                    if (TextUtils.isEmpty(session)||session.equals(Tags.session_logout))
                    {
                        CreateAlertDialog(getString(R.string.login_or_register_continue));
                    }else if (!TextUtils.isEmpty(session)&&session.equals(Tags.session_login))
                    {
                        UserModel userModel = preferences.getUserData(getActivity());
                        String user_id = userModel.getUser_id();
                        if (!userModel.getUser_type().equals(Tags.user_type_grocery))
                        {
                            OrderItems(user_id);

                        }else
                            {
                                CreateAlertDialog(getString(R.string.login_or_register_continue));

                            }
                    }

                }
            });

        homeActivity.HideFab();

    }

    private void CreateAlertDialog(String msg)
    {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_alert,null);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        Button done_btn = view.findViewById(R.id.done_btn);
        Button cancel_btn = view.findViewById(R.id.cancel_btn);
        tv_msg.setText(msg);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                homeActivity.navigateToSettingFragment();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
    }
    private void OrderItems(String user_id) {


        bill_modelList = new ArrayList<>();
        supermarkerItemList = new ArrayList<>();
        minimarkerItemList = new ArrayList<>();
        resultList = new ArrayList<>();
        matrket_id = new ArrayList<>();

        for (int i=0;i<itemsSingleTone.getItemsModelList().size();i++)
        {
            ItemsModel itemsModel_1 = itemsSingleTone.getItemsModelList().get(i);
            for (int x=0;x<itemsSingleTone.getItemsModelList().size();x++)
            {
                ItemsModel itemsModel_2 = itemsSingleTone.getItemsModelList().get(x);

                if (itemsModel_1.getIs_admin().equals(itemsModel_2.getIs_admin())
                        && itemsModel_1.getIs_admin().equals(Tags.isAdmin_supermarket)
                        )
                {
                    if (!supermarkerItemList.contains(itemsModel_2))
                    {
                        supermarkerItemList.add(itemsModel_2);

                    }
                }else if (itemsModel_1.getIs_admin().equals(itemsModel_2.getIs_admin())
                        && itemsModel_1.getIs_admin().equals(Tags.isAdmin_minimarket))
                {
                    if (!minimarkerItemList.contains(itemsModel_2))
                    {
                        minimarkerItemList.add(itemsModel_2);

                    }
                }
            }
        }

        if (supermarkerItemList.size()>0) {

            itemsSingleTone.setSupermarketItemsList(supermarkerItemList,user_id);

            Log.e("smItemSize", supermarkerItemList.size() + "");
            resultList.add(supermarkerItemList);

            for (int i=0;i<resultList.size();i++)
            {
                List<ItemsModel> itemsModelList = resultList.get(i);
                for (ItemsModel itemsModel1:itemsModelList)
                {
                    Log.e("ffffff",itemsModel1.getMarket_id_fk()+"________"+itemsModel1.getProduct_cost());
                }
            }

        }

            Log.e("ssssssssssize",resultList.size()+"");



        if (minimarkerItemList.size()>0){
            Log.e("mmItemSize",minimarkerItemList.size()+"");
            itemsSingleTone.setMinimarketItemsList(minimarkerItemList,user_id);

            List<ItemsModel> minimarketOrderItemList = new ArrayList<>();

            for (int i =0;i<minimarkerItemList.size();i++)
            {

                ItemsModel itemsModel_1 = minimarkerItemList.get(i);
                for (int x =0;x<minimarkerItemList.size();x++)
                {
                    ItemsModel itemsModel_2 = minimarkerItemList.get(x);
                    if (itemsModel_1.getMarket_id_fk().equals(itemsModel_2.getMarket_id_fk())){
                        if (!minimarketOrderItemList.contains(itemsModel_2))
                        {
                            minimarketOrderItemList.add(itemsModel_2);


                        }
                    }
                }






            }


            if (minimarketOrderItemList.size()>0)
            {

                for (ItemsModel itemsModel:minimarketOrderItemList)
                {
                    if (!matrket_id.contains(itemsModel.getMarket_id_fk()))
                    {
                        matrket_id.add(itemsModel.getMarket_id_fk());
                    }
                }

                Map <String,List<ItemsModel>> map = new HashMap<>();
                if (matrket_id.size()>0)
                {
                    for (String id :matrket_id)
                    {
                        List<ItemsModel> itemsModelList = new ArrayList<>();
                        for (ItemsModel itemsModel2:minimarketOrderItemList)
                        {
                            if (id.equals(itemsModel2.getMarket_id_fk()))
                            {
                                itemsModelList.add(itemsModel2);
                                Log.e("cost",itemsModel2.getProduct_cost());
                                map.put(id,itemsModelList);
                            }
                        }
                    }



                    for (String key:map.keySet())
                    {
                        List<ItemsModel> itemsModelList = map.get(key);
                        resultList.add(itemsModelList);
                        Log.e("size",map.size()+"");

                    }
                }


            }

            Log.e("resultSize",resultList.size()+"");

            if (resultList.size()>0)
            {
                Log.e("ssssssssssize",resultList.size()+"");

                for (int i=0;i<resultList.size();i++)
                {
                   List<ItemsModel> lists = resultList.get(i);
                    Log.e("sssize",lists.size()+"");
                    for (ItemsModel itemsModel1:lists)
                    {
                        Log.e("bbbbbbb",itemsModel1.getMarket_id_fk()+"______"+itemsModel1.getProduct_cost());

                    }


                }
            }






    }

    if (resultList.size()>0)
    {
        for (int i=0;i<resultList.size();i++)
        {
            List<ItemsModel> itemsModelList = resultList.get(i);
            String name = itemsModelList.get(0).getMarket_name();
            String phone= itemsModelList.get(0).getMarket_phone();
            int delivery = Integer.parseInt(itemsModelList.get(0).getDelivery_cost());
            Log.e("cost",itemsModelList.get(0).getDelivery_cost()+"__");
            Log.e("name",name+"__");
            Log.e("phone",phone+"__");

            int sub_total=0;
            for (int x=0;x<itemsModelList.size();x++)
            {
                ItemsModel itemsModel = itemsModelList.get(x);
                sub_total = sub_total+Integer.parseInt(itemsModel.getProduct_cost());
                Log.e("cost",itemsModel.getProduct_cost());
            }
            Bill_Model bill_model = new Bill_Model(name,phone,sub_total,delivery,0);
            bill_modelList.add(bill_model);
        }

        itemsSingleTone.setBill_modelList(bill_modelList);

        FragmentTransaction transaction = homeActivity.fragmentManager.beginTransaction();
        transaction.add(R.id.fragmentsContainer,Fragment_Map.getInstance(),"fragment_map");
        transaction.addToBackStack("Fragment_Map");
        transaction.commit();

    }





    }

    public void SaveItem_To_Trolley(ItemsModel itemsModel)
    {
        itemsModel.setProduct_cost(String.valueOf(Integer.parseInt(itemsModel.getProduct_amount())*Integer.parseInt(itemsModel.getItem_one_cost())));

        itemsSingleTone.SaveItem_To_Trolley(itemsModel);
        for (ItemsModel itemsModel2:itemsSingleTone.getItemsModelList())
        {
            Log.e("ItemModel",itemsModel2.getProduct_name()+"\n"+itemsModel2.getId_product()+"\n"+itemsModel2.getProduct_amount()+"\n"+itemsModel2.getIs_admin());
        }
    }

    public void Remove_From_Trolley(ItemsModel itemsModel, int position)
    {
        itemsModelList.remove(itemsModel);
        itemsSingleTone.RemoveItem_From_Trolley(itemsModel);
        if (itemsSingleTone.getItemsModelList().size()==0)
        {
            ll_trolley_container.setVisibility(View.VISIBLE);
            send_btn.setVisibility(View.GONE);
        }
        adapter.notifyItemRemoved(position);
    }
}
