package com.semicolon.criuse.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.Models.UserModel;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Api;
import com.semicolon.criuse.Services.Tags;
import com.semicolon.criuse.Share.Common;
import com.semicolon.criuse.SharedPreferences.Preferences;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Client_Register extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private LinearLayout ll_login;
    private HomeActivity homeActivity;
    private CircleImageView image;
    private EditText edt_name, edt_phone, edt_userName, edt_password, edt_re_password;
    private PhoneInputLayout edt_check_phone;
    private Button btn_Register;
    private Bitmap bitmap;
    private String m_name = "", m_phone = "", m_userName = "", m_password = "", m_rePassword;
    private String imagePath = "";
    private String user_send_photo="";
    private File image_file;
    private final int IMG_REQ = 1;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private double myLat = 0.0, myLng = 0.0;
    private AlertDialog progressDialog;
    private Preferences preferences;
    private String session="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_register, container, false);
        initView(view);
        return view;
    }

    public static Fragment_Client_Register getInstance() {
        Fragment_Client_Register fragment = new Fragment_Client_Register();
        return fragment;
    }

    private void initView(View view) {
        preferences = Preferences.getInstance();
        session=preferences.getSession(getActivity());
        homeActivity = (HomeActivity) getActivity();
        ll_login = view.findViewById(R.id.ll_login);
        image = view.findViewById(R.id.image);
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_check_phone = view.findViewById(R.id.edt_check_phone);
        edt_userName = view.findViewById(R.id.edt_user_name);
        edt_password = view.findViewById(R.id.edt_password);
        edt_re_password = view.findViewById(R.id.edt_re_password);
        btn_Register = view.findViewById(R.id.reg_btn);

        image.setOnClickListener(view1 -> SelectImage());
        btn_Register.setOnClickListener(view12 -> Register());

        ll_login.setOnClickListener(view13 -> homeActivity.DisplayLoginLayout(Tags.client_register));

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

        initGoogleApiClient();

    }




    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void initLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setSmallestDisplacement(10f);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void SelectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        getActivity().startActivityForResult(intent.createChooser(intent, "Select photo"), IMG_REQ);
    }

    private void Register() {
        m_name = edt_name.getText().toString();
        m_phone = edt_phone.getText().toString();
        m_userName = edt_userName.getText().toString();
        m_password = edt_password.getText().toString();
        m_rePassword = edt_re_password.getText().toString();
        edt_check_phone.setPhoneNumber(m_phone);
        if (TextUtils.isEmpty(m_name)) {
            edt_name.setError(getString(R.string.name_req));
        } else if (TextUtils.isEmpty(m_phone)) {
            edt_name.setError(null);
            edt_phone.setError(getString(R.string.phone_req));
        } else if (!edt_check_phone.isValid()) {
            edt_name.setError(null);
            edt_phone.setError(getString(R.string.inv_phone));
        } else if (TextUtils.isEmpty(m_userName)) {
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_userName.setError(getString(R.string.username_req));
        } else if (TextUtils.isEmpty(m_password)) {
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_userName.setError(null);
            edt_password.setError(getString(R.string.password_req));
        } else if (TextUtils.isEmpty(m_rePassword)) {
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_userName.setError(null);
            edt_password.setError(null);
            edt_re_password.setError(getString(R.string.re_pass_req));
        } else if (!m_password.equals(m_rePassword)) {
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_userName.setError(null);
            edt_password.setError(null);
            edt_re_password.setError(getString(R.string.pass_not_match));
            edt_re_password.setText(null);
        } else {
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_userName.setError(null);
            edt_password.setError(null);
            edt_re_password.setError(null);
            //////////////////save data////////////
            if (TextUtils.isEmpty(imagePath)) {
                user_send_photo=Tags.user_send_photo;
                image_file =ConvertBitmapToFile();


            } else {
                image_file = new File(imagePath);
                user_send_photo=Tags.user_send_photo;

            }
            progressDialog = Common.CreateProgressDialog(getActivity(),"Registering....");
            progressDialog.show();
            RequestBody imagePart = RequestBody.create(MediaType.parse("image/*"),image_file);
            RequestBody userNamePart = RequestBody.create(MediaType.parse("text/plain"),m_userName);
            RequestBody userPassPart = RequestBody.create(MediaType.parse("text/plain"),m_password);
            RequestBody userPhonePart = RequestBody.create(MediaType.parse("text/plain"),m_phone);
            RequestBody userFullNamePart = RequestBody.create(MediaType.parse("text/plain"),m_name);
            RequestBody userTokenPart = RequestBody.create(MediaType.parse("text/plain"),"");
            RequestBody userLatPart = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(myLat));
            RequestBody userLngPart = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(myLng));

            RequestBody userEmailPart = RequestBody.create(MediaType.parse("text/plain"),"");
            RequestBody usersendTypePart = RequestBody.create(MediaType.parse("text/plain"),user_send_photo);
            MultipartBody.Part part =MultipartBody.Part.createFormData("user_photo",image_file.getName(),imagePart);
            Api.getServices()
                    .ClientRegister(part,userNamePart,userPassPart,userPhonePart,userFullNamePart,userTokenPart,userLatPart,userLngPart,userEmailPart,usersendTypePart)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            if (response.isSuccessful())
                            {
                                progressDialog.dismiss();
                                if (response.body().getSuccess()==1)
                                {
                                    Log.e("Session",session+"s");

                                    if (session!=null&&!TextUtils.isEmpty(session))
                                    {
                                        if (session.equals(Tags.session_logout))
                                        {
                                            homeActivity.UpdateData(response.body());
                                            homeActivity.UpdateSettingFragmentUi(response.body());

                                            Log.e("data2",response.body().getUser_id());
                                            Log.e("data2",response.body().getUser_full_name());
                                            Log.e("data2",response.body().getUser_phone());

                                            Toast.makeText(homeActivity, R.string.reg_success, Toast.LENGTH_SHORT).show();
                                        }else if (session.equals(Tags.session_login))
                                        {
                                            Toast.makeText(homeActivity, R.string.reg_success, Toast.LENGTH_SHORT).show();

                                        }
                                    }else if (session!=null&&TextUtils.isEmpty(session))
                                        {
                                            homeActivity.UpdateData(response.body());
                                            Toast.makeText(homeActivity, R.string.reg_success, Toast.LENGTH_SHORT).show();

                                        }


                                }else if (response.body().getSuccess()==0)
                                {
                                    Toast.makeText(homeActivity, R.string.reg_failed, Toast.LENGTH_SHORT).show();

                                }else if (response.body().getSuccess()==2)
                                {
                                    Toast.makeText(homeActivity, R.string.user_exist, Toast.LENGTH_SHORT).show();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(homeActivity, "Register failed try again later", Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }
                    });


        }
    }

    private File ConvertBitmapToFile() {
        File file = new File(getActivity().getCacheDir(),System.currentTimeMillis()+".png");
        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.user_profile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,outputStream);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(outputStream.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                image.setImageBitmap(bitmap);
                imagePath = Common.getImagePath(getActivity(), uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdate();
    }

    private void startLocationUpdate() {
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
    public void onConnectionSuspended(int i) {
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

   }
