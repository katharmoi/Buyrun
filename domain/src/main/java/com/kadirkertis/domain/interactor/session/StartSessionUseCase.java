package com.kadirkertis.domain.interactor.session;

import com.kadirkertis.domain.interactor.checkIn.model.CheckInRequest;
import com.kadirkertis.domain.interactor.session.repository.SessionRepository;
import com.kadirkertis.domain.interactor.type.CompletableUseCaseWithParameter;

import io.reactivex.Completable;

/**
 * Created by Kadir Kertis on 9/3/2018.
 */

public final class StartSessionUseCase implements CompletableUseCaseWithParameter<CheckInRequest> {

    private final SessionRepository sessionRepository;

    public StartSessionUseCase(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }


    @Override
    public Completable execute(CheckInRequest request) {
        return sessionRepository.startSession(request);
    }
}
