package com.kadirkertis.domain.repository;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * Created by Kadir Kertis on 11/27/2017.
 */

public interface UserRegisterationRepository {

    Completable saveAuthUserToDbUser();
    Maybe<Boolean> isUserRegistered();

}
