package com.kadirkertis.orfo.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by Kadir Kertis on 17.3.2017.
 */

public class User {

    private String uid ;
    private String name;
    private String email;
    private HashMap<String,Object> registrationDate;

    public User() {
    }

    public User(String uid, String name, String email, HashMap<String,Object> registrationDate) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.registrationDate = registrationDate;
    }

    public String getName() {
        return name;
    }
    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }


    public HashMap<String, Object> getRegistrationDate() {
        if(registrationDate != null){
            return registrationDate;
        }

        HashMap<String, Object> checkTimeObj = new HashMap<>();
        checkTimeObj.put("date", ServerValue.TIMESTAMP);
        return  checkTimeObj;
    }

    @Exclude
    public long getCheckInTimeLong(){
        return (long) registrationDate.get("date");
    }
}
