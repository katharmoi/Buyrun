package com.kadirkertis.domain.interactor.type;

import io.reactivex.Completable;

/**
 * Created by Kadir Kertis on 8/17/2018.
 */

public interface CompletableUseCaseWithParameter<P> {
    Completable execute(P params);
}
