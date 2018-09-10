package com.kadirkertis.domain.interactor.auth.repository;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public interface AuthService {
    Single<AuthResponse> signInUser();

    Single<AuthResponse> signUserOut();

    Single<AuthResponse> deleteUser();

    Observable<AuthResponse> observeAuth();

}
