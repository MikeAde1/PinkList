package com.example.mike.pinklist.ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mike.pinklist.R;
import com.example.mike.pinklist.models.Task;
import com.example.mike.pinklist.utils.AlarmReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;


public class NewTask extends AppCompatActivity {
    private static FirebaseDatabase fdatabase;
    DatePickerDialog datePickerDialog;
    AlarmManager alarmmanager;
    private PendingIntent pendingintent;
    String msg, key;
    EditText writetodo;
    Button writedate;
    private Spinner writepriority;
    private SwitchCompat alarm, notification;
    boolean checked = false;
    boolean notificationValue = true;
    static Calendar calendar;
    Task task;
    DatabaseReference databaseRef;
    FirebaseAuth firebaseAuth;
    int mHour,mMinute,mYear,mMonth,mDay;
    String date,d;
    final String title = "Task Notification Alert (Tap to cancel)";
    long timeInMiliis;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtask);
        calendar = Calendar.getInstance();
        initFireBase();
        initViews();
        checkedChangeListeners();
        Typeface t = Typeface.createFromAsset(getAssets(),"fonts/NotoSans-Bold.ttf");
        writetodo.setTypeface(t);
        setToolBar();
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array,
                R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        writepriority.setAdapter(adapter);
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

    private void setToolBar() {
        Toolbar mytoolbar = findViewById(R.id.toolbars);
        mytoolbar.setTitle("NEW TASK");
        setSupportActionBar(mytoolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_if_arrow_back_1063891);
    }

    private void checkedChangeListeners() {
        alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //helps to check if user has checked the alarm
                checked = isChecked;
                Toast.makeText(NewTask.this, String.valueOf(checked), Toast.LENGTH_SHORT).show();
            }
        });
        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { notificationValue = isChecked; }
        });
    }

    private void initViews() {
        alarm = findViewById(R.id.switch1);
        writetodo = findViewById(R.id.textView13);
        writepriority = findViewById(R.id.spinner);
        notification = findViewById(R.id.switch2);
        writedate = findViewById(R.id.editText);
    }

    private void initFireBase() {
        fdatabase = FirebaseDatabase.getInstance();
        databaseRef = fdatabase.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void timePicker() {
        // Get Current Time
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(NewTask.this,
                new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;mMinute = minute;
                date = date +" "+hourOfDay+":"+minute;
                writedate.setText(d +" at "+hourOfDay+":"+minute);
            }
            }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveText(View view) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.ENGLISH);
            if (date != null){
                try {
                    Date dat = formatter.parse(date);//converts string date to normal time
                    timeInMiliis = dat.getTime();//gets the time in milliseconds
                    if ( dat.before(new Date())){
                        Toast.makeText(getApplicationContext(),"This date is past!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(NewTask.this, "No date entered", Toast.LENGTH_SHORT).show();
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
            String s = sp.format(timeInMiliis);
            String u = String.valueOf(timeInMiliis);//converts long to string
            final String option = writepriority.getSelectedItem().toString();
            Random rand = new Random();
            final int value = rand.nextInt(50);
            task = new Task();
            task.setContent(msg);
            task.setCompleted_by(u);
            task.setDate(s);
            task.setTask_priority(option);
            task.setSave_as_alarm(checked);
            task.setToday_date(me());
            task.setShow_as_notification(notificationValue);
            task.setCompleted(false);

            key  = databaseRef.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).child("task_list").push().getKey();
            task.setKey(key);
            assert key != null;
            databaseRef.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).child("task_list").child(key)
                    .setValue(task);
            Toast.makeText(NewTask.this, "completed", Toast.LENGTH_LONG).show();
            chooseAlertOptions(option,value);
            finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void chooseAlertOptions(String option, int value) {
        switch (option) {
            case "High":
                //true
                if (checked && notificationValue)
                    setAlarm(value);
                if (!checked && notificationValue)
                    setNotification("High",value);
                if (!checked && !notificationValue )
                    setNotification("High",value);
                else
                    setNotification("High",value);
                break;
            case "Medium":
                if (checked)
                    setMediumAlarm(value);
                if (!checked && notificationValue)
                    setNotification("Medium",value);
                if (!checked && !notificationValue )
                    setNotification("Medium",value);
                else
                    setNotification("High",value);
                break;
            case "Low":
                if (checked)
                    setLowAlarm(value);
                if (!checked && notificationValue)
                    setNotification("Low",value);
                if (!checked && !notificationValue )
                    setNotification("Medium",value);
                else
                    setNotification("High",value);
                break;
        }
    }

    private void setNotification(String option, int value) {
        Intent myIntent = new Intent(NewTask.this, AlarmReceiver.class);
        myIntent.putExtra ("message", msg);
        myIntent.putExtra ("title", title);
        myIntent.putExtra("value",value);
        myIntent.putExtra("key", key);
        pendingintent = PendingIntent.getBroadcast(NewTask.this, value, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmmanager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmmanager != null;
        switch (option) {
            case "High":
                alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMiliis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingintent);
                break;
            case "Medium":
                alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMiliis, AlarmManager.INTERVAL_HALF_HOUR, pendingintent);
                break;
            case "Low":
                alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMiliis, AlarmManager.INTERVAL_HOUR, pendingintent);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setAlarm(int value) {
        Intent myIntent = new Intent(NewTask.this, AlarmReceiver.class);
        myIntent.putExtra ("notification", "notification");
        myIntent.putExtra ("message", msg);
        myIntent.putExtra ("title", title);
        myIntent.putExtra("key", key);
        Toast.makeText(this,String.valueOf(value),Toast.LENGTH_SHORT).show();
        pendingintent = PendingIntent.getBroadcast(NewTask.this, value, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmmanager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmmanager != null;
        alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMiliis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingintent);
    }

    private void setLowAlarm(int value){
        Intent myIntent = new Intent(NewTask.this, AlarmReceiver.class);
        myIntent.putExtra ("notification", "notification");
        myIntent.putExtra ("message", task.getContent());
        myIntent.putExtra ("title", title);
        myIntent.putExtra("key", key);
        pendingintent = PendingIntent.getBroadcast(NewTask.this, value, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmmanager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmmanager != null;
        alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP,
                timeInMiliis, AlarmManager.INTERVAL_HOUR, pendingintent);
    }

    private void setMediumAlarm(int value) {
        Intent myIntent = new Intent(NewTask.this, AlarmReceiver.class);
        myIntent.putExtra ("notification", "notification");
        myIntent.putExtra ("message", msg);
        myIntent.putExtra ("title", title);
        myIntent.putExtra("key", key);
        pendingintent = PendingIntent.getBroadcast(NewTask.this, value, myIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmmanager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmmanager != null;
        alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP,timeInMiliis
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
