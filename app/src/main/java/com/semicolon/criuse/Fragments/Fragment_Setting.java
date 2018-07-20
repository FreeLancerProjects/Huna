package com.semicolon.criuse.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;

import de.hdodenhof.circleimageview.CircleImageView;
import it.beppi.tristatetogglebutton_library.TriStateToggleButton;

public class Fragment_Setting extends Fragment{
    private static String TAG="TAG";
    private String user_type="";
    private CardView cardView_user_data;
    private CircleImageView image,img_state;
    private TextView tv_name,tv_state;
    private LinearLayout ll_client_account,ll_grocery_account,ll_driver_account;
    private TriStateToggleButton toggle_sound,toggle_vibrate;
    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting,container,false);
        initView(view);

        return view;
    }

    public static Fragment_Setting getInstance(String user_type)
    {
        Fragment_Setting fragment_setting = new Fragment_Setting();
        Bundle bundle = new Bundle();
        bundle.putString(TAG,user_type);
        fragment_setting.setArguments(bundle);
        return  fragment_setting;
    }
    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            user_type = bundle.getString(TAG);
        }
        cardView_user_data = view.findViewById(R.id.cardView);
        image = view.findViewById(R.id.image);
        img_state = view.findViewById(R.id.img_state);
        tv_name = view.findViewById(R.id.tv_name);
        tv_state = view.findViewById(R.id.tv_state);
        ll_client_account = view.findViewById(R.id.ll_client_account);
        ll_grocery_account = view.findViewById(R.id.ll_grocery_account);
        ll_driver_account = view.findViewById(R.id.ll_driver_account);
        toggle_sound = view.findViewById(R.id.toggle_sound);
        toggle_vibrate = view.findViewById(R.id.toggle_vibrate);


        ll_client_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.RegisterType(Tags.client_register);

            }
        });

        ll_grocery_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.RegisterType(Tags.grocery_register);


            }
        });

        ll_driver_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.RegisterType(Tags.driver_register);


            }
        });



    }
}
