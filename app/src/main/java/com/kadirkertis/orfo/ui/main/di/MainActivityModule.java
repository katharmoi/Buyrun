package com.kadirkertis.orfo.ui.main.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.kadirkertis.data.auth.AuthServiceImpl;
import com.kadirkertis.data.checkIn.UserCheckingRepositoryImpl;
import com.kadirkertis.data.place.PlaceRepositoryImpl;
import com.kadirkertis.data.registeration.UserRegistrationRepositoryImpl;
import com.kadirkertis.device.location.UserTrackingServiceImpl;
import com.kadirkertis.device.qr.QRCodeServiceImpl;
import com.kadirkertis.domain.interactor.auth.ObserveAuthStatusUseCase;
import com.kadirkertis.domain.interactor.auth.SignInUserUseCase;
import com.kadirkertis.domain.interactor.auth.repository.AuthService;
import com.kadirkertis.domain.interactor.checkIn.CheckInUserUseCase;
import com.kadirkertis.domain.interactor.checkIn.repository.UserCheckingRepository;
import com.kadirkertis.domain.interactor.place.GetPlaceUseCase;
import com.kadirkertis.domain.interactor.place.repository.PlaceRepository;
import com.kadirkertis.domain.interactor.qr.ParseQrCodeUseCase;
import com.kadirkertis.domain.interactor.qr.repository.QRCodeService;
import com.kadirkertis.domain.interactor.registeration.IsUserRegisteredUseCase;
import com.kadirkertis.domain.interactor.registeration.RegisterNewUserUseCase;
import com.kadirkertis.domain.interactor.registeration.repository.UserRegisterationRepository;
import com.kadirkertis.domain.interactor.session.StartSessionUseCase;
import com.kadirkertis.domain.interactor.session.repository.SessionRepository;
import com.kadirkertis.domain.interactor.tracking.IsUserInUseCase;
import com.kadirkertis.domain.interactor.tracking.repository.UserTrackingService;
import com.kadirkertis.orfo.di.activity.ActivityScope;
import com.kadirkertis.orfo.ui.main.MainActivity;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Kadir Kertis on 12/1/2017.
 */
@Module
public class MainActivityModule {
    //Repositories
    @Provides
    @ActivityScope
    public static QRCodeService provideQrCodeService(MainActivity activity) {
        return new QRCodeServiceImpl(activity);
    }

    @Provides
    @ActivityScope
    public static AuthService provideAuthService(MainActivity activity, FirebaseAuth auth) {
        return new AuthServiceImpl(activity, auth);
    }

    @Provides
    @ActivityScope
    public static RxPermissions provideRxPermissions(MainActivity activity) {
        return new RxPermissions(activity);
    }

    @Provides
    @ActivityScope
    public static UserTrackingService provideUserTrackingService(RxLocation location, RxPermissions rxPermissions) {
        return new UserTrackingServiceImpl(location, rxPermissions);
    }

    @Provides
    @ActivityScope
    public static UserCheckingRepository provideUserCheckingRepository(FirebaseDatabase db) {
        return new UserCheckingRepositoryImpl(db);
    }

    @Provides
    @ActivityScope
    public static PlaceRepository providePlaceRepository(FirebaseDatabase db) {
        return new PlaceRepositoryImpl(db);
    }

    @Provides
    @ActivityScope
    public static UserRegisterationRepository provideUserRegistrationRepository(SessionRepository sessionRepository,
                                                                                FirebaseDatabase db,
                                                                                FirebaseAuth auth) {
        return new UserRegistrationRepositoryImpl(sessionRepository, db, auth);
    }

    //Provide Interactors
    @Provides
    @ActivityScope
    public static IsUserInUseCase provideIsUserInUseCase(UserTrackingService userTrackingService) {
        return new IsUserInUseCase(userTrackingService);
    }

    @Provides
    @ActivityScope
    public static IsUserRegisteredUseCase provideIsUserRegisteredUseCase(UserRegisterationRepository
                                                                                 userRegisterationRepository) {
        return new IsUserRegisteredUseCase(userRegisterationRepository);
    }

    @Provides
    @ActivityScope
    public static RegisterNewUserUseCase provideRegisterNewUserUseCase(UserRegisterationRepository
                                                                               userRegisterationRepository) {
        return new RegisterNewUserUseCase(userRegisterationRepository);
    }

    @Provides
    @ActivityScope
    public static StartSessionUseCase provideStartSessionUseCase(SessionRepository sessionRepository) {
        return new StartSessionUseCase(sessionRepository);
        //Session Repository In Global Scope
    }

    @Provides
    @ActivityScope
    public static GetPlaceUseCase provideGetPlaceUseCase(PlaceRepository placeRepository) {
        return new GetPlaceUseCase(placeRepository);
    }

    @Provides
    @ActivityScope
    public static ObserveAuthStatusUseCase observeAuthStatusUseCase(AuthService authService) {
        return new ObserveAuthStatusUseCase(authService);
    }

    @Provides
    @ActivityScope
    public static CheckInUserUseCase provideCheckInUserUseCase(
            IsUserRegisteredUseCase isUserRegisteredUseCase,
            RegisterNewUserUseCase registerNewUserUseCase,
            StartSessionUseCase startSessionUseCase,
            UserCheckingRepository userCheckingRepository) {
        return new CheckInUserUseCase(isUserRegisteredUseCase,
                registerNewUserUseCase,
                startSessionUseCase,
                userCheckingRepository);
    }


    @Provides
    @ActivityScope
    public static ParseQrCodeUseCase provideParseQrCodeUseCase(QRCodeService qrCodeService,
                                                               IsUserInUseCase isUserInUseCase,
                                                               GetPlaceUseCase getPlaceUseCase) {
        return new ParseQrCodeUseCase(qrCodeService, isUserInUseCase, getPlaceUseCase);
    }

    @Provides
    @ActivityScope
    public static SignInUserUseCase signInUserUseCase(AuthService authService) {
        return new SignInUserUseCase(authService);
    }

    @Provides
    @ActivityScope
    public static Scheduler provideMainThreadSchedular() {
        return AndroidSchedulers.mainThread();
    }

}
