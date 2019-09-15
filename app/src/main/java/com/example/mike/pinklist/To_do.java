
package com.example.mike.pinklist;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceFragment;
import android.provider.*;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mike.pinklist.models.BackHandledFragment;
import com.example.mike.pinklist.models.Store;
import com.google.firebase.database.FirebaseDatabase;

public class To_do extends AppCompatActivity implements Fragment_todo.OnFragmentInteractionListener,
        Scheduler.OnFragmentInteractionListener, Notifications.OnFragmentInteractionListener,Profile.OnFragmentInteractionListener{
    private static final String TAG = "sending" ;
    private static final java.lang.String SELECTED_ITEM = "selected_item" ;
    private FloatingActionButton fb;
    Context context;
    Fragment selectedFragment;
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
             selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //assert getSupportActionBar() != null;
                    //getSupportActionBar().setTitle(R.string.to_do);
                    selectedFragment = Fragment_todo.newInstance();
                    //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_if_arrow_back_1063891);
                    //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    invalidateOptionsMenu();
                    break;
                case R.id.navigation_scheduler:
                    //sets what's going to be on the dashboard page
                    //getSupportActionBar().setTitle(R.string.title_scheduler);
                    selectedFragment = Scheduler.newInstance();
                    invalidateOptionsMenu();
                    break;
                case R.id.navigation_notifications:
                    //getSupportActionBar().setTitle(R.string.title_notifications);
                    selectedFragment = Notifications.newInstance();
                    invalidateOptionsMenu();
                    break;
                case R.id.profile:
                    //getSupportActionBar().setTitle(R.string.title_profile);
                    //sets what's going to be on the dashboard page
                    selectedFragment = Profile.newInstance();
                    invalidateOptionsMenu();
                    break;
            }

            if (selectedFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //content is the ID for frameLayout
                transaction.replace(R.id.content, selectedFragment, "tag");
                        //.addToBackStack(null);
                //when you want to use replace you can addtobackstack() then pop
                //when you add to backstack,you have to popbackstack() to get the old fragment back.
                transaction.commit();
            }
            return true;
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        /*Backend backend = new Backend();
        backend.onCreate();*/
        context = this;
        cancelNotification(getIntent());
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.toolbar);
        my_toolbar.setElevation(0f);
        setSupportActionBar(my_toolbar);
        //my_toolbar.setNavigationIcon(R.drawable.ic_if_arrow_back_1063891);
        getSupportActionBar().setTitle(R.string.to_do);
        //sets the first view to the first fragment
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, Fragment_todo.newInstance(), "tags");
        transaction.commit();

        fb = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(To_do.this, NewTask.class);
                startActivity(intent);
            }
        });
        //BottomNavigationViewHelper b = new BottomNavigationViewHelper();
    }

    //This is for the search button
        /*@Override
        public boolean onCreateOptionsMenu(Menu menu) {
            menu.add(0,900,3,"Post").setIcon(R.drawable.ic_search_black_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if(item.getItemId() == 900){
        }
            if(item.getItemId()== 901){
        }
        return super.onOptionsItemSelected(item);
    }*/
    private void cancelNotification(Intent intent){
        String messages = intent.getStringExtra("messages");
        int code = intent.getIntExtra("code",0);

        if(messages != null){
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), code, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                Vibrator myVib = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
                if (myVib != null){
                    myVib.cancel();
                }
                MediaPlayer mp = MediaPlayer.create(context.getApplicationContext(), (android.provider.Settings.System.DEFAULT_RINGTONE_URI));
                if (mp != null){
                    mp.stop();
                    mp.reset();
                    mp.release();
                    //create same media player instance make it static
                    //media player created in the broadcast receiver might be the cause
                    //Toast.makeText(context, messages, Toast.LENGTH_SHORT).show();
                }
                alarmManager.cancel(pendingIntent);
            }

            Log.d(TAG,messages);
        //Toast.makeText(context,String.valueOf(code),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onBackPressed() {
        //MenuItem homeItem = navigation.getMenu().getItem(0);
        //navigation.getSelectedItemId();
        if (selectedFragment != null && !selectedFragment.equals(Fragment_todo.newInstance())) {
            selectedFragment = Fragment_todo.newInstance();
            invalidateOptionsMenu();
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, selectedFragment,"tags");
            transaction.commit();
            selectedFragment = null;
        } else {
            super.onBackPressed();// also like finishing the activity
        }
    }
    /*@Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
        if (selectedFragment != null && !selectedFragment.equals(Fragment_todo.newInstance())) {
            selectedFragment = Fragment_todo.newInstance();
            invalidateOptionsMenu();
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, selectedFragment,"tags");
            transaction.commit();
            selectedFragment = null;
        } else {
            super.onBackPressed();
        }
    }*/

}
