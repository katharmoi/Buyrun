package com.kadirkertis.data.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kadir Kertis on 11/13/2017.
 */

public class DataPlace {
    private String id;
    private String placeName;
    private String ownerName;
    private String email;
    private String address;
    private String phone;
    private String placeType;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private HashMap<String,Object> timeAdded;
    private HashMap<String,Object> timeLastEdited;



    public DataPlace(String id,
                 String placeName,
                 String ownerName,
                 String email,
                 String address,
                 String phone,
                 String placeType,
                 String imageUrl,
                 double latitude,
                 double longitude,
                 HashMap<String, Object> timeAdded) {
        this.id = id;
        this.placeName = placeName;
        this.ownerName = ownerName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.placeType = placeType;
        this.imageUrl = imageUrl;
        this.timeAdded = timeAdded;
        this.latitude = latitude;
        this.longitude = longitude;
        HashMap<String,Object> timeLastEditedObj = new HashMap<>();
        timeLastEditedObj.put("date", ServerValue.TIMESTAMP);
        this.timeLastEdited = timeLastEditedObj;
    }

    public DataPlace(){

    }


    public String getId() {
        return id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getPlaceType() {
        return placeType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public HashMap<String, Object> getTimeAdded() {
        if(timeAdded != null){
            return timeAdded;
        }

        HashMap<String,Object> timeAddedObj = new HashMap<>();
        timeAddedObj.put("date", ServerValue.TIMESTAMP);
        return timeAddedObj;
    }

    public HashMap<String,Object> getTimeLastEdited(){
        return timeLastEdited;
    }

    @Exclude
    public long getTimeAddedLong(){
        return (long)timeAdded.get("timeAdded");
    }

    @Exclude
    public long getTimeLastEditedLong(){
        return (long) timeLastEdited.get("date");
    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("placeName", placeName);
        result.put("ownerName", ownerName);
        result.put("email", email);
        result.put("address", address);
        result.put("phone", phone);
        result.put("placeType", placeType);
        result.put("imageUrl", imageUrl);
        result.put("timeAdded", timeAdded);
        result.put("timeLastEdited",timeLastEdited);

        return result;

    }
}
