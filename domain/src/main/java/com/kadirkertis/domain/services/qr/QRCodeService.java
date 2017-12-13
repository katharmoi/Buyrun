package com.kadirkertis.domain.services.qr;

import com.kadirkertis.domain.model.QrResult;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/23/2017.
 */

public interface QRCodeService<T> {

    Completable initiateScan();

    Single<QrResult> parseCode(T codeResult);
}
