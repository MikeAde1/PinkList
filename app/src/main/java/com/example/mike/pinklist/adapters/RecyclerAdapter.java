package com.example.mike.pinklist.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mike.pinklist.R;
import com.example.mike.pinklist.models.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 1/28/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    Context context;
    private List<String> taskList = new ArrayList<>();
    private List<String> datelist = new ArrayList<>();

    public RecyclerAdapter(Context context, List<String> taskList, List<String> datelist){
        this.context = context;
        this.taskList = taskList;
        this.datelist = datelist;
    }
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search,parent);
        return new RecyclerHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        holder.item.setText(taskList.get(position));
        holder.date.setText(datelist.get(position));
    }
    @Override
    public int getItemCount() {
        return taskList.size();
    }
    class RecyclerHolder extends RecyclerView.ViewHolder{
        TextView item,date;
         RecyclerHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.upcomingtv);
            date = (TextView) itemView.findViewById(R.id.dates);
        }
    }
   /* public void setFilter(List<Task> newList){
        taskList = new ArrayList<>();//
        taskList.addAll(newList);// new items in the arraylist from  the Task object
        notifyDataSetChanged();
    }*/
}
