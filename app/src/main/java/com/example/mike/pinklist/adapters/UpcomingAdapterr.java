package com.example.mike.pinklist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mike.pinklist.R;
import com.example.mike.pinklist.models.Task;
import com.example.mike.pinklist.models.Upcoming;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mike on 11/4/2017.
 */

public class UpcomingAdapterr extends BaseAdapter {
    private Context context;
    private List<Task> upcoming_list;
    private LayoutInflater layoutInflater;
    private String s;
    public UpcomingAdapterr(Context context, List<Task> upcoming_list) {
        this.context = context;
        this.upcoming_list = upcoming_list;
        this.layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return upcoming_list.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View_holder holder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.adapter_upcoming_item,null);
            holder = new View_holder();
            holder.img = (ImageView) convertView.findViewById(R.id.image);
            holder.tv =  (TextView)  convertView.findViewById(R.id.upcomingtv);
            holder.ttv = (TextView) convertView.findViewById(R.id.dates);
            convertView.setTag(holder);}
            else{
                holder = (View_holder)convertView.getTag();
        }
        Task task = upcoming_list.get(position);
        holder.tv.setText(task.getContent());
        holder.img.setImageResource(R.drawable.ic_archive_black_24dp);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        SimpleDateFormat dateformate = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.ENGLISH);
        try {
            Date dat = formatter.parse(task.getDate());//converts string date to normal time
            s = dateformate.format(dat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.ttv.setText(s);
        return convertView;
    }
    public void setTasks(List<Task> upcoming_list) {
        this.upcoming_list = upcoming_list;
    }
    class View_holder{
        ImageView img;
        TextView tv,ttv;
    }

    public void setFilter(List<Task> arrayList){
        upcoming_list = new ArrayList<>();
        upcoming_list.addAll(arrayList);
        notifyDataSetChanged();
    }
}
