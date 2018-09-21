package com.semicolon.huna.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.huna.Activities.HomeActivity;
import com.semicolon.huna.Models.ContactsModel;
import com.semicolon.huna.Models.ResponseModel;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Api;
import com.semicolon.huna.Services.Tags;
import com.semicolon.huna.Share.Common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Contactus extends Fragment {

    private ImageView back;
    private ImageView whatsApp_Btn,email_Btn;
    private EditText subject,message;
    private EditText edt_name,edt_email;
    private Button send_Btn;
    private ProgressDialog dialog;
    private EditText edt_phone;
    private PhoneInputLayout edt_check_phone;
    private HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us,container,false);
        initView(view);
        return  view;
    }

    private void initView(View view) {
        homeActivity= (HomeActivity) getActivity();
        back = view.findViewById(R.id.back);
        whatsApp_Btn = view.findViewById(R.id.whatsapp_btn);
        email_Btn = view.findViewById(R.id.email_btn);
        edt_name = view.findViewById(R.id.edt_name);
        edt_email= view.findViewById(R.id.edt_email);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_check_phone = view.findViewById(R.id.edt_check_phone);
        subject = view.findViewById(R.id.subject);
        message = view.findViewById(R.id.message);
        send_Btn = view.findViewById(R.id.send_btn);
        send_Btn.setOnClickListener(view2 ->
                Send(Tags.normal)

        );
        whatsApp_Btn.setOnClickListener(view2 ->
                Send(Tags.whatsapp)
        );
        email_Btn.setOnClickListener(view2 ->
                Send(Tags.email)

        );

        back.setOnClickListener(view1 -> homeActivity.setNavPosition());
        homeActivity.HideFab();

    }

    private void Send(String type) {
        String m_name = edt_name.getText().toString();
        String m_email = edt_email.getText().toString();
        String m_subject = subject.getText().toString();
        String m_message = message.getText().toString();
        String m_phone = edt_phone.getText().toString();
        edt_check_phone.setPhoneNumber(m_phone);
        if (TextUtils.isEmpty(m_name))
        {
            edt_name.setError(getString(R.string.name_req));
        }else if (TextUtils.isEmpty(m_email))
        {
            edt_name.setError(null);
            edt_email.setError(getString(R.string.email_req));
        }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
        {
            edt_name.setError(null);
            edt_email.setError(getString(R.string.inv_email));
        }
        else if (TextUtils.isEmpty(m_phone))
        {
            edt_name.setError(null);
            edt_email.setError(null);
            edt_phone.setError(getString(R.string.phone_req));
        }else if (!edt_check_phone.isValid())
        {
            edt_name.setError(null);
            edt_email.setError(null);
            edt_phone.setError(getString(R.string.inv_phone));

        }
        else if (TextUtils.isEmpty(m_subject))
        {
            edt_name.setError(null);
            edt_email.setError(null);
            edt_phone.setError(null);

            subject.setError(getString(R.string.sub_req));
        }else if (TextUtils.isEmpty(m_message))
        {
            edt_name.setError(null);
            edt_email.setError(null);
            edt_phone.setError(null);

            subject.setError(null);
            message.setError(getString(R.string.msg_req));

        }else
        {
            edt_name.setError(null);
            edt_email.setError(null);
            edt_phone.setError(null);
            subject.setError(null);
            message.setError(null);

            getContacts(type,m_name,m_email,m_subject,m_message,m_phone);
        }

    }

    private void getContacts(String type,String m_name,String m_email, String m_subject, String m_message,String m_phone)
    {
        Api.getServices()
                .getContacts()
                .enqueue(new Callback<ContactsModel>() {
            @Override
            public void onResponse(Call<ContactsModel> call, Response<ContactsModel> response) {
                if (response.isSuccessful())
                {
                    SendData(response.body().getWhatsapp(),response.body().getEmail(),type,m_name,m_email,m_subject,m_message,m_phone);

                }
            }

            @Override
            public void onFailure(Call<ContactsModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void SendData( String whatsapp,String email, String type,String m_name, String m_email, String m_subject, String m_message, String m_phone)
    {
        if (type.equals(Tags.normal))
        {
            dialog = Common.CreateProgressDialog(getActivity(),getString(R.string.sending));
            Map<String,String> map =new HashMap<>();
            map.put("name",m_name);
            map.put("email",m_email);
            map.put("subject",m_subject);
            map.put("message",m_message);
            map.put("phone",m_phone);
            Api.getServices().ContactUs(map)
                    .enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful())
                    {
                        if (response.body().getSuccess()==1)
                        {
                            Toast.makeText(getActivity(), R.string.sended, Toast.LENGTH_SHORT).show();

                            dialog.dismiss();

                        }else if (response.body().getSuccess()==0)
                        {
                            Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("Error",t.getMessage());
                    dialog.dismiss();
                    Toast.makeText(getActivity(),R.string.something_error, Toast.LENGTH_SHORT).show();

                }
            });

        }else if (type.equals(Tags.email))
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{email});
            intent.putExtra(Intent.EXTRA_SUBJECT,m_subject);
            intent.putExtra(Intent.EXTRA_TEXT,m_message);
            PackageManager pm =getActivity().getPackageManager();
            List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
            ResolveInfo best = null;
            for(ResolveInfo info : matches)
            {
                if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                {
                    best = info;

                }
            }

            if (best != null)
            {
                intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

            }

            startActivity(intent);

        }else if (type.equals(Tags.whatsapp))
        {

            if (isWhatsApp_installed())
            {
                String phone_number = whatsapp;
                phone_number = phone_number.replace("+","");
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT,m_name+"\n"+m_message);
                sendIntent.putExtra("jid", phone_number + "@s.whatsapp.net");
                sendIntent.setPackage("com.whatsapp");
                if (sendIntent.resolveActivity(getActivity().getPackageManager()) == null) {
                    Toast.makeText(getActivity(), "Error/n" + "", Toast.LENGTH_SHORT).show();
                    return;
                }
                getActivity().startActivity(sendIntent);
            }else
            {
                Toast.makeText(getActivity(), R.string.wtsnotins, Toast.LENGTH_LONG).show();
            }

        }

    }
    private boolean isWhatsApp_installed()
    {

        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static Fragment_Contactus getInstance()
    {
        Fragment_Contactus fragment = new Fragment_Contactus();
        return fragment;
    }
}
