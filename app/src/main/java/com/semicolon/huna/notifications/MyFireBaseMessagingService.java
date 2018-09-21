package com.semicolon.huna.notifications;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.semicolon.huna.Models.MessageModel;
import com.semicolon.huna.Models.TypingModel;
import com.semicolon.huna.Models.UserModel;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Tags;
import com.semicolon.huna.SharedPreferences.Preferences;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    Preferences preferences = Preferences.getInstance();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String,String> map = remoteMessage.getData();
        for (String key:map.keySet())
        {
            Log.e("Key:",key+"\n"+"Value :"+map.get(key));
        }

        ManageNotification(map,getUser(),getSession(),remoteMessage.getSentTime());
    }

    private void ManageNotification(Map<String, String> map,UserModel user, String session,long not_time) {

        if (session!=null|| !TextUtils.isEmpty(session))
        {
            if (session.equals(Tags.session_login))
            {
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);


                String chat_id = Preferences.getInstance().getChat_id(this);
                String notification_type = map.get("notification_type");

                switch (notification_type)

                {

                    case Tags.not_send_message:
                        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        String className = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
                        if (className.equals("com.semicolon.criuse.Activities.ChatActivity"))
                        {
                            String from_id = map.get("from_user_id");
                            if (from_id.equals(user.getUser_id())||from_id.equals(chat_id))
                            {


                                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");

                                String time = timeFormat.format(new Date(not_time));
                                String date = dateFormat.format(new Date(not_time));

                                String room_id = map.get("room_id");
                                String image = map.get("image");
                                String from_user_name = map.get("from_name");
                                String from_user_id = map.get("from_user_id");
                                String from_user_type = map.get("from_type");
                                String from_user_image = map.get("from_photo");
                                String to_user_name = map.get("to_name");
                                String to_user_id = map.get("'to_user_id");
                                String to_user_type = map.get("to_type");
                                String to_user_image = map.get("to_photo");
                                String msg = map.get("message");
                                String msg_type = map.get("name_message_type");
                                MessageModel messageModel = new MessageModel(room_id,from_user_id,to_user_id,from_user_name,to_user_name,from_user_image,to_user_image,from_user_type,to_user_type,msg,image,msg_type,time,date);
                                EventBus.getDefault().post(messageModel);

                            }else
                                {
                                    //createNot
                                    String from_user_name = map.get("from_name");
                                    String from_user_image = map.get("from_photo");
                                    String msg = map.get("message");
                                    String msg_type = map.get("name_message_type");

                                    CreateNotification(from_user_name,from_user_image,msg,msg_type,manager,builder);

                                }
                        }else
                            {
                                String from_user_name = map.get("from_name");
                                String from_user_image = map.get("from_photo");
                                String msg = map.get("message");
                                String msg_type = map.get("name_message_type");

                                CreateNotification(from_user_name,from_user_image,msg,msg_type,manager,builder);

                                //createNot
                            }
                        break;

                    case Tags.typing:
                        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        String cName = am.getRunningTasks(1).get(0).topActivity.getClassName();
                        if (cName.equals("com.semicolon.criuse.Activities.ChatActivity"))
                        {
                            TypingModel typingModel = new TypingModel(map.get("typing_value"));
                            EventBus.getDefault().post(typingModel);
                        }
                        break;
                }
            }
        }

    }

    private void CreateNotification(String from_user_name, String from_user_image, String msg, String msg_type, NotificationManager manager, NotificationCompat.Builder builder) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {

            String notification_channel_id ="12525362";
            String channel_name ="mchannel12525362";
            int notification_importance =NotificationManager.IMPORTANCE_HIGH;
            String notPath = "android.resource://"+getPackageName()+"/"+ R.raw.not;
            NotificationChannel channel = new NotificationChannel(notification_channel_id,channel_name,notification_importance);
            builder.setSmallIcon(R.mipmap.ic_launcher2);
            channel.setShowBadge(true);
            channel.setSound(Uri.parse(notPath),new AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
            .build()
            );

            manager.createNotificationChannel(channel);

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            Picasso.with(this).load(Tags.IMAGE_URL+from_user_image).into(target);
            builder.setContentTitle(from_user_name);

            if (msg_type.equals(Tags.txt_content_type))
            {
                builder.setContentText(msg);

            }else if (msg_type.equals(Tags.image_content_type))
                {
                    builder.setContentText(getString(R.string.sent_image));

                }

                manager.notify(0,builder.build());

        }else
            {
                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        builder.setLargeIcon(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };

                Picasso.with(this).load(Tags.IMAGE_URL+from_user_image).into(target);
                builder.setSmallIcon(R.mipmap.ic_launcher2);
                builder.setContentTitle(from_user_name);

                if (msg_type.equals(Tags.txt_content_type))
                {
                    builder.setContentText(msg);

                }else if (msg_type.equals(Tags.image_content_type))
                {
                    builder.setContentText(getString(R.string.sent_image));

                }

                manager.notify(0,builder.build());
            }

    }


    private UserModel getUser()
    {
        UserModel userData = preferences.getUserData(this);
        return userData;
    }

    private String getSession()
    {
        String session = preferences.getSession(this);
        return session;
    }
}
