package com.kadirkertis.domain.interactor.qr;

import com.kadirkertis.domain.services.qr.QRCodeService;

import io.reactivex.Completable;

/**
 * Created by Kadir Kertis on 11/29/2017.
 */

public class ScanQRCodeUseCase {
    private QRCodeService<Object> qrService;

    public ScanQRCodeUseCase(QRCodeService<Object> qrService) {
        this.qrService = qrService;
    }

    public Completable execute() {
        return qrService.initiateScan();

    }
}
