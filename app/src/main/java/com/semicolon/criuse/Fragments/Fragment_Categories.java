package com.semicolon.criuse.Fragments;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.Adapters.GroceryAdapter;
import com.semicolon.criuse.Models.AllGroceries_SubCategory;
import com.semicolon.criuse.Models.GroceryPart1;
import com.semicolon.criuse.Models.GroupModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Categories extends Fragment{
    private static String TAG="TAG";
    private HomeActivity homeActivity;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<GroupModel> groupModelList;
    private GroceryPart1 groceryPart1;
    private ImageView img_back;
    private ProgressBar progBar;
    private List<String> images_group;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_categories,container,false);
        initView(view);

        return view;
    }

    public static Fragment_Categories getInstance(GroceryPart1 groceryPart1)
    {
        Fragment_Categories fragment_setting = new Fragment_Categories();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,groceryPart1);
        fragment_setting.setArguments(bundle);
        return  fragment_setting;
    }
    private void initView(View view) {
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            groceryPart1 = (GroceryPart1) bundle.getSerializable(TAG);
        }
        images_group = new ArrayList<>();
        homeActivity = (HomeActivity) getActivity();
        img_back = view.findViewById(R.id.img_back);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.white), PorterDuff.Mode.SRC_IN);
        groupModelList = new ArrayList<>();
        recView = view.findViewById(R.id.recView);
        manager =new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setNestedScrollingEnabled(false);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.BackFromGroceryRegisert2ToRegister1(groceryPart1);
            }
        });

        getData();

    }

    private void getData() {

        Api.getServices()
                .getAllGrocery_subcategories()
                .enqueue(new Callback<List<AllGroceries_SubCategory>>() {
                    @Override
                    public void onResponse(Call<List<AllGroceries_SubCategory>> call, Response<List<AllGroceries_SubCategory>> response) {
                        /*for (AllGroceries_SubCategory allGroceries_subCategory:response.body())
                        {
                            images_group.add(allGroceries_subCategory.getImage_categories());
                        }*/

                        progBar.setVisibility(View.GONE);
                        UpdateAdapterList(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<AllGroceries_SubCategory>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progBar.setVisibility(View.GONE);

                        Toast.makeText(homeActivity, getString(R.string.something_error), Toast.LENGTH_SHORT).show();
                    }
                });
        /*adapter = new GroceryAdapter(groupModelList,getActivity());
        recView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();*/
    }

    private void UpdateAdapterList(List<AllGroceries_SubCategory> allGroceries_subCategoryList) {
        if (allGroceries_subCategoryList.size()>0)
        {
            for (AllGroceries_SubCategory subCategory :allGroceries_subCategoryList)
            {
                GroupModel groupModel = new GroupModel(subCategory.getName_categories(),subCategory.getSubProducts(),subCategory.getImage_categories());
                groupModelList.add(groupModel);
            }

            adapter = new GroceryAdapter(groupModelList,getActivity());
            recView.setAdapter(adapter);

        }

    }


}
