package com.semicolon.criuse.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolon.criuse.Fragments.Fragment_MiniMarket_Category;
import com.semicolon.criuse.Models.MiniMarket_SubCategory;
import com.semicolon.criuse.R;

import java.util.List;

public class MiniMarketDetailsActivity extends AppCompatActivity {
    private ImageView back;
    private TextView tv_title;
    private List<MiniMarket_SubCategory> miniMarket_subCategoryList;
    private Fragment_MiniMarket_Category fragment_miniMarket_category;
    public FragmentManager fragmentManager;
    private String market_name,market_phone,market_delivery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_market_details);
        fragmentManager = getSupportFragmentManager();
        initView();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            miniMarket_subCategoryList = (List<MiniMarket_SubCategory>) intent.getSerializableExtra("data");
            market_name = intent.getStringExtra("market_name");
            market_phone = intent.getStringExtra("market_phone");
            market_delivery = intent.getStringExtra("market_delivery");
            fragment_miniMarket_category = Fragment_MiniMarket_Category.getInstance(miniMarket_subCategoryList,market_name,market_phone,market_delivery);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.minimarket_fragments_container,fragment_miniMarket_category,"fragment1");
            transaction.addToBackStack("Fragment1");
            transaction.commit();
        }
    }

    private void initView() {
        back = findViewById(R.id.back);
        tv_title = findViewById(R.id.tv_title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (fragmentManager.getBackStackEntryCount()==1)
        {
            finish();
        }else
            {
                super.onBackPressed();
            }



    }
}
