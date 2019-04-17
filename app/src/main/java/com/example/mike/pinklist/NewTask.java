package com.example.mike.pinklist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mike.pinklist.models.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;


public class NewTask extends AppCompatActivity {
                private static FirebaseDatabase fdatabase;
                DatePickerDialog datePickerDialog;
                private static TextView textView2;
                AlarmManager alarmmanager;
                private PendingIntent pendingintent;
                String msg;
                EditText writetodo;
                Button writedate;
                private Spinner writepriority;
                private SwitchCompat alarm, notification;
                boolean me;
                //boolean alarmValue = false;
                boolean notificationValue = true;
                static Calendar calendar;
                Task task;
                DatabaseReference databaseRef;
                FirebaseAuth firebaseAuth;
                int mHour,mMinute,mYear,mMonth,mDay;
                String date,d;
                final String title = "Task Notification Alert (Tap to cancel)";
                long l;
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_newtask);

                    fdatabase = FirebaseDatabase.getInstance();
                    databaseRef = fdatabase.getReference("users");
                    firebaseAuth = FirebaseAuth.getInstance();

                    writetodo = (EditText) findViewById(R.id.textView13);
                    writepriority = (Spinner) findViewById(R.id.spinner);
                    alarm = (SwitchCompat) findViewById(R.id.switch1);
                    notification = (SwitchCompat) findViewById(R.id.switch2);
                    alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            //helps to check if user has checked the alarm
                            me=isChecked;
                            //Toast.makeText(getApplicationContext(),String.valueOf(me),Toast.LENGTH_LONG).show();
                        }
                    });
                    notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            //helps to check if user has checked the notification
                            notificationValue = isChecked;
                        }
                    });
                    Typeface t = Typeface.createFromAsset(getAssets(),"fonts/NotoSans-Bold.ttf");
                    writetodo.setTypeface(t);

                    //Bundle bundle = new Bundle();
                    //bundle.putString("message", task.getContent());
                    //Scheduler obj = new Scheduler();
                    //obj.setArguments(bundle);
                    calendar = Calendar.getInstance();
                    Toolbar mytoolbar = (Toolbar) findViewById(R.id.toolbars);
                    mytoolbar.setTitle("NEW TASK");
                    setSupportActionBar(mytoolbar);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_if_arrow_back_1063891);

                    // Create an ArrayAdapter using the string array and a default spinner layout
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array,
                            R.layout.spinner_item);
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    writepriority.setAdapter(adapter);

                    writedate = (Button) findViewById(R.id.editText);
                    // perform click event on edit text
                    writedate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // calender class's instance and get current date , month and year from calender
                             mYear = calendar.get(Calendar.YEAR); // current year
                             mMonth = calendar.get(Calendar.MONTH); // current month
                             mDay = calendar.get(Calendar.DAY_OF_MONTH); // current day
                            // date picker dialog
                            datePickerDialog = new DatePickerDialog(NewTask.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    // set day of month , month and year value in the edit text
                                    date = (dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    SimpleDateFormat inputDateformat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                    SimpleDateFormat dateformate = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.ENGLISH);
                                    try {
                                        d = dateformate.format(inputDateformat.parse(date));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    timePicker();
                                }
                            }, mYear, mMonth, mDay);
                            datePickerDialog.show();
                        }
                    });
                }
                private void timePicker() {
                        // Get Current Time
                     mHour = calendar.get(Calendar.HOUR_OF_DAY);
                     mMinute = calendar.get(Calendar.MINUTE);
                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        mHour = hourOfDay;
                                        mMinute = minute;
                                        date = date +" "+hourOfDay+":"+minute;
                                        writedate.setText(d +" at "+hourOfDay+":"+minute);
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();
                    }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                public void saveText(View view) {
                    //DateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
                    //String st = spd.format(c.getTime());
                    //Log.i("present", c.getTime().toString());
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.ENGLISH);
                    if (date != null){
                        try {
                                Date dat = formatter.parse(date);//converts string date to normal time
                                l = dat.getTime();//gets the time in milliseconds
                                if ( dat.before(new Date())){
                                    Toast.makeText(getApplicationContext(),"This date is past!",Toast.LENGTH_SHORT).show();
                                    return;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }} else {
                        return;
                    }
                    msg = writetodo.getText().toString();
                    if( TextUtils.isEmpty(msg)){
                        writetodo.setError("Required");
                        return;
                    }
                    if(writedate.getText().toString().equals("")){
                        Toast.makeText(NewTask.this, "Date is required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    String s = sp.format(l);
                    String u = String.valueOf(l);//converts long to string

                final String option = writepriority.getSelectedItem().toString();
                Random rand = new Random();
                int value = rand.nextInt(50);
                task = new Task();
                task.setContent(msg);
                task.setCompleted_by(u);
                task.setDate(s);
                task.setTime(value);
                task.setTask_priority(option);
                task.setSave_as_alarm(me);
                task.setToday_date(me());
                task.setShow_as_notification(notificationValue);
                task.setCompleted(false);
                databaseRef.child(firebaseAuth.getCurrentUser().getUid()).child("task_list").push().setValue(task);
                ChooseAlertOptions(option,value);
                finish();
            }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void ChooseAlertOptions(String option, int value) {
        switch (option) {
            case "High":
                //true
                if (me)
                    setAlarm(value);
                if (!me && notificationValue)
                    setNotification("High",value);
                if (!me && !notificationValue )
                    setNotification("Medium",value);
                break;
            case "Medium":
                if (me)
                    setMediumAlarm(value);
                if (!me && notificationValue)
                    setNotification("Medium",value);
                if (!me && !notificationValue )
                    setNotification("Medium",value);
                break;
            case "Low":
                if (me)
                    setLowAlarm(value);
                if (!me && notificationValue)
                    setNotification("Low",value);
                if (!me && !notificationValue )
                    setNotification("Medium",value);
                break;
        }
    }

    private void setNotification(String option, int value) {
        Intent myIntent = new Intent(NewTask.this, AlarmReceiver.class);
        myIntent.putExtra ("message", msg);
        myIntent.putExtra ("title", title);
        myIntent.putExtra("value",value);
        pendingintent = PendingIntent.getBroadcast(NewTask.this, value, myIntent, 0);
        alarmmanager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmmanager != null;
        switch (option) {
            case "High":
                alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP,
                        l, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingintent);
                break;
            case "Medium":
                alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP,
                        l, AlarmManager.INTERVAL_HALF_HOUR, pendingintent);
                break;
            case "Low":
                alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP,
                        l, AlarmManager.INTERVAL_HOUR, pendingintent);
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setAlarm(int value) {
        Intent myIntent = new Intent(NewTask.this, AlarmReceiver.class);
        myIntent.putExtra ("notification", "notification");
        myIntent.putExtra ("message", msg);
        myIntent.putExtra ("title", title);
        //myIntent.putExtra("value",value);
        pendingintent = PendingIntent.getBroadcast(NewTask.this, value, myIntent, 0);
        alarmmanager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmmanager != null;
        alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP,
                l, AlarmManager.INTERVAL_FIFTEEN_MINUTES,pendingintent);
    }

    private void setLowAlarm(int value){
        Intent myIntent = new Intent(NewTask.this, AlarmReceiver.class);
        myIntent.putExtra ("notification", "notification");
        myIntent.putExtra ("message", task.getContent());
        myIntent.putExtra ("title", title);
        pendingintent = PendingIntent.getBroadcast(NewTask.this, value, myIntent, 0);
        alarmmanager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmmanager != null;
        alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP,
                l, AlarmManager.INTERVAL_HOUR, pendingintent);
    }

    private void setMediumAlarm(int value) {
        Intent myIntent = new Intent(NewTask.this, AlarmReceiver.class);
        myIntent.putExtra ("notification", "notification");
        myIntent.putExtra ("message", msg);
        myIntent.putExtra ("title", title);
        pendingintent = PendingIntent.getBroadcast(NewTask.this, value, myIntent,0);
        alarmmanager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmmanager != null;
        alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP,l
                , AlarmManager.INTERVAL_HALF_HOUR, pendingintent);
     }

    private static String me(){
                     Calendar cal = Calendar.getInstance();
                     DateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
                     String s = spd.format(cal.getTime());
                     Log.i("present", cal.getTime().toString());
                     return s;
            }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
