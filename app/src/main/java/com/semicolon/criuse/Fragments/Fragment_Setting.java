package com.semicolon.criuse.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.InternetConnection.Connection;
import com.semicolon.criuse.Models.UserModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;
import com.semicolon.criuse.SharedPreferences.Preferences;
import com.semicolon.criuse.SingleTones.UserSingletone;
import com.squareup.picasso.Picasso;

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
    private CardView soundCardView;
    private Preferences preferences;
    private String session="";
    private UserSingletone userSingletone;
    private UserModel userModel;
    private Connection connection;

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
    private void initView(View view)
    {
        connection = Connection.getInstance();
        userSingletone = UserSingletone.getInstance();
        preferences = Preferences.getInstance();
        session = preferences.getSession(getActivity());



        homeActivity = (HomeActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            user_type = bundle.getString(TAG);
        }
        soundCardView = view.findViewById(R.id.soundCardView);
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


        Log.e("Session",session+"s");
        if (session!=null&& !TextUtils.isEmpty(session))
        {
            if (session.equals(Tags.session_logout))
            {

                tv_name.setVisibility(View.INVISIBLE);
                tv_state.setVisibility(View.INVISIBLE);
                img_state.setVisibility(View.INVISIBLE);
                soundCardView.setVisibility(View.GONE);
            }else if (session.equals(Tags.session_login))
            {
                userModel = userSingletone.getUserModel();
                tv_name.setVisibility(View.VISIBLE);
                img_state.setVisibility(View.VISIBLE);
                soundCardView.setVisibility(View.VISIBLE);
                tv_name.setText(userModel.getUser_full_name());
                Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser_photo())).into(image);
                if (connection.getConnction(getActivity()))
                {
                    tv_state.setVisibility(View.VISIBLE);
                    img_state.setImageResource(R.color.state_on);
                }else
                    {
                        img_state.setImageResource(R.color.state_off);

                        tv_state.setVisibility(View.INVISIBLE);

                    }




            }
        }else
            {
                tv_name.setVisibility(View.INVISIBLE);
                tv_state.setVisibility(View.INVISIBLE);
                img_state.setVisibility(View.INVISIBLE);
                soundCardView.setVisibility(View.GONE);
            }

    }

    ////from home activity
    public void UpdateUi(UserModel userModel)
    {
        userSingletone.setUserModel(userModel);
        tv_name.setVisibility(View.VISIBLE);
        tv_state.setVisibility(View.VISIBLE);
        img_state.setVisibility(View.VISIBLE);
        soundCardView.setVisibility(View.VISIBLE);
        tv_name.setText(userModel.getUser_full_name());
        Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser_photo())).into(image);
        homeActivity.HideFab();

    }
}
