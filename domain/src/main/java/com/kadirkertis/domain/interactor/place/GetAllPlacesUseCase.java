package com.kadirkertis.domain.interactor.place;

import com.kadirkertis.domain.interactor.place.model.Place;
import com.kadirkertis.domain.interactor.place.repository.PlaceRepository;
import com.kadirkertis.domain.interactor.type.SingleUseCase;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 9/6/2018.
 */

public final class GetAllPlacesUseCase implements SingleUseCase<List<Place>> {

    private final PlaceRepository placeRepository;

    public GetAllPlacesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public Single<List<Place>> execute() {
        return placeRepository.getAllPlaces()
                .toSingle();
    }
}
