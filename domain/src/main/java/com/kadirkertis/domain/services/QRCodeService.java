package com.kadirkertis.domain.services;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/23/2017.
 */

public interface QRCodeService<T, R> {

    Completable initiateScan();

    Single<R> parseCode(T codeResult);
}
