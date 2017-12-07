package com.kadirkertis.data.session;


import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/27/2017.
 */

public interface SessionService {

    Completable setPlace(String placeId, String tableNumber);
    Completable setUser(String userId);
    Maybe<String> getPlace();
    Maybe<String> getUser();


}
