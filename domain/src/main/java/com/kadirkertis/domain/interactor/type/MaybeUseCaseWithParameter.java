package com.kadirkertis.domain.interactor.type;

import io.reactivex.Maybe;

/**
 * Created by Kadir Kertis on 11/6/2017.
 */

public interface MaybeUseCaseWithParameter<P, R> {

    Maybe<R> execute(P param);
}
