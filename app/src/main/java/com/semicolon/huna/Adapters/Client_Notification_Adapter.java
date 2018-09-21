package com.semicolon.huna.Adapters;

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

import com.semicolon.huna.Fragments.Fragment_Client_Notification;
import com.semicolon.huna.Models.Client_Notification_Model;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Client_Notification_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_NOTIFICATION_REACT =1;
    private final int ITEM_NOTIFICATION_NORMAL =2;

    private Context context;
    private Fragment_Client_Notification fragment;
    private List<Client_Notification_Model> client_notification_modelList;
    public Client_Notification_Adapter(Context context, List<Client_Notification_Model> client_notification_modelList, Fragment fragment) {
        this.context = context;
        this.client_notification_modelList = client_notification_modelList;
        this.fragment = (Fragment_Client_Notification) fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType== ITEM_NOTIFICATION_REACT)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.client_not_row,parent,false);
            return new MyHolder(view);
        }else
            {
                View view = LayoutInflater.from(context).inflate(R.layout.refuse_order_row,parent,false);
                return new MyHolder2(view);
            }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Client_Notification_Model notificationModel = client_notification_modelList.get(position);

        if (holder instanceof MyHolder)
        {
            MyHolder myHolder = (MyHolder) holder;
            myHolder.BindData(notificationModel);
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Client_Notification_Model model = client_notification_modelList.get(myHolder.getAdapterPosition());
                    fragment.setItem(model);
                }
            });

            myHolder.btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Client_Notification_Model notificationModel = client_notification_modelList.get(holder.getAdapterPosition());
                    fragment.sendAccept(myHolder.getAdapterPosition(),notificationModel.getId_delivery_order(),notificationModel.getBill_num_fk(),notificationModel.getId_delivery_user_fk(),notificationModel.getMarket_type());

                }
            });

            myHolder.btn_refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Client_Notification_Model notificationModel = client_notification_modelList.get(holder.getAdapterPosition());
                    fragment.sendRefuse(myHolder.getAdapterPosition(),notificationModel.getId_delivery_order(),notificationModel.getBill_num_fk(),notificationModel.getId_delivery_user_fk(),notificationModel.getMarket_type());

                }
            });


        }else if (holder instanceof MyHolder2)
        {
            MyHolder2 myHolder2 = (MyHolder2) holder;
            myHolder2.BindData(notificationModel);

        }
        //holder.BindData(notificationModel);
    }

    @Override
    public int getItemCount() {
        return client_notification_modelList.size();
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
            tv_address = itemView.findViewById(R.id.tv_order_address);
            tv_date = itemView.findViewById(R.id.tv_date);
            btn_accept = itemView.findViewById(R.id.btn_accept);
            btn_refuse = itemView.findViewById(R.id.btn_refuse);


        }

        public void BindData(Client_Notification_Model notification_model)
        {

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+notification_model.getDelivery_user_photo())).into(image);
            tv_name.setText(notification_model.getDelivery_user_name());
            tv_date.setText(notification_model.getDelivery_order_time());
            tv_address.setText(notification_model.getBill_address());

        }
    }


    public class MyHolder2 extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView tv_name,tv_date,tv_not_txt;
        public MyHolder2(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_not_txt = itemView.findViewById(R.id.tv_not_txt);



        }

        public void BindData(Client_Notification_Model notification_model)
        {

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+notification_model.getDelivery_user_photo())).into(image);
            tv_name.setText(notification_model.getDelivery_user_name());
            tv_date.setText(notification_model.getDelivery_order_time());

            if (notification_model.getDelivery_orders_replay().equals(Tags.refuse))
            {
                tv_not_txt.setText(context.getString(R.string.order_canceld));
            }else if (notification_model.getDelivery_orders_replay().equals(Tags.refuse_noavailable_driver))
            {
                tv_not_txt.setText(R.string.refuse_nodriver_available);

            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        Client_Notification_Model client_notification_model = client_notification_modelList.get(position);
        if (client_notification_model.getDelivery_orders_replay().equals(Tags.accept))
        {
            return ITEM_NOTIFICATION_REACT;
        }else
            {
                return ITEM_NOTIFICATION_NORMAL;
            }


    }
}
