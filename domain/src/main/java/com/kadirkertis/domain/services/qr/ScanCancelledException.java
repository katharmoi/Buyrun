package com.kadirkertis.domain.services.qr;

/**
 * Created by Kadir Kertis on 11/28/2017.
 */

public class ScanCancelledException extends RuntimeException {
    public ScanCancelledException() {
    }

    public ScanCancelledException(String message) {
        super(message);
    }
}
