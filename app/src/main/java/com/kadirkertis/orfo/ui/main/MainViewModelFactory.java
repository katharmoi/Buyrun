package com.kadirkertis.orfo.ui.main;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.kadirkertis.domain.interactor.auth.ObserveAuthStatusUseCase;
import com.kadirkertis.domain.interactor.auth.SignInUserUseCase;
import com.kadirkertis.domain.interactor.checkIn.CheckInUserUseCase;
import com.kadirkertis.domain.interactor.qr.ParseQrCodeUseCase;

import javax.inject.Inject;

import io.reactivex.Scheduler;

/**
 * Created by Kadir Kertis on 11/26/2017.
 */

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private final ParseQrCodeUseCase parseQrCodeUseCase;
    private final ObserveAuthStatusUseCase observeAuthStatusUseCase;
    private final SignInUserUseCase signInUserUseCase;
    private final CheckInUserUseCase checkInUserUseCase;
    private final Scheduler mainThreadScheduler;


    @Inject
    public MainViewModelFactory(ParseQrCodeUseCase parseQrCodeUseCase,
                                ObserveAuthStatusUseCase observeAuthStatusUseCase,
                                SignInUserUseCase signInUserUseCase,
                                CheckInUserUseCase checkInUserUseCase,
                                Scheduler mainThreadScheduler) {
        this.parseQrCodeUseCase = parseQrCodeUseCase;
        this.observeAuthStatusUseCase = observeAuthStatusUseCase;
        this.signInUserUseCase = signInUserUseCase;
        this.checkInUserUseCase = checkInUserUseCase;
        this.mainThreadScheduler = mainThreadScheduler;
    }

    @NonNull
    @Override
    public MainViewModel create(@NonNull Class modelClass) {
        return new MainViewModel(
                parseQrCodeUseCase,
                signInUserUseCase,
                observeAuthStatusUseCase,
                checkInUserUseCase,
                mainThreadScheduler
        );
    }
}
