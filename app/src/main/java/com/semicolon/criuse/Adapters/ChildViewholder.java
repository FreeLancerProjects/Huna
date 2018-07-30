package com.semicolon.criuse.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolon.criuse.Models.SubProduct;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;
import com.squareup.picasso.Picasso;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ChildViewholder extends ChildViewHolder {
    private TextView tv_child_name;
    private ImageView image;
    public CheckBox checkBox;
    public ChildViewholder(View itemView) {
        super(itemView);

        tv_child_name = itemView.findViewById(R.id.tv_child_name);
        image = itemView.findViewById(R.id.image);
        checkBox = itemView.findViewById(R.id.checkbox);

    }

    public void BindData(Context context,SubProduct subProduct)
    {
        tv_child_name.setText(subProduct.getProduct_title());
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+subProduct.getProduct_image())).into(image);
    }
}
