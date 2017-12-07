package com.kadirkertis.orfo.di.application.shared;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.kadirkertis.data.auth.AuthServiceImpl;
import com.kadirkertis.data.session.SessionService;
import com.kadirkertis.data.session.SessionServiceImpl;
import com.kadirkertis.device.location.UserTrackingServiceImpl;
import com.kadirkertis.device.qr.QRCodeServiceImpl;
import com.kadirkertis.domain.services.AuthService;
import com.kadirkertis.domain.services.QRCodeService;
import com.kadirkertis.domain.services.UserTrackingService;
import com.kadirkertis.orfo.di.application.AppScope;
import com.patloew.rxlocation.RxLocation;

import javax.inject.Singleton;

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
    public static UserTrackingService provideUserTrackingService(RxLocation location) {
        return new UserTrackingServiceImpl(location);
    }

    @Provides
    @AppScope
    public static SessionService provideSessionService(SharedPreferences preferences) {
        return new SessionServiceImpl(preferences);
    }


}
