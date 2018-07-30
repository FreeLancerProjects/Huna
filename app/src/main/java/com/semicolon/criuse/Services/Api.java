package com.semicolon.criuse.Services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static Retrofit instance=null;

    private static Retrofit getInstance ()
    {
        if (instance==null)
        {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1,TimeUnit.MINUTES)
                    .readTimeout(1,TimeUnit.MINUTES)
                    .retryOnConnectionFailure(true)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            instance = new Retrofit.Builder()
                    .baseUrl(Tags.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return instance;
    }

    public static Service getServices()
    {
        Retrofit retrofit = getInstance();
        Service service = retrofit.create(Service.class);
        return service;
    }


}
