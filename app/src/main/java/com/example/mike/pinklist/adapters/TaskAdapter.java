package com.example.mike.pinklist.adapters;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mike.pinklist.AlarmReceiver;
import com.example.mike.pinklist.Fragment_todo;
import com.example.mike.pinklist.R;
import com.example.mike.pinklist.flux.Dispatcher;
import com.example.mike.pinklist.models.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;
import static com.google.android.gms.actions.ItemListIntents.ACTION_DELETE_ITEM;

/**
 * Created by Mike on 10/25/2017.
 */
public class TaskAdapter extends BaseAdapter{
    String TAG = "tag";
    Context context;
    CompoundButton button;
    List<Task> tasks = new ArrayList<>();
    LayoutInflater layoutInflater;
    AlarmManager alarmManager;
    private static FirebaseDatabase database;
    DatabaseReference databaseRef;
    FirebaseAuth firebaseAuth;
    private final int placeholder = 0;
    android.app.AlertDialog alertDialog;
    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        this.layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return tasks.size();
    }
    @Override
    public Object getItem(int position ) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.adapter_task_item,null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.for_date);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            /*holder.content.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                       button = buttonView;
                       buttonView.setTextColor(Color.GRAY);
//                        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
//                        builder.setMessage("Delete task from list?");
//                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                i = placeholder;
//                                databaseRef.child(firebaseAuth.getCurrentUser().getUid()).child("task_list").child(Fragment_todo.task_position.get(position))
//                                        .setValue(null);
//                                Task task;
//                                task = tasks.get(position);
//                                tasks.remove(task);
//                                notifyDataSetChanged();
//                            }
//                        });
//                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                alertDialog.dismiss();
//                            }
//                        });
//                        alertDialog = builder.show();
                        Dispatcher dispatcher = new Dispatcher(context);
                        dispatcher.sendBroadcast(Dispatcher.Broadcast.ACTION_DELETE_ITEM, String.valueOf(position));
                     *//* final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Delete task from list?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String key = String.valueOf(getKeyFromValue(store.getTransCardHashMap(),transCard));
                                Log.d(TAG,key);
                                //       transCardList.remove(transCard);
                                //       cardAdapter.notifyDataSetChanged();
                                parameters.put("key",key);
                                Action action = new Action(ACTION_DELETE_ITEM,parameters);
                                dispatcher.dispatch(action);
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog = builder.show();
                    }*//*
                }
                    else {
                        buttonView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
            });*/
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        Task task = tasks.get(position);
        if (task.getContent().length() > 25){
            String content = task.getContent().substring(0,25)+"...";
            holder.textView.setText(content);
        }
        else {
            holder.textView.setText(task.getContent());
        }
        //You can set another task element to text in this manner.
        String dt = task.getCompleted_by();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(dt));
        SimpleDateFormat sp = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.ENGLISH);
        String s = sp.format(cal.getTime());
        holder.tv.setText(s);
        return convertView;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    class ViewHolder{
        TextView tv,textView;
    }

    public void setFilter(List<Task> arrayList){
        tasks = new ArrayList<>();
        tasks.addAll(arrayList);
        notifyDataSetChanged();
    }
}
