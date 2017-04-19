package com.kadirkertis.orfo.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by Kadir Kertis on 14.4.2017.
 */

public class Message {
    private String userId;
    private String message;
    private String name;
    private HashMap<String, Object> timeAdded;

    public Message(){}

    public Message(String userId,String message, String name, HashMap<String, Object> timeAdded) {
        this.userId = userId;
        this.message = message;
        this.name = name;
        this.timeAdded = timeAdded;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public HashMap<String, Object> getTimeAdded() {
        if (timeAdded != null) {

            return timeAdded;
        }

        HashMap<String, Object> timeObj = new HashMap<>();
        timeObj.put("date", ServerValue.TIMESTAMP);
        return timeObj;
    }

    @Exclude
    public long getTimeAddedLong() {
        return (long) timeAdded.get("time_added");
    }
}
