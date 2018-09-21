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

import com.semicolon.huna.Adapters.Client_Notification_Details_Adapter;
import com.semicolon.huna.Models.Client_Notification_Model;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Tags;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClientNotificationDetailsActivity extends AppCompatActivity {
    private CardView cardView;
    private ImageView image_back,image_icon;
    private TextView tv_info,tv_name,tv_phone,tv_address,tv_cost;
    private CircleImageView image;
    private ExpandableLayout expand_layout;
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private Client_Notification_Model client_notification_model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        initView();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            client_notification_model = (Client_Notification_Model) intent.getSerializableExtra("data");
            UpdateUI(client_notification_model);
        }
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

    private void UpdateUI(Client_Notification_Model client_notification_model) {
        if (client_notification_model.getMarket_type().equals(Tags.market_type_driver))
        {
            tv_info.setText(R.string.driver_info);
        }else if (client_notification_model.getMarket_type().equals(Tags.market_type_grocery))
        {
            tv_info.setText(R.string.groc_info);

        }

        Picasso.with(this).load(Tags.IMAGE_URL+Uri.parse(client_notification_model.getDelivery_user_photo())).placeholder(R.drawable.user_profile).into(image);
        tv_name.setText(client_notification_model.getDelivery_user_name());
        tv_phone.setText(client_notification_model.getDelivery_user_phone());
        tv_address.setText(client_notification_model.getBill_address());
        tv_cost.setText(String.valueOf(client_notification_model.getBill_cost())+" "+getString(R.string.sar));

        adapter = new Client_Notification_Details_Adapter(this,client_notification_model.getBill_product());
        recView.setAdapter(adapter);
    }
}
