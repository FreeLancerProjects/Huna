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

import com.semicolon.criuse.Fragments.Fragment_Client_Current_Order;
import com.semicolon.criuse.Fragments.Fragment_Client_Previous_Order;
import com.semicolon.criuse.Models.ClientOrderModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Client_Current_Previous_Order_Adapter extends RecyclerView.Adapter<Client_Current_Previous_Order_Adapter.MyHolder> {
    private Context context;
    private List<ClientOrderModel> clientOrderModelList;
    private Fragment_Client_Current_Order fragment1;
    private Fragment_Client_Previous_Order fragment2;

    private String order_state;

    public Client_Current_Previous_Order_Adapter(Context context, List<ClientOrderModel> clientOrderModelList,Fragment fragment,String order_state) {
        this.context = context;
        this.clientOrderModelList = clientOrderModelList;
        this.order_state = order_state;
        if (order_state.equals("current"))
        {
            this.fragment1= (Fragment_Client_Current_Order) fragment;
        }else
            {
                this.fragment2 = (Fragment_Client_Previous_Order) fragment;
            }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (order_state.equals("current"))
        {
            view= LayoutInflater.from(context).inflate(R.layout.current_order_row,parent,false);

        }else
            {
                view= LayoutInflater.from(context).inflate(R.layout.previous_order_row,parent,false);

            }
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ClientOrderModel  clientOrderModel = clientOrderModelList.get(position);
        holder.BindData(clientOrderModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientOrderModel  clientOrderModel = clientOrderModelList.get(holder.getAdapterPosition());
                if (order_state.equals("current"))
                {
                    fragment1.setItem(clientOrderModel);
                }else
                    {
                        fragment2.setItem(clientOrderModel);

                    }

            }
        });
    }

    @Override
    public int getItemCount() {
        return clientOrderModelList.size();
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

        public void BindData(ClientOrderModel clientOrderModel)
        {
            tv_name.setText(clientOrderModel.getDelivery_user_name());
            tv_date.setText(clientOrderModel.getDelivery_order_time());
            tv_address.setText(clientOrderModel.getBill_address());
            Picasso.with(context).load(Tags.IMAGE_URL+Uri.parse(clientOrderModel.getDelivery_user_photo())).into(image);


        }
    }
}
