package com.kadirkertis.domain.repository;

import io.reactivex.Completable;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public interface UserCheckingRepository {

    Completable checkUserIn(String placeId, String tableNumber, String userId);
    Completable checkUserOut(String userId);

}
