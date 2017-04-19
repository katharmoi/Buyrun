package com.kadirkertis.orfo.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by Kadir Kertis on 16.3.2017.
 */

public class CheckInUser {
    private String placeId;
    private HashMap<String,Object> checkInTime;


    public CheckInUser() {
    }

    public CheckInUser(String placeId, HashMap<String, Object> checkInTime) {

        this.placeId = placeId;
        this.checkInTime = checkInTime;
    }
    public String getPlaceId() {
        return placeId;
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

    @Exclude
    public HashMap<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("placeId",placeId);
        result.put("checkInTime",checkInTime);
        return result;
    }

}
