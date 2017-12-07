package com.kadirkertis.domain.interactor.registeration;

import com.kadirkertis.domain.model.User;
import com.kadirkertis.domain.repository.UserRegisterationRepository;

import io.reactivex.Maybe;

/**
 * Created by Kadir Kertis on 11/27/2017.
 */

public class CheckUserRegisteredUseCase {

    private final UserRegisterationRepository registerationRepository;

    public CheckUserRegisteredUseCase(final UserRegisterationRepository registerationRepository) {
        this.registerationRepository = registerationRepository;
    }

    public Maybe<Boolean> execute(){
        return registerationRepository.isUserRegistered();
    }
}
