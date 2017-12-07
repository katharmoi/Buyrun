package com.kadirkertis.data.mappers;

import com.annimon.stream.Stream;
import com.google.firebase.database.ServerValue;
import com.kadirkertis.data.model.DataPlace;
import com.kadirkertis.domain.model.Place;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Kadir Kertis on 12/1/2017.
 */

public class DataPlaceToPlaceMapperImpl implements DataPlaceToPlaceMapper {
    @Override
    public Place map(DataPlace dataPlace){
        return new Place(dataPlace.getId(),dataPlace.getPlaceName(),dataPlace.getOwnerName(),
                dataPlace.getEmail(), dataPlace.getAddress(),dataPlace.getPhone(),dataPlace.getPlaceType(),
                dataPlace.getImageUrl(),dataPlace.getLatitude(),dataPlace.getLongitude(),dataPlace.getTimeAddedLong(),
                dataPlace.getTimeLastEditedLong());
    }

    @Override
    public  List<Place> mapList(List<DataPlace> places) {
        return Stream.of(places)
                .map(this::map)
                .toList();
    }
}
