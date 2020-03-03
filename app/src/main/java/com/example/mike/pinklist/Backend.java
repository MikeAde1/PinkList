package com.example.mike.pinklist;

import android.app.Application;

import com.example.mike.pinklist.store.SessionManager;
import com.example.mike.pinklist.models.Store;
import com.example.mike.pinklist.models.Task;
import com.google.firebase.database.FirebaseDatabase;

/**
 *
 * Created by Mike on 10/26/2017.
 */

public class Backend extends Application {

    private static FirebaseDatabase firebaseDatabase;
    SessionManager sessionManager;
    Store store;
    Task task;
    @Override
    public void onCreate() {
        super.onCreate();
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
            store = Store.getInstance();
            store.setTask(task);
            sessionManager = new SessionManager(getApplicationContext());
    }
}
