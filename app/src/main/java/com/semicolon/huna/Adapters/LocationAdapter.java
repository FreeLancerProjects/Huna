package com.semicolon.huna.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.huna.Fragments.Fragment_Location;
import com.semicolon.huna.Models.CountryModel;
import com.semicolon.huna.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class LocationAdapter extends ExpandableRecyclerViewAdapter<LocationParentViewHolder,LocationChildViewHolder> {
    private Context context;
    private Fragment_Location fragment_location;
    public LocationAdapter(List<LocationGroup> groups, Context context, Fragment fragment) {
        super(groups);
        this.context = context;
        fragment_location = (Fragment_Location) fragment;
    }

    @Override
    public LocationParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_parent_row,parent,false);
        return new LocationParentViewHolder(view);
    }

    @Override
    public LocationChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_child_row,parent,false);
        return new LocationChildViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(LocationChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        CountryModel.Neighborhood neighborhood = (CountryModel.Neighborhood) group.getItems().get(childIndex);
        holder.BindData(neighborhood);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment_location.setCity_id(neighborhood.getFrom_id(),neighborhood.getId_area(),neighborhood.getArea_title());

            }
        });

        holder.rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.rb.isChecked())
                {
                    fragment_location.setCity_id(neighborhood.getFrom_id(),neighborhood.getId_area(),neighborhood.getArea_title());

                }

            }
        });

    }

    @Override
    public void onBindGroupViewHolder(LocationParentViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.BindData(group.getTitle());


    }
}
