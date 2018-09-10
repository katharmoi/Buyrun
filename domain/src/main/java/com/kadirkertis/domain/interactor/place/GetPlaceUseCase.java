package com.kadirkertis.domain.interactor.place;

import com.kadirkertis.domain.interactor.place.model.Place;
import com.kadirkertis.domain.interactor.place.repository.PlaceRepository;
import com.kadirkertis.domain.interactor.type.SingleUseCaseWithParameter;

import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/29/2017.
 */

public final class GetPlaceUseCase implements SingleUseCaseWithParameter<Place, String> {

    private final PlaceRepository placeRepository;

    public GetPlaceUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public Single<Place> execute(String placeId) {
        return placeRepository.getPlace(placeId).toSingle();
    }
}
