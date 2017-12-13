package com.kadirkertis.domain.services.qr;

/**
 * Created by Kadir Kertis on 11/28/2017.
 */

public class UnknownQRCodeException extends RuntimeException {
    public UnknownQRCodeException(String message) {
        super(message);
    }
}
