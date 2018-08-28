package com.semicolon.criuse.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.criuse.Activities.MiniMarketDetailsActivity;
import com.semicolon.criuse.Adapters.MiniMarket_Category_Adapter;
import com.semicolon.criuse.Models.ItemsModel;
import com.semicolon.criuse.Models.MiniMarket_SubCategory;
import com.semicolon.criuse.Models.MiniMarket_SubProduct;
import com.semicolon.criuse.Models.UserModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;
import com.semicolon.criuse.SingleTones.UserSingletone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Fragment_MiniMarket_Category extends Fragment {
    private static String TAG="LIST";
    private static String TAG_NAME="NAME";
    private static String TAG_PHONE="PHONE";
    private static String TAG_DELIVERY="DELIVERY";

    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<MiniMarket_SubCategory> miniMarket_subCategoryList;
    private MiniMarketDetailsActivity activity;
    private UserModel userModel;
    private UserSingletone userSingletone;
    private String market_name,market_phone,market_delivery;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minimarket_category,container,false);
        initView(view);
        return view;
    }

    public static Fragment_MiniMarket_Category getInstance(List<MiniMarket_SubCategory> miniMarket_subCategoryList,String market_name,String market_phone,String market_delivery)
    {

            Fragment_MiniMarket_Category fragment = new Fragment_MiniMarket_Category();
            Bundle bundle = new Bundle();
            bundle.putSerializable(TAG, (Serializable) miniMarket_subCategoryList);
            bundle.putString(TAG_NAME,market_name);
            bundle.putString(TAG_PHONE,market_phone);
            bundle.putString(TAG_DELIVERY,market_delivery);
            fragment.setArguments(bundle);
            return fragment;
    }
    private void initView(View view) {
        userSingletone = UserSingletone.getInstance();
        userModel = userSingletone.getUserModel();
        activity = (MiniMarketDetailsActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            miniMarket_subCategoryList = (List<MiniMarket_SubCategory>) bundle.getSerializable(TAG);
            market_name = bundle.getString(TAG_NAME);
            market_phone = bundle.getString(TAG_PHONE);
            market_delivery = bundle.getString(TAG_DELIVERY);

        }
        recView = view.findViewById(R.id.recView);
        manager = new GridLayoutManager(view.getContext(),3);
        recView.setLayoutManager(manager);
        adapter = new MiniMarket_Category_Adapter(getActivity(),miniMarket_subCategoryList,this);
        recView.setAdapter(adapter);

    }

    public void setPos(int pos,String title,String id_categories)
    {
        Log.e("name",market_name+"_+_+");
        Log.e("cost",market_phone+"_+_+");

        List<ItemsModel> itemsModelList = new ArrayList<>();
        MiniMarket_SubCategory miniMarket_subCategory = miniMarket_subCategoryList.get(pos);
        List<MiniMarket_SubProduct> miniMarket_subProductList = miniMarket_subCategory.getMiniMarket_subProductList();

        for (MiniMarket_SubProduct ms:miniMarket_subProductList)
        {
            ItemsModel  itemsModel = new ItemsModel("",id_categories,ms.getId_product(),ms.getProduct_price(),"0",ms.getMarket_id_fk(), Tags.isAdmin_minimarket,ms.getProduct_image(),ms.getProduct_title(),title,market_name,market_phone,market_delivery,ms.getProduct_price());
            itemsModelList.add(itemsModel);
        }

        Fragment_MiniMarket_Category_Details fragment = Fragment_MiniMarket_Category_Details.getInstance(itemsModelList);
        FragmentTransaction transaction = activity.fragmentManager.beginTransaction();
        transaction.hide(activity.fragmentManager.findFragmentByTag("fragment1"));
        transaction.add(R.id.minimarket_fragments_container,fragment,"fragment2");
        transaction.addToBackStack("Fragment2");
        transaction.commit();



        //item model list

    }
}
