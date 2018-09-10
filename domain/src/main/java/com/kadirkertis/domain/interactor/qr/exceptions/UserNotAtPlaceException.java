package com.kadirkertis.domain.interactor.qr.exceptions;

/**
 * Created by Kadir Kertis on 11/30/2017.
 */

public final class UserNotAtPlaceException extends RuntimeException {
    public UserNotAtPlaceException() {
    }

    public UserNotAtPlaceException(String message) {
        super(message);
    }
}
