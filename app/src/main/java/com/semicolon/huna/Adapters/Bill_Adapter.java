package com.semicolon.huna.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semicolon.huna.Models.Bill_Model;
import com.semicolon.huna.R;

import java.util.List;

public class Bill_Adapter extends RecyclerView.Adapter<Bill_Adapter.MyHolder> {
    private Context context;
    private List<Bill_Model> bill_modelList;

    public Bill_Adapter(Context context, List<Bill_Model> bill_modelList) {
        this.context = context;
        this.bill_modelList = bill_modelList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bill_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Bill_Model bill_model = bill_modelList.get(position);
        holder.BindData(bill_model);
    }

    @Override
    public int getItemCount() {
        return bill_modelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_name,tv_phone,tv_subtotal,tv_delivery,tv_total;
        public MyHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_subtotal = itemView.findViewById(R.id.tv_subtotal);
            tv_delivery = itemView.findViewById(R.id.tv_delivery);
            tv_total = itemView.findViewById(R.id.tv_ordercost);


        }

        public void BindData(Bill_Model bill_model)
        {
            tv_name.setText(bill_model.getName());
            tv_phone.setText(bill_model.getPhone());
            tv_delivery.setText(bill_model.getDelivery()+" "+context.getString(R.string.sar));
            tv_subtotal.setText(bill_model.getSubtotal()+" "+context.getString(R.string.sar));
            int total = bill_model.getSubtotal()+bill_model.getDelivery();
            tv_total.setText(total+" "+context.getString(R.string.sar));

        }
    }
}
