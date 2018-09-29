package com.semicolon.huna.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.semicolon.huna.R;
import com.semicolon.huna.SharedPreferences.Preferences;

public class SplashActivity extends AppCompatActivity {

    private Preferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences = Preferences.getInstance();
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (TextUtils.isEmpty(preferences.getFrom_id(SplashActivity.this)))
                        {
                            Intent intent = new Intent(SplashActivity.this,LocationActivity.class);
                            startActivity(intent);
                            finish();
                        }else
                            {
                                Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();
                                Log.e("splllaaaaaaaaaaaash","splaaaaaaaaaaaaaaaaaaash");
                            }

                    }
                },4000);
    }
}
