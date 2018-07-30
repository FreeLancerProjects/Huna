package com.semicolon.criuse.Share;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.semicolon.criuse.Models.UserModel;
import com.semicolon.criuse.R;

import java.io.File;

import dmax.dialog.SpotsDialog;

public class Common {
    public static void CloseKeyBoard(Context context, View view)
    {
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view;
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(),0);

    }
    public static String getImagePath(Context context,Uri uri)
    {
        String [] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor!=null&&cursor.moveToFirst())
        {
            return cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));

        }
        return null;
    }
    public static AlertDialog  CreateProgressDialog(Context context , String msg)
    {
        AlertDialog progressDialog = new SpotsDialog.Builder()
                .setCancelable(true)
                .setContext(context)
                .setMessage(msg)
                .build();

        return progressDialog;
    }
    public static ProgressDialog CreateProgressDialog2(Context context , String msg)
    {
        ProgressBar progressBar = new ProgressBar(context);
        Drawable drawable = progressBar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog .setCancelable(true);
        progressDialog.setMessage(msg);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminateDrawable(drawable);
        progressDialog.show();
        return progressDialog;
    }
    public static String ConvertModelToGson(UserModel userModel)
    {
        Gson gson = new Gson();
        String model = gson.toJson(userModel);
        return model;
    }
    public static File getFileFromUrl(Context context , Uri uri)
    {
        String FullPath = getImagePath(context,uri);

        File file = new File(FullPath);

        return file;
    }


}
