package com.kadirkertis.domain.interactor.registeration;

import com.kadirkertis.domain.model.User;
import com.kadirkertis.domain.repository.UserRegisterationRepository;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * Created by Kadir Kertis on 11/27/2017.
 */

public class RegisterNewUserUseCase {

    private UserRegisterationRepository registrationRepository;

    public RegisterNewUserUseCase(UserRegisterationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public Completable execute(){
        return registrationRepository.saveAuthUserToDbUser();
    }
}
