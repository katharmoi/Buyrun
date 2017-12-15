package com.kadirkertis.domain.interactor.qr;

/**
 * Created by Kadir Kertis on 11/30/2017.
 */

class UserNotAtPlaceException extends RuntimeException {
    public UserNotAtPlaceException() {
    }

    public UserNotAtPlaceException(String message) {
        super(message);
    }
}
