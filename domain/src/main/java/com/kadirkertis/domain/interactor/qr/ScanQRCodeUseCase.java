package com.kadirkertis.domain.interactor.qr;

import com.kadirkertis.domain.services.QRCodeService;

import io.reactivex.Completable;

/**
 * Created by Kadir Kertis on 11/29/2017.
 */

public class ScanQRCodeUseCase {
    private QRCodeService<Object, Object> qrService;

    public ScanQRCodeUseCase(QRCodeService<Object, Object> qrService) {
        this.qrService = qrService;
    }

    public Completable execute() {
        return qrService.initiateScan();

    }
}
