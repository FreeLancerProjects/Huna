package com.semicolon.huna.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.huna.Activities.HomeActivity;
import com.semicolon.huna.Adapters.Country_Adapter;
import com.semicolon.huna.Adapters.Neighborhood_Adapter;
import com.semicolon.huna.Models.CountryModel;
import com.semicolon.huna.Models.UserModel;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Api;
import com.semicolon.huna.Services.Tags;
import com.semicolon.huna.Share.Common;
import com.semicolon.huna.SharedPreferences.Preferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Driver_Register extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private LinearLayout ll_login;
    private HomeActivity homeActivity;
    private CircleImageView image;
    private EditText edt_name,edt_phone,edt_userName,edt_password,edt_re_password;
    private TextView tv_city, tv_neighborhood;
    private PhoneInputLayout edt_check_phone;
    private Button btn_Register;
    private Bitmap bitmap1=null,bitmap2=null,bitmap3=null,bitmap4=null;
    private LinearLayout ll_upload_license_image,ll_upload_residence_image,ll_upload_car_image;
    private ImageView licence_image,residence_image,car_image;
    private String m_name="",m_phone="",m_userName="",m_password="",m_rePassword;
    private String imagePath_1="",imagePath_2="",imagePath_3="",imagePath_4="";
    private String user_send_photo="";

    private final int IMG_REQ_1=1;
    private final int IMG_REQ_2=2;
    private final int IMG_REQ_3=3;
    private final int IMG_REQ_4=4;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private double myLat = 0.0, myLng = 0.0;
    private AlertDialog progressDialog;
    private File userPhoto_File,user_license_photoFile,user_car_photoFile,user_residence_photoFile;
    private Preferences preferences;
    private String session="";
    private LocationManager manager;
    private android.support.v7.app.AlertDialog dialog;
    private final int gps_req=1558;
    private final int read_req=1557;
    private String read_per = Manifest.permission.READ_EXTERNAL_STORAGE;
    private ProgressDialog progDialogCountry;
    private AlertDialog alertDialog_country,alertDialog_neighborhood;
    private List<CountryModel> countryModelList;
    private String city_id="",area_id="";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_register,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        CreateAlertDialog();
        preferences = Preferences.getInstance();
        session=preferences.getSession(getActivity());
        homeActivity = (HomeActivity) getActivity();
        ll_login = view.findViewById(R.id.ll_login);
        image = view.findViewById(R.id.image);
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        tv_city = view.findViewById(R.id.tv_city);
        tv_neighborhood = view.findViewById(R.id.tv_neighborhood);
        edt_check_phone = view.findViewById(R.id.edt_check_phone);
        edt_userName = view.findViewById(R.id.edt_user_name);
        edt_password = view.findViewById(R.id.edt_password);
        edt_re_password = view.findViewById(R.id.edt_re_password);
        btn_Register = view.findViewById(R.id.reg_btn);
        ll_upload_license_image = view.findViewById(R.id.ll_upload_license_image);
        ll_upload_residence_image = view.findViewById(R.id.ll_upload_residence_image);
        ll_upload_car_image = view.findViewById(R.id.ll_upload_car_image);
        licence_image = view.findViewById(R.id.licence_image);
        residence_image = view.findViewById(R.id.residence_image);
        car_image = view.findViewById(R.id.car_form_image);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckReadPermission();
            }
        });

        ll_upload_license_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage(IMG_REQ_2);
            }
        });
        ll_upload_residence_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage(IMG_REQ_3);
            }
        });

        ll_upload_car_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage(IMG_REQ_4);
            }
        });

        ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayLoginLayout(Tags.driver_register);
            }
        });

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        tv_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateCountyAlertDialog(countryModelList);
            }
        });

        tv_neighborhood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog_neighborhood.show();
            }
        });

        Log.e("Session",session+"s");

        if (session!=null&&!TextUtils.isEmpty(session))
        {
            if (session.equals(Tags.session_logout))
            {
                ll_login.setVisibility(View.VISIBLE);
            }else if (session.equals(Tags.session_login))
            {
                ll_login.setVisibility(View.INVISIBLE);

            }
        }else if (session!=null&&TextUtils.isEmpty(session))
            {
                ll_login.setVisibility(View.VISIBLE);

            }

        if (IsGpsOpen())
        {
            initGoogleApiClient();

        }else
        {
            dialog.show();
        }

        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCountryData();

                    }
                },500);
    }

    private void CreateCountyAlertDialog(List<CountryModel> countryModelList) {
        if (countryModelList.size()>0)
        {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.country_neigbourhood_alert_layout,null);
            TextView tv_title = view.findViewById(R.id.tv_title);
            tv_title.setText(getString(R.string.cities));
            RecyclerView recView = view.findViewById(R.id.recView);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
            recView.setLayoutManager(manager);
            RecyclerView.Adapter adapter = new Country_Adapter(getActivity(),countryModelList,this,"fragment_driver_register");
            recView.setAdapter(adapter);

            alertDialog_country = new AlertDialog.Builder(getActivity())
                    .setCancelable(true)
                    .create();

            alertDialog_country.setCanceledOnTouchOutside(false);
            alertDialog_country.setView(view);
            alertDialog_country.getWindow().getAttributes().windowAnimations=R.style.alert_style;
            alertDialog_country.show();
        }
    }


    private void CreateNeighborhoodAlertDialog(List<CountryModel.Neighborhood> neighborhoodList) {
        if (countryModelList.size()>0)
        {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.country_neigbourhood_alert_layout,null);
            TextView tv_title = view.findViewById(R.id.tv_title);
            tv_title.setText(getString(R.string.neighborhood));
            RecyclerView recView = view.findViewById(R.id.recView);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
            recView.setLayoutManager(manager);
            RecyclerView.Adapter adapter = new Neighborhood_Adapter(getActivity(),neighborhoodList,this,"fragment_driver_register");
            recView.setAdapter(adapter);

            alertDialog_neighborhood = new AlertDialog.Builder(getActivity())
                    .setCancelable(true)
                    .create();

            alertDialog_neighborhood.setCanceledOnTouchOutside(false);
            alertDialog_neighborhood.setView(view);
            alertDialog_neighborhood.getWindow().getAttributes().windowAnimations=R.style.alert_style;

        }
    }





    private void getCountryData() {
        progDialogCountry = Common.CreateProgressDialog(getActivity(),"Loading...");
        Api.getServices()
                .getCountries()
                .enqueue(new Callback<List<CountryModel>>() {
                    @Override
                    public void onResponse(Call<List<CountryModel>> call, Response<List<CountryModel>> response) {

                        if (response.isSuccessful())
                        {
                            progDialogCountry.dismiss();
                            Fragment_Driver_Register.this.countryModelList = response.body();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryModel>> call, Throwable t) {
                        progDialogCountry.dismiss();
                        Log.e("Error",t.getMessage());
                        Toast.makeText(homeActivity,R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setItem(CountryModel countryModel)
    {
        city_id = countryModel.getCity_id();
        tv_neighborhood.setVisibility(View.VISIBLE);
        tv_city.setText(countryModel.getCity_title());
        CreateNeighborhoodAlertDialog(countryModel.getSub_areas());
        alertDialog_country.dismiss();

    }
    public void setNeighborhoodItem(CountryModel.Neighborhood neighborhood)
    {
        area_id = neighborhood.getId_area();
        tv_neighborhood.setText(neighborhood.getArea_title());
        alertDialog_neighborhood.dismiss();
    }

    public static Fragment_Driver_Register getInstance()
    {
        Fragment_Driver_Register fragment = new Fragment_Driver_Register();
        return fragment;
    }
    private void CheckReadPermission() {
        String [] readpermissions = {read_per};
        if (ContextCompat.checkSelfPermission(getActivity(),read_per)==PackageManager.PERMISSION_GRANTED)
        {
            SelectImage(IMG_REQ_1);

        }else
        {
            ActivityCompat.requestPermissions(getActivity(),readpermissions,read_req);
        }
    }

    private void Register()
    {
        m_name = edt_name.getText().toString();
        m_phone= edt_phone.getText().toString();
        m_userName = edt_userName.getText().toString();
        m_password = edt_password.getText().toString();
        m_rePassword = edt_re_password.getText().toString();
        edt_check_phone.setPhoneNumber(m_phone);
        if (TextUtils.isEmpty(m_name))
        {
            edt_name.setError(getString(R.string.name_req));
        }else if (TextUtils.isEmpty(m_phone))
        {
            edt_name.setError(null);
            edt_phone.setError(getString(R.string.phone_req));
        }else if (!edt_check_phone.isValid())
        {
            edt_name.setError(null);
            edt_phone.setError(getString(R.string.inv_phone));
        }else if (TextUtils.isEmpty(city_id))
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            tv_city.setError(getString(R.string.city_req));
        }
        else if (TextUtils.isEmpty(area_id))
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            tv_city.setError(null);
            tv_neighborhood.setError(getString(R.string.neighborhood_req));
        }
        else if (TextUtils.isEmpty(imagePath_2))
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            tv_city.setError(null);
            tv_neighborhood.setError(null);
            Toast.makeText(homeActivity, R.string.ch_lis_img, Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(imagePath_3))
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            tv_city.setError(null);
            tv_neighborhood.setError(null);
            Toast.makeText(homeActivity, R.string.ch_res_img, Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(imagePath_4))
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            tv_city.setError(null);
            tv_neighborhood.setError(null);
            Toast.makeText(homeActivity, R.string.ch_car_form, Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(m_userName))
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            tv_city.setError(null);
            tv_neighborhood.setError(null);
            edt_userName.setError(getString(R.string.username_req));
        }else if (TextUtils.isEmpty(m_password))
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_userName.setError(null);
            tv_city.setError(null);
            tv_neighborhood.setError(null);
            edt_password.setError(getString(R.string.password_req));
        }else if (TextUtils.isEmpty(m_rePassword))
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            tv_city.setError(null);
            tv_neighborhood.setError(null);
            edt_userName.setError(null);
            edt_password.setError(null);
            edt_re_password.setError(getString(R.string.re_pass_req));
        }
        else if (!m_password.equals(m_rePassword))
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            tv_city.setError(null);
            tv_neighborhood.setError(null);
            edt_userName.setError(null);
            edt_password.setError(null);
            edt_re_password.setError(getString(R.string.pass_not_match));
            edt_re_password.setText(null);
        }else if (TextUtils.isEmpty(imagePath_1))
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            tv_city.setError(null);
            tv_neighborhood.setError(null);
            edt_userName.setError(null);
            edt_password.setError(null);
            edt_re_password.setError(null);
            Toast.makeText(homeActivity, R.string.ch_prof_img, Toast.LENGTH_SHORT).show();

        }else
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            tv_city.setError(null);
            tv_neighborhood.setError(null);
            edt_userName.setError(null);
            edt_password.setError(null);
            edt_re_password.setError(null);
            //////////////////save data////////////
            userPhoto_File = new File(imagePath_1);
            user_license_photoFile = new File(imagePath_2);
            user_car_photoFile =  new File(imagePath_4);
            user_residence_photoFile = new File(imagePath_3);
            user_send_photo=Tags.user_send_photo;

            progressDialog = Common.CreateAlertDialog(getActivity(),getString(R.string.reging));
            progressDialog.show();
            RequestBody user_photoPart = RequestBody.create(MediaType.parse("image/*"),userPhoto_File);
            RequestBody user_license_photoPart = RequestBody.create(MediaType.parse("image/*"),user_license_photoFile);
            RequestBody user_car_photoPart = RequestBody.create(MediaType.parse("image/*"),user_car_photoFile);
            RequestBody user_residence_photoPart = RequestBody.create(MediaType.parse("image/*"),user_residence_photoFile);

            RequestBody userNamePart = RequestBody.create(MediaType.parse("text/plain"),m_userName);
            RequestBody userPassPart = RequestBody.create(MediaType.parse("text/plain"),m_password);
            RequestBody userPhonePart = RequestBody.create(MediaType.parse("text/plain"),m_phone);
            RequestBody userFullNamePart = RequestBody.create(MediaType.parse("text/plain"),m_name);
            RequestBody userTokenPart = RequestBody.create(MediaType.parse("text/plain"),"");
            RequestBody userLatPart = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(myLat));
            RequestBody userLngPart = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(myLng));
            RequestBody userneighbourhoodPart = RequestBody.create(MediaType.parse("text/plain"),area_id);
            RequestBody userCityPart = RequestBody.create(MediaType.parse("text/plain"),city_id);

            RequestBody userEmailPart = RequestBody.create(MediaType.parse("text/plain"),"");
            MultipartBody.Part part_user_photo =MultipartBody.Part.createFormData("user_photo",userPhoto_File.getName(),user_photoPart);
            MultipartBody.Part part_user_license_photo =MultipartBody.Part.createFormData("user_license_photo",userPhoto_File.getName(),user_license_photoPart);
            MultipartBody.Part part_user_car_photo =MultipartBody.Part.createFormData("user_car_photo",userPhoto_File.getName(),user_car_photoPart);
            MultipartBody.Part part_user_residence_photoPart =MultipartBody.Part.createFormData("user_residence_photo",userPhoto_File.getName(),user_residence_photoPart);

            Api.getServices()
                    .DriverRegister(userNamePart,userPassPart,userPhonePart,userFullNamePart,userTokenPart,userLatPart,userLngPart,userEmailPart,userneighbourhoodPart,userCityPart,part_user_photo,part_user_license_photo,part_user_car_photo,part_user_residence_photoPart)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (response.isSuccessful())
                            {
                                progressDialog.dismiss();
                                Log.e("Session",session+"s");

                                if (response.body().getSuccess()==0)
                                {
                                    Toast.makeText(homeActivity, R.string.reg_failed, Toast.LENGTH_SHORT).show();

                                }else if (response.body().getSuccess()==1)
                                {
                                    if (session!=null&&!TextUtils.isEmpty(session))
                                    {
                                        if (session.equals(Tags.session_logout))
                                        {
                                            homeActivity.UpdateData(response.body());
                                            homeActivity.UpdateSettingFragmentUi(response.body());
                                            Toast.makeText(homeActivity, R.string.reg_success, Toast.LENGTH_SHORT).show();
                                        }else if (session.equals(Tags.session_login))
                                        {
                                            homeActivity.CloseBottomsheet();

                                            Toast.makeText(homeActivity, R.string.reg_success, Toast.LENGTH_SHORT).show();

                                        }
                                    }else if (session!=null&&TextUtils.isEmpty(session))
                                    {
                                        homeActivity.UpdateData(response.body());
                                        Toast.makeText(homeActivity, R.string.reg_success, Toast.LENGTH_SHORT).show();

                                    }
                                }else if (response.body().getSuccess()==2)
                                {
                                    Toast.makeText(homeActivity, R.string.user_exist, Toast.LENGTH_SHORT).show();

                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(homeActivity, R.string.reg_failed, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }
                    });
        }
    }

    private boolean IsGpsOpen() {
        if (manager!=null)
        {
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                return true;
            }

        }
        return false;
    }
    private void CreateAlertDialog()
    {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_gps_dialog,null);
        Button button = view.findViewById(R.id.openBtn);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        button.setOnClickListener(view1 -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent,gps_req);
            dialog.dismiss();
        });

        cancelBtn.setOnClickListener(view1 -> {
            dialog.dismiss();
            homeActivity.navigateToSettingFragment();
        });
        dialog = new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(false);

    }

    private void SelectImage(int IMG_REQ)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        getActivity().startActivityForResult(intent.createChooser(intent,"Select photo"),IMG_REQ);
    }
    private void initGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }
    private void initLocationRequest()
    {
        locationRequest = new LocationRequest();
        locationRequest.setSmallestDisplacement(10f);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdate();
    }
    private void startLocationUpdate()
    {
        initLocationRequest();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation!=null)
        {
            myLat = lastLocation.getLatitude();
            myLng = lastLocation.getLongitude();
            Log.e("lat1",myLat+"");
            Log.e("lng1",myLng+"");
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
    }
    @Override
    public void onConnectionSuspended(int i)
    {
        if (googleApiClient!=null)
        {
            googleApiClient.connect();
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onLocationChanged(Location location) {
        if (location!=null)
        {
            myLat = location.getLatitude();
            myLng = location.getLongitude();
            Log.e("lat2",myLat+"");
            Log.e("lng2",myLng+"");


        }
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQ_1 && resultCode== Activity.RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            Log.e("url1",uri+"//");

            try {
                bitmap1 = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                image.setImageBitmap(bitmap1);
                imagePath_1 = Common.getImagePath(getActivity(),uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if (requestCode==IMG_REQ_2 && resultCode== Activity.RESULT_OK && data!=null) {
            Uri uri = data.getData();
            try {
                bitmap2 = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                licence_image.setImageBitmap(bitmap2);
                Log.e("url2",uri+"//");
                imagePath_2 = Common.getImagePath(getActivity(), uri);
                Log.e("path21",imagePath_2+"____");
                Log.e("path21",uri.getPath()+"____");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode==IMG_REQ_3 && resultCode== Activity.RESULT_OK && data!=null) {
            Uri uri = data.getData();
            try {
                Log.e("url3",uri+"//");

                bitmap3 = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                residence_image.setImageBitmap(bitmap3);
                imagePath_3 = Common.getImagePath(getActivity(), uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode==IMG_REQ_4 && resultCode== Activity.RESULT_OK && data!=null) {
            Uri uri = data.getData();
            try {
                Log.e("url4",uri+"//");

                bitmap4 = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                car_image.setImageBitmap(bitmap4);
                imagePath_4 = Common.getImagePath(getActivity(), uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if (requestCode==gps_req)
        {
            if (resultCode== Activity.RESULT_OK)
            {
                if (IsGpsOpen())
                {
                    initGoogleApiClient();

                }else
                {
                    dialog.show();
                }
            }else
            {
                if (IsGpsOpen())
                {
                    initGoogleApiClient();

                }else
                {
                    dialog.show();
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==read_req)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }

                SelectImage(IMG_REQ_1);
            }
        }
    }

}
