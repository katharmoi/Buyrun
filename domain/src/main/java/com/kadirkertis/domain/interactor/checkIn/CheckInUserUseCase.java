package com.kadirkertis.domain.interactor.checkIn;

import com.kadirkertis.domain.interactor.checkIn.model.CheckInRequest;
import com.kadirkertis.domain.interactor.checkIn.repository.UserCheckingRepository;
import com.kadirkertis.domain.interactor.registeration.IsUserRegisteredUseCase;
import com.kadirkertis.domain.interactor.registeration.RegisterNewUserUseCase;
import com.kadirkertis.domain.interactor.session.StartSessionUseCase;
import com.kadirkertis.domain.interactor.type.CompletableUseCase;
import com.kadirkertis.domain.interactor.type.CompletableUseCaseWithParameter;
import com.kadirkertis.domain.interactor.type.SingleUseCaseWithParameter;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public final class CheckInUserUseCase implements SingleUseCaseWithParameter<String,CheckInRequest> {
    private final IsUserRegisteredUseCase isUserRegisteredUseCase;
    private final RegisterNewUserUseCase registerNewUserUseCase;
    private final StartSessionUseCase startSessionUseCase;
    private final UserCheckingRepository userCheckingRepostory;

    public CheckInUserUseCase(IsUserRegisteredUseCase isUserRegisteredUseCase,
                              RegisterNewUserUseCase registerNewUserUseCase,
                              StartSessionUseCase startSessionUseCase,
                              UserCheckingRepository userCheckingRepostory) {
        this.isUserRegisteredUseCase = isUserRegisteredUseCase;
        this.registerNewUserUseCase = registerNewUserUseCase;
        this.startSessionUseCase = startSessionUseCase;
        this.userCheckingRepostory = userCheckingRepostory;
    }

    @Override
    public Single<String> execute(CheckInRequest request) {
        return isUserRegisteredUseCase.execute()
                .flatMap(isRegistered -> {
                    if (isRegistered) return Maybe.just(true);
                    else return registerNewUserUseCase.execute()
                            .andThen(Maybe.just(true));

                })
                .flatMapCompletable(userRegistered -> startSessionUseCase.execute(request))
                .andThen(userCheckingRepostory.checkUserIn(request));
    }
}
