package com.semicolon.criuse.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.semicolon.criuse.Fragments.Fragment_Car;
import com.semicolon.criuse.Models.ItemsModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrolleyAdapter extends RecyclerView.Adapter<TrolleyAdapter.MyHolder>{
    private Context context;
    private List<ItemsModel> itemsModelList;
    private Fragment_Car fragment_car;

    public TrolleyAdapter(Context context, List<ItemsModel> itemsModelList, Fragment fragment) {
        this.context = context;
        this.itemsModelList = itemsModelList;
        this.fragment_car = (Fragment_Car) fragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trolley_item_row,parent,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ItemsModel itemsModel = itemsModelList.get(position);
        holder.BindData(itemsModel);
        int m = Integer.parseInt(itemsModel.getProduct_amount());

        if (m>0)
        {
            holder.decrease_btn.setVisibility(View.VISIBLE);
        }else
        {
            holder.decrease_btn.setVisibility(View.GONE);

        }
        holder.increase_btn.setOnClickListener(view -> {
            int amount = Integer.parseInt(holder.tv_counter.getText().toString())+1;
            ItemsModel itemsModel1 = itemsModelList.get(holder.getAdapterPosition());

            holder.tv_counter.setText(String.valueOf(amount));
            holder.decrease_btn.setVisibility(View.VISIBLE);
            itemsModel1.setProduct_amount(String.valueOf(amount));
            fragment_car.SaveItem_To_Trolley(itemsModel1);
            holder.StartAnimation(holder.tv_price);
            holder.tv_price.setText(String.valueOf(Integer.parseInt(itemsModel1.getItem_one_cost())*amount)+" ريال");


        });

        holder.decrease_btn.setOnClickListener(view -> {
            int amount = Integer.parseInt(holder.tv_counter.getText().toString())-1;
            ItemsModel itemsModel1 = itemsModelList.get(holder.getAdapterPosition());
            if (amount==0)
            {
                holder.tv_counter.setText("0");

                holder.decrease_btn.setVisibility(View.INVISIBLE);
                itemsModel1.setProduct_amount("0");
                //holder.img_trolley.setVisibility(View.GONE);
                fragment_car.Remove_From_Trolley(itemsModel1,position);
                holder.StartAnimation(holder.tv_price);
                holder.tv_price.setText(itemsModel1.getItem_one_cost()+" ريال");


            }else
                {
                    holder.tv_counter.setText(String.valueOf(amount));
                    holder.decrease_btn.setVisibility(View.VISIBLE);
                    itemsModel1.setProduct_amount(String.valueOf(amount));
                    fragment_car.SaveItem_To_Trolley(itemsModel1);
                    holder.StartAnimation(holder.tv_price);
                    holder.tv_price.setText(String.valueOf(Integer.parseInt(itemsModel1.getItem_one_cost())*amount)+" ريال");


                }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemsModel itemsModel1 = itemsModelList.get(holder.getAdapterPosition());

                fragment_car.Remove_From_Trolley(itemsModel1,holder.getAdapterPosition());
            }
        });



    }

    @Override
    public int getItemCount() {
        return itemsModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private RoundedImageView image;
        private TextView tv_name,tv_price,tv_counter;
        private ImageView increase_btn,decrease_btn,delete;
        public MyHolder(View itemView) {
            super(itemView);
            image   = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_counter = itemView.findViewById(R.id.tv_counter);
            increase_btn = itemView.findViewById(R.id.increase_btn);
            decrease_btn = itemView.findViewById(R.id.decrease_btn);
            delete = itemView.findViewById(R.id.delete);


        }

        public void BindData(ItemsModel itemsModel)
        {
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+itemsModel.getProduct_image())).into(image);
            tv_name.setText(itemsModel.getProduct_name());
            tv_price.setText(itemsModel.getProduct_cost()+" "+context.getString(R.string.sar));
            tv_counter.setText(itemsModel.getProduct_amount());

        }
        public void StartAnimation(View view)
        {
            Animation animation = AnimationUtils.loadAnimation(context,R.anim.bounce);
            view.clearAnimation();
            view.startAnimation(animation);
        }
    }



}
