package com.semicolon.criuse.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.criuse.Fragments.Fragment_Categories;
import com.semicolon.criuse.Models.SubProduct;
import com.semicolon.criuse.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class GroceryAdapter extends ExpandableRecyclerViewAdapter<GroupViewholder,ChildViewholder> {
    private Context context;
    private Fragment_Categories fragment_categories;
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
    public GroceryAdapter(List groups, Context context, Fragment fragment) {
        super(groups);
        this.context=context;
        this.fragment_categories = (Fragment_Categories) fragment;


    }

    @Override
    public GroupViewholder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_category_item_row,parent,false);
        return new GroupViewholder(view);
    }

    @Override
    public ChildViewholder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.child_subcategory_row,parent,false);
        return new ChildViewholder(view);
    }

    @Override
    public void onBindChildViewHolder(ChildViewholder holder, final int flatPosition, ExpandableGroup group, final int childIndex) {
        SubProduct subProduct = (SubProduct) group.getItems().get(childIndex);
        holder.BindData(context,subProduct);

        if (sparseBooleanArray.size()>0)
        {
            if (sparseBooleanArray.get(holder.getAdapterPosition()))
            {
                holder.checkBox.setChecked(true);
            }else
                {
                    holder.checkBox.setChecked(false);
                }
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked())
                {
                    sparseBooleanArray.put(holder.getAdapterPosition(),true);
                }else
                    {
                        sparseBooleanArray.delete(holder.getAdapterPosition());
                    }
                fragment_categories.add_remove_subcategory(holder.checkBox,subProduct.getId_product());
            }
        });
    }

    @Override
    public void onBindGroupViewHolder(GroupViewholder holder, int flatPosition, ExpandableGroup group) {

        holder.bindData(context,group,group.getTitle());

    }
}
