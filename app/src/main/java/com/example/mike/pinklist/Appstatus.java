package com.example.mike.pinklist;

/**
 * Created by Mike on 10/19/2017.
 */


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


    public class Appstatus {

        private static Context context;
        private boolean connected = false;

        public static Appstatus getInstance(Context ctx) {
            context = ctx.getApplicationContext();
            return new Appstatus();
        }

        boolean isOnline() {
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = null;
                if (connectivityManager != null) {
                    networkInfo = connectivityManager.getActiveNetworkInfo();
                }
                connected = networkInfo != null && networkInfo.isAvailable() &&
                        networkInfo.isConnected();
                return connected;


            } catch (Exception e) {
                System.out.println("CheckConnectivity Exception: " + e.getMessage());
                Log.v("connectivity", e.toString());
            }
            return connected;
        }
    }
