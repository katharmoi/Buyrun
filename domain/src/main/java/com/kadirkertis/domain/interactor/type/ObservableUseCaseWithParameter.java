package com.kadirkertis.domain.interactor.type;

import io.reactivex.Observable;

/**
 * Created by Kadir Kertis on 8/17/2018.
 */

public interface ObservableUseCaseWithParameter<T, P> {
    Observable<T> execute(P param);
}
