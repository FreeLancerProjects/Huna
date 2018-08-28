package com.semicolon.criuse.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.Adapters.PlaceAutocompleteAdapter;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Share.Common;
import com.semicolon.criuse.SingleTones.ItemsSingleTone;
import com.semicolon.criuse.SingleTones.Location_Order_SingleTone;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Fragment_Map extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleMap mMap;
    private String fineLoc = Manifest.permission.ACCESS_FINE_LOCATION;
    private String coarseLoc = Manifest.permission.ACCESS_COARSE_LOCATION;
    private AutoCompleteTextView tv_search;
    private Button btn_select_location;
    private final int per_req = 102;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private double myLat = 0.0, myLng = 0.0;
    private LatLngBounds latLngBounds = new LatLngBounds(
            new LatLng(-33.880490, 151.184363),
            new LatLng(-33.858754, 151.229596));

    private PlaceAutocompleteAdapter adapter;
    private GeoDataClient geoDataClient;
    private double address_lat=0.0,address_lng=0.0;
    private Marker marker;
    private HomeActivity homeActivity;
    private SupportMapFragment fragment;
    private Location_Order_SingleTone location_order_singleTone;
    private ItemsSingleTone itemsSingleTone;
    private LatLng startPos,endPos;
    private String country_code="SA";
    private AutocompleteFilter filter;
    private FloatingActionButton fab;
    private ProgressDialog progressDialog;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initView(view);
        return view;
    }

    public static Fragment_Map getInstance()
    {
        Fragment_Map fragment = new Fragment_Map();
        return fragment;
    }
    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        itemsSingleTone = ItemsSingleTone.getInstance();
        if (isServiceOk()) {
            checkPermissions();
        }

        fab = view.findViewById(R.id.fab);
        fab.setEnabled(false);
        tv_search = view.findViewById(R.id.tv_search);
        btn_select_location = view.findViewById(R.id.btn_select_location);
        btn_select_location.setEnabled(false);



        btn_select_location.setOnClickListener(view1 -> {
            address_lat = marker.getPosition().latitude;
            address_lng = marker.getPosition().longitude;
            progressDialog = Common.CreateProgressDialog2(getActivity(),getString(R.string.locating));
            getPlaceName(address_lat,address_lng);




            Log.e("lat111",address_lat+"");
            Log.e("lng111",address_lng+"");

        });

        fab.setOnClickListener(view12 -> {
            marker.setPosition(new LatLng(myLat,myLng));
            startPos = marker.getPosition();
            endPos = new LatLng(myLat,myLng);

            animateMarker(startPos,endPos,marker,"0");
        });

    }

    private void initMap() {
        if (fragment==null)
        {
            fragment = SupportMapFragment.newInstance();
            fragment.getMapAsync(this);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.map,fragment).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setBuildingsEnabled(true);
            mMap.setTrafficEnabled(false);
            mMap.setIndoorEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.maps));
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }



        location_order_singleTone = Location_Order_SingleTone.getInstance();
        mMap.setMyLocationEnabled(true);
        initGoogleApiClient();

        geoDataClient = Places.getGeoDataClient(getActivity(),null);
        /*filter = new AutocompleteFilter.Builder()
                .setCountry(country_code)
                .build();*/
        filter = new AutocompleteFilter.Builder()
                .setCountry(country_code)
                .build();

        adapter = new PlaceAutocompleteAdapter(getActivity(),geoDataClient,latLngBounds,filter);
        tv_search.setAdapter(adapter);
        tv_search.setOnItemClickListener(itemClickListener);



    }

    private boolean isServiceOk()
    {
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());
        if (availability== ConnectionResult.SUCCESS)
        {
            return true;
        }else if (GoogleApiAvailability.getInstance().isUserResolvableError(availability))
        {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(),availability,9001);
            dialog.show();

        }
        return false;
    }

    private void checkPermissions()
    {
        String [] permissions = {fineLoc,coarseLoc};
        if (ContextCompat.checkSelfPermission(getActivity(),fineLoc)== PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(getActivity(),coarseLoc)==PackageManager.PERMISSION_GRANTED)
            {
                initMap();
            }else
                {
                    ActivityCompat.requestPermissions(getActivity(),permissions,per_req);
                }
        }else
            {
                ActivityCompat.requestPermissions(getActivity(),permissions,per_req);

            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == per_req)
        {
            if (grantResults.length>0)
            {
                for ( int i=0;i<grantResults.length;i++)
                {
                    if (grantResults[i]!=PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                }

                initMap();
            }
        }
    }

    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation!=null)
        {
            myLat = lastLocation.getLatitude();
            myLng = lastLocation.getLongitude();
            fab.setEnabled(true);

            getCountryCode(myLat,myLng);
            Log.e("lat1",myLat+"");
            Log.e("lng1",myLng+"");
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
    }

    private void getCountryCode(double myLat, double myLng) {
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> addressList = geocoder.getFromLocation(myLat,myLng,1);
            if (addressList.size()>0)
            {
                Address address = addressList.get(0);
                if (address!=null)
                {
                    country_code = address.getCountryCode();
                    filter = new AutocompleteFilter.Builder()
                            .setCountry(country_code)
                            .build();
                    adapter = new PlaceAutocompleteAdapter(getActivity(),geoDataClient,latLngBounds,filter);
                    tv_search.setAdapter(adapter);
                    tv_search.setOnItemClickListener(itemClickListener);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient!=null)
            googleApiClient.connect();
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
            AddMarker(myLat,myLng);
            fab.setEnabled(true);
            btn_select_location.setEnabled(true);
            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                    address_lat = cameraPosition.target.latitude;
                    address_lng = cameraPosition.target.longitude;
                    marker.setPosition(cameraPosition.target);

                    Log.e("lat",address_lat+"");
                    Log.e("lng",address_lng+"");

                }
            });
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }
    }

    private void AddMarker(double myLat, double myLng) {
       marker =mMap.addMarker(new MarkerOptions().position(new LatLng(myLat,myLng)).icon(BitmapDescriptorFactory.fromBitmap(createMarkerBitmap())));
       mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLat,myLng),16.7f));
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            AutocompletePrediction item = adapter.getItem(i);
            String place_id = item.getPlaceId();
            PendingResult<PlaceBuffer> bufferPendingResult = Places.GeoDataApi.getPlaceById(googleApiClient,place_id);
            bufferPendingResult.setResultCallback(resultCallback);

        }
    };

    private ResultCallback<PlaceBuffer> resultCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {

            try {
                if (!places.getStatus().isSuccess())
                {
                    places.release();
                    return;
                }
                tv_search.setText(null);
                Common.CloseKeyBoard(getActivity(),tv_search);
                Place place = places.get(0);
                address_lat = place.getLatLng().latitude;
                address_lng = place.getLatLng().longitude;

                startPos = marker.getPosition();
                endPos = place.getLatLng();
                animateMarker(startPos,endPos,marker,"0");


                places.release();
            }catch (NullPointerException e)
            {

            }

        }
    };


    private void animateMarker(LatLng startPos, LatLng endPos, Marker marker,String flag)
    {
        final LatLng startPosition = startPos;
        final LatLng finalPosition = endPos;
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 3000;
        final boolean hideMarker = false;

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                LatLng currentPosition = new LatLng(
                        startPosition.latitude * (1 - t) + finalPosition.latitude * t,
                        startPosition.longitude * (1 - t) + finalPosition.longitude * t);


                marker.setPosition(currentPosition);
                if (flag=="0")
                {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                }
                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }


                }
            }
        });
    }


    private Bitmap createMarkerBitmap()
    {
        Bitmap bitmap1 = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.map_marker);
        int newWidth = 55;
        int newHeight = 55;
        float scaleWidth = ((float)newWidth/bitmap1.getWidth());
        float scaleHeight = ((float)newHeight/bitmap1.getHeight());

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap returnedBitmap = Bitmap.createBitmap(bitmap1,0,0,bitmap1.getWidth(),bitmap1.getHeight(),matrix,true);
        return returnedBitmap;
    }

    private void getPlaceName(double lat,double lng)
    {

        Geocoder geocoder = new Geocoder(getActivity(),Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
            if (addressList.size()>0)
            {
                Address address = addressList.get(0);
                if (address!=null)
                {
                    String location = "";

                    if (address.getThoroughfare()!=null)
                    {
                        String street = address.getThoroughfare()+",";

                        location+=street;
                    }
                    if (address.getLocality()!=null)
                    {
                        String locality = address.getLocality()+",";
                        location+=locality;
                    }
                    if (address.getAdminArea()!=null)
                    {
                        String adminArea = address.getAdminArea()+",";
                        location+=adminArea;

                    }
                    if (address.getCountryName()!=null)
                    {
                        String country = address.getCountryName();
                        location+=country;

                    }

                    Log.e("loca",location);

                    location_order_singleTone.setOrderLocation(lat,lng,location);

                    progressDialog.dismiss();
                    FragmentTransaction transaction = homeActivity.fragmentManager.beginTransaction();
                    Fragment_Bill fragment_bill = Fragment_Bill.getInstance(itemsSingleTone.getBill_modelList());
                    transaction.add(R.id.fragmentsContainer,fragment_bill,"fragment_bill");
                    homeActivity.fragmentManager.popBackStack();
                    transaction.addToBackStack("Fragment_Bill");
                    transaction.commit();
                }else
                    {
                        progressDialog.dismiss();

                        Toast.makeText(homeActivity, R.string.inv_loc, Toast.LENGTH_LONG).show();

                    }
            }else
                {
                    progressDialog.dismiss();

                    Toast.makeText(homeActivity, R.string.inv_loc, Toast.LENGTH_LONG).show();

                }
        } catch (IOException e) {
            e.printStackTrace();
            progressDialog.dismiss();

            Toast.makeText(homeActivity, R.string.inv_loc, Toast.LENGTH_LONG).show();

        }

    }


}