package com.semicolon.huna.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.semicolon.huna.Models.GroupModel;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Tags;
import com.squareup.picasso.Picasso;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class GroupViewholder extends GroupViewHolder {
    private TextView tv_group_name;
    private RoundedImageView image;
    private ImageView icon;
    public GroupViewholder(View itemView) {
        super(itemView);
        tv_group_name = itemView.findViewById(R.id.tv_group_name);
        image = itemView.findViewById(R.id.image);
        icon = itemView.findViewById(R.id.icon);

    }

    public void bindData(Context context, ExpandableGroup group, String name)
    {
        if (group instanceof GroupModel)
        {
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+((GroupModel)group).getImage_url())).into(image);
            tv_group_name.setText(name);

        }
    }

    @Override
    public void expand() {
        super.expand();
        icon.setImageResource(R.drawable.white_up_icon);
    }

    @Override
    public void collapse() {
        super.collapse();
        icon.setImageResource(R.drawable.white_down_icon);

    }
}
