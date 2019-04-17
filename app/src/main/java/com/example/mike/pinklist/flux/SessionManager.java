package com.example.mike.pinklist.flux;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.mike.pinklist.Login;

/**
 * Created by Mike on 3/21/2018.
 */

public class SessionManager {
    private static final String PREF_NAME = "final_class";
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    public void checkVibrationStatus(){
        editor.putBoolean("vibrate",true);
        editor.apply();
    }
    public void setVibrationStatus(){
        editor.putBoolean("vibrate",false);
        editor.apply();
    }
    public boolean getVibrationStatus(){
        return pref.getBoolean("vibrate",false);
    }

    public void trueStatus() {
        editor.putBoolean("complete",true);
        editor.apply();
    }

    public void falseStatus() {
        editor.putBoolean("complete",false);
        editor.apply();
    }

    public boolean getStatus() {
        return pref.getBoolean("complete",false);
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Logging Activity
        //Backend.getInstance().signOut();
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        // Add new Flag to start new Activity
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //FirebaseAuth.getInstance().signOut();
        // Staring Login Activity
        _context.startActivity(i);
    }
}

