package com.kadirkertis.domain.services;

import com.sun.org.apache.xpath.internal.operations.Bool;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/20/2017.
 */

public interface UserTrackingService {
    Single<Boolean> isUserIn(double placeLat, double placeLong);
    Completable checkUserOut();
    Single<Boolean> checkPermission();
}
