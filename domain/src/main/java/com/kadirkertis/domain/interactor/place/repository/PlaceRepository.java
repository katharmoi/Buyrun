package com.kadirkertis.domain.interactor.place.repository;

import com.kadirkertis.domain.interactor.place.model.Place;

import java.util.List;

import io.reactivex.Maybe;

/**
 * Created by Kadir Kertis on 11/29/2017.
 */

public interface PlaceRepository {
    Maybe<Place> getPlace(String placeId);

    Maybe<List<Place>> getAllPlaces();

    Maybe<List<Place>> getAllPlacesByDistance(double lat, double lng, double dist);
}
