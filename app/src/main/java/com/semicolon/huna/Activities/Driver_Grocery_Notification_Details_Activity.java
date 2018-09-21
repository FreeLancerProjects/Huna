package com.semicolon.huna.Activities;

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

import com.semicolon.huna.Adapters.Driver_Grocery_Notification_Details_Adapter;
import com.semicolon.huna.Models.Driver_Grocery_Notification_Model;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Tags;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class Driver_Grocery_Notification_Details_Activity extends AppCompatActivity {

    private CardView cardView;
    private ImageView image_back,image_icon;
    private TextView tv_name,tv_phone,tv_address,tv_cost;
    private CircleImageView image;
    private ExpandableLayout expand_layout;
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private Driver_Grocery_Notification_Model driver_grocery_notification_model;
    private String type="";//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__grocery__notification__details_);
        initView();
        getDataFromIntent();
    }
    private void initView() {
        cardView = findViewById(R.id.cardView);
        image_back = findViewById(R.id.image_back);
        image_icon = findViewById(R.id.image_icon);
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
            driver_grocery_notification_model = (Driver_Grocery_Notification_Model) intent.getSerializableExtra("data");
            UpdateUI(driver_grocery_notification_model);
        }
    }
    private void UpdateUI(Driver_Grocery_Notification_Model driver_grocery_notification_model) {


        Picasso.with(this).load(Tags.IMAGE_URL+ Uri.parse(driver_grocery_notification_model.getClient_photo())).placeholder(R.drawable.user_profile).into(image);
        tv_name.setText(driver_grocery_notification_model.getClient_name());
        tv_phone.setText(driver_grocery_notification_model.getClient_phone());
        tv_address.setText(driver_grocery_notification_model.getBill_address());
        tv_cost.setText(String.valueOf(driver_grocery_notification_model.getBill_cost())+" "+getString(R.string.sar));

        adapter = new Driver_Grocery_Notification_Details_Adapter(this,driver_grocery_notification_model.getBill_product());
        recView.setAdapter(adapter);
    }
}
