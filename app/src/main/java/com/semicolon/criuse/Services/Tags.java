package com.semicolon.criuse.Services;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

public class Tags {

    public static void CloseKeyBoard(Context context, View view)
    {
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view;
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(),0);

    }
}
