package com.kadirkertis.domain.interactor.checkIn;

import com.kadirkertis.domain.repository.UserCheckingRepository;

import io.reactivex.Completable;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public class CheckUserInUseCase {
    private UserCheckingRepository userCheckingRepostory;

    public CheckUserInUseCase(UserCheckingRepository userCheckingRepostory) {
        this.userCheckingRepostory = userCheckingRepostory;
    }

    public Completable execute(String placeId, String tableNumber, String userId){
        return userCheckingRepostory.checkUserIn(placeId, tableNumber, userId);
    }
}
