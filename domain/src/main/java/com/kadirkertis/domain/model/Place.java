package com.kadirkertis.domain.model;

import java.util.HashMap;

/**
 * Created by Kadir Kertis on 11/9/2017.
 */

public class Place {
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
    private long timeAdded;
    private long timeLastEdited;

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
                 long timeLastEdited) {
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
        this.timeLastEdited= timeLastEdited;

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

}
