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
import com.semicolon.criuse.Fragments.Fragment_MiniMarkets;
import com.semicolon.criuse.Models.MiniMarketDataModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MiniMarketAdapter extends RecyclerView.Adapter<MiniMarketAdapter.MyHolder>{
    private Context context;
    private List<MiniMarketDataModel> miniMarketModelList;
    private Fragment_MiniMarkets fragment_miniMarkets;

    public MiniMarketAdapter(Context context, List<MiniMarketDataModel> miniMarketModelList, Fragment fragment) {
        this.context = context;
        this.miniMarketModelList = miniMarketModelList;
        this.fragment_miniMarkets = (Fragment_MiniMarkets) fragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.minimarket_row,parent,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MiniMarketDataModel miniMarketModel = miniMarketModelList.get(position);
        holder.BindData(miniMarketModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_miniMarkets.setPos(position);
                //fragment_miniMarkets.setPos(holder.getAdapterPosition(),miniMarketModel.getName_categories());
            }
        });
    }

    @Override
    public int getItemCount() {
        return miniMarketModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private RoundedImageView image;
        private TextView tv_name,tv_distance;
        public MyHolder(View itemView) {
            super(itemView);
            image   = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_distance = itemView.findViewById(R.id.tv_distance);

        }

        public void BindData(MiniMarketDataModel miniMarketModel)
        {

            tv_name.setText(miniMarketModel.getMarket_name());
            tv_distance.setText(String.format("%.1f",miniMarketModel.getDistance())+" "+context.getString(R.string.km));
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+miniMarketModel.getMarket_photo())).into(image);
            //
           // tv_name.setText(miniMarketModel.getName_categories());
        }
    }

}
