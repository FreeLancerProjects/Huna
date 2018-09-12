package com.semicolon.criuse.Activities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.criuse.Adapters.SearchAdapter;
import com.semicolon.criuse.Models.ItemsModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Api;
import com.semicolon.criuse.Share.Common;
import com.semicolon.criuse.SingleTones.ItemsSingleTone;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spencerstudios.com.bungeelib.Bungee;

public class SearchActivity extends AppCompatActivity {
    private ImageView img_back;
    private AutoCompleteTextView searchView;
    private ItemsSingleTone itemsSingleTone;
    private LinearLayout no_result_container;
    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<ItemsModel> itemsModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        itemsSingleTone = ItemsSingleTone.getInstance();
        itemsModelList = new ArrayList<>();
        img_back = findViewById(R.id.img_back);
        searchView = findViewById(R.id.searchView);
        no_result_container = findViewById(R.id.no_result_container);
        progBar = findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = findViewById(R.id.recView);
        manager = new GridLayoutManager(this,2);
        recView.setLayoutManager(manager);
        adapter = new SearchAdapter(this,itemsModelList);
        recView.setAdapter(adapter);


        img_back.setOnClickListener(view ->{
            finish();
            Bungee.fade(SearchActivity.this);
        } );

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH)
                {
                    String query = searchView.getText().toString();
                    Common.CloseKeyBoard(SearchActivity.this,searchView);

                    if (TextUtils.isEmpty(query))
                    {
                        Toast.makeText(SearchActivity.this, R.string.sear_title, Toast.LENGTH_LONG).show();
                    }else
                        {
                            progBar.setVisibility(View.VISIBLE);
                            search(query);
                        }
                }
                return false;
            }
        });
    }

    private void search(String query) {
        Api.getServices().search(query)
                .enqueue(new Callback<List<ItemsModel>>() {
                    @Override
                    public void onResponse(Call<List<ItemsModel>> call, Response<List<ItemsModel>> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body().size()>0)
                            {
                                no_result_container.setVisibility(View.GONE);
                                UpdateAdapter(response.body());

                            }else
                                {
                                    no_result_container.setVisibility(View.VISIBLE);

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ItemsModel>> call, Throwable t) {
                        progBar.setVisibility(View.GONE);
                        Log.e("Error",t.getMessage());
                        Toast.makeText(SearchActivity.this, R.string.something_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void UpdateAdapter(List<ItemsModel> itemsModelList) {
        this.itemsModelList.clear();
        for (ItemsModel itemsModel:itemsModelList)
        {
            itemsModel.setItem_one_cost(itemsModel.getProduct_cost());
            itemsModel.setProduct_amount("0");
            this.itemsModelList.add(itemsModel);
        }

        adapter.notifyDataSetChanged();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.fade(this);
    }
}
