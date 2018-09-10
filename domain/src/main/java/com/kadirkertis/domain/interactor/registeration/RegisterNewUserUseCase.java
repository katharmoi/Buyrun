package com.kadirkertis.domain.interactor.registeration;

import com.kadirkertis.domain.interactor.registeration.repository.UserRegisterationRepository;

import io.reactivex.Completable;

/**
 * Created by Kadir Kertis on 11/27/2017.
 */

public final class RegisterNewUserUseCase {

    private final UserRegisterationRepository registrationRepository;

    public RegisterNewUserUseCase(UserRegisterationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public Completable execute() {
        return registrationRepository.registerUser();
    }
}
