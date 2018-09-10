package com.kadirkertis.domain.interactor.qr.repository;

import com.kadirkertis.domain.interactor.qr.model.QrResult;

import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/23/2017.
 */

public interface QRCodeService {

    Single<QrResult> parseCode();
}
