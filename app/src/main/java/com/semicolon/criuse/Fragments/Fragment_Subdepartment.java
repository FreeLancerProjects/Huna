package com.semicolon.criuse.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.criuse.R;
import com.semicolon.criuse.Share.Common;

public class Fragment_Subdepartment extends Fragment {
    private TextView tv_name;
    private AutoCompleteTextView searchView;
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subdepartment,container,false);
        context = view.getContext();
        initView(view);
        return view;
    }

    private void initView(View view) {
        tv_name = view.findViewById(R.id.tv_name);
        searchView = view.findViewById(R.id.searchView);
        recView = view.findViewById(R.id.recView);
        manager = new GridLayoutManager(context,3);
        recView.setLayoutManager(manager);

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH)
                {
                    String query = searchView.getText().toString();
                    if (!TextUtils.isEmpty(query))
                    {
                        Common.CloseKeyBoard(context,searchView);
                        Search(query);
                    }else
                        {
                            Common.CloseKeyBoard(context,searchView);
                            Toast.makeText(context, R.string.enter_pro_name, Toast.LENGTH_LONG).show();
                        }
                }
                return false;
            }
        });
    }

    private void Search(String query) {
    }

    public static Fragment_Subdepartment getInstance()
    {
        Fragment_Subdepartment fragment = new Fragment_Subdepartment();
        return fragment;
    }
}
