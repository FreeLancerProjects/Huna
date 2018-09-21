package com.semicolon.huna.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.huna.Adapters.Message_Adapter;
import com.semicolon.huna.Models.ChatModel;
import com.semicolon.huna.Models.MessageModel;
import com.semicolon.huna.Models.TypingModel;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Api;
import com.semicolon.huna.Services.Tags;
import com.semicolon.huna.Share.Common;
import com.semicolon.huna.SharedPreferences.Preferences;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pl.tajchert.waitingdots.DotsTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ChatModel chatModel;
    private LinearLayout ll_back,ll_typing;
    private CircleImageView image,chat_image_circle;
    private TextView tv_name;
    private ImageView upload_imageBtn,send_imageBtn;
    private EditText ed_msg;
    private DotsTextView tv_dot;
    private ProgressBar progBar;
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<MessageModel> messageModelList;
    private final String readPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int PER_WRITE_REQ=122;
    private final int IMG_REQ=1995;
    private final int PER_REQ=1232;
    private Uri uri;
    private AlertDialog bill_alert;
    private String url;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        EventBus.getDefault().register(this);
        initView();
        getDataFromIntent();
        displayMessages();


    }



    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            chatModel = (ChatModel) intent.getSerializableExtra("data");
            Log.e("chatid",chatModel.getCurr_id()+"___");
            preferences.CreateUpdateChat_id(this,chatModel.getChat_id());
            UpdateUI(chatModel);
        }
    }

    private void UpdateUI(ChatModel chatModel) {

        Picasso.with(this).load(Tags.IMAGE_URL+Uri.parse(chatModel.getChat_image())).into(image);
        Picasso.with(this).load(Tags.IMAGE_URL+Uri.parse(chatModel.getChat_image())).into(chat_image_circle);
        String name = chatModel.getChat_name().trim();
        name = name.replaceAll("\n","");
        name = name.replaceAll("\t","");
        tv_name.setText(name);


    }

    private void initView()
    {
        preferences = Preferences.getInstance();
        messageModelList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ll_back = findViewById(R.id.ll_back);
        image = findViewById(R.id.image);
        tv_name = findViewById(R.id.tv_name);
        upload_imageBtn = findViewById(R.id.upload_imageBtn);
        send_imageBtn = findViewById(R.id.send_imageBtn);
        ed_msg = findViewById(R.id.ed_msg);
        chat_image_circle = findViewById(R.id.chat_image_circle);
        tv_dot = findViewById(R.id.tv_dot);
        ll_typing = findViewById(R.id.ll_typing);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recView.setLayoutManager(manager);

        progBar = findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send_imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = ed_msg.getText().toString();
                if (!TextUtils.isEmpty(msg))
                {
                    SendTextMessage(msg);
                }
            }
        });

        upload_imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckReadPermission();
            }
        });

        ed_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (ed_msg.getText().toString().length()>0)
                {
                    changeTypingState(Tags.typing);

                }else
                {
                    changeTypingState(Tags.end_typing);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
    private void changeTypingState(String state)
    {
        Map<String,String> map = new HashMap<>();
        map.put("from_id",chatModel.getCurr_id());
        map.put("to_id",chatModel.getChat_id());
        map.put("typing_value",state);

        Api.getServices()
                .typing(chatModel.getRoom_id(),map)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            Log.e("typing","success");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Error_typing",t.getMessage());

                    }
                });
    }
    private void CheckReadPermission()
    {
        if (ContextCompat.checkSelfPermission(this,readPermission)== PackageManager.PERMISSION_GRANTED)
        {
            SelectImage();
        }else
            {
                String [] permissions = new String[]{readPermission};
                ActivityCompat.requestPermissions(this,permissions,PER_REQ);
                SelectImage();

            }
    }
    private void SelectImage()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,getString(R.string.sel_image)),IMG_REQ);
    }
    private void displayMessages()
    {
        Api.getServices()
                .getAllMessage(chatModel.getRoom_id())
                .enqueue(new Callback<List<MessageModel>>() {
                    @Override
                    public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);
                            if (response.body().size()>0)
                            {
                                Log.e("msgmodl",response.body().get(0).getFrom_id()+"__");
                                messageModelList.clear();
                                messageModelList.addAll(response.body());
                                adapter = new Message_Adapter(ChatActivity.this,messageModelList,chatModel);
                                recView.setAdapter(adapter);
                                recView.scrollToPosition(messageModelList.size()-1);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(ChatActivity.this,R.string.something_error, Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void SendTextMessage(String msg)
    {
        Map<String,String> map = new HashMap<>();
        map.put("from_id",chatModel.getCurr_id());
        map.put("to_id",chatModel.getChat_id());
        map.put("message",msg);
        map.put("message_type",Tags.txt_content_type);
        ed_msg.setText("");
        Api.getServices()
                .sendMessage_text(chatModel.getRoom_id(),map)
                .enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        if (response.isSuccessful())
                        {
                            AddNewMessage(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(ChatActivity.this,R.string.something_error, Toast.LENGTH_SHORT).show();

                    }
                });

    }
    private void SendImageMessage(Uri uri,String msg)
    {
        RequestBody from_id_part = Common.getRequestBodyFromData(chatModel.getCurr_id(), "text/plain");
        RequestBody to_id_part = Common.getRequestBodyFromData(chatModel.getChat_id(), "text/plain");
        RequestBody msg_part = Common.getRequestBodyFromData(msg, "text/plain");
        RequestBody msg_type_part = Common.getRequestBodyFromData(Tags.image_content_type, "text/plain");
        MultipartBody.Part image_part = Common.getMultipart(this,uri);
        Api.getServices()
                .sendMessage_image(chatModel.getRoom_id(),from_id_part,to_id_part,msg_part,msg_type_part,image_part)
                .enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        if (response.isSuccessful())
                        {
                            AddNewMessage(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {

                    }
                });

    }
    private void AddNewMessage(MessageModel messageModel) {
        messageModelList.add(messageModel);
        if (adapter==null)
        {
            adapter = new Message_Adapter(this,messageModelList,chatModel);
            recView.setAdapter(adapter);
        }
        adapter.notifyItemInserted(messageModelList.size()-1);
        recView.scrollToPosition(messageModelList.size()-1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieveNewMessge(MessageModel messageModel)
    {
        AddNewMessage(messageModel);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListingForTyping(TypingModel typingModel)
    {
        MediaTask mediaTask = new MediaTask();

        if (typingModel.getType_value().equals(Tags.typing))
        {
            mediaTask.execute();
            ll_typing.setVisibility(View.VISIBLE);

        }else if (typingModel.getType_value().equals(Tags.end_typing))
        {
            mediaTask.cancel(true);
            ll_typing.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQ && resultCode==RESULT_OK && data!=null)
        {
            uri = data.getData();

            if (chatModel.getCurr_type().equals(Tags.user_type_client))
            {
                SendImageMessage(uri,"0");
            }else
            {
                CreateBillDialog(uri);
            }
        }
    }

    private void CreateBillDialog(Uri uri) {
        bill_alert = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();
        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_bill_layout,null);
        ImageView image = view.findViewById(R.id.image);
        EditText edt_order_cost = view.findViewById(R.id.edt_order_cost);
        EditText edt_delivery_cost = view.findViewById(R.id.edt_delivery_cost);
        TextView tv_total = view.findViewById(R.id.tv_total);
        Button btn_send = view.findViewById(R.id.btn_send);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            image.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        edt_order_cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int order_cost = Integer.parseInt(String.valueOf(s));
                String delivery_cost = edt_delivery_cost.getText().toString();
                if (!TextUtils.isEmpty(delivery_cost))
                {
                    int total = order_cost+Integer.parseInt(delivery_cost);
                    tv_total.setText(String.valueOf(total));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_delivery_cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int delivery_cost = Integer.parseInt(String.valueOf(s));
                String order_cost = edt_order_cost.getText().toString();
                if (!TextUtils.isEmpty(order_cost))
                {
                    int total = delivery_cost+Integer.parseInt(order_cost);
                    tv_total.setText(String.valueOf(total));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String order_cost = edt_order_cost.getText().toString();
                String delivery_cost = edt_delivery_cost.getText().toString();

                if (TextUtils.isEmpty(order_cost))
                {
                    Toast.makeText(ChatActivity.this, R.string.enter_ord_cost, Toast.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(delivery_cost))
                {
                    Toast.makeText(ChatActivity.this, R.string.enter_del_cost, Toast.LENGTH_LONG).show();

                }else
                    {
                        String total = tv_total.getText().toString();
                        String msg = getString(R.string.order_cost)+" "+order_cost+" "+getString(R.string.sar)+"\n"+getString(R.string.delivery)+" "+delivery_cost+" "+getString(R.string.sar)+"\n"+getString(R.string.total)+" "+total+" "+getString(R.string.sar);
                        bill_alert.dismiss();

                        SendImageMessage(uri,msg);
                    }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bill_alert.dismiss();
            }
        });

        bill_alert.setView(view);
        bill_alert.show();

    }

    public void setPosTodownloadImage(String url)
    {
        this.url = url;
        checkWritePermission();
    }

    private void checkWritePermission() {
        if (ContextCompat.checkSelfPermission(this,writePermission)==PackageManager.PERMISSION_GRANTED)
        {
            StartDownload();
        }else
            {
                String [] per = {writePermission};
                ActivityCompat.requestPermissions(this,per,PER_WRITE_REQ);
                StartDownload();

            }
    }

    private void StartDownload() {
        Toast.makeText(this, R.string.start_down, Toast.LENGTH_SHORT).show();
        new AsynTask().execute(this.url);
    }

    private class AsynTask extends AsyncTask<String,Void,String>{
        private InputStream inputStream;
        private OutputStream outputStream;
        HttpURLConnection httpURLConnection;
        private URL url;
        private int c;
        @Override
        protected String doInBackground(String... strings) {
            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();
                httpURLConnection.connect();

                File dir = new File(Environment.getExternalStorageDirectory(),"/Cruise");
                if (!dir.exists())
                {
                    dir.mkdirs();
                }

                File imgFile = new File(dir,System.currentTimeMillis()+".png");
                outputStream = new FileOutputStream(imgFile);
                while ((c=inputStream.read())!=-1)
                {
                    outputStream.write(c);
                }

                outputStream.flush();
                return "1";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (inputStream!=null)
                    {
                        inputStream.close();

                    }

                    if (outputStream!=null)
                    {
                        outputStream.close();

                    }

                    if (httpURLConnection!=null)
                    {
                        httpURLConnection.disconnect();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "0";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("1"))
            {
                Toast.makeText(ChatActivity.this, R.string.im_save, Toast.LENGTH_SHORT).show();
            }else
                {
                    Toast.makeText(ChatActivity.this,R.string.failed, Toast.LENGTH_SHORT).show();
                }
        }
    }

    private class MediaTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            String path = "android.resource://"+getPackageName()+"/"+R.raw.typing;
            MediaPlayer mediaPlayer = MediaPlayer.create(ChatActivity.this,R.raw.typing);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.release();
                }
            });
            return null;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PER_WRITE_REQ)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    StartDownload();

                }else
                    {
                        return;
                    }
            }else
                {
                    String [] per = {writePermission};
                    ActivityCompat.requestPermissions(this,per,PER_WRITE_REQ);

                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
