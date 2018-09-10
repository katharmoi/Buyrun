package com.kadirkertis.domain.interactor.checkIn.model;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public final class CheckInUser {

    private final String placeId;
    private final long time;

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
