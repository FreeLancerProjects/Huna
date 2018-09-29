package com.semicolon.huna.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.semicolon.huna.Fragments.Fragment_Location;
import com.semicolon.huna.R;

public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_location_container, Fragment_Location.getInstance()).commit();
    }
}
