package com.kadirkertis.orfo.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by Kadir Kertis on 16.3.2017.
 */

public class CheckInPlace {
    private String uid;
    private String userName;
    private HashMap<String,Object> checkInTime;


    public CheckInPlace() {
    }

    public CheckInPlace(String userId,String userName, HashMap<String, Object> checkInTime) {

        this.uid = userId;
        this.userName = userName;
        this.checkInTime = checkInTime;
    }
    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public HashMap<String, Object> getCheckInTime() {
        if(checkInTime != null){
            return checkInTime;
        }

        HashMap<String, Object> checkTimeObj = new HashMap<>();
        checkTimeObj.put("check_in_time", ServerValue.TIMESTAMP);
        return  checkTimeObj;
    }

    @Exclude
    public long getCheckInTimeLong(){
        return (long) checkInTime.get("check_in_time");
    }

    public HashMap<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("userName",userName);
        result.put("checkInTime",checkInTime);
        return result;
    }


}