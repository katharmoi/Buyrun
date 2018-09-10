package com.kadirkertis.domain.interactor.qr.exceptions;

/**
 * Created by Kadir Kertis on 11/28/2017.
 */

public final class UnknownQRCodeException extends RuntimeException {
    public UnknownQRCodeException() {
    }

    public UnknownQRCodeException(String message) {
        super(message);
    }
}
