package com.semicolon.huna.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.semicolon.huna.Activities.HomeActivity;
import com.semicolon.huna.Activities.MiniMarketDetailsActivity;
import com.semicolon.huna.Adapters.MiniMarketAdapter;
import com.semicolon.huna.Models.MiniMarketDataModel;
import com.semicolon.huna.Models.MiniMarket_SubCategory;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static String TAG_FROM_ID="1";
    private static String TAG_AREA_ID="2";

    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private TextView tv_no_cat;
    private List<MiniMarketDataModel> miniMarketModelList,main_miniMarketModelList;
    private HomeActivity homeActivity;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private double myLat = 0.0, myLng = 0.0;
    private String finLoc = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int per_req = 2011;
    private String from_id="",area_id="";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        return view;
    }
    public static Fragment_Home getInstance(String from_id,String area_id)
    {
        Bundle bundle = new Bundle();
        Fragment_Home fragment_home = new Fragment_Home();
        bundle.putString(TAG_FROM_ID,from_id);
        bundle.putString(TAG_AREA_ID,area_id);
        fragment_home.setArguments(bundle);
        return fragment_home;
    }
    private void initView(View view) {
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            from_id = bundle.getString(TAG_FROM_ID);
            area_id = bundle.getString(TAG_AREA_ID);
        }

        Log.e("from_id",from_id+"_");
        Log.e("area_id",area_id+"_");

        homeActivity = (HomeActivity) getActivity();
        miniMarketModelList = new ArrayList<>();
        main_miniMarketModelList = new ArrayList<>();
        tv_no_cat = view.findViewById(R.id.tv_no_cat);
        progBar = view.findViewById(R.id.progBar);
        recView = view.findViewById(R.id.recView);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(view.getContext());
        recView.setLayoutManager(manager);
        recView.setNestedScrollingEnabled(true);
        adapter = new MiniMarketAdapter(getActivity(), miniMarketModelList, this);
        recView.setAdapter(adapter);

        initGoogleApiClient();

        homeActivity.showFab();

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

    private void startLocationUpdate() {
        initLocationRequest();


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            String[] per = {finLoc};
            ActivityCompat.requestPermissions(getActivity(), per, per_req);
        }

        LocationServices.getFusedLocationProviderClient(getActivity())
                .requestLocationUpdates(locationRequest,new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                    }
                }, Looper.myLooper());



    }

    public void Search(String query)
    {
        if (TextUtils.isEmpty(query))
        {
            tv_no_cat.setVisibility(View.GONE);
            miniMarketModelList.clear();
            miniMarketModelList.addAll(main_miniMarketModelList);
            adapter.notifyDataSetChanged();
        }else
            {
                Api.getServices()
                        .getMatgarSearch(myLat,myLng,area_id,from_id,query)
                        .enqueue(new Callback<List<MiniMarketDataModel>>() {
                            @Override
                            public void onResponse(Call<List<MiniMarketDataModel>> call, Response<List<MiniMarketDataModel>> response) {
                                if (response.isSuccessful())
                                {
                                    homeActivity.HideLoadProgress();
                                    miniMarketModelList.clear();
                                    miniMarketModelList.addAll(response.body());
                                    adapter.notifyDataSetChanged();
                                    if (miniMarketModelList.size()>0)
                                    {
                                        tv_no_cat.setVisibility(View.GONE);
                                    }else
                                        {
                                            tv_no_cat.setText(getString(R.string.no_search_res));

                                            tv_no_cat.setVisibility(View.VISIBLE);

                                        }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<MiniMarketDataModel>> call, Throwable t) {
                                homeActivity.HideLoadProgress();

                                Log.e("Error",t.getMessage());
                                Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
    }
    private void getData(double myLat, double myLng,String id_area,String id_from) {
        Log.e("from_id2",from_id+"_");
        Log.e("area_id2",area_id+"_");
        Api.getServices()
                .getMiniMarketData(myLat, myLng,id_area,id_from)
                .enqueue(new Callback<List<MiniMarketDataModel>>() {
                    @Override
                    public void onResponse(Call<List<MiniMarketDataModel>> call, Response<List<MiniMarketDataModel>> response) {
                        if (response.isSuccessful()) {
                            progBar.setVisibility(View.GONE);
                            miniMarketModelList.clear();
                            main_miniMarketModelList.clear();
                            main_miniMarketModelList.addAll(response.body());
                            miniMarketModelList.addAll(response.body());
                            if (miniMarketModelList.size() > 0) {
                                tv_no_cat.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();

                            } else {
                                tv_no_cat.setText(getString(R.string.no_cat));
                                tv_no_cat.setVisibility(View.VISIBLE);

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<List<MiniMarketDataModel>> call, Throwable t) {
                        Log.e("Error", t.getMessage());
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), R.string.something_error, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void setPos(int pos) {
        MiniMarketDataModel marketDataModel = miniMarketModelList.get(pos);
        List<MiniMarket_SubCategory> miniMarket_subCategoryList = marketDataModel.getMiniMarket_subCategoryList();
        Intent intent = new Intent(getActivity(), MiniMarketDetailsActivity.class);
        intent.putExtra("data", (Serializable) miniMarket_subCategoryList);
        intent.putExtra("market_name", marketDataModel.getMarket_name());
        intent.putExtra("market_phone", marketDataModel.getMarket_phone());
        intent.putExtra("market_delivery", marketDataModel.getDelivery_cost());

        getActivity().startActivity(intent);


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            myLat = location.getLatitude();
            myLng = location.getLongitude();
            Log.e("lat2", myLat + "");
            Log.e("lng2", myLng + "");

            getData(myLat, myLng,area_id,from_id);


        }
        LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(new LocationCallback());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == per_req) {
            if (grantResults.length > 0) {

                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                            String[] per = {finLoc};
                            ActivityCompat.requestPermissions(getActivity(), per, per_req);

                            LocationServices.getFusedLocationProviderClient(getActivity())
                                    .requestLocationUpdates(locationRequest,new LocationCallback(){
                                        @Override
                                        public void onLocationResult(LocationResult locationResult) {
                                            onLocationChanged(locationResult.getLastLocation());
                                        }
                                    }, Looper.myLooper());
                        }else
                            {
                                LocationServices.getFusedLocationProviderClient(getActivity())
                                        .requestLocationUpdates(locationRequest,new LocationCallback(){
                                            @Override
                                            public void onLocationResult(LocationResult locationResult) {
                                                onLocationChanged(locationResult.getLastLocation());
                                            }
                                        }, Looper.myLooper());
                            }
                    } else {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                            String [] per = {finLoc};
                            ActivityCompat.requestPermissions(getActivity(),per,per_req);
                        }else
                            {
                                LocationServices.getFusedLocationProviderClient(getActivity())
                                        .requestLocationUpdates(locationRequest,new LocationCallback(){
                                            @Override
                                            public void onLocationResult(LocationResult locationResult) {
                                                onLocationChanged(locationResult.getLastLocation());
                                            }
                                        }, Looper.myLooper());
                            }


                    }


            }else
            {
                String [] per = {finLoc};
                ActivityCompat.requestPermissions(getActivity(),per,per_req);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (googleApiClient!=null)
        {
            googleApiClient.disconnect();
        }
        LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(new LocationCallback());
        super.onDestroy();

    }
}
