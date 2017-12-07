package com.kadirkertis.domain.model;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public class CheckInPlace {
    private String userId;
    private long time;

    public CheckInPlace(String userId, long time) {
        this.userId = userId;
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public long getTime() {
        return time;
    }
}
