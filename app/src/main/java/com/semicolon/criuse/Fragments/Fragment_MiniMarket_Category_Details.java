package com.semicolon.criuse.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolon.criuse.Activities.MiniMarketDetailsActivity;
import com.semicolon.criuse.Adapters.MiniMarketCategoryDetailsAdapter;
import com.semicolon.criuse.Models.ItemsModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.SingleTones.ItemsSingleTone;

import java.io.Serializable;
import java.util.List;

public class Fragment_MiniMarket_Category_Details extends Fragment {
    private static String TAG="LIST";
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<ItemsModel> itemsModelList;
    private MiniMarketDetailsActivity activity;
    private ImageView img_back;
    private TextView tv_title;
    private ItemsSingleTone itemsSingleTone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minimarket_categorydetails,container,false);
        initView(view);
        return view;
    }

    public static Fragment_MiniMarket_Category_Details getInstance(List<ItemsModel> itemsModelList)
    {

            Fragment_MiniMarket_Category_Details fragment = new Fragment_MiniMarket_Category_Details();
            Bundle bundle = new Bundle();
            bundle.putSerializable(TAG, (Serializable) itemsModelList);
            fragment.setArguments(bundle);
            return fragment;
    }
    private void initView(View view) {
        itemsSingleTone = ItemsSingleTone.getInstance();
        activity = (MiniMarketDetailsActivity) getActivity();

        img_back = view.findViewById(R.id.img_back);
        tv_title = view.findViewById(R.id.tv_title);
        recView = view.findViewById(R.id.recView);
        manager = new GridLayoutManager(getActivity(),2);
        recView.setLayoutManager(manager);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.fragmentManager.popBackStack();
            }
        });
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            itemsModelList = (List<ItemsModel>) bundle.getSerializable(TAG);
            tv_title.setText(itemsModelList.get(0).getDepartment());
            adapter = new MiniMarketCategoryDetailsAdapter(getActivity(),itemsModelList,this);
            recView.setAdapter(adapter);
        }
    }

    public void SaveItem_To_Trolley(ItemsModel itemsModel)
    {
        itemsModel.setProduct_cost(String.valueOf(Integer.parseInt(itemsModel.getProduct_amount())*Integer.parseInt(itemsModel.getProduct_cost())));
        itemsSingleTone.SaveItem_To_Trolley(itemsModel);
        for (ItemsModel itemsModel2:itemsSingleTone.getItemsModelList())
        {
            Log.e("ItemModel",itemsModel2.getProduct_name()+"\n"+itemsModel2.getId_product()+"\n"+itemsModel2.getProduct_amount()+"\n"+itemsModel2.getIs_admin());
        }
    }

    public void Remove_From_Trolley(ItemsModel itemsModel)
    {
        itemsSingleTone.RemoveItem_From_Trolley(itemsModel);
    }
}
