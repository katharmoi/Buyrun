package com.kadirkertis.domain.interactor.tracking.repository;


import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/20/2017.
 */

public interface UserTrackingService {
    Single<Boolean> isUserIn(double placeLat, double placeLong);

    Single<Boolean> checkPermission();
}
