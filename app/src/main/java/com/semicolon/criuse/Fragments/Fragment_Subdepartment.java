package com.semicolon.criuse.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.Adapters.SubdepartmentAdapter;
import com.semicolon.criuse.Models.ItemsModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Share.Common;
import com.semicolon.criuse.SingleTones.ItemsSingleTone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Subdepartment extends Fragment {
    private static String TAG_MODEL_LIST ="1";
    private static String TAG_TITLE ="0";
    private TextView tv_title;
    private AutoCompleteTextView searchView;
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private Context context;
    private TextView tv_no_cat;
    private ImageView img_back;
    private HomeActivity homeActivity;
    private List<ItemsModel> itemsModelList;
    private String title="";
    private ItemsSingleTone itemsSingleTone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_subdepartment,container,false);
        context = view.getContext();
        initView(view);
        return view;
    }
    public static Fragment_Subdepartment getInstance(List<ItemsModel> itemsModelList,String title)
    {
        Fragment_Subdepartment fragment = new Fragment_Subdepartment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG_MODEL_LIST, (Serializable) itemsModelList);
        bundle.putString(TAG_TITLE,title);
        fragment.setArguments(bundle);
        return fragment;
    }
    private void initView(View view)
    {
        itemsSingleTone = ItemsSingleTone.getInstance();
        itemsModelList = new ArrayList<>();
        homeActivity = (HomeActivity) getActivity();
        img_back = view.findViewById(R.id.img_back);
        tv_title = view.findViewById(R.id.tv_title);
        tv_no_cat = view.findViewById(R.id.tv_no_cat);
        searchView = view.findViewById(R.id.searchView);
        recView = view.findViewById(R.id.recView);
        manager = new GridLayoutManager(context,2);
        recView.setLayoutManager(manager);
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH)
                {
                    String query = searchView.getText().toString();
                    if (!TextUtils.isEmpty(query))
                    {
                        Common.CloseKeyBoard(context,searchView);
                        Search(query);
                    }else
                        {
                            Common.CloseKeyBoard(context,searchView);
                            Toast.makeText(context, R.string.enter_pro_name, Toast.LENGTH_LONG).show();
                        }
                }
                return false;
            }
        });
        img_back.setOnClickListener(view1 -> homeActivity.setNavPosition());

        Bundle bundle = getArguments();
        if (bundle!=null)
        {

            itemsModelList = (List<ItemsModel>) bundle.getSerializable(TAG_MODEL_LIST);
            title = bundle.getString(TAG_TITLE);

            UpdateUI(itemsModelList,title);
        }
    }
    private void Search(String query)
    {
    }
    private void UpdateUI(List<ItemsModel> itemsModelList, String title)
    {

        if (itemsModelList.size()>0)
        {
            adapter = new SubdepartmentAdapter(context,this.itemsModelList,this);
            recView.setAdapter(adapter);
            tv_no_cat.setVisibility(View.GONE);
        }else
            {
                tv_no_cat.setVisibility(View.VISIBLE);

            }
        tv_title.setText(title);
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
