package com.kadirkertis.domain.interactor.checkIn.repository;

import com.kadirkertis.domain.interactor.checkIn.model.CheckInRequest;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public interface UserCheckingRepository {

    Single<String> checkUserIn(CheckInRequest request);
    Completable checkUserOut(String userId);

}
