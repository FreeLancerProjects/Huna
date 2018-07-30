package com.semicolon.criuse.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.Models.UserModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Api;
import com.semicolon.criuse.Services.Tags;
import com.semicolon.criuse.Share.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Login extends Fragment {
    private ImageView img_back;
    private HomeActivity homeActivity;
    private EditText edt_user_name,edt_password;
    private Button btn_login;
    private String m_username="",m_password="";
    private AlertDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        img_back = view.findViewById(R.id.img_back);
        edt_user_name = view.findViewById(R.id.edt_user_name);
        edt_password = view.findViewById(R.id.edt_password);
        btn_login = view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(view1 -> Login());
        img_back.setOnClickListener(view1 -> homeActivity.DisplayRegister_Layout());
    }

    private void Login() {
        m_username = edt_user_name.getText().toString();
        m_password = edt_password.getText().toString();

        if (TextUtils.isEmpty(m_username))
        {
            edt_user_name.setError(getActivity().getString(R.string.username_req));
        }else if (TextUtils.isEmpty(m_password))
        {
            edt_user_name.setError(null);
            edt_password.setError(getActivity().getString(R.string.password_req));
        }else
            {
                edt_user_name.setError(null);
                edt_password.setError(null);
                progressDialog = Common.CreateProgressDialog(getActivity(),getString(R.string.ln));
                progressDialog.show();
                Api.getServices().Login(m_username,m_password)
                        .enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                if (response.isSuccessful())
                                {
                                    progressDialog.dismiss();
                                    if (response.body().getSuccess()==1)
                                    {
                                        homeActivity.UpdateData(response.body());
                                        homeActivity.UpdateSettingFragmentUi(response.body());
                                        Log.e("data", Tags.IMAGE_URL+response.body().getUser_photo());
                                    }else if (response.body().getSuccess()==0)
                                    {
                                        Toast.makeText(homeActivity, R.string.check_un_pass, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<UserModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(homeActivity, "Error : something went haywire", Toast.LENGTH_SHORT).show();
                                Log.e("Error",t.getMessage());
                            }
                        });
            }
    }

    public static Fragment_Login getInstance()
    {
        Fragment_Login fragment = new Fragment_Login();
        return fragment;
    }
}
