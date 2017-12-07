package com.kadirkertis.orfo.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentResult;
import com.kadirkertis.data.session.SessionService;
import com.kadirkertis.domain.interactor.checkIn.CheckUserInUseCase;
import com.kadirkertis.domain.interactor.checkIn.CheckUserOutUseCase;
import com.kadirkertis.domain.interactor.qr.ParseQrCodeUseCase;
import com.kadirkertis.domain.model.Item;
import com.kadirkertis.domain.repository.UserRegisterationRepository;
import com.kadirkertis.domain.services.AuthService;
import com.kadirkertis.domain.services.QRCodeService;
import com.kadirkertis.domain.services.UserTrackingService;
import com.kadirkertis.orfo.ui.main.errors.NoNetworkError;
import com.kadirkertis.orfo.ui.main.errors.UncategorizedError;
import com.kadirkertis.orfo.ui.main.errors.UserCancelledSignInError;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Kadir Kertis on 11/25/2017.
 */

public class MainViewModel extends ViewModel {

    private SharedPreferences sharedPreferences;

    private AuthService authService;

    private QRCodeService<Object, String[]> qrCodeService;

    private UserTrackingService userTrackingService;

    private CheckUserInUseCase userInUseCase;

    private CheckUserOutUseCase userOutUseCase;

    private ParseQrCodeUseCase parseQrCodeUseCase;

    private SessionService sessionService;

    private UserRegisterationRepository userRegisterationRepository;


    public MainViewModel(SharedPreferences sharedPreferences,
                         AuthService authService,
                         CheckUserInUseCase userInUseCase,
                         CheckUserOutUseCase userOutUseCase,
                         UserTrackingService userTrackingService,
                         QRCodeService qrCodeService,
                         ParseQrCodeUseCase parseQrCodeUseCase,
                         SessionService sessionService,
                         UserRegisterationRepository userRegisterationRepository
    ) {
        this.sharedPreferences = sharedPreferences;
        this.authService = authService;
        this.userInUseCase = userInUseCase;
        this.userOutUseCase = userOutUseCase;
        this.userTrackingService = userTrackingService;
        this.qrCodeService = qrCodeService;
        this.parseQrCodeUseCase = parseQrCodeUseCase;
        this.sessionService = sessionService;
        this.userRegisterationRepository = userRegisterationRepository;

    }


    public LiveData<FirebaseAuth> getAuthObservable() {
        return LiveDataReactiveStreams.fromPublisher(authService.observeAuth()
                .observeOn(AndroidSchedulers.mainThread()).
                        toFlowable(BackpressureStrategy.LATEST));
    }

    public Completable initiateScan() {
        return qrCodeService.initiateScan();
    }


    public Completable parseSignResult(int resultCode, IdpResponse response) {
        if (resultCode == RESULT_OK) {
            return sessionService.getUser()
                    .flatMap(userId -> userRegisterationRepository.isUserRegistered())
                    .flatMapCompletable(isRegistered -> {
                        if (!isRegistered)
                            return userRegisterationRepository.saveAuthUserToDbUser();

                        return Completable.complete();
                    });

        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                return Completable.error(new UserCancelledSignInError("User Cancelled Sign in"));

            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                return Completable.error(new NoNetworkError("No network"));
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                return Completable.error(new UncategorizedError());
            }
            return Completable.error(new UncategorizedError());
        }
    }

    public Maybe<List<Item>> parseQrResult(IntentResult result) {
        return parseQrCodeUseCase.execute(result);
    }


}
