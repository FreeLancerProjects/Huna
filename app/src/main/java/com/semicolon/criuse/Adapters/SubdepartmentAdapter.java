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
import com.semicolon.criuse.Fragments.Fragment_Subdepartment;
import com.semicolon.criuse.Models.ItemsModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubdepartmentAdapter extends RecyclerView.Adapter<SubdepartmentAdapter.MyHolder>{
    private Context context;
    private List<ItemsModel> itemsModelList;
    private Fragment_Subdepartment subdepartment;

    public SubdepartmentAdapter(Context context, List<ItemsModel> itemsModelList, Fragment fragment) {
        this.context = context;
        this.itemsModelList = itemsModelList;
        this.subdepartment = (Fragment_Subdepartment) fragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subdepartment_item_row,parent,false);
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
            holder.tv_counter.setText(String.valueOf(amount));
            holder.decrease_btn.setVisibility(View.VISIBLE);
            holder.tv_addto_trolley.setVisibility(View.VISIBLE);
            itemsModel.setProduct_amount(String.valueOf(amount));
            holder.StartAnimation(holder.tv_price);
            holder.tv_price.setText(String.valueOf(amount*Integer.parseInt(itemsModel.getItem_one_cost())+" ريال"));

        });

        holder.decrease_btn.setOnClickListener(view -> {
            int amount = Integer.parseInt(holder.tv_counter.getText().toString())-1;

            if (amount==0)
            {
                holder.tv_counter.setText("0");
                holder.decrease_btn.setVisibility(View.INVISIBLE);
                holder.tv_addto_trolley.setVisibility(View.INVISIBLE);
                itemsModel.setProduct_amount("0");
                holder.StartAnimation(holder.tv_price);
                holder.tv_price.setText(itemsModel.getItem_one_cost()+" ريال");
                holder.img_trolley.setVisibility(View.GONE);
                subdepartment.Remove_From_Trolley(itemsModel);



            }else
                {
                    holder.tv_counter.setText(String.valueOf(amount));
                    holder.decrease_btn.setVisibility(View.VISIBLE);
                    holder.tv_addto_trolley.setVisibility(View.VISIBLE);
                    itemsModel.setProduct_amount(String.valueOf(amount));
                    holder.StartAnimation(holder.tv_price);

                    holder.tv_price.setText(String.valueOf(amount*Integer.parseInt(itemsModel.getItem_one_cost())+" ريال"));


                }
        });

        holder.tv_addto_trolley.setOnClickListener(view -> {
            subdepartment.SaveItem_To_Trolley(itemsModel);
            holder.img_trolley.setVisibility(View.VISIBLE);

        });


    }

    @Override
    public int getItemCount() {
        return itemsModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private RoundedImageView image;
        private TextView tv_name,tv_price,tv_counter,tv_addto_trolley;
        private ImageView increase_btn,decrease_btn,img_trolley;
        public MyHolder(View itemView) {
            super(itemView);
            image   = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_counter = itemView.findViewById(R.id.tv_counter);
            tv_addto_trolley = itemView.findViewById(R.id.tv_addto_trolley);
            increase_btn = itemView.findViewById(R.id.increase_btn);
            decrease_btn = itemView.findViewById(R.id.decrease_btn);
            img_trolley = itemView.findViewById(R.id.img_trolley);


        }

        public void BindData(ItemsModel itemsModel)
        {
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+itemsModel.getProduct_image())).into(image);
            tv_name.setText(itemsModel.getProduct_name());
            tv_price.setText(itemsModel.getProduct_cost()+" ريال");

        }
        public void StartAnimation(View view)
        {
            Animation animation = AnimationUtils.loadAnimation(context,R.anim.bounce);
            view.clearAnimation();
            view.startAnimation(animation);
        }

    }

}
