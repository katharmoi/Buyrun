package com.kadirkertis.domain.interactor.registeration;

import com.kadirkertis.domain.interactor.registeration.repository.UserRegisterationRepository;

import io.reactivex.Maybe;

/**
 * Created by Kadir Kertis on 11/27/2017.
 */

public final class IsUserRegisteredUseCase {

    private final UserRegisterationRepository registerationRepository;

    public IsUserRegisteredUseCase(final UserRegisterationRepository registerationRepository) {
        this.registerationRepository = registerationRepository;
    }

    public Maybe<Boolean> execute() {
        return registerationRepository.isUserRegistered();
    }
}
