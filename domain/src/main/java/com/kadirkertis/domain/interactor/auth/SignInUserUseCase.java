package com.kadirkertis.domain.interactor.auth;

import com.kadirkertis.domain.interactor.auth.repository.AuthResponse;
import com.kadirkertis.domain.interactor.auth.repository.AuthService;
import com.kadirkertis.domain.interactor.type.SingleUseCase;

import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 8/17/2018.
 */

public final class SignInUserUseCase implements SingleUseCase<AuthResponse> {
    private final AuthService authService;

    public SignInUserUseCase(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Single<AuthResponse> execute() {
        return authService.signInUser();

    }
}
