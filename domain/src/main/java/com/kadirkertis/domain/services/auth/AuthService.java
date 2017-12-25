package com.kadirkertis.domain.services.auth;
import io.reactivex.Observable;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public interface AuthService<T> {
    public Observable<T> observeAuth();
}
