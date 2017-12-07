package com.kadirkertis.device.location;

/**
 * Created by Kadir Kertis on 12/2/2017.
 */

class LocationPermissionNotSatisfiedException extends RuntimeException{
    public LocationPermissionNotSatisfiedException(String message) {
        super(message);
    }
}
