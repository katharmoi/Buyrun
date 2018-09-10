package com.kadirkertis.domain.interactor.tracking.exceptions;

/**
 * Created by Kadir Kertis on 12/2/2017.
 */

public final class LocationPermissionNotSatisfiedException extends RuntimeException{
    public LocationPermissionNotSatisfiedException(String message) {
        super(message);
    }
}
