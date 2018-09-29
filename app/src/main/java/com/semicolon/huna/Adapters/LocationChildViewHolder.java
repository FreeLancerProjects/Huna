package com.semicolon.huna.Adapters;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.semicolon.huna.Models.CountryModel;
import com.semicolon.huna.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class LocationChildViewHolder extends ChildViewHolder {

    public RadioButton rb;
    public TextView tv_title;
    public LocationChildViewHolder(View itemView) {
        super(itemView);

        rb = itemView.findViewById(R.id.rb);
        tv_title = itemView.findViewById(R.id.tv_title);
    }

    public void BindData(CountryModel.Neighborhood neighborhood)
    {
        tv_title.setText(neighborhood.getArea_title());
    }
}
