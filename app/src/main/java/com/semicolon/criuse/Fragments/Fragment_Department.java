package com.semicolon.criuse.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.criuse.R;

public class Fragment_Department extends Fragment{
    private static  String TAG = "TAG";
    private String user_type="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Department getInstance(String user_type)
    {
        Fragment_Department fragment_department = new Fragment_Department();
        Bundle bundle = new Bundle();
        bundle.putString(TAG,user_type);
        fragment_department.setArguments(bundle);
        return fragment_department;
    }
    private void initView(View view) {
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            user_type = bundle.getString(TAG);
        }
    }
}
