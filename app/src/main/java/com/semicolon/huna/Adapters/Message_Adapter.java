package com.semicolon.huna.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolon.huna.Activities.ChatActivity;
import com.semicolon.huna.Models.ChatModel;
import com.semicolon.huna.Models.MessageModel;
import com.semicolon.huna.R;
import com.semicolon.huna.Services.Tags;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Delta on 19/04/2018.
 */

public class Message_Adapter extends RecyclerView.Adapter {

    private final int txt_right=0;
    private final int txt_left =1;
    private final int img_right=2;
    private final int img_left =3;

    private Context context;
    private List<MessageModel> messageModelList;
    private ChatActivity activity;
    private ChatModel chatModel;

    public Message_Adapter(Context context, List<MessageModel> messageModelList, ChatModel chatModel) {
        this.context = context;
        this.activity = (ChatActivity) context;
        this.messageModelList = messageModelList;
        this.chatModel =chatModel;
        Log.e("msff",messageModelList.get(0).getFrom_id()+"__");
        Log.e("id",chatModel.getCurr_id()+"__");

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == txt_right)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.msg_txt_right,parent,false);
            return new myViewHolder_txtRight(view);
        }else if (viewType == img_right)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.msg_img_right,parent,false);
            return new myViewHolder_imgRight(view);
        }
        else if (viewType == txt_left)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.msg_txt_left,parent,false);

            return new myViewHolder_txtLeft(view);
        }else
            {
                View view = LayoutInflater.from(context).inflate(R.layout.msg_img_left,parent,false);

                return new myViewHolder_imgLeft(view);
            }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();

        if (viewType == txt_right)
        {
            MessageModel messageModel = messageModelList.get(position);

            ((myViewHolder_txtRight)holder).BindData(messageModel);

        }else if (viewType == img_right)
        {
            MessageModel messageModel = messageModelList.get(position);

            ((myViewHolder_imgRight)holder).BindData(messageModel);
        }else if (viewType == txt_left)
        {
            MessageModel messageModel = messageModelList.get(position);

            ((myViewHolder_txtLeft)holder).BindData(messageModel);
        }else if (viewType == img_left)
        {
            MessageModel messageModel = messageModelList.get(position);

            ((myViewHolder_imgLeft)holder).BindData(messageModel);
        }
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class myViewHolder_txtRight extends RecyclerView.ViewHolder{
        TextView msg,time;
        CircleImageView user_image;
        public myViewHolder_txtRight(View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msg);
            time = itemView.findViewById(R.id.time);
            user_image = itemView.findViewById(R.id.user_image);
        }

        public void BindData(final MessageModel messageModel)
        {
            Log.e("right","txtright");


            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+chatModel.getCurr_image())).into(user_image);

            /*if (chatModel.getChat_type().equals(Tags.user_type_client))
            {
                user_type.setText("عميل");
            }else if (chatModel.getChat_type().equals(Tags.user_type_driver))
            {
                user_type.setText("سائق");

            }else if (chatModel.getChat_type().equals(Tags.user_type_grocery))
            {
                user_type.setText("بقالة");

            }*/
            msg.setText(messageModel.getMessage());
            time.setText(messageModel.getMessage_time());
        }
    }
    public class myViewHolder_txtLeft extends RecyclerView.ViewHolder{
        TextView msg,time;
        CircleImageView user_image;

        public myViewHolder_txtLeft(View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msg);
            time = itemView.findViewById(R.id.time);
            user_image = itemView.findViewById(R.id.user_image);
        }
        public void BindData(final MessageModel messageModel)
        {

            /*if (chatModel.getChat_type().equals(Tags.user_type_client))
            {
                user_type.setText("عميل");
            }else if (chatModel.getChat_type().equals(Tags.user_type_driver))
            {
                user_type.setText("سائق");

            }else if (chatModel.getChat_type().equals(Tags.user_type_grocery))
            {
                user_type.setText("بقالة");

            }*/
            msg.setText(messageModel.getMessage());
            time.setText(messageModel.getMessage_time());
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+chatModel.getChat_image())).into(user_image);

        }
    }

    public class myViewHolder_imgRight extends RecyclerView.ViewHolder implements View.OnClickListener{
        Target target;
        CircleImageView user_image;
        TextView time,msg;
        ImageView img,downLoad_btn;

        public myViewHolder_imgRight(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            msg = itemView.findViewById(R.id.msg);
            user_image = itemView.findViewById(R.id.user_image);
            img = itemView.findViewById(R.id.image);
            downLoad_btn = itemView.findViewById(R.id.downLoad_btn);
            downLoad_btn.setOnClickListener(this);

        }
        public void BindData(MessageModel messageModel)
        {

            if (!messageModel.getMessage().equals("0"))
            {
                msg.setVisibility(View.VISIBLE);
                msg.setText(messageModel.getMessage());
            }else
                {
                    msg.setVisibility(View.GONE);
                }
            time.setText(messageModel.getMessage_time());

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+chatModel.getCurr_image())).into(user_image);

            target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    img.setImageBitmap(resizeBitmap(bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+messageModel.getImage())).into(target);

        }

        @Override
        public void onClick(View view) {
            MessageModel messageModel = messageModelList.get(getAdapterPosition());
            activity.setPosTodownloadImage(Tags.IMAGE_URL+messageModel.getImage());
        }
    }
    public class myViewHolder_imgLeft extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Target target;

        CircleImageView user_image;
        TextView time,msg;
        ImageView img,downLoad_btn;
        public myViewHolder_imgLeft(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            msg = itemView.findViewById(R.id.msg);

            user_image = itemView.findViewById(R.id.user_image);
            img = itemView.findViewById(R.id.image);
            downLoad_btn = itemView.findViewById(R.id.downLoad_btn);
            downLoad_btn.setOnClickListener(this);

        }
        public void BindData(MessageModel messageModel)
        {


            time.setText(messageModel.getMessage_time());
            if (!messageModel.getMessage().equals("0"))
            {
                msg.setVisibility(View.VISIBLE);
                msg.setText(messageModel.getMessage());
            }else
            {
                msg.setVisibility(View.GONE);
            }
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+chatModel.getChat_image())).into(user_image);

            target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    img.setImageBitmap(resizeBitmap(bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+messageModel.getImage())).into(target);

        }

        @Override
        public void onClick(View view) {
            MessageModel messageModel = messageModelList.get(getAdapterPosition());
            activity.setPosTodownloadImage(Tags.IMAGE_URL+messageModel.getImage());

        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.e("messageModelList",messageModelList.get(position).getFrom_id()+"___");
        if (messageModelList.get(position).getFrom_id().equals(chatModel.getCurr_id()))
        {
            if (messageModelList.get(position).getMessage_type().equals(Tags.txt_content_type))
            {
                return txt_right;
            }else
                {
                    return img_right;
                }
        }else
            {
                if (messageModelList.get(position).getMessage_type().equals(Tags.txt_content_type))
                {
                    return txt_left;
                }else
                {
                    return img_left;
                }
            }


    }


    private Bitmap resizeBitmap(Bitmap bitmap)
    {
        int maxWidth = 480;
        int maxHeight=720;

        float scale = Math.min(((float)maxHeight / bitmap.getWidth()), ((float)maxWidth / bitmap.getHeight()));
        Matrix matrix = new Matrix();
        matrix.postScale(scale,scale);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return bitmap1;
    }
}
