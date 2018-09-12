package com.semicolon.criuse.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolon.criuse.Adapters.Driver_Grocery_Order_Details_Adapter;
import com.semicolon.criuse.Models.Driver_Grocery_OrderModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class Driver_Grocery_OrderDetailsActivity extends AppCompatActivity {
    private CardView cardView;
    private ImageView image_back,image_icon;
    private TextView tv_info,tv_name,tv_phone,tv_address,tv_cost;
    private CircleImageView image;
    private ExpandableLayout expand_layout;
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private Driver_Grocery_OrderModel driver_grocery_orderModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_grocery);
        initView();
        getDataFromIntent();
    }

    private void initView() {
        cardView = findViewById(R.id.cardView);
        image_back = findViewById(R.id.image_back);
        image_icon = findViewById(R.id.image_icon);
        tv_info = findViewById(R.id.tv_info);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_address = findViewById(R.id.tv_address);
        tv_cost = findViewById(R.id.tv_cost);
        image = findViewById(R.id.image);
        expand_layout = findViewById(R.id.expand_layout);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this);
        recView.setLayoutManager(manager);

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        expand_layout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                if (state==ExpandableLayout.State.EXPANDED)
                {
                    image_icon.setImageResource(R.drawable.exoand_icon);

                }else if (state==ExpandableLayout.State.COLLAPSED)
                {
                    image_icon.setImageResource(R.drawable.collaps_icon);

                }
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expand_layout.toggle(true);
            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            driver_grocery_orderModel = (Driver_Grocery_OrderModel) intent.getSerializableExtra("data");
            UpdateUI(driver_grocery_orderModel);
        }
    }

    private void UpdateUI(Driver_Grocery_OrderModel driver_grocery_orderModel) {
        Picasso.with(this).load(Tags.IMAGE_URL+ Uri.parse(driver_grocery_orderModel.getClient_user_photo())).placeholder(R.drawable.user_profile).into(image);
        tv_name.setText(driver_grocery_orderModel.getClient_user_name());
        tv_phone.setText(driver_grocery_orderModel.getClient_user_phone());
        tv_address.setText(driver_grocery_orderModel.getBill_address());
        tv_cost.setText(String.valueOf(driver_grocery_orderModel.getBill_cost())+" "+getString(R.string.sar));

        adapter = new Driver_Grocery_Order_Details_Adapter(this,driver_grocery_orderModel.getBill_productList());
        recView.setAdapter(adapter);
    }
}
