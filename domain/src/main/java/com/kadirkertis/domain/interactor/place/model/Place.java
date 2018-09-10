package com.kadirkertis.domain.interactor.place.model;

/**
 * Created by Kadir Kertis on 11/9/2017.
 */

public class Place {
    private final String id;
    private final String placeName;
    private final String ownerName;
    private final String email;
    private final String address;
    private final String phone;
    private final String placeType;
    private final String imageUrl;
    private final double latitude;
    private final double longitude;
    private final long timeAdded;
    private final long timeLastEdited;
    private final float rating;
    private final int rateCount;

    public Place(String id,
                 String placeName,
                 String ownerName,
                 String email,
                 String address,
                 String phone,
                 String placeType,
                 String imageUrl,
                 double latitude,
                 double longitude,
                 long timeAdded,
                 long timeLastEdited,
                 float rating,
                 int rateCount) {
        this.id = id;
        this.placeName = placeName;
        this.ownerName = ownerName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.placeType = placeType;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeAdded = timeAdded;
        this.timeLastEdited = timeLastEdited;
        this.rating = rating;
        this.rateCount = rateCount;

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

    public long getTimeAdded() {
        return timeAdded;
    }

    public long getTimeLastEdited() {
        return timeLastEdited;
    }

    public float getRating() {
        return rating;
    }

    public int getRateCount() {
        return rateCount;
    }
}
