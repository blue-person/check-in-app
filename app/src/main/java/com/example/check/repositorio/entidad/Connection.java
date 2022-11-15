package com.example.check.repositorio.entidad;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection {
    Context context;

    public Connection(Context context) {
        this.context = context;
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            boolean conectadoInternet = (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI);
            boolean conectadoDatos = (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE);
            return conectadoInternet || conectadoDatos;
        } else {
            return false;
        }
    }
}