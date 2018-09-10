package com.kadirkertis.domain.interactor.checkIn.model;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public final class CheckInPlace {
    private final String userId;
    private final long time;

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
