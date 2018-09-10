package com.kadirkertis.domain.interactor.qr.exceptions;

/**
 * Created by Kadir Kertis on 11/28/2017.
 */

public final class ScanCancelledException extends RuntimeException {
    public ScanCancelledException() {
    }

    public ScanCancelledException(String message) {
        super(message);
    }
}
