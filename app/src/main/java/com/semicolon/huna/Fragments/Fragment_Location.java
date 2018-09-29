package com.semicolon.huna.Fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.huna.Activities.HomeActivity;
import com.semicolon.huna.Activities.LocationActivity;
import com.semicolon.huna.Adapters.LocationAdapter;
import com.semicolon.huna.Adapters.LocationGroup;
import com.semicolon.huna.Models.CountryModel;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Api;
import com.semicolon.huna.Share.Common;
import com.semicolon.huna.SharedPreferences.Preferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Location extends Fragment{
    private AutoCompleteTextView searchView;
    private ProgressBar progBar,progBar_load;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<LocationGroup> main_groupModelList,groupModelList;
    private Preferences preferences;
    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location,container,false);
        initView(view);
        return view;

    }

    public static Fragment_Location getInstance()
    {
        Fragment_Location fragment = new Fragment_Location();
        return fragment;
    }
    private void initView(View view) {
        preferences = Preferences.getInstance();
        main_groupModelList = new ArrayList<>();
        groupModelList = new ArrayList<>();

        searchView = view.findViewById(R.id.searchView);
        progBar = view.findViewById(R.id.progBar);
        progBar_load = view.findViewById(R.id.progBar_load);
        progBar_load.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH)
                {
                    String query = searchView.getText().toString();
                    if (!TextUtils.isEmpty(query))
                    {
                        Common.CloseKeyBoard(getActivity(),searchView);
                        Search(query);
                    }
                }

                return false;
            }
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchView.getText().length()==0)
                {
                    if (main_groupModelList.size()>0)
                    {
                        groupModelList.clear();
                        groupModelList.addAll(main_groupModelList);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getData();
    }

    private void Search(String query) {
        progBar.setVisibility(View.VISIBLE);
        Api.getServices()
                .getPlaceSearch(query)
                .enqueue(new Callback<List<CountryModel>>() {
                    @Override
                    public void onResponse(Call<List<CountryModel>> call, Response<List<CountryModel>> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.INVISIBLE);

                            if (response.body().size()>0)
                            {
                                groupModelList.clear();
                                for (CountryModel countryModel :response.body())
                                {
                                    LocationGroup locationGroup = new LocationGroup(countryModel.getCity_title(),countryModel.getSub_areas(),countryModel.getCity_id());
                                    groupModelList.add(locationGroup);
                                }
                                adapter.notifyDataSetChanged();
                            }else
                                {
                                    Toast.makeText(getActivity(), "لا توجد بيانات بحث", Toast.LENGTH_SHORT).show();
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryModel>> call, Throwable t) {
                        progBar.setVisibility(View.INVISIBLE);
                        Log.e("Error",t.getMessage());
                        Toast.makeText(getActivity(), R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getData() {
        Api.getServices().getCountries()
                .enqueue(new Callback<List<CountryModel>>() {
                    @Override
                    public void onResponse(Call<List<CountryModel>> call, Response<List<CountryModel>> response) {
                        if (response.isSuccessful())
                        {
                            progBar_load.setVisibility(View.GONE);
                            if (response.body().size()>0)
                            {
                                for (CountryModel countryModel :response.body())
                                {
                                    LocationGroup locationGroup = new LocationGroup(countryModel.getCity_title(),countryModel.getSub_areas(),countryModel.getCity_id());
                                    main_groupModelList.add(locationGroup);
                                    groupModelList.add(locationGroup);
                                }

                                adapter = new LocationAdapter(groupModelList,getActivity(),Fragment_Location.this);
                                recView.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryModel>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progBar_load.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setCity_id (String from_id,String area_id,String area_title)
    {
        preferences.CreateUpdateCity_id(getActivity(),from_id,area_id,area_title);

        if (getActivity() instanceof LocationActivity)
        {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }else if (getActivity() instanceof HomeActivity)
        {
            homeActivity = (HomeActivity) getActivity();
            homeActivity.UpdateFragment_Home(from_id,area_id,area_title);
        }



    }
}
