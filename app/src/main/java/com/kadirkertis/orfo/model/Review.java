package com.kadirkertis.orfo.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

/**
 * Created by Kadir Kertis on 10.4.2017.
 */

public class Review {

    private String reviewId;
    private String userId;
    private String userName;
    private String reviewTitle;
    private String reviewText;
    private float rating;
    private HashMap<String,Object> timeAdded;
    private HashMap<String,Object> timeLastEdited;

    public Review() {
    }

    public Review(String reviewId,
                  String userId,
                  String userName,
                  String reviewTitle,
                  String reviewText,
                  float rating,
                  HashMap<String, Object> timeAdded) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.userName = userName;
        this.reviewTitle = reviewTitle;
        this.reviewText = reviewText;
        this.rating = rating;
        this.timeAdded = timeAdded;

        HashMap<String,Object> timeLastEditedObj = new HashMap<>();
        timeLastEditedObj.put("date", ServerValue.TIMESTAMP);
        this.timeLastEdited = timeLastEditedObj;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public String getReviewText() {
        return reviewText;
    }

    public float getRating() {
        return rating;
    }

    public HashMap<String, Object> getTimeAdded(){
        if(timeAdded != null){
            return timeAdded;
        }

        HashMap<String,Object> timeAddedObj = new HashMap<>();
        timeAddedObj.put("date", ServerValue.TIMESTAMP);
        return timeAddedObj;
    }

    public HashMap<String, Object> getTimeLastEdited() {
        return timeLastEdited;
    }

    @Exclude
    public long getTimeAddedLong(){
        return (long)timeAdded.get("date");
    }

    @Exclude
    public long getTimeLastEditedLong(){
        return (long) timeLastEdited.get("date");
    }

    @Exclude
    public HashMap<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("reviewId",reviewId);
        result.put(" userId", userId);
        result.put("userName",userName);
        result.put("reviewTitle",reviewTitle);
        result.put("reviewText",reviewText);
        result.put("rating",rating);
        result.put("timeAdded",timeAdded);
        result.put("timeLastEdited",timeLastEdited);
        return result;

    }

}
