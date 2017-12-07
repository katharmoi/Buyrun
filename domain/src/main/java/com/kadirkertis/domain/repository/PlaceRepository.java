package com.kadirkertis.domain.repository;

import com.kadirkertis.domain.model.Place;

import io.reactivex.Maybe;

/**
 * Created by Kadir Kertis on 11/29/2017.
 */

public interface PlaceRepository {
    Maybe<Place> getPlace(String placeId);
}
