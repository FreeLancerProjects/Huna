package com.semicolon.criuse.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semicolon.criuse.Fragments.Fragment_Driver_Grocery_Current_Order;
import com.semicolon.criuse.Fragments.Fragment_Driver_Grocery_Previous_Order;
import com.semicolon.criuse.Models.Driver_Grocery_OrderModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Driver_Grocery_Current_Previous_Order_Adapter extends RecyclerView.Adapter<Driver_Grocery_Current_Previous_Order_Adapter.MyHolder> {
    private Context context;
    private List<Driver_Grocery_OrderModel> driver_grocery_orderModelList;
    private Fragment_Driver_Grocery_Current_Order fragment1;
    private Fragment_Driver_Grocery_Previous_Order fragment2;

    private String order_state;

    public Driver_Grocery_Current_Previous_Order_Adapter(Context context, List<Driver_Grocery_OrderModel> driver_grocery_orderModelList, Fragment fragment,String order_state) {
        this.context = context;
        this.driver_grocery_orderModelList = driver_grocery_orderModelList;
        this.order_state = order_state;
        if (order_state.equals("current"))
        {
            this.fragment1 = (Fragment_Driver_Grocery_Current_Order) fragment;
        }else
            {
                this.fragment2 = (Fragment_Driver_Grocery_Previous_Order) fragment;
            }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (order_state.equals("current"))
        {
            view = LayoutInflater.from(context).inflate(R.layout.current_order_row,parent,false);

        }else
            {
                view = LayoutInflater.from(context).inflate(R.layout.previous_order_row,parent,false);

            }
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Driver_Grocery_OrderModel  driver_grocery_orderModel = driver_grocery_orderModelList.get(position);
        holder.BindData(driver_grocery_orderModel);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Driver_Grocery_OrderModel  driver_grocery_orderModel = driver_grocery_orderModelList.get(holder.getAdapterPosition());


                if (order_state.equals("current"))
                {
                    fragment1.setItem(driver_grocery_orderModel);

                }else
                {
                    fragment2.setItem(driver_grocery_orderModel);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return driver_grocery_orderModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView tv_name,tv_date,tv_address;
        public MyHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_date = itemView.findViewById(R.id.tv_date);



        }

        public void BindData(Driver_Grocery_OrderModel driver_grocery_orderModel)
        {
            tv_name.setText(driver_grocery_orderModel.getClient_user_name());
            tv_date.setText(driver_grocery_orderModel.getDelivery_order_time());
            tv_address.setText(driver_grocery_orderModel.getBill_address());
            Picasso.with(context).load(Tags.IMAGE_URL+Uri.parse(driver_grocery_orderModel.getClient_user_photo())).into(image);


        }
    }
}
