package com.semicolon.huna.Share;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.semicolon.huna.Models.UserModel;
import com.semicolon.huna.R;

import java.io.File;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Common {
    public static void CloseKeyBoard(Context context, View view)
    {
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view;
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(),0);

    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getImagePath(Context context,Uri uri)
    {
        int currentApiVersion;
        try
        {
            currentApiVersion = android.os.Build.VERSION.SDK_INT;
        }
        catch(NumberFormatException e)
        {
            //API 3 will crash if SDK_INT is called
            currentApiVersion = 3;
        }
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {
            String filePath = "";
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst())
            {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }
        else if (currentApiVersion <= Build.VERSION_CODES.HONEYCOMB_MR2 && currentApiVersion >= Build.VERSION_CODES.HONEYCOMB)

        {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null)
            {
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }
        else
        {

            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }
    public static AlertDialog CreateAlertDialog(Context context , String msg)
    {
        AlertDialog dialog = new SpotsDialog.Builder()
                .setCancelable(true)
                .setContext(context)
                .setMessage(msg)
                .build();

        return dialog;
    }
    public static ProgressDialog CreateProgressDialog(Context context , String msg)
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


    public static RequestBody getRequestBodyFromData(String request,String mediaType)
    {
        RequestBody  requestBody = RequestBody.create(MediaType.parse(mediaType),request);
        return requestBody;
    }

    public static MultipartBody.Part getMultipart(Context context,Uri uri)
    {
        File file = getFileFromUrl(context,uri);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part part =MultipartBody.Part.createFormData("image",file.getName(),requestBody);
        return part;
    }

}
