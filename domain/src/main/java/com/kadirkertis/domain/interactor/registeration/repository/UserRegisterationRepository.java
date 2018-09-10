package com.kadirkertis.domain.interactor.registeration.repository;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * Created by Kadir Kertis on 11/27/2017.
 */

public interface UserRegisterationRepository {

    Completable registerUser();
    Completable unregisterUser();
    Maybe<Boolean> isUserRegistered();

}
