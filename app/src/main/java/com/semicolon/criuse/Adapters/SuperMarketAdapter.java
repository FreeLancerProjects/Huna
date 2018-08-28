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

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.semicolon.criuse.Fragments.Fragment_Supermarkets;
import com.semicolon.criuse.Models.SuperMarketModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SuperMarketAdapter extends RecyclerView.Adapter<SuperMarketAdapter.MyHolder>{
    private Context context;
    private List<SuperMarketModel> superMarketModelList;
    private Fragment_Supermarkets fragment_supermarkets;
    public SuperMarketAdapter(Context context, List<SuperMarketModel> superMarketModelList, Fragment fragment) {
        this.context = context;
        this.superMarketModelList = superMarketModelList;
        this.fragment_supermarkets = (Fragment_Supermarkets) fragment;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.deparment_item_row,parent,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        SuperMarketModel superMarketModel = superMarketModelList.get(position);
        holder.BindData(superMarketModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment_supermarkets.setPos(holder.getAdapterPosition(),superMarketModel.getName_categories(),superMarketModel.getId_categories(),superMarketModel.getMarket_name(),superMarketModel.getMarket_phone(),superMarketModel.getDelivery_cost(),superMarketModel.getMarket_id_fk());
            }
        });
    }

    @Override
    public int getItemCount() {
        return superMarketModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private RoundedImageView dept_item_image;
        private TextView tv_dept_item_name;
        public MyHolder(View itemView) {
            super(itemView);
            dept_item_image   = itemView.findViewById(R.id.dept_item_image);
            tv_dept_item_name = itemView.findViewById(R.id.tv_dept_item_name);
        }

        public void BindData(SuperMarketModel superMarketModel)
        {
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+superMarketModel.getImage_categories())).into(dept_item_image);
            tv_dept_item_name.setText(superMarketModel.getName_categories());
        }
    }

}
