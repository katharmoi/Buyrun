package com.kadirkertis.domain.interactor.checkIn;

import com.kadirkertis.domain.interactor.checkIn.repository.UserCheckingRepository;

import io.reactivex.Completable;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public final class CheckOutUserUseCase {

    private final UserCheckingRepository userCheckingRepository;

    public CheckOutUserUseCase(UserCheckingRepository userCheckingRepository) {
        this.userCheckingRepository = userCheckingRepository;
    }

    public Completable execute(String userId) {
        return userCheckingRepository.checkUserOut(userId);
    }
}
