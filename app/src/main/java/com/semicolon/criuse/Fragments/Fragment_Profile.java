package com.semicolon.criuse.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.Models.UserModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Api;
import com.semicolon.criuse.Services.Tags;
import com.semicolon.criuse.Share.Common;
import com.semicolon.criuse.SingleTones.UserSingletone;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Profile extends Fragment implements View.OnClickListener{
    private static String TAG="TAG";
    private String user_type="";
    private HomeActivity homeActivity;
    private KenBurnsView image_profile_bg;
    private ImageView image_profile,image_update,image_edit_phone,image_edit_username,image_edit_password,image_edit_city,image_edit_neighbourhood,image_license_update,image_residence_update,image_car_form_update;
    private TextView tv_name,tv_edit_name,tv_phone,tv_user_name,tv_city,tv_neighbourhood;
    private RoundedImageView image_license,image_residence,image_car_form;
    private LinearLayout ll_driver_data_container;
    private UserModel userModel;
    private UserSingletone userSingletone;
    private AlertDialog updateDialog;
    private Uri path_Imageprofile=null,path_Image_licence=null,path_Imageresidence=null,path_ImagecarForm=null;
    private final int IMG_REQ_PROFILE=1;
    private final int IMG_REQ_LICENSE=2;
    private final int IMG_REQ_RESIDENCE=3;
    private final int IMG_REQ_CARFORM=4;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initView(view);
        return view;
    }


    public static Fragment_Profile getInstance(String user_type)
    {

        Fragment_Profile fragment_setting = new Fragment_Profile();
        Bundle bundle = new Bundle();
        bundle.putString(TAG,user_type);
        fragment_setting.setArguments(bundle);
        return  fragment_setting;
    }
    private void initView(View view) {
        userSingletone = UserSingletone.getInstance();
        userModel = userSingletone.getUserModel();
        homeActivity = (HomeActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            user_type = bundle.getString(TAG);
        }

        image_profile_bg = view.findViewById(R.id.image_bg);
        image_profile = view.findViewById(R.id.image_profile);
        image_update = view.findViewById(R.id.image_update);
        image_edit_phone = view.findViewById(R.id.image_edit_phone);
        image_edit_username = view.findViewById(R.id.image_edit_username);
        image_edit_password = view.findViewById(R.id.image_edit_password);
        image_edit_city = view.findViewById(R.id.image_edit_city);
        image_edit_neighbourhood = view.findViewById(R.id.image_edit_neighbourhood);
        image_license_update = view.findViewById(R.id.image_license_update);
        image_residence_update = view.findViewById(R.id.image_residence_update);
        image_car_form_update = view.findViewById(R.id.image_car_form_update);
        tv_name = view.findViewById(R.id.tv_name);
        tv_edit_name = view.findViewById(R.id.tv_edit_name);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_user_name = view.findViewById(R.id.tv_user_name);
        tv_city = view.findViewById(R.id.tv_city);
        tv_neighbourhood = view.findViewById(R.id.tv_neighbourhood);
        image_license = view.findViewById(R.id.image_license);
        image_residence = view.findViewById(R.id.image_residence);
        image_car_form = view.findViewById(R.id.image_car_form);
        ll_driver_data_container = view.findViewById(R.id.ll_driver_data_container);

        image_update.setOnClickListener(this);
        image_edit_phone.setOnClickListener(this);
        image_edit_username.setOnClickListener(this);
        image_edit_password.setOnClickListener(this);
        image_edit_neighbourhood.setOnClickListener(this);
        image_license_update.setOnClickListener(this);
        image_residence_update.setOnClickListener(this);
        image_edit_city.setOnClickListener(this);
        image_car_form_update.setOnClickListener(this);
        tv_edit_name.setOnClickListener(this);

        UpdateUi(userModel);

    }

    private void UpdateUi(UserModel userModel) {
        Log.e("dsf","dsfdsfsggggggggggggg");
        if (userModel!=null)
        {
            Log.e("photo",userModel.getUser_photo()+"ggggggggggggggggggggggggg");

            Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser_photo())).placeholder(R.color.colorPrimary).into(image_profile_bg);
            Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser_photo())).placeholder(R.drawable.avatar_profile).into(image_profile);


            tv_name.setText(userModel.getUser_full_name());
            tv_phone.setText(userModel.getUser_phone());
            tv_user_name.setText(userModel.getUser_name());

            if (userModel.getUser_type().equals(Tags.user_type_driver))
            {
                ll_driver_data_container.setVisibility(View.VISIBLE);
                tv_city.setText(userModel.getUser_city());
                tv_neighbourhood.setText(userModel.getUser_neighborhood());
                Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser_license_photo())).placeholder(R.color.colorPrimary).into(image_license);
                Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser_residence_photo())).placeholder(R.color.colorPrimary).into(image_residence);
                Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser_car_photo())).placeholder(R.color.colorPrimary).into(image_car_form);

            }else if (userModel.getUser_type().equals(Tags.user_type_client))
                {
                    ll_driver_data_container.setVisibility(View.GONE);

                }

        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {

            case R.id.image_update:
                SelectImage(IMG_REQ_PROFILE);
                break;
            case R.id.tv_edit_name:
                CreateUpdateDialog(Tags.update_name);
                break;
            case R.id.image_edit_phone:
                CreateUpdateDialog(Tags.update_phone);

                break;
            case R.id.image_edit_username:
                CreateUpdateDialog(Tags.update_username);

                break;
            case R.id.image_edit_password:

                CreateUpdateDialog(Tags.update_password);

                break;
            case R.id.image_edit_city:
                CreateUpdateDialog(Tags.update_city);

                break;
            case R.id.image_edit_neighbourhood:
                CreateUpdateDialog(Tags.update_neighbourhood);

                break;
            case R.id.image_license_update:

                SelectImage(IMG_REQ_LICENSE);

                break;
            case R.id.image_residence_update:

                SelectImage(IMG_REQ_RESIDENCE);

                break;
            case R.id.image_car_form_update:

                SelectImage(IMG_REQ_CARFORM);

                break;


        }
    }

    private void SelectImage(int img_req) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        getActivity().startActivityForResult(intent.createChooser(intent,"Image"),img_req);
    }

    private void CreateUpdateDialog(String type)
    {
        View updateView = LayoutInflater.from(getActivity()).inflate(R.layout.custom_update_dialog,null);
        TextView title = updateView.findViewById(R.id.tv_title);
        EditText edt_update = updateView.findViewById(R.id.edt_update);
        EditText newPassword = updateView.findViewById(R.id.edt_newPassword);
        PhoneInputLayout edt_phone = updateView.findViewById(R.id.edt_phone);
        Button btn_cancel = updateView.findViewById(R.id.btn_close);
        Button btn_update =updateView.findViewById(R.id.btn_update);
        switch (type)
        {
            case Tags.update_username:
                title.setText(R.string.upd_username);
                edt_update.setHint(R.string.user_name);
                edt_phone.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                edt_update.setVisibility(View.VISIBLE);
                edt_update.setInputType(InputType.TYPE_CLASS_TEXT);


                break;

            case Tags.update_name:
                title.setText(R.string.upd_name);
                edt_update.setHint(R.string.name);
                edt_phone.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                edt_update.setVisibility(View.VISIBLE);
                edt_update.setInputType(InputType.TYPE_CLASS_TEXT);


                break;
            case Tags.update_phone:
                title.setText(R.string.upd_phone);
                edt_phone.getTextInputLayout().getEditText().setHint(R.string.phone);
                edt_phone.getTextInputLayout().getEditText().setTextSize(TypedValue.COMPLEX_UNIT_SP,13f);
                edt_phone.setDefaultCountry("sa");
                edt_phone.setVisibility(View.VISIBLE);
                newPassword.setVisibility(View.GONE);
                edt_update.setVisibility(View.GONE);

                break;
            case Tags.update_password:
                title.setText(R.string.upd_password);
                edt_update.setHint(R.string.old_password);
                edt_update.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                newPassword.setHint(R.string.newpass_txt);
                edt_update.setVisibility(View.VISIBLE);
                edt_phone.setVisibility(View.GONE);
                newPassword.setVisibility(View.VISIBLE);

                break;
            case Tags.update_neighbourhood:
                title.setText(R.string.upd_address);
                edt_update.setHint(R.string.address);
                edt_phone.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                edt_update.setVisibility(View.VISIBLE);
                edt_update.setInputType(InputType.TYPE_CLASS_TEXT);


                break;
            case Tags.update_city:
                title.setText(R.string.update_city);
                edt_update.setHint(R.string.city);
                edt_phone.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                edt_update.setVisibility(View.VISIBLE);
                edt_update.setInputType(InputType.TYPE_CLASS_TEXT);


                break;


        }

        updateDialog = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setView(updateView)
                .create();
        updateDialog.setCanceledOnTouchOutside(false);
        updateDialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDialog.dismiss();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDialog.dismiss();
                switch (type)
                {
                    case Tags.update_name:
                        String name = edt_update.getText().toString();
                        if (TextUtils.isEmpty(name))
                        {
                            edt_update.setError(getString(R.string.name_req));
                        }else
                            {
                                edt_update.setError(null);
                                Update_Name(name);

                            }
                        break;
                    case Tags.update_phone:
                        String phone = edt_phone.getPhoneNumber();
                        if (TextUtils.isEmpty(phone))
                        {
                            edt_phone.getTextInputLayout().getEditText().setError(getString(R.string.phone_req));
                        }else if (!edt_phone.isValid())
                        {
                            edt_phone.getTextInputLayout().getEditText().setError(getString(R.string.inv_phone));

                        }else
                            {
                                edt_phone.getTextInputLayout().getEditText().setError(null);

                                Update_Phone(phone);
                            }
                        break;
                    case Tags.update_username:
                        String username = edt_update.getText().toString();
                        if (TextUtils.isEmpty(username))
                        {
                            edt_update.setError(getString(R.string.username_req));
                        }else
                        {
                            edt_update.setError(null);
                            Update_userName(username);

                        }
                        break;
                    case Tags.update_password:
                        String old_password = edt_update.getText().toString();
                        String newPass  = newPassword.getText().toString();
                        if (TextUtils.isEmpty(old_password))
                        {
                            edt_update.setError(getString(R.string.password_req));
                        }else if (TextUtils.isEmpty(newPass))
                        {
                            edt_update.setError(null);

                            newPassword.setError(getString(R.string.password_req));

                        }else
                            {
                                Update_Password(old_password,newPass);
                            }

                        break;
                    case Tags.update_neighbourhood:
                        String neighbourhood = edt_update.getText().toString();
                        if (TextUtils.isEmpty(neighbourhood))
                        {
                            edt_update.setError(getString(R.string.neighborhood_req));
                        }else
                        {
                            edt_update.setError(null);
                            Update_Neighbourhood(neighbourhood);

                        }
                        break;
                    case Tags.update_city:
                        String city = edt_update.getText().toString();
                        if (TextUtils.isEmpty(city))
                        {
                            edt_update.setError(getString(R.string.city_req));
                        }else
                        {
                            edt_update.setError(null);
                            Update_City(city);

                        }
                        break;


                }
            }
        });
    }

    private void Update_ImageProfile(File file)
    {
        Call<UserModel> call;

        String type = userModel.getUser_type();
        ProgressDialog progressDialog = Common.CreateProgressDialog2(getActivity(),getString(R.string.upd_img_prof));


        RequestBody img_request = RequestBody.create(MediaType.parse("image/*"),file);
        RequestBody name_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_full_name());
        RequestBody phone_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_phone());
        RequestBody email_request = RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody username_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_name());
        MultipartBody.Part part = MultipartBody.Part.createFormData("user_photo",file.getName(),img_request);

        if (type.equals(Tags.user_type_driver))
        {
            RequestBody city_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_city());
            RequestBody neighbourhood_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_neighborhood());

            call = Api.getServices().UpdateDriverImage(userModel.getUser_id(), username_request, phone_request, name_request, email_request, neighbourhood_request, city_request,part);
        }else
        {
            call = Api.getServices().UpdateClientImage(userModel.getUser_id(),username_request,phone_request,name_request,email_request,part);
        }
        progressDialog.show();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful())
                {
                    userModel = response.body();
                    homeActivity.UpdateData(userModel);
                    progressDialog.dismiss();
                    UpdateUi(userModel);
                    Toast.makeText(homeActivity, R.string.suc_upd, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void Update_Name(String name)
    {
        Call<UserModel> call =null;
        String type = userModel.getUser_type();
        ProgressDialog progressDialog = Common.CreateProgressDialog2(getActivity(),getString(R.string.updating_name));

        RequestBody name_request = RequestBody.create(MediaType.parse("text/plain"),name);
        RequestBody phone_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_phone());
        RequestBody email_request = RequestBody.create(MediaType.parse("text/plain"),"");

        RequestBody username_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_name());

        if (type.equals(Tags.user_type_driver))
        {
            RequestBody city_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_city());
            RequestBody neighbourhood_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_neighborhood());
            call = Api.getServices().UpdateDriverData(userModel.getUser_id(), username_request, phone_request, name_request, email_request, neighbourhood_request, city_request);
        }else if (type.equals(Tags.user_type_client))
            {
                call = Api.getServices().UpdateClientData(userModel.getUser_id(),username_request,phone_request,name_request,email_request);
            }

            progressDialog.show();
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful())
                    {
                        userModel = response.body();
                        homeActivity.UpdateData(userModel);
                        UpdateUi(userModel);
                        progressDialog.dismiss();
                        Toast.makeText(homeActivity, R.string.suc_upd, Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Log.e("Error",t.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();

                }
            });


    }
    private void Update_Phone(String phone)
    {
        Call<UserModel> call =null;

        String type = userModel.getUser_type();

        ProgressDialog progressDialog = Common.CreateProgressDialog2(getActivity(),getString(R.string.updating_phone));

        RequestBody name_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_full_name());
        RequestBody phone_request = RequestBody.create(MediaType.parse("text/plain"),phone);
        RequestBody username_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_name());
        RequestBody email_request = RequestBody.create(MediaType.parse("text/plain"),"");

        if (type.equals(Tags.user_type_driver))
        {
            RequestBody city_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_city());
            RequestBody neighbourhood_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_neighborhood());
            call = Api.getServices().UpdateDriverData(userModel.getUser_id(),username_request,phone_request,name_request,email_request,neighbourhood_request,city_request);
        }else if (type.equals(Tags.user_type_client))
        {
           call = Api.getServices().UpdateClientData(userModel.getUser_id(),username_request,phone_request,name_request,email_request);
        }

        progressDialog.show();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful())
                {
                    userModel = response.body();
                    homeActivity.UpdateData(userModel);
                    UpdateUi(userModel);
                    progressDialog.dismiss();
                    Toast.makeText(homeActivity, R.string.suc_upd, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void Update_userName(String user_name)
    {
        Call<UserModel> call =null;

        String type = userModel.getUser_type();

        ProgressDialog progressDialog = Common.CreateProgressDialog2(getActivity(),getString(R.string.updating_username));

        RequestBody name_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_full_name());
        RequestBody phone_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_phone());
        RequestBody username_request = RequestBody.create(MediaType.parse("text/plain"),user_name);
        RequestBody email_request = RequestBody.create(MediaType.parse("text/plain"),"");

        if (type.equals(Tags.user_type_driver))
        {
            RequestBody city_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_city());
            RequestBody neighbourhood_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_neighborhood());
            call = Api.getServices().UpdateDriverData(userModel.getUser_id(),username_request,phone_request,name_request,email_request,neighbourhood_request,city_request);
        }else if (type.equals(Tags.user_type_client))
        {
            call = Api.getServices().UpdateClientData(userModel.getUser_id(),username_request,phone_request,name_request,email_request);
        }

        progressDialog.show();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful())
                {
                    userModel = response.body();
                    homeActivity.UpdateData(userModel);
                    UpdateUi(userModel);
                    progressDialog.dismiss();
                    Toast.makeText(homeActivity, R.string.suc_upd, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void Update_Password(String old_password, String newpassword)
    {

        ProgressDialog progressDialog = Common.CreateProgressDialog2(getActivity(),getString(R.string.updating_pass));
        progressDialog.show();

        Api.getServices().updatePassword(userModel.getUser_id(),old_password,newpassword)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();

                            if (response.body().getSuccess()==1)
                            {
                                userModel = response.body();
                                homeActivity.UpdateData(userModel);
                                Toast.makeText(homeActivity, R.string.suc_upd, Toast.LENGTH_SHORT).show();

                            }else if (response.body().getSuccess()==0)
                            {
                                Toast.makeText(homeActivity, R.string.inc_old_pass, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();
                        Log.e("Error",t.getMessage());
                    }
                });

    }
    private void Update_City(String city)
    {
        Call<UserModel> call =null;

        ProgressDialog progressDialog = Common.CreateProgressDialog2(getActivity(),getString(R.string.updating_city));

        RequestBody name_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_full_name());
        RequestBody phone_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_phone());
        RequestBody username_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_name());
        RequestBody email_request = RequestBody.create(MediaType.parse("text/plain"),"");

        RequestBody city_request = RequestBody.create(MediaType.parse("text/plain"),city);
        RequestBody neighbourhood_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_neighborhood());
        call = Api.getServices().UpdateDriverData(userModel.getUser_id(),username_request,phone_request,name_request,email_request,neighbourhood_request,city_request);

        progressDialog.show();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful())
                {
                    userModel = response.body();

                    homeActivity.UpdateData(userModel);
                    UpdateUi(userModel);
                    progressDialog.dismiss();
                    Toast.makeText(homeActivity, R.string.suc_upd, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void Update_Neighbourhood(String neighbourhood)
    {
        Call<UserModel> call =null;

        ProgressDialog progressDialog = Common.CreateProgressDialog2(getActivity(),getString(R.string.updating_nei));

        RequestBody name_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_full_name());
        RequestBody phone_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_phone());
        RequestBody username_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_name());
        RequestBody email_request = RequestBody.create(MediaType.parse("text/plain"),"");

        RequestBody city_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_city());
        RequestBody neighbourhood_request = RequestBody.create(MediaType.parse("text/plain"),neighbourhood);
        call = Api.getServices().UpdateDriverData(userModel.getUser_id(),username_request,phone_request,name_request,email_request,neighbourhood_request,city_request);

        progressDialog.show();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful())
                {
                    userModel = response.body();
                    homeActivity.UpdateData(userModel);
                    UpdateUi(userModel);
                    progressDialog.dismiss();
                    Toast.makeText(homeActivity, R.string.suc_upd, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void Update_ImageLicense(File file)
    {
        Call<UserModel> call =null;

        ProgressDialog progressDialog = Common.CreateProgressDialog2(getActivity(),getString(R.string.upd_licence_photo));

        RequestBody img_request = RequestBody.create(MediaType.parse("image/*"),file);
        RequestBody name_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_full_name());
        RequestBody phone_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_phone());
        RequestBody username_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_name());
        RequestBody email_request = RequestBody.create(MediaType.parse("text/plain"),"");

        RequestBody city_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_city());
        RequestBody neighbourhood_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_neighborhood());
        MultipartBody.Part part = MultipartBody.Part.createFormData("user_license_photo",file.getName(),img_request);
        call = Api.getServices().UpdateDriverImage(userModel.getUser_id(),username_request,phone_request,name_request,email_request,neighbourhood_request,city_request,part);

        progressDialog.show();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful())
                {
                    userModel = response.body();
                    homeActivity.UpdateData(userModel);
                    UpdateUi(userModel);
                    progressDialog.dismiss();
                    Toast.makeText(homeActivity, R.string.suc_upd, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void Update_ImageResidence(File file)
    {
        Call<UserModel> call=null;

        ProgressDialog progressDialog = Common.CreateProgressDialog2(getActivity(),getString(R.string.upd_resd_photo));

        RequestBody img_request = RequestBody.create(MediaType.parse("image/*"),file);
        RequestBody name_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_full_name());
        RequestBody phone_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_phone());
        RequestBody username_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_name());
        RequestBody email_request = RequestBody.create(MediaType.parse("text/plain"),"");

        RequestBody city_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_city());
        RequestBody neighbourhood_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_neighborhood());
        MultipartBody.Part part = MultipartBody.Part.createFormData("user_residence_photo",file.getName(),img_request);
        call = Api.getServices().UpdateDriverImage(userModel.getUser_id(),username_request,phone_request,name_request,email_request,neighbourhood_request,city_request,part);

        progressDialog.show();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful())
                {
                    userModel = response.body();
                    homeActivity.UpdateData(userModel);
                    UpdateUi(userModel);
                    progressDialog.dismiss();
                    Toast.makeText(homeActivity, R.string.suc_upd, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void Update_ImageCarForm(File file)
    {
        Call<UserModel> call;

        ProgressDialog progressDialog = Common.CreateProgressDialog2(getActivity(),getString(R.string.upd_car_form));

        RequestBody img_request = RequestBody.create(MediaType.parse("image/*"),file);
        RequestBody name_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_full_name());
        RequestBody phone_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_phone());
        RequestBody username_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_name());
        RequestBody email_request = RequestBody.create(MediaType.parse("text/plain"),"");

        RequestBody city_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_city());
        RequestBody neighbourhood_request = RequestBody.create(MediaType.parse("text/plain"),userModel.getUser_neighborhood());

        MultipartBody.Part part = MultipartBody.Part.createFormData("user_car_photo",file.getName(),img_request);
        call = Api.getServices().UpdateDriverImage(userModel.getUser_id(),username_request,phone_request,name_request,email_request,neighbourhood_request,city_request,part);

        progressDialog.show();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful())
                {
                    userModel = response.body();
                    homeActivity.UpdateData(userModel);
                    UpdateUi(userModel);
                    progressDialog.dismiss();
                    Log.e("vvv",userModel.getUser_car_photo());
                    Toast.makeText(homeActivity, R.string.suc_upd, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(homeActivity, R.string.something_error, Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQ_PROFILE && resultCode == Activity.RESULT_OK && data!=null)
        {
            path_Imageprofile = data.getData();
            Update_ImageProfile(Common.getFileFromUrl(getActivity(),path_Imageprofile));
        }else if (requestCode==IMG_REQ_LICENSE && resultCode == Activity.RESULT_OK && data!=null)
        {
            path_Image_licence = data.getData();
            Update_ImageLicense(Common.getFileFromUrl(getActivity(),path_Image_licence));

        }
        else if (requestCode==IMG_REQ_RESIDENCE && resultCode == Activity.RESULT_OK && data!=null)
        {
            path_Imageresidence = data.getData();
            Update_ImageResidence(Common.getFileFromUrl(getActivity(),path_Imageresidence));

        }
        else if (requestCode==IMG_REQ_CARFORM && resultCode == Activity.RESULT_OK && data!=null)
        {
            path_ImagecarForm = data.getData();
            Update_ImageCarForm(Common.getFileFromUrl(getActivity(),path_ImagecarForm));

        }
    }
}
