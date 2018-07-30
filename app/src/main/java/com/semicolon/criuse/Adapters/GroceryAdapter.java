package com.semicolon.criuse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.criuse.Models.SubProduct;
import com.semicolon.criuse.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class GroceryAdapter extends ExpandableRecyclerViewAdapter<GroupViewholder,ChildViewholder> {
    private Context context;
    public GroceryAdapter(List groups,Context context) {
        super(groups);
        this.context=context;


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

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onBindGroupViewHolder(GroupViewholder holder, int flatPosition, ExpandableGroup group) {

        holder.bindData(context,group,group.getTitle());

    }
}
