package com.kadirkertis.domain.interactor.type;

import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 8/17/2018.
 */

public interface SingleUseCase<T> {
    Single<T> execute();
}
