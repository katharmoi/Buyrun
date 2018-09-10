package com.kadirkertis.orfo.ui.main;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.kadirkertis.domain.interactor.auth.ObserveAuthStatusUseCase;
import com.kadirkertis.domain.interactor.auth.SignInUserUseCase;
import com.kadirkertis.domain.interactor.auth.exceptions.UserAuthenticationFailedException;
import com.kadirkertis.domain.interactor.auth.repository.AuthResponse;
import com.kadirkertis.domain.interactor.auth.repository.AuthStatus;
import com.kadirkertis.domain.interactor.checkIn.CheckInUserUseCase;
import com.kadirkertis.domain.interactor.checkIn.exception.CheckInException;
import com.kadirkertis.domain.interactor.checkIn.model.CheckInRequest;
import com.kadirkertis.domain.interactor.qr.ParseQrCodeUseCase;
import com.kadirkertis.orfo.utils.Response;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;


/**
 * Created by Kadir Kertis on 11/25/2017.
 */

public class MainViewModel extends ViewModel {


    private final ParseQrCodeUseCase parseQrCodeUseCase;
    private final ObserveAuthStatusUseCase observeAuthStatusUseCase;
    private final SignInUserUseCase signInUserUseCase;
    private final CheckInUserUseCase checkInUserUseCase;
    private CheckInRequest checkInRequest = new CheckInRequest();

    private final MutableLiveData<Response<AuthResponse>> authResponse = new MutableLiveData<>();
    private final MutableLiveData<Response<CheckInRequest>> qrResponse = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final Scheduler mainThreadSchedular;


    public MainViewModel(final ParseQrCodeUseCase parseQrCodeUseCase,
                         final SignInUserUseCase signInUserUseCase,
                         final ObserveAuthStatusUseCase observeAuthStatusUseCase,
                         final CheckInUserUseCase checkInUserUseCase,
                         final Scheduler mainThreadSchedular) {
        this.parseQrCodeUseCase = parseQrCodeUseCase;
        this.signInUserUseCase = signInUserUseCase;
        this.observeAuthStatusUseCase = observeAuthStatusUseCase;
        this.checkInUserUseCase = checkInUserUseCase;
        this.mainThreadSchedular = mainThreadSchedular;
    }

    MutableLiveData<Response<AuthResponse>> getAuthResponse() {
        return authResponse;
    }

    MutableLiveData<Response<CheckInRequest>> getQrResponse() {
        return qrResponse;
    }


    @Override
    protected void onCleared() {
        disposables.clear();
    }


    void observeAuthStatus() {
        disposables.add(observeAuthStatusUseCase.execute()
                .doOnSubscribe(__ -> authResponse.setValue(Response.loading()))
                .observeOn(mainThreadSchedular)
                .subscribe(response -> {
                            if (response.getStatus() == AuthStatus.AUTHORIZED) {
                                authResponse.setValue(Response.success(response));
                            } else {
                                authResponse.setValue(Response.error(new UserAuthenticationFailedException()));
                            }
                        },
                        error -> authResponse.setValue(Response.error(error))
                )
        );
    }


    void signInUser() {
        disposables.add(signInUserUseCase.execute()
                .doOnSubscribe(__ -> authResponse.setValue(Response.loading()))
                .observeOn(mainThreadSchedular)
                .subscribe(
                        signInResponse -> {
                            if (signInResponse.getStatus() == AuthStatus.AUTHORIZED) {
                                authResponse.setValue(Response.success((signInResponse)));
                                checkInRequest.setUid(signInResponse.getUid());
                            } else {
                                authResponse.setValue(Response.error((new UserAuthenticationFailedException())));
                            }

                        },
                        error -> authResponse.setValue(Response.error(error))
                )
        );
    }

    void loadPlace() {
        disposables.add(parseQrCodeUseCase.execute()
                .doOnSubscribe(__ -> qrResponse.setValue(Response.loading()))
                .flatMap(qrResult -> {
                    checkInRequest.setPlaceId(qrResult.getPlaceId());
                    checkInRequest.setTableNumber(qrResult.getTableNumber());
                    if(checkInRequest!= null && checkInRequest.getUid() != null
                            && checkInRequest.getPlaceId()!= null && checkInRequest.getPlaceId() !=null){
                        return checkInUserUseCase.execute(checkInRequest);
                    }else throw new CheckInException();

                })
                .observeOn(mainThreadSchedular)
                .subscribe(
                        request -> qrResponse.setValue(Response.success(checkInRequest)),
                        error -> qrResponse.setValue(Response.error(error))
                )
        );
    }


}
