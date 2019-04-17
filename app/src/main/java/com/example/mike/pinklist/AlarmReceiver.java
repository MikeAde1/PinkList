    package com.example.mike.pinklist;

    import android.annotation.TargetApi;
    import android.app.Notification;
    import android.app.NotificationManager;
    import android.app.PendingIntent;
    import android.content.BroadcastReceiver;
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.media.MediaPlayer;
    import android.media.Ringtone;
    import android.media.RingtoneManager;
    import android.media.audiofx.BassBoost;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Vibrator;
    import android.provider.Settings;
    import android.support.annotation.RequiresApi;
    import android.support.v7.app.NotificationCompat;
    import android.support.v4.app.NotificationManagerCompat;
    import android.support.v4.content.WakefulBroadcastReceiver;
    import android.support.v7.app.NotificationCompat.Builder;
    import android.util.Log;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.example.mike.pinklist.flux.SessionManager;
    import com.example.mike.pinklist.models.Task;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.ChildEventListener;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;
    import java.util.List;

    import static android.R.id.message;
    import static android.content.Context.MODE_PRIVATE;
    import static android.content.Context.NOTIFICATION_SERVICE;
    import static android.content.Context.VIBRATOR_SERVICE;
    import static com.example.mike.pinklist.Profile.FILENAME;

    /**
     * Created by Mike on 10/28/2017.
     */
    public class AlarmReceiver extends BroadcastReceiver {
        public static final int NOTIFICATION_ID = 0;
        boolean isVibrate ;
        //NotificationManager manager;
        Notification noti;
        Builder nb;
        List<Task> list = new ArrayList<>();
        SessionManager sessionManager;
        String notification, title;
        String msg;
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(final Context context, Intent intent) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            DatabaseReference databaseRef = database.getReference("users");
            notification = intent.getStringExtra("notification");
            msg = intent.getStringExtra("message");
            int value = intent.getIntExtra("value", 0);
            title = intent.getStringExtra("title");
            databaseRef.child(firebaseAuth.getCurrentUser().getUid()).child("task_list").orderByChild("content").
                    equalTo(msg).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null){
                        setNotification(msg,context);
                }}

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            }
       /* private Uri getAlarmUri() {
            Uri alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alert == null) {
                    alert = RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
              if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
              }
            }
            return alert;
        }*/
       private void setNotification(String msg, Context context){
            if (msg.length() > 20)
                msg = msg.substring(0,20)+"...";
            //call firebase and check if data is still in the database
            //icon to use will depend on the logo used for the app
            sessionManager = new SessionManager(context);
            isVibrate = sessionManager.getVibrationStatus();
            if(isVibrate) {
                Vibrator myVib = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                assert myVib != null;
                myVib.vibrate(new long[]{ 500,500,500,500,500,500,500,500 },2);
            }
            if (notification != null){
                MediaPlayer mp = MediaPlayer.create(context, (android.provider.Settings.System.DEFAULT_RINGTONE_URI));
                mp.start();
            }
            //System.out.print(String.valueOf(f));
            Intent notIntent = new Intent(context, To_do.class);
            //clears the activity that started this activity, and also refre
            notIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);//check the reorder flag
            notIntent.putExtra("messages", msg);
            PendingIntent pIntent = PendingIntent.getActivity(context, 123, notIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            //Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = (Builder) new Builder(context).setAutoCancel(false)
                    .setContentIntent(pIntent)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.logo).setContentText(msg);
           /*long [] longs = {500, 500, 500, 500, 500, 500, 500, 500};
           if (isVibrate){o
               builder.setVibrate(longs);
           }*/
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(0,builder.build());
            }
        }
       }
