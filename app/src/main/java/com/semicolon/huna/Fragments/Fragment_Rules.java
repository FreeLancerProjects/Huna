package com.semicolon.huna.Fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.huna.Activities.HomeActivity;
import com.semicolon.huna.Models.RuleModel;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Rules extends Fragment {
    private ProgressBar progBar;
    private TextView tv_title,tv_content;
    private ImageView back;
    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rules,container,false);
        initView(view);
        return  view;
    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(view.getContext(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        tv_title = view.findViewById(R.id.tv_title);
        tv_content = view.findViewById(R.id.tv_content);
        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.setNavPosition();
            }
        });
        homeActivity.HideFab();
        getData();
    }
    private void UpdateUi(RuleModel ruleModel) {
        tv_title.setText(ruleModel.getTitle());
        tv_content.setText(ruleModel.getContent());
    }
    private void getData() {
        Api.getServices()
                .getRuleData().enqueue(new Callback<RuleModel>() {
            @Override
            public void onResponse(Call<RuleModel> call, Response<RuleModel> response) {
                if (response.isSuccessful())
                {
                    progBar.setVisibility(View.GONE);
                    UpdateUi(response.body());
                }
            }

            @Override
            public void onFailure(Call<RuleModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                progBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.something_error, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public static Fragment_Rules getInstance()
    {
        Fragment_Rules fragment = new Fragment_Rules();
        return fragment;
    }
}
