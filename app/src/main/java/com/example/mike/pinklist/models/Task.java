package com.example.mike.pinklist.models;

/**
 * Created by Mike on 10/24/2017.
 */

public class Task {
    int time;
    private String name;
    private String today_date;
    private String content,task_priority;
    private String completed_by;
    private String date;
    private boolean save_as_alarm;
    private boolean show_as_notification;
    private boolean completed = false;
    public Task() {
    }
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getToday_date() {
        return today_date;
    }
    public void setToday_date(String today_date) {
        this.today_date = today_date;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getCompleted_by() {
        return completed_by;
    }
    public void setCompleted_by(String completed_by) {
        this.completed_by = completed_by;
    }
    public String getTask_priority() {
        return task_priority;
    }
    public void setTask_priority(String task_priority) {
        this.task_priority = task_priority;
    }
    public boolean isSave_as_alarm() {
        return save_as_alarm;
    }
    public void setSave_as_alarm(boolean save_as_alarm) {
        this.save_as_alarm = save_as_alarm;
    }
    public boolean isShow_as_notification() {
        return show_as_notification;
    }
    public void setShow_as_notification(boolean show_as_notification) {
        this.show_as_notification = show_as_notification;
    }

    public void setTime(int time) {
        this.time = time;
    }
    public int getTime(){
        return time;
    }
}
