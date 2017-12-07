package com.kadirkertis.device.location;

import android.location.Location;

import com.google.android.gms.location.LocationRequest;
import com.kadirkertis.domain.services.UserTrackingService;
import com.patloew.rxlocation.RxLocation;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Kadir Kertis on 11/21/2017.
 */

public class UserTrackingServiceImpl implements UserTrackingService {

    private RxLocation rxLocation;
    private LocationRequest locationRequest;
    private static final int MAX_RADIUS = 50;

    public UserTrackingServiceImpl(RxLocation location) {
        this.rxLocation = location;
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5000);
    }


    @Override
    public Single<Boolean> isUserIn(double placeLat, double placeLong) {
        float[] results = new float[1];
        return getCurrentLocation()
                .map(location -> {
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(), placeLat, placeLong, results);
                    return results[0] < MAX_RADIUS;
                })
                .singleOrError()
                .subscribeOn(Schedulers.io());
    }


    @Override
    public Completable checkUserOut() {
        return null;
    }

    private Observable<Location> getCurrentLocation() {
        return checkPermission()
                .flatMapObservable((isAllowed) -> {
                    if (isAllowed) return rxLocation.location().updates(locationRequest);
                    else return Observable.error(new LocationPermissionNotSatisfiedException("Application Needs Location Permission"));
                })
                .subscribeOn(Schedulers.io());
    }

    private Single<Boolean> checkPermission() {
        return rxLocation.settings()
                .checkAndHandleResolution(locationRequest)
                .subscribeOn(Schedulers.io());
    }
}
