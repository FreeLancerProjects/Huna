package com.semicolon.huna.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolon.huna.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class LocationParentViewHolder extends GroupViewHolder {
    private ImageView icon;
    public TextView tv_title;
    public LocationParentViewHolder(View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.image_icon);
        tv_title = itemView.findViewById(R.id.tv_title);
    }

    public void BindData(String title)
    {
        tv_title.setText(title);


    }

    @Override
    public void expand() {
        super.expand();
        icon.animate().rotation(180f).start();
    }

    @Override
    public void collapse() {
        super.collapse();
        icon.animate().rotation(0f).start();

    }
}
