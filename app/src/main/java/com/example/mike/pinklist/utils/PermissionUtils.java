package com.example.mike.pinklist.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {

    public static boolean neverAskAgainSelected(final Context context) {
        //final boolean currShouldShowStatus = activity.shouldShowRequestPermissionRationale();
        return getRatinaleDisplayStatus(context);
    }

    public static void setShouldShowStatus(final Context context) {
        SharedPreferences genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = genPrefs.edit();
        editor.putBoolean("permission", true);
        editor.apply();
    }

    private static boolean getRatinaleDisplayStatus(final Context context) {
        SharedPreferences genPrefs =     context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        return genPrefs.getBoolean("permission", false);
    }
}
