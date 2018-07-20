package com.semicolon.criuse.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.criuse.Activities.HomeActivity;
import com.semicolon.criuse.R;
import com.semicolon.criuse.Services.Tags;

import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Client_Register extends Fragment {
    private LinearLayout ll_login;
    private HomeActivity homeActivity;
    private CircleImageView image;
    private EditText edt_name,edt_phone,edt_userName,edt_password,edt_re_password;
    private PhoneInputLayout edt_check_phone;
    private Button btn_Register;
    private Bitmap bitmap;
    private String m_name="",m_phone="",m_userName="",m_password="",m_rePassword;
    private String imagePath="";
    private final int IMG_REQ=1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_register,container,false);
        initView(view);
        return view;
    }
    public static Fragment_Client_Register getInstance()
    {
        Fragment_Client_Register fragment = new Fragment_Client_Register();
        return fragment;
    }
    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        ll_login = view.findViewById(R.id.ll_login);
        image = view.findViewById(R.id.image);
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_check_phone = view.findViewById(R.id.edt_check_phone);
        edt_userName = view.findViewById(R.id.edt_user_name);
        edt_password = view.findViewById(R.id.edt_password);
        edt_re_password = view.findViewById(R.id.edt_re_password);
        btn_Register = view.findViewById(R.id.reg_btn);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayLoginLayout(Tags.client_register);
            }
        });
    }

    private void SelectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        getActivity().startActivityForResult(intent.createChooser(intent,"Select photo"),IMG_REQ);
    }

    private void Register() {
        m_name = edt_name.getText().toString();
        m_phone= edt_phone.getText().toString();
        m_userName = edt_userName.getText().toString();
        m_password = edt_password.getText().toString();
        m_rePassword = edt_re_password.getText().toString();
        edt_check_phone.setPhoneNumber(m_phone);
        if (TextUtils.isEmpty(m_name))
        {
            edt_name.setError(getString(R.string.name_req));
        }else if (TextUtils.isEmpty(m_phone))
        {
            edt_name.setError(null);
            edt_phone.setError(getString(R.string.phone_req));
        }else if (!edt_check_phone.isValid())
        {
            edt_name.setError(null);
            edt_phone.setError(getString(R.string.inv_phone));
        }else if (TextUtils.isEmpty(m_userName))
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_userName.setError(getString(R.string.username_req));
        }else if (TextUtils.isEmpty(m_password))
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_userName.setError(null);
            edt_password.setError(getString(R.string.password_req));
        }else if (TextUtils.isEmpty(m_rePassword))
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_userName.setError(null);
            edt_password.setError(null);
            edt_re_password.setError(getString(R.string.re_pass_req));
        }
        else if (!m_password.equals(m_rePassword))
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_userName.setError(null);
            edt_password.setError(null);
            edt_re_password.setError(null);
            edt_password.setText(null);
        }else
            {
                edt_name.setError(null);
                edt_phone.setError(null);
                edt_userName.setError(null);
                edt_password.setError(null);
                edt_re_password.setError(null);
                //////////////////save data////////////
                if (TextUtils.isEmpty(imagePath))
                {

                }else
                    {

                    }
            }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQ && resultCode== Activity.RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                image.setImageBitmap(bitmap);
                getImagePath(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void getImagePath(Uri uri) {
        String [] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor!=null&&cursor.moveToFirst())
        {
            imagePath = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
            Log.e("imagePath",imagePath);
        }
    }
}
