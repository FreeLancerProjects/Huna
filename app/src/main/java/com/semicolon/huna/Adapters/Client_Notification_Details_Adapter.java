package com.semicolon.huna.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.semicolon.huna.Models.Client_Notification_Model;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Client_Notification_Details_Adapter extends RecyclerView.Adapter<Client_Notification_Details_Adapter.MyHolder> {

    private Context context;
    private List<Client_Notification_Model.Bill_Product> bill_productList;

    public Client_Notification_Details_Adapter(Context context, List<Client_Notification_Model.Bill_Product> bill_productList) {
        this.context = context;
        this.bill_productList = bill_productList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.client_notification_details_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Client_Notification_Model.Bill_Product bill_product = bill_productList.get(position);
        holder.BindData(bill_product);
    }

    @Override
    public int getItemCount() {
        return bill_productList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private RoundedImageView image;
        private TextView tv_name,tv_amount,tv_cost;
        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_cost = itemView.findViewById(R.id.tv_cost);

        }

        public void BindData(Client_Notification_Model.Bill_Product bill_product)
        {
            Picasso.with(context).load(Tags.IMAGE_URL+Uri.parse(bill_product.getProduct_image())).into(image);
            tv_name.setText(bill_product.getProduct_title());
            tv_amount.setText(bill_product.getProduct_amount());
            tv_cost.setText(bill_product.getProduct_cost()+" "+context.getString(R.string.sar));
        }
    }
}
