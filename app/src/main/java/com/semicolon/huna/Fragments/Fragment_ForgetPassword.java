package com.semicolon.huna.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.semicolon.huna.Activities.HomeActivity;
import com.semicolon.huna.Models.ResponseModel;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Api;
import com.semicolon.huna.Share.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_ForgetPassword extends Fragment {
    private EditText edt_user_name,edt_email;
    private Button btn_reset;
    private ProgressDialog progressDialog;
    private HomeActivity homeActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgetpassword,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        edt_user_name = view.findViewById(R.id.edt_user_name);
        edt_email = view.findViewById(R.id.edt_email);
        btn_reset = view.findViewById(R.id.btn_reset);

        btn_reset.setOnClickListener(view1 -> Reset());
    }

    public static Fragment_ForgetPassword getInstance()
    {
        return new Fragment_ForgetPassword();
    }
    private void Reset() {

        String m_username = edt_user_name.getText().toString();
        String m_email = edt_email.getText().toString();

        if (TextUtils.isEmpty(m_username))
        {
            edt_user_name.setError(getString(R.string.username_req));
        }else if (TextUtils.isEmpty(m_email))
        {
            edt_user_name.setError(null);
            edt_email.setError(getString(R.string.email_req));
        }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
        {
            edt_user_name.setError(null);
            edt_email.setError(getString(R.string.inv_email));
        }else
            {
                progressDialog = Common.CreateProgressDialog(getActivity(),getString(R.string.restoring_pass));

                edt_user_name.setError(null);
                edt_email.setError(null);


                Api.getServices()
                        .resetPassword(m_username,m_email)
                        .enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                if (response.isSuccessful())
                                {
                                    if (response.body().getSuccess()==1)
                                    {
                                        progressDialog.dismiss();
                                        homeActivity.navigateToSettingFragment();
                                        Toast.makeText(homeActivity, R.string.msg_will_send_torecover_pass, Toast.LENGTH_LONG).show();

                                    }else
                                        {
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(),R.string.failed, Toast.LENGTH_SHORT).show();
                                        }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                Log.e("Error",t.getMessage());
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_LONG).show();
                            }
                        });

            }
    }
}
