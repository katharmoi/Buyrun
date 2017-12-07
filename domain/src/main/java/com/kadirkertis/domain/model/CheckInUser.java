package com.kadirkertis.domain.model;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public class CheckInUser {

    private String placeId;
    private long time;

    public CheckInUser(String placeId, long time) {
        this.placeId = placeId;
        this.time = time;
    }

    public String getPlaceId() {
        return placeId;
    }

    public long getTime() {
        return time;
    }
}
