package com.kadirkertis.data.registeration;

import java.util.concurrent.Callable;

/**
 * Created by Kadir Kertis on 11/27/2017.
 */

class UserDataDoesNotExistOnAuthError extends Exception {

    public UserDataDoesNotExistOnAuthError() {
    }

    public UserDataDoesNotExistOnAuthError(String message) {
        super(message);
    }
}
