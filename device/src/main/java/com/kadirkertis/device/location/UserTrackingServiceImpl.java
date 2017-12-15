package com.kadirkertis.device.location;

import android.Manifest;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.kadirkertis.domain.services.UserTrackingService;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Kadir Kertis on 11/21/2017.
 */

public class UserTrackingServiceImpl implements UserTrackingService {

    private RxLocation rxLocation;
    private RxPermissions rxPermissions;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private static final int MAX_RADIUS = 50;

    public UserTrackingServiceImpl(RxLocation location, RxPermissions rxPermissions) {
        this.rxLocation = location;
        this.rxPermissions = rxPermissions;
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5000);
        locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)
                .build();
    }


    @Override
    public Single<Boolean> checkUserIn(double placeLat, double placeLong) {
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
                .flatMapObservable((granted) -> {
                    if (granted) return rxLocation.location().updates(locationRequest);
                    else return Observable.error(new LocationPermissionNotSatisfiedException("Application Needs Location Permission"));
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Boolean> checkPermission() {
        return rxLocation.settings()
                .checkAndHandleResolution(locationSettingsRequest)
                .flatMap(settingsOk ->{
                    if(settingsOk) return rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).single(false);
                    else return Single.error(new LocationServiceNotAvailableException());
                } )
                .subscribeOn(Schedulers.io());
    }
}
