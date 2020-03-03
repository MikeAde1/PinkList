package com.example.mike.pinklist.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mike.pinklist.R;
import com.example.mike.pinklist.models.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mike on 10/25/2017.
 */
public class TaskAdapter extends BaseAdapter{
    String TAG = "tag";
    private Context context;
    private List<Task> tasks;
    private LayoutInflater layoutInflater;
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
