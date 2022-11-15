package com.example.check.repositorio.entidad;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Conexion {
    Context context;

    public Conexion(Context context) {
        this.context = context;
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
