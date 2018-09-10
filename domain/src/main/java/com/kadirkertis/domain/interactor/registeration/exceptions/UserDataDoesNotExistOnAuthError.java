package com.kadirkertis.domain.interactor.registeration.exceptions;

/**
 * Created by Kadir Kertis on 11/27/2017.
 */

public final class UserDataDoesNotExistOnAuthError extends RuntimeException {

    public UserDataDoesNotExistOnAuthError() {
    }

    public UserDataDoesNotExistOnAuthError(String message) {
        super(message);
    }
}
