package com.kadirkertis.orfo.di.application.shared;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.kadirkertis.data.auth.AuthServiceImpl;
import com.kadirkertis.data.session.SessionRepositoryImpl;
import com.kadirkertis.domain.interactor.auth.repository.AuthService;
import com.kadirkertis.domain.interactor.session.repository.SessionRepository;
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
    public static SessionRepository provideSessionService(SharedPreferences preferences) {
        return new SessionRepositoryImpl(preferences);
    }

}
