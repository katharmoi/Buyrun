package com.kadirkertis.orfo.ui.main.errors;

import java.util.concurrent.Callable;

/**
 * Created by Kadir Kertis on 11/30/2017.
 */

public class UserCancelledSignInError extends RuntimeException {
    public UserCancelledSignInError(String message) {
        super(message);
    }
}
