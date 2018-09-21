package com.semicolon.huna.Fragments;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.semicolon.huna.Models.GroceryPart1;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Tags;
import com.semicolon.huna.SharedPreferences.Preferences;

import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Grocery_Register extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static String TAG="0";
    private LinearLayout ll_login;
    private HomeActivity homeActivity;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private double myLat = 0.0, myLng = 0.0;
    private EditText edt_name,edt_time,edt_phone,edt_password,edt_re_password,edt_username;
    private TextView tv_upload_image;
    private CircleImageView image;
    private PhoneInputLayout edt_check_phone;
    private Button btn_continue;
    private Bitmap bitmap=null;
    private Uri uri=null;
    private final int IMG_REQ=5;
    private Preferences preferences;
    private String session="";
    private final int read_req=1557;
    private String read_per = Manifest.permission.READ_EXTERNAL_STORAGE;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_register,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        preferences = Preferences.getInstance();
        session=preferences.getSession(getActivity());
        homeActivity = (HomeActivity) getActivity();

        image = view.findViewById(R.id.image);
        tv_upload_image = view.findViewById(R.id.tv_upload_image);
        edt_name = view.findViewById(R.id.edt_name);
        edt_time = view.findViewById(R.id.edt_time);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_check_phone = view.findViewById(R.id.edt_check_phone);
        edt_username = view.findViewById(R.id.edt_username);
        edt_password = view.findViewById(R.id.edt_password);
        edt_re_password = view.findViewById(R.id.edt_re_password);
        btn_continue = view.findViewById(R.id.continue_btn);
        ll_login = view.findViewById(R.id.ll_login);
        edt_check_phone.setDefaultCountry("sa");

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            GroceryPart1 groceryPart1 = (GroceryPart1) bundle.getSerializable(TAG);
            UpdateUI(groceryPart1);
        }
        initGoogleApiClient();

        ll_login.setOnClickListener(view1 -> homeActivity.DisplayLoginLayout(Tags.grocery_register));
        tv_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckReadPermission();
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Next();
            }
        });

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
    }
    private void CheckReadPermission() {
        String [] readpermissions = {read_per};
        if (ContextCompat.checkSelfPermission(getActivity(),read_per)==PackageManager.PERMISSION_GRANTED)
        {
            SelectImage();

        }else
        {
            ActivityCompat.requestPermissions(getActivity(),readpermissions,read_req);
        }
    }
    private void Next() {
        String m_name = edt_name.getText().toString();
        String m_hour = edt_time.getText().toString();
        String m_phone= edt_phone.getText().toString();
        String m_username = edt_username.getText().toString();
        String m_password = edt_password.getText().toString();
        String m_rePasswrd = edt_re_password.getText().toString();
        edt_check_phone.setPhoneNumber(m_phone);

        if (TextUtils.isEmpty(m_name))
        {
            edt_name.setError(getString(R.string.name_req));
        }else if (uri==null)
        {
            edt_name.setError(null);
            Toast.makeText(homeActivity,R.string.ch_prof_img, Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(m_hour))
        {
            edt_name.setError(null);
            edt_time.setError(getString(R.string.time_req));
        }else if (TextUtils.isEmpty(m_phone))
        {
            edt_name.setError(null);
            edt_time.setError(null);
            edt_phone.setError(getString(R.string.phone_req));
        }else if (!edt_check_phone.isValid())
        {
            edt_name.setError(null);
            edt_time.setError(null);
            edt_phone.setError(getString(R.string.inv_phone));

        }else if (TextUtils.isEmpty(m_username))
        {
            edt_name.setError(null);
            edt_time.setError(null);
            edt_phone.setError(null);
            edt_username.setError(getString(R.string.username_req));
        }else if (TextUtils.isEmpty(m_password))
        {
            edt_name.setError(null);
            edt_time.setError(null);
            edt_phone.setError(null);
            edt_username.setError(null);
            edt_password.setError(getString(R.string.password_req));
        }else if (TextUtils.isEmpty(m_rePasswrd))
        {
            edt_name.setError(null);
            edt_time.setError(null);
            edt_phone.setError(null);
            edt_username.setError(null);
            edt_password.setError(null);
            edt_re_password.setError(getString(R.string.re_pass_req));
        }
        else if (!m_password.equals(m_rePasswrd))
        {
            edt_name.setError(null);
            edt_time.setError(null);
            edt_phone.setError(null);
            edt_username.setError(null);
            edt_password.setError(null);
            edt_re_password.setError(getString(R.string.pass_not_match));
            edt_re_password.setText(null);
        }else
            {
                edt_name.setError(null);
                edt_time.setError(null);
                edt_phone.setError(null);
                edt_username.setError(null);
                edt_password.setError(null);
                edt_re_password.setText(null);

                GroceryPart1 groceryPart1 = new GroceryPart1(uri.toString(),m_name,m_hour,m_phone,m_username,m_password,myLat,myLng);
                homeActivity.DisplayGroceryNextDataToComplete(groceryPart1);
            }
    }

    private void SelectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        getActivity().startActivityForResult(intent.createChooser(intent,"Select image"),IMG_REQ);
    }

    private void UpdateUI(GroceryPart1 groceryPart1) {
        if (groceryPart1!=null)
        {
            uri = Uri.parse(groceryPart1.getUri());
            image.setImageURI(Uri.parse(groceryPart1.getUri()));
            edt_name.setText(groceryPart1.getName());
            edt_time.setText(groceryPart1.getHour());
            edt_phone.setText(groceryPart1.getPhone());
            edt_username.setText(groceryPart1.getUsername());
            edt_password.setText(groceryPart1.getPassword());
            edt_re_password.setText(groceryPart1.getPassword());

        }
    }

    public static Fragment_Grocery_Register getInstance(GroceryPart1 groceryPart1)
    {
        Fragment_Grocery_Register fragment = new Fragment_Grocery_Register();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,groceryPart1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQ && resultCode== Activity.RESULT_OK && data!=null)
        {
            uri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
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

                SelectImage();
            }
        }
    }

}
