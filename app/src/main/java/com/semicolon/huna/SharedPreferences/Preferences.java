package com.semicolon.huna.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.semicolon.huna.Models.UserModel;
import com.semicolon.huna.Services.Tags;

public class Preferences {
    private static Preferences instance=null;
    private SharedPreferences sharedPreferences;

    private Preferences() {
    }

    public static synchronized Preferences getInstance()
    {
        if (instance==null)
        {
            instance = new Preferences();
        }
        return instance;
    }

    public void Create_Update_Pref(Context context,String userModel_Gson)
    {
        sharedPreferences = context.getSharedPreferences("UserPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userdata",userModel_Gson);
        editor.apply();
    }
    public void Clear_SharedPref(Context context)
    {
        sharedPreferences = context.getSharedPreferences("UserPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userdata","");
        editor.apply();
        Create_Update_Session(context, Tags.session_logout);
    }
    public UserModel getUserData(Context context)
    {
        sharedPreferences = context.getSharedPreferences("UserPref",Context.MODE_PRIVATE);
        String user_data_gson = sharedPreferences.getString("userdata","");
        Gson gson = new Gson();
        UserModel userModel = gson.fromJson(user_data_gson,UserModel.class);
        return userModel;
    }
    public void Create_Update_Session(Context context,String session)
    {
        sharedPreferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("session",session);
        editor.apply();
    }

    public String getSession(Context context)
    {
        sharedPreferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        String session = sharedPreferences.getString("session","");
        return session;
    }

    public void CreateUpdateChat_id(Context context,String chat_id)
    {
        sharedPreferences = context.getSharedPreferences("chat_id",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("chat_id",chat_id);
        editor.apply();
    }

    public String getChat_id (Context context)
    {
        sharedPreferences = context.getSharedPreferences("chat_id",Context.MODE_PRIVATE);
        String chat_id = sharedPreferences.getString("chat_id","");
        return chat_id;
    }
}
