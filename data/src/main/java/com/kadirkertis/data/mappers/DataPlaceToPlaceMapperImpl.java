package com.kadirkertis.data.mappers;

import com.annimon.stream.Stream;
import com.kadirkertis.data.model.DataPlace;
import com.kadirkertis.domain.interactor.place.model.Place;

import java.util.List;

/**
 * Created by Kadir Kertis on 12/1/2017.
 */

public class DataPlaceToPlaceMapperImpl implements DataPlaceToPlaceMapper {
    @Override
    public Place map(DataPlace dataPlace) {
        return new Place(dataPlace.getId(), dataPlace.getPlaceName(), dataPlace.getOwnerName(),
                dataPlace.getEmail(), dataPlace.getAddress(), dataPlace.getPhone(), dataPlace.getPlaceType(),
                dataPlace.getImageUrl(), dataPlace.getLatitude(), dataPlace.getLongitude(), dataPlace.getTimeAddedLong(),
                dataPlace.getTimeLastEditedLong(), dataPlace.getRating(), dataPlace.getRateCount());
    }

    @Override
    public List<Place> mapList(List<DataPlace> places) {
        return Stream.of(places)
                .map(this::map)
                .toList();
    }
}
