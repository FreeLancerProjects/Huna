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

public class Fragment_MiniMarkets extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private TextView tv_no_cat;
    private List<MiniMarketDataModel> miniMarketModelList;
    private HomeActivity homeActivity;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private double myLat = 0.0, myLng = 0.0;
    private String finLoc = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int per_req = 2011;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minimarkets, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        miniMarketModelList = new ArrayList<>();
        tv_no_cat = view.findViewById(R.id.tv_no_cat);
        progBar = view.findViewById(R.id.progBar);
        recView = view.findViewById(R.id.recView);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(view.getContext());
        recView.setLayoutManager(manager);
        adapter = new MiniMarketAdapter(getActivity(), miniMarketModelList, this);
        recView.setAdapter(adapter);
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
        //LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);



    }


    private void getData(double myLat, double myLng) {
        Api.getServices()
                .getMiniMarketData(myLat, myLng)
                .enqueue(new Callback<List<MiniMarketDataModel>>() {
                    @Override
                    public void onResponse(Call<List<MiniMarketDataModel>> call, Response<List<MiniMarketDataModel>> response) {
                        if (response.isSuccessful()) {
                            progBar.setVisibility(View.GONE);
                            miniMarketModelList.clear();
                            miniMarketModelList.addAll(response.body());
                            if (miniMarketModelList.size() > 0) {
                                tv_no_cat.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();

                            } else {
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

        /*List<MiniMarketDataModel.SubProduct> subProductList = miniMarketModelList.get(pos).getSubProductList();
        List<ItemsModel> itemsModelList = new ArrayList<>();
        Log.e("subProductList.size",subProductList.size()+"");

        for (MiniMarketDataModel.SubProduct subProduct:subProductList)
        {
            ItemsModel itemsModel = new ItemsModel(subProduct.getId_product(),subProduct.getProduct_price(),"0",subProduct.getMarket_id_fk(),subProduct.getIs_admin(),subProduct.getProduct_image(),subProduct.getProduct_title(),title);
            itemsModelList.add(itemsModel);
        }
        Log.e("itemsModelList.size",itemsModelList.size()+"");

        Fragment_Subdepartment fragment_subdepartment = Fragment_Subdepartment.getInstance(itemsModelList,title);
        homeActivity.Hide_Navbottom();
        homeActivity.savePos();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer,fragment_subdepartment).commit();
*/
    }

    public static Fragment_MiniMarkets getInstance() {
        Fragment_MiniMarkets fragment = new Fragment_MiniMarkets();
        return fragment;
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

            getData(myLat, myLng);


        }
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == per_req) {
            if (grantResults.length > 0) {

                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                            String[] per = {finLoc};
                            ActivityCompat.requestPermissions(getActivity(), per, per_req);
                        }
                    } else {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                        }

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
}
