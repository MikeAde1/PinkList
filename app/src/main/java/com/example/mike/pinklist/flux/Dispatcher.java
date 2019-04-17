package com.example.mike.pinklist.flux;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mike.pinklist.Backend;
import com.example.mike.pinklist.adapters.TaskAdapter;
import com.example.mike.pinklist.models.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 3/13/2018.
 */

public class Dispatcher {
    public static final String TAG = "header";
    public static final String DispatcherBroadCast = "BACKEND_BROADCAST_UPDATE";
    private Context context;
    //backend is  for all firebase authentications
    public Dispatcher(Context context){
        this.context = context;
    }
    //used in signIn class
    public enum Broadcast {
        ACTION_DELETE_ITEM
    }

    public void sendBroadcast(Broadcast type, String message){
        Log.e("dispatcher","Broadcast::"+type);
        Intent intent = new Intent(DispatcherBroadCast);
        intent.putExtra("broadcast_type",type);
        intent.putExtra("message",message);
        context.sendBroadcast(intent);
    }
}
