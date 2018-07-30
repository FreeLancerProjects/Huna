package com.semicolon.criuse.InternetConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection {
    private static Connection instance=null;
    private Connection() {
    }

    public static synchronized Connection getInstance()
    {
        if (instance==null)
        {
            instance = new Connection();
        }
        return instance;

    }
    public boolean getConnction(Context context)
    {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (manager!=null&&activeNetworkInfo!=null&&activeNetworkInfo.isAvailable()&&activeNetworkInfo.isConnected())
        {
            return true;
        }

        return false;
    }
}
