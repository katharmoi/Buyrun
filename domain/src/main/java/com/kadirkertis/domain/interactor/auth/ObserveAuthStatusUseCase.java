package com.kadirkertis.domain.interactor.auth;

import com.kadirkertis.domain.interactor.type.ObservableUseCase;
import com.kadirkertis.domain.interactor.auth.repository.AuthResponse;
import com.kadirkertis.domain.interactor.auth.repository.AuthService;

import io.reactivex.Observable;

/**
 * Created by Kadir Kertis on 9/2/2018.
 */

public final class ObserveAuthStatusUseCase implements ObservableUseCase<AuthResponse> {
    private AuthService authService;

    public ObserveAuthStatusUseCase(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Observable<AuthResponse> execute() {
        return authService.observeAuth();
    }
}
