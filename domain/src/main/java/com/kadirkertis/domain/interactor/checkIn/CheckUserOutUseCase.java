package com.kadirkertis.domain.interactor.checkIn;

import com.kadirkertis.domain.repository.UserCheckingRepository;

import io.reactivex.Completable;
import io.reactivex.internal.operators.completable.CompletableAmb;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public class CheckUserOutUseCase {

    private UserCheckingRepository userCheckingRepository;

    public CheckUserOutUseCase(UserCheckingRepository userCheckingRepository) {
        this.userCheckingRepository = userCheckingRepository;
    }

    public Completable execute(String userId){
        return userCheckingRepository.checkUserOut(userId);
    }
}
