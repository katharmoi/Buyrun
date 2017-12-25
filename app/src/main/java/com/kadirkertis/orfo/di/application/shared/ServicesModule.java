package com.kadirkertis.orfo.di.application.shared;

import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.kadirkertis.data.auth.AuthServiceImpl;
import com.kadirkertis.data.session.SessionService;
import com.kadirkertis.data.session.SessionServiceImpl;
import com.kadirkertis.domain.services.auth.AuthService;
import com.kadirkertis.orfo.di.application.AppScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kadir Kertis on 12/1/2017.
 */
@Module
public class ServicesModule {
    @Provides
    @AppScope
    public static AuthService provideAuthService(FirebaseAuth auth) {
        return new AuthServiceImpl(auth);
    }

    @Provides
    @AppScope
    public static SessionService provideSessionService(SharedPreferences preferences) {
        return new SessionServiceImpl(preferences);
    }


}
