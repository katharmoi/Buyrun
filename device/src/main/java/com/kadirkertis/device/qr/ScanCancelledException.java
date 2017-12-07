package com.kadirkertis.device.qr;

/**
 * Created by Kadir Kertis on 11/28/2017.
 */

public class ScanCancelledException extends Exception {
    public ScanCancelledException() {
    }

    public ScanCancelledException(String message) {
        super(message);
    }
}
