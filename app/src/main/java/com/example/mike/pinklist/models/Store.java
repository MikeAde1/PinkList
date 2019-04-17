package com.example.mike.pinklist.models;

/**
 * Created by Mike on 3/10/2018.
 */

public class Store {
    static final Store ourInstance = new Store();
    public static Store getInstance(){
        return ourInstance;
    }
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    Task task;
}
