package com.kadirkertis.domain.interactor.tracking;

import com.kadirkertis.domain.interactor.place.model.Place;
import com.kadirkertis.domain.interactor.tracking.repository.UserTrackingService;
import com.kadirkertis.domain.interactor.type.SingleUseCaseWithParameter;

import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 9/3/2018.
 */

public final class IsUserInUseCase implements SingleUseCaseWithParameter<Boolean, Place> {

    private final UserTrackingService userTrackingService;

    public IsUserInUseCase(UserTrackingService userTrackingService) {
        this.userTrackingService = userTrackingService;
    }

    @Override
    public Single<Boolean> execute(Place place) {
        return userTrackingService.isUserIn(place.getLatitude(), place.getLongitude());
    }
}
