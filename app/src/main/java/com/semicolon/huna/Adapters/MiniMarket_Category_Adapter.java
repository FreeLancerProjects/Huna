package com.semicolon.huna.Adapters;

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
import com.semicolon.huna.Fragments.Fragment_MiniMarket_Category;
import com.semicolon.huna.Models.MiniMarket_SubCategory;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MiniMarket_Category_Adapter extends RecyclerView.Adapter<MiniMarket_Category_Adapter.MyHolder>{
    private Context context;
    private List<MiniMarket_SubCategory> miniMarket_subCategoryList;
    private Fragment_MiniMarket_Category fragment_miniMarket_category;
    public MiniMarket_Category_Adapter(Context context, List<MiniMarket_SubCategory> miniMarket_subCategoryList, Fragment fragment) {
        this.context = context;
        this.miniMarket_subCategoryList = miniMarket_subCategoryList;
        this.fragment_miniMarket_category = (Fragment_MiniMarket_Category) fragment;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.deparment_item_row,parent,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MiniMarket_SubCategory miniMarket_subCategory= miniMarket_subCategoryList.get(position);
        holder.BindData(miniMarket_subCategory);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_miniMarket_category.setPos(position,miniMarket_subCategory.getName_categories(),miniMarket_subCategory.getId_categories());
            }
        });
    }

    @Override
    public int getItemCount() {
        return miniMarket_subCategoryList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        private RoundedImageView dept_item_image;
        private TextView tv_dept_item_name;
        public MyHolder(View itemView) {
            super(itemView);
            dept_item_image   = itemView.findViewById(R.id.dept_item_image);
            tv_dept_item_name = itemView.findViewById(R.id.tv_dept_item_name);
        }

        public void BindData(MiniMarket_SubCategory miniMarket_subCategory)
        {
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+miniMarket_subCategory.getImage_categories())).into(dept_item_image);
            tv_dept_item_name.setText(miniMarket_subCategory.getName_categories());
        }
    }

}
