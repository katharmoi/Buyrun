package com.kadirkertis.data.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.kadirkertis.domain.services.auth.AuthService;

import durdinapps.rxfirebase2.RxFirebaseAuth;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public class AuthServiceImpl implements AuthService<FirebaseAuth> {
    private FirebaseAuth auth;


    public AuthServiceImpl(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public Observable<FirebaseAuth> observeAuth() {
        return RxFirebaseAuth.observeAuthState(auth)
                .subscribeOn(Schedulers.io());
    }


}
