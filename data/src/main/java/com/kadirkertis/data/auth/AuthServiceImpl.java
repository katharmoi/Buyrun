package com.kadirkertis.data.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.kadirkertis.data.R;
import com.kadirkertis.domain.interactor.auth.repository.AuthResponse;
import com.kadirkertis.domain.interactor.auth.repository.AuthService;

import java.util.Arrays;
import java.util.List;

import durdinapps.rxfirebase2.RxFirebaseAuth;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import rx_activity_result2.RxActivityResult;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public final class AuthServiceImpl implements AuthService {

    private final Activity activity;
    private final FirebaseAuth auth;

    public AuthServiceImpl(Activity activity,FirebaseAuth auth) {
        this.activity = activity;
        this.auth = auth;
    }

    @Override
    public Single<AuthResponse> signInUser() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        Intent authUIIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.My_FirebaseUI)
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .build();

        return RxActivityResult.on(activity).startIntent(authUIIntent)
                .map(activityResult -> {
                    Intent data = activityResult.data();
                    int resultCode = activityResult.resultCode();

                    if (resultCode == RESULT_OK) {
                        return AuthResponse.success(auth.getUid());
                    } else {
                        IdpResponse response = IdpResponse.fromResultIntent(data);
                        if (response == null) return AuthResponse.error(activity.getResources().
                                getString(R.string.auth_messages_user_cancelled));
                        return AuthResponse.error(response.getError().getMessage());
                    }
                })
                .singleOrError()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<AuthResponse> signUserOut() {
        return null;
    }

    @Override
    public Single<AuthResponse> deleteUser() {
        return null;
    }

    @Override
    public Observable<AuthResponse> observeAuth() {
        return RxFirebaseAuth.observeAuthState(auth)
                .map(authResult -> {
                    if (authResult != null && authResult.getCurrentUser() != null) {
                        return AuthResponse.success(auth.getUid());
                    } else {
                        return AuthResponse.error(activity.getResources().
                                getString(R.string.auth_messages_user_not_authenticated));
                    }
                })
                .subscribeOn(Schedulers.io());
    }


}
