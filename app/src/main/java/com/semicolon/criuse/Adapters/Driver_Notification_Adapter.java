package com.semicolon.criuse.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.semicolon.criuse.Fragments.Fragment_Driver_Notification;
import com.semicolon.criuse.Models.Driver_Grocery_Notification_Model;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Driver_Notification_Adapter extends RecyclerView.Adapter<Driver_Notification_Adapter.MyHolder> {
    private Context context;
    private List<Driver_Grocery_Notification_Model> notificationList;
    private Fragment_Driver_Notification fragment;
    public Driver_Notification_Adapter(Context context, List<Driver_Grocery_Notification_Model> notificationList, Fragment fragment) {
        this.context = context;
        this.notificationList = notificationList;
        this.fragment = (Fragment_Driver_Notification) fragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.driver_geocery_not_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Driver_Grocery_Notification_Model notificationModel = notificationList.get(position);
        holder.BindData(notificationModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Driver_Grocery_Notification_Model notificationModel = notificationList.get(holder.getAdapterPosition());

                fragment.setItem(notificationModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private CardView card;
        private TextView tv_name,tv_address,tv_date;
        private Button btn_accept,btn_refuse;
        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            card = itemView.findViewById(R.id.card);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_date = itemView.findViewById(R.id.tv_date);
            btn_accept = itemView.findViewById(R.id.btn_accept);
            btn_refuse = itemView.findViewById(R.id.btn_refuse);


        }

        public void BindData(Driver_Grocery_Notification_Model notification_model)
        {

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+notification_model.getClient_photo())).into(image);
            tv_name.setText(notification_model.getClient_name());
            tv_date.setText(notification_model.getDelivery_order_time());
            tv_address.setText(notification_model.getBill_address());

        }
    }
}
