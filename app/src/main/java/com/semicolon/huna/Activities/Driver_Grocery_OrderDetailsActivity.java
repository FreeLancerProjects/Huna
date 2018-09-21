package com.semicolon.huna.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.huna.Adapters.Driver_Grocery_Order_Details_Adapter;
import com.semicolon.huna.Models.ChatModel;
import com.semicolon.huna.Models.Driver_Grocery_OrderModel;
import com.semicolon.huna.Models.ResponseModel;
import com.semicolon.huna.Models.UserModel;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Api;
import com.semicolon.huna.Services.Tags;
import com.semicolon.huna.Share.Common;
import com.semicolon.huna.SingleTones.UserSingletone;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private UserModel userModel;
    private UserSingletone userSingletone;
    private String fragment_type;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_grocery);
        initView();
        getDataFromIntent();
    }

    private void initView() {
        String lng = Locale.getDefault().getDisplayLanguage();

        userSingletone = UserSingletone.getInstance();
        userModel = userSingletone.getUserModel();
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolBar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.h_menu));

        cardView = findViewById(R.id.cardView);
        image_back = findViewById(R.id.image_back);
        if (lng.equals("العربية"))
        {
            image_back.setRotation(180f);
        }
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
            fragment_type = intent.getStringExtra("fragment_type");
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (fragment_type.equals("previous"))
        {
        }else
        {
            getMenuInflater().inflate(R.menu.done_chat_menu,menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.delivered:
                EndOrder(driver_grocery_orderModel);
                break;
            case R.id.chat:
                ChatModel chatModel = new ChatModel(userModel.getUser_id(),driver_grocery_orderModel.getClient_id_fk(),userModel.getUser_type(),userModel.getUser_photo(),"",driver_grocery_orderModel.getClient_user_name(),driver_grocery_orderModel.getClient_user_photo(),driver_grocery_orderModel.getRoom_id());
                Intent intent = new Intent(Driver_Grocery_OrderDetailsActivity.this,ChatActivity.class);
                intent.putExtra("data",chatModel);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void EndOrder(Driver_Grocery_OrderModel driver_grocery_orderModel)
    {
        ProgressDialog progressDialog= Common.CreateProgressDialog(this,getString(R.string.ending_req));

        Api.getServices().endOrder(driver_grocery_orderModel.getBill_id(),userModel.getUser_type(),userModel.getUser_id(),driver_grocery_orderModel.getId_delivery_user_fk(),driver_grocery_orderModel.getRoom_id())
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            if (response.body().getSuccess_end()==1)
                            {
                                finish();
                            }else
                            {
                                Toast.makeText(Driver_Grocery_OrderDetailsActivity.this,R.string.something_error, Toast.LENGTH_LONG).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e("Error",t.getMessage());
                        Toast.makeText(Driver_Grocery_OrderDetailsActivity.this,R.string.something_error, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
