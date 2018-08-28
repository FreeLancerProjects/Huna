package com.semicolon.criuse.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.semicolon.criuse.R;

import spencerstudios.com.bungeelib.Bungee;

public class SearchActivity extends AppCompatActivity {

    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(view ->{
            finish();
            Bungee.fade(SearchActivity.this);
        } );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.fade(this);
    }
}
