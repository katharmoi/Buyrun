package com.kadirkertis.domain.interactor.session.repository;


import com.kadirkertis.domain.interactor.checkIn.model.CheckInRequest;

import io.reactivex.Completable;

/**
 * Created by Kadir Kertis on 11/27/2017.
 */

public interface SessionRepository {

    Completable startSession(CheckInRequest request);

    Completable endSession();

}
